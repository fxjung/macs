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
package macs.ontologies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import macs.agents.AgentVocabulary;
import macs.comparitors.EdgeCompare;
import macs.ontologies.entities.Edge;
import macs.ontologies.entities.NodeList;
import macs.ontologies.entities.problems.NodeData;
import macs.ontologies.entities.problems.SolutionElements;
import macs.uoc.cvrp.CVRPInputs;
import macs.uoc.pfsp.api.PFSPInputs;



/**
 * @author simon martin
 *
 */
final public class SolutionWrapper implements AgentVocabulary{
	
	private List<SolutionElements> nodes;
	private Set<Integer> list = new HashSet<Integer>(); //these are the column headings
	private List<NodeList> bestListSoFar;
	private double bestValueSoFar=0;
	private double value =0.0;
	//Set List pair to ensure a unique Edge pool	
	private SortedSet<Edge> poolSet;
	
	
	private boolean initiator = false;
	private List<String> agents;
	private int problem = 0;
	private int solutionsize = 0;
	private int optcount = 0;
	private List<Double> localopt;
	private int conversations = 0;
	private int poolSize = 0;
	private String jobName = null;
	private float capacity = 0;
	private int depot = 0;
	private CVRPInputs CVRPinputs;
	private PFSPInputs PFSPinputs;
	private List<Edge> edges = null;
	private String agentName = null;
	
	
    /** SingletonHolder is loaded on the first execution of Singleton.getInstance() 
    * or the first access to SingletonHolder.INSTSortedSetANCE, not before.
    */
   final private static class  SolutionWrapperHolder {	  
     private static final SolutionWrapper INSTANCE = new SolutionWrapper();
   }
   
   final public static synchronized SolutionWrapper getInstance() {
	   
     return SolutionWrapperHolder.INSTANCE;
   }
   
   private SolutionWrapper(){
	
		//list = Collections.synchronizedSet(new HashSet<Integer>());
		nodes = Collections.synchronizedList(new ArrayList<SolutionElements>());
		poolSet =  Collections.synchronizedSortedSet(new TreeSet<Edge>(new EdgeCompare()));
		agents = Collections.synchronizedList(new ArrayList<String>());
		bestListSoFar = Collections.synchronizedList(new ArrayList<NodeList>());
		localopt = Collections.synchronizedList(new ArrayList<Double>());
		edges = Collections.synchronizedList(new ArrayList<Edge>());
		
			
		
   }

   /**
    * reduces the risk of cloning. This class is a singleton
    */
   public Object clone()throws CloneNotSupportedException
	{
	    throw new CloneNotSupportedException(); 
	}    

	

	public synchronized String getJobName(){
		return this.jobName;
	}




	public synchronized SolutionElements getNode(int name){
		
		
		SolutionElements node = null;
		
		for(SolutionElements nd : nodes){
			if(nd.getId() == name){
				node = nd;
				break;
			}
		}
		return node;	
	}

	
	
	
	public synchronized List<String> getAgents(){
		return this.agents;
	}


	public synchronized double getValue(){
		return this.value;
	}
	
	public synchronized List<SolutionElements> getNodes(){
		return nodes;
	}

	public synchronized boolean getInitiator(){
		return initiator;
	}


	public synchronized int getSolutionSize() {
		return solutionsize;
	}
	public synchronized String getAgentName(){
		return agentName;
	}
	public synchronized Set<Edge> getPool(){
		return poolSet;
	}
	
    public synchronized int getProblem(){
    	return problem;
    }
    

   
  
    public List<Double> getLocalOpt(){
 	   return this.localopt;
    }
    public int getOptCount(){
 	   return this.optcount;
    }
	
 
	/**
	 * This function checks the flag for each node 
	 * @return true if active list still has active members - false if not.
	 */
	public synchronized boolean isActiveList(){
		int count = 0;
		boolean result = false;
		
		//count false nodes. Find true ones. Break if true node found
		for(int i = 1; i < getSolutionSize()-1;i++ ){
			NodeData node = (NodeData)getNode(i);
			if(node.getFlag()== false){
				count++;				
			}
			else {
				result = true;
				break;
			}
		}
		// if all nodes false return true
		if(count == getSolutionSize()-1){
			result = false;
		}
		return result;
		
	}
	
