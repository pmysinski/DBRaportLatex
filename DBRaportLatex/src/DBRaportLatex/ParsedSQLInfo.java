/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBRaportLatex;

/** Class container which contains all info about sql queries parsed from templates
 * also contains data generated after executing sql queries.
 */
public class ParsedSQLInfo {

    String query;
    String name;
    String group;
    int index;
    String data;
    
    
    
    public ParsedSQLInfo(String query, String name, String group, int index) {
        this.query = query;
        this.name = name;
        this.group = group;
        this.index = index;
        this.data = "";
     }
    
    
public int getIndex() {return index;}
public String getQuery() {return query;}
public String getName() {return name;}
public String getGroup() {return group;}
public String getData() {return data;}
public void setIndex(int index) {this.index = index;}
public void setQuery(String query) {this.query = query;}
public void setName(String name) {this.name = name;}
public void setGroup(String group) {this.group = group;}
public void setData(String data) {this.data = data;}
    
    
    
    
    
    
}
