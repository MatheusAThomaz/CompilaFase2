/**
 *
 * @author Caio Giacomelli
 */

import Lexer.*; 
import java.io.*;

public class CompilerError {
    PrintWriter out;
    private Lexer lexer;
    private String programName;
    
    public CompilerError(PrintWriter out){
        this.out = out;
    }
    
    public void setLexer( Lexer lexer ) {
        this.lexer = lexer;
    }
    
    public void setProgramName(String name ) {
        this.programName = name;
    }
    
    public void signal (String message){
        out.println(programName + "numero da linha" + message + "\n");
    }
}
