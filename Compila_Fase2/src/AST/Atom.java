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
public class Atom {
    
    private int type;
    private NumberFloat floatValue;
    private NumberInt intValue;
    private StringVar string;
    private Name name;
    private Vetor vetor;
    
    public Atom(int type, float value){
        this.type = type;
        this.floatValue = new NumberFloat(value);
    }
    
    public Atom(int type){
        this.type = type;
    }
    
    public Atom(int type, String name){
        this.type = type;
        if(type == Symbol.VARSTRING)
            this.name = new Name(name);
        else
            this.string = new StringVar(name);
    }
    
    public Atom(int type, String name, String indexS){
        this.type = type;
        this.vetor = new Vetor(name,indexS);
    }
    
    public Atom(int type, String name, int indexI){
        this.type = type;
        this.vetor = new Vetor(name, indexI);
    }
    
    
    public void genC(PW pw){
        switch (type) {
            case Symbol.VETOR:
                vetor.genC(pw);
                break;
            case Symbol.NUMBERINT:
                intValue.genC(pw);
                break;
            case Symbol.NUMBERFLOAT:
                floatValue.genC(pw);
                break;
            case Symbol.VARSTRING:
                name.genC(pw);
                break;
            case Symbol.STRINGTEXT:
                string.genC(pw);
                break;
            case Symbol.TRUE:
                pw.print("true");
                break;
            case Symbol.FALSE:
                pw.print("false");
                break;
            default:
                break;
        }
    }
    
    
}
