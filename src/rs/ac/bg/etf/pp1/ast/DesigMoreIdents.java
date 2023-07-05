// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public class DesigMoreIdents extends Designator {

    private Designator Designator;
    private Idents Idents;

    public DesigMoreIdents (Designator Designator, Idents Idents) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.Idents=Idents;
        if(Idents!=null) Idents.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public Idents getIdents() {
        return Idents;
    }

    public void setIdents(Idents Idents) {
        this.Idents=Idents;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(Idents!=null) Idents.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(Idents!=null) Idents.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(Idents!=null) Idents.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesigMoreIdents(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Idents!=null)
            buffer.append(Idents.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesigMoreIdents]");
        return buffer.toString();
    }
}
