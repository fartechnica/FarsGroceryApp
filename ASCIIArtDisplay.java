public class ASCIIArtDisplay {

    public static void displayASCIIArt() {
        System.out.println("   ___          _        ___                                   _               ");
        System.out.println("  / __\\_ _ _ __( )__    / _ \\_ __ ___   ___ ___ _ __ _   _    /_\\  _ __  _ __  ");
        System.out.println(" / _\\/ _` | '__|/ __|  / /_\\/ '__/ _ \\ / __/ _ \\ '__| | | |  //_\\\\| '_ \\| '_ \\ ");
        System.out.println("/ / | (_| | |   \\__ \\ / /_\\| | | (_) | (_|  __/ |  | |_| | /  _  \\ |_) | |_) | ");
        System.out.println("\\/   \\__,_|_|   |___/ \\____/|_|  \\___/ \\___\\___|_|   \\__, | \\_/ \\_/ .__/| .__/ ");
        System.out.println("                                                    |___/        |_|   |_|    ");
    }

    public static void displayText(String text) {
        for (char letter : text.toCharArray()) {
            if(Character.isWhitespace(letter)) {
                System.out.print(letter);
            }
            else {
                System.out.print(letter);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    LoggerUtility.severe("Interrupted Exception, Code: 00013X in class: " + ASCIIArtDisplay.class.getName());
                }
            }

        }
    }

    public static void displayText2(String text) {
        for (char letter : text.toCharArray()) {
            if(Character.isWhitespace(letter)) {
                System.out.print(letter);
            }
            else {
                System.out.print(letter);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    LoggerUtility.severe("Interrupted Exception, Code: 00014X in class: " + ASCIIArtDisplay.class.getName());
                }
            }

        }
    }

}