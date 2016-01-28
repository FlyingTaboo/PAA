package cz.cvut.fit.paa.effenberger.first;

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
			if (this.problem.getSatisfiedCount() == this.problem.getAllCount()) {
				int solution = this.problem.getPrice();
				if (solution > best) {
					best = solution;
					// printBoolArr(array);
				}
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

	public double findCoolingResult(double pocatecniTeplota, double koeficientOchlazeni, double minimalniTeplota,
			double koeficientEquilibrum, double relaxCoef) {
		this.pocatecniTeplota = pocatecniTeplota;
		this.koeficientOchlazeni = koeficientOchlazeni;
		this.minimalniTeplota = minimalniTeplota;
		this.koeficientEquilibrum = koeficientEquilibrum;
		this.problem.setRelaxCoef(relaxCoef);

		double aktualniTeplota = this.pocatecniTeplota;
		int i = 0;
		boolean[] novy = new boolean[this.size];
		boolean[] aktualni = new boolean[this.size];
		boolean[] best = new boolean[this.size];
		aktualni = generateInitState(aktualni);
		boolean term = false;
		while (!term && !isFrozen(aktualniTeplota)) {
			i = 0;
			term = true;
			while (equilibrum(i)) {
				i++;
				novy = generateNextState(aktualni, aktualniTeplota);
				if (!novy.equals(aktualni)) {
					term = false;

				}
				// System.out.println(satCount(novy));
				if (isSatisfied(novy) || getPrice(novy) > getPrice(best)) {
					best = novy.clone();
				}
				aktualni = novy;
			}
			aktualniTeplota = zchladit(aktualniTeplota);
		}
		this.problem.setValues(best);

		return this.problem.getPrice();
	}

	private double getPrice(boolean[] novy) {
		this.problem.setValues(novy);
		return this.problem.getRelaxedPrice();
	}

	private boolean isSatisfied(boolean[] input) {
		this.problem.setValues(input);
		return (this.problem.getSatisfiedCount() == this.problem.getAllCount());
	}

	private boolean[] generateNextState(boolean[] aktualni, double temp) {
		double cena1 = getPrice(aktualni);

		boolean[] novy = getRandomState(aktualni);
		double cena2 = getPrice(novy);
		double delta = cena2 - cena1;
		// System.out.println(delta);
		if (delta > 0) {
			return novy;
		} else {
			Random randomObj = new Random();
			double x = randomObj.nextDouble();
			return (x < Math.exp(delta / temp)) ? novy : aktualni;
		}
	}

	private boolean[] getRandomState(boolean[] aktualni) {
		boolean[] novy;
		novy = aktualni.clone();
		Random index = new Random();
		int random = index.nextInt(novy.length);
		novy[random] = !novy[random];
		return novy;
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