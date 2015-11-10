package soot.jimple.infoflow.sgp;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.collect.ConcurrentHashSet;
import soot.jimple.infoflow.collect.MyConcurrentHashMap;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;

public class SGPMetaData {
	
	//Each SGPMetaData object contains a Set of abstractions and some additional context
	
	/*Local var definitions*/
	Set<Abstraction> absSet;
	Unit stmt;
	
	/*Basic constructor*/
	public SGPMetaData(){
		this.absSet=new HashSet<Abstraction>(); //Do you want this to be a hashset???
		//need to instantiate the Unit object here - what is concrete class?
	}
	
	/*Getters/Setters*/

}
