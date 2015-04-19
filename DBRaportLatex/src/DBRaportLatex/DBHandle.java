/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBRaportLatex;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**Class responsible for connection and executing queries on database
 * One object represent one connection,
 * Connection is enstablished in the constructor and if it fails you need new object
 * Its bad coded but it works:)
 */
public class DBHandle {
    
    String hostname;
    String port;
    String dbpath;
    String encoding;
    String user;
    String password;
    String engine;
    int ok;
    int total_errors;
    
    Connection con;
    Statement stmt; 
    ResultSet rs;
    ResultSetMetaData rsmd;
    
    /** Only one constructor with many params to enstablish connection to database
     *  if it fails function is ok will return 1 and you will need to create new
     *  object or stop your program.
     *  It has multiple params but it depends on engine if all params are used or
     *  only some of them. At the moment implemented are connections to engines:
     *  firebird, mysql, sqlite
     * @param enginee name of database engine eg. firebirdsql, mysql
     * @param host hostname or IPv4 of database server (not used in sqlite)
     * @param prt port of database server connection
     * @param dbpathh path to database or database name 
     * @param enc encoding of database 
     * @param u user
     * @param pw password
     */
    public DBHandle(String enginee,String host,String prt, String dbpathh,String enc, String u, String pw){
      // FireBirdCreator();
        total_errors=0;
        engine = enginee;
        hostname = host;
        port = prt;
        encoding = enc;
        dbpath = dbpathh;
        user = u;
        password = pw;
        
        
        //System.out.print("jdbc:firebirdsql:" + hostname + ":" + port + "/" + dbpath + "?encoding=" + encoding);
        try{
            String constr = "";
            if(engine.equals("firebirdsql")){
                try {
                    Class.forName("org.firebirdsql.jdbc.FBDriver");
                } catch (ClassNotFoundException ex) {
                    System.out.println( "Data Base connection driver error for Firebird\nEXCEPTION: " + ex.getMessage( ) );
                }
                if(hostname.equals("embedded"))
                  constr = "jdbc:" + engine + ":" + hostname + ":" + dbpath;
                else
                  constr = "jdbc:" + engine + ":" + hostname + ":" + port + "/" + dbpath + "?encoding=" + encoding;

                 con = DriverManager.getConnection(constr,user,password);
            }
            if(engine.equals("mysql")){
                constr = "jdbc:" + engine + ":" + hostname +":"+ port + "/" + dbpath + "?user=" + user + "&password=" + password;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException ex) {
                    System.out.println( "Data Base connection driver error for MySql\nEXCEPTION: " + ex.getMessage( ) );
                }
            con = DriverManager.getConnection(constr);
            }
            if(engine.equals("sqlite")){
                constr = "jdbc:" + engine + ":" + dbpath;
                try {
                    Class.forName("org.sqlite.JDBC");
                } catch (ClassNotFoundException ex) {
                    System.out.println( "Data Base connection driver error for SQLITE3\nEXCEPTION: " + ex.getMessage( ) );
                }
            con = DriverManager.getConnection(constr);
            }
          
       
        stmt = con.createStatement(
            ResultSet.TYPE_FORWARD_ONLY,
            ResultSet.CONCUR_READ_ONLY);
        
        System.out.println( "Connection to database: OK" );
        ok=0;
        }
        catch ( SQLException err ) {
         System.out.println( "Connection to database: FAILED\nEXCEPTION: " + err.getMessage( ) );
          ok=1;
        }
    }
       
     /** Check if object is constructed without problems.
      * 0 - no errors
      * 1 - critical error
      * @return 0 - no errors
      */   
      public  int isok(){return(ok);}
    
    
    
 
    
    /** Basic method to query data from database if connection is on
     * @param SQL query to database
     * @return object RecorSet which contains all records returned from query
     */
    public  RecordSet executeSQL2(String SQL){
        int n=0,m=0,i=0,j=0;
        String tmp;
        RecordSet ret = null;
         try{
           rs = stmt.executeQuery( SQL );
           rsmd = rs.getMetaData();
           m=rsmd.getColumnCount();
  
        
       
        ret = new RecordSet(m);

        i=0;
        while(rs.next()) {
            
                for(j=0;j < m; j++){
                    
                    tmp = rs.getString(j+1);
                    if(tmp != null){

         
                     ret.setVal(tmp, i, j);
                     
                     
                    }
                    else
                     ret.setVal("null", i, j);
                   // System.out.println( val[i][j] );
                }
            i++;
        }   
        rs.close();
        }
        catch ( SQLException err ) {
         
           if( err.getErrorCode() != 0){
       System.out.println("SQL STATEMENT PROBLEM: \n" + SQL + "\nHANDLED EXCEPTION:" +  err.getMessage( ));
       total_errors++;
       }
        }
        return(ret);
    }
/*
     public int executeSQLupdate(String SQL){
     // System.out.println( SQL );   
      

    //     System.out.println( SQL );

      
      try{
           stmt.executeUpdate(SQL);
           return(0);
     }
        catch ( SQLException err ) {
           if( err.getErrorCode() != 0){
       System.out.println("SQL STATEMENT PROBLEM: \n" + SQL + "\nHANDLED EXCEPTION:" +  err.getMessage( ));
       total_errors++;
       }
       return(2);
       }
    
     }
     */
       
       
       
     /** Method returning sql query errors count
      * @return error count
      */   
    public int getErrorCount(){
        return(total_errors);
  
    }
    
       
}
