package model;

public class Produto {
    private String nome;
    private int quantidade;
    private double precoUnitario;

    public Produto(String nome, int quantidade, double precoUnitario) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void aumentarQuantidade(int quantidade) {
        if (quantidade > 0) {
            this.quantidade += quantidade;
            System.out.println(quantidade + " unidades adicionadas ao estoque de " + nome);
        } else {
            System.out.println("Quantidade inválida para aumento!");
        }
    }

    public void reduzirQuantidade(int quantidade) {
        if (quantidade > 0 && quantidade <= this.quantidade) {
            this.quantidade -= quantidade;
            System.out.println(quantidade + " unidades vendidas de " + nome);
        } else {
            System.out.println("Quantidade inválida para venda!");
        }
    }

    @Override
    public String toString() {
        return "Produto: " + nome +
               " | Quantidade: " + quantidade +
               " | Preço Unitário: R$ " + precoUnitario +
               " | Total em Estoque: R$ " + (quantidade * precoUnitario);
    }
}
