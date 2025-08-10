package app;

import model.Produto;
import service.Estoque;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class SistemaEstoqueGUI extends JFrame {
    private Estoque estoque;

    private JTextField nomeField, qtdField, precoField, qtdOperacaoField;
    private JTable tabelaProdutos;
    private DefaultTableModel tabelaModel;

    public SistemaEstoqueGUI() {
        estoque = new Estoque();
        setTitle("Sistema de Estoque - Mercado");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(painelPrincipal);

        // Formulário de cadastro
        JPanel painelCadastro = new JPanel(new GridBagLayout());
        painelCadastro.setBorder(BorderFactory.createTitledBorder("Cadastrar Produto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos e labels
        gbc.gridx = 0; gbc.gridy = 0;
        painelCadastro.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        nomeField = new JTextField(15);
        painelCadastro.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painelCadastro.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        qtdField = new JTextField(15);
        painelCadastro.add(qtdField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painelCadastro.add(new JLabel("Preço Unitário (R$):"), gbc);
        gbc.gridx = 1;
        precoField = new JTextField(15);
        painelCadastro.add(precoField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton btnAdicionar = new JButton("Adicionar Produto");
        painelCadastro.add(btnAdicionar, gbc);

        btnAdicionar.addActionListener(e -> adicionarProduto());

        painelPrincipal.add(painelCadastro, BorderLayout.WEST);

        // Tabela e botões laterais
        JPanel painelCentro = new JPanel(new BorderLayout(5,5));

        // Tabela
        String[] colunas = {"Nome", "Quantidade", "Preço Unitário (R$)", "Valor Total (R$)"};
        tabelaModel = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaProdutos = new JTable(tabelaModel);
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        painelCentro.add(scrollPane, BorderLayout.CENTER);

        // Painel de operações (repor, vender, excluir)
        JPanel painelOperacoes = new JPanel();
        painelOperacoes.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        painelOperacoes.add(new JLabel("Quantidade:"));
        qtdOperacaoField = new JTextField(5);
        painelOperacoes.add(qtdOperacaoField);

        JButton btnRepor = new JButton("Repor");
        JButton btnVender = new JButton("Vender");
        JButton btnExcluir = new JButton("Excluir Produto");

        painelOperacoes.add(btnRepor);
        painelOperacoes.add(btnVender);
        painelOperacoes.add(btnExcluir);

        painelCentro.add(painelOperacoes, BorderLayout.SOUTH);

        painelPrincipal.add(painelCentro, BorderLayout.CENTER);

        // Eventos botões operação
        btnRepor.addActionListener(e -> operarEstoque(true));
        btnVender.addActionListener(e -> operarEstoque(false));
        btnExcluir.addActionListener(e -> excluirProduto());

        atualizarTabela();

        // Dica visual
        tabelaProdutos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int linha = tabelaProdutos.getSelectedRow();
                if (linha != -1) {
                    nomeField.setText((String) tabelaModel.getValueAt(linha, 0));
                    qtdField.setText(String.valueOf(tabelaModel.getValueAt(linha, 1)));
                    precoField.setText(String.valueOf(tabelaModel.getValueAt(linha, 2)));
                }
            }
        });
    }

    private void adicionarProduto() {
        String nome = nomeField.getText().trim();
        String qtdStr = qtdField.getText().trim();
        String precoStr = precoField.getText().trim();

        if (nome.isEmpty() || qtdStr.isEmpty() || precoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int qtd = Integer.parseInt(qtdStr);
            double preco = Double.parseDouble(precoStr);

            if (qtd < 0 || preco < 0) {
                JOptionPane.showMessageDialog(this, "Quantidade e preço devem ser positivos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verifica se produto já existe
            Produto existente = estoque.buscarProduto(nome);
            if (existente != null) {
                JOptionPane.showMessageDialog(this, "Produto já existe. Use reposição para aumentar estoque.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            estoque.adicionarProduto(new Produto(nome, qtd, preco));
            limparCampos();
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite valores válidos para quantidade e preço.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void operarEstoque(boolean repor) {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String qtdStr = qtdOperacaoField.getText().trim();
        if (qtdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a quantidade.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int qtd = Integer.parseInt(qtdStr);
            if (qtd <= 0) {
                JOptionPane.showMessageDialog(this, "Quantidade deve ser maior que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nome = (String) tabelaModel.getValueAt(linha, 0);
            Produto produto = estoque.buscarProduto(nome);
            if (produto == null) {
                JOptionPane.showMessageDialog(this, "Produto não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (repor) {
                produto.aumentarQuantidade(qtd);
                JOptionPane.showMessageDialog(this, "Estoque reposto com sucesso.");
            } else {
                if (qtd > produto.getQuantidade()) {
                    JOptionPane.showMessageDialog(this, "Quantidade insuficiente no estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                produto.reduzirQuantidade(qtd);
                JOptionPane.showMessageDialog(this, "Produto vendido com sucesso.");
            }

            qtdOperacaoField.setText("");
            atualizarTabela();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirProduto() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nome = (String) tabelaModel.getValueAt(linha, 0);
        Produto produto = estoque.buscarProduto(nome);
        if (produto == null) {
            JOptionPane.showMessageDialog(this, "Produto não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o produto \"" + nome + "\"?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            estoque.removerProduto(produto);
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Produto excluído.");
            limparCampos();
        }
    }

    private void limparCampos() {
        nomeField.setText("");
        qtdField.setText("");
        precoField.setText("");
    }

    private void atualizarTabela() {
        tabelaModel.setRowCount(0);
        for (Produto p : estoque.buscarTodos()) {
            Object[] linha = {
                p.getNome(),
                p.getQuantidade(),
                String.format("%.2f", p.getPrecoUnitario()),
                String.format("%.2f", p.getQuantidade() * p.getPrecoUnitario())
            };
            tabelaModel.addRow(linha);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SistemaEstoqueGUI().setVisible(true);
        });
    }
}
