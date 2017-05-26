/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import Lexer.Symbol;

/**
 *
 * @author matheus
 */
public class Vetor implements Variables {
    
    private String name;
    private int indexI;
    private int type;
    private String indexS;
    
    
    public Vetor(String name, String indexS)
    {
        this.name = name;
        this.indexS = indexS;
        this.type = Symbol.STRING;
    }
    
    public Vetor(String name, int indexI)
    {
        this.name = name;
        this.indexI = indexI;
        this.type = Symbol.INT;
    }
    
    public void genC(PW pw){
        
        if(type == Symbol.STRING)
            pw.print(name + "[" + indexS + "]");
        else
            pw.print(name + "[" + indexI + "]");
    }
}
