package macs.uoc.pfsp.app;
import java.util.Random;

import macs.uoc.api.Test;
import macs.uoc.pfsp.api.PFSPInputs;


/***********************************************************************************
 * Project SimScheduling - Randomness.java
 * 
 * This class manages the random behavior of the program. 
 * 
 * Date of last revision (YYMMDD): 110407
 * (c) Angel A. Juan, Quim Castella - http://ajuanp.wordpress.com
 **********************************************************************************/

public class Randomness
{
	/******************************************************************************* 
     * INSTANCE FIELDS 
     * ****************************************************************************/
    
    private Test aTest;
    private PFSPInputs inputs;

    
    /******************************************************************************* 
     * CLASS CONSTRUCTOR 
     * ****************************************************************************/

    public Randomness(Test test, PFSPInputs inputs2)
    {     
    	aTest = test;
    	inputs = inputs2;
    }


    /*******************************************************************************
     *  PUBLIC METHOD calcPositionsArray()  
     ******************************************************************************/
    
    public int[] calcPositionsArray(String distribution)
    {
        int nJobs = inputs.getNumberOfJobs();

    	int[] posArray = new int[nJobs];
    	int[] auxArray = new int[nJobs]; // array of "pointers" to jobs in effList
    	       
    	// Reset auxArray
    	for( int i = 0; i < nJobs; i++ )
        	auxArray[i] = i; 

    	// Assign new random positions
    	for( int i = 0; i < nJobs; i++ )
    	{
    		int pos = getRandomPosition(nJobs - i, distribution);
    		posArray[i] = auxArray[pos];
            System.arraycopy(auxArray, pos+1, auxArray, pos, nJobs-i-1-pos);
//    		for( int j = pos; j < nJobs - i - 1; j++ )
//    			auxArray[j] = auxArray[j + 1];
    	}
    	return posArray;
    }

    /*******************************************************************************
     *  PUBLIC METHOD getRandomPosition()
     ******************************************************************************/
    
    public int getRandomPosition(int n, String dist) // random between 0 and n-1
    {

        Random rng = aTest.getRandom();

    	int pos = 0;
    	char distribution = dist.charAt(0);
    	if( distribution == 't' || distribution == 'T' ) // Triangular
    	{
    		pos = (int) (n * (1 - Math.sqrt(rng.nextDouble())));
    	}
    	else // Uniform
    	{
    		int m = 1;
    		if(n > 1){
    			m = n-1;
    		}
    		
            pos = rng.nextInt(m);
    	}
    	return pos;
    }
    
}