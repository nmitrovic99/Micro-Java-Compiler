// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclNoBody extends ClassDecl {

    private String I1;
    private ClassExtends ClassExtends;
    private VarDeclList VarDeclList;

    public ClassDeclNoBody (String I1, ClassExtends ClassExtends, VarDeclList VarDeclList) {
        this.I1=I1;
        this.ClassExtends=ClassExtends;
        if(ClassExtends!=null) ClassExtends.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public ClassExtends getClassExtends() {
        return ClassExtends;
    }

    public void setClassExtends(ClassExtends ClassExtends) {
        this.ClassExtends=ClassExtends;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassExtends!=null) ClassExtends.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassExtends!=null) ClassExtends.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassExtends!=null) ClassExtends.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclNoBody(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(ClassExtends!=null)
            buffer.append(ClassExtends.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclNoBody]");
        return buffer.toString();
    }
}
