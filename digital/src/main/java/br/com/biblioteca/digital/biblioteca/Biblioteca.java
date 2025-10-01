package br.com.biblioteca.digital.biblioteca;

import java.util.ArrayList;
import java.util.List;

import br.com.biblioteca.digital.notificacao.Observador;

public class Biblioteca {

    // 1. Instância estática e privada
    private static Biblioteca instance;
    
    private List<Livro> livros = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Observador> observadores = new ArrayList<>();

    // 2. Construtor privado para impedir a criação de instâncias
    private Biblioteca() {
        // Construtor privado
    }

    // 3. Método público estático para obter a instância única
    public static synchronized Biblioteca getInstance() { // Ex.: Biblioteca.getInstance() [cite: 15]
        if (instance == null) {
            instance = new Biblioteca();
        }
        return instance;
    }
    
    // Métodos para gerenciar o sistema
    public void adicionarLivro(Livro livro) {
        this.livros.add(livro);
        System.out.println("Livro adicionado: " + livro.getTitulo());
        notificarObservadores("Novo livro disponível: " + livro.getTitulo());
    }

    public void adicionarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        System.out.println("Usuário cadastrado: " + usuario.getNome() + " [" + usuario.getTipo() + "]");
    }

    public void adicionarObservador(Observador observador) {
        this.observadores.add(observador);
    }

    public void notificarObservadores(String mensagem) {
        for (Observador observador : observadores) {
            observador.atualizar(mensagem);
        }
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}