/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBRaportLatex;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/** Main class of DBRaportLatex Package. 
 *  Main program using all other classes to achieve program goals.
 *  
 * @author Pawel Mysinski
 */
public class DBRaportLatex {

    /**Main method. Start of the program.
     * Main can be divided into stages. If any of stage fails program should be terminated.
     * 1. Load configuration file
     * 2. Using loaded configuration variables to establish database connection
     * 3. Load templates of latex documents
     * 4. Parse SQL queries From templates
     * 5. Execute those queries and fill in templates with records from it.
     * 6. Save all filled templates in new files.
     * 7. Compile chosen files with latex compiler.
     * 
     * After all 7 stages we can enjoy our generated documents or raports.
     * 
     * @param args the command line arguments
     * @throws java.io.IOException something went wrong
     * @throws java.sql.SQLException something went wrong
     */
    
    public static void main(String[] args) throws IOException, SQLException {

        
        
        
      
        final String WORKDIR = System.getProperty("user.dir");


        //LOAD CONFIG 
        Config cfg;
        
        if(args.length == 0)
            cfg = new Config("dblatexraportconfig.bat");
        else{
            System.out.print(args[0] + "\n");
            cfg = new Config(args[0]);
             }
        if(cfg.isok()!= 0){
             System.out.println(cfg.isok());
            System.exit(1);
        }
        
        
        
        //CONNECTION TO DATABASE
        System.out.print(cfg.toString()+ "\n");
        DBHandle FBH = new DBHandle(cfg.getString("dbengine"),cfg.getString("hostname"),
        cfg.getString("port"), cfg.getString("dbpath"),
        cfg.getString("dbencoding"), cfg.getString("user"),
        cfg.getString("password"));
        
        //init latexcomiler manager
        LatexCompiler comp = new LatexCompiler(cfg.getString("pdflatexpath"),cfg.getString("output"));
        
        
        
         //init templates main class which magange all templates
        Templates temps = new Templates(cfg.getString("templatepath"),cfg.getString("output"), cfg.getString("encodingtex"));
        if(temps.isok()!= 0)
            System.exit(2);
       
        

        
        if(FBH.isok()!= 0)
           System.exit(3);
        
        System.out.println("\n");
        
         //Loading all templates
        temps.loadAllTemplates();
        
        
        
        
        //preparing for main loop
        String pattern;
        pattern = cfg.getString("pattern");
        ArrayList<ParsedSQLInfo>  SQLSt;
        //parsing all statements in all files
        temps.parseSqlStatements(pattern);
     
       //Main loop for filling in templates 
       for(int i=0; i < temps.getLenght(); i++){
        System.out.print("\n");
        SQLSt = temps.getStmts(i);
      
        if(SQLSt != null){
            System.out.println(temps.getFileName(i) + " - processing SQL statements");
            for(int j=0; j < SQLSt.size(); j++){

                
              if(SQLSt.get(j).getName() == null){
                 System.out.println("no returns SQL" + (j+1)); 
                 
                FBH.executeSQL2(SQLSt.get(j).getQuery());
                
              }
              else
                  //executing queries and saving data to object templates
                temps.prepareValues(i,j,SQLSt.get(j).getName(),SQLSt.get(j).getGroup(),FBH.executeSQL2(SQLSt.get(j).getQuery()));
            }
        }
        
       temps.InjectValues(i);//injecting data to templates
        SQLSt = null;
       }
       
        System.out.println("\n");
       temps.saveAllTemplates();
       
      if(cfg.getString("pdfcompilemainfile").equals("ALL"))
       for(int i=0; i < temps.getLenght(); i++){
          comp.compileTemplate(temps.getSavedPath(i));
       }
      if(cfg.getString("pdfcompilemainfile").equals("ONLYBEGDOC"))
       for(int i=0; i < temps.getLenght(); i++){
           if(temps.compileCheck(i)==1)
            comp.compileTemplate(temps.getSavedPath(i));
       }
      
      else if(cfg.getString("pdfcompilemainfile").equals("NONE"));
      else{
          
         String[] tmpfiles = cfg.getString("pdfcompilemainfile").split(",");
          
         for(int i=0;i < tmpfiles.length; i++)
          comp.compileTemplate(temps.getOutputPath() + tmpfiles[i].trim());
  
      }
      
      //WORK DONE OR IS IT? ERRRORS count also.
        System.out.print("\n\n\nWORK DONE"); 
        if(temps.getErrorCount() > 0 || FBH.getErrorCount()> 0){
            System.out.println(" WITH ERRORS!!!");
            if(temps.getErrorCount() > 0)
                System.out.println("\nErrors in templates: " + temps.getErrorCount()+"\n");
            if(FBH.getErrorCount() > 0)
                 System.out.println("\nErrors in executing sql: " + FBH.getErrorCount()+"\n");   
        }
        else
            System.out.println("!!!\n"); 

    
        System.out.println("Directory pdf results output: " + cfg.getString("output"));   
      
      
      
    }

    
    
}