package adminpannelmodels;

public class Order {
    private int orderId;
    private String productName;
    private int quantity;
    private double total;

    public Order(int orderId, String productName, int quantity, double total) {
        this.orderId = orderId;
        this.productName = productName;
        this.quantity = quantity;
        this.total = total;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
