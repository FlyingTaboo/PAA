package cz.cvut.fit.paa.effenberger.first;

import java.time.Instant;
import java.util.Random;

public class Solver {
	private Problem problem;
	private double pocatecniTeplota;
	private double koeficientOchlazeni;
	private double minimalniTeplota;
	private double koeficientEquilibrum;
	private int size;

	public Solver(int size, Problem p) {
		this.problem = p;
		this.size = size;
	}

	public int findExactResult() {
		boolean[] array = new boolean[this.problem.getSize()];
		int best = 0;
		for (int i = 0; i < Math.pow(2, this.problem.getSize()); i++) {
			String bites = Integer.toBinaryString(i);
			bites = fillZeros(bites, this.problem.getSize());
			// System.out.println(bites);
			for (int j = 0; j < this.problem.getSize(); j++) {
				if (bites.charAt(j) == '1') {
					array[j] = true;
				} else {
					array[j] = false;
				}
			}
			this.problem.setValues(array);
			int solution = this.problem.getPrice();
			if (solution > best) {
				best = solution;
			}

		}
		return best;
	}

	private String fillZeros(String input, int lenght) {
		StringBuilder sb = new StringBuilder(lenght);
		for (int i = 0; i < lenght - input.length(); i++) {
			sb.append("0");
		}
		sb.append(input);
		return sb.toString();
	}

	public int findCoolingResult(double pocatecniTeplota, double koeficientOchlazeni, double minimalniTeplota,
			double koeficientEquilibrum) {
		this.pocatecniTeplota = pocatecniTeplota;
		this.koeficientOchlazeni = koeficientOchlazeni;
		this.minimalniTeplota = minimalniTeplota;
		this.koeficientEquilibrum = koeficientEquilibrum;

		double aktualniTeplota = this.pocatecniTeplota;
		int aktualniCena = 0;
		int bestCena = 0;
		int i = 0;
		boolean[] novy = new boolean[this.size];
		boolean[] aktualni = new boolean[this.size];
		aktualni = generateInitState(aktualni);

		boolean endA = false;
		while (!endA && !isFrozen(aktualniTeplota)) {
			i = 0;
			endA = true;
			while (equilibrum(i)) {
				i++;
				novy = generateNextState(aktualni, aktualniTeplota);
				aktualniCena = getPriceOfContent(novy);
				if (!novy.equals(aktualni)) {
					endA = false;
				}
				if (aktualniCena > bestCena) {
					bestCena = aktualniCena;
				}
				aktualni = novy;
			}
			aktualniTeplota = zchladit(aktualniTeplota);
		}
		Instant.now();
		return bestCena;
	}

	private boolean[] generateNextState(boolean[] aktualni, double temp) {
		int cena1 = getPriceOfContent(aktualni);

		boolean[] novy = getRandomState(aktualni);
		int cena2 = getPriceOfContent(novy);

		if (cena2 > cena1) {
			return novy;
		} else {
			int delta = cena2 - cena1;
			Random randomObj = new Random();
			double x = randomObj.nextDouble();
			return (x < Math.exp(delta / temp)) ? novy : aktualni;
		}
	}

	private int getPriceOfContent(boolean[] aktualni) {
		this.problem.setValues(aktualni);
		return this.problem.getPrice();

	}

	private boolean[] getRandomState(boolean[] aktualni) {
		return generateInitState(aktualni);
		// ArrayList<Integer> novy;
		// do {
		// novy = (ArrayList<Integer>) aktualni.clone();
		// Random index = new Random();
		// int random = index.nextInt(novy.size());
		// novy.get(random);
		// novy.set(random, novy.get(random) == 0 ? 1 : 0);
		// } while (getWeightOfContent(novy) > this.capacity);
		// return novy;
		// return aktualni; // TODO
	}

	private boolean[] generateInitState(boolean[] aktualni) {
		Random index = new Random();
		for (int i = 0; i < aktualni.length; i++) {
			aktualni[i] = index.nextBoolean();
		}
		return aktualni;
	}

	private boolean isFrozen(double aktualniTeplota) {
		return aktualniTeplota < this.minimalniTeplota;
	}

	private double zchladit(double aktualniTeplota) {
		return (aktualniTeplota * this.koeficientOchlazeni);
	}

	private boolean equilibrum(int i) {
		return (i < (this.koeficientEquilibrum * this.size));
	}
}