import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    private String username;
    private String password;
    private static Scanner sc = new Scanner(System.in);

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "'}";
    }

    public void initialize() {
        boolean activeSession = true;

        while (activeSession) {
            ASCIIArtDisplay.displayText2("-------------------------------------------------------------------");
            System.out.println("[1] Create a new grocery list.");
            System.out.println("[2] Modify an existing grocery list.");
            System.out.println("[3] Browse the catalog.");
            System.out.println("[0] Logout");
            ASCIIArtDisplay.displayText2("-------------------------------------------------------------------");

            String option = sc.nextLine();

            switch (option) {
                case "1":
                    createGroceryList();
                    break;
                case "2":
                    modifyGroceryList();
                    break;
                case "3":
                    browseCatalog();
                    break;
                case "0":
                    System.out.println("Logging out...");
                    activeSession = false;
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void browseCatalog() {
    }

    private void createGroceryList() {
        System.out.print("Enter a number for this grocery list: ");
        String number = sc.nextLine();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String filename = getUsername() + "List" + number + date + ".txt";

        try (FileWriter writer = new FileWriter(filename)) {
            System.out.println("Creating grocery list. Enter items (name, weight, price). Type 'done' to finish:");

            while (true) {
                String item = sc.nextLine();
                if (item.equalsIgnoreCase("done")) {
                    break;
                }
                writer.write(item + "\n");
            }
            System.out.println("Grocery list created successfully: " + filename);
        } catch (IOException e) {
            System.err.println("Error creating grocery list: " + e.getMessage());
        }
    }
}