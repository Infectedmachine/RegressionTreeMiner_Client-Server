package tree;

import data.Data;
import java.io.Serializable;

/**
 * Modella la classe Nodo Foglia, estende la classe Node
 * @author Nazar Chekalin
 *
 */
public class LeafNode extends Node implements Serializable {
	private static final long serialVersionUID = 1L;
	private double predictedClassValue;

	/**
	 * Costruttore di classe, inizializza i parametri membro
	 * @param trainingSet - Oggetto di tipo Data contenente il training Set
	 * @param beginExampleIndex - indice inizio 
	 * @param endExampleIndex - indice fine
	 */
	public LeafNode(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
		super(trainingSet, beginExampleIndex, endExampleIndex);
		this.predictedClassValue = avg(trainingSet, beginExampleIndex, endExampleIndex);
	}

	/**
	 * Calcola la media dei valori
	 * @param matrix - oggetto di tipo Data contenente il training Set
	 * @param begin - indice inizio
	 * @param end - indice fine
	 * @return double - valore numerico della media
	 */
	private double avg(Data matrix, int begin, int end) {
		double total = 0;

		for (int i = begin; i < end + 1; i++)
			total += matrix.getClassValue(i);

		return total / (end - begin + 1);
	}

	/**
	 * Restituisce il valore di classe
	 * @return double - valore di classe
	 */
	public double getPredictedClassValue() {
		return this.predictedClassValue;
	}

	/**
	 * Restituisce il numero dei figli sotto forma di intero
	 */
	protected int getNumberOfChildren() {
		return 0;
	}

	public String toString() {
		String s = "LEAF : " + "class=" + predictedClassValue + " " + super.toString();
		return s;
	}

	/**
	 * Restituisce una stringa contenente il valore di predizione di classe
	 * @return String
	 */
	public String toRules() {
		String s = " ==> Class=" + predictedClassValue;
		return s;
	}
}
