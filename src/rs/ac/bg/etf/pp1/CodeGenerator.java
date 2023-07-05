package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;

import jdk.nashorn.internal.runtime.FindProperty;
import rs.ac.bg.etf.pp1.CounterVisitor.*;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class CodeGenerator extends VisitorAdaptor {
	
	private List<Obj> objectNodes;
	private List<Integer> nodesName;
	private List<Obj> objectNodesForMatrixColumns;
	private boolean allocationMatrix=false;
	private boolean matrixRowFlag=false;
	private boolean matrixColumnFlag=false;
	private boolean backToSecondProcessInMatrix=false;
	private int matrixRowLevel=0;
	private int matrixColumnLevel=0;
	int nextProces=1;
	
	public void init() {
		objectNodes=new ArrayList<Obj>();
		nodesName=new ArrayList<Integer>();
		objectNodesForMatrixColumns=new ArrayList<Obj>();
	}

	private int mainPc;
	
	public int getMainPc(){
		return mainPc;
	}
	
	//Matched ::= (StmtPrint) PRINT LPAREN Expr:expr RPAREN SEMI
	//bprint i print operacije ocekuju val i width na ExprStack
	public void visit(StmtPrint stmtPrint) {
		if(stmtPrint.getExpr().struct == Tab.charType){
			Code.loadConst(5);
			Code.put(Code.bprint);
		}else if(stmtPrint.getExpr().struct == Tab.intType){
			Code.loadConst(5);
			Code.put(Code.print);
		}
		else {
			Code.loadConst(5);
			Code.put(Code.print);
		}
	}
	
	//Matched ::= (StmtPrintNum) PRINT LPAREN Expr:expr COMMA NUMCONST:numconst RPAREN SEMI
	//bprint i print operacije ocekuju val i width na ExprStack
	public void visit(StmtPrintNum stmtPrintNum) {
		//Code.put(Code.pop);
		if(stmtPrintNum.getExpr().struct == Tab.charType){
			Code.loadConst(5);
			Code.put(Code.bprint);
		}else if(stmtPrintNum.getExpr().struct == Tab.intType){
			Code.loadConst(5);
			Code.put(Code.print);
		}
		else {
			Code.loadConst(5);
			Code.put(Code.print);
		}
		
		//za zarez
		Code.loadConst(44);
		Code.loadConst(1);
		Code.put(Code.bprint);
		Obj con = new Obj(Obj.Con, "$", Tab.intType, stmtPrintNum.getNumconst(), 0);
		Code.load(con);
		Code.loadConst(1);
		Code.put(Code.print);
	}
	
	// Factor ::= 	(FactNumConst) NUMCONST:numconst
	public void visit(FactNumConst factNumConst) {
		//pravim novi objektni cvor sa vrednoscu ove konstante i ubacujem na ExprStack
		Obj con = new Obj(Obj.Con, "$", factNumConst.struct, factNumConst.getNumconst(), 0);
		Code.load(con);
	}
	
	public void visit(FactCharConst factCharConst) {
		Obj con = new Obj(Obj.Con, "$", factCharConst.struct, factCharConst.getCharconst(), 0);
//		con.setLevel(0);
//		con.setAdr(cnst.getN1());
		
		Code.load(con);
	}
	
	int valueOfBoolConst;
	boolean boolPrint;
	public void visit(FactBoolConst factBoolConst) {
		
//		if(factBoolConst.getParent().getClass() == StmtPrintNum.class || factBoolConst.getParent().getClass()==StmtPrint.class) {
//			boolPrint=true;
//		}
		if(factBoolConst.getBoolconst()) {
			valueOfBoolConst=1;
		}
		else {
			valueOfBoolConst=0;
		}
		
		Obj con = new Obj(Obj.Con, "$", new Struct(Struct.Bool), valueOfBoolConst, 0);
		
		Code.load(con);
	}
	
	//MethodTypeName ::= (MethodTypeName) Type:retType IDENT:methName;
	public void visit(MethodTypeName methodTypeName){
		
		if("main".equalsIgnoreCase(methodTypeName.getMethName())){
			mainPc = Code.pc;
		}
		//postavljam adresu gde se nalazi funkcija kao trenutnu vrednost pc-a
		methodTypeName.obj.setAdr(Code.pc);
		// Collect arguments and local variables
		SyntaxNode methodNode = methodTypeName.getParent();
		
		//dohvatam broj promenljivih
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		//dohvatam broj formalnih parametara
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
		// Generate the entry
		//enter b1, b2 - gde je b1 broj formalnih parametara, a b2=br formalnih parametara+br promenljivih
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	
	}
	
	public void visit(MethodVoidName methodVoidName){
		
		if("main".equalsIgnoreCase(methodVoidName.getMethName())){
			mainPc = Code.pc;
		}
		methodVoidName.obj.setAdr(Code.pc);
		// Collect arguments and local variables
		SyntaxNode methodNode = methodVoidName.getParent();
	
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
		// Generate the entry
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		System.out.println("Broj formalnih parametara je: "+fpCnt.getCount());
		System.out.println("Broj promenljivih je: "+varCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	}
	
	public void visit(MethodDeclType methodDecl){
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(MethodDeclVoid methodDecl){
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	// Matched ::= (DesigStmtAssignop) Designator:dest Assignop Expr:e SEMI
	public void visit(DesigStmtAssignop desigStmtAssignop) {
		//ovaj drugi uslov je da bi se razdvojio slucaj kada se radi niz/matrica=new int[]/[][];
		if(desigStmtAssignop.getDesignator().obj.getType().getKind()==Struct.Array && desigStmtAssignop.getDesignator().getClass()!=DesigSingleIdent.class) {
			if(desigStmtAssignop.getDesignator().obj.getType().getElemType()==Tab.charType) {
				//bastore i astore ocekuju adr, index i val na ExprStack
				Code.put(Code.bastore);
			}
			else {
				Code.put(Code.astore);
			}
		}
		else {
			Code.store(desigStmtAssignop.getDesignator().obj);
		}
		
	}
	
//	Designator ::= 	(DesigMoreIdents) Designator:desig Idents:idents
//			|
//			(DesigSingleIdent) IDENT:name
	public void visit(DesigSingleIdent desigSingleIdent) {
		SyntaxNode parent=desigSingleIdent.getParent();
		
		//DesigStmtFuncCall je ProcCall, FactFuncCall je FuncCall
		if(DesigStmtAssignop.class != parent.getClass() && DesigStmtFuncCall.class != parent.getClass() 
				&& FactFuncCall.class != parent.getClass() && StmtRead.class != parent.getClass() 
				&& DesigStmtList.class != parent.getClass() && DesigExis.class!=parent.getClass()){
			Code.load(desigSingleIdent.obj);
		}
		//ako je u pitanju matrica i nije u pitanju alokacija matrice, oznaci da ce sledeci Expr biti broj redova
		if(desigSingleIdent.obj.getFpPos()!=0 && !allocationMatrix) {
			if(nextProces==2) {
//				Code.loadConst(65);
//				Code.loadConst(5);
//				Code.put(Code.print);
				backToSecondProcessInMatrix=true;
			}
			matrixRowLevel++;
			matrixRowFlag=true;
			nextProces=1;
		}
		else {
			allocationMatrix=false;
		}
	}
	
//	Designator ::= 	(DesigMoreIdents) Designator:desig Idents:idents
	public void visit(DesigMoreIdents desigMoreIdents) {
		if(desigMoreIdents.obj.getFpPos()!=0 && matrixRowLevel>0 && nextProces==1) {
			//redukuje se smena matrica[indexReda]
			matrixRowLevel--;
			matrixRowFlag=false;
			//sad mi je na steku: addrOfMatrix, indexReda i hocu da ucitam broj kolona na stek i pomnozim sa indeksom reda
			Code.put(Code.dup_x1);			//..., indexReda, addrOfMAtrix, indexReda
			Code.put(Code.pop);				//..., indexReda, addrOfMAtrix
			Code.put(Code.dup);				//..., indexReda, addrOfMatrix, addrOfMatrix
			Code.put(Code.dup_x2);			//..., addrOfMatrix, indexReda, addrOfMAtrix, addrOfMAtrix
			Code.put(Code.pop);				//..., addrOfMatrix, indexReda, addrOfMAtrix
			Code.loadConst(0);				//..., addrOfMatrix, indexReda, addrOfMAtrix, 0
			Code.put(Code.aload);			//..., addrOfMatrix, indexReda, brojKolonaMatrice
			Code.put(Code.mul);				//..., addrOfMAtrix, indexReda*brojKolonaMatrice
			matrixColumnLevel++;
			matrixColumnFlag=true;
			nextProces=2;
		}
		else if(desigMoreIdents.obj.getFpPos()!=0 && matrixColumnLevel>0 && nextProces==2) {
			//redukuje se smena matrica[indexReda][indexKolone]
//			matrixColumnFlag=false;
			matrixColumnLevel--;
			//na steku bi trebalo da je //..., addrOfMAtrix, indexReda*brojKolonaMatrice, indexKolone
			if(desigMoreIdents.obj.getType()==Tab.charType) {
				//ako se radi o steku charova onda treba da saberem sa 4 da bi mi se preskocila i prva 4B u kome je broj kolona matrice jer cu ovde da radim sa baload i bastore
				Code.loadConst(4);
			}
			else {
				//ako se radi o steku intova onda treba da saberem sa 1 da bi mi se preskocila i prva 4B u kome je broj kolona matrice jer cu ovde da radim sa aload i astore
				Code.loadConst(1);
			}
			//na steku bi trebalo da je //..., addrOfMAtrix, indexReda*brojKolonaMatrice, indexKolone, 1/4
			Code.put(Code.add);
			Code.put(Code.add);		//..., addrOfMAtrix, indexOfElementMatrix
			if(matrixRowLevel>0 && !backToSecondProcessInMatrix) {
//				Code.loadConst(77);
//				Code.loadConst(5);
//				Code.put(Code.print);
				nextProces=1;
			}
			else if(backToSecondProcessInMatrix) {
//				Code.loadConst(88);
//				Code.loadConst(5);
//				Code.put(Code.print);
				backToSecondProcessInMatrix=false;
				nextProces=2;
				matrixColumnLevel++;
			}
			else if(matrixRowLevel==0) {
				nextProces=1;
			}
		}
	}
	
	//Factor ::= (FactFuncCall) Designator:func LPAREN ActualParams RPAREN
	public void visit(FactFuncCall funcCall){
		Obj functionObj = funcCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		//call offset - izgled instrukcije
		Code.put(Code.call);
		
		Code.put2(offset);
	}
	
	//DesignatorStatement ::= (DesigStmtFuncCall) Designator:desig LPAREN ActualParams RPAREN
	public void visit(DesigStmtFuncCall procCall){
		Obj functionObj = procCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		if(procCall.getDesignator().obj.getType() != Tab.noType){
			Code.put(Code.pop);
		}
	}
	
	public void visit(StmtReturnExpr returnWithExpr){
		Code.put(Code.exit);
		Code.put(Code.return_);	
	}
	
	public void visit(StmtReturn stmtReturn){
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	//Terms ::= (AddExpr) Terms:te Addop:addOp Term:t
	public void visit(AddExpr addExpr){
		if(addExpr.getAddop() instanceof AddopPlus) {
			Code.put(Code.add);
		}
		else if(addExpr.getAddop() instanceof AddopMinus) {
			Code.put(Code.sub);
		}
	}
	
	// Term ::= (MultFactors) Term:te Mulop:mulOp Factor:t
	public void visit(MultFactors multFactors) {
		if(multFactors.getMulop() instanceof MulopTimes) {
			Code.put(Code.mul);
		}
		else if(multFactors.getMulop() instanceof MulopDivide) {
			Code.put(Code.div);
		}
		else if(multFactors.getMulop() instanceof MulopMod) {
			Code.put(Code.rem);
		}
	}
	
	public void visit(DesigStmtInc desigStmtInc) {
		//na steku je: niz, index
		if(desigStmtInc.getDesignator().obj.getType().getKind()==Struct.Array) {
			//na steku je: niz, index
			Code.put(Code.dup);		//niz, index, index
			Code.put(Code.dup_x2);		//index, niz, index, index
			Code.put(Code.pop);		//index, niz, index
			if(desigStmtInc.getDesignator().obj.getType().getElemType()==Tab.charType) {
				Code.put(Code.baload);
			}
			else {
				Code.put(Code.aload);
			}
			//sad je na steku niz[index]
			Code.loadConst(1); 		//index, niz[index], 1
			Code.put(Code.add);		//index, niz[index]+1
			Code.load(desigStmtInc.getDesignator().obj);	//index, niz[index]+1, niz
			Code.put(Code.dup_x2);		//niz, index, niz[index+1], niz
			Code.put(Code.pop);			//niz, index, niz[index+1]
			if(desigStmtInc.getDesignator().obj.getType().getElemType()==Tab.charType) {
				//bastore i astore ocekuju adr, index i val na ExprStack
				Code.put(Code.bastore);
			}
			else {
				Code.put(Code.astore);
			}
		}
		else {
			Code.loadConst(1);
			Code.put(Code.add);
			//vracam vrednost sa ExprStack koja je izracunata u tu promenljivu
			Code.store(desigStmtInc.getDesignator().obj);
		}
		
	}
	
	public void visit(DesigStmtDec desigStmtDec) {
		//na steku je: niz, index
		if(desigStmtDec.getDesignator().obj.getType().getKind()==Struct.Array) {
			//na steku je: niz, index
			Code.put(Code.dup);		//niz, index, index
			Code.put(Code.dup_x2);		//index, niz, index, index
			Code.put(Code.pop);		//index, niz, index
			if(desigStmtDec.getDesignator().obj.getType().getElemType()==Tab.charType) {
				Code.put(Code.baload);
			}
			else {
				Code.put(Code.aload);
			}
			//sad je na steku niz[index]
			Code.loadConst(1); 		//index, niz[index], 1
			Code.put(Code.sub);		//index, niz[index]+1
			Code.load(desigStmtDec.getDesignator().obj);	//index, niz[index]+1, niz
			Code.put(Code.dup_x2);		//niz, index, niz[index+1], niz
			Code.put(Code.pop);			//niz, index, niz[index+1]
			if(desigStmtDec.getDesignator().obj.getType().getElemType()==Tab.charType) {
				//bastore i astore ocekuju adr, index i val na ExprStack
				Code.put(Code.bastore);
			}
			else {
				Code.put(Code.astore);
			}
		}
		else {
			Code.loadConst(1);
			Code.put(Code.sub);
			//vracam vrednost sa ExprStack koja je izracunata u tu promenljivu
			Code.store(desigStmtDec.getDesignator().obj);
		}
		
	}
	
//	public void visit(Expr expr) {
//		if(expr.getExprMinus() instanceof ExprMinus) {
//			Code.put(Code.neg);
//		}
//	}
	
//	Terms ::= 	(AddExpr) Terms:te Addop:addOp Term:t
//			|
//			(SingleTerm) ExprMinus:minus Term:t;
	public void visit(SingleTerm singleTerm) {
		if(!(singleTerm.getExprMinus() instanceof NoExprMinus)) {
			Code.put(Code.neg);
		}
	}
	
	//(StmtRead) READ LPAREN Designator:designator RPAREN SEMI
	public void visit(StmtRead stmtRead) {
		//read ne ocekuje nista na stacku vec cita vrednost i stavlja tu procitanu vrednost na exprstack
		if(stmtRead.getDesignator().obj.getType().getElemType()!=null) {
			if(stmtRead.getDesignator().obj.getType().getElemType()==Tab.charType) {
				Code.put(Code.bread);
			}
			else {
				Code.put(Code.read);
			}
		}
		else {
			if(stmtRead.getDesignator().obj.getType()==Tab.charType) {
				Code.put(Code.bread);
			}
			else {
				Code.put(Code.read);
			}
		}
		if(stmtRead.getDesignator().obj.getType().getElemType()!=null) {
			if(stmtRead.getDesignator().obj.getType().getElemType()==Tab.charType) {
				Code.put(Code.bastore);
			}
			else {
				Code.put(Code.astore);
			}
		}
		else {
			Code.store(stmtRead.getDesignator().obj);
		}
	}
	
	// Factor ::= (FactNewArray) NEW Type LSQUARE Expr RSQUARE
	public void visit(FactNewArray factNewArray) {
		//newarray ocekuje n na ExprStack i to ce da bude na ExprStack zbog Expr
		
		Code.put(Code.newarray);
		if(factNewArray.getType().struct==Tab.charType) {
			Code.loadConst(0);
		}
		else {
			Code.loadConst(1);
		}
	}
	
	//Factor: = "new" Type "[" Expr "]" "[" Expr "]"
//   	(FactNewMatrix) NEW Type LSQUARE Expr RSQUARE LSQUARE Expr RSQUARE
	public void visit(FactNewMatrix factNewMatrix) {
		//na ExprStack se trenutno nalaze expr1=brojRedova i expr2=brojKolona : ..., expr1, expr2
		Code.put(Code.dup_x1);			// ..., expr2, expr1, expr2
		if(factNewMatrix.getType().struct==Tab.charType) {
			Code.loadConst(4);			//ako je matrica karaktera onda rezervisem 4 elementa da bih mogao da smestim na ta 4 elementa niza matrice od po 1B, da smestim 4B
		}
		else {
			Code.loadConst(1);			//ako je matrica ciji su elementi velicine 4B onda mi treba samo 1 dodatan element niza jer je on vec 4B
		}
		Code.put(Code.dup_x2);			//..., expr2, 1ili4, expr1, expr2, 1ili4
		Code.put(Code.pop);				//..., expr2, 1ili4, expr1, expr2
		Code.put(Code.mul);				//..., expr2, 1ili4, expr1*expr2
		Code.put(Code.add);				//...,expr2, expr1*expr2+1ili4
		Code.put(Code.newarray);
		if(factNewMatrix.getType().struct==Tab.charType) {
			Code.loadConst(0);
		}
		else {
			Code.loadConst(1);
		}
										//sada je na steku: ..., expr2, addrOfMatrix
		//hocu da ucitam na prvih 4B tog niza koji predstavlja matricu njen broj kolona da bih mogao posle da pristupam elementima matrice
		Code.put(Code.dup);				//..., expr2, addrOfMAtrix, addrOfMatrix
		Code.put(Code.dup_x2);			//..., addrOfMatrix, expr2, addrOfMAtrix, addrOfMatrix
		Code.put(Code.pop);				//..., addrOfMatrix, expr2, addrOfMAtrix
		Code.put(Code.dup_x1);			//..., addrOfMAtrix, addrOfMAtrix, expr2, addrOfMAtrix
		Code.put(Code.pop);				//..., addrOfMAtrix, addrOfMAtrix, expr2	
		//ucitavam 0 jer hocu da u prvom elementu niza, tj na prvih 4B bude vrednost broja kolona
		Code.loadConst(0); 				//..., addrOfMAtrix, addrOfMatrix, expr2, 0
		Code.put(Code.dup_x1);			//..., addrOfMAtrix, addrOfMatrix, 0, expr2, 0
		Code.put(Code.pop);				//..., addrOfMAtrix, addrOfMatrix, 0, expr2
		Code.put(Code.astore);			//..., addrOfMatrix		-> sad je na prva 4B niza broj kolona matrice
		allocationMatrix=true;
	}
	
	//Factor ::= (FactVar) Designator:d
	public void visit(FactVar factVar) {
		//aload i baload ucitava vrednost na ExprStack i ocekuje adr, index na ExprStack
		if(factVar.getDesignator().obj.getType().getKind()==Struct.Array) {
			if(factVar.getDesignator().obj.getType().getElemType()==Tab.charType) {
				Code.put(Code.baload);
			}
			else {
				Code.put(Code.aload);
			}
		}
	}
	
	public void visit(DesigStmtList desigStmtList) {
		boolean charDesignator=false;
		int count=0;
		if(desigStmtList.getDesignator().obj.getType().getElemType()==Tab.charType) {
			charDesignator=true;
		}
		for(int i=0; i<objectNodes.size(); i++) {
			if(objectNodes.get(i).getAdr()==-1) {
				continue;
			}
//			if(objectNodes.get(i).getType().getKind()==Struct.Array) {
//				count=count+2;
//				Code.load(objectNodes.get(i));
//				Code.loadConst(i);
//			}
			if(objectNodes.get(i).getType().getKind()==Struct.Array) {
				nodesName.add(i);
			}
			else {
				Code.load(desigStmtList.getDesignator().obj);
				Code.loadConst(i);
				if(charDesignator) {
					Code.put(Code.baload);
				}
				else {
					Code.put(Code.aload);
				}
			}
			if(objectNodes.get(i).getType().getKind()!=Struct.Array) {
//				count=count+2;
//				Code.load(objectNodes.get(i));
//				Code.loadConst(i);
//				if(charDesignator) {
//					Code.put(Code.bastore);
//				}
//				else {
//					Code.put(Code.astore);
//				}
				Code.store(objectNodes.get(i));
			}
		}
		for(int i=nodesName.size()-1; i>=0; i--) {
			Code.load(desigStmtList.getDesignator().obj);
			Code.loadConst(nodesName.get(i));
			if(charDesignator) {
				Code.put(Code.baload);
			}
			else {
				Code.put(Code.aload);
			}
			if(charDesignator) {
				Code.put(Code.bastore);
			}
			else {
				Code.put(Code.astore);
			}
		}
//		for(int i=0; i<count; i++) {
//			Code.put(Code.pop);
//		}
		count=0;
		charDesignator=false;
		objectNodes.clear();
		nodesName.clear();
	}
	
	public void visit(DesigExis desigExist) {
//		if(desigExist.getParent().getClass()==SingleDesigExist.class || desigExist.getParent().getClass()==MultipleDesigExist.class) {
			objectNodes.add(desigExist.getDesignator().obj);
//		}
		
//		nodesName.add(desigExist.getDesignator().obj.getName());
	}
	
	public void visit(NoDesigExist noDesigExist) {
		objectNodes.add(new Obj(Obj.Con, "epsilon", Tab.nullType, -1, 0));
//		nodesName.add("epsilon");
	}
	
//	public void visit(DesigMoreList desigMoreList) {
//	//na pocetku je na steku niz
//	Code.load(desigMoreList.getDesignator().obj);		//niz, niz
//	Code.put(Code.arraylength);				//niz, length
//	Code.loadConst(1); 						//niz, length, 1
//	Code.put(Code.sub);						//niz, pos
//	
//	int adrWhile1=Code.pc;
//	Code.put(Code.dup);						//niz, pos, pos
//	Code.loadConst(0); 				//niz, pos, pos, 0
//	Code.putFalseJump(Code.ge, 0); 	//niz, pos
//	int endWhile1=Code.pc-2;
//	
//	Code.put(Code.dup);				//niz, pos, pos
//	Code.loadConst(1); 				//niz, pos, pos, 1
//	Code.put(Code.sub);				//niz, pos, pos-1
//	int adrWhile2=Code.pc;
//	Code.put(Code.dup);						//niz, pos, pos-1, pos-1
//	Code.loadConst(0); 				//niz, pos, pos-1, pos-1, 0
//	Code.putFalseJump(Code.ge, 0); 	//niz, pos, pos-1
//	int endWhile2=Code.pc-2;
//	
//	Code.put(Code.dup2);			//niz, pos, pos-1, pos, pos-1
//	Code.put(Code.pop);			//niz, pos, pos-1, pos
//	Code.load(desigMoreList.getDesignator().obj); 		//niz, pos, pos-1, pos, niz
//	Code.put(Code.dup_x1);		//niz, pos, pos-1, niz, pos, niz
//	Code.put(Code.pop);			//niz, pos, pos-1, niz, pos
//	

//	
//	Code.put(Code.aload);		//niz, pos, pos-1, niz[pos]
//	Code.put(Code.dup_x1);		
//	Code.put(Code.pop);			//niz, pos, niz[pos], pos-1
//	Code.put(Code.dup);			//niz, pos, niz[pos], pos-1, pos-1
//	Code.put(Code.dup_x2);		//niz, pos, pos-1, niz[pos], pos-1, pos-1
//	Code.put(Code.pop);			//niz, pos, pos-1, niz[pos], pos-1
//	Code.load(desigMoreList.getDesignator().obj);		//niz, pos, pos-1, niz[pos], pos-1, niz
//	Code.put(Code.dup_x1);
//	Code.put(Code.pop);			//niz, pos, pos-1, niz[pos], niz, pos-1
//	Code.put(Code.aload);		//niz, pos, pos-1, niz[pos], niz[pos-1]
////	Code.put(Code.dup2);		//niz, pos, pos-1, niz[pos], niz[pos-1], niz[pos], niz[pos-1]
//	
//	//ovo je za if
//	Code.putFalseJump(Code.gt, 0);
//	int elseAdr=Code.pc-2;
////	Code.put(Code.pop);
//	Code.loadConst(0);
//	Code.put(Code.pop);		
//	Code.putJump(0);
//	int thenAdr=Code.pc-2;
//	Code.fixup(elseAdr);
//	//ako treba da se zamene vrednosti ovim elementima onda cu prvo da stavim niz[pos] na stek, pa onda niz[pos]=niz[pos-1] i onda niz[pos-1]=pop
//	Code.put(Code.dup2);	//niz, pos, pos-1, pos, pos-1
//	Code.put(Code.dup2);	//niz, pos, pos-1, pos, pos-1, pos, pos-1
//	Code.put(Code.dup2);	//niz, pos, pos-1, pos, pos-1 pos, pos-1, pos, pos-1
//	Code.put(Code.pop);		//niz, pos, pos-1,, pos, pos-1 pos, pos-1, pos
//	Code.load(desigMoreList.getDesignator().obj);		//niz, pos, pos-1, pos, pos-1, pos, niz
//	Code.put(Code.dup_x1);		//niz, pos, pos-1,, pos, pos-1 pos, pos-1, niz, pos, niz
//	Code.put(Code.pop);			//niz, pos, pos-1,, pos, pos-1 pos, pos-1, niz, pos
//	Code.put(Code.aload);		//niz, pos, pos-1,, pos, pos-1 pos, pos-1, niz[pos]
//	Code.put(Code.dup_x2);		//niz, pos, pos-1,, pos, pos-1 niz[pos], pos, pos-1, niz[pos]
//	Code.put(Code.pop);			//niz, pos, pos-1,, pos, pos-1 niz[pos], pos, pos-1
//	Code.load(desigMoreList.getDesignator().obj); 		//niz, pos, pos-1, niz[pos], pos, pos-1, niz
//	Code.put(Code.dup_x2);	//niz, pos, pos-1,, pos, pos-1 niz[pos], niz, pos, pos-1, niz
//	Code.put(Code.dup_x1);		//niz, pos, pos-1,, pos, pos-1 niz[pos], niz, pos, niz, pos-1, niz
//	Code.put(Code.pop);			//niz, pos, pos-1,, pos, pos-1 niz[pos], niz, pos, niz, pos-1
//	Code.put(Code.aload);		//niz, pos, pos-1, pos, pos-1, niz[pos], niz, pos, niz[pos-1]
//	Code.put(Code.astore);		//niz, pos, pos-1, pos, pos-1, niz[pos]
//	Code.put(Code.dup_x2);		
//	Code.put(Code.pop);			//niz, pos, pos-1, niz[pos], pos, pos-1
//	Code.put(Code.dup_x2);		//niz, pos, pos-1, pos-1, niz[pos], pos, pos-1
//	Code.put(Code.pop);
//	Code.put(Code.pop);			//niz, pos, pos-1, pos-1, niz[pos]
//	Code.load(desigMoreList.getDesignator().obj); 		//niz, pos, pos-1, pos-1, niz[pos], niz
//	Code.put(Code.dup_x2);
//	Code.put(Code.pop);			//niz, pos, pos-1, niz, pos-1, niz[pos]
//	Code.put(Code.astore);		//niz, pos, pos-1
//	Code.fixup(thenAdr);
//	Code.loadConst(1); 			//niz, pos, pos-1, 1
//	Code.put(Code.sub);			//niz, pos, pos-2
//	Code.putJump(adrWhile2);
//	//sada sledi endWhile2
//	Code.fixup(endWhile2);
//	Code.put(Code.pop);		//niz, pos
//	Code.loadConst(1); 		//niz, pos, 1
//	Code.put(Code.sub); 	//niz, pos-1
//	Code.putJump(adrWhile1);
//	Code.fixup(endWhile1);
//	Code.put(Code.pop);
//	Code.put(Code.pop);
//}
	
//	public void visit(SingleDesigExist singleDesigExist) {
//		//ako postoji Designator, tj ako nije prazno mesto
//		if(singleDesigExist.getDesigExist().getClass()!=NoDesigExist.class) {
//			objectNodes.add(singleDesigExist.getDesigExist().ge)
//		}
//	}
	
}
