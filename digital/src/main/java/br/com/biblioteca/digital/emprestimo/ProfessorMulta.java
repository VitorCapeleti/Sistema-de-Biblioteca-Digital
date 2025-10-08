package br.com.biblioteca.digital.emprestimo;

// (...professor paga Y por dia) [cite: 26]
public class ProfessorMulta implements MultaStrategy {
    private static final double MULTA_POR_DIA = 2.50;

    @Override
    public double calcularMulta(int diasAtraso) {
        return diasAtraso * MULTA_POR_DIA;
    }
}