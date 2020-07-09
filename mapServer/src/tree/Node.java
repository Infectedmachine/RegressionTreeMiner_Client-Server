package tree;
import data.Data;
import java.io.Serializable;

public abstract class Node implements Serializable{
	private static final long serialVersionUID = 1L;
	protected static int idNoteCount = 0;
	private int idNode;
	private int beginExampleIndex;
	private int endExampleIndex;
	private double variance;

	protected Node(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
		this.beginExampleIndex = beginExampleIndex;
		this.endExampleIndex = endExampleIndex;
		this.variance = calculateVariance(trainingSet, beginExampleIndex, endExampleIndex);
	}

	public int getIdNode() {
		return this.idNode;
	}

	public int getBeginExampleIndex() {
		return this.beginExampleIndex;
	}

	public int getEndExampleIndex() {
		return this.endExampleIndex;
	}

	public double getVariance() {
		return this.variance;
	}

	protected abstract int getNumberOfChildren();

	public String toString() {
		String string = "Node: [Examples: " + String.valueOf(this.beginExampleIndex) + "-"
				+ String.valueOf(this.endExampleIndex) + "] variance: " + String.valueOf(this.variance);

		return string;
	}

	private double calculateVariance(Data matrix, int begin, int end) {

		double value = totalByPower(matrix, begin, end, new CalculatePower());
		double variance = 0;
		int interval = (end + 1) - begin;

		variance = totalByPower(matrix, begin, end, new CalculatePower(2)) - ((value * value) / interval);
		return variance;
	}

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

	private interface iPower {
		public double power(double value);
	}

	private class CalculatePower implements iPower {
		private int times = 0;

		public CalculatePower() {
			this.times = 1;
		}

		public CalculatePower(int times) {
			this.times = times;
		}

		public double power(double value) {
			int times = this.times;

			while (--times > 0)
				value *= value;

			return value;
		}
	}
}
