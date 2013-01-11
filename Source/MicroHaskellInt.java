/**
@author David Adamo Jr

This program is an interpreter for MicroHaskell.
*/

public class MicroHaskellInt {
	
	public static void main(String args[]) throws java.io.IOException {
		System.out.println("Source Program");
		System.out.println("--------------");
		System.out.println("");
		
		ParserDS microHaskell = new ParserDS();
		SyntaxTree syntaxTree = microHaskell.program();
		System.out.println("");
		syntaxTree.print("main program");
		System.out.println("");
		Interpreter interpreter = new Interpreter();
		interpreter.m(syntaxTree);
		//System.out.println(interpreter.store());
	}
}



