package macs.uoc.pfsp.baker;

import macs.uoc.pfsp.api.PFSPInputs;
import macs.uoc.pfsp.api.PFSPJob;

public class BakerInputs implements PFSPInputs
{
    /*******************************************************************************
     *  INSTANCE FIELDS 
     ******************************************************************************/

	private int nJobs;      // #Jobs
	private int nMachines;  // #Machines
	private PFSPJob[] jobs;     // Array of jobs

	/*******************************************************************************
     *  CLASS CONSTRUCTOR 
     ******************************************************************************/
    
    public BakerInputs(int nJobsInProblem, int nMachinesInProblem)
    {
    	nJobs = nJobsInProblem;
    	nMachines = nMachinesInProblem;
    	jobs = new PFSPJob[nJobs];
    	for ( int i = 0; i < nJobs; i++ )
    		jobs[i] = new BakerJob(i, nMachines);
    }
    
    /*******************************************************************************
     * GET METHODS
     ******************************************************************************/
    
    public int getNumberOfJobs()
    {
        return nJobs;
    }
    
    public int getNumberOfMachines()
    {
        return nMachines;
    }
    
    public PFSPJob[] getJobs()
    {
    	return jobs;
    }

    /*******************************************************************************
     * PUBLIC METHODS clone()
     ******************************************************************************/

    @Override
    public BakerInputs clone()
    {
        BakerInputs in = new BakerInputs(nJobs,nMachines);

        for(int i = 0; i< nJobs; i++){
            for(int j = 0; j< nMachines; j++){
                in.jobs[i].setProcessingTime(j, this.jobs[i].getProcessingTime(j));
            }
            in.jobs[i].setTotalProcessingTime(this.jobs[i].getTotalProcessingTime());
        }

        return in;
    }


    
}