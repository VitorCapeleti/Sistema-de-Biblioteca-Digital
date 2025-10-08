package br.com.biblioteca.digital.emprestimo;

// Implementar diferentes estratégias de cálculo de multa (ex.: aluno paga X por dia...) [cite: 25]
public class AlunoMulta implements MultaStrategy {
    private static final double MULTA_POR_DIA = 1.50;

    @Override
    public double calcularMulta(int diasAtraso) {
        return diasAtraso * MULTA_POR_DIA;
    }
}