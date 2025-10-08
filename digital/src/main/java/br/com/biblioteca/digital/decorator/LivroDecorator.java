package br.com.biblioteca.digital.decorator;

import br.com.biblioteca.digital.biblioteca.Livro;

// 1. Classe abstrata Decorator que herda do objeto base
public abstract class LivroDecorator extends Livro {

    // 2. Referência para o objeto que será decorado
    protected Livro livroDecorado;

    public LivroDecorator(Livro livroDecorado) {
        super(livroDecorado.getTitulo(), livroDecorado.getAutor());
        this.livroDecorado = livroDecorado;
    }
    
    // Podemos sobrescrever métodos existentes se quisermos alterar seu comportamento
    @Override
    public String getTitulo() {
        return livroDecorado.getTitulo();
    }
}