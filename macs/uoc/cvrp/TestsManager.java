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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Generates a list of tests to be run.
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class TestsManager
{
    public static ArrayList<CVRPTest> getTestsList(String testsFilePath)
    {   ArrayList<CVRPTest> list = new ArrayList<CVRPTest>();

        try
        {   FileReader reader = new FileReader(testsFilePath);
            Scanner in = new Scanner(reader);
            // The two first lines (lines 0 and 1) of this file are like this:
            //# instance | maxRouteCosts | serviceCosts | maxTime(sec) | ...
            // A-n32-k5       10000000          0              120       ...
            while( in.hasNextLine() )
            {   String s = in.next();
                if (s.charAt(0) == '#') // this is a comment line
                    in.nextLine(); // skip comment lines
                else
                {   String instanceName = s; // e.g.: A-n32-k5
                    float maxRouteCosts = in.nextFloat(); // maxCosts in any route
                    float serviceCosts = in.nextFloat(); // marginal costs per service
                    float maxTime = in.nextFloat(); // max computational time (in sec)
                    String distrib = in.next(); // statistical distribution
                    float firstParam = in.nextFloat(); // distribution parameter
                    float secondParam = in.nextFloat(); // distribution parameter
                    int seed = in.nextInt(); // seed for the RNG
                    CVRPTest aTest = new CVRPTest(instanceName, maxRouteCosts, serviceCosts,
                        maxTime, distrib, firstParam, secondParam, seed);
                    list.add(aTest);
                }
            }
            in.close();
        }
        catch (IOException exception)
        {   System.out.println("Error processing tests file: " + exception);
        }
        return list;
    }
}