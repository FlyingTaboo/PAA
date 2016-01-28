package cz.cvut.fit.paa.effenberger.first;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BackPack {
	private static ArrayList<File> files = new ArrayList<File>();
	private static ArrayList<Problem> problems = new ArrayList<Problem>();

	public static void main(String[] args) throws Exception {
		readFiles(new File("./data/input/in60-348.txt"));
		for (int i = 0; i < files.size(); i++) {
			readFileAndSolve(files.get(i));
		}
		solveAllProblems();
	}

	public static void solveProblem(Problem problem, double pocatecniTeplota, double koeficientOchlazeni,
			double minimalniTeplota, double koeficientEquilibrum, double koeficientRelaxace, Result result) {
		Solver s = new Solver(problem.getSize(), problem);
		double solution = 0;
		long start = System.currentTimeMillis();
		int repeat = 1;
		for (int i = 0; i < repeat; i++) {
			solution += s.findCoolingResult(pocatecniTeplota, koeficientOchlazeni, minimalniTeplota,
					koeficientEquilibrum, koeficientRelaxace);
		}
		long end = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();

		result.addNew(problem.getSatisfiedCount(), problem.getAllCount(), solution, end - start);
		sb.append(problem.getId()).append("\t");
		sb.append(problem.getSatisfiedCount()).append("/").append(problem.getAllCount()).append("\t\t");
		sb.append(solution / repeat).append("\t\t");
		sb.append(end - start).append("\t");
		sb.append(pocatecniTeplota).append("\t");
		sb.append(koeficientOchlazeni).append("\t");
		sb.append(minimalniTeplota).append("\t");
		sb.append(koeficientEquilibrum).append("\t");
		sb.append(koeficientRelaxace).append("\t");
		// System.out.println(sb.toString());
		// System.exit(0);
		/*
		 * int ID = Integer.parseInt(lineArray[0]); int resultSize =
		 * Integer.parseInt(lineArray[1]); int max_weight =
		 * Integer.parseInt(lineArray[2]); ArrayList<Item> items = new
		 * ArrayList<Item>(); for (int i = 0; i < resultSize; i++) { Item item =
		 * new Item(Integer.parseInt(lineArray[i * 2 + 4]),
		 * Integer.parseInt(lineArray[i * 2 + 3]), i); items.add(item); }
		 * 
		 * Solver solverSC = new Solver(ID, resultSize, max_weight, items, 0.0);
		 * 
		 * Instant startSC = Instant.now();
		 * solverSC.setCoolingAttributes(pocatecniTeplota, koeficientOchlazeni,
		 * minimalniTeplota, koeficientEquilibrum);
		 * 
		 * Result resultSC = solverSC.findCoolingResult(); Instant endSC =
		 * Instant.now();
		 * 
		 * String out = resultSize + ";\t" + ID + ";\t"; // out +=
		 * resultDynamic.getTotalPrice() + " vs " + // resultSC.getTotalPrice()
		 * + ";\t"; out += Duration.between(startSC, endSC).toMillis() + ";\t";
		 * // out += resultSC.getExpandovano(); out += pocatecniTeplota + ";\t"
		 * + koeficientOchlazeni + ";\t" + minimalniTeplota + ";\t" +
		 * koeficientEquilibrum + ";\t"; out += resultSC.getTotalPrice();
		 * System.out.println(out);
		 */
	}

	public static void readFiles(File node) {
		if (node.isDirectory()) {
			String[] subFiles = node.list();
			for (String filename : subFiles) {
				readFiles(new File(node, filename));
			}
		} else {
			files.add(node);
		}
	}

	public static void readFileAndSolve(File file) {
		BufferedReader br = null;

		try {
			String line;
			br = new BufferedReader(new FileReader(file));
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			String[] array = line.split(" ");
			int size = Integer.parseInt(array[2]);
			int count = Integer.parseInt(array[3]);

			line = br.readLine();
			array = line.split(" ");
			int[] prices = getPrice(array);
			ArrayList<Formula> formulas = new ArrayList<Formula>();
			for (int i = 0; i < count; i++) {
				line = br.readLine();
				array = line.split(" ");
				int first = Integer.parseInt(array[0]);
				int second = Integer.parseInt(array[1]);
				int third = Integer.parseInt(array[2]);
				addVariables(formulas, first, second, third);
			}

			Problem p = new Problem(formulas, file.getName(), size);
			p.setPrices(prices);

			problems.add(p);
			// solveProblem(500, 0.90, 1, 0.5, 0.95);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static int[] getPrice(String[] array) {
		int[] result = new int[array.length - 1];
		for (int i = 0; i < array.length - 1; i++) {
			result[i] = Integer.parseInt(array[i + 1]);
		}
		return result;

	}

	private static void addVariables(ArrayList<Formula> formulas, int first, int second, int third) {
		formulas.add(new Formula(new Variable(first), new Variable(second), new Variable(third)));
	}

	private static void solveAllProblems() {
		double pocatecniTeplota = 100;
		double koeficientOchlazeni = 0.90;
		double minimalniTeplota = 1;
		double koeficientEquilibrum = 0.5;
		double koeficientRelaxace = 2;

		double[] pocTeploty = new double[] { 50, 75, 100, 200, 500, 600, 700, 800, 900, 1000, 5000, 10000, 25000 };
		for (int i = 0; i < pocTeploty.length; i++) {
			solveProblem(pocTeploty[i], koeficientOchlazeni, minimalniTeplota, koeficientEquilibrum,
					koeficientRelaxace);
		}

		double[] koefOchl = new double[] { 0.55, 0.60, 0.65, 0.70, 0.75, 0.80, 0.85, 0.9, 0.95, 0.99 };
		for (int i = 0; i < koefOchl.length; i++) {
			solveProblem(pocatecniTeplota, koefOchl[i], minimalniTeplota, koeficientEquilibrum, koeficientRelaxace);
		}

		double[] minTepl = new double[] { 0.01, 0.05, 0.1, 0.5, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		for (int i = 0; i < minTepl.length; i++) {
			solveProblem(pocatecniTeplota, koeficientOchlazeni, minTepl[i], koeficientEquilibrum, koeficientRelaxace);
		}

		double[] koefEq = new double[] { 0.01, 0.02, 0.05, 0.1, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5, 10, 15, 25 };
		for (int i = 0; i < koefEq.length; i++) {
			solveProblem(pocatecniTeplota, koeficientOchlazeni, minimalniTeplota, koefEq[i], koeficientRelaxace);
		}

		double[] koefRelax = new double[] { 1, 2, 3, 4, 5, 6, 8, 10, 12, 15, 17, 20, 50 };
		for (int i = 0; i < koefRelax.length; i++) {
			solveProblem(pocatecniTeplota, koeficientOchlazeni, minimalniTeplota, koeficientEquilibrum, koefRelax[i]);
		}

	}

	private static void solveProblem(double pocatecniTeplota, double koeficientOchlazeni, double minimalniTeplota,
			double koeficientEquilibrum, double koeficientRelaxace) {
		Result result = new Result(problems.get(0).getId());
		for (int i = 0; i < 50; i++) {
			solveProblem(problems.get(0), pocatecniTeplota, koeficientOchlazeni, minimalniTeplota, koeficientEquilibrum,
					koeficientRelaxace, result);
		}
		System.out.println(result + "\t" + pocatecniTeplota + "\t" + koeficientOchlazeni + "\t" + minimalniTeplota
				+ "\t" + koeficientEquilibrum + "\t" + koeficientRelaxace);

	}
}