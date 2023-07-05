// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class ConstChar implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private Character charconst;

    public ConstChar (Character charconst) {
        this.charconst=charconst;
    }

    public Character getCharconst() {
        return charconst;
    }

    public void setCharconst(Character charconst) {
        this.charconst=charconst;
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
        buffer.append("ConstChar(\n");

        buffer.append(" "+tab+charconst);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstChar]");
        return buffer.toString();
    }
}
