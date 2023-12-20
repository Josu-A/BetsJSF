package eredua;

import java.io.IOException;
import java.util.function.Supplier;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class BetsLogger {
    private static final Logger logger = Logger.getLogger(BetsLogger.class.getName());
    Formatter plainText;
    
    private BetsLogger() {
        throw new IllegalStateException("Utility class");
    }
    
    static {
        try {
            FileHandler fileHandler = new FileHandler("logs/mylog.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            addShutdownHook(fileHandler);
        }
        catch (SecurityException | IOException e) {
            logger.log(Level.SEVERE, "Error initializing logging", e);
        }
    }
    
    private static void addShutdownHook(FileHandler fileHandler) {
        Runtime.getRuntime().addShutdownHook(new Thread(fileHandler::close));
    }
    
    private static Logger getLogger() {
        return logger;
    }
    
    public static void log(Level level, Supplier<String> msgSupplier) {
        getLogger().log(level, msgSupplier);
    }
    
    public static void log(Level level, String msg) {
        getLogger().log(level, msg);
    }
    
    public static void log(Level level, String msg, Object param1) {
        getLogger().log(level, msg, param1);
    }
}
