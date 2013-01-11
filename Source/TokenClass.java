/**
Token class enumeration definition

TokenClass is an enumeration to represent lexical token classes in the MicroHaskell language.
*/

public enum TokenClass {
  EOF, COMMENT,
  // keywords
  LET, IN, IF, THEN, ELSE, TAIL, HEAD,
  // punctuation
  COLON,
  // operators
  PLUS, MINUS, TIMES, SLASH, LPAREN, RPAREN, AND, OR, ASSIGN, EQ, NE, LT, LTE, GT, GTE, RTBRACE, LTBRACE,
  // ids and integers
  ID, INTEGER
}