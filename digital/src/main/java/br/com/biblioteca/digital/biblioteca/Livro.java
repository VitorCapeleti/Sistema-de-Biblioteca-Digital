package br.com.biblioteca.digital.biblioteca;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Anotação do Lombok para gerar getters, setters, toString, etc.
@NoArgsConstructor
@AllArgsConstructor
public class Livro {
    private String titulo;
    private String autor;
}