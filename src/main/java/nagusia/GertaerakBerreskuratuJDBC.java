package nagusia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class GertaerakBerreskuratuJDBC {
	
	public static void main(String[] args) {
		
		try (
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/gertaerak", "root", "admin");
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM LoginGertaera");	
		) {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("LoginGertaera (Id, Deskribapena, Data)");
			while (rs.next()) {
				Long id = Long.valueOf(rs.getLong("Id"));
				String deskribapena = rs.getString("Deskribapena");
				Date data = rs.getDate("Data");
				System.out.println(id + " / " + deskribapena + " / " + data);
			}
		}
		catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
}
