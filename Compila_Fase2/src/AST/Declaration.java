/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;
import java.util.*;

/**
 *
 * @author matheus
 */
public class Declaration {
    
    private ArrayList<Type> type;
    private ArrayList<IdList> idList;
    
    public Declaration(ArrayList<Type> type, ArrayList<IdList> idList){
        
        this.type = type;
        this.idList = idList;
        
    }
    
    
    public void genC(PW pw){
           int i = 0;
           
           while(i < type.size())
           {
               type.get(i).genC(pw);
               idList.get(i).genC(pw);
               pw.println(";");
               i++;
           }
    }
}
