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

import macs.ontologies.entities.problems.NodeData;

/**
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class Node extends NodeData
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* INSTANCE FIELDS & CONSTRUCTOR */
    private int id; // node ID (depotID = 0)
    private float x; // node x coordinate
    private float y; // node y coordinate
    private float demand; // node demand
    private Route inRoute = null; // route containing the node
    private boolean isInterior = false; // interior node in a route
    private VRPEdge diEdge = null; // edge from depot to node
    private VRPEdge idEdge = null; // edge from node to depot

    public Node(int nodeId, float nodeX, float nodeY, float nodeDemand)
    {   id = nodeId;
        x = nodeX;
        y = nodeY;
        demand = nodeDemand;
       
    }

    /* SET METHODS */
    public void setInRoute(Route r){inRoute = r;}
    public void setIsInterior(boolean value){isInterior = value;}
    public void setDiEdge(VRPEdge e){diEdge = e;}
    public void setIdEdge(VRPEdge e){idEdge = e;}

    /* GET METHODS */
    public int getId(){return id;}
    public float getX(){return x;}
    public float getY(){return y;}
    
    public float getDemand(){return demand;}
    public Route getInRoute(){return inRoute;}
    public boolean getIsInterior(){return isInterior;}
    public VRPEdge getDiEdge(){return diEdge;}
    public VRPEdge getIdEdge(){return idEdge;}

    /* AUXILIARY METHODS */
    @Override
    public String toString() 
    {   String s = "";
        s = s.concat(this.getId() + " ");
        s = s.concat(this.getX() + " ");
        s = s.concat(this.getY() + " ");
        s = s.concat(this.getDemand() + "");
        return s;
    }
}