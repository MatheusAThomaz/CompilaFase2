import Lexer.*;
import AST.*;
import java.util.*;
import java.io.*;
/**
 *
 * @author matheus
 */
public class Compiler {
        
    private char []input;
    private String programName;
    private Lexer lexer;
    private CompilerError error;
    
        public Program compile(char []p_input, PrintWriter outError) {
        Program p;
        input = p_input;
        lexer = new Lexer(input);
        lexer.nextToken();
        error = new CompilerError(outError);
        error.setLexer(lexer);
        p = program();
        //Program result = program();
        if (lexer.token != Symbol.EOF)
          error.signal("something goes wrong.", true);
        return p;
        }
        
        public Program program(){
            if (lexer.token == Symbol.PROGRAM){
                lexer.nextToken();
                programName = lexer.getStringValue();
                error.setProgramName(programName);
                if (lexer.token != Symbol.VARSTRING){
                    error.signal("name of the program expected.", false);
                }
                name();            
                if (lexer.token == Symbol.COLON){
                    lexer.nextToken();
                    body();
                    
                    if (lexer.token == Symbol.END){
                        //System.out.println("É TETRAAAAAAAAA");
                        lexer.nextToken();
                    }
                    else {
                       error.signal("'end' expected.", false);
                    }
                }
                else error.signal("':' expected.", true);
            }         
            else error.signal("'program' expected.", false);
            
            Body body = null;         
            return new Program(body, programName);
        }
        
        public void body(){
            if(lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN
               || lexer.token == Symbol.FLOAT || lexer.token == Symbol.STRING)
            {               
                declaration();               
            }
            
            while (lexer.token == Symbol.IF 
                    || lexer.token == Symbol.WHILE 
                    || lexer.token == Symbol.FOR 
                    || lexer.token == Symbol.VARSTRING 
                    || lexer.token == Symbol.PRINT
                    || lexer.token == Symbol.BREAK)
             {
                 stmt();
             }            
        }
        
        public void compoundStmt(){
            
            switch (lexer.token) {
                case Symbol.FOR:
                    forStmt();
                    break;
                case Symbol.WHILE:
                    whileStmt();
                    break;
                default:
                    ifStmt();
                    break;
            }            
        }
        
        public void forStmt(){
            if (lexer.token == Symbol.FOR){
                lexer.nextToken();
                name();
                
                if (lexer.token == Symbol.INRANGE){
                    lexer.nextToken();
                    if (lexer.token == Symbol.LEFTPAR){
                        lexer.nextToken();
                        if (lexer.token != Symbol.NUMBERINT){
                            error.signal("int value expected.", false);
                        }
                        number();
                        if (lexer.token == Symbol.COMMA){
                            lexer.nextToken();
                            if (lexer.token != Symbol.NUMBERINT){
                                error.signal("int value expected.", false);
                            }
                            number();
                            
                            if (lexer.token == Symbol.RIGHTPAR){
                                lexer.nextToken();
                                if (lexer.token == Symbol.LEFTKEY){
                                    lexer.nextToken();
                                    while (lexer.token == Symbol.IF 
                                           || lexer.token == Symbol.WHILE 
                                           || lexer.token == Symbol.FOR 
                                           || lexer.token == Symbol.VARSTRING 
                                           || lexer.token == Symbol.PRINT
                                           || lexer.token == Symbol.BREAK
                                           || lexer.token == Symbol.ELSE)
                                    {
                                        stmt();
                                    }
                                    
                                    if (lexer.token == Symbol.RIGHTKEY) lexer.nextToken();
                                    else error.signal("need to close curly brackets.", true);
                                }
                                else error.signal("open curly brackets expected.", true);
                            }
                            else error.signal("need to close the parentheses.", false);
                        }
                        else error.signal("comma expected after the variable.", false);
                    }
                    else error.signal("open parentheses expected after inrange.", false);
                }
                else {
                    error.signal("'inrange' expected after the variable.", false);
                }    
                
            }     
        }
        
