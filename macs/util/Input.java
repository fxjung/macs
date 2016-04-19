/**
 * MACS - Multi-Agent Cooperative Search is a framework to develop
 * cooperating agents using different Metaheuristics
 * Copyright (C) 2013 University of Stirling
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */
package macs.util;




import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.ArrayList;

import macs.agents.AgentVocabulary;
import macs.heuristics.JobFiles;
import macs.ontologies.entities.problems.NodeData;
import macs.ontologies.entities.problems.SolutionElements;
import macs.uoc.cvrp.CVRPInputs;
import macs.uoc.cvrp.Node;
import macs.uoc.cvrp.VRPEdge;
import macs.uoc.pfsp.api.PFSPJob;
import macs.uoc.pfsp.base.BaseInputs;

/**
 * @author simon martin
 *
 */
public class Input implements AgentVocabulary {

	/**
	 * @param args
	 */
int problem;
	
	//Constructor  
	
	public Input () {	
	}
	

	

	public static List<SolutionElements> getNodeArray(String filename) throws FileNotFoundException {
		System.out.println("In getNodeArray ");
		List<SolutionElements> nodearray = new ArrayList<SolutionElements>();
    	 
	  	
    	Scanner s = new Scanner(new BufferedReader(new FileReader(filename))); 
    	
    	try {    		
    		while (s.hasNextLine()) {
    			
    			String str = s.nextLine();
    			
    			if(!str.equals("EOF")){
	    			if(str.contains(":")){
	    				continue;
	    			}
	    			else if (str.equals("NODE_COORD_SECTION")){
	    				continue;
	    			}
	    			  			
	    			else {
	    				Scanner intscan = new Scanner(str);
	    			 	NodeData node = new NodeData();		 	
	    				while (intscan.hasNext()){
	    					
	    					node.setId(intscan.nextInt());
	    					node.setX(intscan.nextFloat());
	    					node.setY(intscan.nextFloat());
	    							
	    				}
	    				
	    				nodearray.add(node);
	    				
	    				intscan.close();
	    			}    				
    			}	
    		}
	    	s.close();	    	
		   
		   
		   
    	}
    	catch (NoSuchElementException e){
    	}
	  
    
    	//instantiate the solution wrapper holding the only copy of the data
    	return nodearray;    	  	  
    }
		
	public static List<JobFiles> getBenchmarkNames(String filename) throws FileNotFoundException{
		System.out.println("In Benchmark ");
		List<JobFiles> names = new ArrayList<JobFiles>();
    	 
	  	
    	Scanner s = new Scanner(new BufferedReader(new FileReader(filename))); 
    	
    	try {    		
    		while (s.hasNextLine()) {
    			String str = s.nextLine();
    			System.out.println(str);
    		
    			
    			if(!str.equals("EOF")||!str.startsWith("#")){
    			//System.out.println(str);
					Scanner scan = new Scanner(str);
				 	JobFiles job = new JobFiles();		 	
				 		
				 		job.setDirectory(scan.next());
						job.setName(scan.next());
						job.setRows(scan.nextInt());
						job.setColumns(scan.nextInt());
						job.setConversations(scan.nextInt());
						while (scan.hasNext()){ 
							job.addAgent(scan.next()); 			
						}
					
					names.add(job);
					
					scan.close();
	    			    		
    			}
    			
    		}
	    	s.close();	  	   
		   
    	}
    	catch (NoSuchElementException e){
    	}
	
    
    	//instantiate the solution wrapper holding the only copy of the data
    	return names;    	 
	}
	
	 /**
     * Creates the (edges) savingsList according to the CWS heuristic.
     */
    public static LinkedList<VRPEdge> generateSavingsList(Node[] nodes)
    {
        int nNodes = nodes.length;
        VRPEdge[] savingsArray = new VRPEdge[(nNodes - 1) * (nNodes - 2) / 2];
        Node depot = nodes[0];
        int k = 0;
        for( int i = 1; i < nNodes - 1; i++ ) // node 0 is the depot
        {   for( int j = i + 1; j < nNodes; j++ )
            {   Node iNode = nodes[i];
            	Node jNode = nodes[j];
                // Create ijEdge and jiEdge, and assign costs and savings
                VRPEdge ijEdge = new VRPEdge(iNode, jNode);
                ijEdge.setCosts(ijEdge.calcCosts(iNode, jNode));
                ijEdge.setSavings(ijEdge.calcSavings(iNode, jNode, depot));
                VRPEdge jiEdge = new VRPEdge(jNode, iNode);
                jiEdge.setCosts(jiEdge.calcCosts(jNode, iNode));
                jiEdge.setSavings(jiEdge.calcSavings(jNode, iNode, depot));
                // Set inverse edges
                ijEdge.setInverse(jiEdge);
                jiEdge.setInverse(ijEdge);
                // Add a single new edge to the savingsList
                savingsArray[k] = ijEdge;
                k++;
            }
        }
        // Construct the savingsList by sorting the edgesList. Uses the compareTo()
        //  method of the Edge class (TIE ISSUE #1).
        Arrays.sort(savingsArray);
        List<VRPEdge> sList = Arrays.asList(savingsArray);
        LinkedList<VRPEdge> savingsList = new LinkedList<VRPEdge>(sList);
        //inputs.setList(savingsList);
        return savingsList;
    }

