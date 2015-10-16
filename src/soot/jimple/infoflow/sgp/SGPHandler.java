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
import java.util.Iterator;
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
    
    //Set up strings to hold Abstraction info
    String flwfxn;	
    String acspath;
    String srcctxt;
    
    //Identify name of handler function for the log
    String handler = "Handler: notifyFlowIn\n";
	
	//Get flow function type
	if(type!=null){
		flwfxn = type.toString();
	}else{
		flwfxn="null";
	}
	
	//Get tainted access path
	if((taint.getAccessPath()!=null)&&(taint.getAccessPath().getPlainValue()!=null)){
		acspath = taint.getAccessPath().getPlainValue().toString();	
	}else{
		acspath="null";
	}
	
	//Get source context
	if(taint.getSourceContext()!=null){
		srcctxt = taint.getSourceContext().toString();
	}else{
		srcctxt="null";
	}
	
	//Append log info, pass appended strings to log function for output	
	String flow = "Flow function type: "+flwfxn+"\n";
	String accesspath = "AccessPath value: "+acspath+"\n";    
	String sourcecontext = "Source Context: "+srcctxt+"\n";
	
	this.log(handler+flow+accesspath+sourcecontext);
		
	}

	@Override
	//Handler function that is invoked when a taint is generated in the data flow engine
	public Set<Abstraction> notifyFlowOut(Unit stmt, Abstraction d1,
			Abstraction incoming, Set<Abstraction> outgoing, IInfoflowCFG cfg,
			FlowFunctionType type) {
		
		//Identify name of handler function for the log
		String handler = "Handler: notifyFlowOut\n";
		
		//Set up strings for log file text
		String inabs = "***Incoming Abstraction Data***\n";
		String outabs="***Outgoing Abstraction Set Data***\n";
		String acspathOUTtxt="Outgoing Abstraction Access Path: ";
		String srcctxtOUTtxt="Outgoing Abstraction Source Context: ";
		
		//Set up strings for getting Abstraction data
		String flwfxn;
		String acspathIN;
		String srcctxtIN;
		String acspathOUT;
		String srcctxtOUT;
		
		//Get type of flow function
		if(type!=null){
			flwfxn = type.toString();
		}else{
			flwfxn="null";
		}
		
		
		//Get incoming access path
		if((incoming.getAccessPath()!=null)&&(incoming.getAccessPath().getPlainValue()!=null)){
			acspathIN = incoming.getAccessPath().getPlainValue().toString();
		}else{
			acspathIN="null";
		}
		
		//Get incoming source context
		if(incoming.getSourceContext()!=null){
			srcctxtIN = incoming.getSourceContext().toString();
		}else{
			srcctxtIN="null";
		}

		
		//Get information about all outgoing Abstractions
		Iterator<Abstraction> outIter;
		if(outgoing!=null&&!outgoing.isEmpty()){
		    outIter = outgoing.iterator();

		    int j=0; //loop counter
		    Abstraction temp; //temp object for loop access
		    acspathOUT=null; //initialize strings
		    srcctxtOUT=null;
		    

		    //loop over all Abstractions in outgoing set
		    while(outIter.hasNext()){
			outabs=outabs.concat("Outgoing set "+j+":\n");

			//Advance iterator to next element in set
			temp=outIter.next();

			//Get AccessPath from this Abstraction
			if((temp.getAccessPath()!=null)&&(temp.getAccessPath().getPlainValue()!=null)){
				acspathOUT = temp.getAccessPath().getPlainValue().toString();
			}else{
				acspathOUT="null";
			}
			
			//Get SourceContext from this Abstraction
			if(temp.getSourceContext()!=null){
				srcctxtOUT = temp.getSourceContext().toString();
			}else{
				srcctxtOUT="null";
			}
			
			//Build output string
			outabs=outabs.concat(acspathOUTtxt+acspathOUT+"\n"+srcctxtOUTtxt+srcctxtOUT+"\n");
			
			j++;
		    }
		}else{
			acspathOUT="null";
			srcctxtOUT="null";
		    outabs=outabs.concat("Outgoing set empty - taint not propagated.\n");
		}

		//Append collected info with description strings
		String flow = "Flow function type: "+flwfxn+"\n";
		String accesspathIN = "Incoming AccessPath value: "+acspathIN+"\n";
		String sourcecontextIN = "Incoming Source Context: "+srcctxtIN+"\n";
		
		//Send full output string to log
		this.log(handler+flow+inabs+accesspathIN+sourcecontextIN+outabs+"\n");
		
		return outgoing; //pass on outgoing abstraction set without altering
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
