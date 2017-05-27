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
    
    public void genC(PW pw, boolean flag, String name){
        int i = 0;

        
        if(flag){
            pw.print(name+"["+i+"]" + "=");
            arrayExpr.get(i).genC(pw, false);
            i++;
            
            while(i < arrayExpr.size()){
                pw.println(";");
                pw.print(name+"["+i+"]" + "=");
                arrayExpr.get(i).genC(pw, false);
                i++;
            }
        }
        else
        {
                arrayExpr.get(i).genC(pw, false);
                i++;
            while (i < arrayExpr.size()){
                 pw.print(", ");
                arrayExpr.get(i).genC(pw, false);

                i++;           
            }  
        }
    }
    
}
