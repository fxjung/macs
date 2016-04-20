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
package macs.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import macs.agents.AgentVocabulary;
import macs.ontologies.SolutionWrapper;
import macs.ontologies.entities.Edge;
import macs.ontologies.entities.Solution;
import macs.ontologies.entities.problems.JobData;
import macs.ontologies.entities.problems.NodeData;
import macs.ontologies.entities.problems.SolutionElements;
;



/**
 * @author simon
 *
 */
public class OptUtility implements AgentVocabulary{
	
	
	
	public static double geDecEuclidanDistance(SolutionElements a, SolutionElements b){
		NodeData sa = (NodeData)a;
		NodeData sb = (NodeData)b;
		Float xdistance = new Float(sa.getX() - sb.getX());
		Float ydistance = new Float(sa.getY() - sb.getY());
	
		Double distance = Math.rint(Math.sqrt(Math.pow(xdistance,2) + Math.pow(ydistance, 2)));
		
		
		return distance;
	}
	
	public static double getEuclidanDistance(SolutionElements a, SolutionElements b){
		NodeData sa = (NodeData)a;
		NodeData sb = (NodeData)b;
		double xdistance = sa.getX() - sb.getX();
		double ydistance = sa.getY() - sb.getY();
	
		Double distance = Math.rint(Math.sqrt(xdistance*xdistance + ydistance*ydistance));
		
		return distance;
	}
	/**
	 * This calculation of geographical distance is taken from the TSPLIB definition of GEO
	 * It is designed to comply as near as possible to this defition for TSP calcaultions
	 * @param a
	 * @param b
	 * @return
	 */
	public static int getGeographicalDistance(SolutionElements a, SolutionElements b){
		final double PI = 3.141592; 
		final double RRR = 6378.388;
		NodeData sa = (NodeData)a;
		NodeData sb = (NodeData)b;
		
		/************************************************************************************
		 * Convert lat and longs for each node into radians
		 */
		//calculate latitude node A
		double xdega = Math.rint(sa.getX());
		double xmina = sa.getX() - xdega;
		double lata = PI*(xdega+5.0*xmina/30)/180;
		
		//calculate longitude node A
		double ydega = Math.rint(sa.getY());
		double ymina = sa.getY() - ydega;
		double longa = PI*(ydega+5.0*ymina/30)/180;
		
		
		//calculate latitude node B
		double xdegb = Math.rint(sb.getX());
		double xminb = sb.getX() - xdegb;
		double latb = PI*(xdegb+5.0*xminb/30)/180;
		
		//calculate longitude node B
		double ydegb = Math.rint(sb.getY());
		double yminb = sb.getY() - ydegb;
		double longb = PI*(ydegb+5.0*yminb/30)/180;
		/************************************************************************************
		 * Find the distance between each node
		 */
		double q1 = Math.cos(longa - longb);
		double q2 = Math.cos(lata - latb);
		double q3 = Math.cos(lata + latb);
		
		//distance between a and b
		int dab = (int)(RRR* Math.acos(0.5*((1.0+q1)*q2-(1.0-q1)*q3))+1.0);	

		return dab;
	}
	
	public static int getModulus(int arraysize, int number){
		int rem =0;
		
		if(number < 0){
			rem = arraysize+ number;
		}
		else {
			rem = number % arraysize;
		}
		
		
		  return rem;
	}
		
	
	
	
	public static SolutionElements getNode(List<SolutionElements> nodes, int nodename){
		SolutionElements result = null;
		for(SolutionElements node : nodes){
			if(node.getId()==nodename){
				result = node;
				break;
			}
		}
		
		return result;
	}
	
	public static List<Double> generateTempList(Solution sol,int iterations){
		List<Double> temparray = new ArrayList<Double>();
		double value = sol.getValue();
		 Double newvalue = new Double(value);
		 temparray.add(newvalue);
		 newvalue = 0.9*newvalue;
		 temparray.add(newvalue);
		 for(int i = 0; i < iterations; i++){
			 newvalue = 0.9*newvalue;
			 temparray.add(newvalue);
		 }	
		 return temparray;
	 }
	public static List<Double> generateLogTempList(Solution sol, int iterations){
		List<Double> temparray = new ArrayList<Double>();
		double value = sol.getValue();
		 Double newvalue = new Double(value);
		 for(int i = 0; i < iterations; i++){
			 newvalue = value/Math.log10(1+iterations);
			 temparray.add(newvalue);
		 }
		
		 return temparray;
		
	}
	
