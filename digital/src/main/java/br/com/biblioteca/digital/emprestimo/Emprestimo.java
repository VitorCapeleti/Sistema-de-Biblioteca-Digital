package br.com.biblioteca.digital.emprestimo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import br.com.biblioteca.digital.biblioteca.Livro;
import br.com.biblioteca.digital.biblioteca.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Emprestimo {

    private long id;
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private boolean finalizado = false;

    public Emprestimo(Livro livro, Usuario usuario) {
        this.livro = livro;
        this.usuario = usuario;
        this.dataEmprestimo = LocalDate.now();
        // Por padrão, vamos definir a devolução para 14 dias
        this.dataDevolucaoPrevista = this.dataEmprestimo.plusDays(14);
    }

    public double finalizar(MultaStrategy multaStrategy) {
        if (this.finalizado) {
            System.out.println("Este empréstimo já foi finalizado.");
            return 0;
        }

        this.dataDevolucaoReal = LocalDate.now();
        this.finalizado = true;
        
        long diasAtraso = ChronoUnit.DAYS.between(dataDevolucaoPrevista, dataDevolucaoReal);

        if (diasAtraso > 0) {
            double multa = multaStrategy.calcularMulta((int) diasAtraso);
            System.out.printf("Devolução com %d dias de atraso. Multa aplicada: R$ %.2f\n", diasAtraso, multa);
            return multa;
        } else {
            System.out.println("Livro devolvido no prazo. Nenhuma multa aplicada.");
            return 0;
        }
    }
    // ... (no final da classe Emprestimo.java)

    /**
     * Método SIMULADO para a demo web, onde passamos os dias de atraso
     * manualmente, já que o finalizar() original usa LocalDate.now().
     */
    public double finalizarComAtrasoSimulado(MultaStrategy multaStrategy, int diasAtraso) {
        if (this.finalizado) {
            System.out.println("Este empréstimo já foi finalizado.");
            return 0;
        }
        this.finalizado = true;
        
        if (diasAtraso > 0) {
            double multa = multaStrategy.calcularMulta(diasAtraso);
            System.out.printf("Devolução simulada com %d dias de atraso. Multa aplicada: R$ %.2f\n", diasAtraso, multa);
            return multa;
        } else {
            System.out.println("Livro devolvido (simulado) no prazo. Nenhuma multa aplicada.");
            return 0;
        }
    }
}