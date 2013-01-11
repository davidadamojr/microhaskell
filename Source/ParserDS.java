/**
 * @author David Adamo
 * 
 * Parser for the MicroHaskell language
 */
import java.io.File;
import java.io.FileInputStream;

class ParserDS {
    
    protected HSLexer lexer;    //lexical analyzer
    protected Token token;      //current token
    
    private void getToken() throws java.io.IOException {
        token = lexer.nextToken();
    }
    
    public ParserDS() throws java.io.IOException {
        /*String fileLocation = "C:\\Users\\TUNDE\\Documents\\NetBeansProjects\\MicroHaskell\\src\\test5.hs";
        FileInputStream fileInput = new FileInputStream(new File(fileLocation));*/
        //lexer = new HSLexer(fileInput);
        lexer = new HSLexer(System.in);
        getToken();
    }  
    
	//program parses MicroHaskell programs
    public SyntaxTree program() throws java.io.IOException {
		SyntaxTree programTree = dec_list();
		return programTree;
    }
    
	//dec_list parses a list of declarations
    public SyntaxTree dec_list() throws java.io.IOException {
        SyntaxTree declistTree, declist2Tree;
		declistTree = dec();
		while (token.symbol() == TokenClass.ID){
			declist2Tree = dec();	
			declistTree = new SyntaxTree("nil", declistTree, declist2Tree);
		}
		
		return declistTree;
    }
    
    public SyntaxTree dec() throws java.io.IOException {
		SyntaxTree idTree, expTree, decTree, dec2Tree, arglistTree;
		decTree = null;
        if (token.symbol() == TokenClass.ID){
            //current token is an identifer and a possible declaration
			idTree = new SyntaxTree("id", new SyntaxTree(token.lexeme()));
            getToken();
			//determine which type of declaration
        
			//check to see if it is an "identifer = exp"
			if (token.symbol() == TokenClass.ASSIGN){
				//probably an "identifier = exp"
				getToken();
				expTree = exp();
				decTree = new SyntaxTree("=", idTree, expTree);
			} else {
				//probably an argument list
				arglistTree = argument_list();
				//check for assignment
				if (token.symbol() == TokenClass.ASSIGN){
					//matched "identifier argument_list ="
					getToken();
					expTree = exp();
					dec2Tree = new SyntaxTree("id", arglistTree);
					decTree = new SyntaxTree(idTree.left().root(), dec2Tree.left(), expTree);
				} else {
					ErrorMessage.print(lexer.position(), "'=' expected.");
				}
			}
		} else {
			ErrorMessage.print(lexer.position(), "Identifier expected.");
		}
		
		return decTree;        
    }
    
    public SyntaxTree argument_list() throws java.io.IOException{
		SyntaxTree arglistTree, arglist2Tree;
		arglistTree = null;
        if (token.symbol() == TokenClass.ID){
			arglistTree = new SyntaxTree("id", new SyntaxTree(token.lexeme()));
            getToken();
            while (token.symbol() == TokenClass.ID){
				arglist2Tree = new SyntaxTree("id", new SyntaxTree(token.lexeme()));
				arglistTree = new SyntaxTree("nil", arglistTree, arglist2Tree);
                getToken();
            }
        } else {
            ErrorMessage.print(lexer.position(), "Identifier expected");
        }
		
		return arglistTree;
    }
    
