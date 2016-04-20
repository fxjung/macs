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
package macs.uoc.cvrp;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class MultiStartTester
{
    final static String inputFolder = "/home/smartin/workspace/cvrp/inputs";
    final static String outputFolder = "/home/smartin/workspace/cvrp/outputs";
    final static String testFolder = "//home/smartin/workspace/cvrp/tests";
    final static String fileNameTest = "testsSpm.txt";
    //final static String sufixFileNodes = "_input_nodes.txt";
    final static String sufixFileNodes = ".vrp";
    final static String sufixFileVehicules = "_input_vehicles.txt";
    final static String sufixFileOutput = "_outputs.txt";

    public static void main( String[] args ) throws FileNotFoundException
    {
        System.out.println("****  WELCOME TO THIS PROGRAM  ****");
        long programStart = ElapsedTime.systemTime();
        
        /* 1. GET THE LIST OF TESTS TO RUN FORM "test2run.txt"
              aTest = instanceName + testParameters */
        String testsFilePath = testFolder + File.separator + fileNameTest;
        ArrayList<CVRPTest> testsList = TestsManager.getTestsList(testsFilePath);

        /* 2. FOR EACH TEST (instanceName + testParameters) IN THE LIST... */
        int nTests = testsList.size();
        System.out.println("numbre de test "+nTests);
        for( int k = 0; k < nTests; k++ )
        {   CVRPTest aTest = testsList.get(k);
            System.out.println("\n# STARTING TEST " + (k + 1) + " OF " + nTests);

            // 2.1 GET THE INSTANCE INPUTS (DATA ON NODES AND VEHICLES)
            // "instanceName_input_nodes.txt" contains data on nodes
            String inputNodesPath = inputFolder + File.separator +
                    aTest.getInstanceName() + sufixFileNodes;
            //String inputVehPath = inputFolder + File.separator + aTest.getInstanceName() + sufixFileVehicules;

            // Read inputs files (nodes) and construct the inputs object
            //Inputs inputs = InputsManager.readInputs(inputNodesPath, inputVehPath);
            CVRPInputs inputs = InputsManager.getTSPLIBFormat(inputNodesPath);
            InputsManager.generateDepotEdges(inputs);
            InputsManager.generateSavingsList(inputs);
            
            // 2.2. USE THE MULTI-START ALGORITHM TO SOLVE THE INSTANCE
            //Random rng = new Random();
           // MultiStart algorithm = new MultiStart(aTest, inputs, rng);
           // Outputs output = algorithm.solve();
            
            // 2.3. PRINT OUT THE RESULTS TO FILE "instanceName_seed_outputs.txt"
            /*String outputsFilePath = outputFolder + File.separator +
                   aTest.getInstanceName() + "_" + aTest.getSeed() + sufixFileOutput;*/
            //output.sendToFile(outputsFilePath);
        }

        /* 3. END OF PROGRAM */
        System.out.println("\n****  END OF PROGRAM, CHECK OUTPUTS FILES  ****");
            long programEnd = ElapsedTime.systemTime();
            System.out.println("Total elapsed time = "
                + ElapsedTime.calcElapsedHMS(programStart, programEnd));
    }
}