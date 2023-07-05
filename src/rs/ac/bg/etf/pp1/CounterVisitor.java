package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.FormalParamDecl;
import rs.ac.bg.etf.pp1.ast.MultipleVarDeclarations;
import rs.ac.bg.etf.pp1.ast.SingleVarDeclarations;
import rs.ac.bg.etf.pp1.ast.VarDeclaration;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class CounterVisitor extends VisitorAdaptor {
	
	protected int count;
	
	public int getCount(){
		return count;
	}
	
	public static class FormParamCounter extends CounterVisitor{
	
		public void visit(FormalParamDecl formParamDecl){
			count++;
		}
	}
	
	public static class VarCounter extends CounterVisitor{
		
		public void visit(SingleVarDeclarations singleVarDeclarations) {
			count++;
		}
		
		public void visit(MultipleVarDeclarations multipleVarDeclarations) {
			count++;
		}
	}
}
