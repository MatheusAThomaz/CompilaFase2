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
public class Expr {
    
    private ArrayList<Term> term;
    private char []signal;
    
    public Expr (char[] signal, ArrayList<Term> term)
    {
        this.signal = signal;
        this.term = term;
    }
    
    public void genC(PW pw, boolean flag){
        
        int i = 0, y = 0;
        
        if(term.size() >= 1)
            term.get(i).genC(pw, flag);
        i++;
        
        while(i < term.size())
        {
            
            pw.print("" + signal[y]);
            
            term.get(i).genC(pw, flag);
            i++;
            y++;
        }
    }
    
    
}
