📖🌱 Acervo Digital "Conhecimento que floresce em cada página."

# 1. Visão Geral do Projeto

Este projeto é uma aplicação acadêmica em Java e Spring Boot que simula
um sistema de gerenciamento de biblioteca digital. O objetivo principal
não é apenas criar as funcionalidades (como cadastro de livros e
empréstimos), mas demonstrar como os Padrões de Projeto GoF (Gang of
Four) podem ser aplicados para resolver problemas comuns de design,
resultando em um código mais flexível, desacoplado e manutenível.

A aplicação funciona como um painel de controle web (construído com
Thymeleaf) que interage com uma lógica de negócios centralizada, onde
cada padrão de projeto tem um papel fundamental.

# 2. Arquitetura e Tecnologias

**Back-end:** Java 17, Spring Boot\
**Front-end:** Thymeleaf, HTML, CSS, JavaScript (para UI/UX)\
**Build:** Maven\
**Persistência:** Simulação em memória (utilizando o Padrão Singleton).

A arquitetura é baseada no padrão **MVC (Model-View-Controller):**

-   **@Controller:** O BibliotecaController atua como o cérebro,
    recebendo requisições HTTP do usuário.\
-   **Model:** O Padrão Singleton (Biblioteca.java) serve como nosso
    modelo e "banco de dados" em memória.\
-   **View:** Os arquivos .html do Thymeleaf renderizam a interface para
    o usuário.

# 3. Padrões de Projeto em Ação

## 🏛️ Padrão Singleton (A Fonte da Verdade)

**Problema:** Como garantir que toda a aplicação (cada usuário, cada
requisição) acesse exatamente as mesmas listas de livros, usuários e
empréstimos?\
**Solução:** A classe `Biblioteca.java` foi implementada como um
Singleton. Ela possui um construtor privado e um método estático
`getInstance()` que garante que apenas uma instância deste objeto exista
em toda a aplicação (JVM).

``` java
public class Biblioteca {
    private static Biblioteca instance;
    private List<Livro> livros = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();

    private Biblioteca() {}

    public static synchronized Biblioteca getInstance() {
        if (instance == null) {
            instance = new Biblioteca();
        }
        return instance;
    }
}
```

## 🏭 Padrão Factory Method (A Fábrica de Usuários)

**Problema:** O sistema precisa criar diferentes tipos de usuários (ex:
Aluno e Professor).\
**Solução:** Criamos uma `UsuarioFactory`. O Controller apenas informa à
fábrica o tipo de usuário que deseja e o nome. A fábrica lida com a
lógica de qual classe específica deve ser instanciada.

``` java
public class UsuarioFactory {

    public enum TipoUsuario {
        ALUNO,
        PROFESSOR
    }

    public static Usuario criarUsuario(TipoUsuario tipo, String nome) {
        switch (tipo) {
            case ALUNO:
                return new Aluno(nome);
            case PROFESSOR:
                return new Professor(nome);
            default:
                throw new IllegalArgumentException("Tipo de usuário inválido.");
        }
    }
}
```

## 📡 Padrão Observer (O Centro de Notificações)

**Problema:** Quando um evento importante acontece, múltiplos sistemas
podem querer ser avisados (ex: Email, SMS).\
**Solução:** A `Biblioteca` atua como o **Subject**, mantendo uma lista
de observadores. Quando algo acontece, todos os observadores são
notificados.

``` java
public class Biblioteca {
    private List<Observador> observadores = new ArrayList<>();
    
    public void adicionarObservador(Observador observador) {
        this.observadores.add(observador);
    }

    public void notificarObservadores(String mensagem) {
        for (Observador observador : observadores) {
            observador.atualizar(mensagem);
        }
    }
}
```

## ♟️ Padrão Strategy (O Cálculo de Multa Flexível)

**Problema:** O cálculo de multa muda dependendo do tipo de usuário.\
**Solução:** Criamos uma interface `MultaStrategy` e implementações
diferentes para `Aluno` e `Professor`.

``` java
@PostMapping("/emprestimos/finalizar")
public String finalizarEmprestimo(
        @RequestParam("emprestimoId") long emprestimoId,
        @RequestParam("diasAtraso") int diasAtraso,
        RedirectAttributes redirectAttributes) {
    
    Emprestimo emprestimo = biblioteca.buscarEmprestimoPorId(emprestimoId).get();
    Usuario usuario = emprestimo.getUsuario();
    
    MultaStrategy strategy;

    if (usuario.getTipo().equals("Aluno")) {
        strategy = new AlunoMulta();
    } else {
        strategy = new ProfessorMulta();
    }

    double multa = emprestimo.finalizarComAtrasoSimulado(strategy, diasAtraso);
    return "redirect:/emprestimos";
}
```

## 🎁 Padrão Decorator (Evoluindo um Livro)

**Problema:** Adicionar uma nova funcionalidade a um livro sem alterar a
classe original.\
**Solução:** Criamos a classe `LivroDecorator` e `LivroDigital`, que
"embrulham" o livro original e adicionam comportamento.

``` java
public class LivroDigital extends LivroDecorator {
    @Override
    public String getTitulo() {
        return super.getTitulo() + " [Versão Digital]";
    }
}
```