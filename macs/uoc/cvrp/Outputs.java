/** 
 * MACS - Multi-Agent Cooperative Search is a framework to develop cooperating agents using 
 * different Metaheuristics Copyright (C) 2016  Angel Alejandro Juan Perez. This file is part of MACS. 
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
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class Outputs
{
    /* INSTANCE FIELDS & CONSTRUCTOR */
    private VRPSolution cwsSolution;
    private VRPSolution bestSol;
    public Outputs()
    {   cwsSolution = null;
        bestSol = null;
    }

    /* SET METHODS */
    public void setCWSSol(VRPSolution cwsSol){cwsSolution = cwsSol;}
    public void setOBSol(VRPSolution obSol){bestSol = obSol;}

    /* GET METHODS */
    public VRPSolution getCWSSol(){return cwsSolution;}
    public VRPSolution getOBSol(){return bestSol;}

    /* AUXILIARY METHODS */
    public void sendToFile(String outFile)
    {
        try 
        {   PrintWriter out = new PrintWriter(outFile);
            out.println("***************************************************");
            out.println("*                      OUTPUTS                    *");
            out.println("***************************************************");
            out.println("\r\n");
            out.println("--------------------------------------------");
            out.println("Clarke & Wright Solution (parallel version)");
            out.println("--------------------------------------------");
            out.println(cwsSolution.toString() + "\r\n");
            out.println("--------------------------------------------");
            out.println("\r\n OUR BEST SOLUTION:\r\n");
            out.println("--------------------------------------------");
            out.println(bestSol.toString() + "\r\n");
            out.close();
        } catch (IOException exception) 
        {   System.out.println("Error processing output file: " + exception);
        }
    }
}