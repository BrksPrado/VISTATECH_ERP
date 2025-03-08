import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EstoqueController {
    private EstoqueModel model;
    private EstoqueView view;

    public EstoqueController(EstoqueModel model, EstoqueView view) {
        this.model = model;
        this.view = view;

        // Configura os listeners dos botões
        this.view.setAdicionarListener(new AdicionarListener());
        this.view.setAtualizarListener(new AtualizarListener());
        this.view.setRemoverListener(new RemoverListener());
        this.view.setLimparListener(new LimparListener());

        // Carrega os produtos do banco de dados ao iniciar
        carregarProdutos();
    }

    private void carregarProdutos() {
        List<Produto> produtos = model.carregarProdutos();
        view.carregarProdutos(produtos);
    }

    class AdicionarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Produto produto = view.getProdutoFormulario();
            model.adicionarProduto(produto);
            view.adicionarProdutoTabela(produto);
            view.limparFormulario();
        }
    }

    class AtualizarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int id = view.getProdutoSelecionadoId();
            if (id != -1) {
                Produto produto = view.getProdutoFormulario();
                produto.setId(id);
                model.atualizarProduto(produto);
                view.atualizarProdutoTabela(produto);
                view.limparFormulario();
            }
        }
    }

    class RemoverListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int id = view.getProdutoSelecionadoId();
            if (id != -1) {
                model.removerProduto(id);
                view.removerProdutoTabela();
                view.limparFormulario();
            }
        }
    }

    class LimparListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.limparFormulario();
        }
    }
}