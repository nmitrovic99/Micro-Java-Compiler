// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class Expr implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private Terms Terms;

    public Expr (Terms Terms) {
        this.Terms=Terms;
        if(Terms!=null) Terms.setParent(this);
    }

    public Terms getTerms() {
        return Terms;
    }

    public void setTerms(Terms Terms) {
        this.Terms=Terms;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Terms!=null) Terms.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Terms!=null) Terms.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Terms!=null) Terms.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Expr(\n");

        if(Terms!=null)
            buffer.append(Terms.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Expr]");
        return buffer.toString();
    }
}
