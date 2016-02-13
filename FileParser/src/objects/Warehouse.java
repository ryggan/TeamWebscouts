package objects;

import java.util.ArrayList;
import java.util.List;

import util.Position;

public class Warehouse {
	
	public Position pos;
	private List<Integer> products;
	
	public Warehouse(Integer x, Integer y, ArrayList<Integer> products) {
		this.pos = new Position(x, y);
		this.products = products;
	}
	
	public List<Integer> getProducts() {
		return this.products;
	}
	
}