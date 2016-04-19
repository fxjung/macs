package macs.uoc.pfsp.app;

import macs.uoc.api.Test;
import macs.uoc.pfsp.api.PFSPInputs;
import macs.uoc.pfsp.api.PFSPJob;

/***********************************************************************************
 * Project SimScheduling - RandNEHT.java
 * 
 * This class encapsulates the NEH heuristic with Taillard's accelerations. 
 *  
 * It returns either the NEH solution (usedRandomSelection = false) or an 
 *  alternative solution by selecting jobs from the list at random 
 *  (usedRandomSelection = true).
 *  
 * Notice that the final value of the NEH solution will depend upon the criteria 
 *  used to solve tie issues in: (1) the compareTo() method of the Job class (used 
 *  to sort the jobs in the effList), and (2) the selection of currentSol or auxSol
 *  when they provide the same partial costs. 
 *  
 * Date of last revision (YYMMDD): 110318
 * (c) Angel A. Juan, Quim Castella - http://ajuanp.wordpress.com
 **********************************************************************************/

public class RandNEHT
{ 
	/******************************************************************************* 
     * INSTANCE FIELDS 
     * ****************************************************************************/
     
    private Test aTest;     // Test to run (including parameters and instance's name)
    private PFSPInputs inputs;  // Instance inputs
    private int nJobs;      // #Jobs
    private int nMachines;  // #Machines
    private int[] positions; // Array of randomly selected positions
    private PFSPJob nextJob;
    
    /******************************************************************************* 
     * CLASS CONSTRUCTOR 
     * ****************************************************************************/
      
    public RandNEHT(Test test, PFSPInputs inputs2)	
    {     
    	aTest = test;
    	inputs = inputs2;
    	nJobs = inputs.getNumberOfJobs();
    	nMachines = inputs.getNumberOfMachines();
    	positions = new int[nJobs];
        nextJob = null;
    }				
 	
    /******************************************************************************* 
     * PUBLIC METHOD solve()  
     ******************************************************************************/
    
	public PFSPSolution solve(PFSPJob[] effList, boolean useRandomSelection)
    {	    	
		// 0. Define a new solution
		PFSPSolution currentSol = new PFSPSolution(nJobs, nMachines);
		
		// 1. Calculate the array of randomly selected positions of jobs in effList
		if( !useRandomSelection ) // classical NEH solution
			for( int i = 0; i < nJobs; i++ )
                positions[i] = i;
		else
        {
            Randomness random = new Randomness(aTest, inputs);
			positions = random.calcPositionsArray(aTest.getDistribution()); // Randomized NEH solution
        }

		// 2. Insert the first job in the solution (not an empty solution anymore)
		nextJob = effList[positions[0]];
		currentSol.getJobs()[0] = nextJob;
	
		// 3. Complete the solution with the remaining jobs
		for( int i = 1; i < nJobs; i++ ) {
			// Add nextJob to the end of currentSol (partial solution)
			nextJob = effList[positions[i]];
			currentSol.getJobs()[i] = nextJob;
			
			// Try to improve currentSol by shifting nextJob to the left
			currentSol.improveByShiftingJobToLeft(i);
		}
    	return currentSol;
    }	
}