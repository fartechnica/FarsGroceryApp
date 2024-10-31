import java.util.Scanner;

public class StartUp {

    static Scanner sc = new Scanner(System.in);
    static Credentials credentials = new Credentials("Terms/Terms-to-Block.csv");
    static User loggedInUser;

    public static void IntOptions() {
        boolean running = true;

        while (running) {
            System.out.println();
            ASCIIArtDisplay.displayText2("-------------------------------------------------------------------");
            System.out.println();
            System.out.println("[1] Create Login");
            System.out.println("[2] Login");
            System.out.println("[0] Exit");
            ASCIIArtDisplay.displayText2("-------------------------------------------------------------------");
            System.out.println();

            String option = sc.nextLine();

            if (option.equals("1") || option.equals("2") || option.equals("0")) {
                System.out.println("You have selected: " + option + ". Would you like to proceed? (y/n)");
                String decision = sc.nextLine();

                if (decision.equalsIgnoreCase("y")) {
                    switch (option) {
                        case "1":
                            System.out.println("Creating login...");
                            credentials.createLogin();
                            break;

                        case "2":
                            System.out.println("Logging In...");
                            loggedInUser = credentials.login();
                            if (loggedInUser != null) {
                                System.out.println("Welcome " + loggedInUser.getUsername() + "!");
                            } else {
                                System.out.println("Login failed, please try again.");
                            }
                            break;

                        case "0":
                            running = false;
                            System.out.println("Exiting...");
                            break;
                    }
                } else {
                    System.out.println("Operation cancelled.");
                }
            } else {
                System.out.println("Invalid option, please try again.");
            }
        }
    }
}