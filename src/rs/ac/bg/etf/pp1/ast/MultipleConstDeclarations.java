// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class MultipleConstDeclarations extends ConstDeclIdentList {

    private ConstDeclIdentList ConstDeclIdentList;
    private String constantName;
    private Constant Constant;

    public MultipleConstDeclarations (ConstDeclIdentList ConstDeclIdentList, String constantName, Constant Constant) {
        this.ConstDeclIdentList=ConstDeclIdentList;
        if(ConstDeclIdentList!=null) ConstDeclIdentList.setParent(this);
        this.constantName=constantName;
        this.Constant=Constant;
        if(Constant!=null) Constant.setParent(this);
    }

    public ConstDeclIdentList getConstDeclIdentList() {
        return ConstDeclIdentList;
    }

    public void setConstDeclIdentList(ConstDeclIdentList ConstDeclIdentList) {
        this.ConstDeclIdentList=ConstDeclIdentList;
    }

    public String getConstantName() {
        return constantName;
    }

    public void setConstantName(String constantName) {
        this.constantName=constantName;
    }

    public Constant getConstant() {
        return Constant;
    }

    public void setConstant(Constant Constant) {
        this.Constant=Constant;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclIdentList!=null) ConstDeclIdentList.accept(visitor);
        if(Constant!=null) Constant.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclIdentList!=null) ConstDeclIdentList.traverseTopDown(visitor);
        if(Constant!=null) Constant.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclIdentList!=null) ConstDeclIdentList.traverseBottomUp(visitor);
        if(Constant!=null) Constant.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MultipleConstDeclarations(\n");

        if(ConstDeclIdentList!=null)
            buffer.append(ConstDeclIdentList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+constantName);
        buffer.append("\n");

        if(Constant!=null)
            buffer.append(Constant.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MultipleConstDeclarations]");
        return buffer.toString();
    }
}
