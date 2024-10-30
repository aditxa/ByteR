import java.util.*;

class Customer {
    private String name;
    public boolean isVIP;
    private List<Order> orderHistory;
    private Map<FoodItem, Integer> cart;

    public Customer(String name, boolean isVIP) {
        this.name = name;
        this.isVIP = isVIP;
        this.orderHistory = new ArrayList<>();
        this.cart = new HashMap<>();
    }

    public String getName() {
        return name;
    }
    public List<Order> getOrders() {
        return orderHistory; // Make sure to return the correct list of orders
    }


    public void browseMenu(FoodItem[] menuItems) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Menu Items:");
            for (FoodItem item : menuItems) {
                System.out.println(item);
            }
            System.out.println("\nOptions:");
            System.out.println("1. Search Item");
            System.out.println("2. Filter by Category");
            System.out.println("3. Sort by Price");
            System.out.println("4. Go to Cart");
            System.out.println("5. Leave Review");
            System.out.println("6. View Reviews");
            System.out.println("7. Exit Menu");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (option) {
                case 1:
                    System.out.print("Enter item name or keyword: ");
                    String keyword = scanner.nextLine();
                    searchItem(menuItems, keyword);
                    break;
                case 2:
                    System.out.print("Enter category to filter: ");
                    String category = scanner.nextLine();
                    filterByCategory(menuItems, category);
                    break;
                case 3:
                    System.out.println("Sort by:");
                    System.out.println("1. Ascending");
                    System.out.println("2. Descending");
                    int sortOption = scanner.nextInt();
                    sortByPrice(menuItems, sortOption);
                    break;
                case 4:
                    manageCart();
                    break;
                case 5:
                    leaveReview(); // Call leaveReview method
                    break;
                case 6:
                    viewReviews(); // Call viewReviews method
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void searchItem(FoodItem[] menuItems, String keyword) {
        for (FoodItem item : menuItems) {
            if (item.getName().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(item);
            }
        }
    }

    private void filterByCategory(FoodItem[] menuItems, String category) {
        for (FoodItem item : menuItems) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                System.out.println(item);
            }
        }
    }

    private void sortByPrice(FoodItem[] menuItems, int order) {
        Arrays.sort(menuItems, Comparator.comparingDouble(FoodItem::getPrice));
        if (order == 2) {
            Collections.reverse(Arrays.asList(menuItems));
        }
        System.out.println("Sorted Menu Items:");
        for (FoodItem item : menuItems) {
            System.out.println(item);
        }
    }

    public void manageCart() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Your Cart:");
            viewCart();

            System.out.println("\nCart Options:");
            System.out.println("1. Add Item");
            System.out.println("2. Modify Quantity");
            System.out.println("3. Remove Item");
            System.out.println("4. Checkout");
            System.out.println("5. Exit Cart");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (option) {
                case 1:
                    addItemToCart();
                    break;
                case 2:
                    modifyItemQuantity();
                    break;
                case 3:
                    removeItemFromCart();
                    break;
                case 4:
                    checkout();
                    return;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            cart.forEach((item, quantity) -> System.out.println(item + " - Quantity: " + quantity));
            System.out.println("Total: $" + calculateTotal());
        }
    }

    private void addItemToCart() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the food item to add: ");
        String itemName = scanner.nextLine();

        for (FoodItem item : Main.MENU_ITEMS) {
            if (item.getName().equalsIgnoreCase(itemName) && item.isAvailable()) {
                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                cart.put(item, cart.getOrDefault(item, 0) + quantity);
                System.out.println(quantity + " of " + item.getName() + " added to your cart.");
                return;
            }
        }
        System.out.println("Item not found or not available.");
    }

    private void modifyItemQuantity() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the food item to modify: ");
        String itemName = scanner.nextLine();

        if (cart.containsKey(findFoodItemByName(itemName))) {
            System.out.print("Enter new quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            if (quantity <= 0) {
                cart.remove(findFoodItemByName(itemName));
                System.out.println(itemName + " removed from your cart.");
            } else {
                cart.put(findFoodItemByName(itemName), quantity);
                System.out.println("Quantity updated to " + quantity + " for " + itemName + ".");
            }
        } else {
            System.out.println("Item not found in cart.");
        }
    }

    private FoodItem findFoodItemByName(String name) {
        for (FoodItem item : Main.MENU_ITEMS) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    private void removeItemFromCart() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the food item to remove: ");
        String itemName = scanner.nextLine();

        if (cart.remove(findFoodItemByName(itemName)) != null) {
            System.out.println(itemName + " removed from your cart.");
        } else {
            System.out.println("Item not found in cart.");
        }
    }

    private double calculateTotal() {
        return cart.entrySet().stream().mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue()).sum();
    }

    private void checkout() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Checkout:");
        System.out.println("Your total is: $" + calculateTotal());
        System.out.print("Enter payment details (e.g., credit card number): ");
        String paymentDetails = scanner.nextLine();

        // Assuming a successful payment process
        Order newOrder = new Order(new ArrayList<>(cart.keySet()), calculateTotal(), "Order Received", "");
        orderHistory.add(newOrder);
        cart.clear(); // Clear the cart after checkout
        System.out.println("Order placed successfully! Your order ID is: " + newOrder.getId());
    }

    public void trackOrders() {
        System.out.println("Order History:");
        for (Order order : orderHistory) {
            System.out.println(order);
        }
    }

    public void leaveReview() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the food item you want to review: ");
        String itemName = scanner.nextLine();

        FoodItem item = findFoodItemByName(itemName);
        if (item != null) {
            System.out.print("Enter your review: ");
            String review = scanner.nextLine();
            item.addReview(review); // Add the review to the food item
            System.out.println("Thank you for your review!");
        } else {
            System.out.println("Food item not found.");
        }
    }

    public void viewReviews() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the food item to view reviews: ");
        String itemName = scanner.nextLine();

        FoodItem item = findFoodItemByName(itemName);
        if (item != null) {
            List<String> reviews = item.getReviews();
            if (reviews.isEmpty()) {
                System.out.println("No reviews yet for this item.");
            } else {
                System.out.println("Reviews for " + itemName + ":");
                for (String review : reviews) {
                    System.out.println("- " + review);
                }
            }
        } else {
            System.out.println("Food item not found.");
        }
    }

    public boolean isVIP() {
        return isVIP;
    }
}




