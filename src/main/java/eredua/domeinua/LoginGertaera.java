package eredua.domeinua;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class LoginGertaera {
    
    private Date data;
    private String deskribapena;
    @ManyToOne(fetch = FetchType.EAGER)
    private Erabiltzailea erabiltzailea;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean login;
    
    public LoginGertaera() {
        // Bean empty constructor
    }
    
    public Date getData() {
        return this.data;
    }
    
    public String getDeskribapena() {
        return this.deskribapena;
    }
    
    public Erabiltzailea getErabiltzailea() {
        return this.erabiltzailea;
    }
    
    public Long getId() {
        return this.id;
    }
    
    public boolean isLogin() {
        return this.login;
    }
    
    public void setData(Date data) {
        this.data = data;
    }
    
    public void setDeskribapena(String deskribapena) {
        this.deskribapena = deskribapena;
    }
    
    public void setErabiltzailea(Erabiltzailea erabiltzailea) {
        this.erabiltzailea = erabiltzailea;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setLogin(boolean login) {
        this.login = login;
        String msg = this.login
                ? "k ongi egin du logina"
                : " ogin egiten saiatu da";
        this.deskribapena = this.erabiltzailea.getIzena() + msg;
    }
    
    @Override
    public String toString() {
        return String.format("%s/%s/%s/%s/%b", this.id, this.deskribapena, this.data, this.erabiltzailea, this.login);
    }
}
