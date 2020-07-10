package tree;

import data.*;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;

public class RegressionTree implements Serializable {
	private static final long serialVersionUID = 1L;
	private Node root;
	private RegressionTree childTree[];
	private Node predictionNode;

	public RegressionTree() {

	}

	public RegressionTree(Data trainingSet) {

		learnTree(trainingSet, 0, trainingSet.getNumberOfExamples() - 1, trainingSet.getNumberOfExamples() * 10 / 100);
	}

	private void learnTree(Data trainingSet, int begin, int end, int numberOfExamplesPerLeaf) {
		if (isLeaf(trainingSet, begin, end, numberOfExamplesPerLeaf)) {
			root = new LeafNode(trainingSet, begin, end);
		} else // split node
		{
			root = determineBestSplitNode(trainingSet, begin, end);

			if (root.getNumberOfChildren() > 1) {
				childTree = new RegressionTree[root.getNumberOfChildren()];
				for (int i = 0; i < root.getNumberOfChildren(); i++) {
					childTree[i] = new RegressionTree();
					childTree[i].learnTree(trainingSet, ((SplitNode) root).getSplitInfo(i).beginIndex,
							((SplitNode) root).getSplitInfo(i).endIndex, numberOfExamplesPerLeaf);
				}
			} else
				root = new LeafNode(trainingSet, begin, end);

		}
	}

	private boolean isLeaf(Data trainingSet, int begin, int end, int numberOfExamplesPerLeaf) {
		if ((end - begin + 1) <= numberOfExamplesPerLeaf)
			return true;
		else
			return false;
	}

	private SplitNode determineBestSplitNode(Data trainingSet, int begin, int end) {
		// Node discArray[] = new Node[trainingSet.getNumberOfExplanatoryAttributes()];
		TreeSet<Node> nodes = new TreeSet<Node>();
		Node currentNode;

		for (int i = 0; i < trainingSet.getNumberOfExplanatoryAttributes(); i++) {
			Attribute a = trainingSet.getExplanatoryAttribute(i);
			if (a instanceof DiscreteAttribute) {
				currentNode = new DiscreteNode(trainingSet, begin, end, (DiscreteAttribute) a);
			} else {
				currentNode = new ContinuousNode(trainingSet, begin, end, (ContinuousAttribute) a);
			}
			// Node discrete = new DiscreteNode(trainingSet, begin, end,
			// (DiscreteAttribute) trainingSet.getExplanatoryAttribute(i));
			// discArray[i] = discrete;
			nodes.add(currentNode);
		}

		/*
		 * int best = 0; for (int i = 1; i < discArray.length; i++) if
		 * (discArray[best].getVariance() > discArray[i].getVariance()) best = i;
		 */

		trainingSet.sort(((SplitNode) nodes.first()).getAttribute(), begin, end);
		return (SplitNode) nodes.first();
		// return (SplitNode) discArray[best];
	}

	public void printTree() {
		System.out.println("********* TREE **********\n");
		System.out.println(toString());
		System.out.println("*************************\n");
	}

	public String toString() {
		String tree = root.toString() + "\n";

		if (root instanceof LeafNode) {

		} else // split node
		{
			for (int i = 0; i < childTree.length; i++)
				tree += childTree[i];
		}
		return tree;
	}

	public void printRules() {
		System.out.println("********* RULES **********\n");
		toRules();
		System.out.println("*************************\n");

	}

	private void toRules() {
		if (this.root instanceof DiscreteNode) {
			for (int i = 0; i < this.childTree.length; i++) {
				String rules = "";
				rules += ((DiscreteNode) root).getAttribute() + "=";
				rules += ((DiscreteNode) root).getSplitInfo(i).getSplitValue();
				this.childTree[i].toRules(rules);
			}
		} else if (this.root instanceof ContinuousNode) {
			for (int i = 0; i < this.childTree.length; i++) {
				String rules = "";
				rules += ((ContinuousNode) root).getAttribute()
						+ ((ContinuousNode) root).getSplitInfo(i).getComparator();
				rules += ((ContinuousNode) root).getSplitInfo(i).getSplitValue();
				this.childTree[i].toRules(rules);
			}
		}
	}

	private void toRules(String rules) {
		if (this.root instanceof DiscreteNode) {
			rules += " AND " + ((DiscreteNode) root).getAttribute() + "=";
			for (int i = 0; i < this.childTree.length; i++) {
				String rulesCopy = rules;
				rulesCopy += ((DiscreteNode) root).getSplitInfo(i).getSplitValue();
				this.childTree[i].toRules(rulesCopy);
			}
		} else if (this.root instanceof ContinuousNode) {
			rules += " AND " + ((ContinuousNode) root).getAttribute();
			for (int i = 0; i < this.childTree.length; i++) {
				String rulesCopy = rules;
				rulesCopy += ((ContinuousNode) root).getSplitInfo(i).getComparator()
						+ ((ContinuousNode) root).getSplitInfo(i).getSplitValue();
				this.childTree[i].toRules(rulesCopy);
			}
		} else if (this.root instanceof LeafNode) {
			rules += ((LeafNode) root).toRules();
			System.out.println(rules);
		}
	}

	public void predictClass(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {

		if (root instanceof LeafNode) {
			out.writeObject("OK");
			out.writeObject(((LeafNode) root).getPredictedClassValue());
		} else {
			int choice;
			out.writeObject("QUERY");
			out.writeObject(((SplitNode) root).formulateQuery());

			choice = (Integer) in.readObject();
			if (choice == -1 || choice >= root.getNumberOfChildren()) {
				out.writeObject(
						"The answer should be an integer between 0 and " + (root.getNumberOfChildren() - 1) + "!");
			} else {
				childTree[choice].predictClass(in, out);
			}

		}
	}

	public void save(String fileName) throws FileNotFoundException, IOException {
		FileOutputStream outFile = new FileOutputStream(fileName);
		ObjectOutputStream outStream = new ObjectOutputStream(outFile);
		outStream.writeObject(root);
		outStream.writeObject(childTree);
		outStream.close();
		outFile.close();
	}

	public static RegressionTree load(String fileName)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		RegressionTree tree = new RegressionTree();
		FileInputStream inFile = new FileInputStream(fileName);
		ObjectInputStream inStream = new ObjectInputStream(inFile);
		tree.root = (Node) inStream.readObject();
		tree.childTree = (RegressionTree[]) inStream.readObject();
		return tree;

	}

}
