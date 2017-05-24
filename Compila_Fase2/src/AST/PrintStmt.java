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
public class PrintStmt implements Stmt {
    
    private ArrayList<OrTest> orTest;
    
    public PrintStmt (ArrayList<OrTest> orTest){
        this.orTest = orTest;
    }
    
    public void genC(PW pw)
    {
        int i = 0;
        
        orTest.get(i).genC(pw);
        
        i++;
        
        while(i < orTest.size())
        {
            pw.print(",");
            orTest.get(i).genC(pw);
            i++;
        }
        
        pw.println(";");
    }
}
