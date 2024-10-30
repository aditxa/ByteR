import java.util.*;
class Order {
    private int id;
    private static int orderCounter = 0;
    private List<FoodItem> items;
    private double totalCost;
    private String status;
    private String specialRequest;

    public Order(List<FoodItem> items, double totalCost, String status, String specialRequest) {
        this.id = generateOrderId();
        this.items = items;
        this.totalCost = totalCost;
        this.status = status;
        this.specialRequest = specialRequest;
    }

    public int getId() {
        return id;
    }

    public List<FoodItem> getItems() {
        return items;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    @Override
    public String toString() {
        return "Order #" + id + " - Total: $" + totalCost + ", Status: " + status;
    }

    private int generateOrderId() {
        return ++orderCounter; // Increment orderCounter for unique ID
    }
}
