package br.com.biblioteca.digital.emprestimo;

public interface MultaStrategy {
    double calcularMulta(int diasAtraso);
    double getValorPorDia(); // NOVO MÉTODO
}