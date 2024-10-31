import java.util.Scanner;

public class StartUp {

    static Scanner sc = new Scanner(System.in);

    public static boolean createFlagForLogin() {
        return true;
    }

    public static boolean createAuthFlag() {
        return true;
    }

    static Credentials credentials = new Credentials("Terms/Terms-to-Block.csv");

    static User loggedInUser;


    public static void IntOptions() {
        boolean happy = false;
        boolean operationSuccess = false;

        while (!happy) {
            System.out.println("                                                                        ");
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
                            happy = true;
                            System.out.println("Creating login...");
                            boolean createLoginFlag = createFlagForLogin();
                            credentials.createLogin();
                            operationSuccess = createLoginFlag;
                            IntOptions();
                            break;

                        case "2":
                            happy = true;
                            System.out.println("Logging In...");
                            loggedInUser = credentials.login();
                            if (loggedInUser != null) {
                                System.out.println("Welcome " + loggedInUser.getUsername() + "!");
                                User.initialize();

                            }
                            boolean createAuthFlag = createAuthFlag();

                            operationSuccess = createAuthFlag;
                            break;

                        case "0":
                            happy = true;
                            System.out.println("Exiting...");
                            break;
                    }
                } else if (decision.equalsIgnoreCase("n")) {
                    System.out.println("Operation cancelled.");
                } else {
                    System.out.println("Please provide a valid input (y/n).");
                }
            } else {
                System.out.println("Invalid option, please try again.");
            }
        }

    }

}