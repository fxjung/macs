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
package macs.ontologies.entities;





import jade.content.AgentAction;
import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

import macs.agents.AgentVocabulary;
import macs.ontologies.entities.problems.SolutionElements;
import macs.ontologies.vocabularies.EdgeVocabulary;
import macs.ontologies.vocabularies.NodeListVocabulary;
import macs.ontologies.vocabularies.SolutionDataVocabulary;



public class NodeList implements SolutionDataVocabulary, NodeListVocabulary,EdgeVocabulary, Concept, AgentAction,AgentVocabulary {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double totaldemand;
	private double routecost;
	private List<Integer> intList;
	private int nodelistname =0;
	private List<Edge> edges = null;
	
	
	
	public NodeList(){	
		intList = new ArrayList<Integer>();
		edges = new ArrayList<Edge>();
	}
	public NodeList(int row, int column){
		
		
	}
	
	
	
	public NodeList(List<SolutionElements> nodes,double tCapacity, double saving,double cost){
	
		setTotalDemand(totaldemand);
		setRouteCost(routecost);
		
	}
	
	
	public void setNodeListName(int nodelistname){
		this.nodelistname = nodelistname;
	}	
	
	public void setTotalDemand(double totaldemand){
		this.totaldemand = totaldemand;
		
	}
	
	public void setIntList(List<Integer> intList){
		if(!intList.isEmpty()){
		this.intList = intList;
		}
		
	}
	public void addToEdges(Edge edge){
		if(edges == null){
			edges = new ArrayList<Edge>();
		}
		edges.add(edge);
		
	}
	 public void Edge(Edge anEdge)
	    {   if( this.edges.contains(anEdge) == false )
	    	this.edges.add(anEdge);
	    }
	    
    public void removeEdge(Edge anEdge)
    {   if( this.edges.contains(anEdge) == true )
    	this.edges.remove(anEdge);
    }
    public void removeCosts(Edge anEdge)
    {   this.routecost -= anEdge.getCosts();
    }
    public void addCosts(Edge anEdge)
    {   routecost += anEdge.getCosts();
    }

	public void substractCosts(Edge e1) {
		removeCosts(e1);
	}

	
	public double getTotalDemand(){
		return this.totaldemand;
		
	}
	
	
	public void setEdges(List<Edge> edges){
		
		this.edges = edges;
	}
		
	public void setRouteCost(double d){
		this.routecost = d;
	}
	public double getCost(){
		return this.routecost;
	}
	public List<Integer> getIntList(){
		return this.intList;
		
	}
	public int getName(){
		return this.nodelistname;
	}
		
	public List<Edge> getEdges(){
		return edges;
	}

	 
	

}
