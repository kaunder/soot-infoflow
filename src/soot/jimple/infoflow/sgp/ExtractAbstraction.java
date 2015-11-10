/*******************************************************************************
 * Katie Underwood 2015
 * Class to contain functions for extracting all Abstraction objects generated
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
 * Class for operations related to extracting Abstraction objects
 */
public class ExtractAbstraction{


    
}
