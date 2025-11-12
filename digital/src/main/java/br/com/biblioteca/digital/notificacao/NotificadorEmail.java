package br.com.biblioteca.digital.notificacao;

import br.com.biblioteca.digital.biblioteca.Biblioteca; // Importar

public class NotificadorEmail implements Observador {
    @Override
    public void atualizar(String mensagem) {
        // Cria o objeto de notificação
        NotificacaoEnviada notificacao = new NotificacaoEnviada("E-MAIL", mensagem);
        
        // Usa o Singleton para pegar a instância e adicionar ao log
        Biblioteca.getInstance().adicionarNotificacaoAoLog(notificacao);
        
        // (Ainda podemos manter o log do console, se quisermos)
        System.out.println("[E-MAIL] Notificação registrada no log: " + mensagem);
    }
}