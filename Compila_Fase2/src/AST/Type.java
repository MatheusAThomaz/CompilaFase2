/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import Lexer.Symbol;

/**
 *
 * @author maiks
 */
public class Type {
    int symb;
    
    public Type(int type){
        this.symb = type;
    }
    
    public void genC(PW pw){
        
        switch (this.symb){
            case Symbol.INT:
                pw.print("int ");
            
        }
        
        
    }
    
}
