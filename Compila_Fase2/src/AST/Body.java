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
public class Body {
    
    private Declaration declaration = null;
    private ArrayList<Stmt> statements;
    
    public Body(Declaration declaration, ArrayList<Stmt> statements)
    {
        this.declaration = declaration;
        this.statements = statements;
    }
    public void genC(PW pw){
        
        int  ssize = statements.size(), i = 0;
        
        if(declaration != null)
        declaration.genC( pw);
        
        
        i = 0;
        
        while(i < ssize)
        {
            statements.get(i).genC(pw);
            i++;
        }
        

        
        
    }
}
