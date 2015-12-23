package cz.cvut.fit.paa.effenberger.first;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Solver {
	private static final int MAX_VALUE = 2;
	private static final int MIN_VALUE = -1;

	private int size;
	private int capacity;
	private int pointer = MIN_VALUE;
	private int ID;
	private int C_Max = 0;
	private double K = 0.0;
	private double epsilon;
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
		this.epsilon = e;
		this.K = (this.epsilon * C_Max) / size;
	}

	public Result findExactResult() {
		Instant start = Instant.now(); 
		Result result = new Result(ID, size, capacity);
		while (true){
			pointer++;
			if (pointer == -1) break;
			content.set(pointer, content.get(pointer) +1 );
			
			if (content.get(pointer) == MAX_VALUE){
				content.set(pointer,MIN_VALUE);
				pointer-= 2;
			}
			if (pointer == content.size()-1){
				result.addResult(content, getPriceOfContent(content), getWeightOfContent(content));
				pointer--;
			}
		}
		Instant end = Instant.now();
		result.setDuration(Duration.between(start, end));
		return result;
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
	
	public int getPriceOfRest(ArrayList<Integer> pole, int position){
		int price = 0;
		for(int i=position+1; i<pole.size(); i++){
				price += items.get(i).getPrice();
		}
		return price;
	}
	
	
	
	public Result findHeuristicResult(){
		Instant start = Instant.now(); 
		sortItems();
		int weightOfContent = 0;
		Result result = new Result(ID, size, capacity);
		for(int i=0; i<size; i++){
			if(weightOfContent + items.get(i).getWeight() <= capacity){
				content.set(i, 1);
				weightOfContent += items.get(i).getWeight();
			}else{
				content.set(i, 0);
			}
		}
		result.addResult(content, getPriceOfContent(content), weightOfContent);
		Instant end = Instant.now();
		result.setDuration(Duration.between(start, end));
		return result;
	}
	
	private void sortItems(){
		Collections.sort(this.items);
	}
	
	
	
	public Result findBandBResult(){
		Instant start = Instant.now();
		Result result = new Result(ID, size, capacity);
		ArrayList pole = new ArrayList();
		for(int i=0; i<size; i++){
			pole.add(0);
		}
		findBandBResultRecursivly(pole, 0, result);
		Instant end = Instant.now();
		result.setDuration(Duration.between(start, end));
		return result;
	}
	
	private void findBandBResultRecursivly(ArrayList pole, int position, Result result){
		for(int i=0; i<MAX_VALUE; i++){
			pole.set(position, i);
			fill(pole, position);
			if (goForward(pole, position, result)){
				findBandBResultRecursivly(pole, position+1, result);
			}
		}
	}
	
	private void fill(ArrayList pole, int position){
		for(int i=position+1; i<pole.size(); i++){
			pole.set(i,0);
		}
	}
	
	private boolean goForward(ArrayList<Integer> pole, int position, Result result){
		String r = null;
		if (getWeightOfContent(pole) > this.capacity){
			return false;
		}
		
		if (getPriceOfContent(pole)+getPriceOfRest(pole, position) < result.getTotalPrice()){
			return false;
		}
		result.addResult(pole, getPriceOfContent(pole), getWeightOfContent(pole));
		if (position==size-1) return false;
		return true;
	}
	
	
	public Result findDynamicResult(boolean FTPSA){
		Result result = new Result(ID, size, capacity);
		Instant start = Instant.now();
		
		int maxPrice = 0;
		for(int i=0; i<items.size(); i++){
			maxPrice += items.get(i).getPrice();
		}
		Integer[][] table = new Integer[size+1][maxPrice+1];
		
		for (int i=0; i<size+1; i++){
			table[i][0] = 0;
		}
		
		for (int i=0; i<size; i++){
			for (int j=0; j<maxPrice+1; j++){
				if(table[i][j] != null){
					Integer a = table[i][j+items.get(i).getPrice()];
					if (a == null) a = Integer.MAX_VALUE;
					table[i+1][j+items.get(i).getPrice()] = Math.min(table[i][j] + items.get(i).getWeight(), a);
					
					if (i+1 < size+1 && table[i+1][j] == null){
						table[i+1][j] = table[i][j];
					}
				}
				
			}
			//print(table);
		}
		
		

		
		int maxResult = 0;
		int x=0;
		int y =0;
		for (int j=maxPrice; j>=0; j--){
			for (int i=size; i>=0; i--){
				if(table[i][j] != null && table[i][j] <= capacity){
					maxResult =j;						
					x=i;
					break;
					
				}
			}
			if (maxResult != 0) break;
		}
		
		//print(table);
		//System.out.println(maxResult);
		
		int a = 0;
		int b = 0;
		for(int i=items.size(); i>0; i--){
			
			if(table[i][maxResult].equals(table[i-1][maxResult] )){
				content.set(i-1, 0); 
			}else{
				content.set(i-1, 1);
				maxResult -= items.get(i-1).getPrice();
				if (FTPSA){
					a += items.get(i-1).getFTAPSprice();
				}else{
					a += items.get(i-1).getPrice();
				}
				b += items.get(i-1).getWeight();
				
			}
			
		}
		//System.out.println("\n" +maxResult + " " + a + " " + b);
		
		
		Instant end = Instant.now();
		result.addResult(content, a, b);
		result.setDuration(Duration.between(start, end));
		return result;
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

	public Result findFPTASResult() {
		Instant start = Instant.now();
		Result result = new Result(ID, size, capacity);
		ArrayList<Item> newItems = new ArrayList<Item>();
		
		for (int i=0; i<items.size(); i++){
			int value = new Double(items.get(i).getPrice()/this.K).intValue();
			//System.out.println(items.get(i).getPrice() + " " + this.K + " " +value);
			newItems.add( new Item(items.get(i).getPrice(), value, items.get(i).getWeight(), items.get(i).getOrder()));
		}
		this.items = newItems;
		result = findDynamicResult(true);
		
		
		
		
		Instant end = Instant.now();
		result.setDuration(Duration.between(start, end));
		return result;
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