import java.util.*;
class FoodItem {
    private String name;
    private double price;
    private String category;
    private boolean available;
    private List<String> reviews;


    public FoodItem(String name, double price, String category, boolean available) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = available;
        this.reviews = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public List<String> getReviews() {
        return reviews; // Getter for reviews
    }

    public void addReview(String review) {
        reviews.add(review); // Method to add a review
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return name + " - $" + price + " (" + category + ")";
    }
}