        public void whileStmt(){
            
            
            if (lexer.token == Symbol.WHILE){
                lexer.nextToken();
                
                orTest();
            
                if (lexer.token == Symbol.LEFTKEY){
                    lexer.nextToken();
                    while (lexer.token == Symbol.IF 
                           || lexer.token == Symbol.WHILE 
                           || lexer.token == Symbol.FOR 
                           || lexer.token == Symbol.VARSTRING 
                           || lexer.token == Symbol.PRINT
                           || lexer.token == Symbol.BREAK
                           || lexer.token == Symbol.ELSE)
                    {
                        stmt();
                    }
                    
                    if (lexer.token == Symbol.RIGHTKEY) lexer.nextToken();
                    else error.signal("need to close curly brackets.", true);
                    
                }
                else error.signal("open curly brackets expected.", true);
            }
            
            
            
            
        }
        
        public void ifStmt(){
            boolean isIf = true;          
            
            
            if (lexer.token == Symbol.IF){
                lexer.nextToken();
                
                orTest();
                if (lexer.token == Symbol.LEFTKEY){
                    lexer.nextToken();
                    
                    while (lexer.token == Symbol.IF 
                           || lexer.token == Symbol.WHILE 
                           || lexer.token == Symbol.FOR 
                           || lexer.token == Symbol.VARSTRING 
                           || lexer.token == Symbol.PRINT
                           || lexer.token == Symbol.BREAK
                           || lexer.token == Symbol.ELSE)
                    {
                        stmt();
                    }
                    
                    if (lexer.token == Symbol.RIGHTKEY)
                        lexer.nextToken();
                    else error.signal("need to close curly brackets.", true);                                      
                }                
                else error.signal("open curly brackets expected.", true);
                
                if (lexer.token == Symbol.ELSE){
                    lexer.nextToken();
                    if (lexer.token == Symbol.LEFTKEY){
                        lexer.nextToken();

                        while (lexer.token == Symbol.IF 
                               || lexer.token == Symbol.WHILE 
                               || lexer.token == Symbol.FOR 
                               || lexer.token == Symbol.VARSTRING 
                               || lexer.token == Symbol.PRINT
                               || lexer.token == Symbol.BREAK)
                        {
                            stmt();
                        }

                        if (lexer.token == Symbol.RIGHTKEY) lexer.nextToken();
                        else error.signal("need to close curly brackets.", true);
                    }
                    else error.signal("open curly brackets expected.", true);
                }
            }
            
        }
        
        public void stmt(){
            
            if (lexer.token == Symbol.ELSE){
                error.signal("must be a IF associated with a ELSE.", false);
            }
            
            else if (lexer.token == Symbol.IF || lexer.token == Symbol.WHILE || lexer.token == Symbol.FOR){
                compoundStmt();
            }
            else simpleStmt();
        }
        
        public void simpleStmt(){
            
            switch (lexer.token) {
                case Symbol.PRINT:
                    printStmt();
                    break;
                case Symbol.BREAK:
                    breakStmt();
                    break;
                case Symbol.ELSE:
                    error.signal("must be a IF associated with a ELSE.", false);
                    break;
                default:
                    exprStmt();
                    break;
            }
        }      
        
