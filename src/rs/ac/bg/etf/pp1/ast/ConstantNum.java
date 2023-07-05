// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class ConstantNum extends Constant {

    private ConstNum ConstNum;

    public ConstantNum (ConstNum ConstNum) {
        this.ConstNum=ConstNum;
        if(ConstNum!=null) ConstNum.setParent(this);
    }

    public ConstNum getConstNum() {
        return ConstNum;
    }

    public void setConstNum(ConstNum ConstNum) {
        this.ConstNum=ConstNum;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstNum!=null) ConstNum.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstNum!=null) ConstNum.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstNum!=null) ConstNum.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstantNum(\n");

        if(ConstNum!=null)
            buffer.append(ConstNum.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstantNum]");
        return buffer.toString();
    }
}
