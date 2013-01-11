//@author David Adamo Jr

//Environment is a class to represents the environment for MicroHaskell programs.

import java.util.*;

public class Environment {
	private Environment parent;
	private TreeMap map;
	private int maxIdLength = 2;
	
	public Environment(){
		map = new TreeMap(); //init-env
	}
	
	public Environment(Environment staticParent){
		map = new TreeMap();
		parent = staticParent;
	}
	
	public TreeMap map(){ 
		return map;
	}
	
	/*public Environment newFunction(){
		return new Environment(this);
	}*/
	
	public DenotableValue accessEnv(String id){
		DenotableValue denotVal = (DenotableValue) map.get(id);
		if (denotVal == null){
			/*ErrorMessage.print("identifier " + id + " undeclared");
			return null;*/
			if (parent == null){
				ErrorMessage.print("identifier " + id + " undeclared");
				return null; 
			} else {
				return parent.accessEnv(id);
			}
		} else {
			return denotVal;
		}
	}
	
	public void updateEnvVar(String id, Integer value){
		//update environment with an integer variable
		updateEnv(id, new DenotableValue(Category.VARIABLE, Type.INTEGER, value));
	}
	
	public void updateEnvVar(String id, HaskellList value){
		//update environment with a list variable
		updateEnv(id, new DenotableValue(Category.VARIABLE, Type.LIST, value));
	}
	
	public void updateEnvVar(String id, Boolean value){
		//update environment with a boolean variable
		updateEnv(id, new DenotableValue(Category.VARIABLE, Type.BOOLEAN, value));
	}
	
	/*public void updateEnvVar(String id, SyntaxTree value){
		//update environment with a boolean variable
		updateEnv(id, new DenotableValue(Category.VARIABLE, null, value));
	}*/
	
	public void updateEnvVar(String id){ //add denotable value later
		//update environment with a boolean variable
		updateEnv(id, new DenotableValue(Category.VARIABLE, null, null));
	}
	
	/*public void updateEnvFunc(String id){ //denotable value filled in later
		updateEnv(id, new DenotableValue(Category.FUNCTION, null, null));
	}*/
	
	/*public void updateEnvFunc(String id, DenotableValue denotVal, Environment env){
		updateEnv(id, new DenotableValue(Category.FUNCTION, null, new Function(denotVal.value().denotVal.exprValue(), env)));
                //return this;
	}*/
	
	public Environment updateEnv(String id, DenotableValue denotableValue){
		DenotableValue denotVal = (DenotableValue) map.get(id);
		
		/*if (denotVal != null && denotVal.value() != null) //variable has already been declared
			ErrorMessage.print("Identifier " + id + " previously declared');*/
			
		//you would probably need to loop through the array list and add each of the parameters to the environment
		
		if (id.length() > maxIdLength)
			maxIdLength = id.length();
		
		map.put(id, denotableValue);
                
                return this;
	}
	
	public void print(String functionName){
		/* print out the environment (symbol table) */
		System.out.println("");
		//System.out.println("Identifier table");
		System.out.println("Identifier table for " + functionName);
		System.out.print("--------------------");
		for (int i = 0; i < functionName.length(); i++)
			System.out.print("-");
		System.out.println("");
		System.out.println("");
		System.out.print("Id");
		for (int i = 0; i <= maxIdLength - 2; i++)
			System.out.print(" ");
		System.out.println("Category");
		System.out.print("--");
		for (int i = 0; i <= maxIdLength - 2; i++)
			System.out.print(" ");
		System.out.println("---------");
		Iterator envIterator = map.entrySet().iterator();
		TreeMap functionList = new TreeMap();
		while (envIterator.hasNext()){
			Map.Entry envEntry = (Map.Entry) envIterator.next();
			String entryId = (String) envEntry.getKey();
			System.out.print(entryId);
			for (int i = 0; i <= maxIdLength - entryId.length(); i++)
				System.out.print(" ");
			DenotableValue entryDenotVal = (DenotableValue) envEntry.getValue();
			System.out.println(entryDenotVal);
			if (entryDenotVal.category() == Category.FUNCTION){
				functionList.put(entryId, entryDenotVal.value());
			}
		}
		Iterator funcIterator = functionList.entrySet().iterator();
		while (funcIterator.hasNext()){
			Map.Entry funcEntry = (Map.Entry) funcIterator.next();
			String funcId = (String) funcEntry.getKey();
			((Function) funcEntry.getValue()).print(funcId);
		}
	}
}