package eredua;

import java.io.File;
import java.util.logging.Level;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static final SessionFactory sessionFactory = buildSessionFactory();
	
	private HibernateUtil() {
		throw new IllegalStateException("Utility class");
	}
	
	private static SessionFactory buildSessionFactory() {
		try {
			String configFilePath = "src/hibernate.cfg.xml";
			return new Configuration().configure(new File(configFilePath)).buildSessionFactory();
		}
		catch (HibernateException e) {
			BetsLogger.log(Level.SEVERE, "Errorea SessionFactory sortzen.", e);
			return null;
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
