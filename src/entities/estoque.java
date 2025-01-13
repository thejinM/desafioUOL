package src.entities;

import src.teste.estoqueException;

public class estoque 
{
    private int id;
    private String produto;
    private String descricao;
    private String categoria;
    private double valor;
    private int quantidade;

    public estoque(String produto, String descricao, String categoria, double valor, int quantidade) 
    {
        this.produto = produto;
        this.descricao = descricao;
        this.categoria = categoria;
        this.valor = valor;
        this.quantidade = quantidade;
    }

    public estoque(int id, String produto, String descricao, String categoria, double valor, int quantidade) 
    {
        this.id = id;
        this.produto = produto;
        this.descricao = descricao;
        this.categoria = categoria;
        this.valor = valor;
        this.quantidade = quantidade;
    }

    public void reduzirQuantidade(int quantidade) throws estoqueException 
    {
        if (quantidade > this.quantidade) 
        {
            throw new estoqueException("Quantidade solicitada excede o estoque disponível para o produto: " + this.produto);
        }
        this.quantidade -= quantidade;
    }

    public void aumentarQuantidade(int quantidade) 
    {
        this.quantidade += quantidade;
    }

    public int getID() 
    {
        return id;
    }

    public String getProduto() 
    {
        return produto;
    }

    public String getDescricao() 
    {
        return descricao;
    }

    public String getCategoria() 
    {
        return categoria;
    }

    public double getValor() 
    {
        return valor;
    }

    public int getQuantidade() 
    {
        return quantidade;
    }
}