        public void exprStmt(){           
            name();
            
            if (lexer.token == Symbol.INRANGE){
                error.signal("'for' expected.", false);
            }
            if (lexer.token == Symbol.LEFTCOLCHETE){
                lexer.nextToken();
                number();
                if (lexer.token == Symbol.RIGHTCOLCHETE) lexer.nextToken();
                else error.signal("']' expected.", false);
            }
            
            if (lexer.token == Symbol.LOWER || lexer.token == Symbol.UPPER
                || lexer.token == Symbol.LOWEREQUAL || lexer.token == Symbol.UPPEREQUAL
                || lexer.token == Symbol.DIFERENT || lexer.token == Symbol.EQUAL)
            {
                error.signal ("'if' or 'while' expected.", false);
            }
            
            if (lexer.token == Symbol.ASSIGN){
                lexer.nextToken();
                
                if (lexer.token == Symbol.LEFTCOLCHETE){
                    lexer.nextToken();
                    
                    exprList();
                    
                    if (lexer.token == Symbol.RIGHTCOLCHETE) lexer.nextToken();
                    else error.signal("']' expected.", false);
                }
                else{
                    orTest();
                }
                
                if (lexer.token == Symbol.SEMICOLON){
                    lexer.nextToken();
                }
                else error.signal("';' expected.", true);
            } 
            else error.signal("'=' expected between expressions.", false);
            
            
        }
        
        public void printStmt(){
            
            if(lexer.token == Symbol.PRINT){
                lexer.nextToken();
                
                orTest();
                
                while(lexer.token == Symbol.COMMA)
                {
                    lexer.nextToken();
                    orTest();
                }
                
                if(lexer.token == Symbol.SEMICOLON)
                {
                    lexer.nextToken();
                }
                else error.signal("';' expected.", true);
                
            }
        }
        
        public void exprList(){
            
            expr();
            
            if (lexer.token != Symbol.COMMA && lexer.token != Symbol.RIGHTCOLCHETE){
                error.signal("',' expected between values.", false);
            } 
            while(lexer.token == Symbol.COMMA)
            {
                lexer.nextToken();
                expr();
                if (lexer.token != Symbol.COMMA && lexer.token != Symbol.RIGHTCOLCHETE){
                    error.signal("',' expected between values", false);
                }                
            }
        }
        
        public void orTest(){
            
            andTest();
            
            while(lexer.token == Symbol.OR)
            {
                lexer.nextToken();
                andTest();
            }
        }
        
        public void andTest(){
            
            notTest();
            
            while(lexer.token == Symbol.AND)
            {
                lexer.nextToken();
                notTest();
            }
        }
        
        public void notTest(){
            
            if(lexer.token == Symbol.NOT)
            {
                lexer.nextToken();
            }
            
            comparison();
        }
        
        public void comparison(){

            expr();
            
          
               if(lexer.token == Symbol.LOWER || lexer.token == Symbol.EQUAL
               || lexer.token == Symbol.LOWEREQUAL || lexer.token == Symbol.DIFERENT
               || lexer.token == Symbol.UPPER || lexer.token == Symbol.UPPEREQUAL)
                {
                    lexer.nextToken();
                    expr();
                }
                     
        }
        
        public void expr(){
            
            term();
            
            while(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS)
            {
                lexer.nextToken();
                term();
            }
        }
        
        public void term(){
            
            factor();
            
            while(lexer.token == Symbol.MULT || lexer.token == Symbol.DIV)
            {
                lexer.nextToken();
                factor();
            }
        }
        
        public void factor(){
            
            int tokenAnterior;
            
            if(lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS)
                lexer.nextToken();
            
            
            tokenAnterior = lexer.token;
            
            atom();

            if (lexer.token == Symbol.VARSTRING || lexer.token == Symbol.NUMBERFLOAT || lexer.token == Symbol.NUMBERINT){
                error.signal("operator expected between variables", false);
            }
            
            while(lexer.token == Symbol.CIRCUMFLEX)
            {
                lexer.nextToken();
                factor();
            }
        }
        
        public void declaration(){
           boolean flag = false; 
            
           type();
           idList();
           
           while(lexer.token == Symbol.SEMICOLON)
           {   
               lexer.nextToken();
               if(lexer.token != Symbol.INT && lexer.token != Symbol.BOOLEAN
               && lexer.token != Symbol.FLOAT && lexer.token != Symbol.STRING)
               {    
                   flag = true;
                   break;
               }
               type();              
               idList();   
           }
                     
           if(lexer.token == Symbol.SEMICOLON ){
                lexer.nextToken();                
           }                             
           else if (!flag) error.signal("';' expected.", true);             
        }
        
