package nagusia;

import java.util.Date;
import java.util.logging.Level;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import eredua.BetsLogger;
import eredua.HibernateUtil;
import eredua.domeinua.Erabiltzailea;
import eredua.domeinua.LoginGertaera;

public class GertaerakDataAccess {
    
    public GertaerakDataAccess() {
        // empty constructor
    }
    
    public Erabiltzailea createAndStoreErabiltzailea(String izena, String pasahitza, String mota) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Erabiltzailea e = new Erabiltzailea();
        e.setIzena(izena);
        e.setPasahitza(pasahitza);
        e.setMota(mota);
        session.persist(e);
        
        session.getTransaction().commit();
        return e;
    }
    
    public LoginGertaera createAndStoreLoginGertaera(String erabiltzailea, boolean login, Date data) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        LoginGertaera e = null;
        try {
            e = new LoginGertaera();
            e.setErabiltzailea((Erabiltzailea) session.get(Erabiltzailea.class, erabiltzailea));
            e.setLogin(login);
            e.setData(data);
            session.persist(e);
        }
        catch(HibernateException ex) {
            BetsLogger.log(Level.SEVERE, String.format("Errorea: erabiltzailea ez da existitzen: %s", ex.toString()));
        }
        session.getTransaction().commit();
        return e;
    }
    
    public static void main(String[] args) {
        GertaerakDataAccess e = new GertaerakDataAccess ();
        
        BetsLogger.log(Level.INFO, "Gertaeren sorkuntza:");
        e.createAndStoreErabiltzailea("Ane", "125", "ikaslea");
        e.createAndStoreLoginGertaera("Ane",true, new Date());
        e.createAndStoreLoginGertaera("Ane",false, new Date());
        
        e.createAndStoreErabiltzailea("Kepa", "126", "ikaslea");
        e.createAndStoreLoginGertaera("Kepa",true, new Date());
        e.createAndStoreLoginGertaera("Kepa",false, new Date());
    }
}
