//Function class

//This class represents functions in MicroHaskell

import java.util.*;

public class Function {
	public SyntaxTree par;
	public SyntaxTree funcBody;
	public Environment env;
	
	public Function(SyntaxTree par, SyntaxTree funcBody, Environment env){
		this.par = par;
		this.funcBody = funcBody;
		this.env = env;
	}
	
	public SyntaxTree formPar(){
		return par;
	}
	
	public SyntaxTree funcBody(){
		return funcBody;
	}
	
	public Environment funcEnv(){
		return env;
	}
	
	public void print(String functionName){
                funcBody.print(functionName);
		env.print(functionName);
	}
}