// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class FactNumConst extends Factor {

    private Integer numconst;

    public FactNumConst (Integer numconst) {
        this.numconst=numconst;
    }

    public Integer getNumconst() {
        return numconst;
    }

    public void setNumconst(Integer numconst) {
        this.numconst=numconst;
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
        buffer.append("FactNumConst(\n");

        buffer.append(" "+tab+numconst);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactNumConst]");
        return buffer.toString();
    }
}
