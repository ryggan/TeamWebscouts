package score;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import objects.ObjectMold;
import objects.Order;
import objects.Warehouse;

public class DataParser {

	private Map<String, Integer> singleData = new HashMap<String, Integer>();
	private Map<String, List<Integer>> listData = new HashMap<String, List<Integer>>();
	private List<Warehouse> warehouses = new ArrayList<Warehouse>();
	private List<Order> orders = new ArrayList<Order>();
	private Scanner in;
	
	public DataParser(String path) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		in = new Scanner(new FileReader(path));
		
		getData();
	}
	
	public void getData() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		
		String[] parameters = {"totalRows", "totalColumns", "droneAmount", "totalTicks", "droneMaxLoad", "productAmount",  };
		
		// First & second row contains parameters
		mapIntegers(singleData, parameters);
		
		//Second row holds product weights
		mapIntegerList(listData, "productWeights");
		
		//Third row holds # of warehouses
		mapIntegers(singleData, new String[]{"warehouseAmount"});
		
		//Load warehouses
		ObjectMold warehouseMold = new ObjectMold(Warehouse.class, "Warehouse");
		for (int i = 0; i < singleData.get("warehouseAmount"); ++i)
			warehouses.add((Warehouse) warehouseMold.create(fetchLines(2)));
		
		mapIntegers(singleData, new String[]{"orderAmount"});
		
		// TODO: Replace String with class constructor (class.getConstructor(Class<?>..))
		//Load orders
		ObjectMold orderMold = new ObjectMold(Order.class, "Order");
		for (int i = 0; i < singleData.get("orderAmount"); ++i)
			orders.add((Order) orderMold.create(fetchLines(3)));
	}
	
	public void mapIntegers(Map<String, Integer> map, String[] keys) {
		for (String key : keys)
			map.put(key, in.nextInt());
		
		in.nextLine();
	}
	
	public void mapIntegerList(Map<String, List<Integer>> map, String key) {
		String line = in.nextLine();
		List<Integer> list = stringToIntegerList(line);
			
		map.put(key, list);
	}
	
	private List<Integer> stringToIntegerList(String s) {
		Scanner scanner = new Scanner(s);
		
		List<Integer> result = new ArrayList<Integer>();
		while(scanner.hasNextInt())
			result.add(scanner.nextInt());
		scanner.close();
		
		return result;
	}
	
	@SuppressWarnings("unused")
	private void printList(List<? extends Object> list) {
		for(Object o : list)
			System.out.print(o.toString() + " ");
	}

	private String fetchLines(int n) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < n; ++i) {
			builder.append(in.nextLine());
			if (i != n - 1)
				builder.append('\n');
		}
		
		return builder.toString();
	}
	
	public Order getRandomOrder() {
		return orders.get(new Random().nextInt(orders.size()));
	}
}
