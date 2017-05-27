package Principal;

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
    private boolean isError;
    
    public CompilerError(PrintWriter out){
        this.out = out;
        this.isError = false;
        this.programName = "error before name was seted";
    }
    
    public void setLexer( Lexer lexer ) {
        this.lexer = lexer;
    }

    public void setProgramName(String name ) {
        this.programName = name;
    }
    
    public void signal (String message, boolean goPreviousToken){
        if (goPreviousToken){
            out.println(programName + ", " + lexer.getLineNumberBeforeLastToken() + ", " + message + "\n");
            
        }
        else{
            out.println(programName + ", " + lexer.getLineNumber() + ", " + message + "\n");
        }     
        
        out.flush();
        this.isError = true;       
        throw new RuntimeException(message);       
    }
    
    public boolean checkError(){
        return this.isError;
    }
}
