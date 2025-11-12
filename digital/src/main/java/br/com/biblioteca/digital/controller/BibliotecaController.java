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

    @GetMapping("/livros")
    public String paginaLivros(Model model) {
        model.addAttribute("livros", biblioteca.getLivros());
        model.addAttribute("currentPage", "livros");
        return "livros";
    }
    
    @GetMapping("/usuarios")
    public String paginaUsuarios(Model model) {
        model.addAttribute("usuarios", biblioteca.getUsuarios());
        model.addAttribute("currentPage", "usuarios");
        return "usuarios";
    }

    @GetMapping("/emprestimos")
    public String paginaEmprestimos(Model model) {
        model.addAttribute("emprestimos", biblioteca.getEmprestimos());
        model.addAttribute("livros", biblioteca.getLivros());
        model.addAttribute("usuarios", biblioteca.getUsuarios());
        model.addAttribute("currentPage", "emprestimos");
        
        model.addAttribute("multaValorAluno", new AlunoMulta().getValorPorDia());
        model.addAttribute("multaValorProfessor", new ProfessorMulta().getValorPorDia());
        
        return "emprestimos";
    }

    // --- NOVA PÁGINA DE HISTÓRICO ---
    @GetMapping("/historico")
    public String paginaHistorico(Model model) {
        model.addAttribute("historico", biblioteca.getHistoricoEventos());
        model.addAttribute("currentPage", "historico");
        return "historico"; // Carrega o novo 'historico.html'
    }

    
    // --- (POST) Padrão Factory Method ---
    @PostMapping("/usuarios/criar")
    public String criarUsuario(
            @RequestParam("nome") String nome, 
            @RequestParam("tipo") UsuarioFactory.TipoUsuario tipo,
            RedirectAttributes redirectAttributes) {
        
        Usuario novoUsuario = UsuarioFactory.criarUsuario(tipo, nome);
        biblioteca.adicionarUsuario(novoUsuario);
        
        String mensagem = "Usuário '" + nome + "' (Tipo: " + tipo + ") foi criado.";
        redirectAttributes.addFlashAttribute("mensagemSucesso", mensagem);
        biblioteca.adicionarAoHistorico(mensagem); // ADICIONADO
        
        return "redirect:/usuarios";
    }

    // --- (POST) Padrão Observer ---
    @PostMapping("/livros/adicionar")
    public String adicionarLivro(
            @RequestParam("titulo") String titulo,
            @RequestParam("autor") String autor,
            RedirectAttributes redirectAttributes) { 
        
        Livro novoLivro = new Livro(titulo, autor);
        biblioteca.adicionarLivro(novoLivro);
        
        String mensagem = "Livro '" + titulo + "' foi adicionado ao acervo.";
        redirectAttributes.addFlashAttribute("mensagemSucesso", mensagem);
        biblioteca.adicionarAoHistorico(mensagem); // ADICIONADO
        
        return "redirect:/livros";
    }

    // --- (POST) Padrão Decorator ---
    @PostMapping("/livros/decorar")
    public String decorarLivro(
            @RequestParam("livroTitulo") String livroTitulo,
            @RequestParam("linkDownload") String linkDownload,
            RedirectAttributes redirectAttributes) { 
        
        Optional<Livro> livroOpt = biblioteca.buscarLivroPorTitulo(livroTitulo);
        
        if (livroOpt.isPresent()) {
            Livro livroOriginal = livroOpt.get();
            Livro livroDecorado = new LivroDigital(livroOriginal, linkDownload);
            biblioteca.atualizarLivro(livroOriginal, livroDecorado);
            
            String mensagem = "Livro '" + livroOriginal.getTitulo() + "' foi atualizado para [Versão Digital].";
            redirectAttributes.addFlashAttribute("mensagemSucesso", mensagem);
            biblioteca.adicionarAoHistorico(mensagem); // ADICIONADO
        }
        
        return "redirect:/livros";
    }

    // --- (POST) Criar Empréstimo ---
    @PostMapping("/emprestimos/criar")
    public String criarEmprestimo(
            @RequestParam("usuarioNome") String usuarioNome,
            @RequestParam("livroTitulo") String livroTitulo,
            RedirectAttributes redirectAttributes) {

        Optional<Usuario> usuarioOpt = biblioteca.getUsuarios().stream()
            .filter(u -> u.getNome().equals(usuarioNome)).findFirst();
        Optional<Livro> livroOpt = biblioteca.getLivros().stream()
            .filter(l -> l.getTitulo().equals(livroTitulo)).findFirst();

        if (usuarioOpt.isPresent() && livroOpt.isPresent()) {
            biblioteca.criarEmprestimo(usuarioOpt.get(), livroOpt.get());
            
            String mensagem = "Empréstimo de '" + livroTitulo + "' para '" + usuarioNome + "' foi criado.";
            redirectAttributes.addFlashAttribute("mensagemSucesso", mensagem);
            biblioteca.adicionarAoHistorico(mensagem); // ADICIONADO
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

            String mensagem = String.format("Empréstimo de '%s' finalizado. Multa (Estratégia %s): R$ %.2f", 
                emprestimo.getLivro().getTitulo(),
                usuario.getTipo(), 
                multa);
            
            redirectAttributes.addFlashAttribute("mensagemSucesso", mensagem);
            biblioteca.adicionarAoHistorico(mensagem); // ADICIONADO
        }
        return "redirect:/emprestimos";
    }

    // ... (depois do GetMapping /historico)

    // --- NOVA PÁGINA DE NOTIFICAÇÕES ---
    @GetMapping("/notificacoes")
    public String paginaNotificacoes(Model model) {
        model.addAttribute("notificacoes", biblioteca.getLogDeNotificacoes());
        model.addAttribute("currentPage", "notificacoes");
        return "notificacoes"; // Carrega o novo 'notificacoes.html'
    }
}