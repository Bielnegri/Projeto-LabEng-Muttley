package com.fatec.muttley.config;

import com.fatec.muttley.aluno.Aluno;
import com.fatec.muttley.aluno.AlunoRepository;
import com.fatec.muttley.certificado.Certificado;
import com.fatec.muttley.certificado.CertificadoRepository;
import com.fatec.muttley.colaborador.Colaborador;
import com.fatec.muttley.colaborador.ColaboradorRepository;
import com.fatec.muttley.disciplina.Disciplina;
import com.fatec.muttley.disciplina.DisciplinaRepository;
import com.fatec.muttley.disciplina.enums.TurnoDisciplinaEnum;
import com.fatec.muttley.endereco.Endereco;
import com.fatec.muttley.endereco.EnderecoRepository;
import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.evento.EventoRepository;
import com.fatec.muttley.evento.enums.ModalidadeEventoEnum;
import com.fatec.muttley.evento.enums.StatusEventoEnum;
import com.fatec.muttley.local.Local;
import com.fatec.muttley.local.LocalRepository;
import com.fatec.muttley.medalha.Medalha;
import com.fatec.muttley.medalha.MedalhaRepository;
import com.fatec.muttley.organizador.Organizador;
import com.fatec.muttley.organizador.OrganizadorRepository;
import com.fatec.muttley.palestrante.Palestrante;
import com.fatec.muttley.palestrante.PalestranteRepository;
import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.participacao.ParticipacaoRepository;
import com.fatec.muttley.patrocinador.Patrocinador;
import com.fatec.muttley.patrocinador.PatrocinadorRepository;
import com.fatec.muttley.pessoa.Pessoa;
import com.fatec.muttley.pessoa.PessoaRepository;
import com.fatec.muttley.pessoa.Role;
import com.fatec.muttley.professor.Professor;
import com.fatec.muttley.professor.ProfessorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class MockDataInitializer {

    private final PasswordEncoder passwordEncoder;

    public MockDataInitializer(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner loadMockData(
            PessoaRepository pessoaRepository,
            AlunoRepository alunoRepository,
            ProfessorRepository professorRepository,
            PalestranteRepository palestranteRepository,
            OrganizadorRepository organizadorRepository,
            ColaboradorRepository colaboradorRepository,
            EnderecoRepository enderecoRepository,
            LocalRepository localRepository,
            PatrocinadorRepository patrocinadorRepository,
            DisciplinaRepository disciplinaRepository,
            EventoRepository eventoRepository,
            ParticipacaoRepository participacaoRepository,
            MedalhaRepository medalhaRepository,
            CertificadoRepository certificadoRepository
    ) {
        return args -> {
            if (pessoaRepository.count() > 0 || eventoRepository.count() > 0) {
                return;
            }

            List<Pessoa> pessoas = List.of(
                    pessoaRepository.save(criarPessoa("Ana Souza", "ana.souza@email.com", "(11) 99999-1001", "111.111.111-11")),
                    pessoaRepository.save(criarPessoa("Bruno Lima", "bruno.lima@email.com", "(11) 99999-1002", "222.222.222-22")),
                    pessoaRepository.save(criarPessoa("Carla Mendes", "carla.mendes@email.com", "(11) 99999-1003", "333.333.333-33")),
                    pessoaRepository.save(criarPessoa("Daniel Rocha", "daniel.rocha@email.com", "(11) 99999-1004", "444.444.444-44")),
                    pessoaRepository.save(criarPessoa("Elaine Nunes", "elaine.nunes@email.com", "(11) 99999-1005", "555.555.555-55")),
                    pessoaRepository.save(criarPessoa("Felipe Duarte", "felipe.duarte@email.com", "(11) 99999-1006", "666.666.666-66")),
                    pessoaRepository.save(criarPessoa("Gabriela Martins", "gabriela.martins@email.com", "(11) 99999-1007", "777.777.777-77")),
                    pessoaRepository.save(criarPessoa("Hugo Batista", "hugo.batista@email.com", "(11) 99999-1008", "888.888.888-88")),
                    pessoaRepository.save(criarPessoa("Isabela Costa", "isabela.costa@email.com", "(11) 99999-1009", "999.999.999-99")),
                    pessoaRepository.save(criarPessoa("Joao Pereira", "joao.pereira@email.com", "(11) 99999-1010", "101.101.101-10")),
                    pessoaRepository.save(criarPessoa("Karina Alves", "karina.alves@email.com", "(11) 99999-1011", "202.202.202-20")),
                    pessoaRepository.save(criarPessoa("Lucas Moraes", "lucas.moraes@email.com", "(11) 99999-1012", "303.303.303-30")),
                    pessoaRepository.save(criarPessoa("Marina Teixeira", "marina.teixeira@email.com", "(11) 99999-1013", "404.404.404-40")),
                    pessoaRepository.save(criarPessoa("Nicolas Ferreira", "nicolas.ferreira@email.com", "(11) 99999-1014", "505.505.505-50")),
                    pessoaRepository.save(criarPessoa("Olivia Ramos", "olivia.ramos@email.com", "(11) 99999-1015", "606.606.606-60")),
                    pessoaRepository.save(criarPessoa("Paulo Henrique", "paulo.henrique@email.com", "(11) 99999-1016", "707.707.707-70")),
                    pessoaRepository.save(criarPessoa("Renata Lopes", "renata.lopes@email.com", "(11) 99999-1017", "808.808.808-80")),
                    pessoaRepository.save(criarPessoa("Sergio Araujo", "sergio.araujo@email.com", "(11) 99999-1018", "909.909.909-90")),
                    pessoaRepository.save(criarPessoa("Talita Gomes", "talita.gomes@email.com", "(11) 99999-1019", "121.121.121-12")),
                    pessoaRepository.save(criarPessoa("Vitor Campos", "vitor.campos@email.com", "(11) 99999-1020", "232.232.232-23"))
            );

            pessoas.get(0).setRole(Role.ADMIN);
            pessoaRepository.save(pessoas.get(0));

            alunoRepository.save(new Aluno(null, "FATEC Zona Leste", "2026001", pessoas.get(0)));
            alunoRepository.save(new Aluno(null, "FATEC Zona Leste", "2026002", pessoas.get(1)));
            alunoRepository.save(new Aluno(null, "FATEC Zona Leste", "2026003", pessoas.get(5)));
            alunoRepository.save(new Aluno(null, "FATEC Zona Leste", "2026004", pessoas.get(6)));
            alunoRepository.save(new Aluno(null, "FATEC Zona Leste", "2026005", pessoas.get(9)));
            alunoRepository.save(new Aluno(null, "FATEC Zona Leste", "2026006", pessoas.get(10)));
            alunoRepository.save(new Aluno(null, "FATEC Zona Leste", "2026007", pessoas.get(13)));
            alunoRepository.save(new Aluno(null, "FATEC Zona Leste", "2026008", pessoas.get(18)));

            Professor profSoftware = professorRepository.save(new Professor(0, "Engenharia de Software", "Mestre", pessoas.get(2)));
            Professor profDados = professorRepository.save(new Professor(0, "Banco de Dados e BI", "Doutora", pessoas.get(7)));
            Professor profRedes = professorRepository.save(new Professor(0, "Redes e Segurança", "Especialista", pessoas.get(11)));
            Professor profProduto = professorRepository.save(new Professor(0, "Gestão de Produto Digital", "Mestre", pessoas.get(16)));

            palestranteRepository.save(new Palestrante(null, "Especialista em arquitetura cloud e DevOps", "TechCorp", "Arquiteta de Soluções", pessoas.get(3)));
            palestranteRepository.save(new Palestrante(null, "Pesquisadora em IA aplicada a negocios", "DataWay", "Cientista de Dados", pessoas.get(8)));
            palestranteRepository.save(new Palestrante(null, "Consultor em cibersegurança corporativa", "SecureLab", "Security Engineer", pessoas.get(12)));
            palestranteRepository.save(new Palestrante(null, "Designer de produto com foco em acessibilidade", "Studio Norte", "Product Designer", pessoas.get(14)));
            palestranteRepository.save(new Palestrante(null, "Especialista em APIs e integracoes financeiras", "FinHub", "Tech Lead", pessoas.get(17)));

            organizadorRepository.save(new Organizador(null, "FATEC Zona Leste", "Coordenadora de Eventos", pessoas.get(4)));
            organizadorRepository.save(new Organizador(null, "Centro Academico", "Diretor de Comunicacao", pessoas.get(15)));
            organizadorRepository.save(new Organizador(null, "Liga de Tecnologia", "Curadora de Conteudo", pessoas.get(19)));

            colaboradorRepository.save(new Colaborador(null, "Suporte de TI", "Noite", "Voluntario", pessoas.get(1)));
            colaboradorRepository.save(new Colaborador(null, "Credenciamento", "Integral", "Bolsista", pessoas.get(6)));
            colaboradorRepository.save(new Colaborador(null, "Midias Sociais", "Tarde", "Voluntario", pessoas.get(10)));
            colaboradorRepository.save(new Colaborador(null, "Apoio de Sala", "Noite", "Voluntario", pessoas.get(13)));

            Endereco endereco1 = enderecoRepository.save(new Endereco(null, "SP", "São Paulo", "Centro", "Rua das Flores", 120, "Auditório A"));
            Endereco endereco2 = enderecoRepository.save(new Endereco(null, "SP", "São Paulo", "Bela Vista", "Av. Paulista", 1500, "Sala 12"));
            Endereco endereco3 = enderecoRepository.save(new Endereco(null, "SP", "São Paulo", "Tatuapé", "Rua Serra de Bragança", 840, "Bloco B"));
            Endereco endereco4 = enderecoRepository.save(new Endereco(null, "SP", "São Paulo", "Vila Prudente", "Rua Ibitirama", 410, "Coworking"));
            Endereco endereco5 = enderecoRepository.save(new Endereco(null, "SP", "São Paulo", "Mooca", "Rua Javari", 55, "Hub de Inovação"));

            Local local1 = localRepository.save(new Local(null, "Auditório Principal", "Espaço para palestras e painéis", 250, endereco1));
            Local local2 = localRepository.save(new Local(null, "Laboratório 3", "Ambiente para workshops práticos", 40, endereco2));
            Local local3 = localRepository.save(new Local(null, "Sala Maker", "Sala para prototipacao e dinamicas", 60, endereco3));
            Local local4 = localRepository.save(new Local(null, "Arena de Pitch", "Espaço aberto para apresentações", 120, endereco4));
            Local local5 = localRepository.save(new Local(null, "Hub de Inovação", "Ambiente para eventos híbridos", 180, endereco5));

            Patrocinador patrocinador1 = patrocinadorRepository.save(new Patrocinador(null, "InovaTech", "12.345.678/0001-90", 15000.0, "contato@inovatech.com", "(11) 4000-1000", "https://inovatech.com"));
            Patrocinador patrocinador2 = patrocinadorRepository.save(new Patrocinador(null, "DevSolutions", "98.765.432/0001-10", 8500.0, "parcerias@devsolutions.com", "(11) 4000-2000", "https://devsolutions.com"));
            Patrocinador patrocinador3 = patrocinadorRepository.save(new Patrocinador(null, "DataWay", "21.456.789/0001-32", 12000.0, "eventos@dataway.com", "(11) 4000-3000", "https://dataway.com"));
            Patrocinador patrocinador4 = patrocinadorRepository.save(new Patrocinador(null, "SecureLab", "31.654.987/0001-45", 9800.0, "contato@securelab.com", "(11) 4000-4000", "https://securelab.com"));
            Patrocinador patrocinador5 = patrocinadorRepository.save(new Patrocinador(null, "CloudBridge", "41.258.369/0001-76", 11000.0, "community@cloudbridge.com", "(11) 4000-5000", "https://cloudbridge.com"));

            Disciplina disciplina1 = disciplinaRepository.save(new Disciplina(null, "Laboratório de Engenharia de Software", "Práticas e projeto aplicado", TurnoDisciplinaEnum.NORTUNO, profSoftware, new ArrayList<>()));
            Disciplina disciplina2 = disciplinaRepository.save(new Disciplina(null, "Arquitetura de Sistemas", "Modelagem e padrões arquiteturais", TurnoDisciplinaEnum.NORTUNO, profSoftware, new ArrayList<>()));
            Disciplina disciplina3 = disciplinaRepository.save(new Disciplina(null, "Banco de Dados", "Modelagem, SQL e performance", TurnoDisciplinaEnum.MATUTINO, profDados, new ArrayList<>()));
            Disciplina disciplina4 = disciplinaRepository.save(new Disciplina(null, "Inteligência Artificial", "Fundamentos e aplicações de IA", TurnoDisciplinaEnum.NORTUNO, profDados, new ArrayList<>()));
            Disciplina disciplina5 = disciplinaRepository.save(new Disciplina(null, "Segurança da Informação", "Boas práticas, riscos e defesa", TurnoDisciplinaEnum.VESPERTINO, profRedes, new ArrayList<>()));
            Disciplina disciplina6 = disciplinaRepository.save(new Disciplina(null, "Gestão de Produtos Digitais", "Descoberta, métricas e roadmap", TurnoDisciplinaEnum.NORTUNO, profProduto, new ArrayList<>()));

            List<Evento> eventos = List.of(
                    eventoRepository.save(criarEvento("Workshop de Testes Automatizados", 3, "19:00", "21:30", ModalidadeEventoEnum.PRESENCIAL, disciplina1, patrocinador1, local2)),
                    eventoRepository.save(criarEvento("Palestra: Boas Práticas em APIs", 5, "19:30", "22:00", ModalidadeEventoEnum.ONLINE, disciplina2, patrocinador2, local1)),
                    eventoRepository.save(criarEvento("Mesa Redonda de Carreira em TI", 7, "20:00", "22:00", ModalidadeEventoEnum.PRESENCIAL, disciplina6, patrocinador1, local1)),
                    eventoRepository.save(criarEvento("Imersão em Banco de Dados", 9, "08:30", "12:00", ModalidadeEventoEnum.PRESENCIAL, disciplina3, patrocinador3, local3)),
                    eventoRepository.save(criarEvento("Introducao a Machine Learning", 11, "19:00", "21:00", ModalidadeEventoEnum.ONLINE, disciplina4, patrocinador3, local5)),
                    eventoRepository.save(criarEvento("Clínica de Currículo para Devs", 13, "18:30", "20:30", ModalidadeEventoEnum.PRESENCIAL, disciplina6, patrocinador2, local4)),
                    eventoRepository.save(criarEvento("Cibersegurança na Prática", 15, "19:00", "22:00", ModalidadeEventoEnum.PRESENCIAL, disciplina5, patrocinador4, local2)),
                    eventoRepository.save(criarEvento("Design Sprint para Apps", 17, "09:00", "13:00", ModalidadeEventoEnum.PRESENCIAL, disciplina6, patrocinador2, local3)),
                    eventoRepository.save(criarEvento("Deploy Contínuo com Docker", 19, "19:00", "21:30", ModalidadeEventoEnum.ONLINE, disciplina2, patrocinador5, local5)),
                    eventoRepository.save(criarEvento("Observabilidade e Logs", 21, "20:00", "22:00", ModalidadeEventoEnum.ONLINE, disciplina2, patrocinador5, local5)),
                    eventoRepository.save(criarEvento("SQL para Análise de Dados", 23, "14:00", "17:00", ModalidadeEventoEnum.PRESENCIAL, disciplina3, patrocinador3, local2)),
                    eventoRepository.save(criarEvento("Arquitetura Hexagonal", 25, "19:30", "21:30", ModalidadeEventoEnum.PRESENCIAL, disciplina1, patrocinador1, local1)),
                    eventoRepository.save(criarEvento("LGPD para Projetos Academicos", 27, "18:00", "20:00", ModalidadeEventoEnum.ONLINE, disciplina5, patrocinador4, local5)),
                    eventoRepository.save(criarEvento("Pitch de Soluções Digitais", 29, "10:00", "12:30", ModalidadeEventoEnum.PRESENCIAL, disciplina6, patrocinador2, local4)),
                    eventoRepository.save(criarEvento("Integração com Serviços em Nuvem", 31, "19:00", "21:30", ModalidadeEventoEnum.ONLINE, disciplina2, patrocinador5, local5)),
                    eventoRepository.save(criarEvento("Hackday de Prototipos", 35, "09:00", "18:00", ModalidadeEventoEnum.PRESENCIAL, disciplina1, patrocinador1, local3))
            );

            List<Participacao> participacoes = new ArrayList<>();
            int inscricao = 1001;
            for (int i = 0; i < eventos.size(); i++) {
                Evento evento = eventos.get(i);
                participacoes.add(participacaoRepository.save(new Participacao(0, inscricao++, "Aluno", evento, pessoas.get(i % 8))));
                participacoes.add(participacaoRepository.save(new Participacao(0, inscricao++, "Aluno", evento, pessoas.get((i + 5) % 8))));
                participacoes.add(participacaoRepository.save(new Participacao(0, inscricao++, "Palestrante", evento, pessoas.get(3 + (i % 5)))));
                if (i % 2 == 0) {
                    participacoes.add(participacaoRepository.save(new Participacao(0, inscricao++, "Organizador", evento, pessoas.get(4))));
                }
                if (i % 3 == 0) {
                    participacoes.add(participacaoRepository.save(new Participacao(0, inscricao++, "Colaborador", evento, pessoas.get(10))));
                }
            }

            medalhaRepository.save(new Medalha(null, "Destaque em Participação", "Reconhecimento por contribuição ativa no evento", participacoes.get(0)));
            medalhaRepository.save(new Medalha(null, "Mentoria Tecnica", "Apoio tecnico e troca de conhecimento", participacoes.get(2)));
            medalhaRepository.save(new Medalha(null, "Melhor Pitch", "Apresentação com maior clareza e impacto", participacoes.get(10)));
            medalhaRepository.save(new Medalha(null, "Resolução de Problemas", "Destaque em atividades práticas", participacoes.get(18)));
            medalhaRepository.save(new Medalha(null, "Colaboração", "Atuação colaborativa com outros participantes", participacoes.get(25)));
            medalhaRepository.save(new Medalha(null, "Inovação", "Proposta criativa aplicada ao desafio", participacoes.get(34)));

            for (int i = 0; i < Math.min(12, participacoes.size()); i++) {
                certificadoRepository.save(new Certificado(null, LocalDate.now().minusDays(i), "Coordenação FATEC", participacoes.get(i)));
            }
        };
    }

    private Pessoa criarPessoa(String nome, String email, String telefone, String cpf) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);
        pessoa.setEmail(email);
        pessoa.setTelefone(telefone);
        pessoa.setCpf(cpf);
        pessoa.setSenha(passwordEncoder.encode("123456"));
        pessoa.setRole(Role.USER);
        return pessoa;
    }

    private Evento criarEvento(String tema, int dias, String inicio, String fim, ModalidadeEventoEnum modalidade,
                               Disciplina disciplina, Patrocinador patrocinador, Local local) {
        return new Evento(0, tema, LocalDate.now().plusDays(dias), inicio, fim, modalidade, StatusEventoEnum.CRIADO,
                disciplina, patrocinador, local, null, "Evento academico sobre " + tema.toLowerCase() + ".", new ArrayList<>());
    }
}
