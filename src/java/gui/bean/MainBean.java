package gui.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import businessLogic.bean.FacadeBean;
import domain.Event;
import domain.Question;
import exceptions.EventFinished;
import exceptions.QuestionAlreadyExist;

public class MainBean {
    
	private String galderaIzena;
	private float apostuMin;
    private BLFacade facadeBL;
    private Date data;
    private Event gertaera;
    private Question galdera;
    private static List<Event> gertaerak;
    private static List<Event> gertaeraGuztiak;
    private static List<Question> galderak;
    private static String egunak;
    
    public MainBean() {
        this.facadeBL = FacadeBean.getBusinessLogic();
        gertaerak = this.facadeBL.getEvents(this.data);
        egunak = this.getEventsMonthStringArray();
        //BetsLogger.log(Level.INFO, egunak);
        System.out.println(egunak);
        
        gertaeraGuztiak = new ArrayList<>();
        List<Date> allEventDates = this.facadeBL.getEvents();
        for (Date date : allEventDates) {
            List<Event> eventsOnDate = this.facadeBL.getEvents(date);
            gertaeraGuztiak.addAll(eventsOnDate);
        }
    }
    
    public String galderaSortu() {
    	try {
			Question g = this.facadeBL.createQuestion(this.gertaera, this.galderaIzena, this.apostuMin);
	        gertaerak = this.facadeBL.getEvents(this.data);
    		FacesContext.getCurrentInstance().addMessage(null,
    				new FacesMessage("Galdera '" + g.getQuestion() + "/" + g.getBetMinimum() + "' ondo sortua"));
		}
    	catch (EventFinished | QuestionAlreadyExist e) {
    	    //BetsLogger.log(Level.SEVERE, e.getMessage());
    		e.printStackTrace();
    		FacesContext.getCurrentInstance().addMessage(null,
    				new FacesMessage(e.getMessage()));
    	}
    	return null;
    }
    
    public void gertaeraListListener(AjaxBehaviorEvent event) {
    	FacesContext.getCurrentInstance().addMessage(null,
    			new FacesMessage("Aukeratutako gertaera: "
    					+ this.gertaera.getEventNumber() + "/"
    					+ this.gertaera.getDescription()));
    }
    
    public float getApostuMin() {
    	return this.apostuMin;
    }
    
    public String getBundleString(String name) {
        return ResourceBundle.getBundle("Etiquetas").getString(name);
    }
    
    public Date getData() {
        return this.data;
    }
    
    public String getEventsMonthStringArray() {
        StringBuilder bld = new StringBuilder();
        bld.append("[");
        List<Date> tmp = this.facadeBL.getEvents();
        for (int i = 0; i < tmp.size(); i++) {
            Date dI = tmp.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dI);
            bld.append("'" + calendar.get(Calendar.YEAR) + "-"
                    + (calendar.get(Calendar.MONTH) + 1) + "-"
                    + calendar.get(Calendar.DAY_OF_MONTH) + "'");
            if (i < tmp.size() - 1) {
                bld.append(",");
            }
        }
        bld.append("]");
        return bld.toString();
    }
    
    public Question getGaldera() {
        return this.galdera;
    }
    
    public String getGalderaIzena() {
    	return this.galderaIzena;
    }
    
    public Event getGertaera() {
        return this.gertaera;
    }
    
    @SuppressWarnings("static-method")
    public List<Question> getGalderak() {
        return galderak;
    }
    
    @SuppressWarnings("static-method")
    public List<Event> getGertaerak() {
        return gertaerak;
    }
    
    @SuppressWarnings("static-method")
    public List<Event> getGertaeraGuztiak() {
        return gertaeraGuztiak;
    }
    
    @SuppressWarnings("static-method")
    public String getEgunak() {
        return egunak;
    }
    
    public void onDateSelect(SelectEvent event) {
        this.resetSelections();
        gertaerak = this.facadeBL.getEvents(this.data);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Data aukeraua: " + event.getObject()));
    }
    
    public void onGalderaSelect(SelectEvent event) {
        FacesContext.getCurrentInstance().addMessage("nireForm:mezuak",
                new FacesMessage("Galdera (taula): " + this.galdera.getQuestionNumber()
                        + "/" + this.galdera.getQuestion()));
    }
    
    public void onGertaeraSelect(SelectEvent event) {
        this.resetGalderaSelection();
        this.gertaera = (Event) event.getObject();
        FacesContext.getCurrentInstance().addMessage("nireForm:mezuak",
                new FacesMessage("Gertaera (taula): " + this.gertaera.getEventNumber()
                        + "/" + this.gertaera.getDescription()));
        galderak = this.gertaera.getQuestions();
    }
    
    public void resetGalderaSelection() {
        this.galdera = null;
        galderak = null;
    }
    
    public void resetSelections() {
        this.gertaera = null;
        this.resetGalderaSelection();
    }
    
    public void setApostuMin(float apostuMin) {
    	this.apostuMin = apostuMin;
    }
    
    public void setData(Date data) {
        this.data = data;
    }
    
    public void setGaldera(Question galdera) {
        this.galdera = galdera;
    }
    
    public void setGalderaIzena(String galderaIzena) {
    	this.galderaIzena = galderaIzena;
    }
    
    public void setGertaera(Event gertaera) {
        this.gertaera = gertaera;
    }
    
    @SuppressWarnings("static-method")
    public void setGalderak(List<Question> gL) {
        galderak = gL;
    }
    
    @SuppressWarnings("static-method")
    public void setGertaerak(List<Event> gL) {
        gertaerak = gL;
    }

    @SuppressWarnings("static-method")
    public void setGertaeraGuztiak(List<Event> gG) {
        gertaeraGuztiak = gG;
    }
    
    @SuppressWarnings("static-method")
    public void setEgunak(String eV) {
        egunak = eV;
    }
}
