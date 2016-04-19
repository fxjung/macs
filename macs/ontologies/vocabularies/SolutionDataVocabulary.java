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
package macs.ontologies.vocabularies;


/** 
 * @author simon
 * vocabulary for Ontology to communicate solutions between Agents
 *
 */
public interface SolutionDataVocabulary extends EdgeVocabulary, NodeListVocabulary,JobVocabulary {
	public static final String SOLUTION	= "solution";
	public static final String NODES = "nodes";
	public static final String VALUE = "value";
	public static final String HEURISTICS = "heuristics";
	public static final String FLAG = "flag";
	public static final String INITIATOR = "initiator";
	public static final String LIST = "list";
	public static final String AGENTS = "agents";
	public static final String TIME = "time";
	public static final String LOCALOPT = "localopt";
	public static final String OPTCOUNT = "optcount";
	public static final String CONVERSATIONS = "conversations";
	public static final String HEURISTIC = "heuristic";
	public static final String LINKEDHEURISTICS = "linkedheuristics";
	public static final String MATRIX = "matrix";
	public static final String PROBLEM = "problem";
	public static final String PERMUTATION = "permutation";
	public static final String CONSTRAINT = "constraint";
	public static final String NODELIST = "nodelist";
	public static final String EDGES = "edges";
	public static final String AGENTNAME = "agentname";

	
}
