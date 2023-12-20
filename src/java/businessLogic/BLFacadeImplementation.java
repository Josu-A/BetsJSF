package businessLogic;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import configuration.HibernateUtil;
import dataAccess.HibernateDataAccess;
import domain.Question;
import domain.Event;
import exceptions.EventFinished;
import exceptions.QuestionAlreadyExist;

/**
 * It implements the business logic as a web service.
 */
public class BLFacadeImplementation implements BLFacade {
    HibernateDataAccess dbManager;

    public BLFacadeImplementation() {
        System.out.println("Creating BLFacadeImplementation instance");
        boolean createDB = false;
        if (HibernateUtil.getPropertyByName("hibernate.hbm2ddl.auto").equals("create")) {
            createDB = true;
        }
        this.dbManager = new HibernateDataAccess(createDB);

    }

    public BLFacadeImplementation(HibernateDataAccess da) {
        System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
        this.dbManager = da;
    }

    /**
     * This method creates a question for an event, with a question text and the
     * minimum bet
     * 
     * @param event      to which question is added
     * @param question   text of the question
     * @param betMinimum minimum quantity of the bet
     * @return the created question, or null, or an exception
     * @throws EventFinished        if current data is after data of the event
     * @throws QuestionAlreadyExist if the same question already exists for the
     *                              event
     */
    @Override
    public Question createQuestion(Event event, String question, float betMinimum)
            throws EventFinished, QuestionAlreadyExist {
        HibernateDataAccess.open(false);
        if (new Date().compareTo(event.getEventDate()) > 0) {
            throw new EventFinished(ResourceBundle.getBundle("Etiquetas").getString("ErrorEventHasFinished"));
        }
        return this.dbManager.createQuestion(event, question, betMinimum);
    }

    /**
     * This method invokes the data access to retrieve the events of a given date
     * 
     * @param date in which events are retrieved
     * @return collection of events
     */
    @Override
    public List<Event> getEvents(Date date) {
        HibernateDataAccess.open(false);
        return this.dbManager.getEvents(date);
    }

    /**
     * This method invokes the data access to retrieve the dates a month for which
     * there are events
     * 
     * @param date of the month for which days with events want to be retrieved
     * @return collection of dates
     */
    @Override
    public List<Date> getEventsMonth(Date date) {
        HibernateDataAccess.open(false);
        return this.dbManager.getEventsMonth(date);
    }
    
    /**
     * This method invokes the data access to retrieve the dates for which
     * there are events
     * 
     * @return collection of dates
     */
    @Override
    public List<Date> getEvents() {
        HibernateDataAccess.open(false);
        return this.dbManager.getEvents();
    }

    public void close() {
        HibernateDataAccess dB4oManager = new HibernateDataAccess(false);
        dB4oManager.close();
    }

    /**
     * This method invokes the data access to initialize the database with some
     * events and questions. It is invoked only when the option "initialize" is
     * declared in the tag dataBaseOpenMode of resources/config.xml file
     */
    @Override
    public void initializeBD() {
        HibernateDataAccess.open(false);
        HibernateDataAccess.initializeDB();
    }
}
