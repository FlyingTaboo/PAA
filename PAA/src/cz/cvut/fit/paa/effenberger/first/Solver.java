package cz.cvut.fit.paa.effenberger.first;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

public class Solver {
	private static final int MIN_VALUE = -1;

	private int size;
	private int capacity;
	private int ID;
	private int C_Max = 0;
	private ArrayList<Item> items;
	private ArrayList<Integer> content;
	private double koeficientOchlazeni;
	private double pocatecniTeplota;
	private double minimalniTeplota;
	private double koeficientEquilibrum;

	public Solver(int ID, int size, int capacity, ArrayList<Item> items, double e) {
		this.ID = ID;
		this.size = size;
		this.capacity = capacity;
		this.items = items;
		this.content = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			this.content.add(MIN_VALUE);
		}
		for(int i=0; i<items.size(); i++){
			if (items.get(i).getPrice() > this.C_Max){
				this.C_Max = items.get(i).getPrice();
			}
		}
	}
	
	public int getWeightOfContent(ArrayList<Integer> pole){
		int weight = 0;
		for(int i=0; i<pole.size();i++){
			if (pole.get(i) == 1){
				weight += items.get(i).getWeight();
			}
		}
		return weight;
	}
	public int getPriceOfContent(ArrayList<Integer> pole){
		int price = 0;
		for(int i=0; i<pole.size();i++){
			if (pole.get(i) == 1){
				price += items.get(i).getPrice();
			}
		}
		return price;
	}
	
	
	
	public void print(Integer[][] table){
		System.out.println("/===0=======1=======2=======3=======4=======5=======6=======7=======8=======9======10=======11======12======13=====14==\\");
		for(int i=0; i<size+1; i++){
			for (int j=0; j< ((Integer[]) table[i]).length; j++){
				System.out.print("|   " + (table[i][j] == null ? "\t": table[i][j] + "\t"));
			}
			System.out.print("|");
			System.out.println();
		}
		System.out.println("\\=======================================================================================================================/");
		System.out.println("\n\n");
		
	}
	
	public Result findCoolingResult(){
		Instant start = Instant.now();
		Result result = new Result(ID, size, capacity);
		double aktualniTeplota = this.pocatecniTeplota;
		int aktualniCena = 0;
		int bestCena = 0;
		int bestCenaVaha = 0;
		int expandovano = 0;
		
		int i = 0;
		ArrayList<Integer> novy = new ArrayList<Integer>();
		ArrayList<Integer> aktualni = new ArrayList<Integer>();
		for(int j=0; j<size; j++){
			aktualni.add(1);
			if(getWeightOfContent(aktualni) > capacity){
				aktualni.set(j, 0);
				break;
			}
		}
		for(int j=aktualni.size(); j<size; j++){
			aktualni.add(0);
		}
		boolean endA = false;
		while(!endA&& !isFrozen(aktualniTeplota)){
			i = 0;
			endA = true;
			while(equilibrum(i)) {
				i++;
				expandovano++; 
				novy = generateNextState(aktualni, aktualniTeplota);
                aktualniCena = getPriceOfContent(novy);
                if (!novy.equals(aktualni)) {
                	endA = false;
                }
                if (aktualniCena > bestCena ) {
                    bestCena = aktualniCena;
                    bestCenaVaha = getWeightOfContent(novy);
                    content = novy;
                    result.addResult(content, bestCena, bestCenaVaha);
                }
                aktualni = novy;
			}
			aktualniTeplota = zchladit(aktualniTeplota);
		} 
		Instant end = Instant.now();
		result.setDuration(Duration.between(start, end));
		result.setExpandovano(expandovano);
		return result;
	}
	
	private ArrayList<Integer> generateNextState(ArrayList<Integer> aktualni, double temp) {
        int cena1 = getPriceOfContent(aktualni);

        ArrayList<Integer> novy = getRandomState(aktualni);
        int cena2 = getPriceOfContent(novy);

        if ( cena2 > cena1 ) {
            return novy;
            
        } else {
            int delta = cena2 - cena1;
            Random randomObj = new Random();
            double x = randomObj.nextDouble();
            return ( x < Math.exp(delta / temp) ) ? novy : aktualni;
        }
    }

	private ArrayList<Integer> getRandomState(ArrayList<Integer> aktualni) {
		ArrayList<Integer> novy;
		do{
			novy = (ArrayList<Integer>) aktualni.clone();
			Random index = new Random();
			int random = index.nextInt(novy.size());
			int pred = novy.get(random);
			novy.set(random, novy.get(random) == 0 ? 1 : 0);
		}while(getWeightOfContent(novy)>capacity);
        return novy;
	}

	private boolean isFrozen(double aktualniTeplota) {
	    return aktualniTeplota < this.minimalniTeplota;
	}

	private double zchladit(double aktualniTeplota) {
		 return (aktualniTeplota * this.koeficientOchlazeni);
	}

	public void setCoolingAttributes(double pocatecniTeplota, double koeficientOchlazeni, double minimalniTeplota, double koeficientEquilibrum){
		this.pocatecniTeplota = pocatecniTeplota;
		this.koeficientOchlazeni = koeficientOchlazeni;
		this.minimalniTeplota = minimalniTeplota;
		this.koeficientEquilibrum = koeficientEquilibrum;
	}
	
	private boolean equilibrum(int i) {
        return (i < ( this.koeficientEquilibrum * capacity ));
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}