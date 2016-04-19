package macs.uoc.pfsp.app;

import macs.uoc.pfsp.api.PFSPJob;

/***********************************************************************************
 * Project SimScheduling - Solution.java
 * 
 * This class represents a solution (sequence of jobs) of the FSP. It also
 * includes some important methods to be applied over a solution, like the
 * Taillard's accelerations to improve a given solution.
 * 
 * Date of last revision (YYMMDD): 110407 (c) Angel A. Juan & Quim Castella -
 * http://ajuanp.wordpress.com
 **********************************************************************************/

public class PFSPSolution {

	/*******************************************************************************
	 * INSTANCE FIELDS
	 ******************************************************************************/

	private static int nInstances = 0; // number of instances
	private final int id; // solution ID
	private int costs; // solution costs = end of processing time for all jobs
	private double expcosts; //Stochastic solution cost
	private int nJobs; // number of jobs in the problem
	private PFSPJob[] jobs; // array of jobs in this solution
	private int nMachines; // number of machines in the problem
	private double time; // elapsed computational time (in seconds)

	/*******************************************************************************
	 * CLASS CONSTRUCTOR
	 ******************************************************************************/

	public PFSPSolution(int nJobsInProblem, int nMachinesInProblem) {
		nInstances++;
		id = nInstances;
		costs = 0;
		expcosts = 0;
		nJobs = nJobsInProblem;
		jobs = new PFSPJob[nJobs];
		nMachines = nMachinesInProblem;
		time = 0;
	}

	/*******************************************************************************
	 * SET METHODS
	 ******************************************************************************/

	public void setCosts(int c) {
		costs = c;
	}

	public void setExpCosts(double c) {
		expcosts = c;
	}
	public void setTime(double t, boolean p) {
		time = t;
	}
	public void setTime(double t) {
		//System.out.println("Setting "+t);
		time = t;
	}

	public void setJob(int pos, PFSPJob aJob) {
		jobs[pos] = aJob;
	}
	public void setJob(PFSPJob[] jobs) {
		this.jobs = jobs;
	}

	/*******************************************************************************
	 * GET METHODS
	 ******************************************************************************/

	public int getId() {
		return id;
	}

	public int getCosts() {
		return costs;
	}

	public double getExpCosts() {
		return expcosts;
	}

	public PFSPJob[] getJobs() {
		return jobs;
	}

	public double getTime() {
		return time;
	}

	public int getNJobs() {
		return nJobs;
	}

	public int getNMachines() {
		return nMachines;
	}

	/*******************************************************************************
	 * PUBLIC METHOD copySol()
	 ******************************************************************************/
	@Override
	public PFSPSolution clone() {
		PFSPSolution cloneSol = new PFSPSolution(nJobs, nMachines);

		System.arraycopy(this.jobs, 0, cloneSol.getJobs(), 0, this.jobs.length);

		cloneSol.setCosts(this.getCosts());
		cloneSol.setExpCosts(this.getExpCosts());
		cloneSol.setTime(this.getTime(),false);

		return cloneSol;
	}

	/*******************************************************************************
	 * PUBLIC METHOD calcTotalCosts()
	 ******************************************************************************/
	public double calcExpTotalCosts(int nUsedJobs) {
		// nUsedJobs = # of jobs in the partially filled solution

		double[][] tcosts = new double[nUsedJobs][nMachines];
		for (int column = 0; column < nMachines; column++)
			for (int row = 0; row < nUsedJobs; row++) {
				if (column == 0 && row == 0)
					tcosts[0][0] = jobs[0].getExpProcessingTime(0);
				else if (column == 0)
					tcosts[row][0] = tcosts[row - 1][0]
							+ jobs[row].getExpProcessingTime(0);
				else if (row == 0)
					tcosts[0][column] = tcosts[0][column - 1]
							+ jobs[0].getExpProcessingTime(column);
				else {
					double max = Math.max(tcosts[row - 1][column],
							tcosts[row][column - 1]);
					tcosts[row][column] = max
							+ jobs[row].getProcessingTime(column);
				}
			}
		return tcosts[nUsedJobs - 1][nMachines - 1];
	}

	public int calcTotalCosts(int nUsedJobs) {
		// nUsedJobs = # of jobs in the partially filled solution

		int[][] tcosts = new int[nUsedJobs][nMachines];
		for (int column = 0; column < nMachines; column++)
			for (int row = 0; row < nUsedJobs; row++) {
				if (column == 0 && row == 0)
					tcosts[0][0] = jobs[0].getProcessingTime(0);
				else if (column == 0)
					tcosts[row][0] = tcosts[row - 1][0]
							+ jobs[row].getProcessingTime(0);
				else if (row == 0)
					tcosts[0][column] = tcosts[0][column - 1]
							+ jobs[0].getProcessingTime(column);
				else {
					int max = Math.max(tcosts[row - 1][column],
							tcosts[row][column - 1]);
					tcosts[row][column] = max
							+ jobs[row].getProcessingTime(column);
				}
			}
		return tcosts[nUsedJobs - 1][nMachines - 1];
	}

	/*******************************************************************************
	 * PUBLIC METHOD improveByShiftingJobToLeft() This method implements
	 * Taillard's accelerations where k is the position of the job on the right
	 * extreme.
	 * 
	 * This method also updates the solution cost (makespan) if k == nJobs -1
	 ******************************************************************************/

