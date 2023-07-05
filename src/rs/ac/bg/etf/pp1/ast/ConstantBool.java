// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class ConstantBool extends Constant {

    private ConstBool ConstBool;

    public ConstantBool (ConstBool ConstBool) {
        this.ConstBool=ConstBool;
        if(ConstBool!=null) ConstBool.setParent(this);
    }

    public ConstBool getConstBool() {
        return ConstBool;
    }

    public void setConstBool(ConstBool ConstBool) {
        this.ConstBool=ConstBool;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstBool!=null) ConstBool.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstBool!=null) ConstBool.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstBool!=null) ConstBool.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstantBool(\n");

        if(ConstBool!=null)
            buffer.append(ConstBool.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstantBool]");
        return buffer.toString();
    }
}
