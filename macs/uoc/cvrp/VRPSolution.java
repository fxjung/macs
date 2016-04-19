package macs.uoc.cvrp;
import java.util.LinkedList;

/**
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class VRPSolution
{
    /* INSTANCE FIELDS & CONSTRUCTOR */
    private static long nInstances = 0; // number of instances
    private long id; // solution ID
    private double costs = 0.0; // solution costs
    private double demand = 0.0F; // total demand
    private LinkedList<Route> routes; // list of routes in this solution
    private double time = 0.0; // elapsed computational time (in seconds)

    public VRPSolution()
    {   nInstances++;
        id = nInstances;
        routes = new LinkedList<Route>();
    }
   
    /* GET METHODS */
    public LinkedList<Route> getRoutes(){return routes;}
    public long getId(){return id;}
    public double getCosts(){return costs;}
    public double getDemand(){return demand;}
    public double getTime(){return time;}
    
    /* SET METHODS */
    public void setCosts(double c){costs = c;}
    public void setDemand(double d){demand = d;}
    public void setTime(double t){time = t;}
    
    /*  AUXILIARY METHODS */
    
    public void addCosts(double nCost){
    	this.costs =  this.costs+nCost;
    }
    
    @Override
    public String toString()
    {
        Route aRoute; // auxiliary Route variable
        String s = "";
        s = s.concat("\r\n");
        s = s.concat("Sol ID : " + getId() + "\r\n");
        s = s.concat("Sol costs: " + getCosts() + "\r\n");
        s = s.concat("# of routes in sol: " + routes.size());
        s = s.concat("\r\n\r\n\r\n");
        s = s.concat("List of routes (cost and nodes): \r\n\r\n");
        for (int i = 1; i <= routes.size(); i++)
        {   aRoute = routes.get(i - 1);
            s = s.concat("Route " + i + " || ");
            s = s.concat("Costs = " + aRoute.getCosts() + " || ");
            s = s.concat("Demand  = " + aRoute.getDemand()+ " || ");
            s = s.concat("\r\n");
        }
        s = s.concat("\r\n\r\n");
        return s;
    }
}