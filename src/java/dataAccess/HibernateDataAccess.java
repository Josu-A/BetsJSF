package dataAccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import configuration.UtilDate;
import domain.*;
import eredua.HibernateUtil;
import exceptions.QuestionAlreadyExist;

/**
 * It implements the data access to the objectDb database
 */
public class HibernateDataAccess {
    
    private static Session session;

    public HibernateDataAccess(boolean initializeMode) {
        open(initializeMode);

    }

    public HibernateDataAccess() {
        this(false);
    }

    public static void initializeDB() {
        session.beginTransaction();
        try {

            Calendar today = Calendar.getInstance();

            int month = today.get(Calendar.MONTH);
            month += 1;
            int year = today.get(Calendar.YEAR);
            if (month == 12) {
                month = 0;
                year += 1;
            }

            Event ev1 = new Event("Atlético-Athletic", UtilDate.newDate(year, month, 17));
            Event ev2 = new Event("Eibar-Barcelona", UtilDate.newDate(year, month, 17));
            Event ev3 = new Event("Getafe-Celta", UtilDate.newDate(year, month, 17));
            Event ev4 = new Event("Alavés-Deportivo", UtilDate.newDate(year, month, 17));
            Event ev5 = new Event("Español-Villareal", UtilDate.newDate(year, month, 17));
            Event ev6 = new Event("Las Palmas-Sevilla", UtilDate.newDate(year, month, 17));
            Event ev7 = new Event("Malaga-Valencia", UtilDate.newDate(year, month, 17));
            Event ev8 = new Event("Girona-Leganés", UtilDate.newDate(year, month, 17));
            Event ev9 = new Event("Real Sociedad-Levante", UtilDate.newDate(year, month, 17));
            Event ev10 = new Event("Betis-Real Madrid", UtilDate.newDate(year, month, 17));

            Event ev11 = new Event("Atletico-Athletic", UtilDate.newDate(year, month, 1));
            Event ev12 = new Event("Eibar-Barcelona", UtilDate.newDate(year, month, 1));
            Event ev13 = new Event("Getafe-Celta", UtilDate.newDate(year, month, 1));
            Event ev14 = new Event("Alavés-Deportivo", UtilDate.newDate(year, month, 1));
            Event ev15 = new Event("Español-Villareal", UtilDate.newDate(year, month, 1));
            Event ev16 = new Event("Las Palmas-Sevilla", UtilDate.newDate(year, month, 1));

            Event ev17 = new Event("Málaga-Valencia", UtilDate.newDate(year, month + 1, 28));
            Event ev18 = new Event("Girona-Leganés", UtilDate.newDate(year, month + 1, 28));
            Event ev19 = new Event("Real Sociedad-Levante", UtilDate.newDate(year, month + 1, 28));
            Event ev20 = new Event("Betis-Real Madrid", UtilDate.newDate(year, month + 1, 28));

            if (Locale.getDefault().equals(new Locale("es"))) {
                ev1.addQuestion("¿Quién ganará el partido?", 1);
                ev1.addQuestion("¿Quién meterá el primer gol?", 2);
                ev11.addQuestion("¿Quién ganará el partido?", 1);
                ev11.addQuestion("¿Cuántos goles se marcarán?", 2);
                ev17.addQuestion("¿Quién ganará el partido?", 1);
                ev17.addQuestion("¿Habrá goles en la primera parte?", 2);
            } else if (Locale.getDefault().equals(new Locale("en"))) {
                ev1.addQuestion("Who will win the match?", 1);
                ev1.addQuestion("Who will score first?", 2);
                ev11.addQuestion("Who will win the match?", 1);
                ev11.addQuestion("How many goals will be scored in the match?", 2);
                ev17.addQuestion("Who will win the match?", 1);
                ev17.addQuestion("Will there be goals in the first half?", 2);
            } else {
                ev1.addQuestion("Zeinek irabaziko du partidua?", 1);
                ev1.addQuestion("Zeinek sartuko du lehenengo gola?", 2);
                ev11.addQuestion("Zeinek irabaziko du partidua?", 1);
                ev11.addQuestion("Zenbat gol sartuko dira?", 2);
                ev17.addQuestion("Zeinek irabaziko du partidua?", 1);
                ev17.addQuestion("Golak sartuko dira lehenengo zatian?", 2);
            }

            session.persist(ev1);
            session.persist(ev2);
            session.persist(ev3);
            session.persist(ev4);
            session.persist(ev5);
            session.persist(ev6);
            session.persist(ev7);
            session.persist(ev8);
            session.persist(ev9);
            session.persist(ev10);
            session.persist(ev11);
            session.persist(ev12);
            session.persist(ev13);
            session.persist(ev14);
            session.persist(ev15);
            session.persist(ev16);
            session.persist(ev17);
            session.persist(ev18);
            session.persist(ev19);
            session.persist(ev20);

            session.getTransaction().commit();
            System.out.println("Db initialized");
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates a question for an event, with a question text and the
     * minimum bet
     * 
     * @param event      to which question is added
     * @param question text of the question
     * @param betMinimum minimum quantity of the bet
     * @return the created question, or null, or an exception
     * @throws QuestionAlreadyExist if the same question already exists for the
     *                              event
     */
    public Question createQuestion(Event event, String question, float betMinimum) throws QuestionAlreadyExist {
        System.out.println(">> DataAccess: createQuestion=> event= " + event + " question= " + question + " betMinimum=" + betMinimum);
        session.beginTransaction();
        Event ev = (Event) session.get(Event.class, event.getEventNumber());
        if (ev.doesQuestionExists(question)) {            
            throw new QuestionAlreadyExist(ResourceBundle.getBundle("Etiquetas").getString("ErrorQueryAlreadyExist"));
        }
        Question q = ev.addQuestion(question, betMinimum);
        session.persist(ev);
        session.getTransaction().commit();
        return q;
    }

    /**
     * This method retrieves from the database the events of a given date
     * 
     * @param date in which events are retrieved
     * @return collection of events
     */
    public List<Event> getEvents(Date date) {
        System.out.println(">> DataAccess: getEvents");
        session.beginTransaction();
        List<Event> res = new ArrayList<>();
        Query query = session.createQuery("SELECT ev FROM Event ev WHERE eventDate = :data");
        query.setParameter("data", date);
        List<Event> events = HibernateUtil.castList(Event.class, query.list());
        for (Event ev : events) {
            System.out.println(ev.toString());
            res.add(ev);
        }
        session.getTransaction().commit();
        return res;
    }

    /**
     * This method retrieves from the database the dates a month for which there are
     * events
     * 
     * @param date of the month for which days with events want to be retrieved
     * @return collection of dates
     */
    public List<Date> getEventsMonth(Date date) {
        System.out.println(">> DataAccess: getEventsMonth");
        
        session.beginTransaction();
        List<Date> res = new ArrayList<>();

        Date firstDayMonthDate = UtilDate.firstDayMonth(date);
        Date lastDayMonthDate = UtilDate.lastDayMonth(date);

        Query query = session.createQuery("SELECT DISTINCT eventDate FROM Event ev WHERE eventDate BETWEEN :dataFirst AND :dataLast");
        query.setParameter("dataFirst", firstDayMonthDate);
        query.setParameter("dataLast", lastDayMonthDate);
        List<Date> dates = HibernateUtil.castList(Date.class, query.list());
        for (Date d : dates) {
            System.out.println(d.toString());
            res.add(d);
        }
        session.getTransaction().commit();
        return res;
    }
    
    /**
     * This method retrieves from the database the dates for which there are
     * events
     * 
     * @return collection of dates
     */
    public List<Date> getEvents() {
        System.out.println(">> DataAccess: getEvents");
        session.beginTransaction();
        List<Date> res = new ArrayList<>();

        List<Date> dates = HibernateUtil.castList(Date.class,
                session.createQuery("SELECT DISTINCT ev.eventDate FROM Event ev").list());
        for (Date d : dates) {
            System.out.println(d.toString());
            res.add(d);
        }
        session.getTransaction().commit();
        return res;
    }

    public static void open(boolean initializeMode) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        if (initializeMode) {
            System.out.println("DB initialization mode set to initialize");
            initializeDB();
        }
    }

    public boolean existQuestion(Event event, String question) {
        System.out.println(">> DataAccess: existQuestion=> event= " + event + " question= " + question);
        session.beginTransaction();
        Event ev = (Event) session.get(Event.class, event.getEventNumber());
        boolean exists = ev.doesQuestionExists(question);
        session.getTransaction().commit();
        return exists;

    }

    public void close() {
        session.close();
        System.out.println("DataBase closed");
    }
}
