package macs.uoc.cvrp;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class InputsManager
{
    public static CVRPInputs readInputs(String nodesFilePath, String vehiclesFilePath)
    {
        CVRPInputs inputs = null;
        try
        {   // 1. COUNT THE # OF NODES (# OF LINES IN nodesFilePath)
            BufferedReader br = new BufferedReader(new FileReader(nodesFilePath));
            String f = null;
            int nNodes = 0;
            while( (f = br.readLine()) != null )
            {   if( f.charAt(0) != '#' )
                    nNodes++;
            }
            br.close();
            // 2. CREATE THE INPUTS OBJECT WITH nNodes
            inputs = new CVRPInputs(nNodes);
            // 3. CREATE ALL NODES AND FILL THE NODES LIST
            FileReader reader = new FileReader(nodesFilePath);
            Scanner in = new Scanner(reader);
            String s = null;
            int k = 0;
            while( in.hasNextLine() )
            {   s = in.next();
                if( s.charAt(0) == '#' ) // this is a comment line
                    in.nextLine(); // skip comment lines
                else
                {   float x = Float.parseFloat(s); 
                    float y = in.nextFloat();
                    float demand = in.nextFloat();
                    Node nodeData = new Node(k, x, y, demand);
                    inputs.getNodes()[k] = nodeData;
                    k++;
                }
            }
            in.close();
             // 4. READ VEHICLE CAPACITY (HOMOGENEOUS FLEET)
            reader = new FileReader(vehiclesFilePath);
            in = new Scanner(reader);
            while( in.hasNextLine() )
            {   s = in.next();
                if( s.charAt(0) == '#' ) // this is a comment line
                    in.nextLine(); // skip comment lines
                else
                {   float vCap = Float.parseFloat(s);
                    inputs.setVehCap(vCap);
                }
            }
            in.close();
        }
        catch (IOException exception)
        {   System.out.println("Error processing inputs files: " + exception);
        }
        return inputs;
    }

    /**
     * Creates the (edges) savingsList according to the CWS heuristic.
     */
    public static void generateSavingsList(CVRPInputs inputs)
    {
        int nNodes = inputs.getNodes().length;
        VRPEdge[] savingsArray = new VRPEdge[(nNodes - 1) * (nNodes - 2) / 2];
        Node depot = inputs.getNodes()[0];
        int k = 0;
        for( int i = 1; i < nNodes - 1; i++ ) // node 0 is the depot
        {   for( int j = i + 1; j < nNodes; j++ )
            {   Node iNode = inputs.getNodes()[i];
            	Node jNode = inputs.getNodes()[j];
                // Create ijEdge and jiEdge, and assign costs and savings
                VRPEdge ijEdge = new VRPEdge(iNode, jNode);
                ijEdge.setCosts(ijEdge.calcCosts(iNode, jNode));
                ijEdge.setSavings(ijEdge.calcSavings(iNode, jNode, depot));
                VRPEdge jiEdge = new VRPEdge(jNode, iNode);
                jiEdge.setCosts(jiEdge.calcCosts(jNode, iNode));
                jiEdge.setSavings(jiEdge.calcSavings(jNode, iNode, depot));
                // Set inverse edges
                ijEdge.setInverse(jiEdge);
                jiEdge.setInverse(ijEdge);
                // Add a single new edge to the savingsList
                savingsArray[k] = ijEdge;
                k++;
            }
        }
        // Construct the savingsList by sorting the edgesList. Uses the compareTo()
        //  method of the Edge class (TIE ISSUE #1).
        Arrays.sort(savingsArray);
       
    }

    /*
     * Creates the list of paired edges connecting node i with the depot,
     *  i.e., it creates the edges (0,i) and (i,0) for all i > 0.
     */
    public static void generateDepotEdges(CVRPInputs inputs)
    {   Node[] nodes = inputs.getNodes();
    	Node depot = nodes[0]; // depot is always node 0
        // Create diEdge and idEdge, and set the corresponding costs
        for( int i = 1; i < nodes.length; i++ ) // node 0 is depot
        {   Node iNode = nodes[i];
            VRPEdge diEdge = new VRPEdge(depot, iNode);
            iNode.setDiEdge(diEdge);
            diEdge.setCosts(diEdge.calcCosts(depot, iNode));
            VRPEdge idEdge = new VRPEdge(iNode, depot);
            iNode.setIdEdge(idEdge);
            idEdge.setCosts(idEdge.calcCosts(depot, iNode));
            // Set inverse edges
            idEdge.setInverse(diEdge);
            diEdge.setInverse(idEdge);
        }
    }

    /**
    * @return geometric center for a set of nodes
    */
    public static float[] calcGeometricCenter(List<Node> nodesList)
    {
    	Node[] nodesArray = new Node[nodesList.size()];
        nodesArray = nodesList.toArray(nodesArray);
        return calcGeometricCenter(nodesArray);
    }

    public static float[] calcGeometricCenter(Node[] nodes)
    {
        // 1. Declare and initialize variables
	float sumX = 0.0F; // sum of x[i]
	float sumY = 0.0F; // sum of y[i]
	float[] center = new float[2]; // center as (x, y) coordinates
	// 2. Calculate sums of x[i] and y[i] for all iNodes in nodes
	Node iNode; // iNode = ( x[i], y[i] )
	for( int i = 0; i < nodes.length; i++ )
	{   iNode = nodes[i];
            sumX = sumX + iNode.getX();
            sumY = sumY + iNode.getY();
	}
	// 3. Calculate means for x[i] and y[i]
	center[0] = sumX / nodes.length; // mean for x[i]
	center[1] = sumY / nodes.length; // mean for y[i]
	// 4. Return center as (x-bar, y-bar)
	return center;
    }
    /**Simple parser to read the TSPLIB file format 
    The aim is to read the three sections of the file and put thes results in different arrays
	These arrays can then be used to build the relevant objects needed by the system.*/
    
    public static CVRPInputs getTSPLIBFormat(String filename) throws FileNotFoundException {
		
				
		List<String> strArr = new ArrayList<String>();
    	Scanner s = new Scanner(new BufferedReader(new FileReader(filename))); 
    	int capacity = 0;
    	int dimension = 0;
    	CVRPInputs inputs = null;
    	
    	try {    		
    		while (s.hasNextLine()) {   
    			strArr.add(s.nextLine());	
    			
    		}
	    	s.close();
	    	
	    	
	    	int nod = strArr.indexOf("NODE_COORD_SECTION");
	    	int dem = strArr.indexOf("DEMAND_SECTION");
	    	int dep = strArr.indexOf("DEPOT_SECTION");
	    	List<String> data_sec = strArr.subList(0, nod);
	    	List<String> node_sec = strArr.subList(nod+1, dem);
	    	List<String> dep_sec = strArr.subList(dem+1, dep);
	    	
	    	//add gobal details
			for(int j = 0; j <  data_sec.size();j++){			
				
	    		if(data_sec.get(j).startsWith("CAPACITY")){     			
	    			capacity = Integer.parseInt(scanString(data_sec.get(j)));		
		    		
	    		} 
	    		else if(data_sec.get(j).startsWith("DIMENSION")){     			
	    			dimension = Integer.parseInt(scanString(data_sec.get(j)));		
	    			
	    		} 
	    		
			}
			inputs = new CVRPInputs(dimension);
			inputs.setVehCap(capacity);
			//createList
			for(int i =0; i < node_sec.size(); i++ ){	    	
	    		
				
	    		List<Integer> coord = scanInt(node_sec.get(i));    			   		
    			List<Float> coord1 = scanFloat(dep_sec.get(i));
    			
        		
    			Node nodeData = new Node(coord.get(0).intValue(),coord.get(1),coord.get(2),coord1.get(1));
        		inputs.getNodes()[i] = nodeData;
        		
	    	}
					
		   
    	}
    	catch (NoSuchElementException e){
    	}
    	
       
       
        
        
    
    	//instantiate the solution wrapper holding the only copy of the data
    	return inputs;    	  	  
    }

	

	
	

	private static List<Float> scanFloat(String str){
		List<Float> result = new ArrayList<Float>();
		Scanner s = new Scanner(str);
		while(s.hasNextFloat()){
			//System.out.println("Int "+ s.nextInt());
			result.add(s.nextFloat());
		}
		s.close();
		return result;
		
	}
	
	private static List<Integer> scanInt(String str){
		List<Integer> result = new ArrayList<Integer>();
		Scanner s = new Scanner(str);
		while(s.hasNextInt()){
			//System.out.println("Int "+ s.nextInt());
			result.add(s.nextInt());
		}
		s.close();
		return result;
		
	}
	
	private static String scanString(String str){
		String result= null;
		Scanner s = new Scanner(str);
		while(s.hasNext()){
			s.next();
			s.next();
			result = s.next();
		}
		s.close();
		return result;
	}
}