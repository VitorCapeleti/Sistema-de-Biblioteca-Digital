package br.com.biblioteca.digital.notificacao;

public class NotificadorEmail implements Observador { // Ex.: NotificadorEmail, NotificadorSMS. [cite: 22]
    @Override
    public void atualizar(String mensagem) {
        System.out.println("[E-MAIL] Notificação: " + mensagem);
    }
}