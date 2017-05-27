/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;
import Principal.*;

/**
 *
 * @author matheus
 */
public class NumberInt implements NumberInterface {
    
    private int value;
    
    public NumberInt(int value){
        this.value = value;
    }
    
    public int getInt(){
        return this.value;
    }
    
    public float getValue(){
        //error;
        return 0;
    }
    
    public void genC(PW pw){
        if(!VariablesTable.flag)
            pw.print(" " + value);
        else
            pw.print(" %d ");
    }
    
}
