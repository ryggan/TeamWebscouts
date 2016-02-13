package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Position;

public class Order {

	public Position pos;
	/** Key is product ID, value is amount */
	Map<Integer, Integer> products;
	
	public Order(Integer x, Integer y, ArrayList<Integer> orderList) {
		this.pos = new Position(x, y);
		this.products = parseProductList(orderList);
	}
	
	public boolean finished() {
		return products.isEmpty();
	}
	
	public void take(int id, int amount) {
		int newValue = products.get(id) - amount;
		if (newValue < 0)
			throw new IllegalArgumentException(String.format(
					"Can't remove %d products of type %d from order[%d, %d]. It only needs %d",
					amount, id, pos.x, pos.y, products.get(id)));
		
		products.put(id, newValue);
		
		//If there are no more, we have satisfied the need for that particular product
		if (newValue == 0)
			products.remove(id);
	}
	
	private Map<Integer, Integer> parseProductList(List<Integer> list) {
		Map<Integer, Integer> tempMap = new HashMap<Integer, Integer>();
		
		for (int id : list) {
			Integer prevAmount = tempMap.get(id) == null ? 0 : tempMap.get(id);
			if (prevAmount == 0) {
				tempMap.put(id, 0);
			}
			
			tempMap.put(id, prevAmount + 1);
		}
		
		return tempMap;
	}
	
	public String toString() {
		return String.format("Order {Position = [%d, %d]; Amount of products: %d}", pos.x, pos.y, products.size());
	}
}
