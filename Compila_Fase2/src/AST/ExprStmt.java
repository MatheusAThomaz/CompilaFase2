/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author matheus
 */
public class ExprStmt implements Stmt{
    
    private Name name;
    private Vetor vector;
    private OrTest orTest;
    private ExprList exprList;
    
    
    public ExprStmt(Name name, OrTest orTest){
        
        this.name = name;
        this.orTest = orTest;
        this.vector = null;
        this.exprList = null;
    }
    
    public ExprStmt(Name name, ExprList exprList){
        
        this.name = name;
        this.exprList = exprList;
        this.vector = null;
        this.orTest = null;
    }
    
    public ExprStmt(Vetor vetor, OrTest orTest){
        
        this.name = null;
        this.exprList = null;
        this.vector = vetor;
        this.orTest = orTest;
    }
    
    public ExprStmt(Vetor vetor, ExprList exprList){
        
        this.name = null;
        this.exprList = exprList;
        this.vector = vetor;
        this.orTest = null;
    }
    
    
    
    public void genC(PW pw){
        
        if(name == null)
        {
            vector.genC(pw);
            pw.print(" = ");
            if(exprList == null)
                orTest.genC(pw);
            else
                exprList.genC(pw);
                
        }
        else{
            name.genC(pw);
            pw.print(" = ");
            if(exprList == null)
                orTest.genC(pw);
            else
                exprList.genC(pw);
        }
        
        pw.print(";");
        
    }
}
