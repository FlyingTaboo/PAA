package cz.cvut.fit.paa.effenberger.first;

import java.io.File;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class BackPack {
	private static ArrayList<File> files = new ArrayList<File>();
	private static ArrayList<Problem> problems = new ArrayList<Problem>();
	public static void main(String[] args) throws Exception {
		readFiles(new File("D:\\GDRIVE\\FIT\\test\\knap_40.inst.dat"));
		for (int i = 0; i < files.size(); i++) {
			readFileAndSolve(files.get(i));
		}
		solveAllProblems();
	}



	public static void solveProblem(Problem problem, double pocatecniTeplota, double koeficientOchlazeni, double minimalniTeplota, double koeficientEquilibrum) {
		
		int ID = Integer.parseInt(lineArray[0]);
		int resultSize = Integer.parseInt(lineArray[1]);
		int max_weight = Integer.parseInt(lineArray[2]);
		ArrayList<Item> items = new ArrayList<Item>();
		for (int i = 0; i < resultSize; i++) {
			Item item = new Item(Integer.parseInt(lineArray[i * 2 + 4]), Integer.parseInt(lineArray[i * 2 + 3]), i); 
			items.add(item);
		}
		

		Solver solverSC = new Solver(ID, resultSize, max_weight, items, 0.0);

		
		Instant startSC = Instant.now();
		solverSC.setCoolingAttributes(pocatecniTeplota, koeficientOchlazeni, minimalniTeplota, koeficientEquilibrum);
		
		Result resultSC = solverSC.findCoolingResult();
		Instant endSC = Instant.now();
			
		String out = resultSize + ";\t" + ID + ";\t";
		//out += resultDynamic.getTotalPrice() + " vs " + resultSC.getTotalPrice() + ";\t";
		out += Duration.between(startSC, endSC).toMillis() + ";\t";
		//out += resultSC.getExpandovano();
		out += pocatecniTeplota + ";\t" + koeficientOchlazeni+ ";\t" + minimalniTeplota+ ";\t" + koeficientEquilibrum+ ";\t";
		out += resultSC.getTotalPrice();		
		System.out.println(out);
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
			while ((line = br.readLine()) != null) {
				problems.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void solveAllProblems(){
		double pocatecniTeplota = 500;
		double koeficientOchlazeni = 0.90;
		double minimalniTeplota = 1;
		double koeficientEquilibrum = 0.5;
		
		double[] pocTeploty = new double[]{100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
		for (int i=0; i<pocTeploty.length; i++){
			solveProblem(pocTeploty[i], koeficientOchlazeni, minimalniTeplota, koeficientEquilibrum);
		}
		
		double[] koefOchl = new double[]{0.55, 0.60, 0.65, 0.70, 0.75, 0.80, 0.85, 0.9, 0.95, 0.99};
		for (int i=0; i<pocTeploty.length; i++){
			solveProblem(pocatecniTeplota, koefOchl[i], minimalniTeplota, koeficientEquilibrum);
		}
		
		double[] minTepl = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		for (int i=0; i<pocTeploty.length; i++){
			solveProblem(pocatecniTeplota, koeficientOchlazeni, minTepl[i], koeficientEquilibrum);
		}
		
		double[] koefEq = new double[]{0.1, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5};
		for (int i=0; i<pocTeploty.length; i++){
			solveProblem(pocatecniTeplota, koeficientOchlazeni, minimalniTeplota, koefEq[i]);
		}
		
	}



	private static void solveProblem(double pocatecniTeplota, double koeficientOchlazeni, double minimalniTeplota,
			double koeficientEquilibrum) {
		for(Problem problem: problems){
			solveProblem(problem, pocatecniTeplota, koeficientOchlazeni, minimalniTeplota, koeficientEquilibrum);
		}
		
	}
}