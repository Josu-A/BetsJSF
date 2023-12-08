package eredua.domeinua;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Erabiltzailea {
    
    @OneToMany(targetEntity = LoginGertaera.class, mappedBy = "erabiltzailea",
               fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<LoginGertaera> gertaerak;
    @Id
    private String izena;
    private String mota;
    private String pasahitza;
    
    public Erabiltzailea() {
        // empty constructor
    }
    
    public Set<LoginGertaera> getGertaerak() {
        return this.gertaerak;
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
    
    public void setGertaerak(Set<LoginGertaera> gertaerak) {
        this.gertaerak = gertaerak;
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
    
    @Override
    public String toString() {
        return String.format("%s/%s/%s", this.izena, this.pasahitza, this.mota);
    }
}
