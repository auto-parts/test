package sk.igaraz.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class AutoGarageDB {

	private static final String USERNAME = "postgres";
	private static final String PASSWORD = "lopata";
	private static final String URL = "jdbc:postgresql://localhost:5432/autogarage";
	protected static final String DRIVER = "org.postgresql.Driver";

	public static Connection getConnection() throws Exception {
		Class.forName(DRIVER);
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}


}
