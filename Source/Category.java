//Category is a class to represent the categories of identifiers in a MicroHaskell program

public class Category {
	public static final int VARIABLE = 0;
	public static final int FUNCTION = 1;
	
	public static String toString(int category){
		switch (category){
			case VARIABLE: return "variable";
			case FUNCTION: return "function";
			default: return null;
		}
	}
}