package adminpannelviews;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JFrame {
    private static HomeView instance = null;

    private JMenuBar menuBar;
    private JMenu productMenu;
    private JMenuItem addProductItem;
    private JMenuItem viewProductsItem;

    private JMenu orderMenu;
    private JMenuItem addOrderItem;
    private JMenuItem viewOrdersItem;

    private HomeView() {
        setTitle("Admin Panel - Home");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon("src//assets//bg.jpg");
        Image backgroundImage = icon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        setContentPane(backgroundPanel);
        backgroundPanel.setLayout(new GridBagLayout());

        menuBar = new JMenuBar();

        productMenu = new JMenu("Products");
        addProductItem = new JMenuItem("Add Product");
        viewProductsItem = new JMenuItem("View Products");

        productMenu.add(addProductItem);
        productMenu.add(viewProductsItem);

        orderMenu = new JMenu("Orders");
        addOrderItem = new JMenuItem("Add Order");
        viewOrdersItem = new JMenuItem("View Orders");
        orderMenu.add(addOrderItem);
        orderMenu.add(viewOrdersItem);

        menuBar.add(productMenu);
        menuBar.add(orderMenu);

        setJMenuBar(menuBar);

        JLabel welcomeLabel = new JLabel("Hello Admin!!!", SwingConstants.CENTER);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 300, 20);
        gbc.anchor = GridBagConstraints.NORTH;

        backgroundPanel.add(welcomeLabel, gbc);
    }

    public static HomeView getInstance() {
        if (instance == null) {
            instance = new HomeView();
        }
        return instance;
    }

    public JMenuItem getAddProductItem() {
        return addProductItem;
    }

    public JMenuItem getViewProductsItem() {
        return viewProductsItem;
    }

    public JMenuItem getAddOrderItem() {
        return addOrderItem;
    }

    public JMenuItem getViewOrdersItem() {
        return viewOrdersItem;
    }
}
