// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class FormalParamDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private String ident;
    private ArraySquares ArraySquares;

    public FormalParamDecl (Type Type, String ident, ArraySquares ArraySquares) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.ident=ident;
        this.ArraySquares=ArraySquares;
        if(ArraySquares!=null) ArraySquares.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident=ident;
    }

    public ArraySquares getArraySquares() {
        return ArraySquares;
    }

    public void setArraySquares(ArraySquares ArraySquares) {
        this.ArraySquares=ArraySquares;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(ArraySquares!=null) ArraySquares.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ArraySquares!=null) ArraySquares.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ArraySquares!=null) ArraySquares.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormalParamDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+ident);
        buffer.append("\n");

        if(ArraySquares!=null)
            buffer.append(ArraySquares.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormalParamDecl]");
        return buffer.toString();
    }
}
