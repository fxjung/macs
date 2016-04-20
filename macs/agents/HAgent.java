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
package macs.agents;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

import macs.heuristics.PFSPSolver;
import macs.heuristics.PatternHeuristic;
import macs.heuristics.RCWS;
import macs.ontologies.SolutionWrapper;
import macs.ontologies.entities.NodeList;
import macs.ontologies.entities.Solution;
import macs.ontologies.entities.SolutionData;
import macs.ontologies.semantics.MyOntology;
import macs.parameters.Stopwatch;
import macs.protocol.FinalInitiator;
import macs.protocol.FrequencyInitiator;
import macs.protocol.FrequencyResponder;
import macs.protocol.SetupResponder;
import macs.uoc.api.Test;
import macs.uoc.cvrp.CVRPTest;
import macs.uoc.pfsp.app.PFSPTest;


/**
 * @author simon martin
 *
 */
public class HAgent extends Agent implements AgentVocabulary,AgentState{

	/**
	 * 
	 */
	private static final long serialVersionUID = -235664512765794371L;
	/**
	 * @param args
	 */
	
	//variables
	public static List<Integer> tabulist;
	public int tabusize;
	public int optnumber;
	public int newoptnumber;
	public int temp;
	public int metaiterations;
	public int iterations;
	public int behaviour;
	public int frequency;
	public int poolsize;
	public int searchtype;
	public String filename;
	public double nlist = 0.0;
	public List<Double> templist;	
	public List<String> agents = null;	
	Ontology ontology = MyOntology.getInstance();
	public Codec codec = new XMLCodec();
	public int count = 0;
	public boolean robust = false;
	public int obj = -1;
	public int distribution = 0;
	public float param1 = 0;
	public float param2 = 0;
	public Test aTest;
	
	
	
	 Stopwatch stopwatch = new Stopwatch();
	    
	


	protected void setup()  {
		getContentManager().registerLanguage(codec);		
		getContentManager().registerOntology(ontology);
		
	
		Object[] args = getArguments();
		
		//get behaviour
		behaviour = commandConverter((String)args[0]);			
		
		//get the file name and generate the nodes and start solution from it. 
		//get the size of the neighbour hood list			
		filename = (String)args[1];					
		//get the number of iterations the metaheuristic is going to do
		metaiterations = new Integer((String)args[2]).intValue();			
		
		//get the tabu list size		
		tabusize = new Integer((String)args[3]).intValue();

		
		//get the temp function for Simulated Annealing
		temp = commandConverter((String)args[4]);		
		
		//get which opt function to use
		optnumber = commandConverter((String)args[5]);
		
		//get the neighbour list size		
		nlist = new Double((String)args[6]).doubleValue();
		
		robust = new Boolean((String)args[7]);	
		
		//edge parameter1
		param1 = new Float((String)args[8]).floatValue();
		
		//edge parameter2
		param2 = new Float((String)args[9]).floatValue();
		
		//get the rand distribution for VRP	
		distribution = new Integer((String)args[10]).intValue();
		
	
		
		//vrp test object first first 5 parameters are historical from original uoc code and set by hand
		if(behaviour == CWS){
			//Instance name | Max Route Cost | Service Cost | Max Time | Distribution | param1 | param2 |Seed
			aTest = new CVRPTest(SolutionWrapper.getInstance().getJobName(),100000,0,12,"g",new Float(param1),new Float(param2),distribution);
		}
		else if(behaviour == RNEH){
		
			aTest = new PFSPTest(SolutionWrapper.getInstance().getJobName(),12,1000,"t",new Float(param1),new Float(param2),distribution);
			//# instance | maxTime(sec) | nIter | distribution | beta1 | beta2 | seed
		}
				   
		 
		
		//Wait(5000);
		exitWait(0,false,null);
		
	}

	protected void takeDown() {
		System.out.println(" HAgent " + getAID().getName() + " terminating.");		
	}
	
	
	
	
	
