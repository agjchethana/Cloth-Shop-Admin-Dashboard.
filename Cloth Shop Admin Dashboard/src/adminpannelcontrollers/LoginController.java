package adminpannelcontrollers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import adminpannel.DatabaseConnector;
import adminpannelmodels.User;
import adminpannelviews.HomeView;
import adminpannelviews.LoginView;

public class LoginController {
    private LoginView loginView;
    private JButton loginButton; 

    public LoginController(LoginView loginView, JButton loginButton) {
        this.loginView = loginView;
        this.loginButton = loginButton; 

        this.loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });
    }

    private void authenticate() {
        String username = loginView.getUsernameField().getText();
        String password = new String(loginView.getPasswordField().getPassword());

        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT * FROM user WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Login successful
                        HomeView homeView = HomeView.getInstance();
                        homeView.setVisible(true);
                        loginView.dispose();
                    } else {
                        JOptionPane.showMessageDialog(loginView, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(loginView, "Error occurred while logging in!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
