package macs.uoc.cvrp;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class Outputs
{
    /* INSTANCE FIELDS & CONSTRUCTOR */
    private VRPSolution cwsSolution;
    private VRPSolution bestSol;
    public Outputs()
    {   cwsSolution = null;
        bestSol = null;
    }

    /* SET METHODS */
    public void setCWSSol(VRPSolution cwsSol){cwsSolution = cwsSol;}
    public void setOBSol(VRPSolution obSol){bestSol = obSol;}

    /* GET METHODS */
    public VRPSolution getCWSSol(){return cwsSolution;}
    public VRPSolution getOBSol(){return bestSol;}

    /* AUXILIARY METHODS */
    public void sendToFile(String outFile)
    {
        try 
        {   PrintWriter out = new PrintWriter(outFile);
            out.println("***************************************************");
            out.println("*                      OUTPUTS                    *");
            out.println("***************************************************");
            out.println("\r\n");
            out.println("--------------------------------------------");
            out.println("Clarke & Wright Solution (parallel version)");
            out.println("--------------------------------------------");
            out.println(cwsSolution.toString() + "\r\n");
            out.println("--------------------------------------------");
            out.println("\r\n OUR BEST SOLUTION:\r\n");
            out.println("--------------------------------------------");
            out.println(bestSol.toString() + "\r\n");
            out.close();
        } catch (IOException exception) 
        {   System.out.println("Error processing output file: " + exception);
        }
    }
}