//Syntax Tree is a class to represent a node of a binary syntax tree

class SyntaxTree {
	private Object node;
	private SyntaxTree left;
	private SyntaxTree right;
	
	public SyntaxTree(){
		this(null, null, null);
	}
	
	public SyntaxTree(Object nodeValue){
		this(nodeValue, null, null);
	}
	
	public SyntaxTree(Object nodeValue, SyntaxTree leftTree){
		this(nodeValue, leftTree, null);
	}
	
	public SyntaxTree(Object nodeValue, SyntaxTree leftTree, SyntaxTree rightTree){
		node = nodeValue;
		left = leftTree;
		right = rightTree;
	}
	
	//selector functions
	 
	public String root()	  { return node.toString(); }
	public SyntaxTree left()  { return left; }
	public SyntaxTree right() { return right; }
	public int constValue()   { return Integer.parseInt(left().node.toString()); }
	
	//public SyntaxTree procBody() { return left().left().left(); }
	
	//print the tree in Cambridge Polish prefix notation with a heading
	
	public void print(String funcName){
		System.out.println("");
		System.out.println("Syntax Tree for " + funcName);
		System.out.print("--------------------------------------");
		for (int i = 0; i < funcName.length(); i++)
			System.out.print("-");
		System.out.println("");
		System.out.println(this);
	}
	
	 // toString returns the tree in Cambridge Polish prefix notation.

	  public String toString () {
		if (root () . equals ("id"))
		  return left () . root (); // return only id name
		else if (root () . equals ("int"))
		  if (left () . left () == null) // integer constant
			return left () . root ();
		  else                           // constant identifier
			return left () . left () . root ();
		if (left == null) 
		  return root ();
		else if (right == null)
		  return "(" + root () + " " + left + ")";
		else
		  return "(" + root () + " " + left + " " + right + ")";
	  }
}