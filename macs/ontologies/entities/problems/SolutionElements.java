/** 
 * MACS - Multi-Agent Cooperative Search is a framework to develop cooperating agents using 
 * different Metaheuristics Copyright (C) 2016 Simon Martin. This file is part of MACS. 
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


package macs.ontologies.entities.problems;

import jade.content.Concept;
import macs.ontologies.vocabularies.SolutionDataVocabulary;


public abstract class SolutionElements implements SolutionDataVocabulary, Concept{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id =-1;
		
	public int getId(){
		return this.id;
	}

	public  void setId(int name){
		this.id = name;
	}
	
	
	
	
   
}
