package eredua;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static final SessionFactory sessionFactory = buildSessionFactory();
	private static Configuration configuration;

	private HibernateUtil() {
		throw new IllegalStateException("Utility class");
	}
	
	private static SessionFactory buildSessionFactory() {
		try {
            String configFilePath = "hibernate.cfg.xml";
            Configuration conf = new Configuration().configure(new File(configFilePath));
            configuration = conf.configure();
		    return configuration.buildSessionFactory();
		}
		catch (HibernateException e) {
		    System.err.println(e);
			//BetsLogger.log(Level.SEVERE, "Errorea SessionFactory sortzen.", e);
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
	
	public static String getPropertyByName(String name) {
	    return configuration.getProperty(name);
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
