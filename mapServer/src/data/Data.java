package data;

import database.*;
import java.util.*;
import java.util.Set;
import java.sql.SQLException;

public class Data {

	private Object data[][];
	private int numberOfExamples;
	private List<Attribute> explanatorySet = new LinkedList<Attribute>();
	private ContinuousAttribute classAttribute;
	private List<Example> dataList = new ArrayList<Example>();

	public Data(String table) throws TrainingDataException {
		DbAccess db = new DbAccess();
		try {
			db.initConnection();
			TableData tableData = new TableData(db);
			TableSchema tableSchema = new TableSchema(db, table);
			dataList = tableData.getTransazioni(table);
			this.numberOfExamples = dataList.size();
			initExplanatorySetFromTable(tableData, tableSchema, table);
			initDataFromTable(tableData, tableSchema, table);
			db.closeConnection();
		} catch (Exception ex) {
			throw new TrainingDataException(ex.getMessage());
		}
	}

	public int getNumberOfExamples() {
		return this.numberOfExamples;
	}

	public int getNumberOfExplanatoryAttributes() {
		return this.explanatorySet.size();
	}

	public Double getClassValue(int exampleIndex) {
		return (Double) this.data[exampleIndex][this.getNumberOfExplanatoryAttributes()];
	}

	public Object getExplanatoryValue(int exampleIndex, int attributeIndex) {
		return this.data[exampleIndex][attributeIndex];
	}

	public Attribute getExplanatoryAttribute(int index) {
		return (Attribute) this.explanatorySet.get(index);
	}

	public String toString() {
		String value = "";
		for (int i = 0; i < numberOfExamples; i++) {
			for (int j = 0; j < explanatorySet.size(); j++)
				value += data[i][j] + ",";

			value += data[i][explanatorySet.size()] + "\n";
		}
		return value;

	}

	public void sort(Attribute attribute, int beginExampleIndex, int endExampleIndex) {

		quicksort(attribute, beginExampleIndex, endExampleIndex);
	}

	// scambio esempio i con esempi oj
	private void swap(int i, int j) {
		Object temp;
		for (int k = 0; k < getNumberOfExplanatoryAttributes() + 1; k++) {
			temp = data[i][k];
			data[i][k] = data[j][k];
			data[j][k] = temp;
		}

	}

	/*
	 * Partiziona il vettore rispetto all'elemento x e restiutisce il punto di
	 * separazione
	 */
	private int partition(DiscreteAttribute attribute, int inf, int sup) {
		int i, j;

		i = inf;
		j = sup;
		int med = (inf + sup) / 2;
		String x = (String) getExplanatoryValue(med, attribute.getIndex());
		swap(inf, med);

		while (true) {

			while (i <= sup && ((String) getExplanatoryValue(i, attribute.getIndex())).compareTo(x) <= 0) {
				i++;

			}

			while (((String) getExplanatoryValue(j, attribute.getIndex())).compareTo(x) > 0) {
				j--;

			}

			if (i < j) {
				swap(i, j);
			} else
				break;
		}
		swap(inf, j);
		return j;

	}

	/*
	 * Partiziona il vettore rispetto all'elemento x e restiutisce il punto di
	 * separazione
	 */
	private int partition(ContinuousAttribute attribute, int inf, int sup) {
		int i, j;

		i = inf;
		j = sup;
		int med = (inf + sup) / 2;
		Double x = (Double) getExplanatoryValue(med, attribute.getIndex());
		swap(inf, med);

		while (true) {

			while (i <= sup && ((Double) getExplanatoryValue(i, attribute.getIndex())).compareTo(x) <= 0) {
				i++;

			}

			while (((Double) getExplanatoryValue(j, attribute.getIndex())).compareTo(x) > 0) {
				j--;

			}

			if (i < j) {
				swap(i, j);
			} else
				break;
		}
		swap(inf, j);
		return j;

	}

	/*
	 * Algoritmo quicksort per l'ordinamento di un array di interi A usando come
	 * relazione d'ordine totale "<="
	 * 
	 * @param A
	 */
	private void quicksort(Attribute attribute, int inf, int sup) {

		if (sup >= inf) {

			int pos;
			if (attribute instanceof DiscreteAttribute)
				pos = partition((DiscreteAttribute) attribute, inf, sup);
			else
				pos = partition((ContinuousAttribute) attribute, inf, sup);

			if ((pos - inf) < (sup - pos + 1)) {
				quicksort(attribute, inf, pos - 1);
				quicksort(attribute, pos + 1, sup);
			} else {
				quicksort(attribute, pos + 1, sup);
				quicksort(attribute, inf, pos - 1);
			}

		}

	}

	private void initExplanatorySetFromTable(TableData tableData, TableSchema schema, String table) throws TrainingDataException, SQLException{
		explanatorySet = new LinkedList<Attribute>();
			if (schema.getNumberOfAttributes() < 2)
				throw new TrainingDataException("The DB Table has less than 2 columns!");
			else if (!schema.getColumn(schema.getNumberOfAttributes() - 1).isNumber())
				throw new TrainingDataException("The last column has improper value type!");

			for (int i = 0; i < schema.getNumberOfAttributes() - 1; i++) {
				if (schema.getColumn(i).isNumber()) {
					Attribute a = new ContinuousAttribute(schema.getColumn(i).getColumnName(), i);
					explanatorySet.add(a);
				} else {
					Set<Object> set = tableData.getDistinctColumnValues(table, schema.getColumn(i));
					TreeSet<String> setString = new TreeSet<String>();
					for (Object o : set)
						setString.add((String) o);
					Attribute a = new DiscreteAttribute(schema.getColumn(i).getColumnName(), i, setString);
					explanatorySet.add(a);
				}
			}
			classAttribute = new ContinuousAttribute(
					(schema.getColumn(schema.getNumberOfAttributes() - 1)).getColumnName(),
					schema.getNumberOfAttributes() - 1);
		
	}

	private void initDataFromTable(TableData tableData, TableSchema schema, String table) throws TrainingDataException {
		try {
			List<Example> tuples = tableData.getTransazioni(table);
			int row = tuples.size();
			int col = schema.getNumberOfAttributes();
			this.data = new Object[row][col];

			for (int i = 0; i < row; i++) {
				Example items = tuples.get(i);
				for (int j = 0; j < col; j++)
					if (items.get(j) instanceof Double)
						this.data[i][j] = new Double((Double) items.get(j));
					else
						this.data[i][j] = new String((String) items.get(j));
			}
		} catch (Exception ex) {
			throw new TrainingDataException(ex.getMessage());
		}
	}

}
