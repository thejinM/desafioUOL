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

    public carrinho(String cliente) 
    {
        this.cliente = cliente;
        this.valorTotal = 0;
        this.produtos = new ArrayList<>();
    }

    public void adicionarProduto(estoque produto, int quantidade) throws carrinhoException, estoqueException
    {
        if (quantidade <= 0) 
        {
            throw new carrinhoException("A quantidade deve ser maior que zero.");
        }

        if (produto.getQuantidade() < quantidade) 
        {
            throw new estoqueException("Quantidade solicitada excede o estoque disponível para o produto: " + produto.getProduto());
        }

        for (estoque p : this.produtos) 
        {
            if (p.getID() == produto.getID()) 
            {
                p.reduzirQuantidade(quantidade);
                double subtotal = produto.getValor() * quantidade;
                this.valorTotal += subtotal;
                return;
            }
        }

        produto.reduzirQuantidade(quantidade);
        double subtotal = produto.getValor() * quantidade;
        this.valorTotal += subtotal;
        this.produtos.add(produto);
    }

    public void removerProduto(estoque produto, int quantidade) throws carrinhoException, estoqueException
    {
        if (quantidade <= 0) 
        {
            throw new carrinhoException("A quantidade deve ser maior que zero.");
        }

        if (!this.produtos.contains(produto)) 
        {
            throw new carrinhoException("Produto não está no carrinho.");
        }

        if (quantidade > produto.getQuantidade()) 
        {
            throw new carrinhoException("Quantidade a ser removida é maior que a disponível no carrinho.");
        }

        produto.aumentarQuantidade(quantidade);
        this.valorTotal -= produto.getValor() * quantidade;

        if (produto.getQuantidade() == 0) 
        {
            this.produtos.remove(produto);
        }
    }

    public void finalizarCompra(estoqueDaoJDBC estoqueDao) throws carrinhoException
    {
        if (this.valorTotal == 0) 
        {
            throw new carrinhoException("O carrinho está vazio. Não é possível finalizar a compra.");
        }

        for (estoque produto : this.produtos) 
        {
            produto.reduzirQuantidade(produto.getQuantidade());
            estoqueDao.atualizarQuantidadeNoBanco(produto);
        }
    }

    public String listarProdutos() 
    {
        StringBuilder listagem = new StringBuilder();
        for (estoque produto : this.produtos) 
        {
            double valorTotalProduto = produto.getValor() * produto.getQuantidade();
            listagem.append(String.format("ID: %d | Nome: %s | Valor Unitário: R$%.2f | Quantidade: %d | Valor Total: R$%.2f\n",
                    produto.getID(), produto.getProduto(), produto.getValor(), produto.getQuantidade(), valorTotalProduto));
        }
        listagem.append(String.format("Valor total do carrinho: R$%.2f", this.valorTotal));
        return listagem.toString();
    }

    public int getID() 
    {
        return id;
    }

    public String getCliente() 
    {
        return cliente;
    }

    public double getValorTotal() 
    {
        return valorTotal;
    }
}