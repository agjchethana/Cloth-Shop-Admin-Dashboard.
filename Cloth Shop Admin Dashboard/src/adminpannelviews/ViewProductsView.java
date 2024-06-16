package adminpannelviews;

import javax.swing.*;
import java.awt.*;

public class ViewProductsView extends JFrame {
    private JTable productTable;
    private JScrollPane scrollPane;
    private JButton updateButton;
    private JButton deleteButton;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField quantityField;

    public ViewProductsView() {
        setTitle("View Products");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Name", "Price", "Quantity"};
        Object[][] data = {};

        productTable = new JTable(data, columnNames);
        scrollPane = new JScrollPane(productTable);

        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        nameField = new JTextField(20);
        priceField = new JTextField(10);
        quantityField = new JTextField(5);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("Name: "));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Price: "));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Quantity: "));
        inputPanel.add(quantityField);

        setLayout(new BorderLayout());

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JTable getProductTable() {
        return productTable;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JTextField getPriceField() {
        return priceField;
    }

    public JTextField getQuantityField() {
        return quantityField;
    }

    public void setProductTableData(Object[][] data) {
        productTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[] {"ID", "Name", "Price", "Quantity"}
        ));
    }
}