	public synchronized void setSolutionDataNodes (List<SolutionElements> nodes){
		setNodes(nodes);
		setSolutionSize(nodes.size());
		
	}

	public void setLocalOpt(double value2){
		this.localopt.add(value2);
	}
	public void setEdgeList(Edge edge){
		
		this.edges.add(edge); 
	}
	public void setEdgeLists(List<Edge> edge){
		
		this.edges.addAll(edge); 
		
	}
	public void setConversations(int conversations){
		this.conversations = conversations;
	}
	public void setLocalOpt(List<Double> localopt){
		this.localopt = localopt;
	}
	public void setOptCount(){
		this.optcount = optcount+1;
		
	}

	
		
	public synchronized void setSolutionSize(int size){
		solutionsize = size;
	}
	
	public synchronized void setCapacity(){
		this.capacity = ((NodeData)getNodes().get(0)).getCapacity();
	}
	

	public synchronized List<Integer> getList(){
		return new ArrayList<Integer>(this.list);
	}
	
	public synchronized int getPoolSize(){
		return this.poolSize;
	}
	
	public int getConversations(){
		return this.conversations;
	}
	
	public synchronized List<Edge> getEdgeList(){
		return this.edges;
	}
	public synchronized double getBestValueSoFar(){
		return this.bestValueSoFar;
	}
	public synchronized List<NodeList> getBestListSoFar(){
		return this.bestListSoFar;
	}
	

	public synchronized void setNodes(List<SolutionElements> nd){		
		this.nodes = nd;
	}
	public synchronized  void setAgentName(String agentName){
		this.agentName = agentName;
	}
	

	public synchronized void setProblem(int problem){
		this.problem = problem;
	}
	public synchronized void setAgents(List<String> agents){
		this.agents = agents;
	}
	public synchronized void setPool(List<Edge> list){
		for(Edge edge : list){
			if(poolSet.size()<getPoolSize()){
				poolSet.add(edge);
				setEdgeList(edge);
				//poolList.add(edge);
			}
		}
		
	}
	/**
	 *  @param newList
	 *  we add the elements of a list to the pool 
	 *  if the pool is less than set size the just add element
	 *  if pool max size already reached, only add new element if the candidate elhttps://www.google.co.uk/?gws_rd=sslement has a matching first or second element 
	 *  and the distance is improving
	 */
	public synchronized void addAllStrightToPool(List<Edge> newList){	
		for(Edge newEdge: newList){			
			addStraightToPool(newEdge);		
			setEdgeList(newEdge);
		}		
	}

	/**
	 *  @param newEdge
	 *  we add a new element to the pool. The pool is a size limited LIFO queue with only unique elements
	 *  if the pool does not have the element and the pool is not up to size limit then add
	 *  if pool max size already reached, only add new element if unique and remove the first element
	 *  and the distance is improving
	 */
	public synchronized void addStraightToPool(Edge newEdge){
		
		if(poolSet.size()<getPoolSize()){
			poolSet.add(newEdge);
			setEdgeList(newEdge);
			
		
		}
		else if(poolSet.size()==getPoolSize()){
						
			if(poolSet.add(newEdge)){
				
				setEdgeList(newEdge);
		
			
			Edge remEdge;
			remEdge = poolSet.first();
			if(remEdge.equals(newEdge)){
				remEdge = poolSet.last();
			}
			poolSet.remove(remEdge);
		
			
			}
				
			
		}	
	}

	
	
	/**
	 *  @param newList
	 *  we add the elements of a list to the pool 
	 *  if the pool is less than set size the just add element
	 *  if pool max size already reached, only add new element if the candidate element has a matching first or second element 
	 *  and the distance is improving
	 */
	public synchronized void addAllReplaceToPool(List<Edge> newList){	
		for(Edge newEdge: newList){			
			addReplaceToPool(newEdge);
			
		}		
	}
	
