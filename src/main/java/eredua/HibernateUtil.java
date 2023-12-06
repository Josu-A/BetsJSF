package eredua;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
	
	public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
        List<T> r = new ArrayList<>(c.size());
        for (Object o : c) {
            r.add(clazz.cast(o));
        }
        return r;
    }
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
