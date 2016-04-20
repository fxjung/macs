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
package macs.uoc.cvrp;

import java.util.Random;

import macs.uoc.api.Test;

/**
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class CVRPTest implements Test
{
    /* INSTANCE FIELDS AND CONSTRUCTOR */
    private String instanceName;
    private float maxRouteCosts; // Maximum costs allowed for any single route
    private float serviceCosts; // Costs of completing an individual service
    private float maxTime; // Maximum computing time allowed
    private String distrib; // Statistical distribution for the randomness
    private float firstParam; // First parameter associated with the distribution
    private float secondParam; // Second parameter associated with the distribution
    private int seed; // Seed value for the Random Number Generator (RNG)
 
    
    //Instance name | Max Route Cost | Service Cost | Nax Time | Distribution | param1 | param2 |Seed
    public CVRPTest(String name, float rCosts, float sCosts, float t, 
            String d, float p1, float p2, int s)
    {
        instanceName = name;
        maxRouteCosts = rCosts;
        serviceCosts = sCosts;
        maxTime = t;
        distrib = d;
        firstParam = p1;
        secondParam = p2;
        seed = s;
    }

    /* GET METHODS */
    public String getInstanceName(){return instanceName;}
    public float getMaxRouteCosts(){return maxRouteCosts;}
    public float getServiceCosts(){return serviceCosts;}
    public float getMaxTime(){return maxTime;}
    public String getDistribution(){return distrib;}
    public float getFirstParam(){return firstParam;}
    public float getSecondParam(){return secondParam;}
    public int getSeed(){return seed;}

	@Override
	public int getNIter() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getBeta1() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getBeta2() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Random getRandom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRandom(Random rng) {
		// TODO Auto-generated method stub
		
	}
}