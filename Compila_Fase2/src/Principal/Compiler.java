package Principal;

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
    public Hashtable VariableTable;
    
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
            boolean flag = false;
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
                    flag = true;
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
            if(flag)
                return new IfStmt(orTest, ifStmt, elseStmt);
            else
                return new IfStmt(orTest, ifStmt);
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
            boolean flagV = false, flagExpr = false, v = false;
            
            name = name();
            char ch = 34;
            int c = VariablesTable.getTable(name.getName()) ;
            if(c == -1)
                error.signal("variable "+ch+ name.getName() +ch+ " must be declared", false);
            if(c == Symbol.STRING || c == Symbol.VETORFLOAT || c == Symbol.VETORINT || c == Symbol.VETORCHAR)
                v = true;
            
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
            
            if(v && flagExpr) return new ExprStmt(name, exprList, v);
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
            
            ArrayList<Expr> expr = new ArrayList<Expr>();
            
            expr.add(expr());
            
            if (lexer.token != Symbol.COMMA && lexer.token != Symbol.RIGHTCOLCHETE){
                error.signal("',' expected between values.", false);
            } 
            while(lexer.token == Symbol.COMMA)
            {
                lexer.nextToken();
                expr.add(expr());
                if (lexer.token != Symbol.COMMA && lexer.token != Symbol.RIGHTCOLCHETE){
                    error.signal("',' expected between values", false);
                }                
            }
            
            return new ExprList(expr);
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
            ArrayList<Term> term = new ArrayList<Term>();
            char []signal = new char[100000];
            int i = 0;
            
            term.add(term());
            
            while(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS)
            {
                if (lexer.token == Symbol.PLUS) signal[i] = '+';
                else signal[i] = '-';
                lexer.nextToken();
                term.add(term());
                i++;
            }
            
            return new Expr(signal, term);
        }
        
        public Term term(){
            ArrayList<Factor> fact = new ArrayList<Factor>();
            char []signal = new char[100000];
            int i = 0;
                       
            fact.add(factor());
            
            while(lexer.token == Symbol.MULT || lexer.token == Symbol.DIV)
            {
                if (lexer.token == Symbol.MULT) signal[i] = '*';
                else signal[i] = '/';
                lexer.nextToken();
                fact.add(factor());
                i++;
            }
            
            return new Term(signal, fact);
        }
        
        public Factor factor(){
            ArrayList<Factor> fact = new ArrayList<Factor>();
            Atom atom = null;
            char signal = ' ';
            int i = 0;           
            int tokenAnterior;
            
            if(lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS){
                if (lexer.token == Symbol.PLUS) signal = '+';
                else signal = '-';
                lexer.nextToken();
            }
                
                       
           //tokenAnterior = lexer.token;
            
            atom = atom();
            
            if (lexer.token == Symbol.VARSTRING || lexer.token == Symbol.NUMBERFLOAT || lexer.token == Symbol.NUMBERINT){
                error.signal("operator expected between variables", false);
            }
            
            while(lexer.token == Symbol.CIRCUMFLEX)
            {
                lexer.nextToken();
                fact.add(factor());
            }
            
            return new Factor(signal, atom, fact);
        }
        
        public Declaration declaration(){
           int tipo; 
           ArrayList<Type> type = new ArrayList<Type>();
           ArrayList<IdList> idList = new ArrayList<IdList>();
           
           tipo = lexer.token;           
           boolean flag = false; 
            
           type.add(type());
           idList.add(idList(tipo));
           
           while(lexer.token == Symbol.SEMICOLON)
           {   
               lexer.nextToken();
               
               if(lexer.token != Symbol.INT && lexer.token != Symbol.BOOLEAN
               && lexer.token != Symbol.FLOAT && lexer.token != Symbol.STRING)
               {    
                   
                   flag = true;
                   break;
               }
               
               tipo = lexer.token;
               type.add(type());
               idList.add(idList(tipo));   
           }
                     
           if(lexer.token == Symbol.SEMICOLON ){
                lexer.nextToken();                
           }                             
           else if (!flag) error.signal("';' expected.", true);      
           
           return new Declaration(type, idList);
        }
        
        public IdList idList(int tipo){
            int tipovet = 0;
            ArrayList<Variables> variables = new ArrayList<Variables>();
            Name name;
            NumberInterface number;
            Vetor vetor;
            boolean flag = false;
            
            name = name();
            
            if(lexer.token == Symbol.LEFTCOLCHETE)
            {
                if(tipo == Symbol.INT)
                    tipovet = Symbol.VETORINT;
                else if(tipo == Symbol.FLOAT)
                    tipovet = Symbol.VETORFLOAT;
                else if(tipo == Symbol.STRING)
                    tipovet = Symbol.VETORCHAR;
                
                lexer.nextToken();
                number = number();
                
                vetor = new Vetor(name.getName(), lexer.getNumber());
                
                variables.add(vetor);
                flag = true;
                
                if(lexer.token == Symbol.RIGHTCOLCHETE)
                {
                    lexer.nextToken();
                    VariablesTable.insert(name.getName(),tipovet);
                }
                else error.signal("']' expected.", false);
                
                
            }
            
            if(!flag)
            {
                variables.add(name);
                flag = false;              
                VariablesTable.insert(name.getName(), tipo);
            }
            
            while(lexer.token == Symbol.COMMA)
            {
                flag = false;
                lexer.nextToken();
                
                name = name();
                if(lexer.token == Symbol.LEFTCOLCHETE)
                {
                    
                    if(tipo == Symbol.INT)
                        tipovet = Symbol.VETORINT;
                    else if(tipo == Symbol.FLOAT)
                        tipovet = Symbol.VETORFLOAT;
                    else if(tipo == Symbol.STRING)
                        tipovet = Symbol.VETORCHAR;
                    
                    lexer.nextToken();
                    vetor = new Vetor(name.getName(), lexer.getNumber());
                    number();
                    variables.add(vetor);
                    flag = true;
                    
                    if(lexer.token == Symbol.RIGHTCOLCHETE)
                    {
                        lexer.nextToken();
                        VariablesTable.insert(name.getName(), tipovet);
                    }
                    else error.signal("']' expected.", false);
                }
                
                if(!flag)
                {
                    variables.add(name);
                    flag = false;
                    VariablesTable.insert(name.getName(), tipo);
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
        
        public Atom atom(){
            Name name = null;
            Name indexS = null;
            NumberInterface number;
            String text = null;
            int symb = 0;
           
            
            if(lexer.token == Symbol.VARSTRING){
                name = name();
                
                if(lexer.token == Symbol.LEFTCOLCHETE)
                {
                    lexer.nextToken();
                    
                    switch (lexer.token) {
                        case Symbol.NUMBERINT:
                            number = number();
                            
                            
                            if(lexer.token != Symbol.RIGHTCOLCHETE)
                            {
                                error.signal("']' expected.", false);
                            }
                            
                            lexer.nextToken();
                            
                            return new Atom(Symbol.VETOR, name.getName(), number.getInt());
                        case Symbol.NUMBERFLOAT:
                            //TRATAR ERRO SEMANTICO
                            break;
                        case Symbol.VARSTRING:
                            indexS = name();
                            
                            if(lexer.token != Symbol.RIGHTCOLCHETE)
                            {
                                error.signal("']' expected.", false);
                            }
                            
                            lexer.nextToken();
                            
                            return new Atom(Symbol.VETOR, name.getName(), indexS.getName());
                        default:
                            error.signal("place a valid type.", false);
                            break;
                    }                   
                }
                
                return new Atom(Symbol.VARSTRING, name.getName());
            }
            
            else if(lexer.token == Symbol.NUMBERFLOAT || lexer.token == Symbol.NUMBERINT)
            {
                symb = lexer.token;
                number = number();
                
                if (symb == Symbol.NUMBERINT) return new Atom(symb, number.getInt());
                else return new Atom(symb, number.getValue());
                
            }
            else if(lexer.token == Symbol.STRINGTEXT)
            {
                text = string();
                
                return new Atom(Symbol.STRINGTEXT, text);
            }
            
            else if(lexer.token == Symbol.TRUE)
            {
                lexer.nextToken();
                
                return new Atom(Symbol.TRUE);
            }
            else if(lexer.token == Symbol.FALSE)
            {
                lexer.nextToken();
                
                return new Atom(Symbol.FALSE);
            }
            else
                error.signal("expected a number, variable or boolean type.", false);
            
            return null;          
        }
        
        public CompOp compOp(){
            int symb = 0;
            if(lexer.token == Symbol.LOWER || lexer.token == Symbol.EQUAL
               || lexer.token == Symbol.LOWEREQUAL || lexer.token == Symbol.DIFERENT
               || lexer.token == Symbol.UPPER || lexer.token == Symbol.UPPEREQUAL)
            {
                symb = lexer.token;
                lexer.nextToken();
            }
          
            else
                error.signal("expected a operator.", false);
        
            return new CompOp(symb);
        }
        
        public String string(){
            String str = null;
            if(lexer.token == Symbol.STRINGTEXT){
                str = lexer.getStringValue();
                lexer.nextToken();  
            }
            
            return str;
        }
        
        public Type type(){   
            Type type = null;
            if(lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN
               || lexer.token == Symbol.FLOAT || lexer.token == Symbol.STRING)
            {
                type = new Type(lexer.token);
                lexer.nextToken();        
            }
                
            else
                error.signal("expected a valid type", false);
            
            return type;           
        }
        
        public NumberInterface number(){
            int number = 1;
            
            if(lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS)
            {
                if(lexer.token == Symbol.MINUS)
                    number = -1;
                else
                    number = 1;
                
                signal();  
                
                
            }
            if(lexer.token == Symbol.NUMBERFLOAT || lexer.token == Symbol.NUMBERINT)      
            {
                if(lexer.token == Symbol.NUMBERINT)
                {
                    digit();
                    return new NumberInt(lexer.getNumber()*number);
                }
                    
                else
                {
                    digit();
                    return new NumberFloat(lexer.getNumberFloat()*number);
                }
            }  
            
            return null;
        }
        
        public Name name(){
            String name = null;
            if(lexer.token == Symbol.VARSTRING)
            {
                name = lexer.getStringValue();
                lexer.nextToken();    
            }
            
            
            return new Name(name);
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
