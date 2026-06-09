package com.fatec.muttley.certificado;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fatec.muttley.participacao.Participacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "certificado",
        uniqueConstraints = @UniqueConstraint(name = "uk_certificado_codigo_validacao", columnNames = "codigo_validacao")
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_certificado")
    private Long id;

    @Column(name = "data_emissao")
    private LocalDate dataEmissao;

    private String assinatura;

    @Column(name = "codigo_validacao", unique = true, length = 36)
    private String codigoValidacao;

    @Column(name = "url_publica", unique = true)
    private String urlPublica;

    @Column(name = "caminho_pdf")
    private String caminhoPdf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_participacao")
    @JsonManagedReference
    private Participacao participacao;

    @Column(name = "caminho_assinatura_visual")
    private String caminhoAssinaturaVisual;

    public Certificado(AtualizacaoCertificado dados, Participacao participacao) {
        this.dataEmissao = dados.dataEmissao();
        this.assinatura = dados.assinatura();
        this.participacao = participacao;
    }

    public Certificado(Long id, LocalDate dataEmissao, String assinatura, Participacao participacao) {
        this.id = id;
        this.dataEmissao = dataEmissao;
        this.assinatura = assinatura;
        this.participacao = participacao;
    }

    public void atualizarInformacoes(AtualizacaoCertificado dados, Participacao participacao) {
        if (dados.dataEmissao() != null) {
            this.dataEmissao = dados.dataEmissao();
        }
        if (dados.assinatura() != null) {
            this.assinatura = dados.assinatura();
        }
        if (participacao != null) {
            this.participacao = participacao;
        }
    }

    @PrePersist
    public void preencherDadosPublicos() {
        if (this.codigoValidacao == null || this.codigoValidacao.isBlank()) {
            this.codigoValidacao = UUID.randomUUID().toString();
        }
        if (this.urlPublica == null || this.urlPublica.isBlank()) {
            this.urlPublica = "/certificados/" + this.codigoValidacao;
        }
        if (this.caminhoPdf == null || this.caminhoPdf.isBlank()) {
            this.caminhoPdf = "/certificados/" + this.codigoValidacao + ".pdf";
        }
    }
}
