package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Modella la classe per l'accesso al database
 * @author Nazar Chekalin
 *
 */
public class DbAccess {

	private String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
	private final String DBMS = "jdbc:mysql";
	private final String SERVER = "localhost";
	private final String DATABASE = "MapDB";
	private final String PORT = "3306";
	private final String USER_ID = "MapUser";
	private final String PASSWORD = "map";
	private final String TIMEZONE = "?serverTimezone=UTC";
	private Connection connection;

	/**
	 * Costruttore di classe
	 */
	public DbAccess() {

	}

	/**
	 * Inizializza la connessione al database
	 * @throws DatabaseConnectionException
	 */
	public void initConnection() throws DatabaseConnectionException {
		System.out.println("CONNECTING TO DATABSE...");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			throw new DatabaseConnectionException("Driver.newInstanceException: " + ex.getMessage());
		}
		try {
			Connection conn = DriverManager
					.getConnection(DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE + TIMEZONE, USER_ID, PASSWORD);
			connection = conn;
			System.out.println("DATABASE CONNECTED!");
		} catch (Exception e) {
			throw new DatabaseConnectionException("Connection ERROR to DATABASE:" + e.getMessage());
		}
	}

	/**
	 * Ritorna la sessione della connessione al database.
	 * @return Connection - oggetto di tipo Connection, java.sql.Connection
	 */
	public Connection getConnection() {
		return this.connection;
	}

	/**
	 * Chiude la connessione al database.
	 */
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
	}
}
