package tree;
import data.Data;
import java.io.Serializable;

/**
 * modella la classe astratta Nodo
 * @author Nazar Chekalin
 *
 */
public abstract class Node implements Serializable{
	private static final long serialVersionUID = 1L;
	protected static int idNoteCount = 0;
	private int idNode;
	private int beginExampleIndex;
	private int endExampleIndex;
	private double variance;

	/**
	 * Costruttore di classe, inizializza le variabili membro
	 * @param trainingSet - oggetto di tipo Data contenente il training set
	 * @param beginExampleIndex - indice inizio
	 * @param endExampleIndex - indice fine
	 */
	protected Node(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
		this.beginExampleIndex = beginExampleIndex;
		this.endExampleIndex = endExampleIndex;
		this.variance = calculateVariance(trainingSet, beginExampleIndex, endExampleIndex);
	}

	/**
	 * restituisce l'id del nodo
	 * @return intero
	 */
	public int getIdNode() {
		return this.idNode;
	}

	/**
	 * Restituisce l'indice inizio
	 * @return intero
	 */
	public int getBeginExampleIndex() {
		return this.beginExampleIndex;
	}

	/**
	 * Restituisce l'indice fine
	 * @return intero
	 */
	public int getEndExampleIndex() {
		return this.endExampleIndex;
	}

	/**
	 * Restituisce la varianza del nodo
	 * @return double - valore numerico in virgola mobile
	 */
	public double getVariance() {
		return this.variance;
	}

	protected abstract int getNumberOfChildren();

	public String toString() {
		String string = "Node: [Examples: " + String.valueOf(this.beginExampleIndex) + "-"
				+ String.valueOf(this.endExampleIndex) + "] variance: " + String.valueOf(this.variance);

		return string;
	}

	/**
	 * Calcola la varianza del nodo
	 * @param matrix - oggetto Data contenente il training set
	 * @param begin - indice inizio
	 * @param end - indice fine
	 * @return double - valore numerico
	 */
	private double calculateVariance(Data matrix, int begin, int end) {

		double value = totalByPower(matrix, begin, end, new CalculatePower());
		double variance = 0;
		int interval = (end + 1) - begin;

		variance = totalByPower(matrix, begin, end, new CalculatePower(2)) - ((value * value) / interval);
		return variance;
	}

	/**
	 * Calcola la sommatoria dei valori di classe elevati ad una potenza indicata
	 * @param matrix - oggetti Data contenente il training set
	 * @param begin - indice inizio riga
	 * @param end - indice fine riga
	 * @param valueByPower - istanza dell'oggetto che calcola la potenza di un numero
	 * @return double - risulato sommatoria
	 */
	private double totalByPower(Data matrix, int begin, int end, iPower valueByPower) {

		double total = 0;
		double value = 0;
		int attributeIndex = matrix.getNumberOfExplanatoryAttributes();

		for (int i = begin; i < end + 1; i++) {
			value = (double) matrix.getExplanatoryValue(i, attributeIndex);
			total += valueByPower.power(value);
		}
		return total;

	}

	/**
	 * Modella l'interfaccia iPower che contiene un unico metodo power che calcola la potenza di un dato valore numerico.
	 * @author Nazar Chekalin
	 *
	 */
	private interface iPower {
		public double power(double value);
	}

	/**
	 * Modella la classe che implementa e realizza l'interfaccia iPower e il metodo power. 
	 * @author Nazar Chekalin
	 *
	 */
	private class CalculatePower implements iPower {
		private int times = 0;

		/**
		 * Costruttore di classe senza parametri, inizializza la potenza a 1. 
		 */
		public CalculatePower() {
			this.times = 1;
		}

		/**
		 * Costruttore di classe, inizializza la potenza al valore specificato
		 * @param times - potenza del valore numerico
		 */
		public CalculatePower(int times) {
			this.times = times;
		}

		/**
		 * Calcola la potenza di un valore numerico passato come parametro
		 */
		public double power(double value) {
			int times = this.times;

			while (--times > 0)
				value *= value;

			return value;
		}
	}
}
