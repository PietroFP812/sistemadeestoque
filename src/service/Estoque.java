package service;

import java.util.ArrayList;
import java.util.List;
import model.Produto;

public class Estoque implements IEstoque {
    private List<Produto> produtos;

    public Estoque() {
        produtos = new ArrayList<>();
    }

    @Override
    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
        System.out.println("Produto " + produto.getNome() + " adicionado ao estoque!");
    }

    @Override
    public void listarProdutos() {
        if (produtos.isEmpty()) {
            System.out.println("⚠ Estoque vazio.");
            return;
        }
        System.out.println("\n--- Lista de Produtos no Estoque ---");
        for (Produto p : produtos) {
            System.out.println(p);
        }
    }

    @Override
    public Produto buscarProduto(String nome) {
        for (Produto p : produtos) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }

    // ✅ Método extra para GUI pegar todos os produtos
    public List<Produto> buscarTodos() {
        return produtos;
    }
}
