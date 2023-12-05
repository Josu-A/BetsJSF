package eredua.domeinua;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Erabiltzailea {
    
    @Id
    private String izena;
    private String mota;
    private String pasahitza;
    
    public Erabiltzailea() {
        // empty constructor
    }
    
    public String getIzena() {
        return this.izena;
    }
    
    public String getMota() {
        return this.mota;
    }
    
    public String getPasahitza() {
        return this.pasahitza;
    }
    
    public void setIzena(String izena) {
        this.izena = izena;
    }
    
    public void setMota(String mota) {
        this.mota = mota;
    }
    
    public void setPasahitza(String pasahitza) {
        this.pasahitza = pasahitza;
    }
}
