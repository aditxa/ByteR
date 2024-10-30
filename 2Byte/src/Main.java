import java.util.*;

public class Main {
    public static Customer[] EXISTING_CUSTOMERS = {
        new Customer("a", false),
        new Customer("b", true)
    };

    public static FoodItem[] MENU_ITEMS = {
        new FoodItem("Burger", 1, "Meal", true),
        new FoodItem("Salad", 3, "Salad", true),
        new FoodItem("Roll", 5, "Meal", true),
        new FoodItem("Cake", 7, "Dessert", true),
        new FoodItem("Tea", 10, "Beverage", true)
    };

    private static Customer currentCustomer;
    private static final Admin admin = new Admin();
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            displayMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    loginAsAdmin();
                    break;
                case 2:
                    loginAsCustomer();
                    break;
                case 3:
                    System.out.println("Exiting the application...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("Welcome to the Food Ordering CLI!");
        System.out.println("Please select an option:");
        System.out.println("1. Login as Admin");
        System.out.println("2. Login as Customer");
        System.out.println("3. Exit");
    }

    private static void loginAsAdmin() {
        System.out.println("Admin Login");
        admin.startAdminMenu(MENU_ITEMS, EXISTING_CUSTOMERS);
//        admin.manageMenu(MENU_ITEMS);
//        admin.manageOrders(EXISTING_CUSTOMERS);
        admin.generateDailySalesReport(EXISTING_CUSTOMERS);
    }

    private static void loginAsCustomer() {
        System.out.println("Customer Login");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        currentCustomer = findCustomer(name, EXISTING_CUSTOMERS);
        if (currentCustomer == null) {
            System.out.println("Customer not found. Creating a new customer account...");
            currentCustomer = new Customer(name, false);
            EXISTING_CUSTOMERS = addCustomer(EXISTING_CUSTOMERS, currentCustomer);
        }

        currentCustomer.browseMenu(MENU_ITEMS);
//        currentCustomer.manageCart();//MENU_ITEMS);
//        currentCustomer.trackOrders();
        currentCustomer.leaveReview();
    }

    private static Customer findCustomer(String name, Customer[] customers) {
        for (Customer customer : customers) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null;
    }

    private static Customer[] addCustomer(Customer[] customers, Customer newCustomer) {
        Customer[] updatedCustomers = Arrays.copyOf(customers, customers.length + 1);
        updatedCustomers[customers.length] = newCustomer;
        return updatedCustomers;
    }
}
