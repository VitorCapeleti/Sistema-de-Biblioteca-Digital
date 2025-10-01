package br.com.biblioteca.digital.biblioteca;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Aluno implements Usuario {
    private String nome;

    @Override
    public String getTipo() {
        return "Aluno";
    }
}