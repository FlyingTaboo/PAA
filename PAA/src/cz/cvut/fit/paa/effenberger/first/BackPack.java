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

	public static void main(String[] args) throws Exception {
		readFiles(new File("D:\\GDRIVE\\FIT\\test\\all.inst.dat"));
		for (int i = 0; i < files.size(); i++) {
			readFileAndSolve(files.get(i));
		}
	}

	public static void solveProblem(String line) {
		String[] lineArray = line.split(" ");
		int ID = Integer.parseInt(lineArray[0]);
		int resultSize = Integer.parseInt(lineArray[1]);
		int max_weight = Integer.parseInt(lineArray[2]);
		ArrayList<Item> items = new ArrayList<Item>();
		for (int i = 0; i < resultSize; i++) {
			Item item = new Item(Integer.parseInt(lineArray[i * 2 + 4]), Integer.parseInt(lineArray[i * 2 + 3]), i); 
			items.add(item);
		}
		
		
		
		
		Result resultDynamic = null;
		Result resultFPTAS = null;
		Result resultBF = null;
		Result resultHeur = null;
		Instant startDyn = Instant.now();
		for(int i=0;i<1000;i++){
			Solver solverDyn = new Solver(ID, resultSize, max_weight, items, 0.0);
			resultDynamic = solverDyn.findDynamicResult(false);
		}
		Instant endDyn = Instant.now();
		
		
		int max = resultSize;
		if (resultSize == 5) max = 1000;
		if (resultSize == 7) max = 1000;
		if (resultSize == 10) max = 100;
		if (resultSize == 12) max = 100;
		if (resultSize == 15) max = 10;
		if (resultSize == 17) max = 1;
		if (resultSize == 20) max = 1;
		Instant startBF = Instant.now();
		for(int i=0;i<max; i++){
			Solver solverBF = new Solver(ID, resultSize, max_weight, items, 0.0);
			resultBF = solverBF.findExactResult();
		}
		Instant endBF = Instant.now();
		
		Instant startBandB = Instant.now();
		for(int i=0;i<max; i++){
			Solver solverBandB = new Solver(ID, resultSize, max_weight, items, 0.0);
			solverBandB.findBandBResult();
		}
		Instant endBandB = Instant.now();
		
		Instant startHeur = Instant.now();
		for(int i=0;i<10000; i++){
			Solver solverHeur = new Solver(ID, resultSize, max_weight, items, 0.0);
			resultHeur = solverHeur.findHeuristicResult();
		}
		Instant endHeur = Instant.now();
		
		String out = resultSize + ";\t" + ID + ";\t";
		//out+= resultDynamic.getTotalPrice() + ";";
		out+= ((double) Duration.between(startDyn, endDyn).toMillis())/1000 + ";\t";
		out+= ((double) Duration.between(startBF, endBF).toMillis())/max + ";\t";
		out+= ((double) Duration.between(startBandB, endBandB).toMillis())/max + ";\t";
		out+= ((double) Duration.between(startHeur, endHeur).toMillis())/10000.0 + ";\t\t";
		out+= (resultDynamic.getTotalPrice() - resultHeur.getTotalPrice())/resultDynamic.getTotalPrice();
		
		
		/*
		for(int j=1; j<11; j++){
			Instant startFPTAS = Instant.now();
			for(int i=0;i<100;i++){
				Solver solverFPTAS = new Solver(ID, resultSize, max_weight, items, 0.1*j);
				resultFPTAS = solverFPTAS.findFPTASResult();
			}
			Instant endFPTAS = Instant.now();
			out+=";";
			out+= resultFPTAS.getTotalPrice() + ";";
			out+= Duration.between(startFPTAS, endFPTAS).toMillis();
			out+=";";
			out+=(resultDynamic.getTotalPrice()-resultFPTAS.getTotalPrice())/resultFPTAS.getTotalPrice();
			out+=";";
		}
		*/
		
		//out+= c.toString() + "; ";
		//out+= d.toString();
		
		System.out.println(out);
 		
		
		/*Result heuristic = null;
		Instant start1 = Instant.now();
		heuristic = solver.findHeuristicResult();

		Instant end1 = Instant.now();
		
		System.out.println("Size: " + resultSize + "; " +ID + "; Time exact: " + Duration.between(start, end).toMillis()
				+ "; Time heuristic: " + Duration.between(start1, end1).toMillis() + "; rel.mis: " + (exact.getTotalPrice() - heuristic.getTotalPrice())/exact.getTotalPrice());
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
			while ((line = br.readLine()) != null) {
				solveProblem(line);
				//break;
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

}