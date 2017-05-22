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
public class Term {
    
    private ArrayList<Factor> factor;
    private char []signal;
    
    public Term(char[] signal, ArrayList<Factor> factor)
    {
        this.signal = signal;
        this.factor = factor;
    }
    
    public void genC(PW pw){
        
        int i = 0;
        
        if(factor.size() >= 1)
            factor.get(i).genC(pw);
        i++;
        
        while(i < factor.size())
        {
            pw.print("" + signal[i]);
            
            factor.get(i).genC(pw);
        }
    }
    
    
}
