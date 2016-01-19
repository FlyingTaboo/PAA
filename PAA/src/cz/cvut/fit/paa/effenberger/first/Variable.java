package cz.cvut.fit.paa.effenberger.first;

public class Variable {
	private int order;
	private boolean negation;
	private int price;
	
	public Variable(int order, int price, boolean negation){
		this.order = order;
		this.negation = negation;
		this.price = price;
	}

	public int getOrder() {
		return order;
	}

	public boolean isNegation() {
		return negation;
	}

	public int getPrice() {
		return price;
	}
	
	public boolean isSatisfied(boolean value){
		if(negation){
			return !value;
		}
		return value;
	}
	
	public int getPrice(boolean value){
		if(isSatisfied(value)){
			return this.price;
		}
		return 0;
	}
}
