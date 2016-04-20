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
package macs.protocol;



import java.util.List;

import macs.agents.AgentState;
import macs.agents.AgentVocabulary;
import macs.heuristics.PatternHeuristic;
import macs.ontologies.SolutionWrapper;
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
import jade.content.onto.UngroundedException;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SSIteratedContractNetResponder;

public class FrequencyResponder extends SSIteratedContractNetResponder
		implements AgentVocabulary {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3398369339448338532L;
	Ontology ontology = MyOntology.getInstance();
	public Codec codec = new XMLCodec();
	List<String> agents = null;
	int nResponders = 0; 
	private AgentState as;
	private Solution currentbest = null;
	private boolean initiator = false;
	
	
	
	public FrequencyResponder(Agent a, ACLMessage mt, Solution currentbest,AgentState as) {
		
		super(a, mt);
		myAgent.getContentManager().registerLanguage(codec);		
		myAgent.getContentManager().registerOntology(ontology);
		this.as = as;
		this.currentbest = currentbest;
			
	}		
	
	protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
		//System.out.println("Agent "+myAgent.getLocalName()+": CFP received from "+cfp.getSender().getName()+". Action is ");
		ContentElement ce = null;
		try {
			ce = myAgent.getContentManager().extractContent(cfp);
		} catch (UngroundedException e) {
		
			e.printStackTrace();
		} catch (CodecException e) {
			
			e.printStackTrace();
		} catch (OntologyException e) {
	
			e.printStackTrace();
		}			
		if(ce instanceof SolutionData){				
			
			
			SolutionData proposal = PatternHeuristic.createNewHeuristics(currentbest);
			
				
		
			proposal.setValue(currentbest.getValue());
			
			ACLMessage propose = cfp.createReply();
			propose.setPerformative(ACLMessage.PROPOSE);
			//propose.setReplyByDate(new Date(System.currentTimeMillis() + 20000));
			try {
				myAgent.getContentManager().fillContent(propose, proposal);
			} catch (CodecException e) {
				
				e.printStackTrace();
			} catch (OntologyException e) {
				
				e.printStackTrace();
			}
				
				return propose;
		
		}
		else {
			// We refuse to provide a proposal
			System.out.println("Agent "+myAgent.getLocalName()+": Refuse");
			
			throw new RefuseException("evaluation-failed");
		}
	}
	
	protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose,ACLMessage accept) throws FailureException {
		//System.out.println("Agent "+myAgent.getLocalName()+": Proposal accepted");
		ContentElement ce = null;
		try {
			ce = myAgent.getContentManager().extractContent(accept);
		} catch (UngroundedException e) {
			
			e.printStackTrace();
		} catch (CodecException e) {
			
			e.printStackTrace();
		} catch (OntologyException e) {
			
			e.printStackTrace();
		}	
		ACLMessage inform = null;
		if(ce instanceof SolutionData){		
			
			SolutionData sd = (SolutionData)ce;
			initiator = sd.getInitiator();
			if(sd.getNewNodes()!=null){
				SolutionWrapper.getInstance().addToNodes(sd.getNewNodes());
			}
		
			inform = accept.createReply();
			inform.setPerformative(ACLMessage.INFORM);
			inform.setContent("");
		
			List<Edge> linked = PatternHeuristic.getNewLinkedEdgeList(sd.getLinkedHeuristics());
			List<Edge> unlinked = PatternHeuristic.getUnlinkedEdgeList(sd.getHeuristics(),linked);
			
			currentbest = PatternHeuristic.createNewSolution(linked,unlinked,currentbest,true);
		
			
			
		}
		else {
			System.out.println("Agent "+myAgent.getLocalName()+": Action execution failed");
			throw new FailureException("unexpected-error");
		}	
		
		
		return inform;
		
	}
	protected void handleOutOfSequence(ACLMessage msg){
		reset();
	}
	
	
	protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
		System.out.println("Agent "+myAgent.getLocalName()+": Proposal rejected");
	}
	
	/**
	 * Here the SolutionData objects Heuristic List is compared with the heuristics of 
	 * the last best solution stored in the singleton object.
	 * @param sd
	 * @return
	 */
	

	
	
	
	public int onEnd(){
		as.exitWait(2,initiator,currentbest);
		
		return super.onEnd();
	}
	
	
	
}//end FrequencyResponder

