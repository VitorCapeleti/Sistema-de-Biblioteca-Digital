package br.com.biblioteca.digital.emprestimo;

import br.com.biblioteca.digital.biblioteca.Biblioteca; // IMPORTAR

public class AlunoMulta implements MultaStrategy {
    // A constante FOI REMOVIDA
    
    @Override
    public double calcularMulta(int diasAtraso) {
        // Busca o valor dinâmico do Singleton
        double multaPorDia = Biblioteca.getInstance().getMultaPorDiaAluno();
        return diasAtraso * multaPorDia;
    }

    @Override
    public double getValorPorDia() {
        // Busca o valor dinâmico do Singleton
        return Biblioteca.getInstance().getMultaPorDiaAluno();
    }
}