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
public class ForStmt implements Stmt {
    
    private Name name;
    private NumberInt number1;
    private NumberInt number2;
    private ArrayList<Stmt> stmt;
    
    public ForStmt(Name name, NumberInt number1, NumberInt number2, ArrayList<Stmt> stmt)
    {
        this.name = name;
        this.number1 = number1;
        this.number2 = number2;
        this.stmt = stmt;
    }
    
    public void genC(PW pw)
    {
        int i = 0;
        
        pw.print("for(");
        name.genC(pw);
        pw.print(" = ");
        number1.genC(pw);
        pw.print(";");
        
        name.genC(pw);
        if(number1.getInt() < number2.getInt())
            pw.print(" < ");
        else
            pw.print(" > ");
        number2.genC(pw);
        pw.print(";");
        
        name.genC(pw);
        if(number1.getInt() < number2.getInt())
            pw.println("++){");
        else
            pw.println("--){");
        
        if(stmt != null)
            while(i < stmt.size())
            {
                stmt.get(i).genC(pw);
                i++;
            }
        
        pw.println("}");
        
        
        
    }
}
