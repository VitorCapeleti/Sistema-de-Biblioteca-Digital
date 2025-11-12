package br.com.biblioteca.digital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.biblioteca.digital.biblioteca.*;
import br.com.biblioteca.digital.decorator.LivroDigital;
import br.com.biblioteca.digital.emprestimo.*;
import java.util.List;
import java.util.Optional;

@Controller
public class BibliotecaController {

    private Biblioteca biblioteca = Biblioteca.getInstance();

    @GetMapping("/")
    public String paginaInicial(Model model) {
        model.addAttribute("currentPage", "inicio");
        return "index";
    }

    // --- PÁGINA DE LIVROS ---
    @GetMapping("/livros")
    public String paginaLivros(Model model) {
        model.addAttribute("livros", biblioteca.getLivros());
        model.addAttribute("currentPage", "livros");
        return "livros";
    }
    
    // --- PÁGINA DE USUÁRIOS ---
    @GetMapping("/usuarios")
    public String paginaUsuarios(Model model) {
        model.addAttribute("usuarios", biblioteca.getUsuarios());
        model.addAttribute("currentPage", "usuarios");
        return "usuarios";
    }

    // --- PÁGINA DE EMPRÉSTIMOS ---
    @GetMapping("/emprestimos")
    public String paginaEmprestimos(Model model) {
        model.addAttribute("emprestimos", biblioteca.getEmprestimos());
        model.addAttribute("livros", biblioteca.getLivros());
        model.addAttribute("usuarios", biblioteca.getUsuarios());
        model.addAttribute("currentPage", "emprestimos");
        return "emprestimos";
    }

    
    // --- (POST) Padrão Factory Method ---
    @PostMapping("/usuarios/criar")
    public String criarUsuario(
            @RequestParam("nome") String nome, 
            @RequestParam("tipo") UsuarioFactory.TipoUsuario tipo) {
        
        Usuario novoUsuario = UsuarioFactory.criarUsuario(tipo, nome);
        biblioteca.adicionarUsuario(novoUsuario);
        return "redirect:/usuarios";
    }

    // --- (POST) Padrão Observer ---
    @PostMapping("/livros/adicionar")
    public String adicionarLivro(
            @RequestParam("titulo") String titulo,
            @RequestParam("autor") String autor) {
        
        Livro novoLivro = new Livro(titulo, autor);
        biblioteca.adicionarLivro(novoLivro);
        return "redirect:/livros";
    }

    // --- (POST) Padrão Decorator ---
    @PostMapping("/livros/decorar")
    public String decorarLivro(
            @RequestParam("livroTitulo") String livroTitulo,
            @RequestParam("linkDownload") String linkDownload) {
        
        Optional<Livro> livroOpt = biblioteca.buscarLivroPorTitulo(livroTitulo);
        
        if (livroOpt.isPresent()) {
            Livro livroOriginal = livroOpt.get();
            
            // --- CORREÇÃO 1 AQUI ---
            // O construtor esperava (Livro, String), e não (Livro, Livro)
            Livro livroDecorado = new LivroDigital(livroOriginal, linkDownload);
            
            biblioteca.atualizarLivro(livroOriginal, livroDecorado);
        }
        
        return "redirect:/livros";
    }

    // --- (POST) Criar Empréstimo ---
    @PostMapping("/emprestimos/criar")
    public String criarEmprestimo(
            @RequestParam("usuarioNome") String usuarioNome,
            @RequestParam("livroTitulo") String livroTitulo) {

        Optional<Usuario> usuarioOpt = biblioteca.getUsuarios().stream()
            .filter(u -> u.getNome().equals(usuarioNome)).findFirst();
        Optional<Livro> livroOpt = biblioteca.getLivros().stream()
            .filter(l -> l.getTitulo().equals(livroTitulo)).findFirst();

        if (usuarioOpt.isPresent() && livroOpt.isPresent()) {
            
            // --- CORREÇÃO 2 AQUI ---
            // Invertemos os argumentos para (Usuario, Livro)
            biblioteca.criarEmprestimo(usuarioOpt.get(), livroOpt.get());
        }
        
        return "redirect:/emprestimos";
    }

    // --- (POST) Padrão Strategy ---
    @PostMapping("/emprestimos/finalizar")
    public String finalizarEmprestimo(
            @RequestParam("emprestimoId") long emprestimoId,
            @RequestParam("diasAtraso") int diasAtraso,
            RedirectAttributes redirectAttributes) { 
        
        Optional<Emprestimo> emprestimoOpt = biblioteca.buscarEmprestimoPorId(emprestimoId);

        if (emprestimoOpt.isPresent()) {
            Emprestimo emprestimo = emprestimoOpt.get();
            Usuario usuario = emprestimo.getUsuario();
            
            MultaStrategy strategy; 
            if (usuario.getTipo().equals("Aluno")) {
                strategy = new AlunoMulta();
            } else {
                strategy = new ProfessorMulta();
            }

            double multa = emprestimo.finalizarComAtrasoSimulado(strategy, diasAtraso);
            biblioteca.finalizarEmprestimo(emprestimo); 

            String mensagem = String.format("Empréstimo de '%s' (Usuário: %s) finalizado. Multa aplicada (Estratégia: %s): R$ %.2f", 
                emprestimo.getLivro().getTitulo(),
                usuario.getNome(), 
                usuario.getTipo(), 
                multa);
            redirectAttributes.addFlashAttribute("mensagemSucesso", mensagem);
        }
        return "redirect:/emprestimos";
    }
}