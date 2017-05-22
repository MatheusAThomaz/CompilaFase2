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
public class AndTest {
    
    private ArrayList<NotTest> notTest;
    
    public AndTest(ArrayList<NotTest> notTest)
    {
        this.notTest = notTest;
    }
    
    public void genC(PW pw){
        
        int i = 0;
        
        if(i < notTest.size())
            notTest.get(i).genC(pw);
        
        i++;
        
        while(i < notTest.size())
        {
            pw.print(" && ");
            notTest.get(i).genC(pw);
            i++;
        }
    }
    
}