    /*
     * Creates the list of paired edges connecting node i with the depot,
     *  i.e., it creates the edges (0,i) and (i,0) for all i > 0.
     */
    public static Node[]  generateDepotEdges(CVRPInputs inputs)
    {   Node[] nodes = inputs.getNodes();
    	Node depot = nodes[0]; // depot is always node 0
        // Create diEdge and idEdge, and set the corresponding costs
        for( int i = 1; i < nodes.length; i++ ) // node 0 is depot
        {   Node iNode = nodes[i];        	
            VRPEdge diEdge = new VRPEdge(depot, iNode);
            iNode.setDiEdge(diEdge);
            diEdge.setCosts(diEdge.calcCosts(depot, iNode));
            VRPEdge idEdge = new VRPEdge(iNode, depot);
            iNode.setIdEdge(idEdge);
            idEdge.setCosts(idEdge.calcCosts(depot, iNode));
            // Set inverse edges
            idEdge.setInverse(diEdge);
            diEdge.setInverse(idEdge);
        }
        return nodes;
    }

    /**
    * @return geometric center for a set of nodes
    */
    public static float[] calcGeometricCenter(List<Node> nodesList)
    {
    	Node[] nodesArray = new Node[nodesList.size()];
        nodesArray = nodesList.toArray(nodesArray);
        return calcGeometricCenter(nodesArray);
    }

    public static float[] calcGeometricCenter(Node[] nodes)
    {
        // 1. Declare and initialize variables
	float sumX = 0.0F; // sum of x[i]
	float sumY = 0.0F; // sum of y[i]
	float[] center = new float[2]; // center as (x, y) coordinates
	// 2. Calculate sums of x[i] and y[i] for all iNodes in nodes
	Node iNode; // iNode = ( x[i], y[i] )
	for( int i = 0; i < nodes.length; i++ )
	{   iNode = nodes[i];
            sumX = sumX + iNode.getX();
            sumY = sumY + iNode.getY();
	}
	// 3. Calculate means for x[i] and y[i]
	center[0] = sumX / nodes.length; // mean for x[i]
	center[1] = sumY / nodes.length; // mean for y[i]
	// 4. Return center as (x-bar, y-bar)
	return center;
    }
    /**Simple parser to read the TSPLIB file format 
    The aim is to read the three sections of the file and put thes results in different arrays
	These arrays can then be used to build the relevant objects needed by the system.*/
    
    public static CVRPInputs getTSPLIBFormat(String filename) throws FileNotFoundException {
		
			
		List<String> strArr = new ArrayList<String>();
    	Scanner s = new Scanner(new BufferedReader(new FileReader(filename))); 
    	int capacity = 0;
    	int dimension = 0;
    	CVRPInputs inputs = null;
    	
    	try {    		
    		while (s.hasNextLine()) {   
    			strArr.add(s.nextLine());	
    			
    		}
	    	s.close();
	    	
	    	
	    	int nod = strArr.indexOf("NODE_COORD_SECTION");
	    	int dem = strArr.indexOf("DEMAND_SECTION");
	    	int dep = strArr.indexOf("DEPOT_SECTION");
	    	List<String> data_sec = strArr.subList(0, nod);
	    	List<String> node_sec = strArr.subList(nod+1, dem);
	    	List<String> dep_sec = strArr.subList(dem+1, dep);
	    	
	    	//add gobal details
			for(int j = 0; j <  data_sec.size();j++){			
					
	    		if(data_sec.get(j).startsWith("CAPACITY")){     			
	    			capacity = Integer.parseInt(scanString(data_sec.get(j)));		
		    		
	    		} 
	    		else if(data_sec.get(j).startsWith("DIMENSION")){     			
	    			dimension = Integer.parseInt(scanString(data_sec.get(j)));		
	    			
	    		} 
	    		
			}
			inputs = new CVRPInputs(dimension);
			inputs.setVehCap(capacity);
			//createList
			for(int i =0; i < node_sec.size(); i++ ){	    	
	    		
				
	    		List<Integer> coord = scanInt(node_sec.get(i));    			   		
    			List<Float> coord1 = scanFloat(dep_sec.get(i));
    			
        		
    			Node nodeData = new Node(coord.get(0).intValue(),coord.get(1),coord.get(2),coord1.get(1));
    		
    			
        		inputs.getNodes()[i] = nodeData;
        		
	    	}
					
		   
    	}
    	catch (NoSuchElementException e){
    	}
    	
       
       
        
        
    
    	//instantiate the solution wrapper holding the only copy of the data
    	return inputs;    	  	  
    }

