package tree;

import data.Data;
import java.io.Serializable;

public class LeafNode extends Node implements Serializable {
	private static final long serialVersionUID = 1L;
	private double predictedClassValue;

	public LeafNode(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
		super(trainingSet, beginExampleIndex, endExampleIndex);
		this.predictedClassValue = avg(trainingSet, beginExampleIndex, endExampleIndex);
	}

	private double avg(Data matrix, int begin, int end) {
		double total = 0;

		for (int i = begin; i < end + 1; i++)
			total += matrix.getClassValue(i);

		return total / (end - begin + 1);
	}

	public double getPredictedClassValue() {
		return this.predictedClassValue;
	}

	protected int getNumberOfChildren() {
		return 0;
	}

	public String toString() {
		String s = "LEAF : " + "class=" + predictedClassValue + " " + super.toString();
		return s;
	}

	public String toRules() {
		String s = " ==> Class=" + predictedClassValue;
		return s;
	}
}
