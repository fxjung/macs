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
/***********************************************************************************
 * Project SimScheduling - Test.java
 * 
 * This class represents a test to be run.
 * 
 * Date of last revision (YYMMDD): 100103
 * (c) Angel A. Juan - http://ajuanp.wordpress.com
 **********************************************************************************/

import java.util.Random;

import macs.uoc.api.Test;



public class PFSPTest implements Test
{

    /*******************************************************************************
     *  INSTANCE FIELDS 
     ******************************************************************************/

	private String instanceName;
	private int maxTime; // maximum computational time (in minutes)
	private int nIter; // number of first-level iterations
	private String distribution;
	private float beta1;
	private float beta2;
	private int seed;

    //private RandomStream rng = null; // Random Number Generator (l'Ecuyer SSJ)
	private Random rng = null;

	/*******************************************************************************
     *  CLASS CONSTRUCTOR 
     ******************************************************************************/
    
    public PFSPTest(String name, int time, int n, String d, float b1, float b2, int s)
    {
    	instanceName = name;
    	maxTime = time;
    	nIter = n;
    	distribution = d;
    	beta1 = b1;
    	beta2 = b2;
    	seed = s;
    }
	
    /*******************************************************************************
     * GET METHODS
     ******************************************************************************/
    
    public String getInstanceName()
    {
        return instanceName;
    }
    
    public float getMaxTime()
    {
        return maxTime;
    }
    
    public int getNIter()
    {
        return nIter;
    }
    
    public String getDistribution()
    {
    	return distribution;
    }
    
    public float getBeta1()
    {
        return beta1;
    }
    
    public float getBeta2()
    {
        return beta2;
    }
    
    public int getSeed()
    {
        return seed;
    }

    public Random getRandom()
    {
        return rng;
    }

    public void setRandom(Random rng){
        this.rng = rng;
    }
  
    
    
    
    /*******************************************************************************
    * PUBLIC METHODS clone()
    *******************************************************************************/

    @Override
    public PFSPTest clone(){
        PFSPTest t = new PFSPTest(instanceName, maxTime, nIter, distribution, beta1, beta2, seed);

        t.setRandom(rng);
        
        return t;
    }

	@Override
	public float getMaxRouteCosts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getServiceCosts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFirstParam() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getSecondParam() {
		// TODO Auto-generated method stub
		return 0;
	}
}