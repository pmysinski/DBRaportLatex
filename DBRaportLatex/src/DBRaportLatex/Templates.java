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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/** Class for  responsible for loading templates, preparing data that will be
 * compiled with templates and saving filled in templates as new files
 * Usage of this class:
 * 1. Create object with param constructor, it will prepare all vars that are needed.
 * 2. Load templates
 * 3. Parse loaded templates for sql queries.
 * 4. Execute those queries and prepare all data to inject.
 * 5. Inject data after all queries to file was done and prepared.
 * 6. Save filled in templates as new files.
 */
public class Templates {
    
  String path; 
  String pathoutput;
  String encoding;
  File[] listOfFiles;
  
  ArrayList<ArrayList<ParsedSQLInfo>> sqlinfo;
  
  int ok;
  String[] data;
  int[] compileable;

  int total_errors;
    /**Only one constructor to prepare all things that are needed.
     *
     * @param pathin path to tex templates files.
     * @param pathout path to save filled in templates
     * @param enc encoding of tex files.
     */
    public Templates(String pathin, String pathout, String enc){
    total_errors = 0;
    try{  
    path = pathin;
    pathoutput = pathout;
    encoding = enc;
    int n;
    if(path.indexOf("\\",path.length()-1) < 0 && path.indexOf("/",path.length()-1) < 0){
        path = path + "\\";
    }
    if(pathoutput.indexOf("\\",pathoutput.length()-1) < 0 && pathoutput.indexOf("/",pathoutput.length()-1) < 0){
        pathoutput = pathoutput + "\\";
    }
    File tmp = new File(pathoutput);
    if (!tmp.exists()) {
        tmp.mkdirs();
}
    
    
    int test,k=0;

    File folder = new File(path);
    File[] listOfFilestmp = folder.listFiles(); 
    n = listOfFilestmp.length;
    String[] files = new String[n];
    for (int i = 0; i < n; i++) 
    {
     if (listOfFilestmp[i].isFile()) 
     {
     test = listOfFilestmp[i].getName().indexOf(".tex");
        if(test != -1 && listOfFilestmp[i].getName().length()-".tex".length()-test == 0){
            files[k] = listOfFilestmp[i].getName();
            k++;
           }
        }
    }
    listOfFiles = new File[k];
    sqlinfo = new ArrayList<ArrayList<ParsedSQLInfo>>();
    for(int i=0;i < listOfFiles.length; i++)
        sqlinfo.add(new ArrayList<ParsedSQLInfo>());
    compileable = new int[k];
    data = new String[k];
    for (int i = 0; i < listOfFiles.length; i++){
       listOfFiles[i] = new File(path + files[i]);
      //  System.out.print(listOfFiles[i].getName()+ "\n");
    }
    ok = 0;
    }
    catch (Exception e)
	    {
			System.out.println(e.getMessage());
                        ok = 1;
                        
	    }
    
    
  }
     /** Check if object is constructed without problems.
      * 0 - no errors
      * 1 - critical error
      * @return 0 - no errors
      */   
  int isok(){return(ok);}
  
    /**Method which load k template in alfabetical order from folder you set earlier. . 
     *
     * @param k number of template file in alfabetical order
     */
    public void LoadTemplate(int k){
      
      try {
		BufferedReader in = new BufferedReader(
		   new InputStreamReader(
                      new FileInputStream(listOfFiles[k]), encoding));
 
		String str="";
                String tmp;
		while ((tmp = in.readLine()) != null) {
		    str = str + tmp + "\n";
		}
                in.close();
                in = null;
                data[k]=str;
                System.out.println( "Loaded file: " +  listOfFiles[k].getPath());
                
                
                String start = "\\begin{document}";
                String end = "\\end{document}";
                int from = data[k].indexOf(start);
                int to = data[k].indexOf(end);
                
                if(from == -1 || to == -1)
                    compileable[k] = 0;
                else
                    compileable[k] = 1;
                
                
                
      }
      	    catch (UnsupportedEncodingException e) 
	    {
			System.out.println(e.getMessage());
	    } 
	    catch (IOException e) 
	    {
			System.out.println(e.getMessage());
	    }
	    catch (Exception e)
	    {
			System.out.println(e.getMessage());
	    }
  }

    /**Method which save k template in alfabetical order in folder you set earlier. 
     *
     * @param k number of template file in alfabetical order
     */
    public void saveTemplate(int k){
      
      try {
          PrintWriter output = new PrintWriter(pathoutput + listOfFiles[k].getName(),encoding);
          //System.out.println(data[k]);
          

          
          
          output.print(data[k]);
          output.close();
          output = null;
          System.out.println( "Saved to file: "  + pathoutput +  listOfFiles[k].getName() );
      } catch (Exception ex) {
          Logger.getLogger(Templates.class.getName()).log(Level.SEVERE, null, ex);
      }
       
  }
    
    /**Method to save all templates.
     *
     */
    public void saveAllTemplates(){
      
      for (int i = 0; i < listOfFiles.length; i++){
          this.saveTemplate(i);
      }
       
  }
  
