
package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	//ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	//ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" "		{ }
"\b"	{ }
"\t"	{ }
"\r\n"	{ }
"\f"	{ }

"program"	{ return new_symbol(sym.PROGRAM, yytext()); }
"break"		{ return new_symbol(sym.BREAK, yytext()); }
"class"		{ return new_symbol(sym.CLASS, yytext()); }
"else"		{ return new_symbol(sym.ELSE, yytext()); }
"const"		{ return new_symbol(sym.CONST, yytext()); }
"if"		{ return new_symbol(sym.IF, yytext()); }
"while"		{ return new_symbol(sym.WHILE, yytext()); }
"new"		{ return new_symbol(sym.NEW, yytext()); }
"print"		{ return new_symbol(sym.PRINT, yytext()); }
"read"		{ return new_symbol(sym.READ, yytext()); }
"return"	{ return new_symbol(sym.RETURN, yytext()); }
"void"		{ return new_symbol(sym.VOID, yytext()); }
"extends"	{ return new_symbol(sym.EXTENDS, yytext()); }
"continue"	{ return new_symbol(sym.CONTINUE, yytext()); }
"foreach"	{ return new_symbol(sym.FOREACH, yytext()); }
"+"			{ return new_symbol(sym.PLUS, yytext()); }
"-"			{ return new_symbol(sym.MINUS, yytext()); }
"*"			{ return new_symbol(sym.TIMES, yytext()); }
"/"			{ return new_symbol(sym.DIVIDE, yytext()); }
"%"			{ return new_symbol(sym.MOD, yytext()); }
"=="		{ return new_symbol(sym.EQUALTO, yytext()); }
"!="		{ return new_symbol(sym.NOTEQUALTO, yytext()); }
">"			{ return new_symbol(sym.GT, yytext()); }
">="		{ return new_symbol(sym.GTE, yytext()); }
"<"			{ return new_symbol(sym.LT, yytext()); }
"<="		{ return new_symbol(sym.LTE, yytext()); }
"&&"		{ return new_symbol(sym.LOGAND, yytext()); }
"||"		{ return new_symbol(sym.LOGOR, yytext()); }
"="			{ return new_symbol(sym.EQUAL, yytext()); }
"++"		{ return new_symbol(sym.INC, yytext()); }
"--"		{ return new_symbol(sym.DEC, yytext()); }
";"			{ return new_symbol(sym.SEMI, yytext()); }
":"			{ return new_symbol(sym.COLON, yytext()); }
","			{ return new_symbol(sym.COMMA, yytext()); }
"."			{ return new_symbol(sym.POINT, yytext()); }
"("			{ return new_symbol(sym.LPAREN, yytext()); }
")"			{ return new_symbol(sym.RPAREN, yytext()); }
"["			{ return new_symbol(sym.LSQUARE, yytext()); }
"]"			{ return new_symbol(sym.RSQUARE, yytext()); }
"{"			{ return new_symbol(sym.LBRACE, yytext()); }
"}"			{ return new_symbol(sym.RBRACE, yytext()); }
"=>"		{ return new_symbol(sym.ARROWFUNC, yytext()); }

"//" { yybegin(COMMENT);}
<COMMENT> . {yybegin(COMMENT);}
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

[0-9]+	{ return new_symbol(sym.NUMCONST, new Integer (yytext())); }
("true"|"false")	{ if(yytext().equals("true")){ return new_symbol(sym.BOOLCONST, true); } else{ return new_symbol(sym.BOOLCONST, false); }}
"'"[ -~]"'"		{ return new_symbol(sym.CHARCONST, yytext().charAt(1)); }
([a-z]|[A-Z])[a-zA-Z0-9_]*	{ return new_symbol (sym.IDENT, yytext()); }

.	{ System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)); }