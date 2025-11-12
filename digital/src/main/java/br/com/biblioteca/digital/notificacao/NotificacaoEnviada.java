package br.com.biblioteca.digital.notificacao;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class NotificacaoEnviada {

    private String tipo; // "E-MAIL" ou "SMS"
    private String mensagem;
    private LocalDateTime dataHora;

    public NotificacaoEnviada(String tipo, String mensagem) {
        this.tipo = tipo;
        this.mensagem = mensagem;
        this.dataHora = LocalDateTime.now();
    }
}