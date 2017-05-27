/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;
import java.util.*;

/**
 *
 * @author maiks
 */
public class IdList {
    
    private ArrayList<Variables> variables;
    
    public IdList(ArrayList<Variables> variables)
    {
        this.variables = variables;
    }
    
    public void genC(PW pw){
        
        int i = 0;
        
        variables.get(i).genC(pw);
        i++;
        
        while(i < variables.size())
        {
            pw.print(" , ");
            variables.get(i).genC(pw);
            
            i++;
        }
        
    }
    
    
}
