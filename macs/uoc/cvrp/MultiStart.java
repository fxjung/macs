package macs.uoc.cvrp;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import macs.ontologies.SolutionWrapper;
import macs.ontologies.entities.Edge;
import macs.ontologies.entities.NodeList;
import macs.ontologies.entities.Solution;
import macs.ontologies.entities.SolutionData;
import macs.ontologies.entities.problems.NodeData;
import macs.ontologies.entities.problems.SolutionElements;
import macs.uoc.api.Test;
import macs.util.OptUtility;


/**
 * Iteratively calls the RandCWS and saves the best solution.
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class MultiStart 
{
      
    
    public static SolutionData solve(Test aTest, CVRPInputs inputs, Random rng)
    {
        /* 1. Generates the CWS solution */
        long start = ElapsedTime.systemTime();
        VRPSolution cwsSol = RandCWS.solve(aTest, inputs, rng, true);
        RouteCache cache = new RouteCache();
        double elapsed = ElapsedTime.calcElapsed(start, ElapsedTime.systemTime());
        cwsSol.setTime(elapsed);
       
        VRPSolution bestSol = cwsSol;
       
        /* 3. Iterates calls to RandCWS */
        start = ElapsedTime.systemTime();
        elapsed = 0.0;
        while( elapsed < aTest.getMaxTime() )
        	
        {
        	VRPSolution newSol = RandCWS.solve(aTest, inputs, rng, true);
            newSol = cache.improveRoutesUsingHashTable(newSol);
            if( newSol.getCosts() < bestSol.getCosts() ) 
            {
                bestSol = newSol;
            
            }
            elapsed = ElapsedTime.calcElapsed(start, ElapsedTime.systemTime());
        }
        
        SolutionData outputs = getOutputSolution(bestSol,inputs.getVehCap());

        /* 4. Returns the CWS sol. and the best-found sol. */
        return outputs;
    }
    public static Solution solve(Solution solution, Test aTest, CVRPInputs inputs, Random rng)
    {
    	VRPSolution bestSol = RandCWS.solve(aTest, inputs, rng, false);
    	VRPSolution newSol = null;
    	
    	RouteCache cache = new RouteCache();
   
    	
        /* 1. Generates the CWS solution */
        
        
    	long start = ElapsedTime.systemTime();    
    
    	double elapsed = 0.0;
    	//startCostToFile();
    	while( elapsed < aTest.getMaxTime() )
	    	{
				newSol = RandCWS.solve(aTest, inputs, rng, true);
	      
				newSol = cache.improveRoutesUsingHashTable(newSol);
		        if( newSol.getCosts() < bestSol.getCosts() )
		        {                
		            bestSol = newSol;
		           
		        }
	        elapsed = ElapsedTime.calcElapsed(start, ElapsedTime.systemTime());
	    
            
       }
    	SolutionWrapper.getInstance().setLocalOpt(bestSol.getCosts());
        Solution outputs = getResultSolution(bestSol,solution,false);

        /* 4. Returns the CWS sol. and the best-found sol. */
        return outputs;
    }
    
    private static Solution getResultSolution(VRPSolution vsol,Solution  solution,boolean finalrun){
    
		Solution result = new Solution();	
		List<Integer> list = new ArrayList<Integer>();
		List<Edge> edges = new ArrayList<Edge>();
		LinkedList<Route> routes = vsol.getRoutes();
			for(Route aRoute : routes){
				NodeList nl = new NodeList();
				LinkedList<Integer> froute = new LinkedList<Integer>();
				for(VRPEdge edge : aRoute.getEdges()){
					Edge e = new Edge();
					Node firstNode = edge.getOrigin();
					Node lastNode = edge.getEnd();
					
					int first = firstNode.getId();
					int last = lastNode.getId();
					e.setFirst(first);
					e.setSecond(last);
					edges.add(e);
					
					if(!froute.contains(first)){
						froute.add(first);
						list.add(first);
					}
					
					if(!froute.contains(last)){
						froute.add(last);
						list.add(last);
					}
					
					
				}
				nl.setIntList(froute);
				nl.setTotalDemand(aRoute.getDemand());
				nl.setRouteCost(aRoute.getCosts());
				
				
				result.addToSolution(nl);
			}
			
			result.setEdges(edges);
			result.setList(list);
			
						
			result.setValue(vsol.getCosts());
				
			
		
		return result;
	}
    
   
    
    private static SolutionData getOutputSolution(VRPSolution vsol,float cap){
		SolutionData result = new SolutionData();
		
			
		List<Edge> eList = new LinkedList<Edge>();
		List<SolutionElements> sList = new LinkedList<SolutionElements>();
		
		for(Route aRoute : vsol.getRoutes()){
			List<Integer> list = new ArrayList<Integer>();
			NodeList nList = new NodeList();
			for(VRPEdge edge : aRoute.getEdges()){
				Edge e = new Edge();
				NodeData firstNode = nodeToNodeData(edge.getOrigin());
				NodeData lastNode = nodeToNodeData(edge.getEnd());
				firstNode.setCapacity(cap);
				lastNode.setCapacity(cap);
				
				int first = firstNode.getId();
				int last = lastNode.getId();
				e.setFirst(first);
				e.setSecond(last);
				
				if(!list.contains(first))
					list.add(first);
				if(!list.contains(last))
					list.add(last);
				
				if(!sList.contains(firstNode))
					sList.add(firstNode);
				if(!sList.contains(lastNode))
					sList.add(lastNode);
				eList.add(e);
				
			}
			
			nList.setIntList(list);
			
			result.addToNodeList(nList);
			
		}
		result.setDepot(sList.get(0).getId());
		result.setNodes(sList);
		result.setHeuristics(eList);
		result.setValue(vsol.getCosts());
		
		return result;
	}
    private static NodeData nodeToNodeData(Node node){
    	NodeData nodeData = new NodeData(node.getId(),node.getX(),node.getY(),node.getDemand());    	
    	return nodeData;
    	
    }
    public static void writeCostToFile(VRPSolution bestSol,int number,double time){
    	
		
		String filename = "/home/smartin/workspace/maca/output/cwsOutput#2.csv";
				
		
		String newline = System.getProperty("line.separator");
		//Random Access version
		try{
			
			File f = new File(filename);
			
			// Check file already exists 
			if(f.exists()){
				long fileLength = f.length();
	            RandomAccessFile raf = new RandomAccessFile(f, "rw");
	            raf.seek(fileLength);
	          
	           
	            //value
	            raf.writeBytes(time+","+OptUtility.roundTwoDecimals(bestSol.getCosts()) + newline);
		 
		        
				//Close the output stream
	        	 raf.close();  
				
				
			}
			//First time version
			else {
				// Create file 
				FileWriter fstream = new FileWriter(filename);
		        BufferedWriter out = new BufferedWriter(fstream);
		        
		       
		        
		        //value
	        	out.write(time+","+OptUtility.roundTwoDecimals(bestSol.getCosts()) + newline);
	        	
		        
				//Close the output stream
		        out.close(); 
			}
		}
			
		catch (Exception e){//Catch Assignmentexception if any
			System.err.println("Error: " + e.getMessage());
		}
    }
	public static void startCostToFile(){
		
		
		String filename = "/home/smartin/workspace/maca/output/cwsOutput#2.csv";
				
		
		String newline = System.getProperty("line.separator");
		//Random Access version
		try{
			
			File f = new File(filename);
			
			// Check file already exists 
			if(f.exists()){
				long fileLength = f.length();
	            RandomAccessFile raf = new RandomAccessFile(f, "rw");
	            raf.seek(fileLength);
	          
	           
	            //value
	            raf.writeBytes("Problem: "+ newline);
		 
		        
				//Close the output stream
	        	 raf.close();  
				
				
			}
			//First time version
			else {
				// Create file 
				FileWriter fstream = new FileWriter(filename);
		        BufferedWriter out = new BufferedWriter(fstream);
		        
		       
		        
		      //value
	            out.write("Problem: "+ newline);
	        	
		        
				//Close the output stream
		        out.close(); 
			}
		}
			
		catch (Exception e){//Catch Assignmentexception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
