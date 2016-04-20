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

package macs.ontologies.entities.problems;

import java.util.ArrayList;
import java.util.List;

import macs.ontologies.vocabularies.JobVocabulary;
import jade.content.Concept;

public class JobData extends SolutionElements implements JobVocabulary,Concept {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/******************************************************************************* 
     * INSTANCE FIELDS 
     ******************************************************************************/
	private int id; // job ID
	private List<Integer> processingTimes;    
	private int totalProcessingTime; // total processing time for this job
	
	/******************************************************************************* 
     * CONSTRUCTOR
     ******************************************************************************/
	public JobData(int order, int nMachines)
    {
    	id = order + 1;
    	
    	processingTimes = new ArrayList<Integer>(nMachines);
        totalProcessingTime = 0; 
    }
	
	public JobData(){
		
		processingTimes = new ArrayList<Integer>();
	}
	/******************************************************************************* 
     * SET METHODS 
     ******************************************************************************/
   
    public void setId(int jobId)
    { 
    	id = jobId; 
    }
     
       
    public void setProcessingTime(int machine, int time)
    {
    	
        processingTimes.add(machine,time);
    }
   
    public void setProcessingTimes(List<Integer> list)   
    {    	
        processingTimes = list;
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
   
    
    public int getProcessingTime(int machine)
    {
    	
        return processingTimes.get(machine);
    }
   public List<Integer> getProcessingTimes()  
    {
        return processingTimes;
    }
    
    public int getTotalProcessingTime()
    {
        return totalProcessingTime;
    }

}
