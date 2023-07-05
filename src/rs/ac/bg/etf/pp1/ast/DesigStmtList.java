// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class DesigStmtList extends DesignatorStatement {

    private DesigExistList DesigExistList;
    private Designator Designator;

    public DesigStmtList (DesigExistList DesigExistList, Designator Designator) {
        this.DesigExistList=DesigExistList;
        if(DesigExistList!=null) DesigExistList.setParent(this);
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
    }

    public DesigExistList getDesigExistList() {
        return DesigExistList;
    }

    public void setDesigExistList(DesigExistList DesigExistList) {
        this.DesigExistList=DesigExistList;
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesigExistList!=null) DesigExistList.accept(visitor);
        if(Designator!=null) Designator.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesigExistList!=null) DesigExistList.traverseTopDown(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesigExistList!=null) DesigExistList.traverseBottomUp(visitor);
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesigStmtList(\n");

        if(DesigExistList!=null)
            buffer.append(DesigExistList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesigStmtList]");
        return buffer.toString();
    }
}
