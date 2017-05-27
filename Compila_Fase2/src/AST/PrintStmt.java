/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;
import java.util.*;
import Principal.*;
/**
 *
 * @author matheus
 */
public class PrintStmt implements Stmt {
    
    
    private ArrayList<OrTest> orTest;
    boolean flag = false;
 
    public PrintStmt (ArrayList<OrTest> orTest){
        this.orTest = orTest;
    }
    
    public void genC(PW pw)
    {
        VariablesTable.flag = true;
        char c = 34;
        pw.print("printf(" + c);
        int i = 0;
        
        orTest.get(i).genC(pw, flag);
        
        i++;
        
        while(i < orTest.size())
        {
            orTest.get(i).genC(pw, flag);
            i++;
        }
        
        
        pw.print(c + "");
        
        if(!VariablesTable.string.isEmpty())
        {
            int j = 0;
            while(j < VariablesTable.string.size())
            {
                pw.print(",");
                pw.print(" " + VariablesTable.string.get(j) + " ");
                j++;
            }
        }
        
        pw.println(");");
        
        VariablesTable.flag = false;
        VariablesTable.string.clear();
    }
}
