package macs.uoc.pfsp.base;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import macs.uoc.pfsp.api.InputManager;
import macs.uoc.pfsp.api.PFSPJob;


/***********************************************************************************
 * Project SimScheduling - InputsManager.java
 * 
 * This class reads the inputs from the inputs file.
 * 
 * Date of last revision (YYMMDD): 100103
 * (c) Angel A. Juan - http://ajuanp.wordpress.com
 **********************************************************************************/

public class BaseInputsManager implements InputManager
{

    /*******************************************************************************
     *  INSTANCE FIELDS 
     ******************************************************************************/

	private String filePath; // path of the inputs file
	private BaseInputs inputs;
	
	/*******************************************************************************
     *  CLASS CONSTRUCTOR 
     ******************************************************************************/
    
    public BaseInputsManager(String inputsFilePath)
    {
    	filePath = inputsFilePath;
    }
    
    /*******************************************************************************
     *  METHOD getInputs() 
     ******************************************************************************/
    
    public BaseInputs getInputs()
    {
    	try
        {
        	FileReader reader = new FileReader(filePath);
            Scanner in = new Scanner(new BufferedReader(reader));
            // The two first lines of this file should be like this:
            //   # nJobs | nMachines 	<- line 0
            //  	 20 		5 		<- line 1
            in.nextLine(); // Skip line 0
            int nJobs = in.nextInt();
            int nMachines = in.nextInt();
            inputs = new BaseInputs(nJobs, nMachines);
           
            // The rest of the lines should be like this:
            //   # 		m0 		| 	m1	 | 		m2 		<- line 2 (times job0)
            //   		21       	23			32		<- line 3 (times job1)
            //   		30      	31			10		<- line 4 (times job2)
            //   ...         ...       ...
            in.nextLine(); // Go to the beginning of line 2
            in.nextLine(); // Go to the beginning of line 3
            for( int i = 0; i < nJobs; i++ )
            {
            	PFSPJob iJob = inputs.getJobs()[i];
            	int totalTime = 0;
            	int time = 0;
            	for( int j = 0; j < nMachines; j++ )
            	{
                    time = in.nextInt();
                    iJob.setProcessingTime(j, time);
                    totalTime = totalTime + time;
            	}
            	iJob.setTotalProcessingTime(totalTime);
            }
            in.close();
        }
        catch (IOException exception)
        { 
          	System.out.println("Error processing inputs file: " + exception);
        }
    	
    	return inputs;
    }

    
}