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
		readFiles(new File("D:\\GDRIVE\\FIT\\test\\knap_40.inst.dat"));
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
		Solver solverDyn = new Solver(ID, resultSize, max_weight, items, 0.0);
		resultDynamic = solverDyn.findDynamicResult(false);
		
		
		Result resultSC = null;
		Solver solverSC = new Solver(ID, resultSize, max_weight, items, 0.0);
		double pocatecniTeplota = 500;
		double koeficientOchlazeni = 0.85;
		double minimalniTeplota = 1;
		double koeficientEquilibrum = 2;
		
		Instant startSC = Instant.now();
		solverSC.setCoolingAttributes(pocatecniTeplota, koeficientOchlazeni, minimalniTeplota, koeficientEquilibrum);
		
		resultSC = solverSC.findCoolingResult();
		Instant endSC = Instant.now();
			
		String out = resultSize + ";\t" + ID + ";\t";
		out+= (resultDynamic.getTotalPrice() - resultSC.getTotalPrice())/resultDynamic.getTotalPrice() + ";\t";
		//out += resultDynamic.getTotalPrice() + " vs " + resultSC.getTotalPrice() + ";\t";
		out += Duration.between(startSC, endSC).toMillis() + ";\t";
		out += resultSC.getExpandovano();
		
		
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

	
	private class times{
		private double odchylka;
		private int cas;
		
		times(double odchylka, int cas){
			this.odchylka = odchylka;
			this.cas = cas;
		}
	}
}