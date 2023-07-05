// generated with ast extension for cup
// version 0.8
// 18/5/2023 10:38:24


package rs.ac.bg.etf.pp1.ast;

public abstract class VisitorAdaptor implements Visitor { 

    public void visit(Unmatched Unmatched) { }
    public void visit(DesigExist DesigExist) { }
    public void visit(Mulop Mulop) { }
    public void visit(MethodDecl MethodDecl) { }
    public void visit(Constant Constant) { }
    public void visit(ConstructorDecl ConstructorDecl) { }
    public void visit(Matched Matched) { }
    public void visit(Relop Relop) { }
    public void visit(ProgDeclList ProgDeclList) { }
    public void visit(StatementList StatementList) { }
    public void visit(Addop Addop) { }
    public void visit(Factor Factor) { }
    public void visit(VarDeclIdentList VarDeclIdentList) { }
    public void visit(CondTerm CondTerm) { }
    public void visit(Designator Designator) { }
    public void visit(Term Term) { }
    public void visit(Condition Condition) { }
    public void visit(Terms Terms) { }
    public void visit(ConstDeclList ConstDeclList) { }
    public void visit(DesigExistList DesigExistList) { }
    public void visit(ActualParams ActualParams) { }
    public void visit(ConstructorDeclList ConstructorDeclList) { }
    public void visit(ClassExtends ClassExtends) { }
    public void visit(VarDeclList VarDeclList) { }
    public void visit(FormalParams FormalParams) { }
    public void visit(DesignatorStatement DesignatorStatement) { }
    public void visit(ConstDeclIdentList ConstDeclIdentList) { }
    public void visit(Statement Statement) { }
    public void visit(VarDecl VarDecl) { }
    public void visit(ClassDecl ClassDecl) { }
    public void visit(CondFact CondFact) { }
    public void visit(MethodDeclList MethodDeclList) { }
    public void visit(Expressions Expressions) { }
    public void visit(FormPars FormPars) { }
    public void visit(Idents Idents) { }
    public void visit(NoConstructorDecl NoConstructorDecl) { visit(); }
    public void visit(ConstructorDeclarations ConstructorDeclarations) { visit(); }
    public void visit(ClassExtendsNo ClassExtendsNo) { visit(); }
    public void visit(ClassExtendsYes ClassExtendsYes) { visit(); }
    public void visit(ClassDeclNoBody ClassDeclNoBody) { visit(); }
    public void visit(ClassDeclBody ClassDeclBody) { visit(); }
    public void visit(MulopMod MulopMod) { visit(); }
    public void visit(MulopDivide MulopDivide) { visit(); }
    public void visit(MulopTimes MulopTimes) { visit(); }
    public void visit(AddopMinus AddopMinus) { visit(); }
    public void visit(AddopPlus AddopPlus) { visit(); }
    public void visit(RelopLTE RelopLTE) { visit(); }
    public void visit(RelopLT RelopLT) { visit(); }
    public void visit(RelopGTE RelopGTE) { visit(); }
    public void visit(RelopGT RelopGT) { visit(); }
    public void visit(RelopNoEqTo RelopNoEqTo) { visit(); }
    public void visit(RelopEqTo RelopEqTo) { visit(); }
    public void visit(Assignop Assignop) { visit(); }
    public void visit(Label Label) { visit(); }
    public void visit(IdentArray IdentArray) { visit(); }
    public void visit(IdentField IdentField) { visit(); }
    public void visit(DesigSingleIdent DesigSingleIdent) { visit(); }
    public void visit(DesigMoreIdents DesigMoreIdents) { visit(); }
    public void visit(FactNewParams FactNewParams) { visit(); }
    public void visit(FactNewMatrix FactNewMatrix) { visit(); }
    public void visit(FactNewArray FactNewArray) { visit(); }
    public void visit(FactExpr FactExpr) { visit(); }
    public void visit(FactFuncCall FactFuncCall) { visit(); }
    public void visit(FactVar FactVar) { visit(); }
    public void visit(FactBoolConst FactBoolConst) { visit(); }
    public void visit(FactCharConst FactCharConst) { visit(); }
    public void visit(FactNumConst FactNumConst) { visit(); }
    public void visit(SingleFactor SingleFactor) { visit(); }
    public void visit(MultFactors MultFactors) { visit(); }
    public void visit(SingleTerm SingleTerm) { visit(); }
    public void visit(AddExpr AddExpr) { visit(); }
    public void visit(NoExprMinus NoExprMinus) { visit(); }
    public void visit(ExprMinus ExprMinus) { visit(); }
    public void visit(Expr Expr) { visit(); }
    public void visit(CondFactExpr CondFactExpr) { visit(); }
    public void visit(CondFactExprRelop CondFactExprRelop) { visit(); }
    public void visit(SingleCondFact SingleCondFact) { visit(); }
    public void visit(MultCondFacts MultCondFacts) { visit(); }
    public void visit(SingleCondTerm SingleCondTerm) { visit(); }
    public void visit(MultCondTerms MultCondTerms) { visit(); }
    public void visit(SingleExpr SingleExpr) { visit(); }
    public void visit(MultExpressions MultExpressions) { visit(); }
    public void visit(ActPars ActPars) { visit(); }
    public void visit(SingleDesigExist SingleDesigExist) { visit(); }
    public void visit(MultipleDesigExist MultipleDesigExist) { visit(); }
    public void visit(NoDesigExist NoDesigExist) { visit(); }
    public void visit(DesigExis DesigExis) { visit(); }
    public void visit(NoActualParams NoActualParams) { visit(); }
    public void visit(ActualParameters ActualParameters) { visit(); }
    public void visit(DesigStmtList DesigStmtList) { visit(); }
    public void visit(DesigStmtDec DesigStmtDec) { visit(); }
    public void visit(DesigStmtInc DesigStmtInc) { visit(); }
    public void visit(DesigStmtFuncCall DesigStmtFuncCall) { visit(); }
    public void visit(ListOfStatement ListOfStatement) { visit(); }
    public void visit(StmtForeach StmtForeach) { visit(); }
    public void visit(StmtPrintNum StmtPrintNum) { visit(); }
    public void visit(StmtPrint StmtPrint) { visit(); }
    public void visit(StmtRead StmtRead) { visit(); }
    public void visit(StmtReturnExpr StmtReturnExpr) { visit(); }
    public void visit(StmtReturn StmtReturn) { visit(); }
    public void visit(StmtContinue StmtContinue) { visit(); }
    public void visit(StmtBreak StmtBreak) { visit(); }
    public void visit(StmtWhile StmtWhile) { visit(); }
    public void visit(StmtIfElse StmtIfElse) { visit(); }
    public void visit(ErrAssignment ErrAssignment) { visit(); }
    public void visit(DesigStmtAssignop DesigStmtAssignop) { visit(); }
    public void visit(StmtDesignator StmtDesignator) { visit(); }
    public void visit(UnmatchedIfElse UnmatchedIfElse) { visit(); }
    public void visit(UnmatchedIf UnmatchedIf) { visit(); }
    public void visit(UnmachedStmt UnmachedStmt) { visit(); }
    public void visit(MatchedStmt MatchedStmt) { visit(); }
    public void visit(NoStatement NoStatement) { visit(); }
    public void visit(Statements Statements) { visit(); }
    public void visit(Type Type) { visit(); }
    public void visit(FormalParamDecl FormalParamDecl) { visit(); }
    public void visit(SingleFormalParamDecl SingleFormalParamDecl) { visit(); }
    public void visit(FormalParamDecls FormalParamDecls) { visit(); }
    public void visit(MethodVoidName MethodVoidName) { visit(); }
    public void visit(MethodTypeName MethodTypeName) { visit(); }
    public void visit(MethodDeclVoid MethodDeclVoid) { visit(); }
    public void visit(MethodDeclType MethodDeclType) { visit(); }
    public void visit(NoMethodDecl NoMethodDecl) { visit(); }
    public void visit(MethodDeclarations MethodDeclarations) { visit(); }
    public void visit(NoFormalParams NoFormalParams) { visit(); }
    public void visit(FormalParameters FormalParameters) { visit(); }
    public void visit(NoArraySquares NoArraySquares) { visit(); }
    public void visit(ArraySquares ArraySquares) { visit(); }
    public void visit(ArrayMatrix ArrayMatrix) { visit(); }
    public void visit(SingleVarDeclarations SingleVarDeclarations) { visit(); }
    public void visit(MultipleVarDeclarations MultipleVarDeclarations) { visit(); }
    public void visit(VarDeclSemiError VarDeclSemiError) { visit(); }
    public void visit(VarDeclCommaError VarDeclCommaError) { visit(); }
    public void visit(VarDeclaration VarDeclaration) { visit(); }
    public void visit(NoVarDecl NoVarDecl) { visit(); }
    public void visit(VarDeclarations VarDeclarations) { visit(); }
    public void visit(ConstChar ConstChar) { visit(); }
    public void visit(ConstBool ConstBool) { visit(); }
    public void visit(ConstNum ConstNum) { visit(); }
    public void visit(ConstantChar ConstantChar) { visit(); }
    public void visit(ConstantBool ConstantBool) { visit(); }
    public void visit(ConstantNum ConstantNum) { visit(); }
    public void visit(SingleConstDeclarations SingleConstDeclarations) { visit(); }
    public void visit(MultipleConstDeclarations MultipleConstDeclarations) { visit(); }
    public void visit(ConstDecl ConstDecl) { visit(); }
    public void visit(NoConstDecl NoConstDecl) { visit(); }
    public void visit(ConstDeclarations ConstDeclarations) { visit(); }
    public void visit(NoProgDecl NoProgDecl) { visit(); }
    public void visit(ProgClassDeclarations ProgClassDeclarations) { visit(); }
    public void visit(ProgVarDeclarations ProgVarDeclarations) { visit(); }
    public void visit(ProgConstDeclarations ProgConstDeclarations) { visit(); }
    public void visit(ProgramName ProgramName) { visit(); }
    public void visit(Program Program) { visit(); }


    public void visit() { }
}