	public static int[]  Integer2int (List<Integer> IntegerList){
		int len = IntegerList.size();
		int[] output = new int[len];
		if(IntegerList.isEmpty()){
			output[0] = 0;
		}
		else {
			for(int i =0; i < len; i++){
				output[i] = IntegerList.get(i).intValue();			
			}
		}
		return output;
	}
	
	public static List<Integer>  int2Integer (int[] intlist){
		List<Integer> output = new ArrayList<Integer>();
		if(intlist == null){
			output.add(0);
		}
		else {
			for(int i = 0; i < intlist.length; i++){
				output.add(new Integer(intlist[i]));
			}
		}
		
		return output;
	}
	
	//got to pick the names!
	
	
	/**
	 * Primarily used for TSP this method will use the appropriate distance method
	 * as speicifed in the TSPLIB format file 
	 * @param nodes
	 * @return
	 */
	public static double getSolutionValue (List<SolutionElements> nodes){	
		double newvalue = 0;
		int size = nodes.size();
		String distanceMeasure = ((NodeData)nodes.get(0)).getMeasure();
		
		System.out.println("distanceMeasure "+distanceMeasure);
		for(int i = 0; i < nodes.size(); i++){
			if(distanceMeasure.equals("EUC_2D")){	
				
				newvalue = newvalue + geDecEuclidanDistance(nodes.get(i),nodes.get(getModulus(size,i+1)));
			}
			else if(distanceMeasure.equals("GEO")){
				newvalue = newvalue + getGeographicalDistance(nodes.get(i),nodes.get(getModulus(size,i+1)));
			
			}
			else{
				newvalue = newvalue + geDecEuclidanDistance(nodes.get(i),nodes.get(getModulus(size,i+1)));
			}
			
		}
		

		return newvalue;
	}
	
	
	
	
	public static int getNeighbourListLength(int len, double decimal){		
		int length = new Double(len*decimal).intValue();
		return length;
	}
	



	
	
	public static int calcTotalCosts(List<JobData> jobs) {
		// nUsedJobs = # of jobs in the partially filled solution
		int nUsedJobs = jobs.size();
		int nMachines = jobs.get(0).getProcessingTimes().size();		

		int[][] tcosts = new int[nUsedJobs][nMachines];
		for (int column = 0; column < nMachines; column++)
			for (int row = 0; row < nUsedJobs; row++) {
				if (column == 0 && row == 0)
					tcosts[0][0] = jobs.get(0).getProcessingTime(0);
				else if (column == 0)
					tcosts[row][0] = tcosts[row - 1][0]
							+ jobs.get(row).getProcessingTime(0);
				else if (row == 0)
					tcosts[0][column] = tcosts[0][column - 1]
							+ jobs.get(0).getProcessingTime(column);
				else {
					int max = Math.max(tcosts[row - 1][column],
							tcosts[row][column - 1]);
					tcosts[row][column] = max
							+ jobs.get(row).getProcessingTime(column);
				}
			}
		return tcosts[nUsedJobs - 1][nMachines - 1];
	}
	
