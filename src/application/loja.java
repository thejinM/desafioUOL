package src.application;

import java.sql.Connection;
import java.util.Scanner;

import DAO.estoqueDaoJDBC;
import src.db.conexaoBanco;
import src.entities.carrinho;
import src.entities.estoque;
import src.teste.carrinhoException;
import src.teste.estoqueException;

public class loja {
    public static void main(String[] args) {
        Connection conexao = null;
        Scanner scanner = new Scanner(System.in);

        try {
            conexao = conexaoBanco.getConexao();
            estoqueDaoJDBC estoqueDaoJDBC = new estoqueDaoJDBC(conexao);

            System.out.println("Bem-vindo à Loja Virtual!");

            System.out.println("Você é cliente ou vendedor? (Digite 'cliente' ou 'vendedor')");
            String tipoUsuario = scanner.nextLine().trim().toLowerCase();

            if (tipoUsuario.equals("cliente")) {
                System.out.println("Digite o nome do cliente:");
                String nomeCliente = scanner.nextLine();
                carrinho meuCarrinho = new carrinho(nomeCliente);

                int opcao;
                do {
                    System.out.println("\nMenu do Cliente:");
                    System.out.println("1. Listar produtos do estoque");
                    System.out.println("2. Adicionar produto ao carrinho");
                    System.out.println("3. Listar produtos do carrinho");
                    System.out.println("4. Alterar quantidade de produto no carrinho");
                    System.out.println("5. Remover produto do carrinho");
                    System.out.println("6. Finalizar compra");
                    System.out.println("0. Sair");
                    System.out.print("Escolha uma opção: ");
                    opcao = scanner.nextInt();
                    scanner.nextLine();

                    switch (opcao) {
                        case 1 -> {
                            System.out.println("Produtos disponíveis no estoque:");
                            estoqueDaoJDBC.listarProdutos().forEach(produto -> {
                                System.out.printf("ID: %d | Nome: %s | Descrição: %s | Categoria: %s | Valor: R$%.2f | Estoque: %d%n",
                                        produto.getID(), produto.getProduto(), produto.getDescricao(), produto.getCategoria(),
                                        produto.getValor(), produto.getQuantidade());
                            });
                        }
                        case 2 -> {
                            System.out.print("Digite o ID do produto que deseja adicionar ao carrinho: ");
                            int idProduto = scanner.nextInt();
                            System.out.print("Quantidade desejada: ");
                            int quantidade = scanner.nextInt();
                            scanner.nextLine();

                            try {
                                estoque produto = estoqueDaoJDBC.buscarProdutoPorID(idProduto);
                                meuCarrinho.adicionarProduto(produto, quantidade, estoqueDaoJDBC);
                                System.out.println("Produto adicionado ao carrinho com sucesso!");
                            } catch (estoqueException | carrinhoException e) {
                                System.out.println("Erro: " + e.getMessage());
                            }
                        }
                        case 3 -> {
                            System.out.println("Produtos no carrinho:");
                            meuCarrinho.listarProdutos().forEach(System.out::println);
                            System.out.printf("Valor total do carrinho: R$%.2f%n", meuCarrinho.getValorTotal());
                        }
                        case 4 -> {
                            System.out.print("Digite o ID do produto que deseja alterar a quantidade: ");
                            int idProduto = scanner.nextInt();
                            System.out.print("Digite a nova quantidade: ");
                            int novaQuantidade = scanner.nextInt();
                            scanner.nextLine();

                            try {
                                estoque produto = estoqueDaoJDBC.buscarProdutoPorID(idProduto);
                                meuCarrinho.alterarQuantidadeProduto(produto, novaQuantidade);
                                System.out.println("Quantidade alterada com sucesso!");
                            } catch (carrinhoException | estoqueException e) {
                                System.out.println("Erro: " + e.getMessage());
                            }
                        }
                        case 5 -> {
                            System.out.print("Digite o ID do produto que deseja remover do carrinho: ");
                            int idProduto = scanner.nextInt();
                            scanner.nextLine();

                            try {
                                estoque produto = estoqueDaoJDBC.buscarProdutoPorID(idProduto);
                                meuCarrinho.removerProduto(produto);
                                System.out.println("Produto removido do carrinho com sucesso!");
                            } catch (carrinhoException | estoqueException e) {
                                System.out.println("Erro: " + e.getMessage());
                            }
                        }
                        case 6 -> {
                            System.out.println("Finalizando compra...");
                            meuCarrinho.finalizarCompra();
                            System.out.printf("Compra finalizada com sucesso! Valor total: R$%.2f%n", meuCarrinho.getValorTotal());
                        }
                        case 0 -> System.out.println("Saindo do sistema...");
                        default -> System.out.println("Opção inválida. Tente novamente.");
                    }
                } while (opcao != 0);
            } else {
                System.out.println("Tipo de usuário inválido. Encerrando o sistema.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao conectar com o banco de dados ou executar operações: " + e.getMessage());
        } finally {
            if (conexao != null) {
                conexaoBanco.fecharConexao();
            }
            scanner.close();
        }
    }
}