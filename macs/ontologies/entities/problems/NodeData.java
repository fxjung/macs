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
package macs.ontologies.entities.problems;

import jade.content.Concept;





/**
 * @author simon
 *
 */
public class NodeData extends SolutionElements implements Concept {//SolutionDataVocabulary {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6671073241430403274L;
	protected int id;
	protected float x;
	protected float y;

	private double tempdistance;
	private int nvalue;
	private float capacity;
	private int vehicles;
	protected float demand;
	private String measure = new String();
	private int depot;	
	
    
   
	
	public NodeData(){}
	public NodeData(int name,float xcord, float ycord, float demand){
		this.id = name;
		this.x = xcord;
		this.y = ycord;
		this.demand = demand;
		
	}

	//Active List Flag
	private boolean FLAG = true;
	
	public int getId() {
		return id;
	}
	public float getCapacity(){
		return this.capacity;
	}
	public float getDemand(){
		return this.demand;
	}
	public int getVehicles(){
		return this.vehicles;
	}
	public String getMeasure(){
		return this.measure;
	}
	public boolean getFlag(){
		return FLAG;
	}
	public int getNValue(){
		return nvalue;
	}
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public int getDepot() {
		return depot;
	}
	
	public double getTempDistance(){
		return tempdistance;
	}
	public void setTempDistance(double tempdistance){
		this.tempdistance = tempdistance;
	}
	
	public void setId(int name) {
		this.id = name;
	}
	public void setNValue(int nvalue){
		this.nvalue = nvalue;
	}
	
	public void setX(float xcord) {
		this.x = xcord;
	}
	
	public void setY(float ycord) {
		this.y = ycord;
	}
		
	public void setVehicles(int vehicles){
		this.vehicles = vehicles;
	}
	public void setMeasure(String measure){
		this.measure = measure;
	}
	
	
	public void setFlag(boolean  flag){
		this.FLAG = flag;
	}
	public void setCapacity(float  capacity){
		this.capacity = capacity;
	}
	public void setDemand(float  demand){
		this.demand = demand;
	}
	

	
	@Override 
	public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof NodeData) {
        	NodeData that = (NodeData) other;
            result = (this.getId()==that.getId());
        }
        return result;
    }
	public void setDepot(int depot) {
		this.depot = depot;
		
	}




}
