// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class ConstNum implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private Integer numconst;

    public ConstNum (Integer numconst) {
        this.numconst=numconst;
    }

    public Integer getNumconst() {
        return numconst;
    }

    public void setNumconst(Integer numconst) {
        this.numconst=numconst;
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
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstNum(\n");

        buffer.append(" "+tab+numconst);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstNum]");
        return buffer.toString();
    }
}
