package macs.uoc.pfsp.api;


public interface PFSPJob extends Comparable<PFSPJob> {

	public void setId(int jobId);

	public void setExpProcessingTime(int machine, double time);

	public void setProcessingTime(int machine, int time);
	
	public void setProcessingTimes(int[] processingTimes);

	public void setTotalProcessingTime(int time);
	
	

	/*******************************************************************************
	 * GET METHODS
	 ******************************************************************************/

	public int getId();

	public double getExpProcessingTime(int machine);

	public int getProcessingTime(int machine);
	
	public int[] getProcessingTimes();

	public int getTotalProcessingTime();

}
