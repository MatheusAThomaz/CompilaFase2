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
public class NumberFloat  implements NumberInterface{
    
    private float value;
    
    public NumberFloat(float value){
        this.value = value;
    }
    
    public float getValue(){
        return value;
    }
    
    public int getInt(){
        return 0;
    }
    
    public void genC(PW pw){
        
        if(!VariablesTable.flag)
             pw.print("" + value);
        else
            pw.print(" %f ");
        
    }
    
}
