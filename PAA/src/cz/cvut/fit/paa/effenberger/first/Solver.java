package cz.cvut.fit.paa.effenberger.first;

import java.util.Arrays;

public class Solver {
	private Problem problem;

	public Solver(int size, Problem p) {
		this.problem = p;
	}

	public Result findExactResult() {
		boolean[] array = new boolean[this.problem.getSize()];
		for (int i = 0; i < Math.pow(2, this.problem.getSize()); i++) {
			String bites = Integer.toBinaryString(i);
			bites = fillZeros(bites, this.problem.getSize());
			//System.out.println(bites);
			for (int j = 0; j < this.problem.getSize(); j++) {
				if (bites.charAt(j) == '1') {
					array[j] = true;
				} else {
					array[j] = false;
				}
			}
			this.problem.setValues(array);
			if (this.problem.getPrice() != 0) {
				System.out.println(this.problem.getPrice());
				System.out.println(bites);
			}

		}
		return null;
	}

	private String fillZeros(String input, int lenght) {
		StringBuilder sb = new StringBuilder(lenght);
		for (int i = 0; i < lenght - input.length(); i++) {
			sb.append("0");
		}
		sb.append(input);
		return sb.toString();
	}

	/*
	 * public int getWeightOfContent(ArrayList<Integer> pole) { int weight = 0;
	 * for (int i = 0; i < pole.size(); i++) { if (pole.get(i) == 1) { weight +=
	 * this.items.get(i).getWeight(); } } return weight; }
	 * 
	 * public int getPriceOfContent(ArrayList<Integer> pole) { int price = 0;
	 * for (int i = 0; i < pole.size(); i++) { if (pole.get(i) == 1) { price +=
	 * this.items.get(i).getPrice(); } } return price; }
	 * 
	 * public void print(Integer[][] table) { System.out.println(
	 * "/===0=======1=======2=======3=======4=======5=======6=======7=======8=======9======10=======11======12======13=====14==\\"
	 * ); for (int i = 0; i < this.size + 1; i++) { for (int j = 0; j <
	 * table[i].length; j++) { System.out.print("|   " + (table[i][j] == null ?
	 * "\t" : table[i][j] + "\t")); } System.out.print("|");
	 * System.out.println(); } System.out.println(
	 * "\\=======================================================================================================================/"
	 * ); System.out.println("\n\n");
	 * 
	 * }
	 * 
	 * public Result findCoolingResult() { Instant start = Instant.now(); Result
	 * result = new Result(this.ID, this.size, this.capacity); double
	 * aktualniTeplota = this.pocatecniTeplota; int aktualniCena = 0; int
	 * bestCena = 0; int bestCenaVaha = 0; int expandovano = 0;
	 * 
	 * int i = 0; ArrayList<Integer> novy = new ArrayList<Integer>();
	 * ArrayList<Integer> aktualni = new ArrayList<Integer>(); for (int j = 0; j
	 * < this.size; j++) { aktualni.add(1); if (getWeightOfContent(aktualni) >
	 * this.capacity) { aktualni.set(j, 0); break; } } for (int j =
	 * aktualni.size(); j < this.size; j++) { aktualni.add(0); } boolean endA =
	 * false; while (!endA && !isFrozen(aktualniTeplota)) { i = 0; endA = true;
	 * while (equilibrum(i)) { i++; expandovano++; novy =
	 * generateNextState(aktualni, aktualniTeplota); aktualniCena =
	 * getPriceOfContent(novy); if (!novy.equals(aktualni)) { endA = false; } if
	 * (aktualniCena > bestCena) { bestCena = aktualniCena; bestCenaVaha =
	 * getWeightOfContent(novy); this.content = novy;
	 * result.addResult(this.content, bestCena, bestCenaVaha); } aktualni =
	 * novy; } aktualniTeplota = zchladit(aktualniTeplota); } Instant end =
	 * Instant.now(); result.setDuration(Duration.between(start, end));
	 * result.setExpandovano(expandovano); return result; }
	 * 
	 * private ArrayList<Integer> generateNextState(ArrayList<Integer> aktualni,
	 * double temp) { int cena1 = getPriceOfContent(aktualni);
	 * 
	 * ArrayList<Integer> novy = getRandomState(aktualni); int cena2 =
	 * getPriceOfContent(novy);
	 * 
	 * if (cena2 > cena1) { return novy;
	 * 
	 * } else { int delta = cena2 - cena1; Random randomObj = new Random();
	 * double x = randomObj.nextDouble(); return (x < Math.exp(delta / temp)) ?
	 * novy : aktualni; } }
	 * 
	 * private ArrayList<Integer> getRandomState(ArrayList<Integer> aktualni) {
	 * ArrayList<Integer> novy; do { novy = (ArrayList<Integer>)
	 * aktualni.clone(); Random index = new Random(); int random =
	 * index.nextInt(novy.size()); novy.get(random); novy.set(random,
	 * novy.get(random) == 0 ? 1 : 0); } while (getWeightOfContent(novy) >
	 * this.capacity); return novy; }
	 * 
	 * private boolean isFrozen(double aktualniTeplota) { return aktualniTeplota
	 * < this.minimalniTeplota; }
	 * 
	 * private double zchladit(double aktualniTeplota) { return (aktualniTeplota
	 * * this.koeficientOchlazeni); }
	 * 
	 * public void setCoolingAttributes(double pocatecniTeplota, double
	 * koeficientOchlazeni, double minimalniTeplota, double
	 * koeficientEquilibrum) { this.pocatecniTeplota = pocatecniTeplota;
	 * this.koeficientOchlazeni = koeficientOchlazeni; this.minimalniTeplota =
	 * minimalniTeplota; this.koeficientEquilibrum = koeficientEquilibrum; }
	 * 
	 * private boolean equilibrum(int i) { return (i <
	 * (this.koeficientEquilibrum * this.capacity)); }
	 */
}