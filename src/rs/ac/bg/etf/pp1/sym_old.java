//package rs.ac.bg.etf.pp1;
//
//public class sym {
//	
//	//Keywords (kljucne reci iz pdfa)
//	public static final int PROG = 1;
//	public static final int PRINT = 2;
//	public static final int RETURN = 3;
//	
//	//Identifiers (Identifikatori:ident iz pdfa)
//	public static final int IDENT = 4;
//	
//	//Constants (Celobrojne konstante: number iz pdfa)
//	public static final int NUMBER = 5;
//	
//	//Operators (Operatori: + = , ; ( ) { } iz pdfa)
//	public static final int PLUS = 6;
//	public static final int EQUAL = 7;
//	public static final int COMMA = 8;
//	public static final int SEMI = 9;
//	public static final int LPAREN = 10;
//	public static final int RPAREN = 11;
//	public static final int LBRACE = 12;
//	public static final int RBRACE = 13;
//	
//	public static final int EOF = 14;
//	public static final int VOID = 15;
//	
//}

package rs.ac.bg.etf.pp1;

public class sym_old {
	
	//Keywords (kljucne reci iz MJspecifikacije)
	public static final int PROGRAM = 1;
	public static final int BREAK = 2;
	public static final int CLASS = 3;
	public static final int ELSE = 4;
	public static final int CONST = 5;
	public static final int IF = 6;
	public static final int WHILE = 7;
	public static final int NEW = 8;
	public static final int PRINT = 9;
	public static final int READ = 10;
	public static final int RETURN = 11;
	public static final int VOID = 12;
	public static final int EXTENDS = 13;
	public static final int CONTINUE = 14;
	public static final int FOREACH = 15;
	
	//Types of tokens (vrste tokena iz MJspecifikacije)
	public static final int IDENT = 16;
	public static final int NUMCONST = 17;
	public static final int CHARCONST = 18;
	public static final int BOOLCONST = 19;
	
	//Operators 
	public static final int PLUS = 20;
	public static final int MINUS = 21;
	public static final int TIMES = 22;
	public static final int DIVISION = 23;
	public static final int MODULES = 24;
	public static final int EQUALTO = 25;
	public static final int NOTEQUALTO = 26;
	public static final int GREATERTHAN = 27;
	public static final int GREATERTHANOREQUALTO = 28;
	public static final int LESSTHAN = 29;
	public static final int LESSTHANOREQUALTO = 30;
	public static final int LOGICALAND = 31;
	public static final int LOGICALOR = 32;
	public static final int EQUAL = 33;
	public static final int INCREMENT = 34;
	public static final int DECREMENT = 35;
	public static final int SEMI = 36;
	public static final int COLON = 37;
	public static final int COMMA = 38;
	public static final int POINT = 39;
	public static final int LPAREN = 40;
	public static final int RPAREN = 41;
	public static final int LSQUARE = 42;
	public static final int RSQUARE = 43;
	public static final int LBRACE = 44;
	public static final int RBRACE = 45;
	public static final int ARROWFUNC = 46;
	
	//Comments
	public static final int COMMENT = 47;
	
	public static final int EOF = 48;
	
}
