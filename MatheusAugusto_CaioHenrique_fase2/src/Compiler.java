


import Lexer.*;
import AST.*;
import java.util.*;
/**
 *
 * @author matheus
 */
public class Compiler {
        
    private char []input;
    private String programName;
    Lexer lexer;
    
        public Program compile( char []p_input  ) {
        Program p;
        input = p_input;
        lexer = new Lexer(input);
        lexer.nextToken();
        p = program();
        //Program result = program();
        if (lexer.token != '\0' )
          error();
        return p;
        }
        
        public Program program(){
            if (lexer.token == Symbol.PROGRAM){
                lexer.nextToken();
                
                name();
                programName = lexer.getStringValue();
                
                if (lexer.token == Symbol.COLON){
                    lexer.nextToken();
                    body();
                    
                    if (lexer.token == Symbol.END){
                        //System.out.println("É TETRAAAAAAAAA");
                        lexer.nextToken();
                    }
                    else {
                        error();
                    }
                }
                else error();
            }         
            else error();
            
            return new Program();
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
                        number();
                        if (lexer.token == Symbol.COMMA){
                            lexer.nextToken();
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
                                           || lexer.token == Symbol.BREAK)
                                    {
                                        stmt();
                                    }
                                    
                                    if (lexer.token == Symbol.RIGHTKEY) lexer.nextToken();
                                    else error();
                                }
                                else error();
                            }
                            else error();
                        }
                        else error();
                    }
                    else error();
                }
                else {
                    error();
                }    
                
            }
            else error();        
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
                           || lexer.token == Symbol.BREAK)
                    {
                        stmt();
                    }
                    
                    if (lexer.token == Symbol.RIGHTKEY) lexer.nextToken();
                    else error();
                    
                }
                else error();
            }
            else error();
            
            
            
            
        }
        
        public void ifStmt(){
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
                           || lexer.token == Symbol.BREAK)
                    {
                        stmt();
                    }
                    
                    if (lexer.token == Symbol.RIGHTKEY)
                        lexer.nextToken();
                    else error();                                      
                }
                
                else error();
                
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
                                else error();
                            }
                            else error();
                        }  
            }
            else error();
        }
        
        public void stmt(){
            if (lexer.token == Symbol.IF || lexer.token == Symbol.WHILE || lexer.token == Symbol.FOR){
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
                default:
                    exprStmt();
                    break;
            }
        }      
        
        public void exprStmt(){           
            name();
            
            if (lexer.token == Symbol.LEFTCOLCHETE){
                lexer.nextToken();
                number();
                if (lexer.token == Symbol.RIGHTCOLCHETE) lexer.nextToken();
                else error();
            }
            
            if (lexer.token == Symbol.ASSIGN){
                lexer.nextToken();
                
                if (lexer.token == Symbol.LEFTCOLCHETE){
                    lexer.nextToken();
                    
                    exprList();
                    
                    if (lexer.token == Symbol.RIGHTCOLCHETE) lexer.nextToken();
                    else error();
                }
                else{
                    orTest();
                }
                
                if (lexer.token == Symbol.SEMICOLON){
                    lexer.nextToken();
                }
                else error();
            } 
            else error();
            
            
        }
        
        public void printStmt(){
            
            if(lexer.token == Symbol.PRINT)
            {
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
                else
                    error();
                
            }
            else
                error();
        }
        
        public void exprList(){
            
            expr();
            
            while(lexer.token == Symbol.COMMA)
            {
                lexer.nextToken();
                expr();
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
            
            if(lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS)
                lexer.nextToken();
            
            atom();
            
            while(lexer.token == Symbol.CIRCUMFLEX)
            {
                lexer.nextToken();
                factor();
            }
        }
        
        public void declaration(){
            
           type();
           idList();
           
           while(lexer.token == Symbol.SEMICOLON)
           {   
               lexer.nextToken();
               if(lexer.token != Symbol.INT && lexer.token != Symbol.BOOLEAN
               && lexer.token != Symbol.FLOAT && lexer.token != Symbol.STRING)
               {                  
                   break;
               }
               type();              
               idList();
               
           }
                     
           if(lexer.token == Symbol.SEMICOLON ){
                lexer.nextToken();
                
           }                             
           else{
            error();
           }
               
        }
        
        public void idList(){
            
            name();
            
            if(lexer.token == Symbol.LEFTCOLCHETE)
            {
                lexer.nextToken();
                number();
                if(lexer.token == Symbol.RIGHTCOLCHETE)
                    lexer.nextToken();
                else
                    error();
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
                    else
                        error();
                }
            }
        }
        
        public void breakStmt(){
            if(lexer.token == Symbol.BREAK)
            {
                lexer.nextToken();
                
                if(lexer.token == Symbol.SEMICOLON)
                    lexer.nextToken();
                else
                    error();
                
            }
            else 
                error();
            
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
                            error();
                            break;
                    }
                    
                    if(lexer.token != Symbol.RIGHTCOLCHETE)
                    {
                        error();
                        
                    }
                    
                    lexer.nextToken();
                }
                else
                   error();
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
                error();          
            
        }
        
        public void compOp(){
            if(lexer.token == Symbol.LOWER || lexer.token == Symbol.EQUAL
               || lexer.token == Symbol.LOWEREQUAL || lexer.token == Symbol.DIFERENT
               || lexer.token == Symbol.UPPER || lexer.token == Symbol.UPPEREQUAL)
                    lexer.nextToken();
            
            else
                error();
        }
        
        public void string(){
            if(lexer.token == Symbol.STRINGTEXT)
                lexer.nextToken();
            else
                error();
        }
        
        public void type(){           
            if(lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN
               || lexer.token == Symbol.FLOAT || lexer.token == Symbol.STRING)
                lexer.nextToken();
            else
                error();
            
        }
        
        public void number(){
            
            if(lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS)
            {
                signal();        
            }
            if(lexer.token == Symbol.NUMBERFLOAT || lexer.token == Symbol.NUMBERINT)
                digit();
            else
                error();
            
        }
        
        public void name(){
            if(lexer.token == Symbol.VARSTRING)
                lexer.nextToken();
            else
                error();
        }
        
        public void digit(){
            
            if(lexer.token == Symbol.NUMBERINT)
                lexer.nextToken();
            else if(lexer.token == Symbol.NUMBERFLOAT)
                lexer.nextToken();
            else
                error();
        }
        
        public void signal(){
            
            if(lexer.token == Symbol.MINUS)
                lexer.nextToken();
            
            else if(lexer.token == Symbol.PLUS)
                lexer.nextToken();
            
            else
                error();
        }
                
        
        
        public void error(){
            
        }
                
}
