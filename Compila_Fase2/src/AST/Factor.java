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
public class Factor {
    
    private ArrayList<Factor> factor;
    private Atom atom;
    private char signal;
    
    public Factor(char signal, Atom atom, ArrayList<Factor> factor){
        this.signal = signal;
        this.factor = factor;
        this.atom = atom;
    }
    
    public void genC(PW pw, boolean flag){
       int i = 0;
        if(signal == '+')
            pw.print(" + ");
        else if(signal == '-')
            pw.print(" - ");
        
        atom.genC(pw, flag);
        
        while(i < factor.size())
        {
            pw.print(" ^ ");
            factor.get(i).genC(pw, flag);
            i++;
        }

    }
}
