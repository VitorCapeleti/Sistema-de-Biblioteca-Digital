package br.com.biblioteca.digital.biblioteca;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.biblioteca.digital.emprestimo.Emprestimo; // Importar
import br.com.biblioteca.digital.notificacao.NotificacaoEnviada;
import br.com.biblioteca.digital.notificacao.Observador;

public class Biblioteca {

    private static Biblioteca instance;
    
    private List<Livro> livros = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Observador> observadores = new ArrayList<>();
    private List<Emprestimo> emprestimos = new ArrayList<>(); // NOVO
    private List<String> historicoEventos = new ArrayList<>();
    private List<NotificacaoEnviada> logDeNotificacoes = new ArrayList<>();
    private long proximoIdEmprestimo = 1; // NOVO: Para identificar emprestimos

    private Biblioteca() {
        // Construtor privado

        System.out.println("Registrando observadores padrão (Email e SMS)...");
    this.adicionarObservador(new br.com.biblioteca.digital.notificacao.NotificadorEmail());
    this.adicionarObservador(new br.com.biblioteca.digital.notificacao.NotificadorSMS());
    }

    public static synchronized Biblioteca getInstance() { 
        if (instance == null) {
            instance = new Biblioteca();
        }
        return instance;
    }
    
    // --- Métodos de Livro ---
    public void adicionarLivro(Livro livro) {
        this.livros.add(livro);
        System.out.println("Livro adicionado: " + livro.getTitulo());
        notificarObservadores("Novo livro disponível: " + livro.getTitulo());
    }

    public List<Livro> getLivros() {
        return livros;
    }

    // --- Métodos de Usuário ---
    public void adicionarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        System.out.println("Usuário cadastrado: " + usuario.getNome() + " [" + usuario.getTipo() + "]");
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    // --- Métodos de Observador ---
    public void adicionarObservador(Observador observador) {
        this.observadores.add(observador);
    }

    public void notificarObservadores(String mensagem) {
        for (Observador observador : observadores) {
            observador.atualizar(mensagem);
        }
    }

    // --- MÉTODOS NOVOS PARA EMPRÉSTIMO ---

    public List<Emprestimo> getEmprestimos() {
        return this.emprestimos;
    }

    public void criarEmprestimo(Usuario usuario, Livro livro) {
        Emprestimo emprestimo = new Emprestimo(livro, usuario);
        emprestimo.setId(this.proximoIdEmprestimo++); // Atribuir um ID
        this.emprestimos.add(emprestimo);
        System.out.println("Novo empréstimo: " + livro.getTitulo() + " para " + usuario.getNome());
    }

    public Optional<Emprestimo> buscarEmprestimoPorId(long id) {
        return this.emprestimos.stream()
            .filter(e -> e.getId() == id)
            .findFirst();
    }

    public void finalizarEmprestimo(Emprestimo emprestimo) {
        this.emprestimos.remove(emprestimo);
        System.out.println("Empréstimo finalizado.");
    }

    // ... (depois do método finalizarEmprestimo())

    public Optional<Livro> buscarLivroPorTitulo(String titulo) {
        return this.livros.stream()
            .filter(l -> l.getTitulo().equals(titulo))
            .findFirst();
    }

    public void atualizarLivro(Livro livroOriginal, Livro livroAtualizado) {
        for (int i = 0; i < livros.size(); i++) {
            // Usamos o título original para encontrar, pois o título do decorado pode mudar
            if (livros.get(i).getTitulo().equals(livroOriginal.getTitulo())) {
                livros.set(i, livroAtualizado); // Substitui o objeto na lista
                System.out.println("Livro atualizado (decorado): " + livroAtualizado.getTitulo());
                return;
            }
        }
    }

    // --- MÉTODOS NOVOS PARA HISTÓRICO ---

public List<String> getHistoricoEventos() {
    // Retorna a lista como está (mais novos no topo)
    return this.historicoEventos;
}

public void adicionarAoHistorico(String mensagem) {
    // Adiciona no topo da lista (índice 0) para os mais novos aparecerem primeiro
    this.historicoEventos.add(0, mensagem);
    System.out.println("[HISTÓRICO] " + mensagem); // Mantém o log no console
}

public List<NotificacaoEnviada> getLogDeNotificacoes() {
    return this.logDeNotificacoes;
}

public void adicionarNotificacaoAoLog(NotificacaoEnviada notificacao) {
    // Adiciona no topo (mais recentes primeiro)
    this.logDeNotificacoes.add(0, notificacao);
}
}