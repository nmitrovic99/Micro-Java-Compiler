package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor{
	
	public static Struct boolType = null;
	boolean errorDetected = false;
	
	private Struct varStruct=null;
	private String varSingleName="";
	private List<String> varMultName;
	private boolean arrSquareSingle=false;
	private boolean arrSquareMatrixSingle=false;
	private List<Boolean> arrSquareMult;
	private List<Boolean> arrSquareMatrixMult;
	
	//za deklaraciju konstanti
	private String constSingleName="";
	private List<String> constMultName;
	//svaka procitana konstanta Constant ce ici prvo u ovu promenljivu pa je posle iz nje stavljam u constSingleValue ili constMultValue
	private int constCurrentValue;
	private int constSingleValue;
	private List<Integer> constMultValue;
	private int constSingleType;
	private List<Integer> constMultType;
	
	private List<Struct> actualParamsStructs;
	
	//za [Designator, Designator, Designator, ...] = Designator
	private List<String> designatorsName;
	private int formalParamsNumber=0;
	
	Obj desigSingleIdentObj = null;
	
	Obj currentMethod = null;
	Obj currentCalledFunc = null;
	boolean returnFound = false;
	
	int varDeclCount=0;
	int numOfGlobalVars;
	
	int numOfMatrixs=0;
	
	Logger log = Logger.getLogger(getClass());
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public void init() {
		boolType=new Struct(Struct.Bool);
//		Tab.insert(Obj.Type, "bool", boolType);
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", boolType));
		varMultName=new ArrayList<String>();
		arrSquareMult=new ArrayList<Boolean>();
		arrSquareMatrixMult=new ArrayList<Boolean>();
		constMultName=new ArrayList<String>();
		constMultValue=new ArrayList<Integer>();
		constMultType=new ArrayList<Integer>();
		designatorsName=new ArrayList<String>();
		actualParamsStructs=new ArrayList<Struct>();
	}
	
	public void visit(ProgramName progName) {
		//pravi se novi objektni cvor u tabeli simbola za progName i otvara se novi opseg
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		Tab.openScope();
	} 
	
	public void visit(Program program) {
		//kada se redukuje definicija za program treba da dohvatimo broj promenljivih
		numOfGlobalVars = Tab.currentScope.getnVars();
		//uvezu se lokalni simboli u objektni cvor koji predstavlja program i zatvori se trenutni opseg
    	Tab.chainLocalSymbols(program.getProgramName().obj);
    	Tab.closeScope();
    }
	
//----------ovo dole je za DEKLARACIJU KONSTANTI----------
    
    public void visit(ConstantNum constantNum) {
    	constantNum.struct = constantNum.getConstNum().struct;
    }
    
    public void visit(ConstantBool constantBool) {
    	constantBool.struct = constantBool.getConstBool().struct;
    }
    
    public void visit(ConstantChar constantChar) {
    	constantChar.struct = constantChar.getConstChar().struct;
    }
	
    //ConstNum ::= (ConstNum) NUMCONST:numconst
	public void visit(ConstNum constNum) {
		//ConstNum je tipa Struct i ako je NUMCONST onda je intType strukturni cvor
		constNum.struct = Tab.intType;
		//dohvatamo vrednost koju smo redukovali ovom smenom
		constCurrentValue = constNum.getNumconst();
    }
	
	public void visit(ConstChar constChar) {
		constChar.struct = Tab.charType;
		constCurrentValue = constChar.getCharconst();
	}
	
	public void visit(ConstBool constBool) {
		constBool.struct = boolType;
//		if(constBool.getBoolconst().equals("true")) {
//			constCurrentValue=1;
//		}
//		else {
//			constCurrentValue=0;
//		}
		if(constBool.getBoolconst())
			constCurrentValue = 1;
		else
			constCurrentValue = 0;
	}
	
	/*ConstDeclIdentList ::= (MultipleConstDeclarations) ConstDeclIdentList COMMA IDENT:constantName EQUAL Constant:constant
							|
							(SingleConstDeclarations) IDENT:constantName EQUAL Constant:constant
								; */
    public void visit(SingleConstDeclarations singleConstDeclarations) {
    	report_info("Deklarisana konstanta "+singleConstDeclarations.getConstantName(), singleConstDeclarations);
    	constSingleName = singleConstDeclarations.getConstantName();
    	constSingleType=singleConstDeclarations.getConstant().struct.getKind();
    	//da se dohvati ta vrednost konstante kad se redukuje smena npr ConstNum ::= (ConstNum) NUMCONST:numconst i tad ce da se stavi u constCurrentValue
    	constSingleValue = constCurrentValue;
    }
    
    public void visit(MultipleConstDeclarations multipleConstDeclarations) {
    	report_info("Deklarisana konstanta "+multipleConstDeclarations.getConstantName(), multipleConstDeclarations);
    	constMultName.add(multipleConstDeclarations.getConstantName());
    	constMultType.add(multipleConstDeclarations.getConstant().struct.getKind());
    	constMultValue.add(constCurrentValue);
    }
    
    //ConstDecl ::= (ConstDecl) CONST Type:constantType ConstDeclIdentList SEMI;
    public void visit(ConstDecl constDecl) {
    	//da se dohvati strukturni cvor koji predstavlja neterminal Type 
    	Struct constTypeStruct = constDecl.getType().struct;
    	Obj constDeclObj=null;
    	
    	//za SingleConstDeclarations deo smene
    	if(constTypeStruct.getKind()!=constSingleType) {
    		report_error("GRESKA NA LINIJI " + constDecl.getLine() + " : " + "konstanta " + constSingleName + " je inicijalizovana pogresnim tipom vrednosti! ", null);
    	}
    	else {
			if(Tab.currentScope.findSymbol(constSingleName)!=null) {
				report_error("GRESKA NA LINIJI " + constDecl.getLine() + " : " + "Promenljiva "+constSingleName+" je vec deklarisana!",null);
			}
			else {
				//pravim novi objektni cvor sa dohvacenim imenom i u setAdr stavljam vrednost te konstante
				constDeclObj = Tab.insert(Obj.Con, constSingleName, constTypeStruct);
				constDeclObj.setAdr(constSingleValue);
				report_info("Promenljiva koja oznacava konstantu "+constSingleName+" ubacena u tabelu simbola!", null);
			}
    	}
    	
    	//za MultipleConstDeclarations deo smene
    	for(int i=0; i<constMultName.size(); i++) {
    		if(constTypeStruct.getKind()!=constMultType.get(i)) {
    			report_error("GRESKA NA LINIJI " + constDecl.getLine() + " : " + "konstanta " + constMultName.get(i) + " je inicijalizovana pogresnim tipom vrednosti! ", null);        	}
        	else {
    			if(Tab.currentScope.findSymbol(constMultName.get(i))!=null) {
    				report_error("GRESKA NA LINIJI " + constDecl.getLine() + " : " + "Promenljiva "+constMultName.get(i)+" je vec deklarisana!",null);
    			}
    			else {
    				constDeclObj = Tab.insert(Obj.Con, constMultName.get(i), constTypeStruct);
    				constDeclObj.setAdr(constMultValue.get(i));
    				report_info("Promenljiva koja oznacava konstantu "+constMultName.get(i)+" ubacena u tabelu simbola!", null);
    			}
        	}
    	}
    	constSingleName="";
    	constSingleType=0;
    	constSingleValue=0;
    	constMultName.clear();
    	constMultType.clear();
    	constMultValue.clear();
    }
    
    /*VarDeclIdentList ::= 	(MultipleVarDeclarations) VarDeclIdentList COMMA IDENT:varMultName ArraySquares:arrSquareMult
			|
			(SingleVarDeclarations) IDENT:varSingleName ArraySquares:arrSquareSingle
			;*/
    public void visit(SingleVarDeclarations vardecl){
		varDeclCount++;
		report_info("Deklarisana promenljiva "+vardecl.getVarSingleName(), vardecl);
		varSingleName=vardecl.getVarSingleName();
//		if(!(vardecl.getArraySquares() instanceof NoArraySquares)) {
		if(vardecl.getArraySquares().getClass()==ArraySquares.class) {
			arrSquareSingle=true;
		}
		else if(vardecl.getArraySquares().getClass()==ArrayMatrix.class) {
			arrSquareMatrixSingle=true;
		}
	}
    
	public void visit(MultipleVarDeclarations vardecl){
		varDeclCount++;
		report_info("Deklarisana promenljiva "+vardecl.getVarMultName(), vardecl);
		varMultName.add(vardecl.getVarMultName());
		//ako postoje [] zagrade to znaci da je deklarisan niz ovom promenljivom
		if(vardecl.getArraySquares().getClass()==ArraySquares.class) {
			arrSquareMult.add(true);
			arrSquareMatrixMult.add(false);
		}
		else if(vardecl.getArraySquares().getClass()==ArrayMatrix.class) {
			arrSquareMatrixMult.add(true);
			arrSquareMult.add(false);
		}
		else {
			arrSquareMult.add(false);
			arrSquareMatrixMult.add(false);
		}
//		Obj varNode = Tab.insert(Obj.Var, vardecl.getVarMultName(), varStruct);
	}
	
	//VarDecl ::= (VarDeclaration) Type:varType VarDeclIdentList SEMI
	public void visit(VarDeclaration vardecl){
		varStruct=vardecl.getType().struct;
		//prvo za SingleVarDeclarations
		if(arrSquareSingle) {
			if(Tab.currentScope.findSymbol(varSingleName)!=null) {
				report_error("GRESKA: Promenljiva "+varSingleName+" je vec deklarisana!",null);
			}
			else {
				//pravim novi objektni cvor kome je type pokazuje na novi strukturni cvor koji predstavlja niz, ciji je elemType tip promenljive
				Tab.insert(Obj.Var, varSingleName, new Struct(Struct.Array, vardecl.getType().struct));
				report_info("Promenljiva koja oznacava niz "+varSingleName+" ubacena u tabelu simbola!", null);
			}
		}
		else if(arrSquareMatrixSingle) {
			if(Tab.currentScope.findSymbol(varSingleName)!=null) {
				report_error("GRESKA: Promenljiva "+varSingleName+" je vec deklarisana!",null);
			}
			else {
				//pravim novi objektni cvor kome je type pokazuje na novi strukturni cvor koji predstavlja niz, ciji je elemType tip promenljive
				Obj addedObjSingle = Tab.insert(Obj.Var, varSingleName, new Struct(Struct.Array, vardecl.getType().struct));
				//postavljam FpPos za ovaj objektni cvor koji sam napravio jer ce to da mi oznacava da ovaj niz zapravo predstavlja matricu
				addedObjSingle.setFpPos(1);
				report_info("Promenljiva koja oznacava matricu "+varSingleName+" ubacena u tabelu simbola!", null);
			}
		}
		else {
			if(Tab.currentScope.findSymbol(varSingleName)!=null) {
				report_error("GRESKA: Promenljiva "+varSingleName+" je vec deklarisana!",null);
			}
			else {
				//pravim novi objektni cvor kome je type tip promenljive
				Tab.insert(Obj.Var, varSingleName, varStruct);
				report_info("Promenljiva "+varSingleName+" ubacena u tabelu simbola!", null);
			}
		}
		
		//isto kao za single samo iteriram po nizu koji je napravljen u MultipleVarDeclarations
		for(int i=0; i<varMultName.size(); i++) {
			if(arrSquareMult.get(i)) {
				if(Tab.currentScope.findSymbol(varMultName.get(i))!=null) {
					report_error("GRESKA: Promenljiva "+varMultName.get(i)+" je vec deklarisana!",null);
				}
				else {
					Tab.insert(Obj.Var, varMultName.get(i), new Struct(Struct.Array, vardecl.getType().struct));
					report_info("Promenljiva koja oznacava niz "+varMultName.get(i)+" ubacena u tabelu simbola!", null);
				}
			}
			else if(arrSquareMatrixMult.get(i)) {
				if(Tab.currentScope.findSymbol(varMultName.get(i))!=null) {
					report_error("GRESKA: Promenljiva "+varMultName.get(i)+" je vec deklarisana!",null);
				}
				else {
					Obj addedObjMult = Tab.insert(Obj.Var, varMultName.get(i), new Struct(Struct.Array, vardecl.getType().struct));
			
					//postavljam FpPos za ovaj objektni cvor koji sam napravio jer ce to da mi oznacava da ovaj niz zapravo predstavlja matricu
					addedObjMult.setFpPos(1);
					report_info("Promenljiva koja oznacava matricu "+varMultName.get(i)+" ubacena u tabelu simbola!", null);
				}
			}
			else {
				if(Tab.currentScope.findSymbol(varMultName.get(i))!=null) {
					report_error("GRESKA: Promenljiva "+varMultName.get(i)+" je vec deklarisana!",null);
				}
				else {
					Tab.insert(Obj.Var, varMultName.get(i), varStruct);
					report_info("Promenljiva "+varMultName.get(i)+" ubacena u tabelu simbola!", null);
				}
			}
		}
		varSingleName="";
		varMultName.clear();
		arrSquareSingle=false;
		arrSquareMult.clear();
		arrSquareMatrixSingle=false;
		arrSquareMatrixMult.clear();
	}
	
	//Type ::= (Type) IDENT:typeName;
	public void visit(Type type) {
		// u tabeli simbola trazim objektni cvor koji predstavlja tip koji je prosledjen sa IDENT
    	Obj typeNode = Tab.find(type.getTypeName());
    	//ako nije pronadjen tip u tabeli simbola greska
    	if(typeNode == Tab.noObj) {
    		report_error("Nije pronadjen tip "+ type.getTypeName() + " u tabeli simbola! ", null);
    		type.struct = Tab.noType;
    	}else {
    		//provera da li objektni cvor koji je vracen predstavlja tip
    		if(Obj.Type == typeNode.getKind()) {
    			type.struct=typeNode.getType();
    		}else {
    			report_error("Greska: Ime "+ type.getTypeName()+" ne predstavlja tip!", type);
    			type.struct = Tab.noType;
    		}
    	}
    }
	
	//MethodTypeName ::= (MethodTypeName) Type:retType IDENT:methName;
	public void visit(MethodTypeName methodTypeName) {
		//pravim novi objektni cvor za metod i postavljam da je to currentMethod sa Type i IDENT
		currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethName(), methodTypeName.getType().struct);
		methodTypeName.obj = currentMethod;
		//otvaram novi opseg
		Tab.openScope();
		report_info("Obradjuje se funkcija "+ methodTypeName.getMethName(), methodTypeName);
	}
	
	//MethodVoidName ::= (MethodVoidName) VOID:retVoid IDENT:methName;
	public void visit(MethodVoidName methodVoidName) {
		currentMethod = Tab.insert(Obj.Meth, methodVoidName.getMethName(), Tab.noType);
		methodVoidName.obj = currentMethod;
		Tab.openScope();
		report_info("Obradjuje se funkcija "+ methodVoidName.getMethName(), methodVoidName);
	}
	
	//MethodDecl ::= (MethodDeclType) MethodTypeName LPAREN FormalParams RPAREN VarDeclList LBRACE StatementList RBRACE
	public void visit(MethodDeclType methodDeclType) {
		//ako nije pronadjen return, a metoda nije void onda je greska
		if(!returnFound && currentMethod.getType() != Tab.noType){
			report_error("Semanticka greska na liniji " + methodDeclType.getLine() + ": funkcija " + currentMethod.getName() + " nema return iskaz!", null);
    	}
		//postavljam broj formalnih argumenata za objektni cvor koji predstavlja tu metodu
		currentMethod.setLevel(formalParamsNumber);
		//postavljam lokalne promenljive trenutne metode tako da locals polje od currentMethod ukazuje na njih
		Tab.chainLocalSymbols(currentMethod);
    	Tab.closeScope();
    	
    	returnFound = false;
    	currentMethod = null;
    	formalParamsNumber=0;
	}
	
	//MethodDecl ::= (MethodDeclVoid) MethodVoidName LPAREN FormalParams RPAREN VarDeclList LBRACE StatementList RBRACE
	public void visit(MethodDeclVoid methodDeclVoid) {
		currentMethod.setLevel(formalParamsNumber);
		Tab.chainLocalSymbols(currentMethod);
    	Tab.closeScope();
    	
    	currentMethod = null;
    	formalParamsNumber=0;
	}
	
	//FormalParamDecl ::= (FormalParamDecl) Type:type IDENT:ident ArraySquares;
	public void visit(FormalParamDecl formalParamDecl) {
	    if(formalParamDecl.getArraySquares().getClass()==ArraySquares.class) {
	    	//pravim novi objektni cvor za formalni parametar koji predstavlja niz sa imenom IDENT i tipom Type
	    	Tab.insert(Obj.Var, formalParamDecl.getIdent(), new Struct(Struct.Array, formalParamDecl.getType().struct));
			report_info("Formalni parametar koji oznacava niz "+formalParamDecl.getIdent()+" ubacen u tabelu simbola!", null);
	    }
	    else if(formalParamDecl.getArraySquares().getClass()==ArrayMatrix.class) {
	    	//pravim novi objektni cvor za formalni parametar koji predstavlja matricu preko niza sa imenom IDENT i tipom Type
	    	Obj formalParamMatrix = Tab.insert(Obj.Var, formalParamDecl.getIdent(), new Struct(Struct.Array, formalParamDecl.getType().struct));
			//postavljam da je FpPos=1 
	    	formalParamMatrix.setFpPos(1);
	    	report_info("Formalni parametar koji oznacava matricu "+formalParamDecl.getIdent()+" ubacen u tabelu simbola!", null);
	    }
	    else {
	    	//pravim novi objektni cvor za formalni parametar sa imenom IDENT i tipom Type
	    	Tab.insert(Obj.Var, formalParamDecl.getIdent(), formalParamDecl.getType().struct);
	    	report_info("Formalni parametar "+formalParamDecl.getIdent()+" ubacen u tabelu simbola!", null);
	    }
	    formalParamsNumber++;
	 }
	
