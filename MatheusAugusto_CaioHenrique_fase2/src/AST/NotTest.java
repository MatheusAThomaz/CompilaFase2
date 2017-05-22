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
public class NotTest {
    
    private Comparison comparison;
    private boolean existe_not;
    
    public NotTest(Comparison comparison, boolean not){
        this.existe_not = not;
        this.comparison = comparison;
    }
    
    public void genC(PW pw){
        
        if(existe_not)
            pw.print(" !");
        
        comparison.genC(pw);
        
    }
}
