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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import soot.Unit;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.handlers.TaintPropagationHandler;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.sgp.*;

public class SGPHandlerVerbose implements TaintPropagationHandler{

    //Declare sgpData object which will be used to capture required metadata
    SGPData sgpData;
    
    //File I/O objects
    File file;
    FileOutputStream fout;
    FileWriter foutWriter;
    String filename;
    
    //TESTING ONLY - test counter
    int i=0;
    
    //HashMaps to contain Abstraction counters, contexts
    //SIZE OF HASHMAP???
    ConcurrentHashMap<Integer, Integer> counters;
    ConcurrentHashMap<Integer, String> contexts;
    
	//Default constructor
	public SGPHandlerVerbose(){
		//Instantiate the SGPData object
		sgpData = new SGPData();

		//Instantiate counter hashmap, contextsw hashmap
		 counters = new ConcurrentHashMap<Integer, Integer>();
		 contexts = new ConcurrentHashMap<Integer, String>();
		
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
    
    //Set up local vars to hold Abstraction info
    String flwfxn;	
    String acspath;
    String srcctxt;
    String statement;
    int abshash=0; //0 indicates that the Abstraction was null
    int accesshash=0; //0 indicates that the AccessPath was null
    
    //Identify name of handler function for the log
    String handler = "Handler: notifyFlowIn\n";
	
	//Get flow function type
	if(type!=null){
		flwfxn = type.toString();
	}else{
		flwfxn="null";
	}
	
	//Get the statement
	if(stmt!=null){
		statement=stmt.toString();
	}else{
		statement="null";
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
	
	//Get Abstraction hash
	if(taint!=null){
		abshash=taint.hashCode();
	}
	
	//Get AccessPath hash
	if(taint.getAccessPath()!=null){
		accesshash=taint.getAccessPath().hashCode();
	}
	
	//Append log info, pass appended strings to log function for output	
	//This is prob too much overhead for full run - will need to streamline output
	String flow = "Flow function type: "+flwfxn+"\n";
	String accesspath = "AccessPath value: "+acspath+"\n";    
	String sourcecontext = "Source Context: "+srcctxt+"\n";
	String abshashtxt = "Abstraction Hash:"+abshash+"\n";
	String accesshashtxt = "AccessPath Hash:"+accesshash+"\n";
	String stmttxt = "Soot statement: "+statement+"\n";
	this.log(handler+flow+accesspath+sourcecontext+abshashtxt+accesshashtxt);
		
	}

	@Override
	//Handler function that is invoked when a taint is generated in the data flow engine
	public Set<Abstraction> notifyFlowOut(Unit stmt, Abstraction d1,
			Abstraction incoming, Set<Abstraction> outgoing, IInfoflowCFG cfg,
			FlowFunctionType type) {
		
		//Length counter, source context logic
		Integer tmpctr;
		//Iterate over each Abstraction in the outgoing set
    	for(Abstraction nextOutgoingAbs : outgoing){
    		if(nextOutgoingAbs!=null){
    		if(counters.containsKey(Integer.valueOf(nextOutgoingAbs.hashCode()))){
    			//If this Abstraction is already in the hashmap, find and increment it's counter	
    				tmpctr=counters.get(nextOutgoingAbs.hashCode())+1;
    				counters.replace(Integer.valueOf(nextOutgoingAbs.hashCode()), tmpctr);
    		}else{
    			//Otherwise, add a new entry to context map. 
    			//Add source context for this Abstraction to context hash map
				if(nextOutgoingAbs.getSourceContext()!=null){
					contexts.put(Integer.valueOf(nextOutgoingAbs.hashCode()), nextOutgoingAbs.getSourceContext().toString());
				}else{
					contexts.put(Integer.valueOf(nextOutgoingAbs.hashCode()), "(null SourceContext)");
				}
    			
    			//Assign length counter to newly added Abstraction
    			if(incoming!=null){
    				Integer value = counters.get(Integer.valueOf(incoming.hashCode()));
    				if(value!=null){
    					value++; //If Abstraction has a parent that's already in the hash table, new counter = parent counter ++
    				}else{
    					value=1; //Otherwise, this is the first propagation of this abstraction so new counter =1
    					
    				}
    				counters.put(Integer.valueOf(nextOutgoingAbs.hashCode()), value); //update counter for this Abstraction
    				}
    			}
    		}
    	}

    	//Verbose Output Logic
    	
		//Identify name of handler function for the log
		String handler = "Handler: notifyFlowOut\n";
		
		//Set up local vars for log file text
		String inabs = "***Incoming Abstraction Data***\n";
		String outabs="***Outgoing Abstraction Set Data***\n";
		String acspathOUTtxt="Outgoing Abstraction Access Path: ";
		String srcctxtOUTtxt="Outgoing Abstraction Source Context: ";
		String abshashOUTtxt="Outgoing Abstraction Hash:";
		String acspathhashOUTtxt="Outgoing AccessPath Hash:";
		
		//Set up local vars for getting Abstraction data
		String flwfxn;
		String acspathIN;
		String srcctxtIN;
		String acspathOUT;
		String srcctxtOUT;
		String statement;
		String acspathd1;
		String srcctxtd1;
		int abshashIN=0; //0 indicates incoming Abstraction is null
		int acspathhashIN=0; //0 indicates AccessPath for incoming abstraction is null
		
		//Get type of flow function
		if(type!=null){
			flwfxn = type.toString();
		}else{
			flwfxn="null";
		}
		
		//Get soot statment
		if(stmt!=null){
			statement=stmt.toString();
		}else{
			statement="null";
		}
		
		//Get d1 access path
		if((d1.getAccessPath()!=null)&&(d1.getAccessPath().getPlainValue()!=null)){
			acspathd1 = d1.getAccessPath().getPlainValue().toString();
		}else{
			acspathd1="null";
		}
		
		//Get incoming source context
		if(d1.getSourceContext()!=null){
			srcctxtd1 = d1.getSourceContext().toString();
		}else{
			srcctxtd1="null";
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
		
		//Get incoming Abstraction hash
		if(incoming!=null){
			abshashIN=incoming.hashCode();
		}
		
		//Get incomgin AccessPath hash
		if(incoming.getAccessPath()!=null){
			acspathhashIN=incoming.getAccessPath().hashCode();
		}

		
		//Get information about all outgoing Abstractions
		Iterator<Abstraction> outIter;
		if(outgoing!=null&&!outgoing.isEmpty()){
		    outIter = outgoing.iterator();

		    int j=0; //loop counter
		    Abstraction temp; //temp object for loop access
		    acspathOUT=null; //initialize strings
		    srcctxtOUT=null;
		    int abshashOUT=0; //as above 0 for either hash value indicates the corresp object was null
		    int acspathhashOUT=0;
		    

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
			
			//Get hash for this Abstraction
			if(temp!=null){
				abshashOUT=temp.hashCode();
			}
			
			//Get hash for this AccessPath
			if(temp.getAccessPath()!=null){
				acspathhashOUT=temp.getAccessPath().hashCode();
			}
			
			//Build output string
			outabs=outabs.concat(acspathOUTtxt+acspathOUT+"\n"+srcctxtOUTtxt+srcctxtOUT+"\n"+abshashOUTtxt+abshashOUT+"\n"+acspathhashOUTtxt+acspathhashOUT+"\n");
			
			j++; //advance counter
			abshashOUT=0; //reset hash ints for next iteration
			acspathhashOUT=0;
			
		    }
		}else{
			acspathOUT="null";
			srcctxtOUT="null";
		    outabs=outabs.concat("Outgoing set empty - taint not propagated.\n");
		}

		//Append collected info with description strings
		String flow = "Flow function type: "+flwfxn+"\n";
		String accesspathd1 = "AccessPath at start of method (abs d1):"+acspathd1+"\n";
		String sourcecontextd1="Source Context at start of method (abs d1):"+srcctxtd1+"\n";
		String accesspathIN = "Incoming AccessPath value: "+acspathIN+"\n";
		String sourcecontextIN = "Incoming Source Context: "+srcctxtIN+"\n";
		String acspathhashINtxt = "Incoming AccessPath Hash: "+acspathhashIN+"\n";
		String abshashINtxt = "Incoming Abstraction hash: "+abshashIN+"\n";
		
		//Send full output string to log
		this.log(handler+flow+inabs+accesspathd1+sourcecontextd1+accesspathIN+sourcecontextIN+abshashINtxt+acspathhashINtxt+outabs+"\n");
		
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
    
    /*Output counter data*/
    public void outputData(){
    	
    	//Ensure safe access
    	synchronized(foutWriter){
    		//DEBUG/TESTING: Output size of counters, contexts
    		try{
    			foutWriter.write("********************************************************************************\n");
    			foutWriter.write("**** Data Summary ****\n");
    			foutWriter.write("Number of entries in counters = "+counters.size()+"\n");
    			foutWriter.write("Number of entries in contexts = "+contexts.size()+"\n");
    			foutWriter.write("********************************************************************************\n");
    			//TODO: - make this properly fixed width 
    			foutWriter.write("  Abstraction ID  |  Length  |  Source Context  | ");
    		}catch(IOException e){
    			System.out.println("Error: Could not write to file:"+filename);
    			e.printStackTrace();
    		}
    		
    		//Iterate over all elements of HashMap, output computed lengths
        	for (ConcurrentHashMap.Entry<Integer, Integer> entry : counters.entrySet()) {
        		try{
        			foutWriter.write(entry.getKey()+"       "); //Abstraction id (key)
        			foutWriter.write(entry.getValue()+"       "); //length
        			foutWriter.write(contexts.get(entry.getKey())+"       \n"); //source context
        			foutWriter.write("-----------------------------------------------------------------------------\n");
        		}catch(IOException e){
        			System.out.println("Error: Could not write to file:"+filename);
        			e.printStackTrace();
        		}
        	}
        }
    		
    	}
    	
    	
    
}
