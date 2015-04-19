/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBRaportLatex;

import java.util.ArrayList;

/** Class container which contains all records from one query
 * 
 */
public class RecordSet {
    
    ArrayList<String[]> val;
    int m;
    
    RecordSet(int columncount){
  
        val = new ArrayList<String[]>();
        m = columncount;

    }
    
    void setVal(String str,int i, int j){
        while(val.size() <= i)
            val.add(new String[m]);
        
        val.get(i)[j]=str;
    }
    String getVal(int i, int j){return(val.get(i)[j]);}   
    
    int get_rows(){return(val.size());}
    int get_cols(){return(m);}
    
    
    
    
    void toPrint(){
        for(int i=0;i < val.size();i++){
        for(int j=0;j < m;j++){
            System.out.println(val.get(i)[j] + ", ");
        }
           System.out.println("\n"); 
        }   
    }
}
