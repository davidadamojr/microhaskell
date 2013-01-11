/**
 * @author David Adamo Jr
 * 
 * This class is a MicroHaskell lexical analyzer which reads a PL/0 source 
 * program and outputs the list of tokens comprising the program
 */
public class HSLex {
    
    private static final int MAX_TOKENS = 100;
    
    public static void main (String args []) throws java.io.IOException {
        int i, n;
        Token [] token = new Token [MAX_TOKENS];
        HSLexer lexer = new HSLexer (System . in);
        
        System . out . println ("Source Program");
        System . out . println ("----------------");
        System . out . println ();
        
        n = -1;
        do {
            if (n < MAX_TOKENS)
                token [++n] = lexer . nextToken ();
            else
                ErrorMessage . print (0, "Maximum number of tokens exceeded");
        } while (token [n] . symbol () != TokenClass . EOF);
        
        System . out . println ();
        System . out . println ("List of Tokens");
        System . out . println ("---------------");
        System . out . println ();
        for (i = 0; i < n; i++)
            System . out . println (token [i]);
        System . out . println ();
    }
    
}
