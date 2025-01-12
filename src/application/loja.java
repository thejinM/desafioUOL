package src.application;

import java.sql.Connection;
import java.util.Scanner;

import src.db.conexaoBanco;
import src.entities.carrinho;
import src.entities.estoque;
import src.teste.carrinhoException;
import src.teste.estoqueException;

public class loja
{
    public static void main(String[] args)
    {
        Connection conexao = conexaoBanco.getConexao();
        estoque.inicializarConexao(conexao);
        carrinho.inicializarConexao(conexao);

        Scanner scanner = new Scanner(System.in);
        carrinho meuCarrinho = null;

        System.out.println("Bem-vindo à Loja Virtual!");

        try
        {
            System.out.println("Você é cliente ou vendedor? (Digite 'cliente' ou 'vendedor')");
            String tipoUsuario = scanner.nextLine().trim().toLowerCase();

            if (tipoUsuario.equals("cliente"))
            {
                System.out.println("Digite o nome do cliente:");
                String nomeCliente = scanner.nextLine();
                meuCarrinho = new carrinho(nomeCliente);

                int opcao;
                do
                {
                    System.out.println("\nMenu do Cliente:");
                    System.out.println("1. Listar produtos do estoque");
                    System.out.println("2. Adicionar produto ao carrinho");
                    System.out.println("3. Listar produtos do carrinho");
                    System.out.println("4. Finalizar compra");
                    System.out.println("0. Sair");
                    System.out.print("Escolha uma opção: ");
                    opcao = scanner.nextInt();
                    scanner.nextLine();

                    switch (opcao)
                    {
                        case 1 -> 
                        {
                            System.out.println("Produtos disponíveis no estoque:");
                            estoque.listarProdutos().forEach(produto -> 
                            {
                                System.out.printf("ID: %d | Nome: %s | Descrição: %s | Categoria: %s | Valor: R$%.2f | Estoque: %d%n",
                                        produto.getID(), produto.getProduto(), produto.getDescricao(), produto.getCategoria(),
                                        produto.getValor(), produto.getQuantidade());
                            });
                        }
                        case 2 -> 
                        {
                            System.out.print("Digite o ID do produto que deseja adicionar ao carrinho: ");
                            int idProduto = scanner.nextInt();
                            System.out.print("Quantidade desejada: ");
                            int quantidade = scanner.nextInt();
                            scanner.nextLine();

                            try 
                            {
                                estoque produto = estoque.buscarProdutoPorID(idProduto);
                                meuCarrinho.adicionarProduto(produto, quantidade);
                                System.out.println("Produto adicionado ao carrinho com sucesso!");
                            } 
                            catch (estoqueException | carrinhoException e) {
                                System.out.println("Erro: " + e.getMessage());
                            }
                        }
                        case 3 -> 
                        {
                            System.out.println("Produtos no carrinho:");
                            meuCarrinho.listarProdutos().forEach(System.out::println);
                            System.out.printf("Valor total do carrinho: R$%.2f%n", meuCarrinho.getValorTotal());
                        }
                        case 4 -> 
                        {
                            System.out.println("Finalizando compra...");
                            System.out.printf("Valor total: R$%.2f | ID do carrinho: %d%n", meuCarrinho.getValorTotal(), meuCarrinho.getID());

                            meuCarrinho.finalizarCompra();
                            System.out.println("Compra finalizada com sucesso!");
                        }
                        case 0 -> System.out.println("Saindo do sistema...");
                        default -> System.out.println("Opção inválida. Tente novamente.");
                    }
                }
                while (opcao != 0);
            }
            else if (tipoUsuario.equals("vendedor"))
            {
                int opcao;
                do
                {
                    System.out.println("\nMenu do Vendedor:");
                    System.out.println("1. Adicionar produto ao estoque");
                    System.out.println("2. Listar produtos do estoque");
                    System.out.println("0. Sair");
                    System.out.print("Escolha uma opção: ");
                    opcao = scanner.nextInt();
                    scanner.nextLine();

                    switch (opcao)
                    {
                        case 1 -> 
                        {
                            System.out.print("Nome do produto: ");
                            String nomeProduto = scanner.nextLine();
                            System.out.print("Descrição do produto: ");
                            String descricao = scanner.nextLine();
                            System.out.print("Categoria do produto: ");
                            String categoria = scanner.nextLine();
                            System.out.print("Valor do produto: ");
                            double valor = scanner.nextDouble();
                            System.out.print("Quantidade em estoque: ");
                            int quantidade = scanner.nextInt();
                            scanner.nextLine();

                            estoque novoProduto = new estoque(nomeProduto, descricao, categoria, valor, quantidade);
                            estoque.criarProduto(novoProduto);
                            System.out.println("Produto adicionado ao estoque com sucesso!");
                        }
                        case 2 -> 
                        {
                            System.out.println("Produtos disponíveis no estoque:");
                            estoque.listarProdutos().forEach(produto -> 
                            {
                                System.out.printf("ID: %d | Nome: %s | Descrição: %s | Categoria: %s | Valor: R$%.2f | Estoque: %d%n",
                                        produto.getID(), produto.getProduto(), produto.getDescricao(), produto.getCategoria(),
                                        produto.getValor(), produto.getQuantidade());
                            });
                        }
                        case 0 -> System.out.println("Saindo do sistema...");
                        default -> System.out.println("Opção inválida. Tente novamente.");
                    }
                }
                while (opcao != 0);
            }
            else
            {
                System.out.println("Tipo de usuário inválido. Encerrando o sistema.");
            }
        }
        catch (Exception e)
        {
            System.out.println("Erro: " + e.getMessage());
        }
        finally
        {
            conexaoBanco.fecharConexao();
            scanner.close();
        }
    }
}