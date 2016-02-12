import java.io.*;
import java.util.*;
import java.awt.*;

// Map

public class HashCodeV2 {
	public static void main(String[] args) throws IOException {

		// Andreas är inne och ändrar :D
		
		String annatfilename = "busy_day hej";
		World.init(filename + ".in"); // Testar

		String output = "";

		// Andreas ändrar mera

		Order[] orders = World.getOrders();
//asdf
		// Some good code goes here

		int nrOfRows = 0;
		Random r = new Random();
		int nrOfOrdersDone = 0;
		for (int i=0; i<orders.length; i++) {
			int droneId = i % World.getNrOfDrones();

			//droneId = r.nextInt(World.getNrOfDrones());

			Product[] productsInOrder = orders[i].getProducts();

			int weight = 0;

			int[] productsInLoad = new int[100];
			for (int ff=0; ff<productsInLoad.length-1; ff++) {
				productsInLoad[ff] = -1;
			}
			Product currentProduct = new Product();

			for (int j=0; j<productsInOrder.length; j++) {
				currentProduct = productsInOrder[j];
				weight += currentProduct.getWeight();
				if (weight <= World.getMaxLoad()) {
					boolean breakIt = false;
					for (int w = 0; w<World.getNrOfWarehouses(); w++) {
						if(!breakIt) {
							int wareHouseId = World.getWarehouses()[w].fetch(currentProduct);
							if(wareHouseId != -1) {
								output += "\n" + droneId + " L " + wareHouseId + " " + currentProduct.getType() + " 1";
								nrOfRows++;
								productsInLoad[j] = productsInOrder[j].getType();
								breakIt = true;
							}
						}
					}
				}
			}

			// Drop it like it's hot!
			for(int ff=0; ff<productsInLoad.length-1; ff++) {
				if(productsInLoad[ff] >= 0) {
					output += "\n" + droneId + " D " + i + " " + productsInLoad[ff] + " 1";
					nrOfRows++;
				}
			}
			nrOfOrdersDone++;

		}

		System.out.println("Antal ordrar totalt: " + World.getNrOfOrders());
		System.out.println("Antal ordrar avklarade: " + nrOfOrdersDone);

		// System.out.println(output);
		FileOutput po = new FileOutput(filename, nrOfRows + output);


		// System.out.println("Hämta product " + k1 + " i warehouse " + w1);
		// System.out.println("Hämta product " + k2 + " i warehouse " + w2);

		// System.out.println("Rows: " + World.getRows() + "");
  //       System.out.println("Columns: " + World.getCols() + "");
  //       System.out.println("Max turns: " + World.getmaxTurns() + "");
  //       System.out.println("Number of product types: " + World.getNrOfProductTypes() + "");
  //       System.out.println("Number of drones: " + World.getNrOfDrones() + "");
  //       System.out.println("Number of warehouses: " + World.getNrOfWarehouses() + "");
  //       System.out.println("Product weights: " + World.getProductWeights() + "");
  //       System.out.println("Orders: " + World.getOrders() + "");
  //       System.out.println("Number of orders: " + World.getnrOfOrders() + "");
  //       System.out.println("Max load: " + World.getMaxLoad() + "");
  //       System.out.println("Warehouses: " + World.getWarehouses() + "");
  //       System.out.println("Products: " + World.getProducts() + "");



	}
}

class World {
	private static int rows;
	private static int cols;
	private static int nrOfDrones;
	private static int maxTurns;
	private static int maxLoad;
	private static int nrOfProductTypes;
	private static int[] productWeights;
	private static int nrOfWarehouses;
	private static Warehouse[] warehouses;
	private static Order[] orders;
	private static Product[] products;
	private static int nrOfOrders;

