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
		double aktualniCena = 0;
		double bestCena = 0;
		int i = 0;
		boolean[] novy = new boolean[this.size];
		boolean[] aktualni = new boolean[this.size];
		aktualni = generateInitState(aktualni);

		while (!isFrozen(aktualniTeplota)) {
			i = 0;
			while (equilibrum(i)) {
				i++;
				novy = generateNextState(aktualni, aktualniTeplota);
				printBoolArr(novy);
				aktualniCena = getAktSat(novy);
				
				//System.out.println(aktualniCena);
				if (aktualniCena > bestCena) {
					bestCena = aktualniCena;
					novy.clone();
					
				}
				aktualni = novy;
			}
			aktualniTeplota = zchladit(aktualniTeplota);
		}
		this.problem.setValues(novy);

		return this.problem.getPrice();
	}

	private int getAktSat(boolean[] novy) {
		this.problem.setValues(novy);
		//System.out.println(this.problem.getSatisfiedCount());
		return this.problem.getSatisfiedCount();
	}

	private boolean[] generateNextState(boolean[] aktualni, double temp) {
		double cena1 = getAktSat(aktualni);

		boolean[] novy = getRandomState(aktualni);
		double cena2 = getAktSat(novy);

		if (isNewBetter(novy, aktualni)) {
			return novy;
		} else {
			
			double delta = cena2 - cena1;
			Random randomObj = new Random();
			double x = randomObj.nextDouble();
			return (x < Math.exp(delta / temp)) ? novy : aktualni;
		}
	}

	private boolean isNewBetter(boolean[] novy, boolean[] aktualni) {
		this.problem.setValues(novy);
		int novyCountSat = this.problem.getSatisfiedCount();
		double novyPrice = this.problem.getRelaxedPrice();
		this.problem.setValues(aktualni);
		int aktualniCount = this.problem.getSatisfiedCount();
		double aktualniPrice = this.problem.getRelaxedPrice();
		if (novyCountSat > aktualniCount) {
			return true;
		}
		if (novyCountSat == aktualniCount && novyPrice > aktualniPrice) {
			return true;
		}
		return false;
	}

	private double getPriceOfContent(boolean[] aktualni) {
		this.problem.setValues(aktualni);
		//System.out.println(this.problem.getSatisfiedCount());
		return this.problem.getRelaxedPrice();

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

	private void printBoolArr(boolean[] array) {
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				// System.out.print(array[i] ? "1" : "0");
			}
			// System.out.print("\n");
		}
	}
}

/*
 * Vygeneruj prvnĂ­ nĂˇhodnĂ˝ stav (zcela nĂˇhodnĂ˝) Vygeneruj nĂˇhodnĂ˝ stav odliĹˇnĂ­
 * od pĹ™edchozĂ­ho v jednom bodu (pokud se projdou vĹˇechny ohodnocenĂ­, co pak?)
 * PĹ™ijmi z generovĂˇnĂ­ Ĺ™eĹˇenĂ­, kterĂ© mĂˇ buÄŹ vyĹˇĹˇĂ­ ohodnocenĂ­ nebo mĂˇ vĂ­ce
 * splnÄ›nĂ˝ch formulĂ­ cena Ĺ™eĹˇenĂ­ je poÄŤĂ­tĂˇnĂˇ jak bylo popsaĂˇno (vĹˇe splnÄ›no =
 * x2), (nesplnÄ›no = * procenta) PĹ™ijmi jako Ĺ™eĹˇenĂ­ takovĂ© Ĺ™eĹˇenĂ­, kterĂ© splĹ�uje
 * pĹŻvodnĂ­ podmĂ­nky (ohlednÄ› ceny)
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */