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
public class OrTest {
    
    private ArrayList<AndTest> andTest;
    
    public OrTest (ArrayList<AndTest> AndTest)
    {
        this.andTest = AndTest;
    }
    
    public void genC(PW pw, boolean flag){
        
        int i = 0;
        
        if(i < andTest.size())
            andTest.get(i).genC(pw, flag);
        
        i++;
        
        while(i < andTest.size())
        {
            pw.print(" || ");
            andTest.get(i).genC(pw, flag);
            i++;
        }
    }
    
}
