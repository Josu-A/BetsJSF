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
import org.hibernate.Query;

import eredua.HibernateUtil;
import eredua.domeinua.Erabiltzailea;
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
    
    private void createAndStoreErabiltzailea(String izena, String pasahitza, String mota) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Erabiltzailea e = new Erabiltzailea();
        e.setIzena(izena);
        e.setPasahitza(pasahitza);
        e.setMota(mota);
        session.persist(e);
        
        session.getTransaction().commit();
    }
    
    private void createAndStoreLoginGertaera(String erabiltzailea, boolean login, Date data) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Query q = session.createQuery("SELECT e FROM Erabiltzailea e WHERE izena= :erabiltzailea");
        q.setParameter("erabiltzailea", erabiltzailea);
        List<Erabiltzailea> result = castList(Erabiltzailea.class, q.list());
        
        LoginGertaera e = new LoginGertaera();
        try {
            e.setErabiltzailea(result.get(0));
            e.setLogin(login);
            e.setData(data);
            session.persist(e);
        }
        catch (IndexOutOfBoundsException ex) {
            logger.log(Level.SEVERE, () -> String.format(
                    "Errorea: erabiltzailea ez da existitzen %s", ex.toString()));
        }
        
        session.getTransaction().commit();
    }
    
    private List<LoginGertaera> gertaerakZerrendatu() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        List<LoginGertaera> result = castList(LoginGertaera.class,
                session.createQuery("SELECT lg FROM LoginGertaera lg").list());
        
        session.getTransaction().commit();
        return result;
    }
    
    public static void main(String[] args) {
        GertaerakSortu e = new GertaerakSortu();
        logger.log(Level.INFO, "Gertaeren sorkuntza:");
        e.createAndStoreErabiltzailea("Ane", "125", "ikaslea");
        e.createAndStoreLoginGertaera("Ane", true, new Date());
        e.createAndStoreLoginGertaera("Ane", false, new Date());
        logger.log(Level.INFO, "Gertaeren zerrenda:");
        List<LoginGertaera> gertaerak = e.gertaerakZerrendatu();
        for (int i = 0; i < gertaerak.size(); i++) {
            LoginGertaera ev = gertaerak.get(i);
            logger.log(Level.INFO, () -> String.format("Id: %s Deskribapena: %s Data: %s Login: %s",
                    ev.getId(), ev.getDeskribapena(), ev.getData(), ev.isLogin()));
        }
    }
}
