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
import java.util.LinkedList;

/**
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class VRPSolution
{
    /* INSTANCE FIELDS & CONSTRUCTOR */
    private static long nInstances = 0; // number of instances
    private long id; // solution ID
    private double costs = 0.0; // solution costs
    private double demand = 0.0F; // total demand
    private LinkedList<Route> routes; // list of routes in this solution
    private double time = 0.0; // elapsed computational time (in seconds)

    public VRPSolution()
    {   nInstances++;
        id = nInstances;
        routes = new LinkedList<Route>();
    }
   
    /* GET METHODS */
    public LinkedList<Route> getRoutes(){return routes;}
    public long getId(){return id;}
    public double getCosts(){return costs;}
    public double getDemand(){return demand;}
    public double getTime(){return time;}
    
    /* SET METHODS */
    public void setCosts(double c){costs = c;}
    public void setDemand(double d){demand = d;}
    public void setTime(double t){time = t;}
    
    /*  AUXILIARY METHODS */
    
    public void addCosts(double nCost){
    	this.costs =  this.costs+nCost;
    }
    
    @Override
    public String toString()
    {
        Route aRoute; // auxiliary Route variable
        String s = "";
        s = s.concat("\r\n");
        s = s.concat("Sol ID : " + getId() + "\r\n");
        s = s.concat("Sol costs: " + getCosts() + "\r\n");
        s = s.concat("# of routes in sol: " + routes.size());
        s = s.concat("\r\n\r\n\r\n");
        s = s.concat("List of routes (cost and nodes): \r\n\r\n");
        for (int i = 1; i <= routes.size(); i++)
        {   aRoute = routes.get(i - 1);
            s = s.concat("Route " + i + " || ");
            s = s.concat("Costs = " + aRoute.getCosts() + " || ");
            s = s.concat("Demand  = " + aRoute.getDemand()+ " || ");
            s = s.concat("\r\n");
        }
        s = s.concat("\r\n\r\n");
        return s;
    }
}