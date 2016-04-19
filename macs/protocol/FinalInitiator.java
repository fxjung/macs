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
package macs.protocol;

import jade.content.lang.Codec;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

import java.util.List;

import macs.agents.AgentState;
import macs.ontologies.semantics.MyOntology;



public class FinalInitiator extends SimpleAchieveREInitiator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AgentState as;
	private List<String> agents;
	Ontology ontology = MyOntology.getInstance();
	public Codec codec = new XMLCodec();
	


	public FinalInitiator(Agent a, ACLMessage msg, List<String> agents,AgentState as) {
		
		super(a, msg);
		myAgent.getContentManager().registerLanguage(codec);		
		myAgent.getContentManager().registerOntology(ontology);
		this.as = as;
		this.agents  = agents;
	
		
	}
	public void handleInform(ACLMessage inform){
		//System.out.println("Agent "+inform.getSender().getName()+" performed the requested action -final data");
	}
	protected void handleRefuse(ACLMessage refuse) {
		System.out.println("Agent "+refuse.getSender().getName()+" refused to perform the requested action");
		agents.remove(refuse.getSender().getName());
	}
	protected void handleFailure(ACLMessage failure) {
		if (failure.getSender().equals(myAgent.getAMS())) {
			// FAILURE notification from the JADE runtime: the receiver
			// does not exist
			System.out.println("Responder does not exist");
		}
		else {
			System.out.println("Agent "+failure.getSender().getName()+" failed to perform the requested action");
		}
	}

	
	public int onEnd(){
		
		as.exitWait(0,false,null);
		System.gc();
		return super.onEnd();
		
	}
	

	
}//end SetupInitiator
