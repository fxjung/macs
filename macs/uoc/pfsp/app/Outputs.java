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
package macs.uoc.pfsp.app;
import java.io.IOException;
import java.io.PrintWriter;

/***********************************************************************************
 * Project SimScheduling - Outputs.java
 * 
 * This class represents the outputs (NEH solution and OBS) for a given instance. 
 * 
 * Date of last revision (YYMMDD): 100103
 * (c) Angel A. Juan - http://ajuanp.wordpress.com
 **********************************************************************************/

public class Outputs
{

    /*******************************************************************************
     *  INSTANCE FIELDS 
     ******************************************************************************/

	private PFSPSolution nehSol; // NEH solution
	private PFSPSolution ourBestSol; // Best solution provided by the SSGNEH\
    private PFSPSolution bestStochSol; 
	private double[] detSols = null;
	private double[] stochSols = null;
    
	/*******************************************************************************
     *  CLASS CONSTRUCTOR 
     ******************************************************************************/
    
    public Outputs(PFSPSolution neh, PFSPSolution obs)
    {
    	nehSol = neh;
    	ourBestSol = obs;
    }
    
    public Outputs(PFSPSolution neh, PFSPSolution obs, PFSPSolution stochSol)
    {
    	this(neh, obs, stochSol, null, null);
    }
    
    public Outputs(PFSPSolution neh, PFSPSolution obs, PFSPSolution stochSol, double[] dSols, double[] sSols)
    {
    	nehSol = neh;
    	ourBestSol = obs;
    	bestStochSol = stochSol;
    	detSols = dSols;
    	stochSols = sSols;
    }

    public PFSPSolution getOurBestSol(){
        return ourBestSol;
    }
    public PFSPSolution getNehSol(){
        return nehSol;
    }

    /*******************************************************************************
     * PUBLIC METHOD sendToFile()
     ******************************************************************************/
    
    public void sendToFile(String filePath)
    {
    	try
    	{
    		PrintWriter out = new PrintWriter(filePath);
    		if(ourBestSol != bestStochSol) System.err.println("Look at "+filePath);
    	    out.println("***************************************************");
    	    out.println("*       RESULTS FROM SIMSCHEDULING PROJECT         *");
    	    out.println("***************************************************");
    	        
    	    out.println("\r\n");
    	    out.println("--------------------------------------------");
            out.println("               NEH Solution                 ");
            out.println("--------------------------------------------");
            out.println(nehSol.toString(true));
    	    
            out.println("\r\n");
    	    out.println("--------------------------------------------");
            out.println("Our best solution (provided by the SS-GNEH) ");
            out.println("--------------------------------------------");
            out.println(ourBestSol.toString(true));
            
            out.println("\r\n");
    	    out.println("--------------------------------------------");
            out.println("Our best stoch-sol (provided by the SS-GNEH)");
            out.println("--------------------------------------------");
            out.println(bestStochSol.toString(true));

            
    	    out.close();
    	}
    	catch (IOException exception)
    	{ 
    		System.out.println("Error processing outputs file: " + exception);
    	}				 
    }

 
    public void sendToFileList(String filePath)
    {
    	if(detSols != null && stochSols!= null) {
	    	try
	    	{
	    		PrintWriter out = new PrintWriter(filePath);
	    	    out.println("***************************************************");
	    	    out.println("*       RESULTS FROM SIMSCHEDULING PROJECT         *");
	    	    out.println("***************************************************");
	    	    
	    	    out.println("DET");
	    	    for(double value: detSols) { 
	    	    	out.print(value+"\t"); 
	    	    }
	    	    out.println();
	            out.println("STOCH");
	            for(double value: stochSols) { 
	    	    	out.print(value+"\t"); 
	    	    }
	    	    out.close();
	    	}
	    	catch (IOException exception)
	    	{ 
	    		System.out.println("Error processing outputs file: " + exception);
	    	}		
    	}
    }

}