	public static Solution compareSolutions(Solution newsolution,Solution oldsolution){
		List<Integer> newsol;
		List<Integer> oldsol;
		if(SolutionWrapper.getInstance().getProblem()==VRP){
			newsol = newsolution.getList();
			oldsol = oldsolution.getList();
		}
		else {
			newsol = newsolution.getFirstSolution().getIntList();
			oldsol = oldsolution.getFirstSolution().getIntList();
		}
		
		List<List<Integer>> newpairs = makePairs(newsol);
		List<List<Integer>> oldpairs = makePairs(oldsol);
		//List<Integer> deletions = new ArrayList<Integer>();
		//List<Integer> additions = new ArrayList<Integer>();
		
		for(int i = 0; i < newpairs.size();i++){
			for(int j = 0; j < oldpairs.size();j++){
				if(newpairs.get(i) != oldpairs.get(j)){
					newsolution.setDeletions(oldpairs.get(j));
					
					newsolution.setAdditions(newpairs.get(i));
					//System.out.println("ADD " +newpairs.get(i));
					//System.out.println("DEL " +oldpairs.get(j));
					
					
				}
			}
		}
		/*if(!deletions.isEmpty()){
			newsolution.setDeletions(deletions);
		}
		else {
			newsolution.setDeletions(oldsolution.getDeletions().get(0));
		}
		if(!additions.isEmpty()){
			newsolution.setAdditions(additions);
		}
		else {
			newsolution.setAdditions(oldsolution.getAdditions().get(0));
		}*/
		
		
		return newsolution;
	}
	
	

	

	
	public static List<List<Integer>> makePairs(List<Integer> list){
		List<List<Integer>> output = new ArrayList<List<Integer>>();
		for(int i = 0; i < list.size()-1; i++){
			List<Integer> newlist = list.subList(i, i+2);
			output.add(newlist);
		}
		return output;
	}
	
	
	public static int checkDuplicates(List<SolutionElements> list){
		int result = -1;
		List<Integer> intlist = new ArrayList<Integer>();
		
		for(SolutionElements i : list){
			intlist.add(i.getId());
		}
		for(Integer j : intlist){
			if(Collections.frequency(intlist, j) > 1){				
				result = j;
			}
		}
		
		return result;
		
	}
	public static double roundTwoDecimals(double d) {
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
		
    
	}
	
	public static Edge computeCost(Edge edge){
		
		double cost = 0;
		
			if(SolutionWrapper.getInstance().getProblem()==PFSP){
				JobData first = (JobData)SolutionWrapper.getInstance().getNode(edge.getFirst());
				JobData second = (JobData)SolutionWrapper.getInstance().getNode(edge.getSecond());
				List<JobData> jList = new ArrayList<JobData>(2);
				jList.add(first);
				jList.add(second);
				cost = calcTotalCosts(jList);
				
				//cost = SolutionWrapper.getInstance().getDistance(edge.getFirst(), edge.getSecond());
			}
			else if(SolutionWrapper.getInstance().getProblem()==TSP||SolutionWrapper.getInstance().getProblem()==VRP){
				SolutionElements first = SolutionWrapper.getInstance().getNode(edge.getFirst());
				SolutionElements second = SolutionWrapper.getInstance().getNode(edge.getSecond());
				cost = geDecEuclidanDistance(first, second);
				
			}
			edge.setCosts(cost);
			//System.out.println("DISTANCE " + distance);
		
		return edge;
	}
	public static double computeSingleCost(Edge edge,List<SolutionElements> nodes,int problem){
		
		double cost = 0;
		
			if(problem==PFSP){
				
				JobData first = (JobData)getNode(nodes,edge.getFirst());
				JobData second = (JobData)getNode(nodes,edge.getSecond());
				List<JobData> jList = new ArrayList<JobData>(2);
				jList.add(first);
				jList.add(second);
				cost = calcTotalCosts(jList);
				
				//cost = SolutionWrapper.getInstance().getDistance(edge.getFirst(), edge.getSecond());
			}
			else if(problem==TSP||problem==VRP){
				SolutionElements first = getNode(nodes,edge.getFirst());
				SolutionElements second = getNode(nodes,edge.getSecond());
				cost = geDecEuclidanDistance(first, second);
				
			}
			
			//System.out.println("DISTANCE " + distance);
		
		return cost;
	}
public static Edge computeCost(Edge edge,List<SolutionElements> nodes,int problem){
		
		double cost = 0;
		
			if(problem==PFSP){
				
				JobData first = (JobData)getNode(nodes,edge.getFirst());
				JobData second = (JobData)getNode(nodes,edge.getSecond());
				List<JobData> jList = new ArrayList<JobData>(2);
				jList.add(first);
				jList.add(second);
				cost = calcTotalCosts(jList);
				
				//cost = SolutionWrapper.getInstance().getDistance(edge.getFirst(), edge.getSecond());
			}
			else if(problem==TSP||problem==VRP){
				SolutionElements first = getNode(nodes,edge.getFirst());
				SolutionElements second = getNode(nodes,edge.getSecond());
				cost = geDecEuclidanDistance(first, second);
				
			}
			edge.setCosts(cost);
			//System.out.println("DISTANCE " + distance);
		
		return edge;
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

}

