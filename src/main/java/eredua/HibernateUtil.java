package eredua;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static final Logger logger = Logger.getLogger(HibernateUtil.class.getName());
	private static final SessionFactory sessionFactory = buildSessionFactory();
	
	private HibernateUtil() {
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
	
	private static SessionFactory buildSessionFactory() {
		try {
			String configFilePath = "src/hibernate.cfg.xml";
			return new Configuration().configure(new File(configFilePath)).buildSessionFactory();
		}
		catch (HibernateException ex) {
			logger.log(Level.SEVERE, "Errorea SessionFactory sortzen.", ex);
			return null;
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
