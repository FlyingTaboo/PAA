package cz.cvut.fit.paa.effenberger.first;

public class Variable {
	private int order;
	private boolean negation;
	private int price;

	public Variable(int order, boolean negation) {
		this.order = order;
		this.negation = negation;
	}

	public Variable(int var) {
		this.order = Math.abs(var);
		if (var < 0) {
			this.negation = true;
		} else {
			this.negation = false;
		}
	}

	public int getOrder() {
		return this.order;
	}

	public boolean isNegation() {
		return this.negation;
	}

	public int getPrice() {
		return this.price;
	}

	public boolean isSatisfied(boolean value) {
		if (this.negation) {
			return !value;
		}
		return value;
	}

	public int getPrice(boolean value) {
		if (isSatisfied(value)) {
			return this.price;
		}
		return 0;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.negation ? "!" : "").append(this.order).append(" price: ").append(this.price);
		return sb.toString();
	}
}
