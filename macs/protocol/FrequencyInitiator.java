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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import macs.agents.AgentState;
import macs.agents.AgentVocabulary;
import macs.heuristics.PatternHeuristic;
import macs.ontologies.entities.Edge;
import macs.ontologies.entities.Solution;
import macs.ontologies.entities.SolutionData;
import macs.ontologies.semantics.MyOntology;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

public class FrequencyInitiator extends ContractNetInitiator implements
		AgentVocabulary {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4622680114707216816L;
	private Solution currentbest = null;
	Ontology ontology = MyOntology.getInstance();
	public Codec codec = new XMLCodec();
	List<String> agents = null;
	//SolutionWrapper sw = SolutionWrapper.getInstance();
	int nResponders = 0; 
	private AgentState as;
	private boolean init = false;
	
	
	
	
	public FrequencyInitiator(Agent a, ACLMessage msg, List<String> agents, AgentState as, Solution currentbest) {
		super(a, msg);
		
		myAgent.getContentManager().registerLanguage(codec);		
		myAgent.getContentManager().registerOntology(ontology);
		this.as = as;
		this.currentbest = currentbest;
		this.agents = agents;
		
		
	}
	protected void handlePropose(ACLMessage propose, Vector v) {
		//System.out.println("Agent "+propose.getSender().getName()+" proposed ");
	}
	protected void handleAllResponses(Vector responses, Vector acceptances) {
		if (responses.size() < nResponders) {
			// Some responder didn't reply within the specified timeout
			//System.out.println("Timeout expired: missing "+(nResponders - responses.size())+" responses");
		}
		
		List<Edge> hlist = PatternHeuristic.solutionToEdges(currentbest);
	
		
		List<Pair> agentValues = new ArrayList<Pair>();
		Pair thispair = new Pair();
		thispair.setPair(myAgent.getLocalName(), currentbest.getValue());
		agentValues.add(thispair);
		Enumeration e = responses.elements();			
		while (e.hasMoreElements()) {
			
			ACLMessage msg = (ACLMessage) e.nextElement();
			try {
				ContentElement ce = null;
				ce = myAgent.getContentManager().extractContent(msg);				
				if(ce instanceof SolutionData){
					SolutionData sd = (SolutionData)ce;					
					hlist.addAll(sd.getHeuristics());
					
					
					Pair pair = new Pair();
					pair.setPair(msg.getSender().getLocalName(), sd.getValue());
					agentValues.add(pair);
				
				}				
			}
			catch(CodecException ce){
				ce.printStackTrace();
			}
			catch(OntologyException oe){
				oe.printStackTrace();
			}
		}
		
		
		
		SolutionData newsd  = PatternHeuristic.compareAndRank(hlist);
		
		Pair min = getLowestValue(agentValues);
	
		//legacy code from JADE
		Enumeration ne = responses.elements();			
		while (ne.hasMoreElements()) {
			
			ACLMessage nmsg = (ACLMessage) ne.nextElement();	
			
			
			if (nmsg.getPerformative() == ACLMessage.PROPOSE) {
				ACLMessage reply = nmsg.createReply();
				//reply.setReplyByDate(new Date(System.currentTimeMillis() + 5000));
				if(nmsg.getSender().getLocalName().trim().equals(min.getName().trim())){
					reply.clearAllReceiver();
					newsd.setInitiator(true);
					reply.addReceiver(nmsg.getSender());
				}
				else{
					
					newsd.setInitiator(false);
				}
				
				reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
				acceptances.addElement(reply);					
				try {
					myAgent.getContentManager().fillContent(reply, newsd);
				} catch (CodecException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (OntologyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		}
		if(min.getName().equals(myAgent.getLocalName())){
			init = true;
			
		}
	
		
		currentbest = PatternHeuristic.createNewSolution(newsd.getLinkedHeuristics(),newsd.getHeuristics(),currentbest,true);
		
	}
	
	
	
	protected void handleFailure(ACLMessage failure) {
		if (failure.getSender().equals(myAgent.getAMS())) {
			// FAILURE notification from the JADE runtime: the receiver
			// does not exist
			System.out.println("Responder does not exist");
		}
		else {
			System.out.println("Agent "+failure.getSender().getName()+" failed");
		}
		// Immediate failure --> we will not receive a response from this agent
		nResponders--;
	}
	protected void handleInform(ACLMessage inform) {
		//System.out.println("Agent "+inform.getSender().getName()+" successfully performed the " +
		//"requested action - replied with frequency matches");			
		
	}
	protected void handleRefuse(ACLMessage refuse) {
		System.out.println("Agent "+refuse.getSender().getName()+" refused to perform the requested action");
		agents.remove(refuse.getSender().getName());
		reset();
	}


	
	public void handleOutOfSequence(ACLMessage msg){
		///ACLMessage reply = null;
		//if(msg.getPerformative() == ACLMessage.CFP){
			reset();
			
		//}
	}
	

	
	private Pair getLowestValue(List<Pair> agentValues){
		Collections.sort(agentValues, new PairCompare());
		Pair min = agentValues.get(0);
		return min;		
	}
	
	
	
	public int onEnd(){
		//System.out.println("INIT NAME END " + init);
		as.exitWait(2,init,currentbest);
		return super.onEnd();
		
	}
	
	private class Pair {
		String name;
		double value;
		
		private void setPair(String name, double d){
			this.name = name;
			this.value = d;
		}
		private String getName(){
			return this.name;
			
		}
		private double getValue(){
			return this.value;
			
		}
	}// end Pair
	
	class PairCompare implements Comparator<Pair> {

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		
		@Override
		public int compare(Pair o1, Pair o2) {
			
			// TODO Auto-generated method stub
			int value = 0;
			try{
			if(o1.getValue() > o2.getValue()){
					value = 1;
				}
				else if (o1.getValue() < o2.getValue()){
					value = -1;
				}
				else {
					value = 0;
				}
			}
			catch(NullPointerException ne){
				ne.printStackTrace();
			}
			return value;
		}

		
		  
	  }//PairCompare
	
	
}// end NewFrequencyInitiator

