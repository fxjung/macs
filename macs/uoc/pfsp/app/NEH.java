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
package macs.uoc.pfsp.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import macs.heuristics.PatternHeuristic;
import macs.ontologies.entities.Edge;
import macs.ontologies.entities.NodeList;
import macs.ontologies.entities.Solution;
import macs.ontologies.entities.SolutionData;
import macs.ontologies.entities.problems.JobData;
import macs.ontologies.entities.problems.NodeData;
import macs.ontologies.entities.problems.SolutionElements;
import macs.uoc.api.Test;
import macs.uoc.cvrp.VRPEdge;
import macs.uoc.pfsp.api.PFSPInputs;
import macs.uoc.pfsp.api.PFSPJob;
import macs.uoc.pfsp.app.PFSPSolution;
import macs.uoc.pfsp.base.BaseJob;


public class NEH {
	
	
	/** General Values */
	private static final double TIME_FACTOR = 1.00; //def 0.03 

	/*******************************************************************************
	 * INSTANCE FIELDS
	 ******************************************************************************/
	

	/*******************************************************************************
	 * PUBLIC METHOD getOutputs()
	 *******************************************************************************/
	public Outputs getOutput(Outputs output) {
		return output;
	}

	/*******************************************************************************
	 * PUBLIC METHOD run()
	 *******************************************************************************/
	public static SolutionData run(PFSPTest test, PFSPInputs aInputs){
		
		//1.  Initalise the algorithm 
		PFSPTest aTest = test;
		PFSPInputs inputs = aInputs;
		PFSPJob[] effList = createEffList(inputs);
		int nJobs = inputs.getNumberOfJobs();
		int nMachines = inputs.getNumberOfMachines();	
		
		double elapsedTime = 0;

		// DEMON PARAMETERS
		int delta;
		
		
		
		PFSPSolution newSol;
			
		
		RandNEHT nehtAlg = new RandNEHT(aTest, inputs); // Rand NEH with Taillard's accel.


		long startTime = ElapsedTime.systemTime();
		PFSPSolution nehSol = nehtAlg.solve(effList, false); // Computation of the NEH
		System.out.println("nehSol "+nehSol.getCosts());
		int nIter = 0;
		// solution
		
		PFSPSolution bestSol = nehSol;
		while(elapsedTime < aTest.getMaxTime() )	   
        {
            newSol = nehtAlg.solve(effList, true);
            //System.out.println("newSol"+newSol.getCosts());
            if( newSol.getCosts() < bestSol.getCosts() ) 
            {
                bestSol = newSol;
                System.out.println("bestSol cost: " + newSol.getCosts());
            }
            elapsedTime = ElapsedTime.calcElapsed(startTime,
    				ElapsedTime.systemTime());
            nIter++;
        }

		
	
		
		
		//4. Set output
		//Outputs output = new Outputs(nehSol, bestSol);
		return getOutputSolution(bestSol);
		
		
	}
	
	public static Solution solve(Solution solution, Test test, PFSPInputs aInputs){
		
		//1.  Initalise the algorithm 
		double elapsedTime = 0;
		Test aTest = test;
				
		int nJobs = aInputs.getNumberOfJobs();
		int nMachines = aInputs.getNumberOfMachines();	
		
		//2. creatEfflist and then reorder
		PFSPJob[] effList = createEffList(aInputs);
		
		List<Integer> fromEdges = PatternHeuristic.edgesToList(solution.getEdges());
		
		PFSPJob[] newEfflist = reOrderEfflist(fromEdges,effList);
		
		
		// DEMON PARAMETERSnull
		int delta;
		
		
		
		PFSPSolution newSol;
			
		
		RandNEHT nehtAlg = new RandNEHT(aTest, aInputs); // Rand NEH with Taillard's accel.


		long startTime = ElapsedTime.systemTime();
		PFSPSolution nehSol = nehtAlg.solve(effList, false); // Computation of the NEH
		//System.out.println("nehSol "+nehSol.getCosts());
		int nIter = 0;
		// solution
		PFSPSolution bestSol = nehSol;
		while( nIter == 0 ||nIter < 1000) //elapsed < aTest.getMaxTime() )	   
        {
            newSol = nehtAlg.solve(newEfflist, true);
            //System.out.println("newSol"+newSol.getCosts());
            if( newSol.getCosts() < bestSol.getCosts() ) 
            {
                bestSol = newSol;
                System.out.println("bestSol cost: " + bestSol.getCosts());
            }
            elapsedTime = ElapsedTime.calcElapsed(startTime,
    				ElapsedTime.systemTime());
            nIter++;
        }

		
	
		
		
		//4. Set output
		//Outputs output = new Outputs(nehSol, bestSol);
		return getResultSolution(bestSol, solution, false);
		
		
	}
	/*******************************************************************************
	 * PRIVATE METHOD createEffList()
	 ******************************************************************************/

