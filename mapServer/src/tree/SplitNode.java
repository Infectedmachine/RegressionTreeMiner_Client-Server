package tree;

import data.*;
import java.util.*;
import java.io.Serializable;

abstract class SplitNode extends Node implements Comparable<SplitNode>, Serializable {
	private static final long serialVersionUID = 1L;
	class SplitInfo implements Serializable{
		private static final long serialVersionUID = 1L;
		Object splitValue;
		int beginIndex;
		int endIndex;
		int numberChild;
		String comparator = "=";

		SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild) {
			this.splitValue = splitValue;
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;
			this.numberChild = numberChild;
		}

		SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild, String comparator) {
			this.splitValue = splitValue;
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;
			this.numberChild = numberChild;
			this.comparator = comparator;
		}

		int getBeginindex() {
			return beginIndex;
		}

		int getEndIndex() {
			return endIndex;
		}

		Object getSplitValue() {
			return splitValue;
		}

		public String toString() {
			return "child " + numberChild + " split value" + comparator + splitValue + "[Examples:" + beginIndex + "-"
					+ endIndex + "]";
		}

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

	public Attribute getAttribute() {
		return attribute;
	}

	public double getVariance() {
		return splitVariance;
	}

	public int getNumberOfChildren() {
		return mapSplit.size();
	}

	public SplitInfo getSplitInfo(int child) {
		return mapSplit.get(child);
	}

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

	public int compareTo(SplitNode o) {
		if (this.splitVariance == o.splitVariance)
			return 0;
		else if (this.splitVariance > o.splitVariance)
			return 1;
		else
			return -1;
	}
}
