// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class SingleVarDeclarations extends VarDeclIdentList {

    private String varSingleName;
    private ArraySquares ArraySquares;

    public SingleVarDeclarations (String varSingleName, ArraySquares ArraySquares) {
        this.varSingleName=varSingleName;
        this.ArraySquares=ArraySquares;
        if(ArraySquares!=null) ArraySquares.setParent(this);
    }

    public String getVarSingleName() {
        return varSingleName;
    }

    public void setVarSingleName(String varSingleName) {
        this.varSingleName=varSingleName;
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
        if(ArraySquares!=null) ArraySquares.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ArraySquares!=null) ArraySquares.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ArraySquares!=null) ArraySquares.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleVarDeclarations(\n");

        buffer.append(" "+tab+varSingleName);
        buffer.append("\n");

        if(ArraySquares!=null)
            buffer.append(ArraySquares.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleVarDeclarations]");
        return buffer.toString();
    }
}
