/** 
 * MACS - Multi-Agent Cooperative Search is a framework to develop cooperating agents using 
 * different Metaheuristics Copyright (C) 2016 Simon Martin, Angel Alejandro Juan Perez. This file is part of MACS. 
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