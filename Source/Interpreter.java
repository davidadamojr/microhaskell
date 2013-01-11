//Interpreter.java

//Interpreter is a class to represent an interpreter for the PL/0 programming language, traverses the syntax tree constructed by the parser

public class Interpreter {
	
	protected ExpressibleValue expValue; //holds the final result
	protected Environment env;
	
	public Interpreter(){
                env = new Environment();
	}
	
	//m interprets MicroHaskell programs
	public void m(SyntaxTree syntaxTree){
		d(syntaxTree);
                 env.print("main program");
                 System.out.println("");
                 System.out.println("Final Output");
                 System.out.println("-------------");
                 System.out.println(env.accessEnv(syntaxTree.left().toString()).value());
	}
	
	//d interprets declarations
	public void d(SyntaxTree syntaxTree){
		if (syntaxTree.root().equals("nil")){
			d(syntaxTree.left());
			d(syntaxTree.right());
		}
		else if (syntaxTree.root().equals("=")){
			ExpressibleValue expression = e(syntaxTree.right(), env);
			//DenotableValue denotVal = new DenotableValue(Category.VARIABLE, expression.type(), expression.value()); 
			switch(expression.type()){
				case Type.INTEGER:
					env.updateEnvVar(syntaxTree.left().toString(), (Integer) expression.value());
					break;
				case Type.LIST:
					env.updateEnvVar(syntaxTree.left().toString(), (HaskellList) expression.value());
					break;
				case Type.BOOLEAN:
					env.updateEnvVar(syntaxTree.left().toString(), (Boolean) expression.value());
					break;
			}
                       
		} else { //syntaxTree.root().equals("f")
			//function definition and construction of funcDenot
                        SyntaxTree parameters = syntaxTree.left();
                        Environment funcEnv = new Environment(env);
                        Function funcDenot = new Function(parameters, syntaxTree.right(), funcEnv);
                        DenotableValue denotVal = new DenotableValue(Category.FUNCTION, null, funcDenot);                     		
                        env.updateEnv(syntaxTree.root(), denotVal);
		}
                
	}
	
