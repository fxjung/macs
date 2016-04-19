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