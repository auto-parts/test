package sk.igaraz.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class TecdocDb {

	private static final String USERNAME = "";
	private static final String PASSWORD = "";
	private static final String URL = "jdbc:odbc:tecdoc";
	private static final String DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver";

	public static Connection getConnection() throws Exception {
		String driver = DRIVER;
		String url = URL;
		String username = USERNAME;
		String password = PASSWORD;
		Class.forName(driver);
		return DriverManager.getConnection(url, username, password);
	}

}
