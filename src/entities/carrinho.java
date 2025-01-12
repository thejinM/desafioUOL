package src.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import src.teste.carrinhoException;
import src.teste.estoqueException;

public class carrinho 
{
  private static Connection conexao;
  private int id;
  private String cliente;
  private double valorTotal;

  public carrinho(String cliente)
  {
      this.cliente = cliente;
      this.valorTotal = 0;
      criarCarrinhoNoBanco();
  }

  private void criarCarrinhoNoBanco()
  {
      String sql = "INSERT INTO carrinho (valor_total) VALUES (?)";
      try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
      {
          stmt.setDouble(1, valorTotal);
          stmt.executeUpdate();

          try (ResultSet rs = stmt.getGeneratedKeys())
          {
              if (rs.next())
              {
                  this.id = rs.getInt(1);
              }
          }
      } 
      catch (SQLException e)
      {
          throw new RuntimeException("Erro ao criar carrinho no banco: " + e.getMessage());
      }
  }

  public static void inicializarConexao(Connection conexao)
  {
      carrinho.conexao = conexao;
  }

  public void adicionarProduto(estoque produto, int quantidade) throws carrinhoException, estoqueException
  {
      if (quantidade <= 0)
      {
          throw new carrinhoException("A quantidade deve ser maior que zero.");
      }

      produto.reduzirQuantidade(quantidade);
      double subtotal = produto.getValor() * quantidade;
      this.valorTotal += subtotal;
      atualizarValorTotalNoBanco();

      String sql = "INSERT INTO produto_carrinho (id_carrinho, id_produto, quantidade, subtotal) VALUES (?, ?, ?, ?)";
      try (PreparedStatement stmt = conexao.prepareStatement(sql))
      {
          stmt.setInt(1, this.id);
          stmt.setInt(2, produto.getID());
          stmt.setInt(3, quantidade);
          stmt.setDouble(4, subtotal);
          stmt.executeUpdate();
      } 
      catch (SQLException e)
      {
          throw new RuntimeException("Erro ao adicionar produto ao carrinho no banco: " + e.getMessage());
      }
  }

  private void atualizarValorTotalNoBanco()
  {
      String sql = "UPDATE carrinho SET valor_total = ? WHERE id = ?";
      try (PreparedStatement stmt = conexao.prepareStatement(sql))
      {
          stmt.setDouble(1, this.valorTotal);
          stmt.setInt(2, this.id);
          stmt.executeUpdate();
      } 
      catch (SQLException e)
      {
          throw new RuntimeException("Erro ao atualizar valor total do carrinho no banco: " + e.getMessage());
      }
  }

  public List<String> listarProdutos()
  {
      List<String> listagem = new ArrayList<>();
      String sql = "SELECT e.nome, pc.quantidade, pc.subtotal FROM produto_carrinho pc JOIN estoque e ON pc.id_produto = e.id WHERE pc.id_carrinho = ?";
      try (PreparedStatement stmt = conexao.prepareStatement(sql))
      {
          stmt.setInt(1, this.id);
          try (ResultSet rs = stmt.executeQuery())
          {
              while (rs.next())
              {
                  String nome = rs.getString("nome");
                  int quantidade = rs.getInt("quantidade");
                  double subtotal = rs.getDouble("subtotal");
                  listagem.add("- " + nome + " | Quantidade: " + quantidade + " | Subtotal: R$" + subtotal);
              }
          }
      } 
      catch (SQLException e)
      {
          throw new RuntimeException("Erro ao listar produtos do carrinho: " + e.getMessage());
      }
      return listagem;
  }

  public void removerProduto(estoque produto, int quantidade) throws carrinhoException, estoqueException
  {
      if (quantidade <= 0)
      {
          throw new carrinhoException("A quantidade deve ser maior que zero.");
      }

      String sqlBusca = "SELECT quantidade, subtotal FROM produto_carrinho WHERE id_carrinho = ? AND id_produto = ?";
      try (PreparedStatement stmtBusca = conexao.prepareStatement(sqlBusca))
      {
          stmtBusca.setInt(1, this.id);
          stmtBusca.setInt(2, produto.getID());
          try (ResultSet rs = stmtBusca.executeQuery())
          {
              if (rs.next())
              {
                  int quantidadeAtual = rs.getInt("quantidade");
                  double subtotalAtual = rs.getDouble("subtotal");

                  if (quantidade > quantidadeAtual)
                  {
                      throw new carrinhoException("Quantidade a ser removida excede a quantidade no carrinho.");
                  }

                  double subtotalRemovido = produto.getValor() * quantidade;
                  this.valorTotal -= subtotalRemovido;
                  atualizarValorTotalNoBanco();

                  int novaQuantidade = quantidadeAtual - quantidade;

                  if (novaQuantidade == 0)
                  {
                      String sqlRemove = "DELETE FROM produto_carrinho WHERE id_carrinho = ? AND id_produto = ?";
                      try (PreparedStatement stmtRemove = conexao.prepareStatement(sqlRemove))
                      {
                          stmtRemove.setInt(1, this.id);
                          stmtRemove.setInt(2, produto.getID());
                          stmtRemove.executeUpdate();
                      }
                  } 
                  else
                  {
                      String sqlAtualiza = "UPDATE produto_carrinho SET quantidade = ?, subtotal = ? WHERE id_carrinho = ? AND id_produto = ?";
                      try (PreparedStatement stmtAtualiza = conexao.prepareStatement(sqlAtualiza))
                      {
                          stmtAtualiza.setInt(1, novaQuantidade);
                          stmtAtualiza.setDouble(2, subtotalAtual - subtotalRemovido);
                          stmtAtualiza.setInt(3, this.id);
                          stmtAtualiza.setInt(4, produto.getID());
                          stmtAtualiza.executeUpdate();
                      }
                  }

                  produto.aumentarQuantidade(quantidade);
              } 
              else
              {
                  throw new carrinhoException("O produto não está no carrinho.");
              }
          }
      } 
      catch (SQLException e)
      {
          throw new RuntimeException("Erro ao remover produto do carrinho: " + e.getMessage());
      }
  }

  public void finalizarCompra() throws carrinhoException
  {
      if (this.valorTotal == 0)
      {
          throw new carrinhoException("O carrinho está vazio. Não é possível finalizar a compra.");
      }

      String sql = "UPDATE carrinho SET finalizado = TRUE WHERE id = ?";
      try (PreparedStatement stmt = conexao.prepareStatement(sql))
      {
          stmt.setInt(1, this.id);
          stmt.executeUpdate();
      } 
      catch (SQLException e)
      {
          throw new RuntimeException("Erro ao finalizar compra no banco: " + e.getMessage());
      }
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