	//e interprets expressions
	public ExpressibleValue e(SyntaxTree syntaxTree, Environment env){
		if (syntaxTree.root().equals("let-in")){
			//let-in expression
			d(syntaxTree.left());
			return e(syntaxTree.right(), env);
		}
		else if (syntaxTree.root().equals("id")){
			//variable access
			DenotableValue denotVal = env.accessEnv(syntaxTree.left().root()); //get the value of the identifier
			if (denotVal.category() == Category.FUNCTION){
				ErrorMessage.print("Cannot access value of function");
			} else {
				return denotVal.exprValue();
			}
		}
		else if (syntaxTree.root().equals("if")) {
			//if statment
			ExpressibleValue expression1 = e(syntaxTree.left(), env);
			if (expression1.type() != Type.BOOLEAN){
				ErrorMessage.print("Comparison expression must return boolean type");
			} else {
				ExpressibleValue expression2 = e(syntaxTree.right().left(), env);
				ExpressibleValue expression3 = e(syntaxTree.right().right().left(), env);
				
				if (expression2.type() != expression3.type()){
					ErrorMessage.print("Error!");
				} else if ((Boolean) expression1.value() == true){
					return expression2;
				} else {
					//expression 1 is false
					return expression3;
				}
			}
		} else if (syntaxTree.root().equals("<") || syntaxTree.root().equals(">") || syntaxTree.root().equals("<=") || syntaxTree.root().equals(">=")){
			//relational operators
			ExpressibleValue expression1 = e(syntaxTree.left(), env);
			ExpressibleValue expression2 = e(syntaxTree.right(), env);
			if (expression1.type() != Type.INTEGER || expression2.type() != Type.INTEGER){
				ErrorMessage.print("Can only compare integers with '>'");
			} else {
				switch(syntaxTree.root()){
					case "<":
						return new ExpressibleValue(Type.BOOLEAN, (Integer) expression1.value() < (Integer) expression2.value());
						//break;
					case ">":
						return new ExpressibleValue(Type.BOOLEAN, (Integer) expression1.value() > (Integer) expression2.value());
						//break;
					case "<=":
						return new ExpressibleValue(Type.BOOLEAN, (Integer) expression1.value() <= (Integer) expression2.value());
						//break;
					case ">=":
						return new ExpressibleValue(Type.BOOLEAN, (Integer) expression1.value() < (Integer) expression2.value());
						//break;
				}
			}
		} else if (syntaxTree.root().equals("+") || syntaxTree.root().equals("-") || syntaxTree.root().equals("*") || syntaxTree.root().equals("/")){
			//arithmetic operations
			ExpressibleValue expression1 = e(syntaxTree.left(), env);
			ExpressibleValue expression2 = e(syntaxTree.right(), env);
			if (expression1.type() != Type.INTEGER || expression2.type() != Type.INTEGER){
				ErrorMessage.print("Integer types expected for arithmetic operations.");
			} else {
				switch (syntaxTree.root()){
					case "+":
						return new ExpressibleValue(Type.INTEGER, (Integer) expression1.value() + (Integer) expression2.value());
						//break;
					case "-":
						return new ExpressibleValue(Type.INTEGER, (Integer) expression1.value() - (Integer) expression2.value());
						//break;
					case "*":
						return new ExpressibleValue(Type.INTEGER, (Integer) expression1.value() * (Integer) expression2.value());
						//break;
					case "/":
						if ((Integer) expression2.value() == 0){
							ErrorMessage.print("Error: division by zero.");
							return null;
						}
						return new ExpressibleValue(Type.INTEGER, (Integer) expression1.value() / (Integer) expression2.value());
				}
			}
		} else if (syntaxTree.root().equals("==")){
			//equality
			ExpressibleValue expression1 = e(syntaxTree.left(), env);
			ExpressibleValue expression2 = e(syntaxTree.right(), env);
			if (expression1.type() == Type.INTEGER && expression2.type() == Type.INTEGER){
				//equality for integers
				return new ExpressibleValue(Type.BOOLEAN, (Integer) expression1.value() == (Integer) expression2.value());
			} else if (expression1.type() == Type.LIST && expression2.type() == Type.LIST){
				//equality for lists
				return new ExpressibleValue(Type.BOOLEAN, HaskellList.eqlist((HaskellList) expression1.value(), (HaskellList) expression2.value()));
			} else {
				ErrorMessage.print("Type error.");
				return null;
			}
		} else if (syntaxTree.root().equals("/=")){
			//inequality
			ExpressibleValue expression1 = e(syntaxTree.left(), env);
			ExpressibleValue expression2 = e(syntaxTree.right(), env);
			if (expression1.type() == Type.INTEGER && expression2.type() == Type.INTEGER){
				//equality for integers
				return new ExpressibleValue(Type.BOOLEAN, (Integer) expression1.value() != (Integer) expression2.value());
			} else if (expression1.type() == Type.LIST && expression2.type() == Type.LIST){
				//equality for lists
				return new ExpressibleValue(Type.BOOLEAN, HaskellList.neqlist((HaskellList) expression1.value(), (HaskellList) expression2.value()));
			} else {
				ErrorMessage.print("Type error.");
			}	
		} else if (syntaxTree.root().equals("&&")){
			//&&
			ExpressibleValue expression1 = e(syntaxTree.left(), env);
			if (expression1.type() != Type.BOOLEAN){
				ErrorMessage.print("Type error: Boolean expected.");
				return null;
			} else if (expression1.value() == false){
				return new ExpressibleValue(Type.BOOLEAN, expression1.value());
			} else {
				ExpressibleValue expression2 = e(syntaxTree.right(), env);
				if (expression2.type() == Type.BOOLEAN){
					return new ExpressibleValue(Type.BOOLEAN, expression2.value());
				} else {
					ErrorMessage.print("Type error: Boolean expected.");
					return null;
				}
			}	
		} else if (syntaxTree.root().equals("||")){
			ExpressibleValue expression1 = e(syntaxTree.left(), env);
			if (expression1.type() != Type.BOOLEAN){
				ErrorMessage.print("Type error: Boolean expected.");
				return null;
			} else if (expression1.value() == true) {
				return new ExpressibleValue(Type.BOOLEAN, expression1.value());
			} else {
				ExpressibleValue expression2 = e(syntaxTree.right(), env);
				if (expression2.type() == Type.BOOLEAN){
					return new ExpressibleValue(Type.BOOLEAN, expression2.value());
				} else {
					ErrorMessage.print("Type error: Boolean expected.");
					return null;
				}
			}
		} else if (syntaxTree.root().equals(":")){
			//cons
			ExpressibleValue expression1 = e(syntaxTree.left(), env);
			ExpressibleValue expression2 = e(syntaxTree.right(), env);
			if (expression1.type() != Type.INTEGER || expression2.type() != Type.LIST){
				ErrorMessage.print("Type error: integers expected for cons");
				return null;
			} else {
				return new ExpressibleValue(Type.LIST, HaskellList.cons((Integer) expression1.value(), (HaskellList) expression2.value()));
			}
		} else if (syntaxTree.root().equals("[]")) {
			//empty list
			return new ExpressibleValue(Type.LIST, new HaskellList());
		} else if (syntaxTree.root().equals("tail")) {
			//tail of list
			ExpressibleValue expression1 = e(syntaxTree.left(), env);
			if (expression1.type() != Type.LIST){
				ErrorMessage.print("Type error: list type expected.");
				return null;
			} else {
				return new ExpressibleValue(Type.LIST, HaskellList.tail((HaskellList) expression1.value()));
			}
		} else if (syntaxTree.root().equals("head")) {
			//head of list
			ExpressibleValue expression1 = e(syntaxTree.left(), env);
			if (expression1.type() != Type.LIST){
				ErrorMessage.print("Type error: list type expected.");
				return null;
			} else {
				return new ExpressibleValue(Type.INTEGER, HaskellList.head((HaskellList) expression1.value()));
			}
                } else if (syntaxTree.root().equals("nil")){
                    //function call
                    DenotableValue denotVal = env.accessEnv(syntaxTree.left().toString());
                    if (denotVal.category() != Category.FUNCTION){
                        ErrorMessage.print("Type error: function expected");
                        return null;
                    } else {
                        Function function = (Function) denotVal.value();
                        //update the function's environment
                        pm(syntaxTree.right(), function.formPar(), function.funcEnv());
                        return e(function.funcBody(), function.env);
                    }
		} else { //integer constant - syntaxTree.root.().equals("int")
			//System.out.println(syntaxTree.left());
			return new ExpressibleValue(Type.INTEGER, syntaxTree.constValue());
		}
		
		return null;
	} 
	
	//pm interprets actual parameters
	public Environment pm(SyntaxTree actualParams, SyntaxTree formalParams, Environment funcEnv){
            //parameter matching
            if (actualParams.root().equals("int")||actualParams.root().equals("id")||actualParams.root().equals("tail")||actualParams.root().equals("head")){
                //only one paramete, 
                ExpressibleValue actualExp = e(actualParams, funcEnv);
                DenotableValue denotVal = new DenotableValue(Category.VARIABLE, actualExp.type(), actualExp.value());
                funcEnv.updateEnv(formalParams.left().toString(), denotVal);
            } else if (actualParams.root().equals("nil")){
                //more than one parameter
                pm(actualParams.left(), formalParams.left(), funcEnv);
                pm(actualParams.right(), formalParams.right(), funcEnv);
            } else {
                ErrorMessage.print("Error: parameter mismatch");
            }
            
            return funcEnv;
	}
}