//	NADJI 
	// Designator = ident {"." ident | "[" Expr "]"}			
//	Designator ::= 	(DesigMoreIdents) Designator:desig Idents:idents
//					|
//					(DesigSingleIdent) IDENT:name		
	public void visit(DesigSingleIdent desigSingleIdent) {
		Obj obj;
		//ovaj IDENT iz DesigSingleIdent ako mu je parent tipa DesigMoreIdents to znaci da predstavlja designator koji je niz i onda smestam u desigSingleIdentObj da bih mogao da koristim taj objektni cvor u DesigMoreIdents
		if(desigSingleIdent.getParent().getClass()==DesigMoreIdents.class) {
			desigSingleIdentObj = Tab.find(desigSingleIdent.getName());
		}
		
		obj = Tab.find(desigSingleIdent.getName());
    	if(obj == Tab.noObj){
			report_error("Greska na liniji " + desigSingleIdent.getLine()+ " : ime "+desigSingleIdent.getName()+" nije deklarisano! ", null);
    	}
    	else {
    		report_info("Koristi se simbol "+ desigSingleIdent.getName()+" na liniji " + desigSingleIdent.getLine(), null);
    	}
    	desigSingleIdent.obj = obj;
	}
	
	public void visit(DesigMoreIdents desigMoreIdents) {
		//dohvatam taj objektni cvor koji predstavlja niz
		Obj desigMoreIdentsObj = Tab.find(desigSingleIdentObj.getName());
		if(desigMoreIdentsObj == Tab.noObj) {
			report_error("GRESKA NA LINIJI " + desigMoreIdents.getLine()+ " : ime "+desigSingleIdentObj.getName()+" nije deklarisano! ", null);
    	}
//		else if(desigMoreIdentsObj.getKind()==Obj.Con) {
//			report_error("OVO ZAPRAVO STAVLJA DA JE KONSTANTA", null);
//		}
		else if(desigMoreIdentsObj.getType().getKind()!=Struct.Array) {
			report_error("GRESKA NA LINIJI " + desigMoreIdents.getLine()+ " : "+desigMoreIdents.getDesignator().obj.getName()+" mora biti tipa Array! ", null);
		}
    	else {
    		report_info("Koristi se simbol "+ desigSingleIdentObj.getName()+" na liniji " + desigMoreIdents.getLine(), null);
    	}
//		System.out.println(desigMoreIdentsObj);
//		desigMoreIdents.obj = desigMoreIdentsObj;
		desigMoreIdents.obj = desigMoreIdents.getDesignator().obj;
	}
	
	// Expressions = Expr {"," Expr}
