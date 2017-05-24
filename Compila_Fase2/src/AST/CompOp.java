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
public class CompOp {
    
    private int type;
    
    public CompOp (int type)
    {
        this.type = type;
    }
    
    public void genC (PW pw)
    {
        switch (type) {
            case Symbol.LOWER:
                pw.print(" < ");
                break;
            case Symbol.LOWEREQUAL:
                pw.print(" <= ");
                break;
            case Symbol.UPPER:
                pw.print(" > ");
                break;
            case Symbol.UPPEREQUAL:
                pw.print(" >= ");
                break;
            case Symbol.DIFERENT:
                pw.print(" != ");
                break;
            case Symbol.EQUAL:
                pw.print(" == ");
                break;
            default:
                break;
        }
    }
}
