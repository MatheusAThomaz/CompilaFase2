/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;
import Principal.*;

/**
 *
 * @author maiks
 */
public class Char{
    
    private char ch;
    
    public void Char(char ch){
        
        this.ch = ch;
        
    }
    
    public void genC(PW pw){
        
        if(!VariablesTable.flag)
            pw.print("" + ch);
        else
        {
            pw.print("%c");
        }
        
    }
    
}
