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
import java.util.*; 




/***********************************************************************************
 * Class RouteCache.java
 * Two routes are said to be "nodes-equivalent" if they contain the same set of
 * nodes (notice that a set of nodes can be sorted in different ways).
 * This class uses a hash table to save the best known-so-far route for each set of
 * nodes. Therefore, given a route we can try to improve it by looking up at the
 * hash table, which could contain a better nodes-equivalent route.
 * Timestamp (YYMMDD_HHMM): 110817
 * (c) Angel Juan - ajuanp(@)gmail.com
 **********************************************************************************/
public class RouteCache 
{
    /*******************************************************************************
     * INSTANCE FIELDS
     ******************************************************************************/
    // Declare and define some basic parameters of the hash table
    private HashMap<Integer, Route> tableR;
    private int initialCapacity = 100000;
    private float loadFactor = 0.8F;
    private int tableSize;
	
    /*******************************************************************************
     * CLASS CONSTRUCTOR
     *****************************************************************************/
    public RouteCache()
    {   // Initialize the hash table
	tableR = new HashMap<Integer, Route>(initialCapacity, loadFactor);
	tableSize = 0;
    }
	
    /*******************************************************************************
     * PUBLIC METHOD improveRoutesUsingHashTable()
     * Given a solution, it tries to enhance it by improving each of its routes.
     * To improve each route it uses the findImprovedRoute() method, which
     * implements a hash table. Finally, it returns the improved solution.
     *****************************************************************************/
    public VRPSolution improveRoutesUsingHashTable(VRPSolution sol)
    {
        /***************************************************************************
         * 1. RESET TOTAL COSTS OF THE GIVEN SOLUTION
         **************************************************************************/
	sol.setCosts(0);

        /***************************************************************************
         * 2. FOR EACH ROUTE IN THE GIVEN SOLUTION, TRY TO IMPROVE IT
         **************************************************************************/
	Route iRoute = null;
        for( int i = 0; i < sol.getRoutes().size(); i++ )
    	{   
            // 2.1. Get a route iRoute in the solution
            iRoute = sol.getRoutes().get(i);
    		
            // 2.2. Try to improve iRoute by using the hash table
            //  If there isn't a better equivalent route in the current hash table
            //  it returns iRoute after adding iRoute to the hash table
            Route betterRoute = findImprovedRoute(iRoute);
    		
            // 2.3. If betterRoute outperforms iRoute, update solution
            if( betterRoute.getCosts() < iRoute.getCosts() )
            {   sol.getRoutes().remove(iRoute);
                sol.getRoutes().add(i, betterRoute);
            }
    		
            // 2.4. Add costs of the best-found route to the solution
            sol.addCosts(betterRoute.getCosts());
        }

        /***************************************************************************
         * 3. RETURN THE UPDATED SOLUTION
         **************************************************************************/
	return sol;
    }
		
    /*******************************************************************************
     * PRIVATE METHOD findImprovedRoute()
     * Given aRoute it returns either an improved route (using the same nodes) from
     * the hash table, or simply aRoute if there isn't a better route in the table
     ******************************************************************************/
    private Route findImprovedRoute(Route aRoute)
    {
	// 1. TRY TO IMPROVE THE GIVEN ROUTE
        // WARNING!: THIS WILL NOT WORK AT THIS MOMENT SINCE IT CREATES NEW EDGES
        //aRoute = improveNodesOrder(aRoute);

        // 2. OBTAIN, IF IT EXISTS, THE CACHED ROUTE COVERING THE SAME SET OF NODES
	int nodesHash = getNodesHashFromRoute(aRoute);
	Route cachedRoute = (Route) tableR.get(nodesHash);
	
	// 3. IF IT DOES NOT EXIST ALREADY, SAVE aRoute IN CACHE AND RETURN IT
	if( cachedRoute == null )
	{   tableR.put(nodesHash, aRoute);
            return aRoute;
	}
	// 4. IF IT EXISTS ALREADY, UPDATE THE CACHE AND RETURN THE BEST ROUTE
	else
	{   // 4.1. If aRoute is better than cachedRoute, save aRoute and return it
            if( aRoute.getCosts() < cachedRoute.getCosts() )
            {   tableR.remove(nodesHash);
		tableR.put(nodesHash, aRoute);
		return aRoute;
            }
            // 4.2. If cachedRoute is better than aRoute, return cachedRoute
            else
                return cachedRoute;
	}
    }
	
