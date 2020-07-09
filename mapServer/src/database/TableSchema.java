package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;

public class TableSchema implements Iterable<Column> {
	List<Column> tableSchema = new ArrayList<Column>();

	public TableSchema(DbAccess db, String tableName) throws SQLException {

		HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();
		// http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR", "string");
		mapSQL_JAVATypes.put("VARCHAR", "string");
		mapSQL_JAVATypes.put("LONGVARCHAR", "string");
		mapSQL_JAVATypes.put("BIT", "string");
		mapSQL_JAVATypes.put("SHORT", "number");
		mapSQL_JAVATypes.put("INT", "number");
		mapSQL_JAVATypes.put("LONG", "number");
		mapSQL_JAVATypes.put("FLOAT", "number");
		mapSQL_JAVATypes.put("DOUBLE", "number");

		Connection con = db.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet res = meta.getTables(null, null, tableName, null);
		if (res.next()) {
			res = meta.getColumns(null, null, tableName, null);

			while (res.next()) {

				if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
					tableSchema.add(
							new Column(res.getString("COLUMN_NAME"), mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));

			}
		} else {
			res.close();
			throw new SQLException("Table doesn't exist!");
		}
		res.close();

	}

	public int getNumberOfAttributes() {
		return tableSchema.size();
	}

	public Column getColumn(int index) {
		return tableSchema.get(index);
	}

	public Iterator<Column> iterator() {
		return tableSchema.iterator();
	}

}