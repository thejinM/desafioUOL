package src.entities;

import java.util.ArrayList;
import java.util.List;

import DAO.estoqueDaoJDBC;
import src.teste.carrinhoException;
import src.teste.estoqueException;

public class carrinho 
{
    private int id;
    private String cliente;
    private double valorTotal;
    private List<estoque> produtos; 

    public carrinho(String cliente) {
        this.cliente = cliente;
        this.valorTotal = 0;
        this.produtos = new ArrayList<>();
    }

    public void adicionarProduto(estoque produto, int quantidade, estoqueDaoJDBC estoqueDaoJDBC) throws carrinhoException, estoqueException {
        if (quantidade <= 0) {
            throw new carrinhoException("A quantidade deve ser maior que zero.");
        }

        produto.reduzirQuantidade(quantidade);

        estoqueDaoJDBC.atualizarQuantidadeNoBanco(produto);
        
        double subtotal = produto.getValor() * quantidade;
        this.valorTotal += subtotal;

        this.produtos.add(produto);
    }

    public List<estoque> listarProdutos() {
        return this.produtos; // Retorna a lista de produtos
    }

    // Classe carrinho


// Classe carrinho

public void removerProduto(estoque produto) throws carrinhoException, estoqueException {
    // Procurar o produto no carrinho com base no ID
    boolean produtoEncontrado = false;
    for (estoque produtoNoCarrinho : produtos) {
        if (produtoNoCarrinho.getID() == produto.getID()) {
            produtoEncontrado = true;
            int quantidade = produtoNoCarrinho.getQuantidade();
            
            // Atualizar o estoque (adicionar a quantidade removida)
            produto.aumentarQuantidade(quantidade);

            // Remover o produto do carrinho
            this.produtos.remove(produtoNoCarrinho);
            this.valorTotal -= produtoNoCarrinho.getValor() * quantidade;  // Atualiza o valor total do carrinho
            break;
        }
    }

    if (!produtoEncontrado) {
        throw new carrinhoException("Produto não encontrado no carrinho.");
    }
}


// Classe carrinho

// Classe carrinho

public void alterarQuantidadeProduto(estoque produto, int novaQuantidade) throws carrinhoException {
    if (novaQuantidade <= 0) {
        throw new carrinhoException("A nova quantidade deve ser maior que zero.");
    }

    // Procurar o produto no carrinho com base no ID
    boolean produtoEncontrado = false;
    for (estoque produtoNoCarrinho : produtos) {
        if (produtoNoCarrinho.getID() == produto.getID()) {
            produtoEncontrado = true;
            
            // Quantidade atual no carrinho
            int quantidadeAtual = produtoNoCarrinho.getQuantidade();
            
            // Calcular a diferença de quantidade
            int quantidadeDiferenca = novaQuantidade - quantidadeAtual;
            
            // Atualizar a quantidade no carrinho
            produtoNoCarrinho.aumentarQuantidade(quantidadeDiferenca);
            
            // Atualizar o valor total do carrinho com a diferença de quantidade
            this.valorTotal += produtoNoCarrinho.getValor() * quantidadeDiferenca;
            break;
        }
    }

    if (!produtoEncontrado) {
        throw new carrinhoException("Produto não encontrado no carrinho.");
    }

    System.out.println("Quantidade alterada com sucesso!");
}

    public void finalizarCompra() throws carrinhoException {
        if (this.valorTotal == 0) {
            throw new carrinhoException("O carrinho está vazio. Não é possível finalizar a compra.");
        }
    }

    public int getID() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public double getValorTotal() {
        return valorTotal;
    }
}