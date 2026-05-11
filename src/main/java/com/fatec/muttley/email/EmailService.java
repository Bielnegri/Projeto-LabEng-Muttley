package com.fatec.muttley.email;

import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.participacao.ParticipacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private GmailService gmailService;

    @Autowired
    private ParticipacaoRepository participacaoRepository;

    public void enviarConfirmacaoCadastro(Participacao participacao) {
        String destinatario = participacao.getPessoa().getEmail();
        String nome = participacao.getPessoa().getNome();
        Evento evento = participacao.getEvento();

        String assunto = "Inscrição confirmada — " + evento.getTema();
        String corpo = "Olá, " + nome + "!\n\n"
                + "Sua inscrição no evento \"" + evento.getTema() + "\" foi confirmada com sucesso.\n\n"
                + "Data: " + evento.getData() + "\n"
                + "Horário: " + evento.getHorarioInicio() + " - " + evento.getHorarioFim() + "\n"
                + "Local: " + (evento.getLocal() != null ? evento.getLocal().getNome() : "A definir") + "\n\n"
                + "Número de inscrição: " + participacao.getInscricao() + "\n\n"
                + "Até lá!\n"
                + "Equipe Muttley";

        try {
            gmailService.enviarEmail(destinatario, assunto, corpo);
        } catch (Exception e) {
            System.err.println("Erro ao enviar confirmação para " + destinatario + ": " + e.getMessage());
        }
    }

    public void enviarEventoCancelado(Evento evento) {
        List<Participacao> inscritos = participacaoRepository.findByEventoIdOrderByInscricaoAsc(evento.getId());

        for (Participacao participacao : inscritos) {
            String destinatario = participacao.getPessoa().getEmail();
            String nome = participacao.getPessoa().getNome();

            String assunto = "Evento cancelado — " + evento.getTema();
            String corpo = "Olá, " + nome + "!\n\n"
                    + "Informamos que o evento \"" + evento.getTema() + "\", "
                    + "agendado para " + evento.getData() + ", foi cancelado.\n\n"
                    + "Lamentamos o inconveniente e esperamos contar com sua presença em futuros eventos.\n\n"
                    + "Equipe Muttley";

            try {
                gmailService.enviarEmail(destinatario, assunto, corpo);
            } catch (Exception e) {
                System.err.println("Erro ao enviar cancelamento para " + destinatario + ": " + e.getMessage());
            }
        }
    }

    public void enviarEventoConcluido(Evento evento) {
        List<Participacao> inscritos = participacaoRepository.findByEventoIdOrderByInscricaoAsc(evento.getId());

        for (Participacao participacao : inscritos) {
            String destinatario = participacao.getPessoa().getEmail();
            String nome = participacao.getPessoa().getNome();

            String assunto = "Obrigado pela participação — " + evento.getTema();
            String corpo = "Olá, " + nome + "!\n\n"
                    + "O evento \"" + evento.getTema() + "\" foi concluído com sucesso. "
                    + "Agradecemos sua participação!\n\n"
                    + "Em breve seu certificado estará disponível.\n\n"
                    + "Equipe Muttley";

            try {
                gmailService.enviarEmail(destinatario, assunto, corpo);
            } catch (Exception e) {
                System.err.println("Erro ao enviar conclusão para " + destinatario + ": " + e.getMessage());
            }
        }
    }
}