//		Expressions ::= (MultExpressions) Expressions COMMA Expr:expr
//						|
//						(SingleExpr) Expr:e
	public void visit(SingleExpr singleExpr) {
			//ovo SingleExpr je u Expressions, a Expressions se koristi samo u ActPars
		singleExpr.struct = singleExpr.getExpr().struct;
			//jer cu u DesigStmtFuncCall da proveravam pomocu ove liste
		actualParamsStructs.add(singleExpr.getExpr().struct);
	}
		
	public void visit(MultExpressions multExpressions) {
		multExpressions.struct=multExpressions.getExpr().struct;
		actualParamsStructs.add(multExpressions.getExpr().struct);
	}
	
	// DesignatorStatement = Designator "(" [ActPars] ")"
//	DesignatorStatement ::= (DesigStmtFuncCall) Designator:desig LPAREN ActualParams RPAREN
	public void visit(DesigStmtFuncCall funcCall) {
    	Obj func = funcCall.getDesignator().obj;
    	//Designator mora oznacavati nestaticku metodu unutrasnje klase ili globalnu funkciju glavnog programa.
    	if(funcCall.getDesignator().obj.getKind()!=Obj.Meth) {
    		report_error("GRESKA NA LINIJI " + funcCall.getLine()+ " : " + func.getName() + " mora biti globalna funkcija glavnog programa!", null);
    	}
    	if(Obj.Meth == func.getKind()){
			report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + funcCall.getLine(), null);
			funcCall.struct = func.getType();
    	}else{
			report_error("GRESKA NA LINIJI " + funcCall.getLine()+" : ime " + func.getName() + " nije funkcija!", null);
			funcCall.struct = Tab.noType;
    	}
    	
    	//chr(e) e mora biti izraz tipa int - provera za ovo
    	if(func.getName().equalsIgnoreCase("chr")) {
    		if(funcCall.getActualParams() instanceof NoActualParams) {
    			report_error("GRESKA NA LINIJI " + funcCall.getLine()+" : funkcija chr mora imati parametar!", null);
    		}
    		else if(funcCall.getActualParams().struct.getKind()!=Struct.Int) {
    			report_error("GRESKA NA LINIJI " + funcCall.getLine()+" : parametar funkcije chr mora biti tipa Int!", null);
    		}
    	}
    	
    	//ord(c) c mora biti tipa char - provera za ovo
    	if(func.getName().equalsIgnoreCase("ord")) {
    		if(funcCall.getActualParams() instanceof NoActualParams) {
    			report_error("GRESKA NA LINIJI " + funcCall.getLine()+" : funkcija ord mora imati parametar!", null);
    		}
    		else if(funcCall.getActualParams().struct.getKind()!=Struct.Char) {
    			report_error("GRESKA NA LINIJI " + funcCall.getLine()+" : parametar funkcije ord mora biti tipa Char!", null);
    		}
    	}
    	
    	//len(a) a mora biti niz ili znakovni niz - provera za ovo
    	if(func.getName().equalsIgnoreCase("len")) {
    		if(funcCall.getActualParams() instanceof NoActualParams) {
    			report_error("GRESKA NA LINIJI " + funcCall.getLine()+" : funkcija len mora imati parametar!", null);
    		}
    		else if(funcCall.getActualParams().struct.getKind()!=Struct.Array) {
    			report_error("GRESKA NA LINIJI " + funcCall.getLine()+" : parametar funkcije len mora biti niz!", null);
    		}
    	}
    	//dohvatam objektni cvor koji predstavlja metodu koja se sada poziva
    	currentCalledFunc=Tab.find(funcCall.getDesignator().obj.getName());
    	//dohvatam lokalne promenljive te metode
		Collection<Obj> locals=currentCalledFunc.getLocalSymbols();
		//dohvatam broj formalnih parametara
		int numOfFormalParams = currentCalledFunc.getLevel();
		if(numOfFormalParams!=actualParamsStructs.size()) {
			report_error("GRESKA NA LINIJI " + funcCall.getLine()+ " : " + func.getName() + " mora imati isti broj stvarnih i formalnih parametara!", null);
		}
		else {
			for(int i=0; i<actualParamsStructs.size(); i++) {
				if(!actualParamsStructs.get(i).assignableTo(locals.iterator().next().getType())) {
					report_error("GRESKA NA LINIJI " + funcCall.getLine()+ " : " + "Tip " + (i+1) + ". formalnog i stvarnog parametra nisu kompatibilni"+"!", null);
				}
			}
		}
		actualParamsStructs.clear();
		currentCalledFunc=null;
    }
	
