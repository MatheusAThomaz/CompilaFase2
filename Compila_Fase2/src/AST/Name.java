/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import Lexer.Symbol;
import Principal.VariablesTable;

/**
 *
 * @author matheus
 */
public class Name implements Variables{
    
    private String value;
    
    public Name(String value){
        this.value = value;
    }
    
    public String getName(){
        return this.value;
    }
    
    public void genC(PW pw)
    {
        if(VariablesTable.flag)
        {
            int tipo = VariablesTable.getTable(value);
            
            if(tipo == Symbol.INT || tipo == Symbol.VETORINT)
                pw.print(" %d ");
            else if(tipo == Symbol.FLOAT || tipo == Symbol.VETORFLOAT)
                pw.print(" %f ");
            else if(tipo == Symbol.CHAR)
                pw.print(" %c ");
            else if(tipo == Symbol.STRING)
                pw.print(" %s ");       
        }
        
        else
            pw.print(value);
    }
}
