/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;
import java.util.*;

/**
 *
 * @author matheus
 */
public class ExprList {
    
    private ArrayList <Expr> arrayExpr;
    
    public ExprList(ArrayList <Expr> arrayExpr){
        this.arrayExpr = arrayExpr;
    }
    
    public void genC(PW pw){
        int i = 0;
        while (i < arrayExpr.size()){
            arrayExpr.get(i).genC(pw, false);
            i++;           
        }  
    }
    
}
