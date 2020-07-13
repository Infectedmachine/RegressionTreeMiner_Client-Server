package database;

/**
 * Modella l'entità colonna della tabella del database
 * @author Nazar Chekalin
 *
 */
public class Column {
	private String name;
	private String type;

	/**
	 * Costruttore di classe
	 * @param name - nome colonna
	 * @param type - tipo della colonna
	 */
	Column(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Restituisce il nome della colonna
	 * @return String - Stringa contenente il nome della colonna
	 */
	public String getColumnName() {
		return name;
	}

	/**
	 * Restituisce un valore booleano che indica se la colonna è numerica
	 * @return boolean - true, la colonna è numerica - false, altrimenti. 
	 */
	public boolean isNumber() {
		return type.equals("number");
	}

	@Override
	public String toString() {
		return name + ":" + type;
	}
}