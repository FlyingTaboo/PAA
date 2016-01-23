package cz.cvut.fit.paa.effenberger.first;

public class Formula {
	private Variable firstVar;
	private Variable secondVar;
	private Variable thirdVar;

	public Formula(Variable f, Variable s, Variable t) {
		this.firstVar = f;
		this.secondVar = s;
		this.thirdVar = t;
	}

	public Variable getFirstVar() {
		return this.firstVar;
	}

	public Variable getSecondVar() {
		return this.secondVar;
	}

	public Variable getThirdVar() {
		return this.thirdVar;
	}

	public int getTotalPrice(boolean f, boolean s, boolean t) {
		return this.firstVar.getPrice(f) + this.secondVar.getPrice(s) + this.thirdVar.getPrice(t);
	}

	public boolean isSatisfied(boolean f, boolean s, boolean t) {
		return this.firstVar.isSatisfied(f) || this.secondVar.isSatisfied(s) || this.thirdVar.isSatisfied(t);
	}

	public boolean isSatisfied(boolean[] input) {
		int o1 = this.firstVar.getOrder() - 1;
		int o2 = this.secondVar.getOrder() - 1;
		int o3 = this.thirdVar.getOrder() - 1;
		return isSatisfied(input[o1], input[o2], input[o3]);

	}

	public int getTotalPrice(boolean[] input) {
		int o1 = this.firstVar.getOrder() - 1;
		int o2 = this.secondVar.getOrder() - 1;
		int o3 = this.thirdVar.getOrder() - 1;
		return getTotalPrice(input[o1], input[o2], input[o3]);
	}

}
