package nagusia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import eredua.domeinua.Erabiltzailea;

public class GertaerakBerreskuratuJDBC {
    
    private static final Logger logger = Logger.getLogger(GertaerakBerreskuratuJDBC.class.getName());
    
    public GertaerakBerreskuratuJDBC() {
        try {
            FileHandler fileHandler = new FileHandler("logs/mylog.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            addShutdownHook(fileHandler);
        }
        catch (SecurityException | IOException e) {
            logger.log(Level.SEVERE, "Error initializing logging", e);
        }
    }
    
    private static void addShutdownHook(FileHandler fileHandler) {
        Runtime.getRuntime().addShutdownHook(new Thread(fileHandler::close));
    }
    
    public static String getErabiltzaileaJDBC(Erabiltzailea e) {
        String msg = "--/--/--";
        try (
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost/gertaerak", "root", "admin");
            PreparedStatement s = c.prepareStatement("SELECT * FROM Erabiltzailea WHERE Izena=?");
        ) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            s.setString(1, e.getIzena());
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                msg = String.format("%s/%s/%s", rs.getString("Izena"), rs.getString("Pasahitza"), rs.getString("Mota"));    
            }
            rs.close();
        }
        catch (SQLException | ClassNotFoundException ex) {
            logger.log(Level.SEVERE, ex.toString());
        }
        return msg;
    }
    
    public static void main(String[] args) {
        
        try (
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost/gertaerak", "root", "admin");
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM LoginGertaera");    
        ) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.log(Level.INFO, "LoginGertaera (Id, Deskribapena, Data)");
            while (rs.next()) {
                Long id = Long.valueOf(rs.getLong("Id"));
                String deskribapena = rs.getString("Deskribapena");
                Date data = rs.getDate("Data");
                logger.log(Level.INFO, () -> String.format("%s / %s / %s", id, deskribapena, data));
            }
        }
        catch (SQLException | ClassNotFoundException ex) {
        	logger.log(Level.SEVERE, ex.toString());
        }
    }
}