    public SyntaxTree exp() throws java.io.IOException {
        SyntaxTree expTree, exp1Tree, exp2Tree, declistTree, boolexpTree;
        //check for "let dec-list in exp"
		expTree = null;
        if (token.symbol() == TokenClass.LET){
            //probably a "let dec-list in exp"
            getToken();
            declistTree = dec_list();
            if (token.symbol() == TokenClass.IN){
                getToken();
                expTree = exp();
				expTree = new SyntaxTree("let-in", declistTree, expTree);
            } else {
                ErrorMessage.print(lexer.position(), "'in' expected.");
            }
        } else         
        //not a "let" expression, check for an "if (Boolean-exp) then (exp) else (exp)" expression
        if (token.symbol() == TokenClass.IF){
            //probably an "if (Boolean-exp) then (exp) else (exp)"
            getToken();
            //find the left parentheses
            if (token.symbol() == TokenClass.LPAREN){
                getToken();
                boolexpTree = boolean_exp();
                if (token.symbol() == TokenClass.RPAREN){
                    getToken();
                    if (token.symbol() == TokenClass.THEN){
                        getToken();
                        if (token.symbol() == TokenClass.LPAREN){
                            getToken();
                            exp1Tree = exp();
                            if (token.symbol() == TokenClass.RPAREN){
                                getToken();
                                if (token.symbol() == TokenClass.ELSE){
                                    getToken();
                                    if (token.symbol() == TokenClass.LPAREN){
                                        getToken();
										exp2Tree = exp();
                                        if (token.symbol() == TokenClass.RPAREN){
                                            getToken();
											exp2Tree = new SyntaxTree("else", exp2Tree);
											exp1Tree = new SyntaxTree("then", exp1Tree, exp2Tree);
											expTree = new SyntaxTree("if", boolexpTree, exp1Tree);
                                        } else {
                                            ErrorMessage.print(lexer.position(), "')' expected.");
                                        }
                                    } else {
                                        ErrorMessage.print(lexer.position(), "'(' expected.");
                                    }
                                } else {
                                    ErrorMessage.print(lexer.position(), "'else' expected.");
                                }
                            } else {
                                ErrorMessage.print(lexer.position(), "')' expected.");
                            }                            
                        } else {
                            ErrorMessage.print(lexer.position(), "'(' expected.");
                        }
                    } else {
                        ErrorMessage.print(lexer.position(), "'then' expected.");
                    }
                } else {
                    ErrorMessage.print(lexer.position(), "')' expected.");
                }
            } else {
                ErrorMessage.print(lexer.position(), "'(' expected.");
            }
        } else {
            expTree = boolean_exp();
        }
		
		return expTree;
    }
    
    public SyntaxTree boolean_exp() throws java.io.IOException {
		String op;
        SyntaxTree boolexpTree, boolexp2Tree;
		boolexpTree = relational_exp();
        //check for the boolean operator "&&" or "||"
        while (token.symbol() == TokenClass.AND || token.symbol() == TokenClass.OR){
			if (token.symbol() == TokenClass.AND) op = "&&"; else op = "||";
            getToken();
            boolexp2Tree = relational_exp();
			boolexpTree = new SyntaxTree(op, boolexpTree, boolexp2Tree);
        }
		
		return boolexpTree;
    }
    
    public SyntaxTree relational_exp() throws java.io.IOException {
		String op;
        SyntaxTree relationalexpTree, relationalexp2Tree;
		relationalexpTree = list_exp();
        //check for the "relational-operator"
        switch (token.symbol()){
            case EQ: 
				op = "=="; 
				getToken(); 
				relationalexp2Tree = list_exp(); 
				relationalexpTree = new SyntaxTree(op, relationalexpTree, relationalexp2Tree);
				break;
            case NE: 
				op = "/="; 
				getToken(); 
				relationalexp2Tree = list_exp(); 
				relationalexpTree = new SyntaxTree(op, relationalexpTree, relationalexp2Tree);
				break;
            case LT: 
				op = "<"; 
				getToken(); 
				relationalexp2Tree = list_exp(); 
				relationalexpTree = new SyntaxTree(op, relationalexpTree, relationalexp2Tree);
				break;
            case LTE: 
				op = "<="; 
				getToken(); 
				relationalexp2Tree = list_exp(); 
				relationalexpTree = new SyntaxTree(op, relationalexpTree, relationalexp2Tree);
				break;
            case GT: 
				op = ">"; 
				getToken(); 
				relationalexp2Tree = list_exp(); 
				relationalexpTree = new SyntaxTree(op, relationalexpTree, relationalexp2Tree);
				break;
            case GTE: 
				op = ">="; 
				getToken(); 
				relationalexp2Tree = list_exp(); 
				relationalexpTree = new SyntaxTree(op, relationalexpTree, relationalexp2Tree);
				break;
        }
		
		return relationalexpTree;
    }
    
