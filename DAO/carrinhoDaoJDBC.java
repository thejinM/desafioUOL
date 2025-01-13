package DAO;

import src.entities.carrinho;
import src.entities.estoque;
import src.teste.carrinhoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class carrinhoDaoJDBC {
    private Connection conexao;

    public carrinhoDaoJDBC(Connection conexao) {
        this.conexao = conexao;
    }

    public int criarCarrinhoNoBanco(carrinho carrinho) {
        String sql = "INSERT INTO carrinho (valor_total) VALUES (?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, carrinho.getValorTotal());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new carrinhoException("Erro ao criar carrinho no banco: " + e.getMessage());
        }
        return -1;
    }

    public void adicionarProduto(int carrinhoId, estoque produto, int quantidade, double subtotal) {
        String sql = "INSERT INTO produto_carrinho (id_carrinho, id_produto, quantidade, subtotal) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, carrinhoId);
            stmt.setInt(2, produto.getID());
            stmt.setInt(3, quantidade);
            stmt.setDouble(4, subtotal);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new carrinhoException("Erro ao adicionar produto ao carrinho no banco: " + e.getMessage());
        }
    }

    public void atualizarValorTotal(int carrinhoId, double valorTotal) {
        String sql = "UPDATE carrinho SET valor_total = ? WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setDouble(1, valorTotal);
            stmt.setInt(2, carrinhoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new carrinhoException("Erro ao atualizar valor total do carrinho no banco: " + e.getMessage());
        }
    }

    public List<String> listarProdutos(int carrinhoId) {
        List<String> listagem = new ArrayList<>();
        String sql = "SELECT e.nome, pc.quantidade, pc.subtotal FROM produto_carrinho pc JOIN estoque e ON pc.id_produto = e.id WHERE pc.id_carrinho = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, carrinhoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nome = rs.getString("nome");
                    int quantidade = rs.getInt("quantidade");
                    double subtotal = rs.getDouble("subtotal");
                    listagem.add("- " + nome + " | Quantidade: " + quantidade + " | Subtotal: R$" + subtotal);
                }
            }
        } catch (SQLException e) {
            throw new carrinhoException("Erro ao listar produtos do carrinho: " + e.getMessage());
        }
        return listagem;
    }

    // Classe carrinhoDaoJDBC

// Método para remover produto do carrinho no banco
public void removerProduto(int carrinhoId, estoque produto, int quantidade) {
    String sql = "DELETE FROM produto_carrinho WHERE id_carrinho = ? AND id_produto = ? AND quantidade = ?";
    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        stmt.setInt(1, carrinhoId);
        stmt.setInt(2, produto.getID());
        stmt.setInt(3, quantidade);
        stmt.executeUpdate();
    } catch (SQLException e) {
        throw new carrinhoException("Erro ao remover produto do carrinho no banco: " + e.getMessage());
    }
}

// Método para alterar a quantidade de um produto no carrinho no banco
public void alterarQuantidadeProduto(int carrinhoId, estoque produto, int novaQuantidade) {
    String sql = "UPDATE produto_carrinho SET quantidade = ? WHERE id_carrinho = ? AND id_produto = ?";
    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        stmt.setInt(1, novaQuantidade);
        stmt.setInt(2, carrinhoId);
        stmt.setInt(3, produto.getID());
        stmt.executeUpdate();
    } catch (SQLException e) {
        throw new carrinhoException("Erro ao alterar a quantidade do produto no carrinho no banco: " + e.getMessage());
    }
}

    public void finalizarCompra(int carrinhoId) {
        String sql = "UPDATE carrinho SET finalizado = TRUE WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, carrinhoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new carrinhoException("Erro ao finalizar compra no banco: " + e.getMessage());
        }
    }
}