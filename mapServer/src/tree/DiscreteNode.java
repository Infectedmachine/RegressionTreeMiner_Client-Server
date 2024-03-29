package tree;

import java.util.*;
import data.*;
import java.io.Serializable;

/**
 * Modella la classe Attributo Discreto, estende la classe SplitNode
 * @author Nazar Chekalin
 *
 */
public class DiscreteNode extends SplitNode implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore di classe, invoca il costruttore della super-classe SplitNode.
	 * @param trainingSet - oggetto di tipo Data
	 * @param beginExampleIndex - indice di rigo inizio
	 * @param endExampleIndex - indice di rigo fine
	 * @param attribute - attributo della tabella del training set
	 */
	public DiscreteNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, DiscreteAttribute attribute) {
		super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
	}

	protected void setSplitInfo(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {

		TreeSet<String> distSet = new TreeSet<String>();

		for (int i = beginExampleIndex; i < endExampleIndex + 1; i++)
			distSet.add((String) trainingSet.getExplanatoryValue(i, attribute.getIndex()));

		int begin = beginExampleIndex;
		int end = begin;
		boolean flag = true;
		int row = begin;
		int counter = 0;

		for (String value : distSet) {
			begin = row;
			flag = true;
			while (flag && row < endExampleIndex + 1) {
				String matrixValue = (String) trainingSet.getExplanatoryValue(row, attribute.getIndex());
				if (value.equals(matrixValue))
					row++;
				else
					flag = false;
			}
			end = row - 1;
			SplitInfo split = new SplitInfo(value, begin, end, counter++);
			super.mapSplit.add(split);
		}
	}

	protected int testCondition(Object value) {
		for (int i = 0; i < super.mapSplit.size(); i++)
			if (super.mapSplit.get(i).getSplitValue() == value)
				return i;
		return -1;
	}

	public String toString() {
		return super.toString();
	}
}
