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