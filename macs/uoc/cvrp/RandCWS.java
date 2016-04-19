package macs.uoc.cvrp;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import macs.uoc.api.Test;



/**  
 * Biased-randomized version of the Clarke & Wright savings (CWS) heuristic.
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class RandCWS
{
    /**
     * Generates a biased-randomized solution based on the CWS heuristic.
     * @param aTest 
     * @param inputs
     * @param useRandom
     * @return CWS sol if useRandom = false; biased-randomized sol otherwise.
     */
    public static VRPSolution solve(Test aTest, CVRPInputs inputs, Random rng, 
            boolean useRandom)
    {
        /* 1. RESET VARIABLES */
        // dummySol resets isInterior and inRoute in nodes
        VRPSolution currentSol = generateDummySol(inputs);
        
        Node depot = inputs.getNodes()[0];
        int index;
        double beta = aTest.getFirstParam();
       
        /* 2. MAKE A COPY OF THE SAVINGS LIST  */
        List<VRPEdge> savingsCopy = new LinkedList<VRPEdge>();
        for( VRPEdge e : inputs.getSavings() )
        	savingsCopy.add(0, e); // Copy the savingsList in reverse order   
        
       
        /* 3. PERFORM THE EDGE-SELECTION & ROUTING-MERGING ITERATIVE PROCESS */
        while( savingsCopy.isEmpty() == false )
        {   
            // 3.1. Select the next edge from the list (either at random or not)
            if( useRandom == false ) // classical Clarke & Wright solution
                index = 0; // greedy behavior
            else // suffle the savingsList
                index = getRandomPosition(beta, rng, savingsCopy.size());           
           
            VRPEdge ijEdge = savingsCopy.get(index);
            savingsCopy.remove(ijEdge); // remove edge from listmaca.ontologies.entities.problems.
            
            // 3.2. Determine the nodes i < j that define the edge
            Node iNode = ijEdge.getOrigin();
            Node jNode = ijEdge.getEnd();

            // 3.3. Determine the routes associated with each node
            Route iR = iNode.getInRoute();
            Route jR = jNode.getInRoute();

            // 3.4. If all necessary conditions are satisfied, merge
            boolean isMergePossible = false;
            isMergePossible = checkMergingConditions(aTest, inputs, iR, jR, ijEdge);
            if( isMergePossible == true )
            {   // 3.4.1. Get an edge iE in iR containing nodes i and 0
                VRPEdge iE = getEdge(iR, iNode, depot); // iE is either (0,i) or (i,0)
                // 3.4.2. Remove edge iE from iR route and update costs
                iR.getEdges().remove(iE);
                iR.setCosts(iR.getCosts() - iE.getCosts());
                // 3.4.3. If there are more than one edge then i will be interior
                if( iR.getEdges().size() > 1 )
                    iNode.setIsInterior(true);
                // 3.4.4. If new route iR does not start at 0 it must be reversed
                if( iR.getEdges().get(0).getOrigin() != depot ) 
                    iR.reverse();
                // 3.4.5. Get an edge jE in jR containing nodes j and 0
                VRPEdge jE = getEdge(jR, jNode, depot); // jE is either (0,j) or (j,0)
                // 3.4.6. Remove edge jE from jR route
                jR.getEdges().remove(jE);
                jR.setCosts(jR.getCosts() - jE.getCosts());
                // 3.4.7. If there are more than one edge then j will be interior
                if( jR.getEdges().size() > 1 )
                    jNode.setIsInterior(true);
                // 3.4.8. If new route jR starts at 0 it must be reversed
                if( jR.getEdges().get(0).getOrigin() == depot )
                    jR.reverse(); // reverseRoute(inputs, jR);
                // 3.4.9. Add ijEdge = (i, j) to new route iR
                iR.getEdges().add(ijEdge);
                iR.setCosts(iR.getCosts() + ijEdge.getCosts());
                iR.setDemand(iR.getDemand() + ijEdge.getEnd().getDemand());
                jNode.setInRoute(iR);
                // 3.4.10. Add route jR to new route iR
                for( VRPEdge e : jR.getEdges() )
                {   
                    iR.getEdges().add(e);
                    iR.setDemand(iR.getDemand() + e.getEnd().getDemand());
                    iR.setCosts(iR.getCosts() + e.getCosts());
                    e.getEnd().setInRoute(iR);
                }
                // 3.4.11. Delete route jR from currentSolution
                currentSol.setCosts(currentSol.getCosts() - ijEdge.getSavings());
                currentSol.getRoutes().remove(jR);
            }
        }
        /* 4. RETURN THE SOLUTION */
        return currentSol;
    }
    
    /** 
     * Constructs an initial dummy feasible solution as described in the CWS
     *  heuristic: dummySol = { (0,i,0) / i in vrpNodesList }
     * During this process, inRoute and isInterior values are assigned.
     */
    private static VRPSolution generateDummySol(CVRPInputs inputs)
    { 
        VRPSolution sol = new VRPSolution();
        for( int i = 1; i < inputs.getNodes().length; i++ ) // i = 0 is the depot
        {   
        	Node iNode = inputs.getNodes()[i];
            // Get diEdge and idEdge
            VRPEdge diEdge = iNode.getDiEdge();
            VRPEdge idEdge = iNode.getIdEdge();
            // Create didRoute (and set corresponding total costs and demand)
            Route didRoute = new Route();
            didRoute.getEdges().add(diEdge);
            didRoute.setDemand(didRoute.getDemand() + diEdge.getEnd().getDemand());
            didRoute.setCosts(didRoute.getCosts() + diEdge.getCosts());
            didRoute.getEdges().add(idEdge);
            didRoute.setCosts(didRoute.getCosts() + idEdge.getCosts());
            // Update iNode properties (inRoute and isInterior)
            iNode.setInRoute(didRoute); // save route to which node belongs
            iNode.setIsInterior(false); // node is directly connected to depot
            // Add didRoute to current solution
            sol.getRoutes().add(didRoute);
            sol.setCosts(sol.getCosts() + didRoute.getCosts());
            sol.setDemand(sol.getDemand() + didRoute.getDemand());
        }
        return sol;
    }

    /** 
     * Given aRoute, iNode and depot, returns the edge in aRoute which
     * contains iNode and depot (it will be the first of the last edge)
     */
    private static VRPEdge getEdge(Route aRoute, Node iNode, Node depot)
    {   
        // Check if firstEdge in aRoute contains iNode and depot
        VRPEdge firstEdge = aRoute.getEdges().get(0);
        Node origin = firstEdge.getOrigin();
        Node end = firstEdge.getEnd();
        if ( ( origin == iNode && end == depot )
                || ( origin == depot && end == iNode ) )
            return firstEdge;
        else
        {   int lastIndex = aRoute.getEdges().size() - 1;
            VRPEdge lastEdge = aRoute.getEdges().get(lastIndex);
            return lastEdge;
        }
    }

    private static boolean checkMergingConditions(Test aTest, CVRPInputs inputs,
            Route iR, Route jR, VRPEdge ijEdge)
    {
        // Condition 1: iR and jR are not the same route
        if( iR == jR )
            return false;
        // Condition 2: both nodes are exterior nodes in their respective routes
        Node iNode = ijEdge.getOrigin();
        Node jNode = ijEdge.getEnd();
        if( iNode.getIsInterior() == true || jNode.getIsInterior() == true )
            return false;
        // Condition 3: demand after merging can be covered by a single vehicle
        if( inputs.getVehCap() < iR.getDemand() + jR.getDemand() )
            return false;
        // Condition 4: total costs (distance) after merging are feasible
        float maxRoute = aTest.getMaxRouteCosts();
        float serviceCosts = aTest.getServiceCosts();
        int nodesInIR = iR.getEdges().size();
        int nodesInJR = jR.getEdges().size();
        double newCost = iR.getCosts() + jR.getCosts() - ijEdge.getSavings();
        if( newCost > maxRoute - serviceCosts * (nodesInIR + nodesInJR - 2) )
            return false;
        
        return true;
    }

    private static int getRandomPosition(double beta, Random r, int size)
    {   
        int index = (int) (Math.log(r.nextDouble()) / Math.log(1 - beta));
            index = index % size;
        return index;
    }
  
}