package edu.pitt.sis.xiaolin;

public class Cell {
	public String cell;

	public Cell(String cell) {
		this.cell = cell;
	}
	
	public String toString() {
		if (cell == null || cell.isEmpty()) {
			return " ";
		} else {
			return cell;
		}
	}

	//
	public boolean isReference() {
		// contain a word like =A1
		if (cell.startsWith("="))
			return true;
		return false;
	}

	public boolean isCalculation() {
		//if the cell is postfix expression, it should be end with operator
		if (cell.endsWith("+") || cell.endsWith("-") || cell.endsWith("*")
				|| cell.endsWith("/"))
			return true;
		return false;
	}

	public boolean isEmpty() {
		if (this.cell.isEmpty())
			return true;
		return false;
	}

	//compute postfix expression
	public Cell computeRPN() {
		double result = 0;
		if (this.isCalculation()) {

			String[] tokens = this.cell.substring(1).split(" ");
			//first value
			double x = Double.parseDouble(tokens[0]);
			//second value
			double y = Double.parseDouble(tokens[1]);
			
			if (tokens[2].equals("+")) {
				result = x + y;
			} else if (tokens[2].equals("-")) {
				result = x - y;
			} else if (tokens[2].equals("*")) {
				result = x * y;
			} else if (tokens[2].equals("/")) {
				result = x / y;
			}

		}
		return new Cell(Double.toString(result));
	}	
}
