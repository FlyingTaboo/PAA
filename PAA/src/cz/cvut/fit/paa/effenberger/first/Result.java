package cz.cvut.fit.paa.effenberger.first;

import java.time.Duration;
import java.util.ArrayList;

public class Result {
	private int ID;
	private int capacity;
	private int n;
	private int priceOfResult;
	@SuppressWarnings("unused")
	private Duration duration;
	private ArrayList<ArrayList<Integer>> result;
	private double epsilon;
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(ID).append(" ");
		sb.append(n).append(" ");
		sb.append(priceOfResult);
		if (result != null){
			for(int i=0;i<result.size();i++){
				sb.append("  ");
				for(int j=0;j<result.get(i).size();j++){
					sb.append(result.get(i).get(j)).append(" ");
				}
				sb.deleteCharAt(sb.length()-1);
			}
		}
		//sb.append(" Time: " + duration.toMillis());
		return sb.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String toString(ArrayList<Item> items) throws Exception{
		if (items == null || items.isEmpty()) throw new Exception(); //TODO
		@SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayList<ArrayList<Integer>> retVal = new ArrayList();
		if (result == null) return null;
 		for(int i=0; i<result.size();i++){
			ArrayList<Integer> sortedList = new ArrayList<Integer>(n);
			for(int j=0; j<n;j++) sortedList.add(new Integer(0));
			if (items.size() != result.get(i).size()){
				throw new Exception();
			}
			for(int j=0;j<result.get(i).size();j++){
				sortedList.set(items.get(j).getOrder(), result.get(i).get(j));
			}
			retVal.add((ArrayList) sortedList.clone());
		}
		result = retVal;
		
		// projhit v cyklu a radit na spravne pozice hodnoty dle order u items
		return this.toString();
	}
	
	public Result(int ID, int n, int capacity){
		this.ID = ID;
		this.capacity = capacity;
		this.n = n;
		this.priceOfResult = Integer.MIN_VALUE;
	}
	
	public Result(int ID, int n, int capacity, double e) {
		this.ID = ID;
		this.capacity = capacity;
		this.n = n;
		this.priceOfResult = Integer.MIN_VALUE;
		this.epsilon = e;
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addResult(ArrayList<Integer> result, int priceOfResult, int weight){
		if (weight <= capacity && priceOfResult != 0){
			if (this.priceOfResult < priceOfResult){
				this.priceOfResult = priceOfResult;
				this.result = new ArrayList();
				this.result.add((ArrayList<Integer>) result.clone());
			}else if (this.priceOfResult == priceOfResult && !this.result.contains(result)){
				this.result.add((ArrayList<Integer>) result.clone()); 
			}
		}
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public float getTotalPrice() {
		return this.priceOfResult;
	}
	
	public int getTotalIntPrice() {
		return new Float(this.priceOfResult).intValue();
	}
	
	

}