//	ActPars ::= (ActPars) Expressions;
	public void visit(ActPars actPars) {
		actPars.struct = actPars.getExpressions().struct;
	}
	
	// ActualParams = [ActPars]						
//	ActualParams ::= 	(ActualParameters) ActPars
//						|
//						(NoActualParams) /* epsilon */
//						;
	public void visit(ActualParameters actualParameters) {
		actualParameters.struct = actualParameters.getActPars().struct;
	}
	
//	Expr ::= (Expr) Terms;
	public void visit(Expr expr) {
		expr.struct = expr.getTerms().struct;
		
	}
	
//	Term ::= 	(MultFactors) Term:te Mulop:mulOp Factor:t
//			|
//			(SingleFactor) Factor:t
//			;
	public void visit(SingleFactor singleFactor) {
		singleFactor.struct = singleFactor.getFactor().struct;
    }
	
	public void visit(MultFactors multFactors) {
		Struct te=multFactors.getTerm().struct;
		Struct t=multFactors.getFactor().struct;
		if(te!=Tab.intType || t!=Tab.intType) {
			report_error("GRESKA NA LINIJI "+ multFactors.getLine()+" : nekompatibilni tipovi u izrazu za mnozenje ili deljenje. Tipovi moraju biti tipa int!", null);
			multFactors.struct = Tab.noType;
		}
		else {
			multFactors.struct=te;
		}
	}
	