	// Scrapa input-filen
	public static void init(String file) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
			int inputRow = 0;
		    while ((line = br.readLine()) != null) {
		       // process the line.
		    	inputRow++;
		    	if(inputRow == 1) {
		    		String[] w = line.split("\\s+");
					for (int i = 0; i < w.length; i++) {

					    rows = Integer.parseInt(w[0]);
					    cols = Integer.parseInt(w[1]);
					    nrOfDrones = Integer.parseInt(w[2]);
					    maxTurns = Integer.parseInt(w[3]);
					    maxLoad = Integer.parseInt(w[4]);
					}
				} else if(inputRow == 2) {
					nrOfProductTypes = Integer.parseInt(line);
					products = new Product[nrOfProductTypes];
				} else if(inputRow == 3) {
					String[] w = line.split("\\s+");
					productWeights = new int[nrOfProductTypes];
					for (int i = 0; i < w.length; i++) {
						productWeights[i] = Integer.parseInt(w[i]);
						products[i] = new Product(i, Integer.parseInt(w[i]));

					}

				} else if(inputRow == 4) {
					// Skapa Warehouses
					nrOfWarehouses = Integer.parseInt(line);
					warehouses = new Warehouse[nrOfWarehouses];
					for (int i=0; i<nrOfWarehouses; i++) {
						String[] w = br.readLine().split("\\s+");
						int xLocation = Integer.parseInt(w[0]);
						int yLocation = Integer.parseInt(w[1]);
						int[] productStock = new int[nrOfProductTypes];
						String[] p = br.readLine().split("\\s+");
						for (int j=0; j<p.length; j++) {
							productStock[j] = Integer.parseInt(p[j]);
						}
						warehouses[i] = new Warehouse(i, new Point(xLocation, yLocation), productStock);
					}

					nrOfOrders = Integer.parseInt(br.readLine());
					orders = new Order[nrOfOrders];

					for (int i=0; i<nrOfOrders; i++) {
						String[] w = br.readLine().split("\\s+");
						int xLocation = Integer.parseInt(w[0]);
						int yLocation = Integer.parseInt(w[1]);

						int[] productsInOrder = new int[Integer.parseInt(br.readLine())];
						String[] p = br.readLine().split("\\s+");
						for (int j=0; j < p.length; j++) {
							productsInOrder[j] = Integer.parseInt(p[j]);
						}
						Order order = new Order(new Point(xLocation, yLocation), productsInOrder);
						orders[i] = order;
					}

				}
		    }
		}
	}

    public static int getRows() {
        return rows;
    }

    public static int getCols() {
        return cols;
    }

    public static int getNrOfDrones() {
        return nrOfDrones;
    }

    public static int getmaxTurns() {
        return maxTurns;
    }

    public static int getMaxLoad() {
        return maxLoad;
    }

    public static int getNrOfProductTypes () {
        return nrOfProductTypes;
    }

    public static int getNrOfWarehouses () {
        return nrOfWarehouses;
    }

    public static int[] getProductWeights() {
        return productWeights;
    }

    public static int getProductWeight(int i) {
        return productWeights[i];
    }

    public static Order[] getOrders() {
        return orders;
    }

    public static int getNrOfOrders() {
        return nrOfOrders;
    }

    public static Warehouse[] getWarehouses() {
        return warehouses;
    }

    public static Product[] getProducts() {
        return products;
    }

}

class Warehouse {
	private int id;
	private Point location;
	private int[] stock;

	public Warehouse (int id, Point location, int[] stock) {
		this.id = id;
		this.location = location;

		this.stock = new int[stock.length];
		for(int i = 0; i < stock.length; i++) {
			this.stock[i] = stock[i];
		}
	}

	public int getProductStock(int product) {
		return this.stock[product];
	}

	public void setProductStock(int product) {
		// this.stock[product] =
	}

    public boolean isInStock(int product) {
        return stock[product] != 0;
    }

    public void printProductsInStock() {
        for (int i=0; i<stock.length; i++) {
            if(stock[i] != 0) {
                System.out.println(i + ": " + stock[i]);
            }
        }
    }

	public int fetch(Product p) {
        int tid = -1;
        if (stock[p.getType()] > 0) {
            tid = this.id;
            stock[p.getType()]--;
        }
        return tid;
    }

}


class Order {

	private Point destination;
	private Product[] items;
	private boolean[] delivered;

	public Order (Point destination, int[] itemtypes) {
		this.destination = destination;
		delivered = new boolean[itemtypes.length];
		items = new Product[itemtypes.length];
		for(int i = 0; i < items.length; i++) {
			items[i] = new Product(itemtypes[i], World.getProductWeight(itemtypes[i]));
			delivered[i] = false;
		}
	}

	public boolean getCompleted() {
		boolean complete = true;
		for(int i = 0; i < delivered.length; i++) {
			if (!delivered[i]) {
				complete = false;
				break;
			}
		}
		return complete;
	}

	public Product[] getProducts() {
		return items;
	}

}


class Product {

	private int type;
	private int weight;

	public Product (int type, int weight) {
		this.type = type;
		this.weight = weight;
	}

	public Product () {
		this.type = 0;
		this.weight = 0;
	}

	public int getType() {
		return this.type;
	}
	public int getWeight() {
		return this.weight;
	}
}


class Drone {
	private int id;

	public Drone(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
}

class FileOutput {

   public FileOutput(String filename, String str) throws IOException {
      File file = new File(filename);
      // creates the file
      file.createNewFile();
      // creates a FileWriter Object
      FileWriter writer = new FileWriter(file);
      writer.write(str);
      writer.flush();
      writer.close();
   }
}
