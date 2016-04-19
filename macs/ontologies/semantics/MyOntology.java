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
package macs.ontologies.semantics;




import macs.ontologies.entities.Edge;
import macs.ontologies.entities.NodeList;
import macs.ontologies.entities.SolutionData;
import macs.ontologies.entities.problems.JobData;
import macs.ontologies.entities.problems.NodeData;
import macs.ontologies.entities.problems.SolutionElements;
import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;


public class MyOntology extends BeanOntology {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9154071576537619454L;
	public static final String ONTOLOGY_NAME = "MyOntology";
	private static Ontology theInstance = new MyOntology(ONTOLOGY_NAME);
	public static Ontology getInstance() {
	return theInstance;
	}
	private MyOntology(String name) {
		super(name);
			try {
					add(SolutionData.class);
					add(Edge.class);
					add(JobData.class);
					add(NodeData.class);
					add(SolutionElements.class);					
					add(NodeList.class);
						
					// Add all ontological classes included in a package
					add("maca.uoc.cvrp");
					
					
					
					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
	}
}
