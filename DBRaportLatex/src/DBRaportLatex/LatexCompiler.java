/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBRaportLatex;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;




/** Basic class for executing shell commands to run latex compiler
 *  
 */
public class LatexCompiler {
    
    String latexcompilerpath;
    String pdfoutput;
    int ok;
    
    /** Constructor for initalization paths where latex compilator is located
     *  and where to output comiles pdfs.
     * 
     * 
     * @param latexcompilerpathh path to latexcomiler executable file.
     * @param pdfoutputt path to output pdf files.
     */
    public LatexCompiler(String latexcompilerpathh,String pdfoutputt){
    
        latexcompilerpath = latexcompilerpathh;
        pdfoutput = pdfoutputt;
        

     File tmp = new File(pdfoutput);
    if (!tmp.exists()) {
        tmp.mkdirs();
}
        
        
    
}
    
    /**Method to comile exact file you put as parameter.
     * 
     * @param filetexpath full path to latex file
     * @return 0
     */
    public int compileTemplate(String filetexpath){
       
       
       
       
       System.out.print( executeCommand(latexcompilerpath + " --output-directory=" + pdfoutput + " " + filetexpath));
     // System.out.print( executeCommand("ping www.google.pl"));
       
       
       return(0);
   }
   
   
    
    
    
     /**Method to execute shell command. Only working in windows.
     * 
     * @param command shell command
     * @return returns from shells window after running command
     */
    	private String executeCommand(String command) {
 
		StringBuffer output = new StringBuffer();
                System.out.print("Executing shell command: \n" + "cmd /c start " + command);
		Process p;
		try {
			p = Runtime.getRuntime().exec("cmd /c start " + command);
			p.waitFor();
                        
                      //  try {Thread.sleep(5000);} catch(InterruptedException ex) {Thread.currentThread().interrupt();}
                        
			BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(p.getInputStream()));
 
                        String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		}
 
		return output.toString();
 
	}
    
    
    
}
