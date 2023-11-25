package eredua.bean;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import domain.Event;
import domain.Question;

public class MainBean {
	
	private BLFacade facadeBL;
	private Date data;
	private Event gertaera;
	private Question galdera;
	private static List<Event> gertaerak;
	private static List<Question> galderak;
	
	public MainBean() {
		this.facadeBL = FacadeBean.getBusinessLogic();
		gertaerak = this.facadeBL.getEvents(this.data);
	}
	
	public String getBundleString(String name) {
		return ResourceBundle.getBundle("Etiquetas").getString(name);
	}
	
	public Date getData() {
		return this.data;
	}
	
	public Question getGaldera() {
		return this.galdera;
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
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public void setGaldera(Question galdera) {
		this.galdera = galdera;
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
}
