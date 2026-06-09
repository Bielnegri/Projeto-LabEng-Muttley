package com.fatec.muttley.participacao;

import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.evento.EventoService;
import com.fatec.muttley.evento.enums.StatusEventoEnum;
import com.fatec.muttley.pessoa.Pessoa;
import com.fatec.muttley.pessoa.PessoaService;
import com.fatec.muttley.pessoa.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipacaoService {

    @Autowired
    private ParticipacaoRepository participacaoRepository;

    @Autowired
    private ParticipacaoMapper participacaoMapper;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private EventoService eventoService;

    public Participacao salvarOuAtualizar(AtualizacaoParticipacao dto) {
        Pessoa pessoa = pessoaService.procurarPorId(dto.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com o id: " + dto.pessoaId()));
        Evento evento = eventoService.procurarPorId(dto.eventoId())
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado com o id: " + dto.eventoId()));

        if (dto.id() != null) {
            Participacao existente = participacaoRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada com o id: " + dto.id()));
            participacaoMapper.updateEntityFromDto(dto, existente);
            existente.setPessoa(pessoa);
            existente.setEvento(evento);
            return participacaoRepository.save(existente);
        } else {
            Participacao novoParticipacao = participacaoMapper.toEntityFromAtualizacao(dto);
            novoParticipacao.setPessoa(pessoa);
            novoParticipacao.setEvento(evento);
            return participacaoRepository.save(novoParticipacao);
        }
    }

    public List<Participacao> procurarTodos() {
        return participacaoRepository.findAll(Sort.by("inscricao").ascending());
    }

    public List<Participacao> procurarPorEvento(Long eventoId) {
        return participacaoRepository.findByEventoIdComDadosOrderByInscricaoAsc(eventoId);
    }

    public List<Participacao> procurarPorPessoa(Long pessoaId) {
        return participacaoRepository.findByPessoaIdComDados(pessoaId);
    }

    public Participacao registrarInscricaoPublica(Long eventoId, InscricaoPublicaRequest dados) {
        Evento evento = eventoService.procurarPorId(eventoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento nao encontrado."));

        if (evento.getStatus() != StatusEventoEnum.CRIADO || inscricoesEncerradas(evento)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inscricoes encerradas para este evento.");
        }

        Pessoa pessoa = resolverPessoa(dados);
        if (participacaoRepository.existsByEventoIdAndPessoaId(eventoId, pessoa.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pessoa ja inscrita neste evento.");
        }

        Participacao participacao = new Participacao();
        participacao.setInscricao(participacaoRepository.findMaiorNumeroInscricao() + 1);
        participacao.setTipo("Participante");
        participacao.setPessoa(pessoa);
        participacao.setEvento(evento);
        return participacaoRepository.save(participacao);
    }

    public void apagarPorId(Long id) {
        participacaoRepository.deleteById(id);
    }

    public Optional<Participacao> procurarPorId(Long id) {
        return participacaoRepository.findById(id);
    }

    public Optional<Participacao> procurarPorIdComDados(Long id) {
        return participacaoRepository.findByIdComDados(id);
    }

    @Transactional
    public void confirmarPresenca(Long eventoId, Long pessoaId) {
        if(!participacaoRepository.existsByEventoIdAndPessoaId(eventoId, pessoaId)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Você não está inscrito nesse evento."
            );
        }

        Participacao participacao = participacaoRepository.findByEventoIdAndPessoaId(
                eventoId, pessoaId).orElseThrow(

        );

        if (participacao.isPresente()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Presença já confirmada anteriormente.");
        }

        participacao.setPresente(true);

        participacaoRepository.save(participacao);
    }

    @Transactional
    public void marcarPresente(Long participacaoId) {
        participacaoRepository.findById(participacaoId).ifPresent(p -> {
            p.setPresente(true);
            participacaoRepository.save(p);
        });
    }

    private Pessoa resolverPessoa(InscricaoPublicaRequest dados) {
        String cpf = normalizar(dados.cpf());
        String email = normalizar(dados.email()).toLowerCase();
        String nome = normalizar(dados.nomeCompleto());

        Optional<Pessoa> pessoaPorCpf = pessoaService.procurarPorCpf(cpf);
        Optional<Pessoa> pessoaPorEmail = pessoaService.procurarPorEmail(email);

        if (pessoaPorCpf.isPresent() && pessoaPorEmail.isPresent()
                && !pessoaPorCpf.get().getId().equals(pessoaPorEmail.get().getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF e email pertencem a pessoas diferentes.");
        }

        Pessoa pessoa = pessoaPorCpf.or(() -> pessoaPorEmail).orElseGet(Pessoa::new);
        if (pessoa.getNome() == null || pessoa.getNome().isBlank()) {
            pessoa.setNome(nome);
        }
        if (pessoa.getCpf() == null || pessoa.getCpf().isBlank()) {
            pessoa.setCpf(cpf);
        }
        if (pessoa.getEmail() == null || pessoa.getEmail().isBlank()) {
            pessoa.setEmail(email);
        }
        pessoa.setRole(Role.USER);
        return pessoaService.salvar(pessoa);
    }

    private String normalizar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private boolean inscricoesEncerradas(Evento evento) {
        if (evento.getData() == null || evento.getHorarioInicio() == null || evento.getHorarioInicio().isBlank()) {
            return false;
        }

        try {
            LocalDateTime inicioEvento = LocalDateTime.of(
                    evento.getData(),
                    LocalTime.parse(evento.getHorarioInicio(), DateTimeFormatter.ofPattern("HH:mm"))
            );
            return !inicioEvento.isAfter(LocalDateTime.now());
        } catch (RuntimeException exception) {
            return false;
        }
    }
}
