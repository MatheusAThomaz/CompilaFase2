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
public class ForStmt {
    
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
            pw.print("++");
        else
            pw.print("--");
        
        
        
    }
}
