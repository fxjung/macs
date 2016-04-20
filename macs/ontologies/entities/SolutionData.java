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

import jade.content.AgentAction;
import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;







import macs.agents.AgentVocabulary;
import macs.ontologies.entities.NodeList;
import macs.ontologies.entities.problems.NodeData;
import macs.ontologies.entities.problems.SolutionElements;
import macs.ontologies.vocabularies.EdgeVocabulary;
import macs.ontologies.vocabularies.NodeListVocabulary;
import macs.ontologies.vocabularies.SolutionDataVocabulary;



/**
 * @author simon
 *
 */
public class SolutionData implements SolutionDataVocabulary, NodeListVocabulary,EdgeVocabulary, Concept, AgentAction,AgentVocabulary {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7188162370999172521L;

	private List<SolutionElements> nodes;
	
	private List<Integer> list; //these are the column headings
	private List<Integer> permutation; // this is the flowshop matrix
	//private SimpleMatrix matrix;
	private List<NodeList> nodelist;
	
	
	private double value =0;
	
	private boolean flag = false;
	private List<Edge> heuristics;
	private List<Edge> linkedheuristics;
	private List<Edge> edges;
	private boolean initiator = false;
	private List<String> agents;
	private int problem = 0;
	private int row = 0; //rows
	private int column = 0; // columns
	private String time = "0";
	private List<Double> localopt;
	private int optcount = 0;
	private int conversations = 0;
	private int depot = -1;
	private String agentname = null;

	private int objectiveFunction;
	private List<NodeData> newNodes;
	private String jobName = null;
	private double temp;
	private List<Integer>objectives;
	
	 
	
	
	
	
 	
	public SolutionData(){
		list = new ArrayList<Integer>();
		nodes = new ArrayList<SolutionElements>();
		heuristics = new ArrayList<Edge>();
		linkedheuristics = new ArrayList<Edge>();
		agents = new ArrayList<String>();
		permutation = new ArrayList<Integer>(); 
		//matrix = new SimpleMatrix();
		localopt = new ArrayList<Double>();		
		nodelist = new ArrayList<NodeList>();
		edges = new ArrayList<Edge>();
		
	}
	
	public List<SolutionElements> getNodes(){
		return nodes;
	}	
	
	public List<Integer> getList(){
		return list;
	}
	public List<Integer>getPermutation(){
	
		return this.permutation;
	}
	/*public SimpleMatrix getMatrix(){
		return matrix;
	}*/
	public List<Edge> getHeuristics(){
		return heuristics;
	}
	public List<Edge> getLinkedHeuristics(){
		return linkedheuristics;
	}
	
	public double getValue() {
		return value;
	}
	public List<NodeData> getNewNodes(){
		return this.newNodes;
	}
	public String getTime(){
		return this.time;
	}
	public double getTemp(){		
		return this.temp;
	}
	
	public int getProblem(){
		return this.problem;
	}
	public List<String> getAgents(){
		return this.agents;
	}
	public String getAgentName(){
		return agentname;
	}
	public int getConversations(){
		return conversations;
	}
	public boolean getFlag(){
		return flag;
	}
	public boolean getInitiator(){
		return initiator;
	}
	public List<NodeList> getNodeList(){
		return this.nodelist;
	}
	public int getRow(){
		return this.row;
	}
	public int getDepot(){
		return this.depot;
	}
	public int getColumn(){
		return this.column;
	}
	
	public List<Integer> getObjectives(){
		return this.objectives;
	}
	public int getObjective(int i){
		return this.objectives.get(i);
	}
	public List<Edge>  getEdgeList(){
		return this.edges;
	}
	public String getJobName(){
		return this.jobName;
	}
	
	public void setNodes(List<SolutionElements> nodes){		
		this.nodes = nodes;
	}
	public void setJobName(String jobName){
		this.jobName = jobName;		
	}
	public void setEdgeList(List<Edge> edges){
		
			this.edges = edges;
		
	}
	
	public void setList(List<Integer> list){		
		this.list = list;
	}
	public void setTemp(double temp){		
		this.temp = temp;
	}
	
	public void setPermutation(List<Integer> permutation){
		this.permutation = permutation;
		
	}
	public void setNewNodes(List<NodeData> newNodes){
		this.newNodes = newNodes;
	}
	public void setNodeList(List<NodeList> nList){
		this.nodelist = nList;
	}
	public void addToNodeList(NodeList nList){
		this.nodelist.add(nList);
	}
	public void setValue(double value){
		this.value = value;
	}
	public void setObjectives(List<Integer> objectives){
		this.objectives = objectives;
	}

	public void setFlag(boolean flag){
		this.flag = flag;
	}
	public void setInitiator(boolean initiator){
		this.initiator = initiator;
	}
	public void setAgents(List<String> agents){
		this.agents = agents;
	}
	
	public void setProblem(int problem){
		this.problem = problem;
	}
	public void setConversations(int conversations){
		this.conversations= conversations;
	}
	public void setAgentName(String agentName){
		this.agentname = agentName;
	}
    
	public  void setRow(int row){		
		this.row = row;	
	}
	public  void setDepot(int depot){		
		this.depot = depot;	
	}
	public void setTime(String time){
		this.time = time;
	}
	public  void setColumn(int column){		
		this.column = column;	
	}
		
	/*public void setMatrix(SimpleMatrix matrix){
		this.matrix = matrix;
	}*/
	public void setNode(SolutionElements node){
		
		nodes.add(node);	
	}
	public void setLocalOpt(List<Double> list2){
		this.localopt = list2;
	}
	public void setOptCount(int optCount){
		this.optcount=optCount;
		
	}
	
	
	
	public void setObjectievFunction(int objectiveFunction){
		this.objectiveFunction = objectiveFunction;
	}
	
	public void setValueAt(int i, int j,int value) {
        if (i >= row || j >= column || i < 0 || j < 0) 
            throw new IllegalArgumentException(
                    "index (" + i + ", " +j +") out of bounds");
       permutation.add(i * column + j,value);        
	}
	
	public int getListValue(int val){
		return list.get(val);
	}
	public int getObjectievFunction(){
		return objectiveFunction;
	}
	public void setHeuristics(List<Edge> heuristics){
		this.heuristics = heuristics;
	}
	public void setLinkedHeuristics(List<Edge> linkedheuristics){
		this.linkedheuristics = linkedheuristics;
	}
	public int getValueAt(int i, int j) {
        if (i >= row || j >= column || i < 0 || j < 0) 
            throw new IllegalArgumentException(
                    "index (" + i + ", " +j +") out of bounds");
        int  value = permutation.get(i * column + j);
         return value != 0 ? value : 0;
	}
    
    
   public List<Double> getLocalOpt(){
	   return this.localopt;
   }
   public int getOptCount(){
	   return this.optcount;
   }


  

}
