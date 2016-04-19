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

	import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import macs.heuristics.PatternHeuristic;
import macs.ontologies.SolutionWrapper;
import macs.ontologies.entities.Edge;
import macs.ontologies.entities.NodeList;
import macs.ontologies.entities.Solution;
import macs.ontologies.entities.SolutionData;
import macs.ontologies.entities.problems.JobData;
import macs.ontologies.entities.problems.SolutionElements;
import macs.uoc.api.Test;
import macs.uoc.cvrp.VRPSolution;
import macs.uoc.pfsp.api.PFSPInputs;
import macs.uoc.pfsp.api.PFSPJob;
import macs.uoc.pfsp.app.PFSPSolution;
import macs.util.OptUtility;



	public class ILSESP {
		
		
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
		
		
		public static SolutionData run(PFSPTest aTest, PFSPInputs aInputs){
			// DEMON PARAMETERS
			int delta;
			int credit = 0;

			//List<Double> local = new ArrayList<Double>();
			//0. Get the first NEH 
			RandNEHT nehtAlg = new RandNEHT(aTest, aInputs); // Rand NEH with Taillard's
													// accel.
			LocalSearch locSearch = new LocalSearch(aTest, aInputs); // Local Search procedures
			PFSPJob[] effList = createEffList(aInputs);

			long startTime = ElapsedTime.systemTime();
			PFSPSolution nehSol = nehtAlg.solve(effList, false); // Computation of the NEH
													// solution

			

			double elapsedTime = ElapsedTime.calcElapsed(startTime,
					ElapsedTime.systemTime());
			nehSol.setTime(elapsedTime);
			
			
			
			
			//1.  Initalise the algorithm 			
		
		
			// 0. Create a base solution and set initial time variables
			PFSPSolution baseSol = nehSol;
			startTime = ElapsedTime.systemTime();
			double elapsed = 0.0;
			//double maxTime = aInputs.getNumberOfJobs() * aInputs.getNumberOfMachines(); 
			//maxTime = TIME_FACTOR * maxTime;
			
			//maxTime = TIME_FACTOR;
			double maxTime = aTest.getMaxTime();		
			// 1. CONSTRUCT A RANDOMIZED STARTING POINT
			int nTrials = 0;
			do {
				PFSPSolution newSol = nehtAlg.solve(effList, true);
				if (newSol.getCosts() < baseSol.getCosts()) {
					baseSol = newSol;
				}
				nTrials++;
			} while (baseSol.getCosts() >= nehSol.getCosts()
					&& nTrials <= aInputs.getNumberOfJobs());

			// 2. LOCAL SEARCH PROCESS (randomJobShifting while improving)
			locSearch.globalImprovement(baseSol);

			

			// 3. ITERATIVE DISRUPTION AND LOCAL SEARCH
			PFSPSolution bestSol = baseSol.clone();

		

			PFSPSolution bestStochSol = bestSol;

			while (elapsed < maxTime) {
				// 3.0 Use a copy of the baseSol to explore
				PFSPSolution currentSol = baseSol.clone();

				// 3.1 Perturbation
				locSearch.enhancedSwap(currentSol);
				
				// 3.2 Local Search
				locSearch.globalImprovement(currentSol);

				// 3.3 Update Elapsed time
				elapsed = ElapsedTime.calcElapsed(startTime,
						ElapsedTime.systemTime());

				// 3.4 Acceptance Criterion
				delta = currentSol.getCosts() - baseSol.getCosts();

				if (delta < 0) // improvement
				{
					// Update data
					credit = -delta;
					baseSol = currentSol;

					if (baseSol.getCosts() < bestSol.getCosts()) {
						bestSol = baseSol;
						//local.add(new Double(bestSol.getCosts()));
					
						bestSol.setTime(elapsed);
					}
				} else if (delta > 0 && delta <= credit) {
					credit = 0;
					baseSol = currentSol;
				}
			}

		
			// 4. Set output
			//Outputs output = new Outputs(nehSol, bestSol);
			return getOutputSolution(bestSol);		
			
			
		}
		
		public static Solution solve(Solution solution, PFSPTest aTest, PFSPInputs aInputs){
			
			// DEMON PARAMETERS
			int delta;
			int credit = 0;
			//2. creatEfflist and then reorder
			//for(Edge ed : solution.getEdges())
				//System.out.println("Ils "+ed.getFirst()+","+ed.getSecond()+"  "+ed.getCosts());
			
			//0. Get the first NEH 
			RandNEHT nehtAlg = new RandNEHT(aTest, aInputs); // Rand NEH with Taillard's
													// accel.
			LocalSearch locSearch = new LocalSearch(aTest, aInputs); // Local Search procedures
			
			PFSPJob[] effList = createEffList(aInputs);		
			List<Integer> fromEdges = PatternHeuristic.edgesToList(solution.getEdges());
			
			PFSPJob[] newEfflist = reOrderEfflist(fromEdges,effList);

			long startTime = ElapsedTime.systemTime();
			PFSPSolution nehSol = nehtAlg.solve(newEfflist, true); // Computation of the NEH
											

			

			double elapsedTime = ElapsedTime.calcElapsed(startTime,
					ElapsedTime.systemTime());
			nehSol.setTime(elapsedTime);
			
			
			
			
			//1.  Initalise the algorithm 			
		
		
			// 0. Create a base solution and set initial time variables
		
			
			
			//---------------------------//
			PFSPSolution baseSol = nehSol;
			startTime = ElapsedTime.systemTime();
			double elapsed = 0.0;
			//double maxTime = aInputs.getNumberOfJobs() * aInp					uts.getNumberOfMachines(); 
			//maxTime = TIME_FACTOR * maxTime;
			
			//maxTime = TIME_FACTOR;
			double maxTime = aTest.getMaxTime();
			// 1. CONSTRUCT A RANDOMIZED STARTING POINT
			int nTrials = 0;
			do {
				PFSPSolution newSol = nehtAlg.solve(newEfflist, true);
				if (newSol.getCosts() < baseSol.getCosts()) {
					baseSol = newSol;
				}
				nTrials++;
			} while (baseSol.getCosts() >= nehSol.getCosts()
					&& nTrials <= aInputs.getNumberOfJobs());

			// 2. LOCAL SEARCH PROCESS (randomJobShifting while improving)
			locSearch.globalImprovement(baseSol);

			

			// 3. ITERATIVE DISRUPTION AND LOCAL SEARCH
			PFSPSolution bestSol = baseSol.clone();

		
			int nter = 0;
			PFSPSolution bestStochSol = bestSol;
			//startCostToFile();

			//while( nter == 0 ||nter < 500) //elapsed < aTest.getMaxTime() )	   
			while( elapsed < aTest.getMaxTime() )	 
	        {
				// 3.0 Use a copy of the baseSol to explore
				PFSPSolution currentSol = baseSol.clone();
				
				
				// 3.1 Perturbation
				locSearch.enhancedSwap(currentSol);

				// 3.2 Local Search
				locSearch.globalImprovement(currentSol);

				// 3.3 Update Elapsed time
				elapsed = ElapsedTime.calcElapsed(startTime,
						ElapsedTime.systemTime());

				// 3.4 Acceptance Criterion
				delta = currentSol.getCosts() - baseSol.getCosts();

				if (delta < 0) // improvement
				{
					// Update data
					credit = -delta;
					baseSol = currentSol;

					if (baseSol.getCosts() < bestSol.getCosts()) {
						bestSol = baseSol;
					
						bestSol.setTime(elapsed);
						//writeCostToFile(bestSol,nter,elapsed);
						
					}
				} else if (delta > 0 && delta <= credit) {
					credit = 0;
					baseSol = currentSol;
					//writeCostToFile(baseSol,nter,elapsed);
					
				
				}
				
			
				nter++;
			}

			SolutionWrapper.getInstance().setLocalOpt(bestSol.getCosts());
			// 4. Set output
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
			//System.out.println(" newJobs "+ list);
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
				//System.out.println("Empty");
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
			for(int i = 0; i < jobs.length;i++){
				//sList.add(nodeToJobData(jobs[i]));
				jobNames.add(jobs[i].getId());
						
			}
			nList.setIntList(jobNames);
			
			nList.setRouteCost(psol.getCosts());
			
			
			
			solution.addToSolution(nList);
			
			
			
			solution.setValue(psol.getCosts());
			solution.setTimes(new Double(psol.getTime()).toString());
			
				
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
			result.setTime(new Double(psol.getTime()).toString());
			//result.setLocalOpt(local);
			
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
	    
	 private static PFSPSolution getPFSPSolution(Solution sol, PFSPInputs aInputs){
		 PFSPJob[] jobs = getSolJobs(sol,aInputs);
		 PFSPSolution newSol = new PFSPSolution(aInputs.getNumberOfJobs(), aInputs.getNumberOfMachines());
		 newSol.setJob(jobs);
		 for( int i = 1; i < aInputs.getNumberOfJobs(); i++ ) {
			 newSol.improveByShiftingJobToLeft(i);
		 }
		 return newSol;
	 }


	 private static PFSPJob[] getSolJobs(Solution sol,PFSPInputs aInputs){
	    	List<Integer> list = sol.getFirstSolution().getIntList();	    	
			PFSPJob[] jobList = new PFSPJob[list.size()];
			PFSPJob[] oldJobs = aInputs.getJobs();
			for(int i = 0; i < list.size();i++){
				for(int j =0; j< oldJobs.length;j++){
					if(list.get(i)==oldJobs[j].getId()){
						jobList[i] = oldJobs[j];
					}
				}	
			
			}
			return jobList;
	    }
	 public static void writeCostToFile(PFSPSolution bestSol,int number,double time){
	    	
			
			String filename = "/home/smartin/workspace/maca/output/pfspOutput#2.csv";
					
			Properties p = System.getProperties();
			String newline = p.getProperty("line.separator");
		
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
			        
			        ///value
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
			
			
			String filename = "/home/smartin/workspace/maca/output/pfspOutput#2.csv";
					
			
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
