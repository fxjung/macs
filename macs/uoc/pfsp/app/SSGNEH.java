package macs.uoc.pfsp.app;

import java.util.Arrays;

import macs.uoc.pfsp.api.PFSPInputs;
import macs.uoc.pfsp.api.PFSPJob;


/***********************************************************************************
 * Project SimScheduling - SSGNEH.java
 * 
 * This class encapsulates the SS-GNEH methodology for solving the FSP.
 * 
 * Date of last revision (YYMMDD): 110407 (c) Angel A. Juan & Quim Castella -
 * http://ajuanp.wordpress.com
 **********************************************************************************/

public class SSGNEH {

	/*******************************************************************************
	 * CONSTANT FIELDS
	 ******************************************************************************/
	/** MCS Values */
	private static final int FINAL_DET_SIMULATIONS = 1000;
	private static final int FINAL_STOCH_SIMULATIONS = FINAL_DET_SIMULATIONS;
	private static final int FAST_SIMULATIONS = 500;
	private static final int SLOW_SIMULATIONS = 1000000;
	
	
	/** General Values */
	private static final double TIME_FACTOR = 1.00; //def 0.03 

	/*******************************************************************************
	 * INSTANCE FIELDS
	 ******************************************************************************/
	private PFSPTest aTest; // Test to run (including parameters and instance's
						// name)
	private PFSPInputs inputs; // Instance inputs
	private Outputs output; // Solution output

	private int nJobs; // #Jobs
	private int nMachines; // #Machines
	private PFSPJob[] effList; // Jobs sorted by processing time
	private PFSPSolution nehSol; // classical NEH solution

	private RandNEHT nehtAlg; // Randomized NEH with Taillard's accelerations
	private LocalSearch locSearch; // Local Search procedures

	long startTime;
	double elapsedTime;

	// DEMON PARAMETERS
	int delta;
	int credit;

	/*******************************************************************************
	 * CLASS CONSTRUCTOR
	 ******************************************************************************/
	
	public SSGNEH(PFSPTest test, PFSPInputs aInputs) { 
		aTest = test;
		inputs = aInputs;
		effList = createEffList();
		nJobs = inputs.getNumberOfJobs();
		nMachines = inputs.getNumberOfMachines();

		
		
		nehtAlg = new RandNEHT(aTest, inputs); // Rand NEH with Taillard's
												// accel.
		locSearch = new LocalSearch(aTest, inputs); // Local Search procedures

		startTime = ElapsedTime.systemTime();
		nehSol = nehtAlg.solve(effList, false); // Computation of the NEH
												// solution

		

		elapsedTime = ElapsedTime.calcElapsed(startTime,
				ElapsedTime.systemTime());
		nehSol.setTime(elapsedTime);
	}
	
	
	/*******************************************************************************
	 * PUBLIC METHOD getOutputs()
	 *******************************************************************************/
	public Outputs getOutput() {
		return output;
	}

	/*******************************************************************************
	 * PUBLIC METHOD run()
	 *******************************************************************************/
	public void run() {
		// 0. Create a base solution and set initial time variables
		PFSPSolution baseSol = nehSol;
		startTime = ElapsedTime.systemTime();
		double elapsed = 0.0;
		double maxTime = inputs.getNumberOfJobs()
				* inputs.getNumberOfMachines();
		maxTime = TIME_FACTOR * maxTime;
		
		maxTime = TIME_FACTOR;
		// 1. CONSTRUCT A RANDOMIZED STARTING POINT
		int nTrials = 0;
		do {
			PFSPSolution newSol = nehtAlg.solve(effList, true);
			if (newSol.getCosts() < baseSol.getCosts()) {
				baseSol = newSol;
			}
			nTrials++;
		} while (baseSol.getCosts() >= nehSol.getCosts()
				&& nTrials <= inputs.getNumberOfJobs());

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
				
					bestSol.setTime(elapsed);
				}
			} else if (delta > 0 && delta <= credit) {
				credit = 0;
				baseSol = currentSol;
			}
		}

	
		// 4. Set output
		output = new Outputs(nehSol, bestSol);
	}

	/*******************************************************************************
	 * PRIVATE METHOD createEffList()
	 ******************************************************************************/

	private PFSPJob[] createEffList() {
		PFSPJob[] array = inputs.getJobs();
		// Sort using the compareTo() method of the Job class (TIE ISSUE #1)
		Arrays.sort(array);
		return array;
	}

	/*******************************************************************************
	 * PRIVATE METHOD sumJobProcTimes()
	 ******************************************************************************/

	private double sumJobProcTimes() {
		PFSPJob[] jobs = inputs.getJobs();
		double sum = 0.0;
		for (int i = 0; i < nJobs; i++) {
			sum += jobs[i].getTotalProcessingTime();
		}
		return sum / (10 * nJobs * nMachines);
	}

	/*******************************************************************************
	 * PRIVATE METHOD printSolOnScreen()
	 ******************************************************************************/

	private void printSolOnScreen(PFSPSolution aSolution, boolean isNEHSol) {
		if (isNEHSol == true)
			System.out.print("\n** NEH Solution: ");
		else
			System.out.print("\n** NEH = " + nehSol.getCosts() + "; Seed = "
					+ aTest.getSeed() + "; OBS = " + aSolution.getCosts()
					+ "; Time = " + aSolution.getTime());

		// System.out.println(aSolution.toString(false) + "Still working...");
	}
	
	
}