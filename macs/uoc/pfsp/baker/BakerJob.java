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
package macs.uoc.pfsp.baker;

import macs.uoc.pfsp.api.PFSPJob;

public class BakerJob implements PFSPJob
{

    /******************************************************************************* 
     * INSTANCE FIELDS 
     ******************************************************************************/
	
	private int id; // job ID
	private int[] processingTimes;
	private int[] varianceList; 
    private double[] expProcessingTimes;
	private int totalProcessingTime; // total processing time for this job
        
    /******************************************************************************* 
     * CONSTRUCTOR 
     ******************************************************************************/
  
    public BakerJob(int order, int nMachines)
    {
    	id = order + 1;
    	processingTimes = new int[nMachines];
    	varianceList = new int[nMachines]; 
        expProcessingTimes = new double[nMachines];
    	totalProcessingTime = 0; 
    }
	
    /******************************************************************************* 
     * SET METHODS 
     ******************************************************************************/
   
    public void setId(int jobId)
    { 
    	id = jobId; 
    }
     
    public void setExpProcessingTime(int machine, double time)
    {
        expProcessingTimes[machine] = time;
    }
    
    public void setProcessingTime(int machine, int time)
    {
        processingTimes[machine]= time;
    }
   
    public void setTotalProcessingTime(int time)
    {
        totalProcessingTime = time;
    }
    
    /******************************************************************************* 
     * GET METHODS 
     ******************************************************************************/
    
    public int getId()
    {
    	return id;
    }
    public double getExpProcessingTime(int machine)
    {
        return expProcessingTimes[machine];
    }
    
    public int getProcessingTime(int machine)
    {
        return processingTimes[machine];
    }
    
    public int getTotalProcessingTime()
    {
        return totalProcessingTime;
    }
    
    /******************************************************************************* 
     * COMPARE TO METHOD
     ******************************************************************************/
    
    public int compareTo(PFSPJob other) 
    {
    	// Used to sort the jobs in the efficiencyList
    	int s1 = this.getTotalProcessingTime();
    	int s2 = other.getTotalProcessingTime();

    	int value = 1; // s1 comes after s2 in the list

    	if( s1 > s2 ) // TIE ISSUE #1 
    		value = -1; // s1 comes before s2 in the list

    	else if( s1 == s2 && Math.random() > 0.5 )
    		value = -1; // If tie, do it random
        
    	return value;
    }
    
    /******************************************************************************* 
     * TO STRING METHOD
     ******************************************************************************/
    @Override
    public String toString()
    {
        String s = "";
        s = s.concat("\n Job Id: " + this.getId() + " ");
        s = s.concat("\n Job Total Time: " + this.getTotalProcessingTime() + " ");
        return s;
    }

	public void setVariance(int machine, int variance) {
	    varianceList[machine] = variance;		
	}
    
	public int getVariance(int machine) { 
		 return varianceList[machine];
	}

	@Override
	public void setProcessingTimes(int[] processingTimes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[] getProcessingTimes() {
		// TODO Auto-generated method stub
		return null;
	}
}