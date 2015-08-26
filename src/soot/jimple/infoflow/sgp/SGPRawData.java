/*******************************************************************************
 * Katie Underwood 2015
 * Class to contain all Abstraction objects generated
 * during apk analysis.
 * 
 * Structure of code adapted from soot.jimple.infoflow.results.InfoflowResults
 ******************************************************************************/
package soot.jimple.infoflow.sgp;

import java.io.*;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.collect.ConcurrentHashSet;
import soot.jimple.infoflow.collect.MyConcurrentHashMap;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;

/**
 * Class for collecting all Abstraction objects
 * 
 * @author Katie Underwood
 */
public class SGPRawData {

    /*Local variable definitions*/
	
	//Define a list of Sets to contain the incoming sets of abstractions
    private ArrayList<Set<Abstraction>> abstractionSetList;
    
    

    
    /*Basic constructor*/
    public SGPRawData(){
	this.abstractionSetList = new ArrayList<Set<Abstraction>>(); 
    } 

    
    /*Add a new set of Abstractions to the abstractionSetList ArrayList*/
    public void addSet(Set<Abstraction> inputAbstrSet){
    	abstractionSetList.add(inputAbstrSet);
    }
    
    /*Get the set of Abstractions at a specified element of the ArrayList*/
    public Set<Abstraction> getSet(int index){
    	
    	return abstractionSetList.get(index);
    }
    
    /*Dump all collected Abstractions from all sets to file*/
    public void toFile(String filename){
    	
    	//Iterators for iterating over each element of each set in the list
    	//Iterator listIter = new

    	//Declare output filestream based on input name
    	FileOutputStream fout = null;
    	
    	//Try to open file for output, catch exception if unsuccessful
    	try{
    		fout = new FileOutputStream(filename);    		
    	}catch(FileNotFoundException e){
    		System.out.println("Error: Could not open "+filename+" for writing.");
    		return;
    	}
    	
    	//Instantiate PrintWriter to perform the output
    	PrintWriter fwriter = new PrintWriter(fout);
    	
    	//Counters
    	int i=0;
    	int j=0;
    	
    	//Declare an iterator for accessing members of each Set<Abstraction>
    	Iterator<Abstraction> setIter;
    	
    	//Declare a temp Abstraction object for output
    	Abstraction temp;
    	
    	fwriter.println("************************************************************");
    	
    	//Write contents of abstractionSetList to file
    	//Iterate over each Set in the list
    	for(Set<Abstraction> set : abstractionSetList){
    		
    		//Log info in file
    		fwriter.println("Now printing contents of Set "+i);
    		
    		//If current set not null, instantiate the iterator
    		if (set!=null&&!set.isEmpty()){
    			setIter = set.iterator();
    		}else{
    			fwriter.println("Set "+i+" was null or empty, continuing...");
    			fwriter.println("************************************************************");
    			i++;
    			continue;
    		}
    		
    		
    		//Reset set element counter
    		j=0;
    		
    		//Flush printwriter
    		fwriter.flush();
    		
    		//Iterate over each Abstraction in the given set and output
    		while(setIter.hasNext()){
    			
    			//Output which element we're printing
        		fwriter.println("Now printing Access Path of Abstraction "+j+" from Set "+i);
        		temp = setIter.next();
    			fwriter.println("Access Path:"+temp.getAccessPath().toString());//IS THIS WHAT YOU WANT???
    			fwriter.println("Field Count:"+temp.getAccessPath().getFieldCount());//IS THIS WHAT YOU WANT???
    			//fwriter.println("Base Type:"+temp.getAccessPath().getBaseType());//IS THIS WHAT YOU WANT???
    			//fwriter.println("Plain Value:"+temp.getAccessPath().getPlainValue());//IS THIS WHAT YOU WANT???
    			//What info do you want to output here??
    			
    			//Incr set element counter
    			j++;
    		}
    		
    		fwriter.println("************************************************************");
    		i++; //incr list counter
    	}
    	
    	fwriter.println("END OF OUTPUT");
    	fwriter.println("************************************************************");
    	
    	//Flush print buffer
    	fwriter.flush();
    	
    	//Close filestream
    	try{
    		fout.close();
    	}catch(IOException e){
    		System.out.println("Error: Could not close "+filename);
    		//Should throw exception up here? Best practice?
    	}
    	
    }
    

    /*NOT MODIFIED YET    
    //
    // Gets the number of entries in this result object
    // @return The number of entries in this result object
    //
	public int size() {
		return this.results == null ? 0 : this.results.size();
	}
	
    //
    // Gets the total number of source-to-sink connections. If there are two
    // connections along different paths between the same source and sink,
    // size() will return 1, but numConnections() will return 2.
    // @return The number of source-to-sink connections in this result object
    //
	public int numConnections() {
		int num = 0;
		if (this.results != null)
			for (Entry<ResultSinkInfo, Set<ResultSourceInfo>> entry : this.results.entrySet())
 				num += entry.getValue().size();
		return num;
	}
	
    //
    // Gets whether this result object is empty, i.e. contains no information
    // flows
    // @return True if this result object is empty, otherwise false.
    //
	public boolean isEmpty() {
		return this.results == null || this.results.isEmpty();
		}*/
	
	



    


	

    /*NOT MODIFIED YET
    public void printResults() {
		for (ResultSinkInfo sink : this.results.keySet()) {
			logger.info("Found a flow to sink {}, from the following sources:", sink);
			for (ResultSourceInfo source : this.results.get(sink)) {
				logger.info("\t- {}", source.getSource());
				if (source.getPath() != null)
					logger.info("\t\ton Path {}", Arrays.toString(source.getPath()));
			}
		}
		}*/




    /*NOT MODIFIED YET
    @Override
	public String toString() {
		boolean isFirst = true;
		StringBuilder sb = new StringBuilder();
		for (ResultSinkInfo sink : this.results.keySet())
			for (ResultSourceInfo source : this.results.get(sink)) {
				if (!isFirst)
					sb.append(", ");
				isFirst = false;
				
				sb.append(source);
				sb.append(" -> ");
				sb.append(sink);
			}
		return sb.toString();
		}*/
	

    /*NOT MODIFIED YET
    @Override
    	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InfoflowResults other = (InfoflowResults) obj;
		if (results == null) {
			if (other.results != null)
				return false;
		} else if (!results.equals(other.results))
			return false;
		return true;
		}*/
	
}//end of class
