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