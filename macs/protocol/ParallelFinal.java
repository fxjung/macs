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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;




import macs.agents.LaunchState;
import macs.ontologies.entities.SolutionData;
import macs.ontologies.semantics.MyOntology;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SSIteratedAchieveREResponder;

public class ParallelFinal extends ParallelBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3951986047954878667L;
	
	private LaunchState as;

	Ontology ontology = MyOntology.getInstance();
	public Codec codec = new XMLCodec();
	
	private List<SolutionData> sollist = null;
	
	
	public ParallelFinal(Agent a,LaunchState as,int agents) {
		super(a,WHEN_ALL);
		myAgent.getContentManager().registerLanguage(codec);		
		myAgent.getContentManager().registerOntology(ontology);
		this.as = as;
		
		sollist = new ArrayList<SolutionData>();
		runBehaviours(agents);
		
	}
	
	public void runBehaviours(int agents){	
		for(int i = 0; i < agents ; i++){
		
			addSubBehaviour(new FinalResponder(myAgent,myAgent.blockingReceive()));
		}
	}
	
	public int onEnd(){
		try {
			as.launchState(2, sollist);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
		return super.onEnd();
	}
	
	public class SolutionDataCompare implements Comparator<SolutionData> {


		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */

		@Override
		public synchronized int compare(SolutionData o1, SolutionData o2) {
			
		
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
			  
		}//end SolutionDataCompare
	
	public class FinalResponder extends SSIteratedAchieveREResponder {
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 8697295112642764564L;
		Ontology ontology = MyOntology.getInstance();
		public Codec codec = new XMLCodec();


		public FinalResponder(Agent a, ACLMessage mt) {
			super(a, mt);
			myAgent.getContentManager().registerLanguage(codec);		
			myAgent.getContentManager().registerOntology(ontology);
		
			closeSessionOnNextReply();
		}
		protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
			//System.out.println("Agent "+myAgent.getLocalName()+": REQUEST received from "+request.getSender().getName());
			ACLMessage inform = null;
			try {
				ContentElement ce = null;
				ce = myAgent.getContentManager().extractContent(request);			
				if(ce instanceof SolutionData){					
					SolutionData sd = (SolutionData)ce;	
					sollist.add(sd);
				
					
				
					
					
				}
				else{
					System.out.println("Agent "+myAgent.getLocalName()+": Action failed");
					throw new FailureException("unexpected-error");
				}
				
				//System.out.println("Agent "+myAgent.getLocalName()+": Action successfully performed - Final data");
				inform = request.createReply();
				inform.setPerformative(ACLMessage.INFORM);
				
				SolutionData newsd = new SolutionData();
				newsd.setFlag(true);
				myAgent.getContentManager().fillContent(inform, newsd);					
				
			}
			
			catch(CodecException ce){
				ce.printStackTrace();
			}
			catch(OntologyException oe){
				oe.printStackTrace();
			} catch (FailureException e) {
				
				e.printStackTrace();
			}
			
			return inform;
			
			
		}
		

		
		public int onEnd(){
			
			return super.onEnd();
		}

		
		
	}//end ParallelFinal
	


}