	public void improveByShiftingJobToLeft(int k) {
		int bestPosition = k;
		int minMakespan = Integer.MAX_VALUE;
		int newMakespan = Integer.MAX_VALUE;

		int[][] eMatrix = null;
		int[][] qMatrix = null;
		int[][] fMatrix = null;
		int maxSum = 0;
		int newSum = 0;

		// Calculate eMatrix
		eMatrix = calcEMatrix(k);

		// Calculate qMatrix
		qMatrix = calcQMatrix(k);

		// Calculate fMatrix
		fMatrix = calcFMatrix(k, eMatrix);

		// Calculate bestPosition (0...k) and minMakespan (mVector)
		for (int i = k; i >= 0; i--) {
			maxSum = 0;
			for (int j = 0; j < nMachines; j++) {
				newSum = fMatrix[i][j] + qMatrix[i][j];
				if (newSum > maxSum)
					maxSum = newSum;
			}
			newMakespan = maxSum;
			// TIE ISSUE #2 - In case of tie, do swap
			if (newMakespan <= minMakespan) {
				minMakespan = newMakespan;
				bestPosition = i;
			}
		}

		// Update solution with bestPosition and minMakespan
		if (bestPosition < k) // if i == k do nothing
		{
			PFSPJob auxJob = jobs[k];
			for (int i = k; i > bestPosition; i--)
				jobs[i] = jobs[i - 1];

			jobs[bestPosition] = auxJob;
		}
		if (k == nJobs - 1)
			this.setCosts(minMakespan);
	}

	/*******************************************************************************
	 * PRIVATE METHOD calcEMatrix()
	 ******************************************************************************/

	private int[][] calcEMatrix(int k) {
		int[][] e = new int[k][nMachines];

		for (int i = 0; i < k; i++) {
			for (int j = 0; j < nMachines; j++) {
				if (i == 0 && j == 0)
					e[0][0] = jobs[0].getProcessingTime(0);
				else if (j == 0)
					e[i][0] = e[i - 1][0] + jobs[i].getProcessingTime(0);
				else if (i == 0)
					e[0][j] = e[0][j - 1] + jobs[0].getProcessingTime(j);
				else {
					int max = Math.max(e[i - 1][j], e[i][j - 1]);
					e[i][j] = max + jobs[i].getProcessingTime(j);
				}
			}
		}
		return e;
	}

	/*******************************************************************************
	 * PRIVATE METHOD calcQMatrix()
	 ******************************************************************************/

	private int[][] calcQMatrix(int k) {
		int[][] q = new int[k + 1][nMachines];

		for (int i = k; i >= 0; i--) {
			for (int j = nMachines - 1; j >= 0; j--) {
				if (i == k)
					q[k][j] = 0; // dummy file to make possible fMatrix +
									// qMatrix
				else if (i == k - 1 && j == nMachines - 1)
					q[k - 1][nMachines - 1] = jobs[k - 1]
							.getProcessingTime(nMachines - 1);
				else if (j == nMachines - 1)
					q[i][nMachines - 1] = q[i + 1][nMachines - 1]
							+ jobs[i].getProcessingTime(nMachines - 1);
				else if (i == k - 1)
					q[k - 1][j] = q[k - 1][j + 1]
							+ jobs[k - 1].getProcessingTime(j);
				else {
					int max = Math.max(q[i + 1][j], q[i][j + 1]);
					q[i][j] = max + jobs[i].getProcessingTime(j);
				}
			}
		}
		return q;
	}

	/*******************************************************************************
	 * PRIVATE METHOD calcFMatrix()
	 ******************************************************************************/

	private int[][] calcFMatrix(int k, int[][] e) {
		int[][] f = new int[k + 1][nMachines];

		for (int i = 0; i <= k; i++) {
			for (int j = 0; j < nMachines; j++) {
				if (i == 0 && j == 0)
					f[0][0] = jobs[k].getProcessingTime(0);
				else if (j == 0)
					f[i][0] = e[i - 1][0] + jobs[k].getProcessingTime(0);
				else if (i == 0)
					f[0][j] = f[0][j - 1] + jobs[k].getProcessingTime(j);
				else {
					int max = Math.max(e[i - 1][j], f[i][j - 1]);
					f[i][j] = max + jobs[k].getProcessingTime(j);
				}
			}
		}
		return f;
	}

	/*******************************************************************************
	 * PUBLIC METHOD toString()
	 ******************************************************************************/

	public String toString(boolean printDetails) {
		String s = "";
		s = s.concat("\r\n");
		s = s.concat("Sol ID : " + this.getId() + "\r\n");
		s = s.concat("Sol costs: " + this.getCosts() + "\r\n");
		s = s.concat("Sol expCosts: " + this.getExpCosts() + "\r\n");
		double time = this.getTime();
		int timeInt = (int) Math.round(time);
		s = s.concat("Sol time: " + ElapsedTime.calcHMS(timeInt) + " (" + time
				+ " sec.)");
		s = s.concat("\r\n");
		if (printDetails == true) {
			s = s.concat("List of jobs: \r\n");
			for (int i = 0; i < jobs.length; i++)
				s = s.concat("" + jobs[i].getId() + "\r\n");
		}
		return s;
	}
}