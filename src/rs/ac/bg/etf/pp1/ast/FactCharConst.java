// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class FactCharConst extends Factor {

    private Character charconst;

    public FactCharConst (Character charconst) {
        this.charconst=charconst;
    }

    public Character getCharconst() {
        return charconst;
    }

    public void setCharconst(Character charconst) {
        this.charconst=charconst;
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
        buffer.append("FactCharConst(\n");

        buffer.append(" "+tab+charconst);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactCharConst]");
        return buffer.toString();
    }
}
