package cz.cvut.fit.paa.effenberger.first;

import java.util.ArrayList;

public class Problem {
	private ArrayList<Formula> formulas;
	private boolean[] input;
	private int prices[];
	private boolean isSet = false;
	private String ID;
	private int size;

	Problem(ArrayList<Formula> formulas, String string, int size) {
		this.formulas = formulas;
		this.ID = string;
		this.size = size;
	}

	public int getPrice() {
		if (!this.isSet) {
			return -1;
		}
		return getTotalPrice();
	}

	public void setValues(boolean[] input) {
		this.input = input;
		this.isSet = true;
	}

	public String getId() {
		return this.ID;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Problem " + this.ID).append("\n");
		sb.append("\t").append("Formulas size: ").append(this.formulas.size());
		sb.append("\n\t");
		for (int i = 0; i < this.formulas.size(); i++) {
			sb.append("(").append(this.formulas.get(i).getFirstVar()).append(" v ")
					.append(this.formulas.get(i).getSecondVar()).append(" v ")
					.append(this.formulas.get(i).getThirdVar()).append(") & ");
		}
		return sb.toString();
	}

	public int getSize() {
		return this.size;
	}

	public void setPrices(int[] prices) {
		this.prices = prices;
	}

	private int getTotalPrice() {
		int result = 0;
		for (int i = 0; i < this.input.length; i++) {
			if (this.input[i]) {
				result += this.prices[i];
			}
		}
		return result;
	}

	public int getSatisfiedCount() {
		int result = 0;
		for (int i = 0; i < this.formulas.size(); i++) {
			Formula akt = this.formulas.get(i);
			if (akt.isSatisfied(this.input)) {
				result++;
			}
		}
		System.out.println("++++++++ result" + result);
		return result;
	}

	public double getRelaxedPrice() {
		int result = getTotalPrice();
		int sats = getSatisfiedCount();
		int allCount = getAllCount();

		if (sats == allCount) {
			return result * 2;
		} else {
			return (result * 1.0) * (allCount * 1.0 / sats * 1.0);
		}
	}

	public int getAllCount() {
		return this.formulas.size();
	}

}
