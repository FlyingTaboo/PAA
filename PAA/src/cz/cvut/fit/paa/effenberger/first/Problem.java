package cz.cvut.fit.paa.effenberger.first;

import java.util.ArrayList;

public class Problem {
	private ArrayList<Formula> formulas;
	private boolean [] input;
	private boolean isSet = false;
	private int ID;
	
	Problem(ArrayList<Formula> formulas, int ID){
		this.formulas = formulas;
		this.ID = ID;
	}
	
	public int getPrice(){
		if (!isSet){
			return -1;
		}
		int result = 0;
		for(int i=0;i<formulas.size(); i++){
			Formula akt =formulas.get(i); 
			if (akt.isSatisfied(input[i*3], input[i*3 +1], input[i*3 + 2])){
				result += akt.getTotalPrice(input[i*3], input[i*3 +1], input[i*3 + 2]);
			}else{
				return 0; // nesplněna některá z formulí
			}
		}
		return result;
	}
	
	
	public void setValues(boolean [] input){
		this.input = input;
		this.isSet = true;
	}
	
	public int getId(){
		return this.ID;
	}
	
	
	
}
