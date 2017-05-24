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
public class WhileStmt implements Stmt {
    
    private ArrayList<Stmt> stmt;
    private OrTest orTest;
    
    
    public WhileStmt(OrTest orTest, ArrayList<Stmt> stmt)
    {
        this.stmt =stmt;
    }
    
    public void genC(PW pw)
    {
        int i = 0;
        pw.print("while(");
        
        orTest.genC(pw);
        pw.print("){");
        while(i < stmt.size())
        {
            stmt.get(i).genC(pw);
            i++;
        }
        
        pw.print("}");
    }
    
}
