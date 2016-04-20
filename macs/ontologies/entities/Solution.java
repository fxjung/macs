/** 
 * MACS - Multi-Agent Cooperative Search is a framework to develop cooperating agents using 
 * different Metaheuristics Copyright (C) 2016 Simon Martin. This file is part of MACS. 
 * 
 * MACS is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * 
 * MACS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with MACS. 
 * If not, see <http://www.gnu.org/licenses/>.
 */
package macs.ontologies.entities;


import java.util.ArrayList;
import java.util.List;

import macs.ontologies.entities.problems.NodeData;




/**
 * @author simon
 * simple class as data structure to hold all data pertaining to a single STR solution
 */
public class Solution implements Cloneable {
	
	/**
	 * 
	 */

	
	//Variables

	 List<NodeList> solution;
	 double value;
	 List<List<Integer>> deletions;
	 List<List<Integer>> additions;
	
	 int m; //rows
	 int n; //columns
	 String times ="0";
	 private List<Double> localopt;
	 private int optcount = 0;
	 private List<NodeData> newNodes;
	 private List<Edge> edges;
	 private List<Integer> list;
	 private List<Edge> edgeList;
	

	 
	

	// constructors
	public Solution(int listsize) {
		value = 0;	
		deletions = new ArrayList<List<Integer>>();
		additions = new ArrayList<List<Integer>>();		
		solution = new ArrayList<NodeList>(listsize);
		localopt = new ArrayList<Double>();
		edges = new ArrayList<Edge>();
		list = new ArrayList<Integer>();
			
	}
	public Solution(){
		value = 0;	
		deletions = new ArrayList<List<Integer>>();
		additions = new ArrayList<List<Integer>>();
		solution = new ArrayList<NodeList>();
		localopt = new ArrayList<Double>();
		edges = new ArrayList<Edge>();
		list = new ArrayList<Integer>();
		
	}
	public Solution(int row, int column){
		value = 0;	
		deletions = new ArrayList<List<Integer>>();
		additions = new ArrayList<List<Integer>>();
		solution = new ArrayList<NodeList>();
	
		localopt = new ArrayList<Double>();
		list = new ArrayList<Integer>();
		
	}
	
	/**
	 * Give a list of nodes set the solution list
	 * @param solution
	 */
	public void setSolution(List<NodeList> solution){
		
		this.solution = solution;		
	}
		
	public void setDeletions(List<Integer> del1,List<Integer> del2 ){
		if(deletions.size() < 3){		
			deletions.add(del1);
			deletions.add(del2);
			
		}		
		
	}
	public void setDeletions(List<Integer> del){
		deletions.add(del);
	}
	public void setDeletions(List<Integer> del1,List<Integer> del2,List<Integer> del3  ){
		if(deletions.size() < 4){		
			deletions.add(del1);
			deletions.add(del2);
			deletions.add(del3);
		}		
		
	}
	public void setDeletionList(List<List<Integer>> deletions){
		this.deletions = deletions;
	}
	
	public void setAdditions(List<Integer> add1,List<Integer> add2){
		if(additions.size() < 3){
			additions.add(add1);
			additions.add(add2);
		}
		
	}
	public void setAdditions(List<Integer> add1,List<Integer> add2,List<Integer> add3){
		if(additions.size() < 4){
			additions.add(add1);
			additions.add(add2);
			additions.add(add3);
		}
		
	}
	public void setAdditions(List<Integer> add){
		this.additions.add(add);
	}
	public void setAdditionList(List<List<Integer>> additions){
		this.additions = additions;
	}
	public void setValue(double d){
		this.value = d;
	}
	public void setNewNodes(List<NodeData> newNodes){
		this.newNodes = newNodes;
	}
	public void setList(List<Integer> list){
		this.list = list;
	}
	public void setEdgeList(List<Edge> edgeList){
		if(edgeList == null){
			edgeList = new ArrayList<Edge>();
		}
		this.edgeList = edgeList;
	}
	
	public void addToSolution(NodeList nl){
		
		solution.add(nl);
	
	}

	
	public List<List<Integer>> getDeletions(){
		return deletions;
		
	}
	
	public List<List<Integer>> getAdditions(){
		return additions;
		
	}
	public List<Edge> getEdges(){
		return edges;
		
	}
	public List<Edge> getAllEdges(){
		List<Edge> e = new ArrayList<Edge>();
		for(NodeList nl : getSolution()){
			e.addAll(nl.getEdges());
		}
		return e;
		
	}
	public List<Edge> getEdgeList(){
		return this.edgeList;
	}
	
	public void setLocalOpt(List<Double> list){
		this.localopt = list;
	}
	public void setOptCount(int optCount){
		this.optcount=optCount;
		
	}	
	
	// get values
	public List<NodeList> getSolution(){
		return solution;
	}
	
	public NodeList getFirstSolution(){
		return solution.get(0);
	}

	public double getValue(){
		double newValue = 0;
    	for(NodeList nl : getSolution()){
    		newValue = newValue + nl.getCost();   		
    		
    	}   	
    
		return newValue;
	}
	public List<NodeData> getNewNodes(){
		return this.newNodes;
	}
	
	public int getRowSize(){
	    	return this.m;
	}
    public int getColumnSize(){
    	return this.n;
    }
    public String getTimes(){
    	return times;
   } 
    public List<Integer> getList(){
    	return this.list;
    }
 
   
	    
    public void setRowSize(int m){
    	this.m =m;
    }
    public void setColumnSize(int n){
    	this.n =n;
    }
    public void setTimes(String time){
    	this.times = time;
    }
    public void setEdges(List<Edge> edges){
    	this.edges = edges;
    }
	public List<Double> getLocalOpt(){
 	   return this.localopt;
    }
    public int getOptCount(){
 	   return this.optcount;
    }
  
   
    public Object clone() {
    	
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            // This should never happen
            throw new InternalError(e.toString());
        }
    }
  

 
 
    

}//end Solution
