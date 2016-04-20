
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
package macs.uoc.cvrp;
import java.util.LinkedList;
import java.util.List;



/**
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class Route 
{
    /* INSTANCE FIELDS & CONSTRUCTOR */
    private double costs = 0.0; // route total costs
    private double demand = 0.0F; // route total demand
    private LinkedList<VRPEdge> vRPEdges; // edges list
    private float[] center; // (x-bar, y-bar) for all (x,y) in the route

    public Route() 
    {   vRPEdges = new LinkedList<VRPEdge>();
        center = new float[2];
    }
    
    /* SET METHODS */
    public void setCosts(double c){costs = c;}
    public void setDemand(double d){demand = d;}
    public void setCenter(float[] coord){center = coord;}
    public void setEdges(LinkedList<VRPEdge> e){vRPEdges = e;}

    /* LIST GET METHODS*/
    public double getCosts(){return costs;}
    public double getDemand(){return demand;}
    public float[] getCenter(){return center;}
    public List<VRPEdge> getEdges(){return vRPEdges;}
    
    /* AUXILIARY METHODS */

    /** 
     * Reverses a route, e.g. (0 -> 2 -> 6 -> 0) becomes (0 -> 6 -> 2 -> 0)
     */
  
    public void reverse()
    {   
        for( int i = 0; i < vRPEdges.size(); i++ )
        {   VRPEdge e = vRPEdges.get(i);
        	VRPEdge invE = e.getInverseEdge();
            vRPEdges.remove(e);
            vRPEdges.add(0, invE);
        }
    }
/////////////////////
public void addEdge(VRPEdge anEdge)
{   if( vRPEdges.contains(anEdge) == false )
vRPEdges.add(anEdge);
}

public void removeEdge(VRPEdge anEdge)
{   if( vRPEdges.contains(anEdge) == true )
	vRPEdges.remove(anEdge);
}
public void removeCosts(VRPEdge anEdge)
{   costs -= anEdge.getCosts();
}
public void addCosts(VRPEdge anEdge)
{   costs += anEdge.getCosts();
}

public void substractCosts(VRPEdge e1) {
removeCosts(e1);
}
    @Override
    public String toString() 
    {   String s = "";
        s = s.concat("\nRute costs: " + (this.getCosts()));
        s = s.concat("\nRuta demand:" + this.getDemand());
        s = s.concat("\nRuta edges: " + this.getEdges());
        return s;
    }
}