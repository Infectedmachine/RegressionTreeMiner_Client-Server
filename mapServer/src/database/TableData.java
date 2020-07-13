package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Modella l'insieme delle tuple del training set
 * @author Nazar Chekalin
 *
 */
public class TableData {

	DbAccess db;

	/**
	 * Costruttore di classe
	 * @param db - Oggetto istanza DbAccess
	 */
	public TableData(DbAccess db) {
		this.db = db;
	}

	/**
	 * Restituisce una lista di tuple del training Set
	 * @param table - nome della tabella
	 * @return List - lista delle tuple
	 * @throws SQLException
	 * @throws EmptySetException
	 */
	public List<Example> getTransazioni(String table) throws SQLException, EmptySetException {
		LinkedList<Example> transSet = new LinkedList<Example>();
		Statement statement;
		TableSchema tSchema = new TableSchema(db, table);

		String query = "select ";

		for (int i = 0; i < tSchema.getNumberOfAttributes(); i++) {
			Column c = tSchema.getColumn(i);
			if (i > 0)
				query += ",";
			query += c.getColumnName();
		}
		if (tSchema.getNumberOfAttributes() == 0)
			throw new SQLException();
		query += (" FROM " + table);

		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		boolean empty = true;
		while (rs.next()) {
			empty = false;
			Example currentTuple = new Example();
			for (int i = 0; i < tSchema.getNumberOfAttributes(); i++)
				if (tSchema.getColumn(i).isNumber())
					currentTuple.add(rs.getDouble(i + 1));
				else
					currentTuple.add(rs.getString(i + 1));
			transSet.add(currentTuple);
		}
		rs.close();
		statement.close();
		if (empty)
			throw new EmptySetException();

		return transSet;

	}

	/**
	 * Restituisce un Set contenente i distinti valori di una colonna
	 * @param table - nome tabella del database
	 * @param column - Oggetto di tipo Column
	 * @return Set - set di valori distinti della colonna
	 * @throws SQLException
	 */
	public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
		Set<Object> valueSet = new TreeSet<Object>();
		Statement statement;
		TableSchema tSchema = new TableSchema(db, table);

		String query = "select distinct ";

		query += column.getColumnName();

		query += (" FROM " + table);

		query += (" ORDER BY " + column.getColumnName());

		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		while (rs.next()) {
			if (column.isNumber())
				valueSet.add(rs.getDouble(1));
			else
				valueSet.add(rs.getString(1));

		}
		rs.close();
		statement.close();

		return valueSet;

	}

}
