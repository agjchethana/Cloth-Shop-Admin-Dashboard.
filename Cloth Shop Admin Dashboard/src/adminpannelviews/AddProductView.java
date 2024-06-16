package adminpannelviews;

import javax.swing.*;
import java.awt.*;

public class AddProductView extends JFrame {
    private JTextField nameField;
    private JTextField priceField;
    private JTextField quantityField;
    private JButton addButton;

    public AddProductView() {
        setTitle("Add Product");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        
        panel.add(new JLabel("Product Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Price:"));
        priceField = new JTextField();
        panel.add(priceField);

        panel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        panel.add(quantityField);

        addButton = new JButton("Add Product");
        panel.add(new JLabel("")); // Placeholder
        panel.add(addButton);

        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        paddedPanel.add(panel, BorderLayout.CENTER);

        add(paddedPanel);
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

    public JButton getAddButton() {
        return addButton;
    }
}
