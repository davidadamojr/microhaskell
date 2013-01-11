//DenotableValue.java

//DenotableValue is a class to represent the denotable values of identifiers

public class DenotableValue {
	private int category;
	private Integer type;
	private Object value;
	
	public DenotableValue(int category, Integer type, Object value){
		this.category = category;
		this.type = type;
		this.value = value;
	}
	
	public int category(){
		return category;
	}
	
	public int type(){
		return type;
	}
	
	public Object value(){
		return value;
	}
	
	public ExpressibleValue exprValue(){
		ExpressibleValue expValue = new ExpressibleValue(type, value);
		return expValue;
	}

	public String toString(){
		String printString = Category.toString(category);
		/*if (category == Category.VARIABLE || category == Category.FUNCTION)
			printString = printString + "(" + value + ")";*/
		return printString + "(" + value.toString() + ")";
	}
}