    /***
     * 
     * 
    The aim is to read the three sections of the file and put thes results in different arrays
   	These arrays can then be used to build the relevant objects needed by the system.*/
       
       public static List<SolutionElements> getTSPFormat(String filename) throws FileNotFoundException {
   				
   		List<String> strArr = new ArrayList<String>();
       	Scanner s = new Scanner(new BufferedReader(new FileReader(filename))); 
       	String measure = null;
    	List<SolutionElements> output = new ArrayList<SolutionElements>();
       
       	
       	
       	try {    		
       		while (s.hasNextLine()) {   
       			strArr.add(s.nextLine());	
       			
       		}
   	    	s.close();
   	    	
   	    	//problem information
   	    	for(String line :strArr){
   	    		if(line.startsWith("EDGE_WEIGHT_TYPE")){
   	    			measure = scanString(line);
   	    			
   	    		}  	    		
   	    		
   	    	}
   	    	
   	    	//chop out the node section
   	    	
   	   	    	
   	    	//got nodes type is it
   	    	int nodeNum = strArr.indexOf("NODE_COORD_SECTION");
   	    	int endNum = strArr.indexOf("EOF");
   	    	
   	    	
   	    	for(int i = nodeNum+1; i < endNum;i++){
   	    		
   	    		String line = strArr.get(i);
   	    		
   	    		List<Integer> id = scanInt(line);
   	    		List<Float> coordinates = scanFloat(line);
   	    		NodeData node = new NodeData();
   	    		node.setMeasure(measure);
   	    		node.setId(id.get(0));
   	    		node.setX(coordinates.get(1));
   	    		node.setY(coordinates.get(2));
   	    		output.add(node);
   	    	}
   	    	
   	    	
   	    
   		   
       	}
       	catch (NoSuchElementException e){
       		e.printStackTrace();
       	}     	
          
       	//SolutionElements se = output.get(0);
       	//output.add(se);
           
           
       
       	//instantiate the solution wrapper holding the only copy of the data
       	return output;    	  	  
       }
   

	private static List<Float> scanFloat(String str){
		List<Float> result = new ArrayList<Float>();
		Scanner s = new Scanner(str);
		while(s.hasNextFloat()){
			//System.out.println("Int "+ s.nextInt());
			result.add(s.nextFloat());
		}
		s.close();
		return result;
		
	}
	
	private static List<Integer> scanInt(String str){
		List<Integer> result = new ArrayList<Integer>();
		Scanner s = new Scanner(str);
		while(s.hasNextInt()){
			//System.out.println("Int "+ s.nextInt());
			result.add(s.nextInt());
		}
		s.close();
		return result;
		
	}
	
	private static String scanString(String str){
		String result= null;
		Scanner s = new Scanner(str);
		while(s.hasNext()){
			s.next();
			s.next();
			result = s.next();
		}
		s.close();
		return result;
	}
	 public BaseInputs getJobMatrix(String filePath)
	    {
		 	BaseInputs inputs = null;
	    	try
	        {
	        	FileReader reader = new FileReader(filePath);
	            Scanner in = new Scanner(new BufferedReader(reader));
	            // The two first lines of this file should be like this:
	            //   # nJobs | nMachines 	<- line 0
	            //  	 20 		5 		<- line 1
	            in.nextLine(); // Skip line 0
	            int nJobs = in.nextInt();
	            int nMachines = in.nextInt();
	            inputs = new BaseInputs(nJobs, nMachines);
	           
	            // The rest of the lines should be like this:
	            //   # 		m0 		| 	m1	 | 		m2 		<- line 2 (times job0)
	            //   		21       	23			32		<- line 3 (times job1)
	            //   		30      	31			10		<- line 4 (times job2)
	            //   ...         ...       ...
	            in.nextLine(); // Go to the beginning of line 2
	            in.nextLine(); // Go to the beginning of line 3
	            for( int i = 0; i < nJobs; i++ )
	            {
	            	PFSPJob iJob = inputs.getJobs()[i];
	            	int totalTime = 0;
	            	int time = 0;
	            	for( int j = 0; j < nMachines; j++ )
	            	{
	                    time = in.nextInt();
	                    iJob.setProcessingTime(j, time);
	                    totalTime = totalTime + time;
	            	}
	            	iJob.setTotalProcessingTime(totalTime);
	            }
	            in.close();
	        }
	        catch (IOException exception)
	        { 
	          	System.out.println("Error processing inputs file: " + exception);
	        }
	    	return inputs;
	    }
	

}//end Input

