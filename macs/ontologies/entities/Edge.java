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

import macs.agents.AgentVocabulary;
import macs.ontologies.SolutionWrapper;
import macs.ontologies.entities.problems.SolutionElements;
import macs.ontologies.vocabularies.EdgeVocabulary;
import macs.ontologies.vocabularies.NodeListVocabulary;
import macs.ontologies.vocabularies.SolutionDataVocabulary;
import macs.util.OptUtility;
import jade.content.AgentAction;
import jade.content.Concept;



public class Edge implements SolutionDataVocabulary, NodeListVocabulary, EdgeVocabulary, Concept, AgentAction,AgentVocabulary {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7359896867604868331L;
	protected  int first = -1;
	protected  int second = -1;
	protected  int score = 0;
	protected  double costs = -1;
	protected  double improvement;
	protected  int t4;
	
	
	
	public Edge(){}
	
	public Edge(int first, int second){
		setFirst(first);
		setSecond(second);
	}
	
	public void setCosts(double gi){
		this.costs = gi;
		//distance = OptUtility.getEuclidanDistance(nodelist.get(0), nodelist.get(1));
	}
	public void setFirst(int first){
		this.first = first;
	}
	public void setSecond(int second){
		this.second = second;
	}
	
	public void setScore(int value){ 		
		this.score = value;
	}
	public void setImprovement(double improvement){
		this.improvement = improvement;
	}
	public void setSequentialEdge(int t4){	
		this.t4 = t4;
	}
	
	public void incrementScore(){
		score++;
	}

	public int getFirst(){
		return this.first;
	}
	
	public int getSecond(){
		return this.second;
	}
	
	public double getCosts(){
		return this.costs;
	}
	
	public int getScore(){
		return this.score;
	}	
	public double getImprovement(){
		return this.improvement;
	}
	public int getSequentialEdge(){
		return this.t4;
	}
	
	
	public boolean equalsCompare(Edge that){
		boolean result = false;
		result = (this.getSecond() == that.getFirst());
		
		return result;
	}
	
	public boolean equalsFirst(Edge that){
		boolean result = false;
		result = (this.getFirst() == that.getSecond());
		
		return result;
	}
	public boolean equalsSecond(Edge that){
		boolean result = false;
		result = (this.getSecond() == that.getFirst());
		
		return result;
	}
	public boolean equalsElement(Edge that){
		boolean result = false;
		if(equalsFirstElement(that)){
			result = true;
		}
		else if (equalsSecondElement(that)){
			result = true;
		}
		return result;
	}
	public boolean equalsFirstElement(Edge that){
		boolean result = false;
		if(this.getFirst() == that.getFirst()&&this.getSecond() != that.getSecond()){
			result = true;
		}		
		return result;
	}
	
	public boolean equalsSecondElement(Edge that){
		boolean result = false;
		if(this.getSecond() == that.getSecond()&&this.getFirst() != that.getFirst()){
			result = true;
		}		
		return result;
	}
	
	public double calcCosts(){		
		SolutionElements fNode = SolutionWrapper.getInstance().getNode(getFirst());
		SolutionElements sNode = SolutionWrapper.getInstance().getNode(getSecond());
		setCosts(OptUtility.geDecEuclidanDistance(fNode, sNode));
		return getCosts() ;	
		
	}
	
	@Override 
	public boolean equals(Object other) {
      boolean result = false;
        if (other instanceof Edge) {
        	Edge that = (Edge) other;
            result = (this.getFirst()==that.getFirst()&&this.getSecond()==that.getSecond());
        }
        return result;
    }
	
	

		
	
	
	
	
	
    /*@Override public int hashCode() {
        return (41 * (41 + getFirst()) + (41+getSecond()) + (41+getScore()) + getDistance());
    }*/

    
    
}
