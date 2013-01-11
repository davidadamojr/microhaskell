//Type is a class to represent the "types" in a MicroHaskell program

public class Type {
	public static final int INTEGER = 0;
	public static final int LIST = 1;
	public static final int BOOLEAN = 2;
	
	public static String toString(int type){
		switch (type){
			case INTEGER: return "integer";
			case LIST: return "list";
			case BOOLEAN: return "boolean";
			default: return null;
		}
	}
}