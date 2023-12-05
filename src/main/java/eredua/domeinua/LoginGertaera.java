package eredua.domeinua;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LoginGertaera {
	
	private Date data;
	private String deskribapena;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	public LoginGertaera() {
		// Bean empty constructor
	}
	
	public Date getData() {
		return this.data;
	}
	
	public String getDeskribapena() {
		return this.deskribapena;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public void setDeskribapena(String deskribapena) {
		this.deskribapena = deskribapena;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
}
