// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class MultipleVarDeclarations extends VarDeclIdentList {

    private VarDeclIdentList VarDeclIdentList;
    private String varMultName;
    private ArraySquares ArraySquares;

    public MultipleVarDeclarations (VarDeclIdentList VarDeclIdentList, String varMultName, ArraySquares ArraySquares) {
        this.VarDeclIdentList=VarDeclIdentList;
        if(VarDeclIdentList!=null) VarDeclIdentList.setParent(this);
        this.varMultName=varMultName;
        this.ArraySquares=ArraySquares;
        if(ArraySquares!=null) ArraySquares.setParent(this);
    }

    public VarDeclIdentList getVarDeclIdentList() {
        return VarDeclIdentList;
    }

    public void setVarDeclIdentList(VarDeclIdentList VarDeclIdentList) {
        this.VarDeclIdentList=VarDeclIdentList;
    }

    public String getVarMultName() {
        return varMultName;
    }

    public void setVarMultName(String varMultName) {
        this.varMultName=varMultName;
    }

    public ArraySquares getArraySquares() {
        return ArraySquares;
    }

    public void setArraySquares(ArraySquares ArraySquares) {
        this.ArraySquares=ArraySquares;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclIdentList!=null) VarDeclIdentList.accept(visitor);
        if(ArraySquares!=null) ArraySquares.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclIdentList!=null) VarDeclIdentList.traverseTopDown(visitor);
        if(ArraySquares!=null) ArraySquares.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclIdentList!=null) VarDeclIdentList.traverseBottomUp(visitor);
        if(ArraySquares!=null) ArraySquares.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MultipleVarDeclarations(\n");

        if(VarDeclIdentList!=null)
            buffer.append(VarDeclIdentList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+varMultName);
        buffer.append("\n");

        if(ArraySquares!=null)
            buffer.append(ArraySquares.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MultipleVarDeclarations]");
        return buffer.toString();
    }
}
