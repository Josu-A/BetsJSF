package nagusia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.hibernate.classic.Session;

import eredua.HibernateUtil;
import eredua.domeinua.LoginGertaera;

public class GertaerakSortu {
    private static final Logger logger = Logger.getLogger(GertaerakSortu.class.getName());

    public GertaerakSortu() {
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
    
    private static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
        List<T> r = new ArrayList<>(c.size());
        for (Object o : c) {
            r.add(clazz.cast(o));
        }
        return r;
    }
    
    private void createAndStoreLoginGertaera(String deskribapena, Date data) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        LoginGertaera e = new LoginGertaera();
        e.setDeskribapena(deskribapena);
        e.setData(data);
        session.persist(e);
        
        session.getTransaction().commit();
    }
    
    private List<LoginGertaera> gertaerakZerrendatu() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        List<LoginGertaera> result = castList(LoginGertaera.class,
                session.createQuery("select lg from LoginGertaera lg").list());
        
        session.getTransaction().commit();
        return result;
    }
    
    public static void main(String[] args) {
        GertaerakSortu e = new GertaerakSortu();
        logger.log(Level.INFO, "Gertaeraren sorkuntza");
        e.createAndStoreLoginGertaera("Anek ondo egin du logina", new Date());
        e.createAndStoreLoginGertaera("Nerea saiatu da login egiten", new Date());
        e.createAndStoreLoginGertaera("Kepak ondo egin du logina", new Date());
        logger.log(Level.INFO, "Gertaeren zerrenda:");
        
        List<LoginGertaera> gertaerak = e.gertaerakZerrendatu();
        for (int i = 0; i < gertaerak.size(); i++) {
            LoginGertaera ev = gertaerak.get(i);
            logger.log(Level.INFO, () -> String.format("Id: %s Deskribapena: %s Data: %s",
            		ev.getId(), ev.getDeskribapena(), ev.getData()));
        }
    }
}