        public void idList(){
            
            name();
            
            if(lexer.token == Symbol.LEFTCOLCHETE)
            {
                lexer.nextToken();
                number();
                if(lexer.token == Symbol.RIGHTCOLCHETE)
                    lexer.nextToken();
                else error.signal("']' expected.", false);
            }
            
            while(lexer.token == Symbol.COMMA)
            {
                lexer.nextToken();
                
                name();
                if(lexer.token == Symbol.LEFTCOLCHETE)
                {
                    lexer.nextToken();
                    number();
                    if(lexer.token == Symbol.RIGHTCOLCHETE)
                        lexer.nextToken();
                    else error.signal("']' expected.", false);
                }
            }
        }
        
        public void breakStmt(){
            if(lexer.token == Symbol.BREAK)
            {
                lexer.nextToken();
                
                if(lexer.token == Symbol.SEMICOLON)
                    lexer.nextToken();
                else error.signal("';' expected.", true);
                
            }
            
        }
        
        public void atom(){
            
            if(lexer.token == Symbol.VARSTRING){
                name();
                if(lexer.token == Symbol.LEFTCOLCHETE)
                {
                    lexer.nextToken();
                    
                    switch (lexer.token) {
                        case Symbol.NUMBERINT:
                        case Symbol.NUMBERFLOAT:
                            number();
                            break;
                        case Symbol.VARSTRING:
                            name();
                            break;
                        default:
                            error.signal("place a valid type.", false);
                            break;
                    }
                    
                    if(lexer.token != Symbol.RIGHTCOLCHETE)
                    {
                        error.signal("']' expected.", false);
                    }
                    
                    lexer.nextToken();
                }
            }
            else if(lexer.token == Symbol.NUMBERFLOAT || lexer.token == Symbol.NUMBERINT)
            {
                number();
            }
            else if(lexer.token == Symbol.STRINGTEXT)
            {
                string();
            }
            else if(lexer.token == Symbol.TRUE)
            {
                lexer.nextToken();
            }
            else if(lexer.token == Symbol.FALSE)
            {
                lexer.nextToken();
            }
            else
                error.signal("expected a number, variable or boolean type.", false);          
            
        }
        
        public void compOp(){
            if(lexer.token == Symbol.LOWER || lexer.token == Symbol.EQUAL
               || lexer.token == Symbol.LOWEREQUAL || lexer.token == Symbol.DIFERENT
               || lexer.token == Symbol.UPPER || lexer.token == Symbol.UPPEREQUAL)
                    lexer.nextToken();
            
            else
                error.signal("expected a operator.", false);
        }
        
        public void string(){
            if(lexer.token == Symbol.STRINGTEXT)
                lexer.nextToken();              
        }
        
        public void type(){           
            if(lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN
               || lexer.token == Symbol.FLOAT || lexer.token == Symbol.STRING)
                lexer.nextToken();
            else
                error.signal("expected a valid type", false);
            
        }
        
        public void number(){
            
            if(lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS)
            {
                signal();        
            }
            if(lexer.token == Symbol.NUMBERFLOAT || lexer.token == Symbol.NUMBERINT)
                digit();           
        }
        
        public void name(){
            if(lexer.token == Symbol.VARSTRING)
                lexer.nextToken();
        }
        
        public void digit(){
            
            if(lexer.token == Symbol.NUMBERINT)
                lexer.nextToken();
            else if(lexer.token == Symbol.NUMBERFLOAT)
                lexer.nextToken();
        }
        
        public void signal(){
            
            if(lexer.token == Symbol.MINUS)
                lexer.nextToken();
            
            else if(lexer.token == Symbol.PLUS)
                lexer.nextToken();
            
            else
                error.signal("place a valid signal.", false);
        }
               
}