    /**Method to load all templates from folder you set earlier.
     *
     */
    public void loadAllTemplates(){
      
      for (int i = 0; i < listOfFiles.length; i++){
          this.LoadTemplate(i);
      }
  }
  
    /**Method to parse k file in alfabetical order and get all sql statements also
     * their grouping and enviroment name by using regex pattern. 
     *
     * @param k  number of template file in alfabetical order
     * @param pat regex pattern to parse 3 groups of statement.
     * @return table of sql queries
     */
  public String[][] getSqlStatements(int k,String pat)
  {
    String[][] arrayOfString1 = new String[200][4];
    int i = 0;
    if(pat.equals(""))
        pat = "@@(?:([a-zA-Z]+)(?:@([0-9, ]+))?)?@@(.+?)@END@(.*?\\n|.*)";
    
    Pattern localPattern = Pattern.compile(pat, Pattern.DOTALL | Pattern.MULTILINE);
    Matcher localMatcher = localPattern.matcher(this.data[k]);
    while (localMatcher.find())
    {
      arrayOfString1[i][0] = localMatcher.group(2);
      arrayOfString1[i][1] = localMatcher.group(3);
      arrayOfString1[i][2] = localMatcher.group(1);
      arrayOfString1[i][3] = localMatcher.group(0);
      i++;
     // sqlinfo.get(k).add(new ParsedSQLInfo(localMatcher.group(3),localMatcher.group(1), localMatcher.group(2), localMatcher.end()));
      
    }
    String[][] arrayOfString2 = new String[i][4];
    for (int j = 0; j < i; j++)
    {
      arrayOfString2[j][0] = arrayOfString1[j][0];
      arrayOfString2[j][1] = arrayOfString1[j][1];
      arrayOfString2[j][2] = arrayOfString1[j][2];
      arrayOfString2[j][3] = arrayOfString1[j][3];
    }
    return arrayOfString2;
  }
  
  
  
    /**Method to parse k file in alfabetical order and save in object all sql statements also
     * their grouping and enviroment name by using regex pattern. 
     * @param pat regex pattern to parse 3 groups of statement.
     */
  public void parseSqlStatements(String pat)
  {
      
    if(pat.equals(""))
        pat = "@@(?:([a-zA-Z]+)(?:@([0-9, ]+))?)?@@(.+?)@END@(.*?\\n|.*)";
    
    Pattern localPattern = Pattern.compile(pat, Pattern.DOTALL | Pattern.MULTILINE);
    Matcher localMatcher;
    for(int k=0;k < this.getLenght();k++){

    localMatcher = localPattern.matcher(this.data[k]);
    while (localMatcher.find())
        {
          sqlinfo.get(k).add(new ParsedSQLInfo(localMatcher.group(3),localMatcher.group(1), localMatcher.group(2), localMatcher.end()));
        }
    }
    
    
    
  }
  
   
    /** Method which generate data from records from query, after executing this
     * method, all data will be contained in sqlinfo list. 
     * To inject that data you have to run injectvalues for k file after
     * all data have been prepared.
     *
     * @param k number of file
     * @param c number of sql statement
     * @param env name of enviroment in tex file added to records
     * @param grouping grouping
     * @param rs record set returned from executed query
     * @return 0
     */
    public int prepareValues(int k,int c,String env,String grouping,RecordSet rs){
        if(rs == null)
            return(0);
        
       String start = "\\begin{document}";
       String end = "\\end{document}";
      // int to = this.data[k].indexOf(where) + where.length();
   //    rs.toPrint();
    //   if(from > to || from < 0 || to < 0)
    //       return(1);
        char l = (char) ('A' + (char)c);
        String prefix;
        if(env == null)
            prefix =  listOfFiles[k].getName().substring(0,listOfFiles[k].getName().indexOf('.')) + l;
        else
            prefix = env;
        
      //  System.out.println("PREFIX:'" + prefix +"'");
      //  System.out.println("ENV:'" + env +"'");
        
        
       if(grouping == null){
           prepareValuesSimple(k,c, prefix, rs);
       }
       else{
           String[] groupstmp = grouping.split(",");
           int[] group = new int[groupstmp.length];
           for(int i=0;i<groupstmp.length;i++)
               group[i] = Integer.parseInt(groupstmp[i]);
           

          sqlinfo.get(k).get(c).setData(prepareValuesGrouping(group, 0,0,rs.get_rows(),  rs, prefix)); 
          
           groupstmp = null;
           group = null;
           System.out.println(  rs.get_rows() + " records SQL" + (c+1));   
           rs = null;
       }
      return(0);  
   }
      
