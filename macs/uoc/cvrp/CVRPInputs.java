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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;







import macs.ontologies.entities.Edge;


/**
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class CVRPInputs 
{
    /* INSTANCE FIELDS & CONSTRUCTOR */
    private Node[] nodes; // List of all nodes in the problem/sub-problem
    private float vCap = 0.0F; // Vehicle capacity (homogeneous fleet)
    private LinkedList<VRPEdge> savings = null; 
    private float[] vrpCenter; // (x-bar, y-bar) is a geometric VRP center
   
    public CVRPInputs(int n)
    {   nodes = new Node[n]; // n nodes, including the depot
        vrpCenter = new float[2];
    }
    public CVRPInputs(){
    	vrpCenter = new float[2];
    }


    /* GET METHODS */
    public Node[] getNodes(){return nodes;}
    public LinkedList<VRPEdge> getSavings(){return savings;}
    public float getVehCap(){return vCap;}
    public float[] getVrpCenter(){return vrpCenter;}

    /* SET METHODS */
    public void setVrpCenter(float[] center){vrpCenter = center;}
    public void setVehCap(float c){vCap = c;}
    public void setList(LinkedList<VRPEdge> sList){savings = sList;}
    public void setNodes(Node[] nodes){this.nodes = nodes;}
   
    
    
    /*Auxillary Methods from Agent PLatform*/
    /**
     * This method tkaes the lightweight list of good edges identified by the pattern matcher and locates them the heavy weight VRPEdges list
     * It identifies them, removes them from the list and adds the to the head of the savings list 
     * 
     * @param edges
     * @param vrpEdges
     * @return LinkedList<VRPEdge>
     */
    public LinkedList<VRPEdge> sortEdges(List<Edge> edges,LinkedList<VRPEdge> sav){
		
		LinkedList<VRPEdge> vrpEdgesCopy = new LinkedList<VRPEdge>(sav);
		Collections.copy(vrpEdgesCopy, getSavings());
		
		if(!edges.isEmpty()){
			for(Edge edge : edges){
				
				for(VRPEdge vrpEdge : vrpEdgesCopy){					
					
					if(edge.getFirst()==vrpEdge.getOrigin().getId()&&edge.getSecond()==vrpEdge.getEnd().getId()
							||edge.getFirst()==vrpEdge.getEnd().getId()&&edge.getSecond()==vrpEdge.getOrigin().getId()){
						
						sav.remove(vrpEdge);						
						sav.add(0,vrpEdge);
						
						
					}
					
				}
			}
		}
		
		return sav;
		
    }
	
	
}