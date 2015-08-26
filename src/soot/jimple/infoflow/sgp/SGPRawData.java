/*******************************************************************************
 * Katie Underwood 2015
 * Class to contain all Abstraction objects generated
 * during apk analysis.
 * 
 * Structure of code adapted from soot.jimple.infoflow.results.InfoflowResults
 ******************************************************************************/
package soot.jimple.infoflow.sgp;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
    //private List<Set<Abstraction>> abstractionSetList;

    
    /*Basic constructor*/
    public SGPRawData(){
	//this.abstractionSetList = new List YOU ARE HERE
    } 

    
    /*Add a new set of Abstractions*/
    public void addSet(Set<Abstraction> inputAbstrSet){
	//code goes here
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