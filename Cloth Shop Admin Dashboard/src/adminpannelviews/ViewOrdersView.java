package adminpannelviews;

import javax.swing.*;
import adminpannelmodels.Order;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ViewOrdersView extends JFrame {
    private JTable ordersTable;
    private JTextField productNameField;
    private JTextField quantityField;
    private JTextField totalField;
    private JButton deleteButton;
    private JButton updateButton;

    public ViewOrdersView() {
        setTitle("View Orders");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        ordersTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 5));

        JLabel productNameLabel = new JLabel("Product Name:");
        productNameField = new JTextField(15);
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField(5);
        JLabel totalLabel = new JLabel("Total:");
        totalField = new JTextField(10);

        inputPanel.add(productNameLabel);
        inputPanel.add(productNameField);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);
        inputPanel.add(totalLabel);
        inputPanel.add(totalField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        ordersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                int selectedRow = ordersTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Object productName = ordersTable.getValueAt(selectedRow, 1);
                    Object quantity = ordersTable.getValueAt(selectedRow, 2);
                    Object total = ordersTable.getValueAt(selectedRow, 3);

                    productNameField.setText(productName.toString());
                    quantityField.setText(quantity.toString());
                    totalField.setText(total.toString());
                }
            }
        });
    }

    public void setOrdersTableData(List<Order> orders) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Order ID");
        model.addColumn("Product Name");
        model.addColumn("Quantity");
        model.addColumn("Total");

        for (Order order : orders) {
            model.addRow(new Object[] { order.getOrderId(), order.getProductName(), order.getQuantity(), order.getTotal() });
        }

        ordersTable.setModel(model);
    }

    public JTable getOrdersTable() {
        return ordersTable;
    }

    public JTextField getProductNameField() {
        return productNameField;
    }

    public JTextField getQuantityField() {
        return quantityField;
    }

    public JTextField getTotalField() {
        return totalField;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }
}