	/**
	 *  @param newEdge
	 *  we add a new element to the pool. The pool is a size limited LIFO queue with only unique elements and a replacement strategy
	 *  if the pool is less than set size the just add element
	 *  if pool max size already reached, only add new element if the candidate element has a matching first or second element 
	 *  and the distance is improving
	 */
	public synchronized void addReplaceToPool(Edge newEdge){
		
		if(poolSet.size()<getPoolSize()){
			
			this.poolSet.add(newEdge);
			
		}
		else if(poolSet.size()==getPoolSize()){
						
			LinkedList<Edge> setCopy = new LinkedList<Edge>(poolSet);
			ListIterator<Edge> it = setCopy.listIterator();
			
			
			while(it.hasNext()){
				Edge poolEdge = it.next();
				if(poolEdge.equalsElement(newEdge)){					
					it.set(newEdge);
				}
			}
			poolSet.clear();
			poolSet.addAll(setCopy);
					
			
		}
		
		setEdgeList(newEdge);
	}
	public synchronized void removeFromPool(Edge heuristic){		
		this.poolSet.remove(heuristic);
	}

	
	public synchronized void setList(Collection<Integer> list){
		
		
		this.list = new HashSet<Integer>(list);
	}
	
	
	
	
	public synchronized void setBestSoFar(List<NodeList> list2, double d){
		if(this.bestValueSoFar == 0){
			this.bestListSoFar = list2;
			this.bestValueSoFar = d;
			
		} 
		else if(this.bestValueSoFar >= d){
			
			this.bestListSoFar = list2;
			this.bestValueSoFar = d;
		}
	}
	
	
	public synchronized void setValue(double value){
		this.value = value;
	}
		


	public synchronized void setPoolSize(int poolsize){
		this.poolSize = poolsize;
	}
   

	public synchronized void setJobName(String jobName){
		this.jobName = jobName;
	}
	
	
	/*public synchronized void setObjFunction(int objFunction){
		this.objFunction = objFunction;
	}*/

	public synchronized void addToNodes(List<NodeData> newNodes){
		this.nodes.addAll(newNodes);
	}
	
	public synchronized int checkSize(int size){
		int result = -1;
		if(solutionsize <= size){
			result = size+1;
			solutionsize = size+1;
		}
		else if (solutionsize > size){
			result = solutionsize;
		}
		return result;
		
	}
	public synchronized int getDepot(){
		return this.depot;
		
	}
	public synchronized NodeData getDepotNode(){
		
		return (NodeData)getNode(this.depot);
		
	}
	public synchronized void setDepot(int depot){
		 this.depot = depot;
		
	}
	public synchronized void setVRPInputs (CVRPInputs CVRPinputs){
		this.CVRPinputs = CVRPinputs;
	}
 
    public synchronized CVRPInputs getVRPInputs() { 
    	return this.CVRPinputs;
    }
    
    public synchronized void setPFSPInputs (PFSPInputs PFSPinputs){
		this.PFSPinputs = PFSPinputs;
	}
 
    public synchronized PFSPInputs getPFSPInputs() { 
    	return this.PFSPinputs;
    }
 
    public void reset(){
   	 this.nodes.clear();
   	 this.value= 0;
   	 this.poolSet.clear();
   	 this.initiator = false;
   	 this.agents.clear();
   	 this.problem = 0;
   	 this.bestListSoFar.clear();
   	 this.bestValueSoFar = 0;
   	 this.localopt.clear();
   	 this.optcount = 0;
   	 this.poolSize = 0;   	
   	 this.jobName = null;
   	 this.depot =0;
   	 this.CVRPinputs = null;
   	 this.PFSPinputs = null;
   	 this.capacity = 0;
   	 this.edges.clear();
   	 this.agentName = null;
   	
   }
  
	

	
}//end SolutionWrapper
