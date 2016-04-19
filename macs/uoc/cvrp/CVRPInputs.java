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