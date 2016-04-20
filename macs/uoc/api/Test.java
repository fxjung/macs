/** 
 * MACS - Multi-Agent Cooperative Search is a framework to develop cooperating agents using 
 * different Metaheuristics Copyright (C) 2016 Simon Martin Angel Alejandro Juan Perez. This file is part of MACS. 
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
	  
   
