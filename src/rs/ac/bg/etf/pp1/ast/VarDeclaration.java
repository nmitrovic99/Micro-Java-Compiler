// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class VarDeclaration extends VarDecl {

    private Type Type;
    private VarDeclIdentList VarDeclIdentList;

    public VarDeclaration (Type Type, VarDeclIdentList VarDeclIdentList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarDeclIdentList=VarDeclIdentList;
        if(VarDeclIdentList!=null) VarDeclIdentList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public VarDeclIdentList getVarDeclIdentList() {
        return VarDeclIdentList;
    }

    public void setVarDeclIdentList(VarDeclIdentList VarDeclIdentList) {
        this.VarDeclIdentList=VarDeclIdentList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(VarDeclIdentList!=null) VarDeclIdentList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarDeclIdentList!=null) VarDeclIdentList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarDeclIdentList!=null) VarDeclIdentList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclaration(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclIdentList!=null)
            buffer.append(VarDeclIdentList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclaration]");
        return buffer.toString();
    }
}
