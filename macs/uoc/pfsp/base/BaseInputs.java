package macs.uoc.pfsp.base;

import macs.uoc.pfsp.api.PFSPInputs;
import macs.uoc.pfsp.api.PFSPJob;

/***********************************************************************************
 * Project SimScheduling - Inputs.java
 * 
 * This class represents the inputs of an FSP instance.
 * 
 * Date of last revision (YYMMDD): 100103
 * (c) Angel A. Juan - http://ajuanp.wordpress.com, Quim Castella
 **********************************************************************************/

public class BaseInputs implements PFSPInputs
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
    
    public BaseInputs(int nJobsInProblem, int nMachinesInProblem)
    {
    	nJobs = nJobsInProblem;
    	nMachines = nMachinesInProblem;
    	jobs = new PFSPJob[nJobs];
    	for ( int i = 0; i < nJobs; i++ )
    		jobs[i] = new BaseJob(i, nMachines);
    }
    public BaseInputs(){}
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
    public BaseInputs clone()
    {
        BaseInputs in = new BaseInputs(nJobs,nMachines);

        for(int i = 0; i< nJobs; i++){
            for(int j = 0; j< nMachines; j++){
                in.jobs[i].setProcessingTime(j, this.jobs[i].getProcessingTime(j));
            }
            in.jobs[i].setTotalProcessingTime(this.jobs[i].getTotalProcessingTime());
        }

        return in;
    }

    
}