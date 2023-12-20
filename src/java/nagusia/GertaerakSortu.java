package nagusia;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.hibernate.classic.Session;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import eredua.BetsLogger;
import eredua.HibernateUtil;
import eredua.domeinua.Erabiltzailea;
import eredua.domeinua.LoginGertaera;

public class GertaerakSortu {

    public GertaerakSortu() {
        // Empty constructor
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
        List<Erabiltzailea> result = HibernateUtil.castList(Erabiltzailea.class, q.list());
        
        LoginGertaera e = new LoginGertaera();
        try {
            e.setErabiltzailea(result.get(0));
            e.setLogin(login);
            e.setData(data);
            session.persist(e);
        }
        catch (IndexOutOfBoundsException | HibernateException ex) {
            BetsLogger.log(Level.SEVERE, String.format(
                    "Errorea: erabiltzailea ez da existitzen %s", ex.toString()));
        }
        
        session.getTransaction().commit();
    }
    
    private List<LoginGertaera> gertaerakZerrendatu() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        List<LoginGertaera> result = HibernateUtil.castList(LoginGertaera.class,
                session.createQuery("SELECT lg FROM LoginGertaera lg").list());
        
        session.getTransaction().commit();
        return result;
    }
    
    public void printObjMemDB(String azalpena, Erabiltzailea e) {
        BetsLogger.log(Level.INFO, () -> String.format("\tMem:<%s> DB:<%s> => %n%s",
                e, GertaerakBerreskuratuJDBC.getErabiltzaileaJDBC(e), azalpena));
    }
    
    public static void main(String[] args) {
        GertaerakSortu e = new GertaerakSortu();
        BetsLogger.log(Level.INFO, "Gertaeren sorkuntza:");
        e.createAndStoreErabiltzailea("Ane", "125", "ikaslea");
        e.createAndStoreLoginGertaera("Ane", true, new Date());
        e.createAndStoreLoginGertaera("Ane", false, new Date());
        BetsLogger.log(Level.INFO, "Gertaeren zerrenda:");
        List<LoginGertaera> gertaerak = e.gertaerakZerrendatu();
        for (int i = 0; i < gertaerak.size(); i++) {
            LoginGertaera ev = gertaerak.get(i);
            BetsLogger.log(Level.INFO, () -> String.format("Id: %s Deskribapena: %s Data: %s Login: %b",
                    ev.getId(), ev.getDeskribapena(), ev.getData(), ev.isLogin()));
        }
    }
}