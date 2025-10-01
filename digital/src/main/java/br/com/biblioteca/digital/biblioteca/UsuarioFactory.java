package br.com.biblioteca.digital.biblioteca;

public class UsuarioFactory {

    public enum TipoUsuario {
        ALUNO,
        PROFESSOR
    }

    public static Usuario criarUsuario(TipoUsuario tipo, String nome) { // Ex.: UsuarioFactory.criarUsuario(tipo) [cite: 18]
        switch (tipo) {
            case ALUNO:
                return new Aluno(nome);
            case PROFESSOR:
                return new Professor(nome);
            default:
                throw new IllegalArgumentException("Tipo de usuário desconhecido.");
        }
    }
}