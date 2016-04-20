/** 
 * MACS - Multi-Agent Cooperative Search is a framework to develop cooperating agents using 
 * different Metaheuristics Copyright (C) 2016 Angel Alejandro Juan Perez. This file is part of MACS. 
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
package macs.uoc.pfsp.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import macs.uoc.pfsp.api.InputManager;
import macs.uoc.pfsp.api.PFSPInputs;
import macs.uoc.pfsp.baker.BakerInputsManager;
import macs.uoc.pfsp.base.BaseInputsManager;


public class NEHTester {
	public static void main(String[] args){
		System.out.println("****  WELCOME TO THIS PROGRAM  ****");
		long programStart = ElapsedTime.systemTime();

        /***************************************************************************
        * 1. GET THE LIST OF TESTS TO RUN FORM "test2run.txt"
        * aTest = instanceName + testParameters
        ***************************************************************************/
        
        String testsFilePath = "/home/smartin/workspace/pfsp/inputs" + File.separator + "tests2run.txt";
		TestsPlanner planner = new TestsPlanner(testsFilePath);
		ArrayList<PFSPTest> testsList = planner.getTestsList();
		
        /***************************************************************************
        * 2. FOR EACH TEST (instanceName + testParameters) IN THE LIST...
        ***************************************************************************/
		//FastResume.genResumer("results_in_run"+programStart);
		for(PFSPTest aTest : testsList)
		{
			boolean bakerInput = false; 
//			System.out.println("\n# STARTING TEST " + (k + 1) + " OF " + nTests);      	
            // 2.1. GET THE INSTANCE INPUTS (JOBS DATA)
			String folder;
			char kCode = aTest.getInstanceName().charAt(0); 
			if( kCode == 't' )
				folder = "taillard";
			else if (kCode == 'b') {
				folder = "baker";
				bakerInput = true; 
			} else
				folder = "watson";

			String inputsFilePath = "/home/smartin/workspace/pfsp/inputs" + File.separator + folder +
			File.separator + aTest.getInstanceName() + "_inputs.txt";
			
			InputManager inMngr;
			if(bakerInput)	inMngr = new BakerInputsManager(inputsFilePath);
			else inMngr = new BaseInputsManager(inputsFilePath);
			
			PFSPInputs aInputs = inMngr.getInputs();

            Random rngJava = new Random(aTest.getSeed());
            aTest.setRandom(rngJava);       

            int s = Math.max(aTest.getSeed(), 128);
            int seedArray[] = {s, s, s, s};
           
           
       
            
            //NEH nehAlg = new NEH(aTest, aInputs);
           
            
            // 2.3 RUN THE ALGORITHM
            //nehAlg.run();

            // 2.4 SELECT THE BEST SOLUTION SO FAR
            //Outputs output = nehAlg.getOutput();
            
            
 
			// 2.5 PRINT OUT THE RESULTS TO A FILE
			
            
            
            
            
			// 2.6 END OF THE CURRENT TEST
			long testEnd = ElapsedTime.systemTime();
			//System.out.println("Elapsed time for this test = " +
				//ElapsedTime.calcElapsedHMS(testStart, testEnd)+"\n");
            
            FastResume.flush();
		}

        /***************************************************************************
        * 3. END OF PROGRAM
        ***************************************************************************/
		System.out.println("\n****  END OF PROGRAM, CHECK OUTPUTS FILES  ****");
		long programEnd = ElapsedTime.systemTime();
		System.out.println("Total elapsed time = " + 
				ElapsedTime.calcElapsedHMS(programStart, programEnd));
		FastResume.finish();
    
	}
    

}
