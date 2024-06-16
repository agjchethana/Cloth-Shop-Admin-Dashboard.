package adminpannelviews;

import javax.swing.*;
import java.awt.*;

public class AddOrderView extends JFrame {
    private JTextField productNameField;
    private JTextField quantityField;
    private JTextField totalField;
    private JButton addButton;

    public AddOrderView() {
        setTitle("Add Order");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        productNameField = new JTextField(15);
        quantityField = new JTextField(5);
        totalField = new JTextField(10);
        addButton = new JButton("Add Order");

        panel.add(new JLabel("Product Name:"));
        panel.add(productNameField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Total:"));
        panel.add(totalField);
        panel.add(addButton);

        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        paddedPanel.add(panel, BorderLayout.CENTER);

        add(paddedPanel);
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

    public JButton getAddButton() {
        return addButton;
    }
}
