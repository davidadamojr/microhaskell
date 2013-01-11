//ExpressibleValue.java

//ExpressibleValue is a class to represent the expressible values of identifiers in MicroHaskell programs.

public class ExpressibleValue {
	private int type;
	private Object value;
	
	public ExpressibleValue(int type, Object value){
		this.type = type;
		this.value = value;
	}
	
	public int type(){
		return type;
	}
	
	public Object value(){
		return value;
	}
	
	public String toString(){
		String printString = value.toString();
		return printString;
		/*if (type == Type.INTEGER )
			printString = printString + "(" + value + ")";
		else if (type == Type.LIST) 
			printString = printString + "(" +*/
	}
}