////////////////////////////////////////////////
//import java.util.*;
//class Customer {
//    private String name;
//    private boolean isVIP;
//    private List<Order> orders;
//
//    public Customer(String name, boolean isVIP) {
//        this.name = name;
//        this.isVIP = isVIP;
//        this.orders = new ArrayList<>();
//
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public boolean isVIP() {
//        return isVIP;
//    }
//
//    public List<Order> getOrders() {
//        return orders;
//    }
//
//    public void browseMenu(FoodItem[] menuItems) {
//        System.out.println("Menu:");
//        for (FoodItem item : menuItems) {
//            if (item != null && item.isAvailable()) {
//                System.out.println(item);
//            }
//        }
//
//        System.out.println("1. Search by name");
//        System.out.println("2. Filter by category");
//        System.out.println("3. Sort by price");
//        System.out.println("4. Add to cart");
//        System.out.println("5. Go back");
//
//        int choice = Main.scanner.nextInt();
//        Main.scanner.nextLine(); // Consume newline character
//
//        switch (choice) {
//            case 1:
//                searchByName(menuItems);
//                break;
//            case 2:
//                filterByCategory(menuItems);
//                break;
//            case 3:
//                sortByPrice(menuItems);
//                break;
//            case 4:
//                manageCart(menuItems);
//                break;
//            case 5:
//                return;
//            default:
//                System.out.println("Invalid choice. Please try again.");
//        }
//    }
//
//    private void searchByName(FoodItem[] menuItems) {
//        System.out.print("Enter the item name: ");
//        String name = Main.scanner.nextLine();
//
//        System.out.println("Search results:");
//        for (FoodItem item : menuItems) {
//            if (item != null && item.getName().toLowerCase().contains(name.toLowerCase()) && item.isAvailable()) {
//                System.out.println(item);
//            }
//        }
//    }
//
//    private void filterByCategory(FoodItem[] menuItems) {
//        System.out.print("Enter the category: ");
//        String category = Main.scanner.nextLine();
//
//        System.out.println("Items in the " + category + " category:");
//        for (FoodItem item : menuItems) {
//            if (item != null && item.getCategory().equalsIgnoreCase(category) && item.isAvailable()) {
//                System.out.println(item);
//            }
//        }
//    }
//
//    private void sortByPrice(FoodItem[] menuItems) {
//        List<FoodItem> sortedItems = new ArrayList<>();
//        for (FoodItem item : menuItems) {
//            if (item != null) {
//                sortedItems.add(item);
//            }
//        }
//        sortedItems.sort(Comparator.comparingDouble(FoodItem::getPrice));
//
//        System.out.println("Menu sorted by price:");
//        for (FoodItem item : sortedItems) {
//            if (item.isAvailable()) {
//                System.out.println(item);
//            }
//        }
//    }
//
//    public void manageCart(FoodItem[] menuItems) {
//        // Implement the logic to add items to the customer's cart, update quantities, and place the order
//        System.out.println("Manage Cart feature is not implemented yet.");
//    }
//
//    public void trackOrders() {
//        System.out.println("Orders History:");
//        if (orders.isEmpty()) {
//            System.out.println("You have no orders.");
//        } else {
//            for (Order order : orders) {
//                System.out.println(order);
//            }
//        }
//    }
//
//    public void leaveReview() {
//        System.out.print("Enter the order number you want to review: ");
//        int orderIndex = Main.scanner.nextInt() - 1; // Assuming orders are indexed from 1
//        Main.scanner.nextLine(); // Consume newline character
//
//        if (orderIndex < 0 || orderIndex >= orders.size()) {
//            System.out.println("Invalid order number.");
//            return;
//        }
//
//        System.out.print("Enter your review: ");
//        String review = Main.scanner.nextLine();
//
//        // Assuming Order class has a method to add review
//        orders.get(orderIndex).addReview(review);
//        System.out.println("Thank you for your review!");
//    }
//}
//
