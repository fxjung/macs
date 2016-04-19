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
package macs.agents;

/**
 * @author simon
 *
 */
public interface AgentVocabulary {
	
	//Zero is not to be defined as it is to be used as a null option
	public static final int TWOOPT = 1;
	public static final int THREEOPT = 2;	
	public static final int GEO = 3;
	public static final int LOG = 4;
	public static final int TABU = 5;
	public static final int SA = 6;
	public static final int REC = 7;
	public static final int BA = 8;
	public static final int BrA = 9;
	public static final int rBA = 10;
	public static final int rArB = 11;
	public static final int ArB = 12;
	public static final int rAB = 13;
	public static final int rBrA = 14;
	public static final int OPT = 15;
	public static final int FREQ = 16;
	public static final int TSP = 17;
	public static final int PFSP = 18;
	public static final int NEH = 19;
	public static final int SWAP = 20;
	public static final int SHUFFLE = 21;
	public static final int SHIFT = 22;
	public static final int REVERSE = 23;
	public static final int NN = 24;
	public static final int VRP = 25;
	public static final int WO = 26;
	public static final int CWS = 27;
	public static final int RNEH = 28;
	

	
	

}
