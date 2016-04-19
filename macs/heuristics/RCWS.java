/**
 * MACS - Multi-Agent Cooperative Search is a framework to develop
 * cooperating agents using different Metaheuristics
 * Copyright (C) 2013 University of Stirling
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package macs.heuristics;



import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import macs.ontologies.SolutionWrapper;
import macs.ontologies.entities.Edge;
import macs.ontologies.entities.Solution;
import macs.ontologies.entities.SolutionData;
import macs.uoc.api.Test;
import macs.uoc.cvrp.CVRPInputs;
import macs.uoc.cvrp.CVRPTest;
import macs.uoc.cvrp.MultiStart;
import macs.uoc.cvrp.Node;
import macs.uoc.cvrp.VRPEdge;
import macs.util.Input;



		

public class RCWS {
	
	/**
	 * The programme is used by the meta-heuristic agents to optimise using the Randomised CWS method
	 * It takes the in the solution object that are identified as good by the pattern matcher. The savings list is reordered with 
	 * These patterns at the head of the list. This biases the list. The hope is that it will improve the search.
	 * @param solution
	 * @param aTest
	 * @param finalrun
	 * @return solution
	 * @throws FileNotFoundException 
	 */
	public static Solution solve(Solution solution, Test aTest,boolean finalrun) throws FileNotFoundException{
		/* 1. Get the edges found by the pattern matcher. They are contained in the solution object. */
	
		
		
		List<Edge> edges = solution.getEdges();
		List<Edge> unique = PatternHeuristic.removeDuplicates(edges);
		//for(Edge e : unique)
			//System.out.println("Edges "+e.getFirst()+","+e.getSecond()+"::"+e.getCosts());
		
		//1.2 get savings list from ememory
		CVRPInputs inputs = SolutionWrapper.getInstance().getVRPInputs();		
		
		
		//remove good edges from current position in savings list and add them to head of list
		LinkedList<VRPEdge> newSavings = inputs.sortEdges(unique,inputs.getSavings());
		inputs.setList(newSavings);
		
		
		
		/* 2. USE THE MULTI-START ALGORITHM TO SOLVE THE INSTANCE */
	    Random rng = new Random();
	    
	    Solution output = MultiStart.solve(solution,aTest, inputs, rng);
		
		
		
		
        return output;
   }
	
/**
 *  This program is used by the launcher to reading the problem files and build an initial solution. It also build a SolutionData object
 *  This object is used by the meta-heuristic agents to solves  given problem. This object is serialised by the launcher agent and sent
 *  to the meta-heuristic agents.
 * @param inputNodesPath
 * @return SolutionData
 * @throws FileNotFoundException
 */
public static SolutionData solve(String inputNodesPath) throws FileNotFoundException{
	

    
    /* 1. Create Test Object "test2run.txt" */
    CVRPTest aTest = new CVRPTest(null, 100000, 0, 5, 
            "g", new Float(0.33),  new Float(0.23), 41611944);

    /* 2. Read in file and generate nodes edges  and savings list */
   
    CVRPInputs inputs = Input.getTSPLIBFormat(inputNodesPath);
    
    Node[] nodes =Input.generateDepotEdges(inputs);
    LinkedList<VRPEdge> savings = Input.generateSavingsList(nodes);
    inputs.setList(savings);
    // 2.2. USE THE MULTI-START ALGORITHM TO SOLVE THE INSTANCE
    Random rng = new Random();
   
    SolutionData output = MultiStart.solve( aTest,  inputs,  rng);
     
  

    /* 3. END OF PROGRAM output SoluationData object for launch agent*/
    
   return output;
		
		
    }
	
}

