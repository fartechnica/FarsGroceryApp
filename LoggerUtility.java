import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtility {

    private static final Logger logger = Logger.getLogger(LoggerUtility.class.getName());

    private static String currentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return now.format(formatter);
    }

    public static void logInit() {
        try {
            new File("logs").mkdirs();
            FileHandler fileHandler = new FileHandler("logs/newLog_" + currentTime() + ".txt", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false); // Prevent logging to console
        } catch (IOException e) {
            logger.severe("Failed to start file handler, Code: 00012X, in class: " + LoggerUtility.class.getName() + " Message: " + e.getMessage());
        }
    }

    public static void info(String message) {
        logger.info(currentTime() + " INFO: " + message);
    }

    public static void severe(String message) {
        logger.severe(currentTime() + " SEVERE: " + message);
    }
}