/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author matheus
 */
public class NumberFloat {
    
    private float value;
    
    public NumberFloat(float value){
        this.value = value;
    }
    
    public void genC(PW pw){
        pw.print(""+ value);
        
    }
    
}
