package nagusia;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Query;
import org.hibernate.PropertyValueException;

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
        catch(PropertyValueException ex) {
            BetsLogger.log(Level.INFO, String.format("Errorea: data falta da: %s", ex.toString()));
            e = null;
            session.getTransaction().rollback();
        }
        catch(HibernateException ex) {
            BetsLogger.log(Level.SEVERE, String.format("Errorea: erabiltzailea ez da existitzen: %s", ex.toString()));
        }
        session.getTransaction().commit();
        return e;
    }
    
    public Erabiltzailea createAndStoreErabiltzaileaLoginGertaeraBatekin(String izena,
            String pasahitza, String mota, boolean login, Date data) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Erabiltzailea e = new Erabiltzailea();
        e.setIzena(izena);
        e.setPasahitza(pasahitza);
        e.setMota(mota);
        LoginGertaera lg = new LoginGertaera();
        lg.setErabiltzailea(e);
        lg.setLogin(login);
        lg.setData(data);
        
        HashSet<LoginGertaera> gs = new HashSet<>();
        gs.add(lg);
        e.setGertaerak(gs);
        
        session.persist(e);
        //session.persist(lg);
        
        session.getTransaction().commit();
        return e;
    }
    
    public Erabiltzailea getErabiltzailea(String erabiltzaileaIzena) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Query q = session.createQuery("SELECT lg.erabiltzailea FROM LoginGertaera lg WHERE izena = :erabiltzaileaIzena");
        q.setParameter("erabiltzaileaIzena", erabiltzaileaIzena);
        List<Erabiltzailea> result = HibernateUtil.castList(Erabiltzailea.class, q.list());
        
        session.getTransaction().commit();
        return result.isEmpty() ? null : (Erabiltzailea) result.get(0);
    }
    
    public List<Erabiltzailea> getErabiltzaileak() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        List<Erabiltzailea> result = HibernateUtil.castList(Erabiltzailea.class,
                session.createQuery("SELECT e FROM Erabiltzailea e").list());
        
        session.getTransaction().commit();
        return result;
    }
    
    public List<LoginGertaera> getLoginGertaerak() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        List<LoginGertaera> result = HibernateUtil.castList(LoginGertaera.class,
                session.createQuery("SELECT lg FROM LoginGertaera lg").list());
        
        session.getTransaction().commit();
        return result;
    }
    
    public List<LoginGertaera> getLoginGertaerakv1(String erabiltzaileaIzena) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Query q = session.createQuery("SELECT lg FROM LoginGertaera lg INNER JOIN " 
                + "lg.erabiltzailea e WHERE e.izena = :erabiltzaileaIzena");
        q.setParameter("erabiltzaileaIzena", erabiltzaileaIzena);
        List<LoginGertaera> result = HibernateUtil.castList(LoginGertaera.class, q.list());
        
        session.getTransaction().commit();
        return result;
    }
    
    public List<LoginGertaera> getLoginGertaerakv2(String erabiltzaileaIzena) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Query q = session.createQuery("SELECT lg FROM LoginGertaera lg WHERE "
                + "lg.erabiltzailea.izena = :erabiltzaileaIzena");
        q.setParameter("erabiltzaileaIzena", erabiltzaileaIzena);
        List<LoginGertaera> result = HibernateUtil.castList(LoginGertaera.class, q.list());
        
        session.getTransaction().commit();
        return result;
    }
    
    public List<LoginGertaera> getLoginGertaerakv3(String erabiltzaileaIzena) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Criteria c = session.createCriteria(LoginGertaera.class)
                .createCriteria("erabiltzailea")
                .add(Restrictions.eq("izena", erabiltzaileaIzena));
        List<LoginGertaera> result = HibernateUtil.castList(LoginGertaera.class, c.list());
        
        session.getTransaction().commit();
        return result;
    }
    
    public boolean deleteErabiltzailea(String erabiltzailea) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        try {
            Erabiltzailea e = (Erabiltzailea) session.get(Erabiltzailea.class, erabiltzailea);
            session.delete(e);
            session.getTransaction().commit();
        }
        catch (HibernateException ex) {
            BetsLogger.log(Level.SEVERE, String.format("Errorea: %s", ex.toString()));
            return false;
        }
        
        return true;
    }
    
    public static void main(String[] args) {
        String userNameAne = "Ane";
        String userNameKepa = "Kepa";
        String userNameNekane = "Nekane";
        
        String userTypeIkaslea = "ikaslea";
        
        GertaerakDataAccess e = new GertaerakDataAccess();
        
        BetsLogger.log(Level.INFO, "Gertaeren sorkuntza:");
        e.createAndStoreErabiltzailea(userNameAne, "125", userTypeIkaslea);
        e.createAndStoreLoginGertaera(userNameAne, true, new Date());
        e.createAndStoreLoginGertaera(userNameAne, false, new Date());
        
        e.createAndStoreErabiltzailea(userNameKepa, "126", userTypeIkaslea);
        e.createAndStoreLoginGertaera(userNameKepa, true, new Date());
        e.createAndStoreLoginGertaera(userNameKepa, false, new Date());
        
        List<Erabiltzailea> erabiltzaileak = e.getErabiltzaileak();
        BetsLogger.log(Level.INFO, () -> String.format("3.1 => Erabiltzaileak: %s", erabiltzaileak.toString()));
        
        List<LoginGertaera> loginGertaerak = e.getLoginGertaerak();
        BetsLogger.log(Level.INFO, () -> String.format("3.1 => Login Gertaerak: %s", loginGertaerak.toString()));
        
        Erabiltzailea erab = loginGertaerak.get(0).getErabiltzailea();
        BetsLogger.log(Level.INFO, () -> String.format("3.2 => %s", erab));
        
        List<LoginGertaera> lg1 = e.getLoginGertaerakv1(erab.getIzena());
        BetsLogger.log(Level.INFO, () -> String.format("3.3.1 => %sren Login Gertareak: %s",
                erab.getIzena(), lg1));
        
        List<LoginGertaera> lg2 = e.getLoginGertaerakv2(erab.getIzena());
        BetsLogger.log(Level.INFO, () -> String.format("3.3.2 => %sren Login Gertareak: %s",
                erab.getIzena(), lg2));
        
        List<LoginGertaera> lg3 = e.getLoginGertaerakv3(erab.getIzena());
        BetsLogger.log(Level.INFO, () -> String.format("3.3.3 => %sren Login Gertareak: %s",
                erab.getIzena(), lg3));
        
        BetsLogger.log(Level.INFO, () -> String.format("3.4 => %s", erab.getGertaerak()));
        
        //LoginGertaera lgAne = e.createAndStoreLoginGertaera(userNameAne, true, null);
        //BetsLogger.log(Level.INFO, () -> String.format("4.1 => %s", lgAne));
        
        e.createAndStoreErabiltzailea(userNameNekane, "127", userTypeIkaslea);
        e.createAndStoreLoginGertaera(userNameNekane, true, new Date());
        BetsLogger.log(Level.INFO, () -> String.format("4.2.1 => %s", e.getLoginGertaerak()));
        e.deleteErabiltzailea(userNameNekane);
        BetsLogger.log(Level.INFO, () -> String.format("4.2.2 => %s", e.getLoginGertaerak()));
        
        Erabiltzailea erab2 = e.createAndStoreErabiltzaileaLoginGertaeraBatekin("Peru", "128", userTypeIkaslea, true, new Date());
        BetsLogger.log(Level.INFO, () -> String.format("4.3 => %s", e.getErabiltzaileak()));
        BetsLogger.log(Level.INFO, () -> String.format("4.3 => %s", e.getLoginGertaerak()));
        BetsLogger.log(Level.INFO, () -> String.format("4.3 => Erabiltzailea: %s Bere gertaerak: %s", erab2, erab2.getGertaerak()));
    }
}
