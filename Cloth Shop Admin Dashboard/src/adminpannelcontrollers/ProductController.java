package adminpannelcontrollers;

import javax.swing.*;

import adminpannel.DatabaseConnector;
import adminpannelmodels.Product;
import adminpannelviews.AddProductView;
import adminpannelviews.ViewProductsView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    private AddProductView addProductView;
    private ViewProductsView viewProductsView;
    private List<Product> products;

    public ProductController(AddProductView addProductView, ViewProductsView viewProductsView) {
        this.addProductView = addProductView;
        this.viewProductsView = viewProductsView;
        this.products = new ArrayList<>();

        this.addProductView.getAddButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        this.viewProductsView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.viewProductsView.getUpdateButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });

        this.viewProductsView.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });
        
        loadProductsFromDatabase();
    }
    
    private void loadProductsFromDatabase() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT * FROM product";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("productid");
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    int quantity = resultSet.getInt("quantity");
                    products.add(new Product(id, name, price, quantity));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(viewProductsView, "Failed to load products from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addProduct() {
        String name = addProductView.getNameField().getText();
        String priceText = addProductView.getPriceField().getText();
        String quantityText = addProductView.getQuantityField().getText();

        if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(addProductView, "Please fill in all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);

            Product product = new Product(products.size() + 1, name, price, quantity);
            products.add(product);

            if (addProductToDatabase(product)) {
                JOptionPane.showMessageDialog(addProductView, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateProductTable();
            } else {
                JOptionPane.showMessageDialog(addProductView, "Failed to add product to the database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(addProductView, "Please enter valid numbers for Price and Quantity.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean addProductToDatabase(Product product) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "INSERT INTO product (name, price, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, product.getName());
                preparedStatement.setDouble(2, product.getPrice());
                preparedStatement.setInt(3, product.getQuantity());
                preparedStatement.executeUpdate();
                return true; 
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; 
        }
    }

    private void deleteProduct() {
        int selectedRow = viewProductsView.getProductTable().getSelectedRow();
        if (selectedRow != -1) {
            Product productToDelete = products.get(selectedRow);
            products.remove(selectedRow);

            if (deleteProductFromDatabase(productToDelete)) {
                JOptionPane.showMessageDialog(viewProductsView, "Product deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateProductTable();
            } else {
                JOptionPane.showMessageDialog(viewProductsView, "Failed to delete product from the database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(viewProductsView, "Please select a product to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean deleteProductFromDatabase(Product product) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "DELETE FROM product WHERE productid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, product.getId());
                preparedStatement.executeUpdate();
                return true; 
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; 
        }
    }

    private void updateProduct() {
        int selectedRow = viewProductsView.getProductTable().getSelectedRow();
        if (selectedRow != -1) {
            String name = viewProductsView.getNameField().getText();
            double price = Double.parseDouble(viewProductsView.getPriceField().getText());
            int quantity = Integer.parseInt(viewProductsView.getQuantityField().getText());

            Product productToUpdate = products.get(selectedRow);
            productToUpdate.setName(name);
            productToUpdate.setPrice(price);
            productToUpdate.setQuantity(quantity);

            if (updateProductInDatabase(productToUpdate)) {
                JOptionPane.showMessageDialog(viewProductsView, "Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateProductTable();
            } else {
                JOptionPane.showMessageDialog(viewProductsView, "Failed to update product in the database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(viewProductsView, "Please select a product to update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean updateProductInDatabase(Product product) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "UPDATE product SET name = ?, price = ?, quantity = ? WHERE productid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, product.getName());
                preparedStatement.setDouble(2, product.getPrice());
                preparedStatement.setInt(3, product.getQuantity());
                preparedStatement.setInt(4, product.getId());
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; 
        }
    }

    private void updateProductTable() {
        Object[][] data = new Object[products.size()][4];
        for (int i = 0; i < products.size(); i++) {
            data[i][0] = products.get(i).getId();
            data[i][1] = products.get(i).getName();
            data[i][2] = products.get(i).getPrice();
            data[i][3] = products.get(i).getQuantity();
        }
        viewProductsView.setProductTableData(data);
    }

    public void showAddProductView() {
        addProductView.setVisible(true);
    }

    public void showViewProductsView() {
        updateProductTable();
        viewProductsView.setVisible(true);
    }
}
