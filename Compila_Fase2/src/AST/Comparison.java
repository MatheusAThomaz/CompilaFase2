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
public class Comparison {
    
    private Expr expr1;
    private CompOp compOp;
    private Expr expr2;
    
    public Comparison(Expr expr1){
        this.expr1 = expr1;
        this.compOp = null;
        this.expr2 = null;
    }
    
    public Comparison(Expr expr1, CompOp compOp, Expr expr2){
        this.expr1 = expr1;
        this.compOp = compOp;
        this.expr2 = expr2;       
    }
    
    public void genC(PW pw, boolean flag){
        
        expr1.genC(pw, flag);
        
        if(compOp != null)
        {
            compOp.genC(pw);
            expr2.genC(pw, flag);
        }
        
        
    }
}
