package app;

import javax.swing.*;
import java.awt.*;
import model.Produto;
import service.Estoque;
import service.IEstoque;

public class SistemaEstoqueGUI extends JFrame {
    private Estoque estoque; // Alterado para Estoque direto
    private JTextField nomeField, qtdField, precoField;
    private JTextArea displayArea;

    public SistemaEstoqueGUI() {
        estoque = new Estoque();
        setTitle("Sistema de Estoque");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel de entrada
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        inputPanel.add(nomeField);

        inputPanel.add(new JLabel("Quantidade:"));
        qtdField = new JTextField();
        inputPanel.add(qtdField);

        inputPanel.add(new JLabel("Preço Unitário:"));
        precoField = new JTextField();
        inputPanel.add(precoField);

        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Adicionar Produto");
        JButton listarBtn = new JButton("Listar Produtos");

        addBtn.addActionListener(e -> adicionarProduto());
        listarBtn.addActionListener(e -> listarProdutos());

        buttonPanel.add(addBtn);
        buttonPanel.add(listarBtn);

        // Área de exibição
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Layout principal
        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void adicionarProduto() {
        try {
            String nome = nomeField.getText();
            int qtd = Integer.parseInt(qtdField.getText());
            double preco = Double.parseDouble(precoField.getText());

            estoque.adicionarProduto(new Produto(nome, qtd, preco));

            nomeField.setText("");
            qtdField.setText("");
            precoField.setText("");

            JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite valores válidos para quantidade e preço.");
        }
    }

    private void listarProdutos() {
        displayArea.setText("");
        for (Produto p : estoque.buscarTodos()) {
            displayArea.append(p.toString() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SistemaEstoqueGUI().setVisible(true));
    }
}
