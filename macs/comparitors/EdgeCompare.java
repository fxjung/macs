/** 
 * MACS - Multi-Agent Cooperative Search is a framework to develop cooperating agents using 
 * different Metaheuristics Copyright (C) 2016 Simon Martin. This file is part of MACS. 
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
package macs.comparitors;

import java.util.Comparator;

import macs.ontologies.entities.Edge;


public class EdgeCompare implements Comparator<Edge> {


/* (non-Javadoc)
 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
 */

@Override
public synchronized int compare(Edge o1, Edge o2) {
	
	// TODO Auto-generated method stub
		int value = 0;
		try{
		if(o1.getCosts() > o2.getCosts()){
				value = 1;
			}
			else if (o1.getCosts() < o2.getCosts()){
				value = -1;
			}
			else {
				value = 0;
			}
		}
		catch(NullPointerException ne){
			ne.printStackTrace();
		}
		return value;
	}
	  
}//end EdgeCompare


