package com.fatec.muttley.email;

import com.fatec.muttley.certificado.Certificado;
import com.fatec.muttley.email.dto.CertificadoEmail;
import com.fatec.muttley.email.dto.CredenciaisEmail;
import com.fatec.muttley.email.dto.EventoEmail;
import com.fatec.muttley.email.dto.InscricaoEmail;
import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.pessoa.Pessoa;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_INSCRICAO = "email.inscricao.confirmada";
    private static final String TOPIC_CREDENCIAIS = "email.credenciais.login";
    private static final String TOPIC_CANCELADO = "email.evento.cancelado";
    private static final String TOPIC_CONCLUIDO = "email.evento.concluido";
    private static final String TOPIC_CERTIFICADO = "email.certificado";

    public void publicarConfirmacaoCadastro(Participacao participacao) {
        Evento evento = participacao.getEvento();
        var dto = new InscricaoEmail(
                participacao.getPessoa().getEmail(),
                participacao.getPessoa().getNome(),
                evento.getTema(),
                evento.getData().toString(),
                evento.getHorarioInicio(),
                evento.getHorarioFim(),
                evento.getLocal() != null ? evento.getLocal().getNome() : "A definir",
                participacao.getInscricao()
        );
        kafkaTemplate.send(TOPIC_INSCRICAO, String.valueOf(participacao.getId()), dto);
        log.info("Email de confirmação enfileirado: participacaoId={}", participacao.getId());
    }

    public void publicarCredenciaisLogin(Participacao participacao){
        Pessoa pessoa = participacao.getPessoa();

        var dto = new CredenciaisEmail(
                pessoa.getEmail(),
                pessoa.getNome(),
                pessoa.getEmail(),
                pessoa.getSenha()
        );
        kafkaTemplate.send(TOPIC_CREDENCIAIS, String.valueOf(participacao.getId()), dto);
        log.info("Email com credenciais de login enfileirado: participacaoId={}", participacao.getId());
    }

    public void publicarEventoCancelado(Evento evento, List<Participacao> inscritos) {
        inscritos.forEach(p -> {
            var dto = new EventoEmail(
                    p.getPessoa().getEmail(),
                    p.getPessoa().getNome(),
                    evento.getTema(),
                    evento.getData().toString()
            );
            kafkaTemplate.send(TOPIC_CANCELADO, String.valueOf(evento.getId()), dto);
        });
        log.info("Emails de cancelamento enfileirados: eventoId={}, total={}", evento.getId(), inscritos.size());
    }

    public void publicarEventoConcluido(Evento evento, List<Participacao> inscritos) {
        inscritos.forEach(p -> {
            var dto = new EventoEmail(
                    p.getPessoa().getEmail(),
                    p.getPessoa().getNome(),
                    evento.getTema(),
                    evento.getData().toString()
            );
            kafkaTemplate.send(TOPIC_CONCLUIDO, String.valueOf(evento.getId()), dto);
        });
        log.info("Emails de conclusão enfileirados: eventoId={}, total={}", evento.getId(), inscritos.size());
    }

    public void publicarCertificados(List<Certificado> certificados, String baseUrl){
        certificados.forEach(c -> {
            var dto = new CertificadoEmail(
                    c.getParticipacao().getPessoa().getEmail(),
                    c.getParticipacao().getPessoa().getNome(),
                    c.getParticipacao().getEvento().getTema(),
                    c.getParticipacao().getEvento().getData().toString(),
                    c.getDataEmissao().toString(),
                    baseUrl,
                    c.getUrlPublica()
            );
            kafkaTemplate.send(TOPIC_CERTIFICADO, dto);
        });
        log.info("Emails com certificados enfileirados: total={}", certificados.size());
    }
}
