package objects;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ObjectMold {
	
	private Class<?>[] classes;
	Class<?> moldClass;
	
	public ObjectMold(Class<?> moldClass, String object) throws IOException, ClassNotFoundException {
		Scanner data = new Scanner(new FileReader(getClassName(moldClass.getName()) + ".txt"));
		this.moldClass = moldClass;
		
		ArrayList<Class<?>> tempList = new ArrayList<Class<?>>();
		
		while(data.hasNext()) {
			//Next class to be loaded in
			String argumentClass = data.next();
			if (argumentClass.equals("None"))
				tempList.add(null);
			else {
				tempList.add(Class.forName(argumentClass));
			}
		}
		
		classes = new Class[tempList.size()];
		for (int i = 0; i < tempList.size(); ++i) {
			classes[i] = tempList.get(i);
		}
		
		data.close();
	}
	
	
	public Object create(String data) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Scanner scanner = new Scanner(data);
		
		List<Object> arguments = new ArrayList<Object>();
		
		int index = 0;
		
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Class<?> classType = classes[index];
			index++;
			
			
			if (classType == null)
				continue;
			
			if (classType.equals(Integer.class)) {
				Scanner lineScanner = new Scanner(line);
				while(lineScanner.hasNextInt()) {
					int next = lineScanner.nextInt();
					arguments.add(next);
				}
				
				lineScanner.close();
				
			} else if (classType.equals(List.class)) {
				arguments.add(stringToIntegerList(line));
			}
		}
		
		Object[] argumentArray = getArrayFromList(arguments);
		Class<?>[] classArray = new Class<?>[argumentArray.length];
		for (int i = 0; i < classArray.length; ++i) {
			classArray[i] = argumentArray[i].getClass();
		}
		
		
		Constructor<?> constructor = moldClass.getConstructor(classArray);
		scanner.close();
		
		return constructor.newInstance(argumentArray);
	}
	
	private List<Integer> stringToIntegerList(String s) {
		Scanner scanner = new Scanner(s);
		
		List<Integer> result = new ArrayList<Integer>();
		while(scanner.hasNextInt())
			result.add(scanner.nextInt());
		scanner.close();
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	/** NOTE: Removes null elements */
	private <T> T[] getArrayFromList(List<T> list) {
		T[] array = (T[]) new Object[list.size()];
		for (int i = 0; i < list.size(); ++i)
			if (list.get(i) != null)
				array[i] = list.get(i);
		
		return array;
	}
	
	private String getClassName(String classPath) {
		String[] temp = classPath.split("\\.", 0);
		return temp[temp.length - 1];
	}
}