//	Terms ::= 	(AddExpr) Terms:te Addop:addOp Term:t
//			|
//			(SingleTerm) ExprMinus:minus Term:t;
	public void visit(SingleTerm singleTerm) {
		singleTerm.struct = singleTerm.getTerm().struct;
		if(!(singleTerm.getExprMinus() instanceof NoExprMinus) && singleTerm.getTerm().struct.getKind()!=Struct.Int) {
			report_error("GRESKA: Promenljiva mora biti tipa int ako postoji minus!",null);
		}
	}
	
	public void visit(AddExpr addExpr) {
		Struct te = addExpr.getTerms().struct;
		Struct t = addExpr.getTerm().struct;
		if(te.equals(t) && te == Tab.intType) {
			addExpr.struct = te;
			report_info("Kompatibilni su tipovi za sabiranje ili oduzimanje na liniji: "+addExpr.getLine(), null);
		}
		else {
			report_error("GRESKA NA LINIJI "+ addExpr.getLine()+" : nekompatibilni tipovi u izrazu za sabiranje ili oduzimanje. Tipovi moraju biti tipa int!", null);
			addExpr.struct = Tab.noType;
		}
	}
	
	//Factor ::= 	(FactNumConst) NUMCONST:numconst
	public void visit(FactNumConst factNumConst) {
		factNumConst.struct = Tab.intType;
	}
	
	public void visit(FactCharConst factCharConst) {
		factCharConst.struct = Tab.charType;
	}
	
	public void visit(FactBoolConst factBoolConst) {
		factBoolConst.struct = boolType;
	}
	
	//Factor ::= (FactExpr) LPAREN Expr RPAREN
	public void visit(FactExpr factExpr) {
		factExpr.struct = factExpr.getExpr().struct;
	}
	
	//Factor ::= (FactVar) Designator:d
	public void visit(FactVar factVar) {
		
		if(factVar.getDesignator() instanceof DesigMoreIdents) {
			//ako je designator element niza da se dohvati tip tog niza
			factVar.struct = desigSingleIdentObj.getType().getElemType();
		}
		else {
			factVar.struct = factVar.getDesignator().obj.getType();
		}
	}
	
	//Matched ::= (StmtReturnExpr) RETURN Expr:t SEMI
	public void visit(StmtReturnExpr stmtReturnExpr) {
		returnFound = true;
		Struct currMethType = currentMethod.getType();
		if(!currMethType.compatibleWith(stmtReturnExpr.getExpr().struct)) {
			report_error("GRESKA: tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
		}
	}
	
	//Matched ::= (StmtReturn) RETURN SEMI
	public void visit(StmtReturn stmtReturn) {
		//Ako neterminal Expr nedostaje, tekuca metoda mora biti deklarisana kao void.
		if(currentMethod.getType() != Tab.noType) {
			report_error("GRESKA: Metoda " + currentMethod.getName()+" mora biti deklarisana kao void!", null);
		}
	}
	
	// DesignatorStatement = Designator Assignop Expr
	//Matched ::= (DesigStmtAssignop) Designator:dest Assignop Expr:e SEMI
	public void visit(DesigStmtAssignop desigStmtAssignop){
		//Designator mora oznacavati promenljivu, element niza ili polje unutar objekta.
		if(desigStmtAssignop.getDesignator().obj.getKind()!=Obj.Var && desigStmtAssignop.getDesignator().obj.getType().getElemType()==null 
				&& (desigStmtAssignop.getDesignator().obj.getKind()==Obj.Con && desigStmtAssignop.getDesignator().getParent().getClass()!=FactVar.class)) {
			report_error("GRESKA NA LINIJI " + desigStmtAssignop.getLine() + " : " + desigStmtAssignop.getDesignator().obj.getName() + "moraa oznacavati promenljivu, element niza ili polje unutar objekta!", null);
		}
		else {
	    	if(desigStmtAssignop.getDesignator().obj.getType().getKind()==Struct.Array) {
//	    		report_info("EXPR JE TIPA: " + desigStmtAssignop.getExpr().struct, null);
//	    		report_info("DESIGNATOR JE TIPA: " + desigStmtAssignop.getDesignator().obj.getType(), null);
	    		//ovo je za proveru tipa niz[i]=promenljiva_a da li su kompatibilnog tipa
	    		if(!desigStmtAssignop.getExpr().struct.assignableTo(desigStmtAssignop.getDesignator().obj.getType().getElemType()))
		        	report_error("GRESKA NA LINIJI " + desigStmtAssignop.getLine() + " : " + "nekompatibilni tipovi u dodeli vrednosti! ", null);
	    	}
	    	else {
	    		if(!desigStmtAssignop.getExpr().struct.assignableTo(desigStmtAssignop.getDesignator().obj.getType()))
	        		report_error("GRESKA NA LINIJI " + desigStmtAssignop.getLine() + " : " + "nekompatibilni tipovi u dodeli vrednosti!!! ", null);
	    	}
		}
    }
	
	// DesignatorStatement ::= (DesigStmtInc) Designator:desig INC
	public void visit(DesigStmtInc desigStmtInc) {
		//Designator mora oznacavati promenljivu, element niza ili polje unutar objekta.
		if(!(desigStmtInc.getDesignator().obj.getKind()==Obj.Var || desigStmtInc.getDesignator().obj.getType().getElemType()!=null)) {
			report_error("GRESKA NA LINIJI " + desigStmtInc.getLine() + " : " + desigStmtInc.getDesignator().obj.getName() + "mora oznacavati promenljivu, element niza ili polje unutar objekta!", null);
		}
		if(desigStmtInc.getDesignator().obj.getType().getKind()==Struct.Array) {
			if(desigStmtInc.getDesignator().obj.getType().getElemType().getKind()!=Struct.Int)
        		report_error("GRESKA NA LINIJI " + desigStmtInc.getLine() + " : " + "Element mora biti tipa int! ", null);
    	}
    	else {
    		if(desigStmtInc.getDesignator().obj.getType().getKind()!=Struct.Int)
        		report_error("GRESKA NA LINIJI " + desigStmtInc.getLine() + " : " + "Promenljiva mora biti tipa int! ", null);
    	}
	}
	
	// DesignatorStatement ::= (DesigStmtInc) Designator:desig DEC
	public void visit(DesigStmtDec desigStmtDec) {
		//Designator mora oznacavati promenljivu, element niza ili polje unutar objekta.
		if(!(desigStmtDec.getDesignator().obj.getKind()==Obj.Var || desigStmtDec.getDesignator().obj.getType().getElemType()!=null)) {
			report_error("GRESKA NA LINIJI " + desigStmtDec.getLine() + " : " + desigStmtDec.getDesignator().obj.getName() + "mora oznacavati promenljivu, element niza ili polje unutar objekta!", null);
		}
		if(desigStmtDec.getDesignator().obj.getType().getKind()==Struct.Array) {
			if(desigStmtDec.getDesignator().obj.getType().getElemType().getKind()!=Struct.Int)
        		report_error("GRESKA NA LINIJI " + desigStmtDec.getLine() + " : " + "Element mora biti tipa int! ", null);
    	}
    	else {
    		if(desigStmtDec.getDesignator().obj.getType().getKind()!=Struct.Int)
        		report_error("GRESKA NA LINIJI " + desigStmtDec.getLine() + " : " + "Promenljiva mora biti tipa int! ", null);
    	}
	}
	
//	DesigExist ::= 	(DesigExis) Designator:designator
//			|
//			(NoDesigExist) /* epsilon */
//			;
	public void visit(DesigExis desigExis) {
		//Svi Designator neterminali sa leve strane znaka za dodelu vrednosti moraju oznacavati promenljivu,element niza ili polje unutar objekta.
		if(!(desigExis.getDesignator().obj.getKind()==Obj.Var  || desigExis.getDesignator().obj.getType().getElemType()!=null)) {
			report_error("GRESKA NA LINIJI " + desigExis.getLine() + " : " + desigExis.getDesignator().obj.getName() + " moraaa oznacavati promenljivu, element niza ili polje unutar objekta!", null);
		}
		else {
//			if((desigExis.getDesignator().obj.getType().getKind()==Struct.Array && desigExis.getDesignator().getParent().getClass()!=FactVar.class) || desigExis.getDesignator().obj.getType().getKind()!=Struct.Array) {
			//Designator sa desne strane znaka za dodelu vrednosti mora biti kompatibilan pri dodeli sa tipom svih neterminala Designator sa leve strane
			//ubacujem u listu sve promenljive da bih posle mogao da proverim designator sa desne strane da li je kompatibilan sa svim designatorima sa leve strane
			String name=desigExis.getDesignator().obj.getName();
			designatorsName.add(name);
//			}
		}
	}
	
	//DesignatorStatement ::= (DesigStmtList) LSQUARE DesigExistList RSQUARE EQUAL Designator:designator
	public void visit(DesigStmtList desigStmtList) {
		//Designator sa desne strane znaka za dodelu vrednosti mora predstavljati niz.
		if(desigStmtList.getDesignator().obj.getType().getKind()!=Struct.Array) {
			report_error("GRESKA NA LINIJI " + desigStmtList.getLine() + " : promenljiva " + desigStmtList.getDesignator().obj.getName() + " mora predstavljati niz.!", null);
		}
		Obj desig=null;
		for(int i=0; i<designatorsName.size(); i++) {
			desig=Tab.find(designatorsName.get(i));
			if(desig.getType().getKind()==Struct.Array) {
				if(!desig.getType().getElemType().assignableTo(desigStmtList.getDesignator().obj.getType().getElemType())) {
					report_error("GRESKA NA LINIJI " + desigStmtList.getLine() + " : Designator sa desne strane znaka za dodelu vrednosti mora biti kompatibilan pri dodeli sa tipom svih neterminala Designator sa leve strane!" , null);
				}
			}
			else if(!desig.getType().assignableTo(desigStmtList.getDesignator().obj.getType().getElemType())) {
				report_error("GRESKA NA LINIJI " + desigStmtList.getLine() + " : Designatorr sa desne strane znaka za dodelu vrednosti mora biti kompatibilan pri dodeli sa tipom svih neterminala Designator sa leve strane!" , null);
			}
		}
		designatorsName.clear();
	}
	
	//Matched ::= (StmtRead) READ LPAREN Designator:designator RPAREN SEMI
	public void visit(StmtRead stmtRead) {
		//Designator mora oznacavati promenljivu, element niza ili polje unutar objekta.
		if(!(stmtRead.getDesignator().obj.getKind()==Obj.Var || stmtRead.getDesignator().obj.getType().getElemType()!=null)) {
			report_error("GRESKA NA LINIJI " + stmtRead.getLine() + " : " + stmtRead.getDesignator().obj.getName() + " mora oznacavati promenljivu, element niza ili polje unutar objekta!", null);
		}
		//Designator mora biti tipa int, char ili bool.
		else {
			if(stmtRead.getDesignator().obj.getType().getElemType()!=null) {
				if(stmtRead.getDesignator().obj.getType().getElemType()!=Tab.intType && stmtRead.getDesignator().obj.getType().getElemType()!=Tab.charType && stmtRead.getDesignator().obj.getType().getElemType()!=boolType)
		    		report_error("GRESKA NA LINIJI " + stmtRead.getLine() + " : " + "Promenljiva mora biti tipa int, char ili bool! ", null);
			}
			else {
				if(stmtRead.getDesignator().obj.getType().getKind()!=Struct.Int && stmtRead.getDesignator().obj.getType().getKind()!=Struct.Char && stmtRead.getDesignator().obj.getType().getKind()!=Struct.Bool) {
					report_error("GRESKA NA LINIJI " + stmtRead.getLine() + " : " + "Promenljiva koja nije niz mora biti tipa int, char ili bool! ", null);
				}
			}
		}
			
	}
	
	public void visit(StmtPrint stmtPrint) {
		if(stmtPrint.getExpr().struct.getKind()!=Struct.Int && stmtPrint.getExpr().struct.getKind()!=Struct.Char && stmtPrint.getExpr().struct.getKind()!=Struct.Bool)
    		report_error("GRESKA NA LINIJI " + stmtPrint.getParent().getLine() + " : " + "Print Expr mora biti tipa int, char ili bool! ", null);
	}
	
	public void visit(StmtPrintNum stmtPrintNum) {
		if(stmtPrintNum.getExpr().struct.getKind()!=Struct.Int && stmtPrintNum.getExpr().struct.getKind()!=Struct.Char && stmtPrintNum.getExpr().struct.getKind()!=Struct.Bool)
    		report_error("GRESKA NA LINIJI " + stmtPrintNum.getParent().getLine() + " : " + "Print Expr mora biti tipa int, char ili bool! ", null);
	}
    
	// CondFact ::= (CondFactExprRelop) Expr:first Relop:relop Expr:second
	public void visit(CondFactExprRelop condFactExprRelop) {
		//Tipovi oba izraza moraju biti kompatibilni.
		if(!condFactExprRelop.getExpr().struct.compatibleWith(condFactExprRelop.getExpr1().struct)) {
			report_error("GRESKA" + " : " + "Tipovi oba operatora moraju biti kompatibilni! ", null);
		}
		//Uz promenljive tipa klase ili niza, od relacionih operatora, mogu se koristiti samo != i ==.
		else if(condFactExprRelop.getExpr().struct.getKind()==Struct.Array) {
			if(!(condFactExprRelop.getRelop() instanceof RelopEqTo) && !(condFactExprRelop.getRelop() instanceof RelopNoEqTo)) {
				report_error("GRESKA" + " : " + "Uz promenljive tipa niza, od relacionih operatora, mogu se koristiti samo != i == ", null);
			}
		}
	}
	
	// Factor = Designator "(" [ActPars] ")"
   	//Factor ::= (FactFuncCall) Designator:func LPAREN ActualParams RPAREN
	public void visit(FactFuncCall factFuncCall) {
		Obj func = factFuncCall.getDesignator().obj;
		if(factFuncCall.getDesignator().obj.getKind()!=Obj.Meth) {
    		report_error("GRESKA NA LINIJI " + factFuncCall.getLine()+ " : " + factFuncCall.getDesignator().obj.getName() + " mora biti globalna funkcija glavnog programa!", null);
    		factFuncCall.struct=Tab.noType;
		}
		else {
			if(Tab.noType == func.getType()){
				report_error("Semanticka greska " + func.getName() + " ne moze se koristiti u izrazima jer nema povratnu vrednost ", factFuncCall);
			}else{
				report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + factFuncCall.getLine(), null);
				factFuncCall.struct = func.getType();
			}
		}
	}
	
	//Idents ::= (IdentArray) LSQUARE Expr:exp RSQUARE
	public void visit(IdentArray identArray) {
//		System.out.println(identArray.getExpr().struct);
		if(!identArray.getExpr().struct.equals(Tab.intType) && !identArray.getExpr().struct.equals(new Struct(Struct.Array))) {
			report_error("GRESKA NA LINIJI " + identArray.getParent().getLine()+ " : "+"indeks za niz mora biti tipa int!", null);
		}
	}
	
	// Factor ::= (FactNewArray) NEW Type LSQUARE Expr RSQUARE
	public void visit(FactNewArray factNewArray) {
		if(!factNewArray.getExpr().struct.equals(Tab.intType)) {
			report_error("GRESKA NA LINIJI " + factNewArray.getLine()+ " : "+"Velicina niza mora biti tipa int!", null);
		}
		else {
			factNewArray.struct=factNewArray.getType().struct;
		}
	}
	
	//Factor: = "new" Type "[" Expr "]" "[" Expr "]"
//   (FactNewMatrix) NEW Type LSQUARE Expr RSQUARE LSQUARE Expr RSQUARE
	public void visit(FactNewMatrix factNewMatrix) {
		if(!factNewMatrix.getExpr().struct.equals(Tab.intType) || !factNewMatrix.getExpr1().struct.equals(Tab.intType)) {
			report_error("GRESKA NA LINIJI " + factNewMatrix.getLine()+ " : "+"Velicina matrice mora biti tipa int!", null);
		}
		else {
			factNewMatrix.struct=factNewMatrix.getType().struct;
		}
	}
	
    public boolean passed(){
    	return !errorDetected;
    }
    
    
    
    

    
     


}
