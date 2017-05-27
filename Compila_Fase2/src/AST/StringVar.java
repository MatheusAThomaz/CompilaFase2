/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import Principal.VariablesTable;

/**
 *
 * @author matheus
 */
public class StringVar{
    
    private String value;
    
    public String getValue(){
        return this.value;
    }
    
    public StringVar(String value){
        this.value = value;
    }
    
    public void genC(PW pw){
       
            pw.print(value);
    }
}
