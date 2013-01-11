/**
Token class definition

@author David Adamo Jr

This is a class to represent lexical tokens in the MicroHaskell programming language
*/

public class Token {
	
    private TokenClass symbol; //current token
    private String lexeme; //lexeme

    public Token () { }

    public Token (TokenClass symbol) {
        this (symbol, null);
    }

    public Token (TokenClass symbol, String lexeme){
        this . symbol = symbol;
        this . lexeme = lexeme;
    }

    public TokenClass symbol () { return symbol; }

    public String lexeme () { return lexeme; }

    public String toString () {
        switch (symbol) { 
            case LET :		return "(keyword, let)";
            case IN : 		return "(keyword, in)";
            case IF : 		return "(keyword, if)";
            case THEN : 	return "(keyword, then)";
            case ELSE :		return "(keyword, else)";
            case TAIL :		return "(keyword, tail)";
            case HEAD :		return "(keyword, head)";
            case PLUS :		return "(operator, +)";
            case MINUS :	return "(operator, -)";
            case TIMES :	return "(operator, *)";
            case SLASH :	return "(operator, /)";
            case LPAREN :	return "(operator, ()";
            case RPAREN :	return "(operator, ))";
            case AND : 		return "(operator, &&)";
            case OR :		return "(operator, ||)";
            case ASSIGN : 	return "(operator, =)";
			case LTBRACE: 	return "(operator, [)";
			case RTBRACE: 	return "(operator, ])";
            case EQ : 		return "(operator, ==)";
            case NE :		return "(operator, /=)";
            case LT :		return "(operator, <)";
            case LTE :		return "(operator, <=)";
            case GT :		return "(operator, >)";
            case GTE :		return "(operator, >=)";
            case COLON : 	return "(punctuation, :)";
            case ID :		return "(identifier, " + lexeme + ") ";
            case INTEGER :	return "(integer, " + lexeme + ") ";
            case COMMENT :	return "";
            default: 		ErrorMessage . print (0, "Unrecognized token"); return null;
        }
    }
}