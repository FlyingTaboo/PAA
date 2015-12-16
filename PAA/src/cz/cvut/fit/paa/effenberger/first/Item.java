package cz.cvut.fit.paa.effenberger.first;

public class Item implements Comparable<Item>{
	private int price;
	private int weight;
	private float ratio;
	private int order;
	private int FTAPSprice;
	
	public Item(int price, int weight, int order){
		this.price = price;
		this.weight = weight;
		this.ratio = price/weight;
		this.order = order;
		this.FTAPSprice = price;
	}
	
	public Item(int FTAPSprice, int price, int weight, int order){
		this.FTAPSprice = FTAPSprice;
		this.price = price;
		this.weight = weight;
		this.ratio = price/weight;
		this.order = order;
	}
	
	public int  getPrice(){
		return this.price;
	}
	
	public int getFTAPSprice(){
		return this.FTAPSprice;
	}
	
	public int getWeight(){
		return this.weight;
	}
	public float getRatio(){
		return this.ratio;
	}
	public int getOrder(){
		return this.order;
	}
	
	@Override
	public boolean equals(Object second){
		if (!(second instanceof Item)){
			return false;
		}
		if (this.price == ((Item) second).getPrice() &&
				this.weight == ((Item) second).getWeight() &&
				this.order == ((Item) second).getOrder()){
			return true;
		}
		
		return false;
	}

	@Override
	public int compareTo(Item o) {
		if (this.ratio > o.getRatio()) return -1;
		if (this.ratio == o.getRatio()) return -1;
		return 1;
	}
	
	@Override
	public String toString(){
		return "[" +this.price + " " + this.weight + " " + this.ratio + " " + this.order + "]";
	}
	
	
	
}
