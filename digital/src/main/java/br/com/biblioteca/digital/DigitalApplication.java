package br.com.biblioteca.digital;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.biblioteca.digital.biblioteca.Biblioteca;
import br.com.biblioteca.digital.biblioteca.Livro;
import br.com.biblioteca.digital.biblioteca.Usuario;
import br.com.biblioteca.digital.biblioteca.UsuarioFactory;
import br.com.biblioteca.digital.emprestimo.AlunoMulta;
import br.com.biblioteca.digital.emprestimo.MultaStrategy;
import br.com.biblioteca.digital.emprestimo.ProfessorMulta;
import br.com.biblioteca.digital.notificacao.NotificadorEmail;
import br.com.biblioteca.digital.notificacao.NotificadorSMS;

@SpringBootApplication
public class DigitalApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DigitalApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("--- INICIANDO SISTEMA DE BIBLIOTECA DIGITAL ---");

		// 1. Testando Singleton
		System.out.println("\n--- Padrão Singleton ---");
		Biblioteca biblioteca = Biblioteca.getInstance();
		System.out.println("Instância da Biblioteca obtida com sucesso.");

		// 2. Testando Observer
		System.out.println("\n--- Padrão Observer ---");
		biblioteca.adicionarObservador(new NotificadorEmail());
		biblioteca.adicionarObservador(new NotificadorSMS());
		System.out.println("Observadores (E-mail e SMS) registrados.");
		
		// 3. Testando Factory Method
		System.out.println("\n--- Padrão Factory Method ---");
		Usuario aluno = UsuarioFactory.criarUsuario(UsuarioFactory.TipoUsuario.ALUNO, "João Silva");
		Usuario professor = UsuarioFactory.criarUsuario(UsuarioFactory.TipoUsuario.PROFESSOR, "Dr. Carlos Andrade");
		
		biblioteca.adicionarUsuario(aluno);
		biblioteca.adicionarUsuario(professor);
		
		// Ação que dispara notificação para os observadores
		System.out.println("\n--- Adicionando Livros (dispara notificações) ---");
		biblioteca.adicionarLivro(new Livro("Padrões de Projeto", "GoF"));
		biblioteca.adicionarLivro(new Livro("Código Limpo", "Robert C. Martin"));

		// 4. Testando Strategy
		System.out.println("\n--- Padrão Strategy ---");
		MultaStrategy multaParaAluno = new AlunoMulta();
		MultaStrategy multaParaProfessor = new ProfessorMulta();
		
		int diasAtraso = 5;
		System.out.println("Calculando multa para " + diasAtraso + " dias de atraso:");
		System.out.printf("Multa para Aluno: R$ %.2f\n", multaParaAluno.calcularMulta(diasAtraso));
		System.out.printf("Multa para Professor: R$ %.2f\n", multaParaProfessor.calcularMulta(diasAtraso));
		
		System.out.println("\n--- SISTEMA FINALIZADO ---");
	}
}