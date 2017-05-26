/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author maiks
 */
public class Boolean{
     
    private boolean bool;
    
    public void Boolean(boolean bool){
        
        this.bool = bool;
        
    }
    
    public void genC(PW pw){
        
        if(bool)
            pw.print("1");
        else
            pw.print("0");     
    }
}
