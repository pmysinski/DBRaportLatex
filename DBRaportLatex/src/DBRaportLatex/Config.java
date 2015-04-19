/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBRaportLatex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Simple class to read file as config with vars, which are used later in your program.
 * It has single constructor and simple methods to get you vars as strings.
 * All variables are stored as strings.
 */
public  class Config {
      int ok;
      String path;
      
      String[] strindex;
      String[] value; 
      

/** Only one constructor in class Its opening file named as param pathcfg 
 * If config is not found, example of config will be generated
 * File analysis starts from line with string #dbLatexRaportConfig
 * Vars are read as nameofvar=value
 * Lines with start with char # are not read. 
 * @param pathcfg path to the config with full name
 
 */
    public Config(String pathcfg){
        path = pathcfg;
        File cfg = new File(path);
        ok=1;
        if(!cfg.exists()){
            System.out.print("Configuration file does not exist!!!\nCreating template of config.cfg\n");
            createConfig();
            ok=2; 
        }
        else{
        try {
		BufferedReader in = new BufferedReader(
		   new InputStreamReader(
                      new FileInputStream(cfg), "UTF-8"));
 

                String tmp;
                String[] strindextmp = new String[50];
                String[] valuetmp = new String[50];
                int count=0;
                int flag = 0;
		while ((tmp = in.readLine()) != null) {
                      if(flag == 0 && tmp.equals("#dbLatexRaportConfig"))
                          flag = 1;
                  //  System.out.print(tmp.length() + ": " + tmp + "index#:" + tmp.indexOf('#') + "\n");
                    
                    if(flag == 1 && tmp.indexOf('#') != 0 && tmp.length() != 0 && tmp.indexOf('=') != -1){

                    
                    strindextmp[count] = tmp.substring(0,tmp.indexOf('='));
                    valuetmp[count] = tmp.substring(tmp.indexOf('=')+1,tmp.length());
                    
                    count++;
                    }
                    
		}
                in.close();
        
                strindex = new String[count];
                value = new String[count];
                  
                for(int i=0;i < count; i++){
                    strindex[i] = strindextmp[i];
                    value[i] = valuetmp[i];

                }
                
                
                ok=0;
                
      }
      	    catch (UnsupportedEncodingException e) 
	    {
			System.out.println(e.getMessage());
                        ok=1; 
	    } 
	    catch (IOException e) 
	    {
			System.out.println(e.getMessage());
                        ok=1; 
	    }
	    catch (Exception e)
	    {
			System.out.println(e.getMessage());
                        ok=1; 
	    }
     }
        if(ok==0)
            System.out.print("Config loaded: " + path + "\n");
        else if(ok==1)
            System.out.print("CONFIG LOADING ERROR " + path + "\n");
    }
        
     /** Check if object is constructed without problems.
      * 0 - no errors
      * 1 - critical error
      * @return 0 - no errors
      */   
      public  int isok(){return(ok);}
        
        
        
/** Method that creates example of config named as the file which you was trying load. 
*
*/
    public void createConfig(){
        
      try {
          String out = "\n#dbLatexRaportConfig#WYGENEROWANY CONFIG\n";
          out +=" #WYGENEROWANY CONFIG\n";
          out += "#TO JEST PRZYKLADOWY KOMENTARZ NIE BRANY POD UWAGE\n";
          out += "#PONIZEJ SA WSZYSTKIE WARTOSCI KTORE NALEZY ZMIENIC:\n";
          out += "#szablony:\n\n";
          out += "templatepath=szablony/\n";
          out += "output=wyniki/\n";
          out += "encodingtex=UTF-8\n\n\n";
          out += "#OBSLUGIWANE dbengine: firebirdsql, mysql,sqlite\n";     
          out += "#dbpath oznacza rowniez dbname w przypadku mysql\n";   
          out += "#BAZA DANYCH POLACZENIE:\n\n";
          out += "dbengine=firebirdsql\n";
          out += "hostname=//localhost\n";
          out += "port=3050\n";    
          out += "dbpath=D:/test.fdb\n";       
          out += "user=SYSDBA\n";
          out += "password=masterkey\n";       
          out += "dbencoding=WIN1250\n\n\n";        
          out += "#TEX KOMPILATOR:\n\n";
          out += "pdflatexpath=texlive/2014/bin/win32/lualatex.exe\n";       
          out += "#pdfcompilemainfile nazwy plikow do kompilacji po przecinkach\n";   
          out += "#lub ALL lub NONE lub ONLYBEGDOC gdzie skompilowane zostana tylko";
          out += " dokumenty zawierajace begindocument i enddocument\n";   
          out += "pdfcompilemainfile=main.txt\n";  
          
          
          String oututf = new String (out.getBytes("ASCII"),"UTF-8");
                  
          try (PrintWriter output = new PrintWriter(path,"UTF-8")) {
              output.print(oututf);
          }
      } catch (FileNotFoundException | UnsupportedEncodingException ex) {
          Logger.getLogger(Templates.class.getName()).log(Level.SEVERE, null, ex);
      }
        
        
        
    }
    
    /** Simple method to get read variables that you read from file.
     *  You have to type your variable name as param and you will get as result
     *  value of your var as string. 
     *  If variable does not exists, empty string will be returned.
     * @param name name of variable
     * @return value of variable
     */
    public  String getString(String name){
        
        for(int i=0;i < value.length; i++){
        
            if(strindex[i].indexOf(name) == 0)
                return(value[i]);
        }
        System.out.print("CONFIG VARIABLE ERROR: " + name + "\n");
        return("");
    }
    
 
    
    
    
    /** Overrided method which returns all vars and their values as string in 
     * multiple lines
     * @return string "nameofvar=value" to print
     */
    @Override
    public String toString(){
        String ret="";
        for(int i=0;i < value.length; i++){
            
            if(strindex[i].indexOf("password")== -1)
                ret = ret + strindex[i] + "=" + value[i] + "\n";
            else
                ret = ret + strindex[i] + "=*********\n";

        }
            
       return(ret); 
    }
    
    
    
    
}
