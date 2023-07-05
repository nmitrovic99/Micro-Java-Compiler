// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class ConstantChar extends Constant {

    private ConstChar ConstChar;

    public ConstantChar (ConstChar ConstChar) {
        this.ConstChar=ConstChar;
        if(ConstChar!=null) ConstChar.setParent(this);
    }

    public ConstChar getConstChar() {
        return ConstChar;
    }

    public void setConstChar(ConstChar ConstChar) {
        this.ConstChar=ConstChar;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstChar!=null) ConstChar.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstChar!=null) ConstChar.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstChar!=null) ConstChar.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstantChar(\n");

        if(ConstChar!=null)
            buffer.append(ConstChar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstantChar]");
        return buffer.toString();
    }
}
