/*******************************************************************************
 * Katie Underwood 2015
 * Handler function to be passed to Infoflow.setTaintPropagationHandler
 * 
 * 
 ******************************************************************************/
package soot.jimple.infoflow.sgp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import soot.Unit;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.handlers.TaintPropagationHandler;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.sgp.*;

public class SGPHandler implements TaintPropagationHandler{

    //Declare sgpData object which will be used to capture required metadata
    SGPData sgpData;
    
    //File I/O objects
    File file;
    FileOutputStream fout;
    FileWriter foutWriter;
    String filename;
    
    //TESTING ONLY - test counter
    int i=0;
	
	//Default constructor
	public SGPHandler(){
		//Instantiate the SGPData object
		sgpData = new SGPData();

		//Instantiate file object
		filename="sgpOutputFile.txt";//for now use default name
		file = new File(filename);
		
		
		//create output file
		try {
			file.createNewFile();
		} catch (IOException e) {
			System.out.println("Error: Could not create file: "+filename);
			e.printStackTrace();
		}

		//Instantiate file writer
		try {
			foutWriter = new FileWriter(file);
		} catch (IOException e) {
			System.out.println("Error: Could not instantiate file writer for "+filename);
			e.printStackTrace();
		}


	
	}
	
    @Override
    //Handler function that is invoked when a taint is propagated in the data flow engine
    public void notifyFlowIn(Unit stmt, Abstraction taint, IInfoflowCFG cfg,
			     FlowFunctionType type) {
	//System.out.println("I am calling from notifyFlowIn, a taint is being propagated!");
	String handler = "Handler: notifyFlowIn\n";
	String flwfxn = type.toString();
	String flow = "Flow function type: "+flwfxn+"\n";
	String acspath = taint.getAccessPath().getPlainValue().toString();
	String accesspath = "AccessPath value: "+acspath+"\n";    
	String srcctxt = taint.getSourceContext().toString();
	String sourcecontext = "Source Context: "+srcctxt+"\n";
	
		this.log(handler+flow+accesspath+sourcecontext);
		
	}

	@Override
	//Handler function that is invoked when a taint is generated in the data flow engine
	public Set<Abstraction> notifyFlowOut(Unit stmt, Abstraction d1,
			Abstraction incoming, Set<Abstraction> outgoing, IInfoflowCFG cfg,
			FlowFunctionType type) {
		// TODO Auto-generated method stub
		System.out.println("I am calling from notifyFlowOut, a new taint has been created!");
		YOU ARE HERE - modify the log so that it operates as above but shows the difference in incoming and outgoing Abstr
		this.log("I am calling from notifyFlowOut, a new taint has been created! ");
		return outgoing; //pass on outgoing abstraction without altering
	}

    /*Write to the log file*/
    private void log(String str){
	synchronized(foutWriter){ //ensure safe concurrent access
	    try {
		foutWriter.write("********************************************************************************\n");
		foutWriter.write("Log "+i+":\n");
		foutWriter.write(str);
		foutWriter.write("********************************************************************************\n");
	    } catch (IOException e) {
		System.out.println("Error: Could not write to file: "+filename);
		e.printStackTrace();
	    }
	    try {
		foutWriter.flush();//flush buffer
	    } catch (IOException e) {
		System.out.println("Error: Could not flush file: "+filename);
		e.printStackTrace();
	    } 
	    i++; //increment log counter
	}
    }
    
    /*Close the open file stream*/
    public void closeFile(){
	try{
	    foutWriter.close();
	}catch(IOException e){
	    System.out.println("Error: coult not close "+filename);
	}
    }
    
}
