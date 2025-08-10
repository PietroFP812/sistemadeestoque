package service;

import model.Produto;

public interface IEstoque {
    void adicionarProduto(Produto produto);
    void listarProdutos();
    Produto buscarProduto(String nome);
}