    public SyntaxTree list_exp() throws java.io.IOException {
		SyntaxTree listexpTree, listexp2Tree, atomicexpTree;
		listexpTree = null;
        if (token.symbol() == TokenClass.LTBRACE){
            //probably an empty list "[]"
            getToken();
            if (token.symbol() == TokenClass.RTBRACE){
                //definitely an empty list "[]"
                getToken();
				listexpTree = new SyntaxTree("[]");
            } else {
                ErrorMessage.print(lexer.position(), "']' expected.");
            }
        } else
        //check for "tail atomic-exp"
        if (token.symbol() == TokenClass.TAIL){
            getToken();
            atomicexpTree = atomic_exp();
			listexpTree = new SyntaxTree("tail", atomicexpTree);
        } else {
            //most likely one of the RHS that begins with primary-exp
            listexpTree = primary_exp();
            //check for "primary-exp : list-exp"
            if (token.symbol() == TokenClass.COLON){
                //probably a "primary-exp : list-exp"
                getToken();
                listexp2Tree = list_exp();
				listexpTree = new SyntaxTree(":", listexpTree, listexp2Tree);
            } 
        }
		
		return listexpTree;
    }
    
    public SyntaxTree primary_exp() throws java.io.IOException {
		String op;
		SyntaxTree primaryexpTree, termTree;
        primaryexpTree = term();
        while (token.symbol() == TokenClass.PLUS || token.symbol() == TokenClass.MINUS){
			if (token.symbol() == TokenClass.PLUS) op = "+"; else op = "-";
            getToken();
            termTree = term();
			primaryexpTree = new SyntaxTree(op, primaryexpTree, termTree);
        }
		
		return primaryexpTree;
    }
    
    public SyntaxTree term() throws java.io.IOException {
		String op;
		SyntaxTree termTree, factorTree;
        termTree = factor();
        while (token.symbol() == TokenClass.TIMES || token.symbol() == TokenClass.SLASH){
			if (token.symbol() == TokenClass.TIMES) op = "*"; else op = "/";
            getToken();
            factorTree = factor();
			termTree = new SyntaxTree(op, termTree, factorTree);
        }
		
		return termTree;
    }
    
    public SyntaxTree factor() throws java.io.IOException {
		SyntaxTree factorTree, atomicexpTree;
        if (token.symbol() == TokenClass.HEAD){
            //check for "[head]" - optional
            getToken();
			atomicexpTree = atomic_exp();
			factorTree = new SyntaxTree("head", atomicexpTree);
        } else {
			factorTree = atomic_exp();
		}
		
		return factorTree;
    }
    
    public SyntaxTree atomic_exp() throws java.io.IOException {
		SyntaxTree atomicexpTree, parameterexpTree;
		atomicexpTree = null;
        //check for integer
        if (token.symbol() == TokenClass.INTEGER){
			atomicexpTree = new SyntaxTree("int", new SyntaxTree(token.lexeme()));
            getToken();
        } else
        //check for left parentheses, could be an "( exp )"
        if (token.symbol() == TokenClass.LPAREN){
            //probably a "( exp )"
            getToken();
            atomicexpTree = exp();
            if (token.symbol() == TokenClass.RPAREN){
                getToken();
            } else {
                ErrorMessage.print(lexer.position(), "expected ')'");
            }
        } else 
        //check for "identifier {parameter-exp}"
        if (token.symbol() == TokenClass.ID){
            //probably an "identifier {parameter-exp}"
			atomicexpTree = new SyntaxTree("id", new SyntaxTree(token.lexeme()));
            getToken();
            while (token.symbol() == TokenClass.INTEGER || token.symbol() == TokenClass.LPAREN 
                    || token.symbol() == TokenClass.ID){
                parameterexpTree = parameter_exp();
				atomicexpTree = new SyntaxTree("nil", atomicexpTree, parameterexpTree);
            }
        }
		
		return atomicexpTree;
    }
    
    public SyntaxTree parameter_exp() throws java.io.IOException {
        SyntaxTree parameterexpTree = null;
        //check for "( exp )"
        if (token.symbol() == TokenClass.LPAREN){
            //probably an "( exp )"
            getToken();
            parameterexpTree = exp();
            if (token.symbol() == TokenClass.RPAREN){
                getToken();
            } else {
                ErrorMessage.print(lexer.position(), "')' expected.");
            }
        } else
        //check for "integer"
        if (token.symbol() == TokenClass.INTEGER) {
            //most likely an "integer"
			parameterexpTree = new SyntaxTree("int", new SyntaxTree(token.lexeme()));
            getToken();
        } else 
        //check for "identifier"
        if (token.symbol() == TokenClass.ID){
            //most likely an "identifier"
			parameterexpTree = new SyntaxTree("id", new SyntaxTree(token.lexeme()));
            getToken();
        } else {
            ErrorMessage.print(lexer.position(), "'(', integer or identifier expected");
        }   
		
		return parameterexpTree;
    }
}