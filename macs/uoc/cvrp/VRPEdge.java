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



/**
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class VRPEdge implements Comparable<VRPEdge>
{
    /* INSTANCE FIELDS & CONSTRUCTOR */
    private Node origin; // origin node
    private Node end; // end node
    private double costs = 0.0; // edge costs
    private double savings = 0.0; // edge savings (Clarke & Wright)
    private Route inRoute = null; // route containing this edge (0 if no route assigned)
    private VRPEdge inverseEdge = null; // edge with inverse direction
            
    public VRPEdge(Node originNode, Node endNode) 
    {   origin = originNode;
        end = endNode;
    }

    /* SET METHODS */
    public void setCosts(double c){costs = c;}
    public void setSavings(double s){savings = s;}
    public void setInRoute(Route r){inRoute = r;}
    public void setInverse(VRPEdge e){inverseEdge = e;}

    /* GET METHODS */
    public Node getOrigin(){return origin;}
    public Node getEnd(){return end;}
    public double getCosts(){return costs;}
    public double getSavings(){return savings;}
    public Route getInRoute(){return inRoute;}
    public VRPEdge getInverseEdge(){return inverseEdge;}

    /* AUXILIARY METHODS */
    
    public double calcCosts(Node origin, Node end)
    {   double X1 = origin.getX();
        double Y1 = origin.getY();
        double X2 = end.getX();
        double Y2 = end.getY();
        double d = Math.sqrt((X2 - X1) * (X2 - X1) + (Y2 - Y1) * (Y2 - Y1));
        return d;
    }

    public double calcSavings(Node origin, Node end, Node depot)
    {   double X1 = origin.getX();
        double Y1 = origin.getY();
        double X2 = end.getX();
        double Y2 = end.getY();
        double Xd = depot.getX();
        double Yd = depot.getY();
        // Costs of originNode to depot
        double odC = Math.sqrt((Xd - X1)*(Xd - X1) + (Yd - Y1)*(Yd - Y1));
        // Costs of depot-(0, 0) to endNode
        double deC = Math.sqrt((X2 - Xd)*(X2 - Xd) + (Y2 - Yd)*(Y2 - Yd));
        // Costs of originNode to endNode
        double oeC = Math.sqrt((X2 - X1)*(X2 - X1) + (Y2 - Y1)*(Y2 - Y1));
        //Return cost depot to savings
        return odC + deC - oeC;
    }
    
    
    
    
    @Override
    public int compareTo(VRPEdge otherEdge) 
    {   VRPEdge other = otherEdge;
        double s1 = this.getSavings();
        double s2 = other.getSavings();
        if( s1 < s2 )
            return -1;
        else
            return 1;
    }
    
    @Override
    public String toString() 
    {   String s = "";
        s = s.concat("\nEdge origin: " + this.getOrigin());
        s = s.concat("\nEdge end: " + this.getEnd());
        s = s.concat("\nEdge costs: " + (this.getCosts()));
        s = s.concat("\nEdge savings: " + (this.getSavings()));
        return s;
    }
}