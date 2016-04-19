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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SSIteratedAchieveREResponder;
import macs.agents.AgentState;
import macs.agents.AgentVocabulary;
import macs.heuristics.PatternHeuristic;
import macs.ontologies.SolutionWrapper;
import macs.ontologies.entities.Edge;
import macs.ontologies.entities.Solution;
import macs.ontologies.entities.SolutionData;
import macs.ontologies.entities.problems.JobData;
import macs.ontologies.entities.problems.NodeData;
import macs.ontologies.entities.problems.SolutionElements;
import macs.ontologies.semantics.MyOntology;
import macs.uoc.cvrp.CVRPInputs;
import macs.uoc.cvrp.Node;
import macs.uoc.cvrp.VRPEdge;
import macs.uoc.pfsp.api.PFSPInputs;
import macs.uoc.pfsp.api.PFSPJob;
import macs.uoc.pfsp.base.BaseInputs;
import macs.uoc.pfsp.base.BaseJob;
import macs.util.Input;



public class SetupResponder extends SSIteratedAchieveREResponder implements AgentVocabulary {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4572810130984026579L;
	private AgentState as;
	
	Ontology ontology = MyOntology.getInstance();
	public Codec codec = new XMLCodec();
	private Solution solution = new Solution();
	private int state =0;

	public SetupResponder(Agent a, ACLMessage mt,AgentState as,double nlist) {
		super(a, mt);
		myAgent.getContentManager().registerLanguage(codec);		
		myAgent.getContentManager().registerOntology(ontology);		
		this.as = as;
		SolutionWrapper.getInstance().reset();
		
		closeSessionOnNextReply();
	}
	protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
		
		
		ACLMessage inform = null;
	
		try {
		
			ContentElement ce = null;
			ce = myAgent.getContentManager().extractContent(request);			
			if(ce instanceof SolutionData){					
				SolutionData sd = (SolutionData)ce;
				
				
				SolutionWrapper.getInstance().setAgentName(myAgent.getLocalName());
				SolutionWrapper.getInstance().setJobName(sd.getJobName());
				
				if (sd.getProblem()== PFSP){
					SolutionWrapper.getInstance().setProblem(PFSP);
					List<SolutionElements> nData = sd.getNodes();
		
					//populate input object with node
					PFSPInputs inputs = new BaseInputs(sd.getColumn(),sd.getRow());				
					
					
					List<Integer> list = new ArrayList<Integer>();
					for(int i= 0; i < nData.size();i++){
						JobData jd = (JobData)nData.get(i);
						PFSPJob job = new BaseJob();
						int id = jd.getId();
						job.setId(id);
						list.add(id);
						List<Integer> proc = jd.getProcessingTimes();
						int[] processingTimes = new int[proc.size()];
						for(int j = 0; j < proc.size();j++)
							processingTimes[j] = proc.get(j);							
						job.setProcessingTimes(processingTimes);
						job.setTotalProcessingTime(jd.getTotalProcessingTime());
	                    inputs.getJobs()[i]=job;						
					}
					
					
					//put nodes in memory	
					SolutionWrapper.getInstance().setList(list);
					SolutionWrapper.getInstance().setPFSPInputs(inputs);						
					SolutionWrapper.getInstance().setNodes(nData);
					SolutionWrapper.getInstance().setSolutionSize(sd.getNodes().size());
					SolutionWrapper.getInstance().setPoolSize((int)(sd.getNodes().size()*0.2));
					//set number of jobs, values, number of agents, number of conversations -- used by PatternHeuristic
					SolutionWrapper.getInstance().setSolutionSize(sd.getNodes().size());
					SolutionWrapper.getInstance().setValue(sd.getValue());		
					SolutionWrapper.getInstance().setAgents(sd.getAgents());
					SolutionWrapper.getInstance().setConversations(sd.getConversations());
					//SolutionWrapper.getInstance().setPool(sd.getHeuristics());
					
					//Build first solution from the seed sent by the launcher
					Set<Edge> heuristics = SolutionWrapper.getInstance().getPool();					
					List<Edge> linkedheuristics = PatternHeuristic.getLinkedEdgeList(new ArrayList<Edge>(heuristics));
					solution = PatternHeuristic.createNewSolution(linkedheuristics, null, null, false);
					
				}
				else if (sd.getProblem()== VRP){
					SolutionWrapper.getInstance().setProblem(VRP);
					List<SolutionElements> nData = sd.getNodes();
				
					//populate input object with node
					CVRPInputs inputs = new CVRPInputs(nData.size());
					inputs.setVehCap(((NodeData)nData.get(0)).getCapacity());
					
					for(int i = 0; i < nData.size();i++){
						Node nd = new Node(nData.get(i).getId(),((NodeData)nData.get(i)).getX(),((NodeData)nData.get(i)).getY(),((NodeData)nData.get(i)).getDemand());
	                    inputs.getNodes()[i] = nd;
					}
					//generate savings list and put it in memory	
					 Node[] depotEdges = Input.generateDepotEdges(inputs);
					 LinkedList<VRPEdge> savings = Input.generateSavingsList(depotEdges);
					 inputs.setList(savings);
					 SolutionWrapper.getInstance().setVRPInputs(inputs);
					
					//put nodes in memory					
					SolutionWrapper.getInstance().setNodes(nData);	
					SolutionWrapper.getInstance().setDepot(sd.getDepot());					
					SolutionWrapper.getInstance().setCapacity();				
					SolutionWrapper.getInstance().setSolutionSize(sd.getNodes().size());
					SolutionWrapper.getInstance().setPoolSize((int)(sd.getNodes().size()*0.2));									
		
					SolutionWrapper.getInstance().setSolutionSize(sd.getNodes().size());
					SolutionWrapper.getInstance().setValue(sd.getValue());		
					SolutionWrapper.getInstance().setAgents(sd.getAgents());
					SolutionWrapper.getInstance().setConversations(sd.getConversations());
					
					
					
					
					Set<Edge> heuristics = SolutionWrapper.getInstance().getPool();
					List<Edge> linkedheuristics = PatternHeuristic.getLinkedEdgeList(new LinkedList<Edge>(heuristics));
					solution = PatternHeuristic.createNewSolution(linkedheuristics, null, null, false);
					
				}
				
			
				if(sd.getAgents().size()==1){
					state = 3;
				}
				else {
					state = 1;
				}
				
			}
			else{
				System.out.println("Agent "+myAgent.getLocalName()+": Action failed");
				throw new FailureException("unexpected-error");
			}
			
			
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
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return inform;
		
		
	}
	
	

	
	public int onEnd(){
		
		as.exitWait(state,false,solution);
		
		return super.onEnd();
	}

	
	
}//end SetupResponder
