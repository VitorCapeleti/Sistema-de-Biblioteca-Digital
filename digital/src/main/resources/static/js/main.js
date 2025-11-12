// Executa quando o HTML da página terminar de carregar
document.addEventListener('DOMContentLoaded', () => {
    
    // --- Lógica 1: Notificação "Toast" ---
    
    // Procura pela div oculta que o Controller (Thymeleaf) pode ter criado
    const flashMessageDiv = document.getElementById('flash-message');
    
    if (flashMessageDiv) {
        // Se a div existe, pegamos a mensagem dela
        const message = flashMessageDiv.innerText;
        
        // Criamos a notificação toast
        const toastContainer = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = 'toast';
        toast.innerText = message;
        
        // Adicionamos o toast na tela
        toastContainer.appendChild(toast);
        
        // Removemos o toast do HTML depois que a animação (CSS) terminar
        setTimeout(() => {
            toast.remove();
        }, 4500); // 4.5 segundos
    }

    // --- Lógica 2: "Accordion" de Empréstimo ---

    // Pega TODOS os botões "Devolver"
    const toggleButtons = document.querySelectorAll('.btn-toggle-devolucao');
    
    toggleButtons.forEach(button => {
        button.addEventListener('click', (event) => {
            event.preventDefault(); // Impede o botão de fazer outra coisa
            
            // Pega o ID do formulário que este botão deve abrir
            // (Ex: data-target="form-emprestimo-1")
            const targetFormId = button.getAttribute('data-target');
            const targetForm = document.getElementById(targetFormId);
            
            if (targetForm) {
                // Mostra ou esconde o formulário
                if (targetForm.style.display === 'block') {
                    targetForm.style.display = 'none';
                    button.innerText = 'Devolver'; // Texto volta ao original
                } else {
                    targetForm.style.display = 'block';
                    button.innerText = 'Cancelar'; // Muda o texto do botão
                }
            }
        });
    });

    // ... (fim da lógica do Accordion)

    // --- Lógica 3: "Accordion" de Notificação ---
    
    // Pega TODOS os itens de notificação clicáveis
    const notificacaoItems = document.querySelectorAll('.notificacao-item');
    
    notificacaoItems.forEach(item => {
        item.addEventListener('click', () => {
            // Pega o ID do detalhe que este item deve abrir
            const targetDetailId = item.getAttribute('data-target');
            const targetDetail = document.getElementById(targetDetailId);
            
            if (targetDetail) {
                // Mostra ou esconde o detalhe
                if (targetDetail.style.display === 'block') {
                    targetDetail.style.display = 'none';
                } else {
                    targetDetail.style.display = 'block';
                }
            }
        });
    });

}); // Fim do 'DOMContentLoaded'