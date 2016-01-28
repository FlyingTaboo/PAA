package cz.cvut.fit.paa.effenberger.first;

public class Result {
	private String id;

	private int satisfiedCount = 0;
	private int allCount = 0;
	private double solution = 0.0;
	private long time = 0;
	private int count = 0;

	public Result(String id) {
		this.id = id;
	}

	public void addNew(int satisfiedCount, int allCount, double solution, long l) {
		this.satisfiedCount += satisfiedCount;
		this.allCount += allCount;
		this.solution += solution;
		this.time += l;
		this.count++;
	}

	public String getId() {
		return this.id;
	}

	public int getSatisfiedCount() {
		return this.satisfiedCount / this.count;
	}

	public int getAllCount() {
		return this.allCount / this.count;
	}

	public double getSolution() {
		return this.solution / this.count;
	}

	public long getTime() {
		return this.time / this.count;
	}

	@Override
	public String toString() {
		return this.id + "\t" + getSatisfiedCount() + "/" + getAllCount() + "\t" + getSolution() + "\t" + getTime();
	}
}
