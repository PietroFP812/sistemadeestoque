package app;

import java.util.Scanner;
import model.Produto;
import service.Estoque;
import service.IEstoque;

public class SistemaEstoque {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        IEstoque estoque = new Estoque();
        int opcao;

        do {
            System.out.println("\n===== SISTEMA DE ESTOQUE =====");
            System.out.println("1 - Adicionar produto");
            System.out.println("2 - Listar produtos");
            System.out.println("3 - Vender produto");
            System.out.println("4 - Repor produto");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            opcao = sc.nextInt();
            sc.nextLine(); // Limpa buffer

            switch (opcao) {
                case 1:
                    System.out.print("Nome do produto: ");
                    String nome = sc.nextLine();
                    System.out.print("Quantidade: ");
                    int qtd = sc.nextInt();
                    System.out.print("Preço unitário: ");
                    double preco = sc.nextDouble();
                    estoque.adicionarProduto(new Produto(nome, qtd, preco));
                    break;

                case 2:
                    estoque.listarProdutos();
                    break;

                case 3:
                    System.out.print("Nome do produto a vender: ");
                    String vendaNome = sc.nextLine();
                    Produto prodVenda = estoque.buscarProduto(vendaNome);
                    if (prodVenda != null) {
                        System.out.print("Quantidade a vender: ");
                        int qtdVenda = sc.nextInt();
                        prodVenda.reduzirQuantidade(qtdVenda);
                    } else {
                        System.out.println("Produto não encontrado.");
                    }
                    break;

                case 4:
                    System.out.print("Nome do produto para reposição: ");
                    String repNome = sc.nextLine();
                    Produto prodRep = estoque.buscarProduto(repNome);
                    if (prodRep != null) {
                        System.out.print("Quantidade a repor: ");
                        int qtdRep = sc.nextInt();
                        prodRep.aumentarQuantidade(qtdRep);
                    } else {
                        System.out.println("Produto não encontrado.");
                    }
                    break;

                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        sc.close();
    }
}
