package com.fatec.muttley.certificado;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class CertificadoServiceTest {

    @Mock
    private CertificadoRepository certificadoRepository;

    @Mock
    private CertificadoMapper certificadoMapper;

    @InjectMocks
    private CertificadoService certificadoService;

    @Test
    void deveCriarCertificadoQuandoIdForNulo(){
        AtualizacaoCertificado dto = new AtualizacaoCertificado(
               null,
                Date.valueOf(LocalDate.now()),
                "Gabriel"
        );
        Certificado novo = new Certificado();
        novo.setAssinatura("Gabriel");
        Certificado salvo = new Certificado();
        salvo.setId(1L);
        salvo.setAssinatura("Gabriel");

        when(certificadoMapper.toEntityFromAtualizacao(dto)).thenReturn(novo);
        when(certificadoRepository.save(novo)).thenReturn(salvo);

        Certificado resultado = certificadoService.salvarOuAtualizar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getAssinatura()).isEqualTo("Gabriel");
        verify(certificadoMapper).toEntityFromAtualizacao(dto);
        verify(certificadoMapper, never()).updateEntityFromDto(any(), any());
        verify(certificadoRepository).save(novo);
    }

    @Test
    void deveAtualizarCertificadoQuandoIdForInformado() {
        AtualizacaoCertificado dto = new AtualizacaoCertificado(
                10L,
                Date.valueOf(LocalDate.now()),
                "Bruno"
        );
        Certificado existente = new Certificado();
        existente.setId(10L);
        existente.setAssinatura("Bruno");

        when(certificadoRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(certificadoRepository.save(existente)).thenReturn(existente);

        Certificado resultado = certificadoService.salvarOuAtualizar(dto);

        assertThat(resultado.getId()).isEqualTo(10L);
        verify(certificadoRepository).findById(10L);
        verify(certificadoMapper).updateEntityFromDto(dto, existente);
        verify(certificadoRepository).save(existente);
        verify(certificadoMapper, never()).toEntityFromAtualizacao(any());
    }

    @Test
    void deveLancarExcecaoAoAtualizarCertificadoInexistente() {
        AtualizacaoCertificado dto = new AtualizacaoCertificado(
                99L,
                Date.valueOf(LocalDate.now()),
                "Carlos"
        );

        when(certificadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> certificadoService.salvarOuAtualizar(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(certificadoRepository).findById(99L);
        verify(certificadoRepository, never()).save(any());
    }

    @Test
    void deveRetornarListaOrdenadaPorNomeAoProcurarTodos() {
        List<Certificado> certificados = List.of(new Certificado(), new Certificado());
        Sort sortEsperado = Sort.by("assinatura").ascending();

        when(certificadoRepository.findAll(sortEsperado)).thenReturn(certificados);

        List<Certificado> resultado = certificadoService.procurarTodos();

        assertThat(resultado).hasSize(2);
        verify(certificadoRepository).findAll(sortEsperado);
    }

    @Test
    void deveProcurarCertificadoPorId() {
        Certificado certificado = new Certificado();
        certificado.setId(5L);

        when(certificadoRepository.findById(5L)).thenReturn(Optional.of(certificado));

        Optional<Certificado> resultado = certificadoService.procurarPorId(5L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(5L);
        verify(certificadoRepository).findById(5L);
    }

    @Test
    void deveApagarCertificadoPorId() {
        certificadoService.apagarPorId(7L);

        verify(certificadoRepository).deleteById(7L);
    }
}

