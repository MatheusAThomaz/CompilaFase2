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
public interface NumberInterface {
    public void genC(PW pw);
    
    public float getValue();
    
    public int getInt();
    
}
