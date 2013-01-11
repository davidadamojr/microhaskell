//This class represents MicroHaskell lists

import java.util.*;

public class HaskellList {
	
	private ArrayList contents;
	
	public HaskellList(){
		contents = new ArrayList(); //nil
	}
	
	public HaskellList(List tailList){
                contents = new ArrayList();
		for (int i=0; i<tailList.size(); i++){
                    contents.add((Integer) tailList.get(i));
                }
	}
	
	/**
	adds an element to the head of the list
	*/
	public static HaskellList cons(Integer number, HaskellList oldList){
		oldList.contents.add(0, number.intValue());
                return oldList;
	}
	
	/**
	gets the first element of the list - head
	*/
	public static Integer head(HaskellList fullList){
		return (Integer) fullList.contents.get(0);
	}
	
	/**
	gets all elements except the first one - tail
	*/
	public static HaskellList tail(HaskellList fullList){
                List tailList = fullList.contents.subList(1, fullList.contents.size());
		return new HaskellList(tailList);
	}
	
	/**
	checks if two lists are equal
	*/
	public static boolean eqlist(HaskellList firstList, HaskellList secondList){
		if (firstList.contents.equals(secondList.contents)){ //the lists are equal
			return true;
		} else {
			return false;
		}
	}
	
	/**
	checks if two lists are not equal
	*/
	public static boolean neqlist(HaskellList firstList, HaskellList secondList){
		if (!firstList.contents.containsAll(secondList.contents)){
			//hooray! The lists are not equal
			return true;
		} else {
			return false;
		}
	}
	
	public String toString(){
		return contents.toString();
	}
}