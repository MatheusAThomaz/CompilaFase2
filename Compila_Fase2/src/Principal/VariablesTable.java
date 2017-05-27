/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;
import java.util.*;

/**
 *
 * @author matheus
 */
public class VariablesTable {
    
        public static Hashtable VariableTable = new Hashtable();
        public static ArrayList<String> string = new ArrayList<String>();
        public static boolean flag;
    
        public static void insert(String name, int symbol){
            VariableTable.put(name, symbol);
        }
        
        public static int getTable(String name){
            Object obj = VariableTable.get(name);
            
            if(obj == null)
                return -1;
            
            return ((Integer)obj).intValue();
        }
           
}
