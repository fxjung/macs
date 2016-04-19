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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import macs.behaviour.OutputBehaviour;
import macs.heuristics.JobFiles;
import macs.heuristics.PFSPSolver;
import macs.heuristics.PatternHeuristic;
import macs.heuristics.RCWS;
import macs.ontologies.SolutionWrapper;
import macs.ontologies.entities.Edge;
import macs.ontologies.entities.SolutionData;
import macs.ontologies.entities.problems.SolutionElements;
import macs.ontologies.semantics.MyOntology;
import macs.protocol.ParallelFinal;
import macs.protocol.SetupInitiator;
import macs.util.Input;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;


public class LaunchAgent extends Agent implements AgentVocabulary, LaunchState{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1559695444603810526L;
	public List<String> agents = null;	
	//public SolutionWrapper sw = SolutionWrapper.getInstance();	
	Ontology ontology = MyOntology.getInstance();
	public Codec codec = new XMLCodec();
	List<JobFiles> names = null;
	int count = 0;
	int jobcount = 0;
	int agentcount = 0;
	int innercount = 0;
	int outercount = 0;
	int overallcount = 0;
	int problem = 0;
	boolean psize = false;
	int objectiveFunction = -1;
	String directory;
	JobFiles job = new JobFiles();
	List<SolutionData> inputSolutions = new ArrayList<SolutionData>();
	boolean multiple = false;
	
	SolutionData startresult;
	


	protected void setup() {
		getContentManager().registerLanguage(codec);		
		getContentManager().registerOntology(ontology);
		
	
		Object[] args = getArguments();	
		
		
		problem = commandConverter((String)args[0]);
		String filename = (String)args[1];
		psize = new Boolean((String)args[2]);
		multiple = new Boolean((String)args[3]);
		
	
		
		try {
			
			names = Input.getBenchmarkNames(filename);
			System.out.println("Names" + names.size());
			ListIterator<JobFiles> nit = names.listIterator();
			
			if(!names.isEmpty()){
				while(nit.hasNext()){
					job = nit.next();
					directory = job.getDirectory();
					String name = job.getName();
					name = directory+name;
					List<String> agents = job.getAgents();
					
					agentcount = agents.size();
					int convserations = job.getConversations();
			
					
				
					
					if(problem==PFSP){						
						SolutionData solution = null;							
						solution = getSolution(name,agents,convserations,problem);					
						inputSolutions.add(solution);						
					}			
						
					else if(problem==VRP){
						SolutionData solution1 = null;												
						solution1 = getSolution(name,agents,convserations,problem);					
						inputSolutions.add(solution1);
						
					}
				}
				
			}
			else{
				System.err.println("No inputfiles ");
				System.exit(1);
			}
				
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		doWait(10000);
		try {
			launchState(0,null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void launchState(int state, List<SolutionData> result) throws FileNotFoundException{
	
		overallcount++;
		
		switch(state){
		
		case 0:	
			if(outercount < 20){
				
				if(innercount < inputSolutions.size()){					
				
				startresult = inputSolutions.get(innercount);
				
				jobcount++;
				addBehaviour( new SetupInitiator(this, createMessage(startresult,ACLMessage.REQUEST),startresult.getAgents(),this));
				}
				
			}
			else{
				System.out.println("System Exit");				
				
			}
			
			
		
		break;
		case 1:
	
				
			addBehaviour(new ParallelFinal(this,this,agentcount));
		
			
			
			break;
		
		case 2:
			
			if(result !=null){
				addBehaviour(new OutputBehaviour(directory,startresult.getJobName(),problem,startresult ,result,this,jobcount,agentcount,overallcount,multiple));
			}	
			innercount++;
			
			if(innercount == inputSolutions.size()){
				innercount = 0;
				outercount++;
			
			}
			break;
		}
		
	}
	
	
	protected ACLMessage createMessage(SolutionData sol,int message){
		
		
		int performative = 0;
		String protocol = null;
		if(message == ACLMessage.CFP){
			performative = ACLMessage.CFP;
			protocol = FIPANames.InteractionProtocol.FIPA_CONTRACT_NET;
		}
		else if (message == ACLMessage.REQUEST){
			performative = ACLMessage.REQUEST;
			protocol = FIPANames.InteractionProtocol.FIPA_REQUEST;
		}
		else {
			System.err.println("UNRECOGNISED PROTOCOL: " + message);
			System.exit(1);
		}
		ACLMessage msg = new ACLMessage(performative);
		Iterator<String> it = sol.getAgents().listIterator();
		while(it.hasNext()) {
			String agent = (String)it.next();
				
				if(!getLocalName().equals(agent)){
					msg.addReceiver(new AID(agent, AID.ISLOCALNAME));
				}
		}
		
		msg.setProtocol(protocol);
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());
		
	
	
		try {			
			
			getContentManager().fillContent(msg,sol);			
			
		}
		catch (CodecException ce) {
			ce.printStackTrace();
			
		}
		catch (OntologyException oe) {
			oe.printStackTrace();
			
		}
		return msg;
		
	}

	
	public SolutionData getSolution(String name,List<String> agents,int conversations,int problem) throws Exception{
		
		StringTokenizer stok = new StringTokenizer(name,"/,\\");
		List<String> jobNames = new ArrayList<String>();
		while(stok.hasMoreTokens()){
			jobNames.add(stok.nextToken());
			
		}			
		String jobName = jobNames.get(jobNames.size()-1);
		
		SolutionData startsolution = new SolutionData();	
		if(problem == VRP){
			
				    
			startsolution = RCWS.solve(name);		
		
			startsolution.setJobName(jobName);	
			
		}
		else if(problem == PFSP){
			startsolution = PFSPSolver.solve(name);		
			
			startsolution.setJobName(jobName);
		}
		
		startsolution = getHeuristics(startsolution,problem);		
	
		startsolution.setProblem(problem);
		startsolution.setAgents(agents);
		
		startsolution.setConversations(conversations);
		
		
		SolutionWrapper.getInstance().setNodes(startsolution.getNodes());
		
		System.out.println("First Value " + startsolution.getValue());
		System.out.println("Solutionsize " + startsolution.getNodes().size());
		
		
		return startsolution;		
	}
	
	
	//This generates the column headings
    public List<Integer> getColumnHeadings(int size){
    	List<Integer> output = new ArrayList<Integer>(size);
    	for(int i = 0; i < size; i++){
    		output.add(i);
    	}
    	if(output.size() != size){
    		throw new IllegalArgumentException(
                    "index (" + output.size()+ "  "+size +") out of bounds"); 
    		
    	}
		return output;
    	
    }
	protected void takeDown() {
		System.out.println(" HAgent " + getAID().getName() + " terminating.");		
	}
	
	

	
	/**
	 * This method takes a SotuionData object and breakes the new solution list into Edges
	 * Each edge is individaully costed
	 * @param result
	 * @param problem
	 * @return
	 */
	public SolutionData getHeuristics(SolutionData result,int problem){
		List<SolutionElements> nodes = result.getNodes();
		List<Edge> hlist = PatternHeuristic.nodesToEdgeList(nodes,problem);
		result.setHeuristics(hlist);		
		return result;
	}
		
	
	
	private int commandConverter(String command){
		int value = 0;
		if (command.equals("pfsp")) {
			value = PFSP;
		}
		else if (command.equals("vrp")) {
			value = VRP;
		}
		
		
		else {
			System.err.println("UNRECOGNISED COMMAND");
			System.exit(1);
		}
		return value;
	}


	
}
