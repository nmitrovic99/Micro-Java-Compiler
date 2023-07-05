// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class SingleDesigExist extends DesigExistList {

    private DesigExist DesigExist;

    public SingleDesigExist (DesigExist DesigExist) {
        this.DesigExist=DesigExist;
        if(DesigExist!=null) DesigExist.setParent(this);
    }

    public DesigExist getDesigExist() {
        return DesigExist;
    }

    public void setDesigExist(DesigExist DesigExist) {
        this.DesigExist=DesigExist;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesigExist!=null) DesigExist.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesigExist!=null) DesigExist.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesigExist!=null) DesigExist.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleDesigExist(\n");

        if(DesigExist!=null)
            buffer.append(DesigExist.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleDesigExist]");
        return buffer.toString();
    }
}
