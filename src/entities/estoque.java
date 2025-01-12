package src.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import src.teste.estoqueException;

public class estoque 
{
  private static Connection conexao;
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

  public static void inicializarConexao(Connection conexao)
  {
      estoque.conexao = conexao;
  }

  public static void criarProduto(estoque produto)
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
          throw new RuntimeException("Erro ao adicionar produto ao estoque: " + e.getMessage());
      }
  }

  public static List<estoque> listarProdutos()
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
          throw new RuntimeException("Erro ao listar produtos do estoque: " + e.getMessage());
      }
      return produtos;
  }

  public static estoque buscarProdutoPorID(int id) throws estoqueException
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
          throw new RuntimeException("Erro ao buscar produto por ID: " + e.getMessage());
      }
  }

  public void reduzirQuantidade(int quantidade) throws estoqueException
  {
      if (quantidade > this.quantidade)
      {
          throw new estoqueException("Quantidade solicitada excede o estoque disponível para o produto: " + this.produto);
      }
      this.quantidade -= quantidade;
      atualizarQuantidadeNoBanco();
  }

  public void aumentarQuantidade(int quantidade)
  {
      this.quantidade += quantidade;
      atualizarQuantidadeNoBanco();
  }

  private void atualizarQuantidadeNoBanco()
  {
      String sql = "UPDATE estoque SET quantidade = ? WHERE id = ?";
      try (PreparedStatement stmt = conexao.prepareStatement(sql))
      {
          stmt.setInt(1, this.quantidade);
          stmt.setInt(2, this.id);
          stmt.executeUpdate();
      } 
      catch (SQLException e)
      {
          throw new RuntimeException("Erro ao atualizar quantidade do produto no banco: " + e.getMessage());
      }
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