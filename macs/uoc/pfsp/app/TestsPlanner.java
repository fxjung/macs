package macs.uoc.pfsp.app;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/***********************************************************************************
 * Project SimORouting - TestPlanner.java
 * 
 * This class creates a list of tests to be run.
 * 
 * Date of last revision (YYMMDD): 091205
 * (c) Angel A. Juan - http://ajuanp.wordpress.com
 **********************************************************************************/

public class TestsPlanner 
{
	
    /*******************************************************************************
     *  INSTANCE FIELDS 
     ******************************************************************************/
	
	private String testsFilePath;
	private ArrayList<PFSPTest> list;
	
	/*******************************************************************************
     *  CLASS CONSTRUCTOR 
     ******************************************************************************/
    
    public TestsPlanner(String path)
    {
    	testsFilePath = path;
    	list = new ArrayList<PFSPTest>();
    }
	    
    /*******************************************************************************
     * PUBLIC METHOD getTestsList()
     ******************************************************************************/
	
    public ArrayList<PFSPTest> getTestsList()
    {
    	try
        {
        	FileReader reader = new FileReader(testsFilePath);
            Scanner in = new Scanner(reader);
            
            //A�adido para que use la configuraci�n regional de la maquina.
            in.useLocale(Locale.ROOT);  
            
            // The two first lines (lines 0 and 1) of this file are like this:
            // # instance | maxTime(min) | nIter | distrib | beta1 | beta2 | seed
            // tai003_20_5		10 		    1000  triangular  0.20 	  0.25   32321
            while ( in.hasNextLine() )
            {	
            	String s = in.next();
            	if( s.charAt(0) == '#') // this is a comment line
            	{
            		in.nextLine(); // skip comment lines
            	}
            	else
            	{
            		String instance = s;
                	int maxTime = in.nextInt();
                	int nIter = in.nextInt();
                	String distribution = in.next();
                    float beta1 = in.nextFloat();
                    float beta2 = in.nextFloat();
                    int seed = in.nextInt();
                   
            		PFSPTest aTest = new PFSPTest(instance, maxTime, nIter, distribution,
            				beta1, beta2, seed);
            		list.add(aTest);
            	}
            }
            in.close();
        }
        catch (IOException exception)
        { 
          	System.out.println("Error processing tests file: " + exception);
        }

    	return list;
    }
}