    /** Method which generate data from records from query, after executing this
     * method, all data will be contained in sqlinfo list,
     * this method is not using grouping
     * To inject that data you have to run injectvalues for k file after
     * all data have been prepared.
     *
     * @param k number of file
     * @param c number of sql statement
     * @param prefix name of enviroment in tex file added to records
     * @param rs record set returned from executed query
     * @return 0
     */
    public int prepareValuesSimple(int k,int c, String prefix, RecordSet rs){
       String temp1 = "";
       String temp2 = "";

      StringBuilder result = new StringBuilder(); 
       
        for(int i=0; i < rs.get_rows(); i++){
  
         temp1 = "\\" + prefix;
        for(int j=0; j < rs.get_cols(); j++)
           temp1 += "{" + rs.getVal(i, j) + "}";
        
          result.append(temp1 + "\n");
   //     temp2 = temp2.concat(temp1 + "\n");
      }
        
     temp2 = result.toString();
        
        
       sqlinfo.get(k).get(c).setData(temp2); 

       System.out.println(  rs.get_rows() + " records SQL" + (c+1));  
    return(0);
}
      
    /**Method which generate data from records from query, after executing this
     * method, all data will be contained in sqlinfo list,
     * this method is using grouping
     * To inject that data you have to run injectValues for k file after
     * all data have been prepared.
     *
     * @param group table of integers, which size represent count of groups and elements represent count of elements in each group
     * @param gid number of actual group
     * @param s start index 
     * @param e end index
     * @param rs records from query
     * @param prefix name of enviroment
     * @return prepared data
     */
    public String prepareValuesGrouping(int[] group, int gid,int s, int e, RecordSet rs, String prefix){

      if(gid == group.length){
          int sc=0;
          for(int i=0;i<gid;i++)
             sc += group[i];
       return(prepareInjectValues(prefix, rs,s,e,sc));
      }
      if(gid < group.length){
          StringBuilder result = new StringBuilder(); 
          char x;
          String strreturn="";
          String strtmp="";
          String endin;
          int index1=s;
          int index2=s;
          int check=0;
          

          int col =0;
          for(int i=0;i<gid;i++)
              col += group[i];
          

          index2++;
          while(index2 <= rs.get_rows() && index2 <= e){
           if(index2 == rs.get_rows() || index2 == e)
               check = 2;
           else{
            for(int i=0;i<group[gid];i++)
               if(!rs.getVal(index1,i+col).equals(rs.getVal(index2,i+col)))check = 1;
           }
           
           if(check >= 1){

               x = (char) ('A' + (char)gid);
               strtmp= "\\" + prefix+x;
               
               
               for(int i=0;i<group[gid];i++)
                  strtmp += "{" + rs.getVal(index1,i+col)+ "}";
               
         //    System.out.print("odalam w " +  gid + " index1:" + index1 + " index2:" + index2 + "\n");
              strtmp += "\n"; 
              strtmp += prepareValuesGrouping(group, gid+1,index1, index2, rs, prefix);
              
              
              endin =  "\\end" + prefix + x + "\n";

              result.append(strtmp.concat(endin));

               
               index1 = index2;
               check = 0;
           }

            index2++;
          }
          return(result.toString());    
      }
     return("");
}
      
    /**Method used by prepareValuesGrouping as part of it
     */
    private String prepareInjectValues(String prefix,RecordSet rs,int s, int e,int sc){
       String temp1 = "";
       String temp2 = "";

       
       StringBuilder result = new StringBuilder();

        for(int i=s; i < e; i++){
  
         temp1 = "\\" + prefix;
        for(int j=sc; j < rs.get_cols(); j++)
         temp1 += "{" + rs.getVal(i, j) + "}";

         temp1 += "\n";
    result.append(temp1);
     //   temp2 = temp2.concat(temp1);
      }

    return(result.toString());
}    
      
     /**Method which inject data to templates at exact places where the statements ends
     * @param k number of file in alfabetical order.
     */
    public void InjectValues(int k){
        int to;
        for(int i=sqlinfo.get(k).size()-1; i >= 0; i--){
            to = sqlinfo.get(k).get(i).getIndex();
             data[k] = data[k].substring(0,to) + sqlinfo.get(k).get(i).getData() + data[k].substring(to,data[k].length());
        }
        
    }
    
   
     /**Get number of files.
     */
     public int getLenght(){return data.length;}
    
    /**Get full path with file name of output file.
     *  @param k number of file in alfabetical order
     */
     public String getSavedPath(int k){return (pathoutput + listOfFiles[k].getName());}
     
     /**Get full path of output.
     */
     public String getOutputPath(){return (pathoutput);}
    
     
    /**Check if file is compilable.
     *  @param k number of file in alfabetical order 
     */
     public int compileCheck(int k){return(compileable[k]);}
    
    /**Method returning arrayList of object ParsedSQLInfo which conaints all
     * parsed statements and additional infos
     *  @param k number of file in alfabetical order 
     *  @return arrayList of object ParsedSQLInfo
     */
     public ArrayList<ParsedSQLInfo> getStmts(int k){return sqlinfo.get(k);}
    
     
    /** get name of file
     *  @param k number of file in alfabetical order 
     *  @return file name in String
     */
     public String getFileName(int k ){
        return(listOfFiles[k].getName());
        
    }
    
    /** Error Count
     *  @return number of errors in object
     */
     public int getErrorCount(){
        return(total_errors);
  
    }
    
    
    
}
