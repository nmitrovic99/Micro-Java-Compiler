package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;

public class RuleVisitor extends VisitorAdaptor{

	int printCallCount = 0;
	int printCallNumCount = 0;
	int varDeclCount = 0;
	
	Logger log = Logger.getLogger(getClass());

	public void visit(SingleVarDeclarations vardecl){
		varDeclCount++;
	}
	
	public void visit(MultipleVarDeclarations vardecl){
		varDeclCount++;
	}
	
    public void visit(StmtPrint print) {
		printCallCount++;
	}
    
    public void visit(StmtPrintNum print) {
    	printCallNumCount++;
	}

}
