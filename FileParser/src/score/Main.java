package score;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, IOException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		DataParser parser = new DataParser("busy_day.in");
		
		System.out.printf("Printing a random Order:\n%s", parser.getRandomOrder());
	}
}
