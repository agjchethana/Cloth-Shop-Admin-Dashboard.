package adminpannelcontrollers;

import javax.swing.*;
import adminpannel.DatabaseConnector;
import adminpannelmodels.Order;
import adminpannelviews.AddOrderView;
import adminpannelviews.ViewOrdersView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderController {
    private AddOrderView addOrderView;
    private ViewOrdersView viewOrdersView;
    private List<Order> orders;

    public OrderController(AddOrderView addOrderView, ViewOrdersView viewOrdersView) {
        this.addOrderView = addOrderView;
        this.viewOrdersView = viewOrdersView;
        this.orders = new ArrayList<>();

        this.addOrderView.getAddButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOrder();
            }
        });

        updateOrdersTable();

        viewOrdersView.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteOrder();
            }
        });

        viewOrdersView.getUpdateButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateOrder();
            }
        });
    }

    private void addOrder() {
        String productName = addOrderView.getProductNameField().getText();
        String quantityText = addOrderView.getQuantityField().getText();
        String totalText = addOrderView.getTotalField().getText();

        if (productName.isEmpty() || quantityText.isEmpty() || totalText.isEmpty()) {
            JOptionPane.showMessageDialog(addOrderView, "Please fill in all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            double total = Double.parseDouble(totalText);

            if (!isProductAvailable(productName)) {
                JOptionPane.showMessageDialog(addOrderView, "Product not available.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isProductStockSufficient(productName, quantity)) {
                JOptionPane.showMessageDialog(addOrderView, "Insufficient product stock.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Order order = new Order(0, productName, quantity, total);
            if (addOrderToDatabase(order)) {
                updateProductStock(productName, -quantity);
                JOptionPane.showMessageDialog(addOrderView, "Order added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateOrdersTable();
            } else {
                JOptionPane.showMessageDialog(addOrderView, "Failed to add order.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(addOrderView, "Please enter valid numbers for Quantity and Total.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean addOrderToDatabase(Order order) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "INSERT INTO orders (productname, quantity, total) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, order.getProductName());
                preparedStatement.setInt(2, order.getQuantity());
                preparedStatement.setDouble(3, order.getTotal());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        order.setOrderId(generatedKeys.getInt(1));
                        orders.add(order);
                        return true;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void updateOrder() {
        int selectedRow = viewOrdersView.getOrdersTable().getSelectedRow();
        if (selectedRow != -1) {
            int orderId = (Integer) viewOrdersView.getOrdersTable().getValueAt(selectedRow, 0);
            String productName = viewOrdersView.getProductNameField().getText();
            int quantity = Integer.parseInt(viewOrdersView.getQuantityField().getText());
            double total = Double.parseDouble(viewOrdersView.getTotalField().getText());

            if (!isProductAvailable(productName)) {
                JOptionPane.showMessageDialog(viewOrdersView, "Product not available.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int currentQuantity = getOrderQuantity(orderId);
            int quantityDifference = quantity - currentQuantity;

            if (!isProductStockSufficient(productName, quantityDifference)) {
                JOptionPane.showMessageDialog(viewOrdersView, "Insufficient product stock.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Order order = new Order(orderId, productName, quantity, total);
            if (updateOrderInDatabase(order)) {
                updateProductStock(productName, -quantityDifference);
                JOptionPane.showMessageDialog(viewOrdersView, "Order updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateOrdersTable();
            } else {
                JOptionPane.showMessageDialog(viewOrdersView, "Failed to update order.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(viewOrdersView, "Please select an order to update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getOrderQuantity(int orderId) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return order.getQuantity();
            }
        }
        return 0;
    }

    private boolean updateOrderInDatabase(Order order) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "UPDATE orders SET productname = ?, quantity = ?, total = ? WHERE orderid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, order.getProductName());
                preparedStatement.setInt(2, order.getQuantity());
                preparedStatement.setDouble(3, order.getTotal());
                preparedStatement.setInt(4, order.getOrderId());
                return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void deleteOrder() {
        int selectedRow = viewOrdersView.getOrdersTable().getSelectedRow();
        if (selectedRow != -1) {
            int orderId = Integer.parseInt(viewOrdersView.getOrdersTable().getValueAt(selectedRow, 0).toString());
            if (deleteOrderFromDatabase(orderId)) {
                JOptionPane.showMessageDialog(viewOrdersView, "Order deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateOrdersTable();
            } else {
                JOptionPane.showMessageDialog(viewOrdersView, "Failed to delete order.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(viewOrdersView, "Please select an order to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean deleteOrderFromDatabase(int orderId) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "DELETE FROM orders WHERE orderid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, orderId);
                return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void updateOrdersTable() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT * FROM orders";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Order> updatedOrders = new ArrayList<>();
                while (resultSet.next()) {
                    int orderId = resultSet.getInt("orderid");
                    String productName = resultSet.getString("productname");
                    int quantity = resultSet.getInt("quantity");
                    double total = resultSet.getDouble("total");
                    updatedOrders.add(new Order(orderId, productName, quantity, total));
                }
                orders = updatedOrders;
                viewOrdersView.setOrdersTableData(orders);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void showAddOrderView() {
        addOrderView.setVisible(true);
    }

    public void showViewOrdersView() {
        updateOrdersTable();
        viewOrdersView.setVisible(true);
    }

    private boolean isProductAvailable(String productName) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT COUNT(*) FROM product WHERE LOWER(name) = LOWER(?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, productName);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean isProductStockSufficient(String productName, int requiredQuantity) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT quantity FROM product WHERE LOWER(name) = LOWER(?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, productName);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int currentStock = resultSet.getInt("quantity");
                    return currentStock >= requiredQuantity;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void updateProductStock(String productName, int quantityChange) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "UPDATE product SET quantity = quantity + ? WHERE LOWER(name) = LOWER(?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, quantityChange);
                preparedStatement.setString(2, productName);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