	private static PFSPJob[] createEffList(PFSPInputs inputs) {
		
		PFSPJob[] array = inputs.getJobs();
		// Sort using the compareTo() method of the Job class (TIE ISSUE #1)
		Arrays.sort(array);
		return array;
	}
	
	private static PFSPJob[] reOrderEfflist(List<Integer> list, PFSPJob[] efflist){
		System.out.println(" newJobs "+ list);
		List<PFSPJob> jobs = new LinkedList<PFSPJob>(Arrays.asList(efflist));
		LinkedList<PFSPJob> jobsCopy = new LinkedList<PFSPJob>(jobs);
		Collections.copy(jobsCopy, jobs);
		
		if(!list.isEmpty()){
			for(Integer i : list){
				//System.out.println();
				for(PFSPJob job : jobsCopy){					
					//System.out.println();
					if(i == job.getId()){
						
						jobs.remove(job);						
						jobs.add(job);
						
						//System.out.println();
					}
					
				}
			}
		}
		else{
			System.out.println("Empty");
			//System.exit(1);
		}
		PFSPJob[] output = jobs.toArray(new PFSPJob[jobs.size()]);
		return output;
	}
	
	
private static Solution getResultSolution(PFSPSolution psol,Solution  solution,boolean finalrun){
    	
		
		//List<SolutionElements> sList = new LinkedList<SolutionElements>();
		NodeList nList = new NodeList();
		List<Integer> jobNames = new ArrayList<Integer>();
		PFSPJob[] jobs = psol.getJobs();
		for(int i = 0; i < jobs.length-1;i++){
			//sList.add(nodeToJobData(jobs[i]));
			jobNames.add(jobs[i].getId());
					
		}
		nList.setIntList(jobNames);
		nList.setRouteCost(psol.getCosts());
		
		solution.addToSolution(nList);	
		
		
		solution.setValue(psol.getCosts());
		
			
		return solution;
	}
    
   
    
    private static SolutionData getOutputSolution(PFSPSolution psol){
		SolutionData result = new SolutionData();
		
		
	
		List<SolutionElements> sList = new LinkedList<SolutionElements>();
		NodeList nList = new NodeList();
		List<Integer> jobNames = new ArrayList<Integer>();
		PFSPJob[] jobs = psol.getJobs();
		
		for(int i = 0; i < jobs.length;i++){
			sList.add(nodeToJobData(jobs[i]));
			jobNames.add(jobs[i].getId());
					
		}
	
		nList.setIntList(jobNames);
		result.addToNodeList(nList);
		result.setNodes(sList);
		
		result.setValue(psol.getCosts());
		
		return result;
	}
    private static SolutionElements nodeToJobData(PFSPJob job){
    	JobData jobData = new JobData();   
    	jobData.setId(job.getId());
    	for(int i = 0; i < job.getProcessingTimes().length;i++){
    		jobData.setProcessingTime(i, job.getProcessingTime(i));    	
    	}
    	jobData.setTotalProcessingTime(job.getTotalProcessingTime());
    	
    	
    	return jobData;
    	
    }
}
