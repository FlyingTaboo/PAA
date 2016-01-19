package cz.cvut.fit.paa.effenberger.first;

public class Formula {
	private Variable firstVar;
	private Variable secondVar;
	private Variable thirdVar;
	
	public Formula(Variable f, Variable s, Variable t){
		this.firstVar = f;
		this.secondVar = s;
		this.thirdVar = t;
	}

	public Variable getFirstVar() {
		return firstVar;
	}

	public Variable getSecondVar() {
		return secondVar;
	}

	public Variable getThirdVar() {
		return thirdVar;
	}
	
	public int getTotalPrice(boolean f, boolean s, boolean t){
		return this.firstVar.getPrice(f) + this.secondVar.getPrice(s) + this.thirdVar.getPrice(t); 
	}
	
	public boolean isSatisfied(boolean f, boolean s, boolean t){
		return this.firstVar.isSatisfied(f) || this.secondVar.isSatisfied(s) || this.thirdVar.isSatisfied(t); 
	}
	
}
