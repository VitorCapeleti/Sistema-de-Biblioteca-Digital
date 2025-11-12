package br.com.biblioteca.digital.emprestimo;

public class ProfessorMulta implements MultaStrategy {
    private static final double MULTA_POR_DIA = 2.50;

    @Override
    public double calcularMulta(int diasAtraso) {
        return diasAtraso * MULTA_POR_DIA;
    }

    // NOVO MÉTODO IMPLEMENTADO
    @Override
    public double getValorPorDia() {
        return MULTA_POR_DIA;
    }
}