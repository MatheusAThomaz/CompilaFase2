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
            Body body = null; 
             
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
                    body = body();
                    
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
            
                   
            return new Program(body, programName);
        }
        
        public Body body(){
            
            Declaration declaration = null;
            ArrayList<Stmt> stmt = new ArrayList<Stmt>();
            
            if(lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN
               || lexer.token == Symbol.FLOAT || lexer.token == Symbol.STRING)
            {               
              declaration =  declaration();               
            }
            
            while (lexer.token == Symbol.IF 
                    || lexer.token == Symbol.WHILE 
                    || lexer.token == Symbol.FOR 
                    || lexer.token == Symbol.VARSTRING 
                    || lexer.token == Symbol.PRINT
                    || lexer.token == Symbol.BREAK)
             {
                 stmt.add(stmt());
             }        
            
            
            return new Body(declaration, stmt);
            
        }
        
        public Stmt compoundStmt(){
            
            Stmt stmt;
            
            switch (lexer.token) {
                case Symbol.FOR:
                    stmt = forStmt();
                    return stmt;
                case Symbol.WHILE:
                    stmt = whileStmt();
                    return stmt;
                default:
                    stmt = ifStmt();
                    return stmt;
            }            
        }
        
        public ForStmt forStmt(){
            Name name = null;
            NumberInt number1 = null;
            NumberInt number2 = null;
            ArrayList<Stmt> stmt = new ArrayList<Stmt>();
            
            if (lexer.token == Symbol.FOR){
                lexer.nextToken();
                name = name();
                
                if (lexer.token == Symbol.INRANGE){
                    lexer.nextToken();
                    if (lexer.token == Symbol.LEFTPAR){
                        lexer.nextToken();
                        if (lexer.token != Symbol.NUMBERINT){
                            error.signal("int value expected.", false);
                        }
                        number();
                        number1 = new NumberInt(lexer.getNumber());
                        
                        if (lexer.token == Symbol.COMMA){
                            lexer.nextToken();
                            if (lexer.token != Symbol.NUMBERINT){
                                error.signal("int value expected.", false);
                            }
                            number();
                            number2 = new NumberInt(lexer.getNumber());
                            
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
                                        stmt.add(stmt());
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
            
            return new ForStmt(name, number1, number2, stmt);
        }
        
        public Stmt whileStmt(){
            ArrayList<Stmt> stmt = new ArrayList<Stmt>();
            OrTest orTest = null;
            
            if (lexer.token == Symbol.WHILE){
                lexer.nextToken();
                
                orTest = orTest();
            
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
                        stmt.add(stmt());
                    }
                    
                    if (lexer.token == Symbol.RIGHTKEY) lexer.nextToken();
                    else error.signal("need to close curly brackets.", true);
                    
                }
                else error.signal("open curly brackets expected.", true);
            }
     
            return new WhileStmt(orTest, stmt);
        }
        
        public Stmt ifStmt(){
            OrTest orTest = null;
            ArrayList<Stmt> ifStmt = new ArrayList<Stmt>();
            ArrayList<Stmt> elseStmt = new ArrayList<Stmt>();
            
          
            if (lexer.token == Symbol.IF){
                lexer.nextToken();
                
                orTest = orTest();
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
                        ifStmt.add(stmt());
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
                            elseStmt.add(stmt());
                        }

                        if (lexer.token == Symbol.RIGHTKEY) lexer.nextToken();
                        else error.signal("need to close curly brackets.", true);
                    }
                    else error.signal("open curly brackets expected.", true);
                }
            }
            return new IfStmt(orTest, ifStmt, elseStmt);
        }
        
        public Stmt stmt(){
            
            Stmt stmt = null;
            
            if (lexer.token == Symbol.ELSE){
                error.signal("must be an IF associated within an ELSE.", false);
            }
            
            else if (lexer.token == Symbol.IF || lexer.token == Symbol.WHILE || lexer.token == Symbol.FOR){
                stmt = compoundStmt();
            }
            else stmt = simpleStmt();
            
            return stmt;
        }
        
        public Stmt simpleStmt(){
            
            Stmt stmt;
            
            switch (lexer.token) {
                case Symbol.PRINT:
                    stmt = printStmt();
                    return stmt;
                case Symbol.BREAK:
                    stmt = breakStmt();
                    return stmt;
                case Symbol.ELSE:
                    error.signal("must be an IF associated within an ELSE.", false);
                    return null;
                default:
                    stmt = exprStmt();
                    return stmt;
            }
        }      
        
        public Stmt exprStmt(){
            Name name = null;
            Vetor vetor = null;
            OrTest orTest = null;
            ExprList exprList = null;
            boolean flagV = false, flagExpr = false;
            
            name = name();
            
            if (lexer.token == Symbol.INRANGE){
                error.signal("'for' expected.", false);
            }
            
            if (lexer.token == Symbol.LEFTCOLCHETE){
                lexer.nextToken();
                number();
                vetor = new Vetor(name.getName(), lexer.getNumber());
                flagV = true;
                
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
                    
                    exprList = exprList();
                    flagExpr = true;
                    if (lexer.token == Symbol.RIGHTCOLCHETE) lexer.nextToken();
                    else error.signal("']' expected.", false);
                }
                else{
                    orTest = orTest();
                }
                
                if (lexer.token == Symbol.SEMICOLON){
                    lexer.nextToken();
                }
                else error.signal("';' expected.", true);
            } 
            else error.signal("'=' expected between expressions.", false);
            
            if (flagV && flagExpr) return new ExprStmt(vetor, exprList);
            else if (!flagV && flagExpr) return new ExprStmt(name, exprList);
            else if (flagV && !flagExpr) return new ExprStmt(vetor, orTest);
            else return new ExprStmt(name, orTest);
                       
        }
        
        public Stmt printStmt(){
            
            ArrayList<OrTest> orTest = new ArrayList<OrTest>();
            
            if(lexer.token == Symbol.PRINT){
                lexer.nextToken();
                
                orTest.add(orTest());
                
                while(lexer.token == Symbol.COMMA)
                {
                    lexer.nextToken();
                    orTest.add(orTest());
                }
                
                if(lexer.token == Symbol.SEMICOLON)
                {
                    lexer.nextToken();
                }
                else error.signal("';' expected.", true);              
            }
            
            return new PrintStmt(orTest);
        }
        
        public ExprList exprList(){
            
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
        
        public OrTest orTest(){
            ArrayList<AndTest> andTest = new ArrayList<AndTest>();
            
            andTest.add(andTest());
            
            while(lexer.token == Symbol.OR)
            {
                lexer.nextToken();
                andTest.add(andTest());
            }
            
            return new OrTest(andTest);
        }
        
        public AndTest andTest(){
            ArrayList<NotTest> notTest = new ArrayList<NotTest>();
            
            notTest.add(notTest());  
            
            while(lexer.token == Symbol.AND)
            {
                lexer.nextToken();
                notTest.add(notTest());
            }
            
            return new AndTest(notTest);
        }
        
        public NotTest notTest(){
            Comparison comp = null;
            boolean not = false;
            
            if(lexer.token == Symbol.NOT)
            {
                lexer.nextToken();
                not = true;
            }
            
            comp = comparison();
            
            return new NotTest(comp, not);
        }
        
        public Comparison comparison(){
            CompOp compOp = null;
            Expr expr1 = null;
            Expr expr2 = null;
            boolean flag = false;

            expr1 = expr();
       
               if(lexer.token == Symbol.LOWER || lexer.token == Symbol.EQUAL
               || lexer.token == Symbol.LOWEREQUAL || lexer.token == Symbol.DIFERENT
               || lexer.token == Symbol.UPPER || lexer.token == Symbol.UPPEREQUAL)
                {
                    compOp = compOp();
                    expr2 = expr();
                    flag = true;
                }
               
               if (flag) return new Comparison(expr1, compOp, expr2);
               else return new Comparison(expr1);
                     
        }
        
        public Expr expr(){
            
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
        
        public Declaration declaration(){
            
           ArrayList<Type> type = new ArrayList<Type>();
           ArrayList<IdList> idList = new ArrayList<IdList>();
           
           boolean flag = false; 
            
           type.add(type());
           idList.add(idList());
           
           while(lexer.token == Symbol.SEMICOLON)
           {   
               lexer.nextToken();
               if(lexer.token != Symbol.INT && lexer.token != Symbol.BOOLEAN
               && lexer.token != Symbol.FLOAT && lexer.token != Symbol.STRING)
               {    
                   flag = true;
                   break;
               }
           type.add(type());
           idList.add(idList());   
           }
                     
           if(lexer.token == Symbol.SEMICOLON ){
                lexer.nextToken();                
           }                             
           else if (!flag) error.signal("';' expected.", true);      
           
           return new Declaration(type, idList);
        }
        
        public IdList idList(){
            
            ArrayList<Variables> variables = new ArrayList<Variables>();
            Name name;
            NumberInterface number;
            Vetor vetor;
            boolean flag = false;
            
            name = name();
            
            if(lexer.token == Symbol.LEFTCOLCHETE)
            {
                
                lexer.nextToken();
                number = number();
                
                vetor = new Vetor(name.getName(), lexer.getNumber());
                
                variables.add(vetor);
                flag = true;
                
                if(lexer.token == Symbol.RIGHTCOLCHETE)
                    lexer.nextToken();
                else error.signal("']' expected.", false);
            }
            
            if(!flag)
            {
                variables.add(name);
                flag = false;
            }
            
            while(lexer.token == Symbol.COMMA)
            {
                flag = false;
                lexer.nextToken();
                
                name = name();
                if(lexer.token == Symbol.LEFTCOLCHETE)
                {
                    lexer.nextToken();
                    vetor = new Vetor(name.getName(), lexer.getNumber());
                
                    variables.add(vetor);
                    flag = true;
                    if(lexer.token == Symbol.RIGHTCOLCHETE)
                        lexer.nextToken();
                    else error.signal("']' expected.", false);
                }
                
                if(!flag)
                {
                    variables.add(name);
                    flag = false;
                }
            }
            
            return new IdList(variables);
        }
        
        public BreakStmt breakStmt(){
            if(lexer.token == Symbol.BREAK)
            {
                lexer.nextToken();
                
                if(lexer.token == Symbol.SEMICOLON)
                    lexer.nextToken();
                else error.signal("';' expected.", true);
                
            }
            return new BreakStmt();
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
        
        public CompOp compOp(){
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
        
        public Type type(){           
            if(lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN
               || lexer.token == Symbol.FLOAT || lexer.token == Symbol.STRING)
                lexer.nextToken();
            else
                error.signal("expected a valid type", false);
            
            return new Type();
            
        }
        
        public NumberInterface number(){
            
            if(lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS)
            {
                signal();        
            }
            if(lexer.token == Symbol.NUMBERFLOAT || lexer.token == Symbol.NUMBERINT)
                digit();           
        }
        
        public Name name(){
            if(lexer.token == Symbol.VARSTRING)
                lexer.nextToken();
            
            return new Name();
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
