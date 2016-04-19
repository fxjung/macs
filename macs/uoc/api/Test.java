package macs.uoc.api;

import java.util.Random;

public interface Test {    
	
	
	//General
	public String getInstanceName();
	public float getMaxTime();
	public String getDistribution();
	public int getSeed();

	
	//PFSP Methods
	public int getNIter();
	public float getBeta1();
	public float getBeta2();
	public Random getRandom();
	public void setRandom(Random rng);
	
	
	//CVRP Methods
	
	 public float getMaxRouteCosts();
	 public float getServiceCosts();	    
	 public float getFirstParam();
	 public float getSecondParam();
}
	  
   
