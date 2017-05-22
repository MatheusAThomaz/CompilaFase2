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
public class Program{
    
    private Body body;
    private String name;
    
    public Program(Body body, String name)
    {
        this.body = body;
        this.name = name;
    }
 
    public void genC(PW pw){
        pw.out.println("#include <stdio.h>");
        pw.println("int main(){");
        
        body.genC(pw);
        pw.println("return 0;");
        pw.println("}");
        
    }
    
    
    
}
