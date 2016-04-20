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

package macs.heuristics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import macs.agents.AgentVocabulary;
import macs.ontologies.SolutionWrapper;
import macs.ontologies.entities.Edge;
import macs.ontologies.entities.NodeList;
import macs.ontologies.entities.Solution;
import macs.ontologies.entities.SolutionData;
import macs.ontologies.entities.problems.JobData;
import macs.ontologies.entities.problems.SolutionElements;
import macs.parameters.Trough;
import macs.util.OptUtility;




	public class PatternHeuristic implements AgentVocabulary {
		
		public static Solution createNewSolution(List<Edge>linkedlist,List<Edge>unlinkedlist,Solution currentbest,boolean randomise){
			
			Solution sol = new Solution();
			NodeList nl = new NodeList();	
		
			
		
			List<Integer> therest = new ArrayList<Integer>();			
		
			
		
			if(unlinkedlist==null&&currentbest==null){
				
				if(SolutionWrapper.getInstance().getProblem()==PFSP) {
					sol.setEdges(linkedlist);					
				}
				else if (SolutionWrapper.getInstance().getProblem()==VRP){
					sol.setEdges(new ArrayList<Edge>(linkedlist));					
				}
				
			}
			else if(!unlinkedlist.isEmpty()||!linkedlist.isEmpty()){
				if(SolutionWrapper.getInstance().getProblem()==PFSP){
					linkedlist.addAll(unlinkedlist);	
					sol.setEdges(linkedlist);
								
				}
				else if (SolutionWrapper.getInstance().getProblem()==VRP){
					sol.setEdges(new ArrayList<Edge>(linkedlist));
				}
				
			}
			else{
				if (SolutionWrapper.getInstance().getProblem()==PFSP){
					List<Edge> pool = new LinkedList<Edge>(SolutionWrapper.getInstance().getPool());
					sol.setEdges(pool);
					
				}
				else if (SolutionWrapper.getInstance().getProblem()==VRP){
					
					List<Edge> pool = new LinkedList<Edge>(SolutionWrapper.getInstance().getPool());
					sol.setEdges(pool);
					
				}
				
			}
		
			
			
			nl.setIntList(therest);
			sol.addToSolution(nl);
		
			
			
			//*******************************************************************************************
			if(SolutionWrapper.getInstance().getProblem()!=VRP){
				if(sol.getSolution().size()>SolutionWrapper.getInstance().getSolutionSize()){
					
					System.out.println("WRONG SIZE " + sol.getSolution().size());
					System.out.println("WRONG SIZE LIST  " + sol.getSolution());
				
					System.exit(1);
				}
			}
			else{
				if(checkDuplicates(therest)!=-1){
					System.out.println("DUPLICATES " + checkDuplicates(therest));
					
					System.out.println("DUPLICATES  " + therest);
				
					System.exit(1);
				}
			}
					
			return sol;
			
			
				
				
		}//createNewSolution
	
	public static int checkDuplicates(List<Integer> list){
		int result = -1;
		for(Integer i : list){
			if(Collections.frequency(list, i) > 1){
				result = i;
			}
		}
		return result;
		
	}
	
	public static List<Edge> removeDuplicates(List<Edge> edges){
		ListIterator<Edge> eit = edges.listIterator();
	
		while(eit.hasNext()){
			Edge edge = eit.next();
			if(Collections.frequency(edges, edge) > 1){
				eit.remove();
			}
		}
		return edges;
	}
	/**
	 * The method takes a list of edges and tries to build a linked list of edges
	 * where the last element of one edge pair is the first of another
	 * Any edges not linked in this way are added to the back of the list
	 * @param scoreList
	 * @return
	 */
	
	public static List<Edge> getLinkedEdgeList(List<Edge> scoreList){
		Trough trough = new Trough(2,false);
		boolean test = true;
		int size = 0;
		//This is the front part of the list we will build
		//we will try to link the ranked heuristics from one if possible
		LinkedList<Edge> linkedlist = new LinkedList<Edge>();
		Set<Edge> set = new HashSet<Edge>();
		//start by looking at the heuristics. Do any follow on from each other?
		LinkedList<Edge> rankedheuristicscopy = new LinkedList<Edge>(scoreList);
		Collections.copy(rankedheuristicscopy, new ArrayList<Edge>(scoreList));
		//build linked list from heuristics if we can
		//int count = 0;
		while(!rankedheuristicscopy.isEmpty()&&test){
			
			
			for(Edge hd : scoreList){
				
				if(linkedlist.isEmpty()&&set.add(hd)){
					linkedlist.add(hd);
					
					rankedheuristicscopy.remove(hd);
					
				}
				
				if(hd.equalsFirst(linkedlist.getLast())&&set.add(hd)){
					
					linkedlist.addLast(hd);
					
					rankedheuristicscopy.remove(hd);
					
				} 
				else if(hd.equalsSecond(linkedlist.getFirst())&&set.add(hd)){
					linkedlist.addFirst(hd);
					
					rankedheuristicscopy.remove(hd);
					
				}
				else{
				
					
				}
				
				
			}//end for
			size = rankedheuristicscopy.size();
			trough.addElement(size);
			test = !trough.isTrough(size);
			//count++;
			
		}//end while
		
		return linkedlist;
	
}// end createLinkedList

	



public static List<Edge> getUnLinkedEdges(List<Edge>linkedlist,List<Edge>rankedheuristics){
	List<Edge> resultList = new LinkedList<Edge>();	
	if(linkedlist.removeAll(rankedheuristics)){
		resultList = linkedlist;
	}
	
	return resultList;	
}
	
	

public static SolutionData createNewHeuristics(Solution currentbest){
	List<Edge> edges = solutionToEdges(currentbest);	
	SolutionData output = new SolutionData();	
	output.setHeuristics(edges);	
	return output;
}
public static void setSolutionEdgesToPool(Solution currentbest){
	
	List<Edge> output = solutionToEdges(currentbest);
	SolutionWrapper.getInstance().setPool(output);
}

public static List<Edge> solutionToEdges(Solution currentbest){
	List<Edge> output = new ArrayList<Edge>();
		if(currentbest != null){
		for(NodeList nl : currentbest.getSolution()){		
			List<Integer> list = nl.getIntList();
			output = listToEdgeList(list);
		}
	}
	return output;
}
public static List<Edge> listToEdgeList(List<Integer> currentbest){
	
	Set<Edge> output = new HashSet<Edge>();	

	if(!currentbest.isEmpty()){
		
		for(int i = 0; i < currentbest.size()-1; i++){	
			Edge edge = new Edge();
			int first = currentbest.get(i);
			int second = currentbest.get(i+1);
			
			edge.setFirst(first);
			edge.setSecond(second);
			if(edge.getCosts()==-1.0)
				edge = computeCost(edge);
			output.add(edge);			
		}	
		//hamiltonian case
		Edge nedge = new Edge();
		nedge.setFirst(currentbest.get(currentbest.size()-1));
		nedge.setSecond(currentbest.get(0));
		if(nedge.getCosts()==-1.0)
			nedge = computeCost(nedge);
		
		output.add(nedge);
		
	}

	
	return new ArrayList<Edge>( output);
}
public static List<Edge> nodesToEdgeList(List<SolutionElements> currentbest,int problem){
	
	Set<Edge> output = new HashSet<Edge>();	

	if(!currentbest.isEmpty()){
		
		
		for(int i = 0; i < currentbest.size()-1; i++){	
			
			Edge edge = new Edge();
			int first = currentbest.get(i).getId();
			int second = currentbest.get(i+1).getId();			
			edge.setFirst(first);
			edge.setSecond(second);		
			edge.setCosts(OptUtility.computeSingleCost(edge, currentbest, problem));
			output.add(edge);
		}	
		//hamiltonian case
		Edge nedge = new Edge();
		nedge.setFirst(currentbest.get(currentbest.size()-1).getId());
		nedge.setSecond(currentbest.get(0).getId());
		nedge.setCosts(OptUtility.computeSingleCost(nedge, currentbest, problem));
		
		output.add(nedge);
		
	}

	
	return new ArrayList<Edge>( output);
}

/**
 * This algorithm works by taking a list of Edge objects and a comparable list of edge pairs
 * The pairs counted in the heuristic list. The idea is to find how frequently a heuristic apprars 
 * If a heuristic appears more than once it is counted and give a score which is stored with that heuristic
 * The final ranked list is sort according to these scores
 * @param hlist
 * @return
 */
public  static SolutionData compareAndRank(List<Edge> hlist){
	addGoodEdgesToPool(hlist);
		
	List<Edge>  scoreList;
	List<Edge>  unrankedList = new ArrayList<Edge>();
	List<Edge>  rankedList = new ArrayList<Edge>();
	SolutionData sd = new SolutionData();
	
	
	scoreList = new LinkedList<Edge>(SolutionWrapper.getInstance().getPool());
	if(!scoreList.isEmpty()){
		
		rankedList = getLinkedEdgeList(scoreList);	
		unrankedList = getUnLinkedEdges(rankedList,scoreList);	
		
		sd.setLinkedHeuristics(rankedList);
		sd.setHeuristics(unrankedList);	
		
	}
	return sd;
}



/**
 * Method that scores Edges found by the agents and adds them too the pool
 * If the pool does not already contain them
 * @param hlist
 */

private static void addGoodEdgesToPool (List<Edge> hlist){	
	for(Edge hd : hlist){
		int score = Collections.frequency(hlist,hd);	
		
		if(score >= SolutionWrapper.getInstance().getAgents().size()){	
			
				hd.setScore(score);	
				if(hd.getCosts()==-1)
					hd = computeCost(hd);
				SolutionWrapper.getInstance().addStraightToPool(hd);	
						
			
		}
	}
}




public static List<Integer> edgesToList(Collection<Edge> hlist){
	List<Integer> ll = new ArrayList<Integer>();
	for(Edge h : hlist){
		int first = h.getFirst();
		int second = h.getSecond();
		
		if(!ll.contains(first)){
			ll.add(first);
		}
		if(!ll.contains(second)){
			ll.add(second);
		}
	}
	return ll;
}

	public static List<Edge> getNewLinkedEdgeList(List<Edge> linked){
		SolutionWrapper.getInstance().addAllStrightToPool(linked);
		List<Edge> scoreList = new LinkedList<Edge>(SolutionWrapper.getInstance().getPool());
		List<Edge> result = null;
		if(scoreList != null){
			result = getLinkedEdgeList(scoreList);
		}
		
		
		return result;
		
	}
	public static List<Edge> getUnlinkedEdgeList(List<Edge> unlinked,List<Edge> linked){
		//try to make a linked list of the edges that we not previously linked
		List<Edge> otherEdges = getNewLinkedEdgeList(unlinked);
		List<Edge> result = new LinkedList<Edge>();
		if(otherEdges != null){
			result = getUnLinkedEdges(linked,otherEdges);
		}
		else{
			//if not possible to link return the the original list
			result = unlinked;
		}
		
	
		
		return result;
		
	}
	public static Set<Edge> getUnLinkedEdges(Set<Edge>linkedlist,Set<Edge>rankedheuristics){		
		Set<Edge> unlinkedlist = new HashSet<Edge>();
		
			for(Edge rh : rankedheuristics){
				if(!linkedlist.contains(rh)){
					unlinkedlist.add(rh);	
				}	
			}
			
		
		
		//DEBUG*******************************************
			
		//for(Edge it : unlinkedlist){
		//	System.out.println("UNLINKED LIST " + it.getFirst() + ", "+ it.getSecond());
			
		//}
		//System.out.println("LIST SIZE " + unlinkedlist.size());
		//DEBUG*******************************************
		
		return unlinkedlist;
	}
	
	
	public static Edge computeCost(Edge edge){
		double cost = 0;
		
			if(SolutionWrapper.getInstance().getProblem()==PFSP){
				JobData first = (JobData)SolutionWrapper.getInstance().getNode(edge.getFirst());
				JobData second = (JobData)SolutionWrapper.getInstance().getNode(edge.getSecond());
				List<JobData> jList = new ArrayList<JobData>(2);
				jList.add(first);
				jList.add(second);
				cost = OptUtility.calcTotalCosts(jList);
				
				//cost = SolutionWrapper.getInstance().getDistance(edge.getFirst(), edge.getSecond());
			}
			else if(SolutionWrapper.getInstance().getProblem()==VRP){
				SolutionElements first = SolutionWrapper.getInstance().getNode(edge.getFirst());
				SolutionElements second = SolutionWrapper.getInstance().getNode(edge.getSecond());
				cost = OptUtility.geDecEuclidanDistance(first, second);
				
			}
			edge.setCosts(cost);
			
		
		return edge;
	}
	

}


