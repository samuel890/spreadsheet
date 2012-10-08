package edu.pitt.sis.xiaolin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class Spreadsheet {

	/**
	 * Author: Xiaolin Lin 
	 * Date: 2012-10-07 
	 * Contact: linxiaolin1989@gmail.com
	 *   
	 * You could run these code in terminal
	 * by providing valid input file path and output file path for example: java
	 * -jar Spreadsheet.jar /parent/child/input.csv /parent/child/output.csv
	 */

	public static void main(String[] args) throws Exception {

		if (args.length > 2 || args.length <= 1) {
			System.out.println("Please provide valid input and output file paths!");
			System.out.println("e.g. java -jar Spreadsheet.jar /parent/child/input.csv /parent/child/output.csv");

		}
		String csv_file = args[0];
		String target_file = args[1];
		evaluator(csv_file, target_file);

	}
	
	public static void evaluator(String csv_file, String target_file)
			throws Exception {

		int rows = getMaxRow(csv_file);
		int cols = getMaxColumn(csv_file);
		Cell[][] cells = new Cell[rows][cols];

		cells = computeReference(formatCSV(csv_file));

		writeCells(cells, target_file);

	}

	// compute cell with reference
	public static Cell[][] computeReference(Cell[][] cells) {
		// A-Z dictionary list
		HashMap<String, Integer> letters = new HashMap<String, Integer>();
		for (int value = 1; value <= 26; value++) {
			// e.g. 1+64=65 which is equal 'A' ASCII decimal value
			char c = (char) (value + 64);
			String key = Character.toString(c);
			// e.g key: A; value: 65
			letters.put(key, value);
		}

		// compute the cell with references
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[0].length; j++) {
				if (cells[i][j] != null && cells[i][j].isReference()) {
					// get the letter as column
					String letter = cells[i][j].toString().substring(1, 2);
					// get the number as row
					String number = cells[i][j].toString().substring(2);

					int col = letters.get(letter) - 1;
					int row = Integer.parseInt(number) - 1;
					cells[i][j] = cells[row][col];

				}
			}
		}
		return cells;
	}

	// scan and compute the RPN expression field
	public static Cell[][] formatCSV(String file) throws Exception {

		int rows = getMaxRow(file);
		int cols = getMaxColumn(file);

		int row = 0;
		String str = "";
		String[] tokens;
		Cell[][] cells = new Cell[rows][cols];

		BufferedReader br = new BufferedReader(new FileReader(file));
		while ((str = br.readLine()) != null) {
			// break cell with commas
			tokens = str.split(",");
			for (int col = 0; col < tokens.length; col++) {
				Cell temp = new Cell(tokens[col].trim());
				// if cell is a calculation then use computeRPN method compute
				// it

				if (!temp.isEmpty() && temp.isCalculation()) {
					temp = temp.computeRPN();
				}
				// add cell into array line by line
				cells[row][col] = temp;
			}
			row++;
		}

		return cells;

	}

	// write the cells array out to target_file
	public static void writeCells(Cell[][] cells, String target_file) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[0].length; j++) {
				if (cells[i][j] != null) {
					sb.append(cells[i][j] + "\t");
				} else {
					sb.append("\t");
				}
			}
			sb.append("\n");
		}

		try {
			File file = new File(target_file);
			// if target does not exist, create new one
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sb.toString());
			bw.close();

			System.out.println("Done! Please check " + target_file + " !");

		} catch (Exception e) {
			System.out.println("create " + target_file + " failed\n" + e);
		}
	}

	// get maximum row number
	public static int getMaxRow(String file) throws Exception {

		int rows = 0;
		String str = "";

		BufferedReader br = new BufferedReader(new FileReader(file));
		while ((str = br.readLine()) != null) {
			rows++;
		}

		return rows;

	}

	// get maximum column number
	public static int getMaxColumn(String file) throws Exception {

		int columns = 0, tmp = 0;
		String str = "";
		String[] tokens;

		BufferedReader br = new BufferedReader(new FileReader(file));
		while ((str = br.readLine()) != null) {
			tokens = str.split(",");
			tmp = tokens.length;
			if (tmp > columns)
				columns = tmp;
		}

		return columns;

	}

}
