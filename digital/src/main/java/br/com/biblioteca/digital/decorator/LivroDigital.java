package br.com.biblioteca.digital.decorator;

import br.com.biblioteca.digital.biblioteca.Livro;

// 3. Decorator concreto que adiciona uma nova funcionalidade
public class LivroDigital extends LivroDecorator {

    private String linkDownload;

    public LivroDigital(Livro livroDecorado, String linkDownload) {
        super(livroDecorado);
        this.linkDownload = linkDownload;
    }

    // Funcionalidade extra adicionada pelo Decorator
    public String getLinkDownload() {
        return this.linkDownload;
    }

    // Podemos também modificar o comportamento de um método existente
    @Override
    public String getTitulo() {
        return super.getTitulo() + " [Versão Digital]";
    }
}