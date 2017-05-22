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
public class IfStmt implements Stmt {
    
    private OrTest orTest;
    private ArrayList<Stmt> ifStmt;
    private ArrayList<Stmt> elseStmt;
    
    public IfStmt(OrTest orTest, ArrayList<Stmt> ifStmt){
        
        this.orTest = orTest;
        this.ifStmt = ifStmt;
        this.elseStmt = elseStmt;
        
    }
    
    public IfStmt(OrTest orTest, ArrayList<Stmt> ifStmt, ArrayList<Stmt> elseStmt){
        
        this.orTest = orTest;
        this.ifStmt = ifStmt;
        this.elseStmt = elseStmt;
        
    }
    
    public void genC(PW pw){
        int i = 0;
        pw.print("if(");
        
        orTest.genC(pw);
        
        pw.println("{");
        
        while(i < ifStmt.size())
        {
            ifStmt.get(i).genC(pw);
            i++;
        }
        
        i = 0;
        
        if(elseStmt != null)
        {
            pw.println("else {");
            while(i < elseStmt.size())
            {
                elseStmt.get(i).genC(pw);
                i++;
            }
            
            pw.println("}");
        }
        
        pw.println("}");
        
    }
    
}
