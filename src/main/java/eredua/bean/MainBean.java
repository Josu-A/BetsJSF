package eredua.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import domain.Event;

public class MainBean {
	
	private BLFacade facadeBL;
	private Date data;
	private Event gertaera;
	private static List<Event> gertaerak;
	
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
	
	public Event getGertaera() {
		return this.gertaera;
	}
	
	public List<Event> getGertaerak() {
		return gertaerak;
	}
	
	public static Event getObject(String description) {
		for (Event e : gertaerak) {
			if (description.equals(e.getDescription())) {
				return e;
			}
		}
		return null;
	}
	
	public void onDateSelect(SelectEvent event) {
		gertaerak = this.facadeBL.getEvents(this.data);
		System.out.println(gertaerak.get(0).getDescription());
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Data aukeraua: " + event.getObject()));
	}
	
	public void onGertaeraSelect(SelectEvent event) {
		this.gertaera = (Event) event.getObject();
		FacesContext.getCurrentInstance().addMessage("nireForm:mezuak",
				new FacesMessage("Gertaera (taula): " + this.gertaera.getEventNumber()
						+ "/" + this.gertaera.getDescription()));
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public void setGertaera(Event gertaera) {
		this.gertaera = gertaera;
	}
	
	public void setGertaerak(List<Event> gL) {
		gertaerak = gL;
	}
}