	public void exitWait(int state, boolean initiator,Solution currentbest){	
		
		
		//System.out.println("Call Back worked! "+ state);
		switch(state){
		case 0 : 
			
			SolutionWrapper.getInstance().reset();					
			addBehaviour(new SetupResponder(this,blockingReceive(),this,nlist));
		
			count = 0;
			
		
			break;
		case 1 :
			//System.exit(1);
			stopwatch.start();
			
			
			try {
				currentbest = runMetaHeuristic(behaviour,optnumber,currentbest,tabusize, metaiterations, temp,false,aTest,-1);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			if(robust){
				for(NodeList nl :currentbest.getSolution()){
					System.out.println("CurrentBest 1 " + nl.getIntList());
				}				
				System.out.println("CurrentBest Value 1 " + currentbest.getValue());
			}
			//Take first solution and add it to the pool as a seed.
			PatternHeuristic.setSolutionEdgesToPool(currentbest);
			//Add current best to global currentbest value
			SolutionWrapper.getInstance().setBestSoFar(currentbest.getSolution(), currentbest.getValue());
			
					
			if(getAID().getLocalName().equals("agent1")){
				
				addBehaviour(new FrequencyInitiator(this, createMessage(currentbest,ACLMessage.CFP,SolutionWrapper.getInstance().getProblem(),false),SolutionWrapper.getInstance().getAgents(),this,currentbest));				
			}
			else {
				
				addBehaviour(new FrequencyResponder(this,blockingReceive(),currentbest,this));			
				
			}
		
		
			break;
		
		case 2 :
			
			
			
			
			if(count < SolutionWrapper.getInstance().getConversations() ){		
				try {
					if(count == 0||count==Math.round(SolutionWrapper.getInstance().getConversations()/2)||count==SolutionWrapper.getInstance().getConversations()-1){
						currentbest = runMetaHeuristic(behaviour, optnumber,currentbest,tabusize, metaiterations, temp,false,aTest,count);
					}
					else{
						currentbest = runMetaHeuristic(behaviour, optnumber,currentbest,tabusize, metaiterations, temp,false,aTest,-1);
					}
				} catch (Exception e) {
					
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(robust){
					for(NodeList nl :currentbest.getSolution()){
						System.out.println("CurrentBest  before 2 " + nl.getIntList());
					}
					System.out.println("CurrentBest Value before 2 " + currentbest.getValue());
				}
				SolutionWrapper.getInstance().setBestSoFar(currentbest.getSolution(), currentbest.getValue());
				if(initiator){
					
					addBehaviour(new FrequencyInitiator(this, createMessage(currentbest,ACLMessage.CFP,SolutionWrapper.getInstance().getProblem(),false),SolutionWrapper.getInstance().getAgents(),this,currentbest));				
				}
				else {
					
					addBehaviour(new FrequencyResponder(this,blockingReceive(),currentbest,this));			
					
				}
				
			}
			
			else {
				try {
					currentbest = runMetaHeuristic(behaviour, optnumber,currentbest,tabusize, metaiterations, temp,true,aTest,-1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SolutionWrapper.getInstance().setBestSoFar(
						currentbest.getSolution(), currentbest.getValue());
				stopwatch.stop();
			
				String time = stopwatch.toString();	
				Solution sol = new Solution();
				
				
				sol.setSolution(SolutionWrapper.getInstance().getBestListSoFar());
				sol.setValue(SolutionWrapper.getInstance().getBestValueSoFar());
				
				if(robust){
					for(NodeList nl :SolutionWrapper.getInstance().getBestListSoFar()){
						System.out.println("BEST LIST 2b "+ nl.getIntList());
					}			
					
					System.out.println("BEST VALUE  2b  "+ SolutionWrapper.getInstance().getBestValueSoFar());
				}
				
			
				
				
				sol.setTimes(time);
				agents = new ArrayList<String>();
				agents.add("launcher");
				
				addBehaviour( new FinalInitiator(this, createMessage(sol,ACLMessage.REQUEST,SolutionWrapper.getInstance().getProblem(),true),agents,this));
				
				
				break;
			}
			if(robust){
				for(NodeList nl :currentbest.getSolution()){
					System.out.println("CurrentBest 2c " + nl.getIntList());
				}
				System.out.println("CurrentBest Value 2c " + currentbest.getValue());
			}
			
			
			
			count++;
		
			break;
			
		case 3 :
			
			//stand alone mode
			stopwatch.start();			
			try {
		
					
					currentbest = runMetaHeuristic(behaviour, optnumber,currentbest,tabusize, metaiterations, temp,false,aTest,-1);
				
					
					
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
					stopwatch.stop();
					String time = stopwatch.toString();	
					Solution sol = new Solution();
					
					
					sol.setSolution(currentbest.getSolution());
					sol.setValue(currentbest.getValue());
					if(robust){
						for(NodeList nl :SolutionWrapper.getInstance().getBestListSoFar()){
							System.out.println("BEST LIST "+ nl.getIntList());
						}						
						System.out.println("BEST VALUE "+ SolutionWrapper.getInstance().getBestValueSoFar());
					}
					
				
					sol.setLocalOpt(SolutionWrapper.getInstance().getLocalOpt());
					sol.setOptCount(SolutionWrapper.getInstance().getOptCount());
					
					sol.setTimes(time);
					agents = new ArrayList<String>();
					agents.add("launcher");
					
					addBehaviour( new FinalInitiator(this, createMessage(sol,ACLMessage.REQUEST,SolutionWrapper.getInstance().getProblem(),true),agents,this));
					
				
					break;

					    
				
			
			
			
	}//end switch
				
}
	private int commandConverter(String command){
		int value = 0;
		if (command.equals("twoopt")) {
			value = TWOOPT;
		}
			
		else if (command.equals("geo")) {
			value = GEO;
		}
		else if (command.equals("log")) {
			value = LOG;
		}
		else if (command.equals("tabu")) {
			value = TABU;
		}
		else if (command.equals("sa")) {
			value = SA;
		}
		else if (command.equals("receiver")){
			value = REC;
		}
		else if (command.equals("opt")){
			value = OPT;
		}
		else if (command.equals("freq")){
			value = FREQ;
		}
		else if (command.equals("swap")){
			value = SWAP;
		}
		
		else if (command.equals("shuffle")){
			value = SHUFFLE;
		}
		else if (command.equals("shift")){
				    
			value = SHIFT;
		}
		else if (command.equals("reverse")){
			value = REVERSE;
		}
		else if (command.equals("neh")){
			value = NEH;
		}
		else if (command.equals("nn")){
			value = NN;
		}
		else if (command.equals("wo")){
			value = WO;
		}
		else if (command.equals("vrp")){
			value = VRP;
		}
		else if (command.equals("cws")){
			value = CWS;
		}
		else if (command.equals("rneh")){
			value = RNEH;
		}
		else if (command.equals("pfsp")){
			value = PFSP;
		}
		
		else {
			System.err.println("UNRECOGNISED COMMAND");
			System.exit(1);
		}
		return value;
	}
	

	
	private Solution runMetaHeuristic(int searchtype, int optnumber,Solution startsolution,int tabusize, 
			int iterations, int tempfunction, boolean finalrun, Test aTest,int count) throws Exception{
		Solution result = new Solution(SolutionWrapper.getInstance().getSolutionSize());
		
		switch(searchtype){
		
		
		case CWS:
			result = RCWS.solve(startsolution,aTest,finalrun);
			break;
			
		
		case RNEH:
			result = PFSPSolver.solve(startsolution,aTest);
			break;
	
			

		}//end switch	
		return result;
	}
	
	protected ACLMessage createMessage(Solution currentbest,int message,int problem,boolean finalrun){
		
	
		SolutionData sol = new SolutionData();
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
		if(finalrun){
			msg.addReceiver(new AID("launcher", AID.ISLOCALNAME));
			
		}
		else{
			for (String agent : SolutionWrapper.getInstance().getAgents()) {
				
					if(!getLocalName().equals(agent)){
						msg.addReceiver(new AID(agent, AID.ISLOCALNAME));
					}
			}
		}
		
		msg.setProtocol(protocol);
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());
	
		
		sol.setNodeList(currentbest.getSolution());
		
		sol.setValue(currentbest.getValue());
		
		sol.setTime(currentbest.getTimes());
		sol.setLocalOpt(SolutionWrapper.getInstance().getLocalOpt());
		sol.setOptCount(SolutionWrapper.getInstance().getOptCount());
		sol.setEdgeList(SolutionWrapper.getInstance().getEdgeList());
		sol.setAgentName(SolutionWrapper.getInstance().getAgentName());
	
		
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
		
	

	
	
	

}//end HAgent
