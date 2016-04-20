/** 
 * MACS - Multi-Agent Cooperative Search is a framework to develop cooperating agents using 
 * different Metaheuristics Copyright (C) 2016 Simon Martin Angel Alejandro Juan Perez. This file is part of MACS. 
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
package macs.heuristics;


import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

import macs.ontologies.SolutionWrapper;
import macs.ontologies.entities.Solution;
import macs.ontologies.entities.SolutionData;
import macs.ontologies.entities.problems.SolutionElements;
import macs.uoc.api.Test;
import macs.uoc.pfsp.api.InputManager;
import macs.uoc.pfsp.api.PFSPInputs;
import macs.uoc.pfsp.app.ILSESP;
import macs.uoc.pfsp.app.PFSPTest;
import macs.uoc.pfsp.baker.BakerInputsManager;
import macs.uoc.pfsp.base.BaseInputsManager;


	public class PFSPSolver {
	
	
	
	/**
	 *  This program is used by the launcher to reading the problem files and build an initial solution. It also build a SolutionData object
	 *  This object is used by the meta-heuristic agents to solves  given problem. This object is serialised by the launcher agent and sent
	 *  to the meta-heuristic agents.
	 * @param inputNodesPath
	 * @return SolutionData
	 * @throws FileNotFoundException
	 */
	public static SolutionData solve(String inputNodesPath) throws FileNotFoundException{
		boolean bakerInput = false; 
		
		//1. Get the test object -- In this case it is hard coded
		//# instance | maxTime(sec) | nIter | distribution | beta1 | beta2 | seed --- Test object format
		// tai001_20_5	5	1000	t	0.2	0.2	258258
		PFSPTest aTest = new PFSPTest("null",5,1000,"t",new Float(0.2),new Float(0.2),258258);
		
		//2. Parse the data file and create an Inputs object	
     	
        // 2.1. GET THE INSTANCE INPUTS (JOBS DATA)
		
		String folder;
		char kCode = aTest.getInstanceName().charAt(0); 
		if( kCode == 't' ){
			folder = "taillard";
		}
		else if (kCode == 'b') {
			folder = "baker";
			bakerInput = true; 
		} 
		else {
			folder = "watson";
		}
		
		InputManager inMngr;
		if(bakerInput)	inMngr = new BakerInputsManager(inputNodesPath);
		else inMngr = new BaseInputsManager(inputNodesPath);		
		PFSPInputs aInputs = inMngr.getInputs();

        //3. Set random number generator
		Random rngJava = new Random(aTest.getSeed());
        aTest.setRandom(rngJava);       

        //4. Run the NEH algorithm using NEH Wrapper class      
        
        //4.1 RUN THE ALGORITHM
        SolutionData output = ILSESP.run(aTest,aInputs);
        output.setRow(aInputs.getNumberOfMachines());
        output.setColumn(aInputs.getNumberOfJobs());

    	return output;
	}
		
	public static Solution solve(Solution solution, Test aTest) throws FileNotFoundException{
	
		PFSPTest test = (PFSPTest) aTest;
		
		//1. Get the test object -- In this case it is hard coded
		//# instance | maxTime(sec) | nIter | distribution | beta1 | beta2 | seed --- Test object format
		// tai001_20_5	5	1000	t	0.2	0.2	258258
		//aTest = new PFSPTest("tai001_20_5",5,1000,"t",new Float(0.2),new Float(0.2),258258);
		
		//2. Parse the data file and create an Inputs object	
     	
        // 2.1. GET THE INSTANCE INPUTS (JOBS DATA)
		
		PFSPInputs aInputs = SolutionWrapper.getInstance().getPFSPInputs();
	
        //3. Set random number generator
		Random rngJava = new Random(aTest.getSeed());
        aTest.setRandom(rngJava);       

        //4. Run the NEH algorithm using NEH Wrapper class      
        
        //4.1 RUN THE ALGORITHM
        Solution output = ILSESP.solve(solution, test,aInputs);
      

    	return output;
	}
	
	
	
	
	
	
	
	
	
	private static List<SolutionElements> getNodes(){
		List<SolutionElements> nodes = SolutionWrapper.getInstance().getNodes();
	
		return nodes;
		
	}
	


}
