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
package macs.heuristics;

import java.util.ArrayList;
import java.util.List;

public class JobFiles {
	String filename = null;
	String directory = null;
	String instance = null;
	List<String> agents = null;
	double nl = 0;
	int rows = 0;
	int columns = 0;
	int conversations = 0;
	public JobFiles(){
		
		agents = new ArrayList<String>();		
	}
	
	public void setName(String filename){
		this.filename = filename;
	}
	public void setInstance(String instance){
		this.instance = instance;
	}
	public void setDirectory(String directory){
		this.directory = directory;
	}
	public void setNl(double nl){
		this.nl = nl;
			
	}
	public void setRows(int rows){
		this.rows = rows;
	}
	public void setColumns(int columns){
		this.columns = columns;
	}
	public void setConversations(int converstations){
		this.conversations = converstations;
	}
	
	public void addAgent(String agent){
		this.agents.add(agent);
	}
	public String getInstance(){
		return this.instance;
	}
	public String getName(){
		return this.filename;
	}
	public String getDirectory(){
		return directory;
	}
	public int getRows(){
		return this.rows;
	}
	public int getColumns(){
		return this.columns;
	}
	
	public List<String> getAgents(){
		return this.agents;
	}
	public double getNl(){
		return nl;
	}
	public int getConversations(){
		return this.conversations;
	}

}
