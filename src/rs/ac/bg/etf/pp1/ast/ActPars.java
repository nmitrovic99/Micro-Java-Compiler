// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class ActPars implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private Expressions Expressions;

    public ActPars (Expressions Expressions) {
        this.Expressions=Expressions;
        if(Expressions!=null) Expressions.setParent(this);
    }

    public Expressions getExpressions() {
        return Expressions;
    }

    public void setExpressions(Expressions Expressions) {
        this.Expressions=Expressions;
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
        if(Expressions!=null) Expressions.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expressions!=null) Expressions.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expressions!=null) Expressions.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActPars(\n");

        if(Expressions!=null)
            buffer.append(Expressions.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActPars]");
        return buffer.toString();
    }
}
