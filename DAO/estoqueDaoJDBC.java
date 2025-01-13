package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import src.entities.estoque;
import src.teste.estoqueException;


public class estoqueDaoJDBC 
{
    private Connection conexao;

    public estoqueDaoJDBC(Connection conexao)
    {
        this.conexao = conexao;
    }

    public void criarProduto(estoque produto)
    {
        String sql = "INSERT INTO estoque (nome, descricao, categoria, valor, quantidade) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql))
        {
            stmt.setString(1, produto.getProduto());
            stmt.setString(2, produto.getDescricao());
            stmt.setString(3, produto.getCategoria());
            stmt.setDouble(4, produto.getValor());
            stmt.setInt(5, produto.getQuantidade());
            stmt.executeUpdate();
        } 
        catch (SQLException e)
        {
            throw new estoqueException("Erro ao adicionar produto ao estoque: " + e.getMessage());
        }
    }

    public List<estoque> listarProdutos()
    {
        List<estoque> produtos = new ArrayList<>();
        String sql = "SELECT * FROM estoque";
        try (Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                produtos.add(new estoque
                (
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getString("categoria"),
                    rs.getDouble("valor"),
                    rs.getInt("quantidade")
                ));
            }
        } 
        catch (SQLException e)
        {
            throw new estoqueException("Erro ao listar produtos do estoque: " + e.getMessage());
        }
        return produtos;
    }

    public estoque buscarProdutoPorID(int id) throws estoqueException
    {
        String sql = "SELECT * FROM estoque WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql))
        {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    return new estoque(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getString("categoria"),
                        rs.getDouble("valor"),
                        rs.getInt("quantidade")
                    );
                } 
                else
                {
                    throw new estoqueException("Produto com ID " + id + " não encontrado no estoque.");
                }
            }
        } 
        catch (SQLException e)
        {
            throw new estoqueException("Erro ao buscar produto por ID: " + e.getMessage());
        }
    }

    public void atualizarQuantidadeNoBanco(estoque produto)
    {
        String sql = "UPDATE estoque SET quantidade = ? WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql))
        {
            stmt.setInt(1, produto.getQuantidade());
            stmt.setInt(2, produto.getID());
            stmt.executeUpdate();
        } 
        catch (SQLException e)
        {
            throw new estoqueException("Erro ao atualizar quantidade do produto no banco: " + e.getMessage());
        }
    }
    

}