    /*******************************************************************************
     * PRIVATE METHOD getNodesHashFromRoute()
     * Given a route (e.g.: 0-1-2-23-15-0), this method creates a string containing
     * its nodes sorted and separated by semicolons (e.g. "0:0:1:2:15:23"). Then,
     * the method generates a hash code (integer) for this string and returns it.
     * This integer (hash code) can be used as a key in a hash table.
     ******************************************************************************/
    private int getNodesHashFromRoute(Route aRoute)
    {
        // 1. CREATE A NEW nodesID ARRAY
	//  (e.g.: route 0-1-2-23-15-0 has 5 edges and 6 nodes)
	int length = aRoute.getEdges().size() + 1;
        int[] nodesID = new int[length];
		
	// 2. STORE THE ID OF THE FIRST NODE (DEPOT) IN THE FIRST EDGE
        nodesID[0] = 0;
		
	// 3. STORE THE IDs OF ALL REMAINING NODES IN aRoute
	VRPEdge iEdge = null;
        for( int i = 0; i < aRoute.getEdges().size() - 1; i++ )
	{   iEdge = (VRPEdge)aRoute.getEdges().get(i);
            nodesID[i + 1] = iEdge.getEnd().getId();
	}
		
	// 4. CLOSE THE ARRAY BY ADDING THE DEPOT AS THE LAST NODE
	nodesID[length - 1] = 0;

	// 5. SORT NODES BY ID (e.g.: (0,0,1,2,15,23)
	Arrays.sort(nodesID);
	
	// 6. CONSTRUCT A STRING BY ADDING EACH NODE IN nodesID SEPARATED BY ":"
	//  (e.g.: "0:0:1:2:15:23")
        StringBuilder nodes = new StringBuilder("0:0");
        for( int i = 2; i < nodesID.length; i++ )
            nodes.append(":").append(nodesID[i]);

        // 7. RETURN A HASH CODE ASSOCIATED WITH THE RESULTING STRING
        return nodes.toString().hashCode();
    }
		
    /*******************************************************************************
     * PRIVATE METHOD improveNodesOrder()
     * Given aRoute, this method tries to sort its nodes in a more efficient way.
     * (e.g. by eliminating possible knots in the current route)
     ******************************************************************************/
    private Route improveNodesOrder(Route aRoute)
    {
        List<VRPEdge> vRPEdges = aRoute.getEdges();
        // Edges in aRoute must be directed
        VRPEdge e1 = null;
        VRPEdge e2 = null;
        VRPEdge e3 = null;
        double currentCosts = 0;
        double alterCosts = 0;
        Node originE1 = null;
        Node originE2 = null;
        Node endE2 = null;
        Node endE3 = null;
	if( vRPEdges.size() >= 4 ) // if size <= 3 there aren't knots
	{   for( int i = 0; i <= vRPEdges.size() - 3; i++ )
            {   // Get the current way of sorting the 3 next edges
		e1 = vRPEdges.get(i);
		e2 = vRPEdges.get(i + 1);
		e3 = vRPEdges.get(i + 2);
		currentCosts = e1.getCosts() + e2.getCosts() + e3.getCosts();
		// Construct the alternative way
		originE1 = e1.getOrigin();
		originE2 = e2.getOrigin();
		endE2 = e2.getEnd();
		endE3 = e3.getEnd();
		VRPEdge e1b = new VRPEdge(originE1, endE2); // WARNING: INVERSE EDGE? INROUTE? ETC.
		e1b.setCosts(e1b.calcCosts(originE1, endE2));
		VRPEdge e2b = new VRPEdge(endE2, originE2);
                e2b.setCosts(e2b.calcCosts(endE2, originE2));
		VRPEdge e3b = new VRPEdge(originE2, endE3);
		e3b.setCosts(e3b.calcCosts(originE2, endE3));
		alterCosts = e1b.getCosts() + e2b.getCosts() + e3b.getCosts();
		// Compare both ways and, if appropriate, update route
		if( alterCosts < currentCosts )
		{   aRoute.removeEdge(e1);
                    aRoute.removeCosts(e1);
                    aRoute.getEdges().add(i, e1b);
                    aRoute.addCosts(e1b);
                    aRoute.removeEdge(e2);
                    aRoute.removeCosts(e2);
                    aRoute.getEdges().add(i+1, e2b);
                    aRoute.addCosts(e2b);
                    aRoute.removeEdge(e3);
                    aRoute.removeCosts(e3);
                    aRoute.getEdges().add(i+2, e3b);
                    aRoute.addCosts(e3b);
		}
            }
	}
	return aRoute;
    }

    /*******************************************************************************
    * PUBLIC METHOD getTableRSize()
    *******************************************************************************/
    public int getTableRSize()
    {   tableSize = tableR.size();
	return tableSize;
    }
}