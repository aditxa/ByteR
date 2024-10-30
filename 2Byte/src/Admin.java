import java.util.*;
import java.util.Scanner;// Ensure correct imports

class Admin {
    public void startAdminMenu(FoodItem[] menuItems, Customer[] customers) {
        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. Menu Management");
            System.out.println("2. Order Management");
            System.out.println("3. Exit");

            int choice = Main.scanner.nextInt();
            Main.scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    manageMenu(menuItems);
                    break;
                case 2:
                    manageOrders(customers);
                    break;
                case 3:
                    System.out.println("Exiting Admin Menu.");
                    return; // Exit the admin menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    public void manageMenu(FoodItem[] menuItems) {
        System.out.println("Menu Management:");
        System.out.println("1. Add new item");
        System.out.println("2. Update existing item");
        System.out.println("3. Remove item");
        System.out.println("4. Go back");

        int choice = Main.scanner.nextInt(); // Corrected `Main` to `Main`
        Main.scanner.nextLine(); // Consume newline character

        switch (choice) {
            case 1:
                addNewItem(menuItems);
                break;
            case 2:
                updateExistingItem(menuItems);
                break;
            case 3:
                removeItem(menuItems);
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void addNewItem(FoodItem[] menuItems) {
        System.out.print("Enter the name of the new item: ");
        String name = Main.scanner.nextLine();

        System.out.print("Enter the price of the new item: ");
        double price = Main.scanner.nextDouble();
        Main.scanner.nextLine(); // Consume newline character

        System.out.print("Enter the category of the new item: ");
        String category = Main.scanner.nextLine();

        System.out.print("Is the new item available? (true/false): ");
        boolean available = Main.scanner.nextBoolean();
        Main.scanner.nextLine(); // Consume newline character

        FoodItem newItem = new FoodItem(name, price, category, available);
        addItemToMenu(menuItems, newItem);
        System.out.println("New item added successfully.");


    }

    private void updateExistingItem(FoodItem[] menuItems) {
        System.out.print("Enter the name of the item to update: ");
        String name = Main.scanner.nextLine();

        FoodItem item = findItem(menuItems, name);
        if (item == null) {
            System.out.println("Item not found. Please try again.");
            return;
        }

        System.out.print("Enter the new price for the item: ");
        double newPrice = Main.scanner.nextDouble();
        Main.scanner.nextLine(); // Consume newline character

        System.out.print("Enter the new category for the item: ");
        String newCategory = Main.scanner.nextLine();

        System.out.print("Is the item now available? (true/false): ");
        boolean newAvailability = Main.scanner.nextBoolean();
        Main.scanner.nextLine(); // Consume newline character

        item.setPrice(newPrice);
        item.setCategory(newCategory);
        item.setAvailable(newAvailability);
        System.out.println("Item updated successfully.");
    }

    private void removeItem(FoodItem[] menuItems) {
        System.out.print("Enter the name of the item to remove: ");
        String name = Main.scanner.nextLine();

        FoodItem item = findItem(menuItems, name);
        if (item == null) {
            System.out.println("Item not found. Please try again.");
            return;
        }

        // Update the status of all pending orders containing the removed item
        for (Customer customer : Main.EXISTING_CUSTOMERS) {
            for (Order order : customer.getOrders()) {
                if (order.getStatus().equals("pending") && order.getItems().contains(item)) {
                    order.setStatus("denied");
                }
            }
        }

        // Remove the item from the menu
        removeItemFromMenu(menuItems, item);
        System.out.println("Item removed successfully.");
    }

    public void manageOrders(Customer[] customers) {

        displayOrderTable(customers);

        System.out.println("Order Management:");
        System.out.println("1. View pending orders");
        System.out.println("2. Update order status");
        System.out.println("3. Process refunds");
        System.out.println("4. Handle special requests");
        System.out.println("5. Go back");

        int choice = Main.scanner.nextInt();
        Main.scanner.nextLine(); // Consume newline character

        switch (choice) {
            case 1:
                viewPendingOrders(customers);
                break;
            case 2:
                updateOrderStatus(customers);
                break;
            case 3:
                processRefunds(customers);
                break;
            case 4:
                handleSpecialRequests(customers);
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    private void displayOrderTable(Customer[] customers) {
        System.out.printf("%-10s %-15s %-20s %-30s %-10s%n", "Order ID", "Order Status", "Customer (VIP)", "Items", "Total Amount");

        for (Customer customer : customers) {
            for (Order order : customer.getOrders()) {
                String vipStatus = customer.isVIP() ? " (V)" : "";
                String itemsList = String.join(", ", order.getItems().stream().map(FoodItem::getName).toList());
                System.out.printf("%-10d %-15s %-20s %-30s $%-10.2f%n",
                    order.getId(), order.getStatus(), customer.getName() + vipStatus, itemsList, order.getTotalCost());
            }
        }
    }
    private void viewPendingOrders(Customer[] customers) {
        System.out.println("Pending Orders:");
        for (Customer customer : customers) {
            for (Order order : customer.getOrders()) {
                if (order.getStatus().equals("pending")) {
                    System.out.println(order);
                }
            }
        }
    }


    private void updateOrderStatus(Customer[] customers) {
        System.out.print("Enter the Order ID to update: ");
        int orderId = Main.scanner.nextInt();
        Main.scanner.nextLine(); // Consume newline

        System.out.print("Enter new status (Pending/Completed/Cancelled): ");
        String newStatus = Main.scanner.nextLine();

        for (Customer customer : customers) {
            for (Order order : customer.getOrders()) {
                if (order.getId() == orderId) {
                    order.setStatus(newStatus);
                    System.out.println("Order status updated successfully.");
                    return;
                }
            }
        }
        System.out.println("Order not found.");
    }

    private void processRefunds(Customer[] customers) {
        System.out.print("Enter the order ID to process a refund: ");
        int orderId = Main.scanner.nextInt();
        Main.scanner.nextLine(); // Consume newline character

        Order order = findOrder(customers, orderId);
        if (order == null) {
            System.out.println("Order not found. Please try again.");
            return;
        }

        if (order.getStatus().equals("canceled") || order.getStatus().equals("denied")) {
            double refundAmount = order.getTotalCost();
            System.out.println("Refund of $" + refundAmount + " has been processed.");
        } else {
            System.out.println("The order is not eligible for a refund.");
        }
    }

    private void handleSpecialRequests(Customer[] customers) {
        System.out.print("Enter the order ID with a special request: ");
        int orderId = Main.scanner.nextInt();
        Main.scanner.nextLine(); // Consume newline character

        Order order = findOrder(customers, orderId);
        if (order == null) {
            System.out.println("Order not found. Please try again.");
            return;
        }

        System.out.println("Special request: " + order.getSpecialRequest());
        System.out.println("Special request has been processed.");
    }

    public void generateDailySalesReport(Customer[] customers) {
        System.out.println("Generating Daily Sales Report...");

        double totalSales = 0;
        int totalOrders = 0;
        Map<FoodItem, Integer> popularItems = new HashMap<>();

        for (Customer customer : customers) {
            for (Order order : customer.getOrders()) {
                if (order.getStatus().equals("completed")) {
                    totalSales += order.getTotalCost();
                    totalOrders++;
                    for (FoodItem item : order.getItems()) {
                        popularItems.put(item, popularItems.getOrDefault(item, 0) + 1);
                    }
                }
            }
        }

        System.out.println("Total Sales: $" + totalSales);
        System.out.println("Total Orders: " + totalOrders);
        System.out.println("Most Popular Items:");
        for (Map.Entry<FoodItem, Integer> entry : sortByValue(popularItems).entrySet()) {
            System.out.println(entry.getKey().getName() + ": " + entry.getValue() + " orders");
        }
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private FoodItem findItem(FoodItem[] menuItems, String name) {
        for (FoodItem item : menuItems) {
            if (item != null && item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    private Order findOrder(Customer[] customers, int orderId) {
        for (Customer customer : customers) {
            for (Order order : customer.getOrders()) {
                if (order.getId() == orderId) {
                    return order;
                }
            }
        }
        return null;
    }

    private void addItemToMenu(FoodItem[] menuItems, FoodItem newItem) {
        for (int i = 0; i < menuItems.length; i++) {
            if (menuItems[i] == null) {
                menuItems[i] = newItem;
                System.out.println("New item added successfully.");
                return;
            }
        }
        FoodItem[] newMenuItems = new FoodItem[menuItems.length + 1];
        System.arraycopy(menuItems, 0, newMenuItems, 0, menuItems.length);
        newMenuItems[menuItems.length] = newItem;
        Main.MENU_ITEMS = newMenuItems;
        System.out.println("New item added successfully (new array created).");// Corrected missing closing brace
    }

    private void removeItemFromMenu(FoodItem[] menuItems, FoodItem item) {
        for (int i = 0; i < menuItems.length; i++) {
            if (menuItems[i] == item) {
                menuItems[i] = null;
                return;
            }
        }
    }
}
