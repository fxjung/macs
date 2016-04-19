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
package macs.behaviour;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jade.core.behaviours.OneShotBehaviour;
import macs.agents.AgentVocabulary;
import macs.agents.LaunchState;
import macs.ontologies.entities.Edge;
import macs.ontologies.entities.NodeList;
import macs.ontologies.entities.SolutionData;
import macs.util.OptUtility;



public class OutputBehaviour extends OneShotBehaviour implements
		AgentVocabulary {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7405485277751273635L;
	private LaunchState as;
	private List<SolutionData> resultlist;
	private SolutionData startsolution;
	private int jobname;
	private String directory;
	private String filename;
	private int agentcount = 0;
	private int problem = 0;
	
	
	public OutputBehaviour(String directory, String filename, int problem,SolutionData startsolution, List<SolutionData> resultlist, LaunchState as, int jobname,int agentcount,int countNumber,boolean multipule){
		this.as = as;
		
		this.startsolution = startsolution;
		this.jobname = jobname;
		this.directory = directory;
		this.agentcount = agentcount;
		this.resultlist = resultlist;
		this.filename = filename;
		this.problem = problem;	
		
	
	}
	public OutputBehaviour(String directory, String filename, int problem,SolutionData startsolution, List<SolutionData> resultlist, int jobname,int agentcount,int countNumber,boolean multipule){
		
		
		this.startsolution = startsolution;
		this.jobname = jobname;
		this.directory = directory;
		this.agentcount = agentcount;
		this.resultlist = resultlist;
		this.filename = filename;
		this.problem = problem;	
		
	
	}

	@Override
	public void action() {
		
		//1. sort the result list the first item is the smallest. Get the final result
		Collections.sort(resultlist, new SolutionDataCompare());
		SolutionData finalResult = resultlist.get(0);
		
		//2. Go through the results fromt the different agents get the egdes found by each agent 
		//and associate them with the agent that found them. Do the same for search progress of objective function values
		Map<String,List<Edge>> edgeList = new HashMap<String,List<Edge>>(); // Hash table of agent and edges
		Map<String,List<Double>> valueList = new HashMap<String,List<Double>>(); // Hash table of agent and edges
		
		for (SolutionData results : resultlist ){
			if(!results.getEdgeList().isEmpty()){
				edgeList.put(results.getAgentName(), results.getEdgeList());
			}
			
			if(!results.getLocalOpt().isEmpty()){
				valueList.put(results.getAgentName(), results.getLocalOpt());
			}
		}
		
		
		//3. Display the best result and value	
	    for(NodeList nl :finalResult.getNodeList()){
	    	if(!nl.getIntList().isEmpty())
	    	System.out.println("FINAL RESULT " + nl.getIntList());
	    }
		System.out.println("FINAL RESULT " + OptUtility.roundTwoDecimals(finalResult.getValue()));		
		System.out.println("File name " + filename);
		
	
		//4. Build the name of the output file
		String name = filename; //name of the instance that was solved
		String problemDir = getProblem(problem);
		
				
		//5.find out which operating system so to use the correct file partitioning
		Properties prop = System.getProperties();
		String fs = prop.getProperty("file.separator");
		
		//6. build the file names depending on how many agents where used in the test
		
		filename = directory+"maca"+fs+"output"+fs+problemDir+fs+"agent"+agentcount+fs+name;
				
		//7. Write the file. It should be a one off but the facility is for the file to be random access
		// if needed
		String newline = System.getProperty("line.separator");
		//Random Access version
		try{
			
			File f = new File(filename);
			
			// Check file already exists 
			if(f.exists()){
				long fileLength = f.length();
	            RandomAccessFile raf = new RandomAccessFile(f, "rw");
	            raf.seek(fileLength);
	            
	            //outout the instance name
	            raf.writeBytes("Job Name: " + jobname+newline);
		        //output Seed solution
	            raf.writeBytes("Start Solution "+newline);
	            for(NodeList nl :startsolution.getNodeList()){
		         	if(!nl.getIntList().isEmpty())
		        	raf.writeBytes(nl.getIntList() + newline );				    	
			    }
	            //value
	            raf.writeBytes(OptUtility.roundTwoDecimals(startsolution.getValue()) + newline );
		        
		        //Output the finale solution and value
	            raf.writeBytes("Final Solution "+newline);
		        for(NodeList nl :finalResult.getNodeList()){
		         	if(!nl.getIntList().isEmpty())
		         		raf.writeBytes(nl.getIntList() + newline );				    	
			    }
		       //value
		        raf.writeBytes(OptUtility.roundTwoDecimals(finalResult.getValue()) + newline );
	        	
	        	//Execution Time
		        raf.writeBytes("Execution time: " +finalResult.getTime());
		        raf.writeBytes(newline+newline);
	        	
	        	//Output the search Progress for each agent
		        if(!valueList.isEmpty()){
		        raf.writeBytes("Search Progress " + newline);
	        	 for(String agent : valueList.keySet()){
	        		 raf.writeBytes(agent + newline);
	        		 for(Double value : valueList.get(agent)){
	        			double val = OptUtility.roundTwoDecimals(value);
	        			raf.writeBytes(new Double(val).toString()+" ");
	        		 }
	        		 raf.writeBytes(newline);
	        	 }
	        	 raf.writeBytes(newline);
		        }
	        	//Output the edges found per agent
		        if(!edgeList.isEmpty()){
	        	 raf.writeBytes("Edges found during search"+newline);
	        	 for(String agent : edgeList.keySet()){
	        		 raf.writeBytes(agent + newline);	        		
	        		 for(Edge edge : edgeList.get(agent)){        			
	        			 raf.writeBytes(edge.getFirst()+","+edge.getSecond()+" ");
	        		 }
	        		 raf.writeBytes(newline);
	        	 }
	        	 raf.writeBytes(newline);
		        }
				//Close the output stream
	        	 raf.close();  
				
				
			}
			//First time version
			else {
				// Create file 
				FileWriter fstream = new FileWriter(filename);
		        BufferedWriter out = new BufferedWriter(fstream);
		        
		        //output the Instance name
		        out.write("Job Name: " + jobname+newline);
		        //output Seed solution
		        out.write("Start Solution "+newline);
		        for(NodeList nl :startsolution.getNodeList()){
		         	if(!nl.getIntList().isEmpty())
		        	out.write(nl.getIntList() + newline );				    	
			    }
		        //value
	        	out.write(OptUtility.roundTwoDecimals(startsolution.getValue()) + newline );
	        	
		        //Output the finale solution and value
		        out.write("Final Solution "+newline);
		        for(NodeList nl :finalResult.getNodeList()){
		         	if(!nl.getIntList().isEmpty())
		        	out.write(nl.getIntList() + newline );				    	
			    }
		       //value
	        	out.write(OptUtility.roundTwoDecimals(finalResult.getValue()) + newline );
	        	
	        	//Execution Time
	        	out.write("Execution time: " +finalResult.getTime());
	        	out.write(newline+newline);
	        	
	        	//Output the search Progress for each agent
		        if(!valueList.isEmpty()){
		        	out.write("Search Progress " + newline);
	        	 for(String agent : valueList.keySet()){
	        		 out.write(agent + newline);
	        		 for(Double value : valueList.get(agent)){
	        			double val = OptUtility.roundTwoDecimals(value);
	        			out.write(new Double(val).toString()+" ");
	        		 }
	        		 out.write(newline);
	        	 }
	        	 out.write(newline);
		        }
		        
	        	//Output the edges found per agent
		        if(!edgeList.isEmpty()){
		        	out.write("Edges found during search"+newline);
	        	 for(String agent : edgeList.keySet()){
	        		 
	        		 out.write(agent + newline);	        		
	        		 for(Edge edge : edgeList.get(agent)){   
	        			
	        			out.write(edge.getFirst()+","+edge.getSecond()+" ");
	        			
	        		 }
	        		 
	        		 out.write(newline);
	        	 }
	        	 out.write(newline);
		        }
		        
				//Close the output stream
		        out.close(); 
			}
		}
			
		catch (Exception e){//Catch Assignmentexception if any
			System.err.println("Error: " + e.getMessage());
		}
			
		
	}
	
			

	
	


	
	/*
	 * Simple method to convert the problem value as an int into a string 
	 * So that it can be used as a directory name in the output file name
	 */
	private String getProblem(int value){
		String result = null;
		switch(value){
		case PFSP:
			result = "PFSP";
			break;
		
		case VRP:
			result = "CVRP";
			break;
		
		}
		return result;
	}
	
	
	
	

	
	
	
	public int onEnd(){
		try {
			as.launchState(0,null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.onEnd();
		
	}
	public class SolutionDataCompare implements Comparator<SolutionData> {


		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */

		@Override
		public synchronized int compare(SolutionData o1, SolutionData o2) {
			
			// TODO Auto-generated method stub
				int value = 0;
				try{
				if(o1.getValue() > o2.getValue()){
						value = 1;
					}
					else if (o1.getValue() < o2.getValue()){
						value = -1;
					}
					else {
						value = 0;
					}
				}
				catch(NullPointerException ne){
					ne.printStackTrace();
				}
				return value;
			}
			  
		}//end SolutionDataCompare
	

	
	

	
}
