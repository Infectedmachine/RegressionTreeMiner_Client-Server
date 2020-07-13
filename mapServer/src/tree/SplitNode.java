package tree;

import data.*;
import java.util.*;
import java.io.Serializable;

/**
 * Modella la classe SplitNode che estende la classe Node
 * @author Nazar Chekalin
 *
 */
abstract class SplitNode extends Node implements Comparable<SplitNode>, Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Modella la sotto classe SplitInfo 
	 * @author Nazar Chekalin
	 *
	 */
	class SplitInfo implements Serializable{
		private static final long serialVersionUID = 1L;
		Object splitValue;
		int beginIndex;
		int endIndex;
		int numberChild;
		String comparator = "=";

		/**
		 * Costruttore di classe, inizializza i parametri di classe
		 * @param splitValue - valore
		 * @param beginIndex - indice inizio split
		 * @param endIndex - indice fine split
		 * @param numberChild - numero dei figli
		 */
		SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild) {
			this.splitValue = splitValue;
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;
			this.numberChild = numberChild;
		}

		/**
		 * Costruttore di classe, inizializza i parametri di classe
		 * @param splitValue - valore
		 * @param beginIndex - indice inizio split
		 * @param endIndex - indice fine split
		 * @param numberChild - numero di figli
		 * @param comparator - Stringa contenendo il comparatore
		 */
		SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild, String comparator) {
			this.splitValue = splitValue;
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;
			this.numberChild = numberChild;
			this.comparator = comparator;
		}

		/**
		 * Restituisce l'indice inizio split
		 * @return int - intero che indica l'indice
		 */
		int getBeginindex() {
			return beginIndex;
		}

		/**
		 * Restituisce l'indice fine split
		 * @return int - intero che indica l'indice
		 */
		int getEndIndex() {
			return endIndex;
		}

		/**
		 * Restituisce il valore del nodo
		 * @return Object - oggetto valore
		 */
		Object getSplitValue() {
			return splitValue;
		}

		public String toString() {
			return "child " + numberChild + " split value" + comparator + splitValue + "[Examples:" + beginIndex + "-"
					+ endIndex + "]";
		}

		/**
		 * Restituisce il comparatore
		 * @return String - stringa contenente il comparatore
		 */
		String getComparator() {
			return comparator;
		}

	}

	protected Attribute attribute;
	protected List<SplitInfo> mapSplit = new ArrayList<SplitInfo>();
	protected double splitVariance;

	protected abstract void setSplitInfo(Data trainingSet, int beginExampelIndex, int endExampleIndex,
			Attribute attribute);

	protected abstract int testCondition(Object value);

	/**
	 * Costruttore di classe, inizializza gli attributi membro
	 * @param trainingSet - Oggetto di tipo Data contenente il training set
	 * @param beginExampleIndex - indice rigo inizio
	 * @param endExampleIndex - indice rigo fine
	 * @param attribute - attributo
	 */
	public SplitNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {
		super(trainingSet, beginExampleIndex, endExampleIndex);
		this.attribute = attribute;
		trainingSet.sort(attribute, beginExampleIndex, endExampleIndex); // order by attribute
		setSplitInfo(trainingSet, beginExampleIndex, endExampleIndex, attribute);

		// compute variance
		splitVariance = 0;
		for (int i = 0; i < mapSplit.size(); i++) {
			double localVariance = new LeafNode(trainingSet, mapSplit.get(i).getBeginindex(),
					mapSplit.get(i).getEndIndex()).getVariance();
			splitVariance += (localVariance);
		}
	}

	/**
	 * Restituisce l'attributo del nodo
	 * @return Attribute
	 */
	public Attribute getAttribute() {
		return attribute;
	}

	/**
	 * Restituisce la varianza dello split
	 */
	public double getVariance() {
		return splitVariance;
	}

	/**
	 * Restituisce il numero dei figli del nodo
	 */
	public int getNumberOfChildren() {
		return mapSplit.size();
	}

	/**
	 * Restituisce il figlio del nodo in base all'indice del figlio
	 * @param child - indice del figlio
	 * @return SplitInfo - figlio del nodo pari in posizione dell'indice
	 */
	public SplitInfo getSplitInfo(int child) {
		return mapSplit.get(child);
	}

	/**
	 * Restituisce una stringa contenente i figlio del nodo con le relative informazioni concatenate
	 * @return String
	 */
	public String formulateQuery() {
		String query = "";
		for (int i = 0; i < mapSplit.size(); i++)
			query += (i + ":" + attribute + mapSplit.get(i).getComparator() + mapSplit.get(i).getSplitValue()) + "\n";
		return query;
	}

	public String toString() {
		String v = "";
		if (this instanceof DiscreteNode)
			v += "DISCRETE ";
		else 
			v += "CONTINUOUS ";
		v += "SPLIT : attribute=" + attribute + " " + super.toString() + " Split Variance: " + getVariance()
				+ "\n";

		for (int i = 0; i < mapSplit.size(); i++) {
			v += "\t" + mapSplit.get(i) + "\n";
		}

		return v;
	}

	/**
	 * Effettua una comparazione dei nodi di split basata sulla varianza dello split.
	 * Ritorna 0 se sono uguali, 1 se il nodo in input ha varianza minore, -1 varianza maggiore del nodo in input.
	 *
	 */
	public int compareTo(SplitNode o) {
		if (this.splitVariance == o.splitVariance)
			return 0;
		else if (this.splitVariance > o.splitVariance)
			return 1;
		else
			return -1;
	}
}
