import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.*;

import adminpannel.DatabaseConnector;
import adminpannelcontrollers.LoginController;
import adminpannelcontrollers.OrderController;
import adminpannelcontrollers.ProductController;
import adminpannelmodels.User;
import adminpannelviews.AddOrderView;
import adminpannelviews.AddProductView;
import adminpannelviews.HomeView;
import adminpannelviews.LoginView;
import adminpannelviews.ViewOrdersView;
import adminpannelviews.ViewProductsView;

public class Main {
    public static void main(String[] args) throws SQLException {
        User user = new User("admin", "password");

        LoginView loginView = new LoginView();
        JButton loginButton = loginView.getLoginButton();
        AddProductView addProductView = new AddProductView();
        ViewProductsView viewProductsView = new ViewProductsView();
        AddOrderView addOrderView = new AddOrderView();
        ViewOrdersView viewOrdersView = new ViewOrdersView();

        LoginController loginController = new LoginController(loginView, loginButton); 
        ProductController productController = new ProductController(addProductView, viewProductsView);
        OrderController orderController = new OrderController(addOrderView, viewOrdersView);
        
        DatabaseConnector db = new DatabaseConnector();
        db.getConnection();

        loginView.setVisible(true);

        HomeView homeView = HomeView.getInstance();
        homeView.getAddProductItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productController.showAddProductView();
            }
        });
        homeView.getViewProductsItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productController.showViewProductsView();
            }
        });

        homeView.getAddOrderItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderController.showAddOrderView();
            }
        });
        homeView.getViewOrdersItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	orderController.showViewOrdersView();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = loginView.getUsernameField().getText();
                String password = new String(loginView.getPasswordField().getPassword());

                if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                    homeView.setVisible(true);
                    loginView.dispose();
                }
            }
        });
    }
}
