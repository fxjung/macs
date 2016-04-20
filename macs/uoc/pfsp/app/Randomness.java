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