/** 
 * MACS - Multi-Agent Cooperative Search is a framework to develop cooperating agents using 
 * different Metaheuristics Copyright (C) 2016 Angel Alejandro Juan Perez. This file is part of MACS. 
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
package macs.uoc.pfsp.app;
import java.util.ArrayList;

import macs.uoc.pfsp.api.PFSPInputs;
import macs.uoc.pfsp.api.PFSPJob;

/***********************************************************************************
 * Project SimScheduling -LocalSearch.java
 *
 * This class contains all the local search procedures.
 *
 * Date of last revision (YYMMDD): 110407
 * (c) Angel A. Juan & Quim Castella - http://ajuanp.wordpress.com
 **********************************************************************************/

public class LocalSearch
{
    
	/******************************************************************************* 
     * INSTANCE FIELDS 
     * ****************************************************************************/
     
    private PFSPTest aTest; // Test to run (including parameters and instance's name)
    private PFSPInputs inputs; // Instance inputs
    private Randomness random;

    private int[] positions; // Array of randomly selected positions
    private int nJobs; // #Jobs
    
    /******************************************************************************* 
     * CLASS CONSTRUCTOR 
     * ****************************************************************************/
      
    public LocalSearch(PFSPTest test, PFSPInputs inputs2)
    {     
    	aTest = test;
    	inputs = inputs2;
        random = new Randomness(aTest, inputs);

    	positions = new int[nJobs];
        nJobs = inputs.getNumberOfJobs();
    }				

    public void globalImprovement(PFSPSolution aSol)
    {
        boolean hasImproved;
        int beforeCosts;
        do
        {
            hasImproved = false;
            beforeCosts = aSol.getCosts();

            randomJobShifting(aSol);
            if( beforeCosts > aSol.getCosts() )
                hasImproved = true;
        } while( hasImproved );
    }


	/******************************************************************************* 
	* PUBLIC METHOD randomShiftingJob()
    *
    * Try to improve the solution by taking the jobs randomly without repetition
    * and shifting them from the end to the left.
	******************************************************************************/
	
	public void randomJobShifting(PFSPSolution aSol)
    {
        positions = random.calcPositionsArray("uniform"); //uniform

		for( int i = 0; i < nJobs - 1; i++ )
        {
			int j = positions[i];
			if( j < nJobs - 1)
			{
				PFSPJob aJob = aSol.getJobs()[j];
                System.arraycopy(aSol.getJobs(), j+1, aSol.getJobs(), j, nJobs-1-j);
				aSol.getJobs()[nJobs - 1] = aJob;
			}
			aSol.improveByShiftingJobToLeft(nJobs - 1);	
		}
	}

    /*******************************************************************************
	* PUBLIC METHOD enhancedSwap()
    * Perturbation method
	*******************************************************************************/
    
    public void enhancedSwap(PFSPSolution aSol)
    {
        int posA = random.getRandomPosition(nJobs, "uniform");
        int posB = random.getRandomPosition(nJobs, "uniform");
        while (posB == posA)
            posB = random.getRandomPosition(nJobs, "uniform");

        swapJobs(aSol, posA, posB);
        if( posA < posB )
        {
            aSol.improveByShiftingJobToLeft(posA);
            aSol.improveByShiftingJobToLeft(posB);
        }
        else
        {
            aSol.improveByShiftingJobToLeft(posB);
            aSol.improveByShiftingJobToLeft(posA);
        }
        aSol.setCosts(aSol.calcTotalCosts(nJobs));
    }
    
    public void swapJobs(PFSPSolution aSol, int posA, int posB)
    {        
        PFSPJob aux = aSol.getJobs()[posA];
        aSol.setJob(posA,aSol.getJobs()[posB]);
        aSol.setJob(posB,aux);
    }
    
    /*******************************************************************************
	* Other Perturbation Methods (used in other versions)
	*******************************************************************************/
    
    public void randomSwapping(PFSPSolution aSol)
    {
        int posA = random.getRandomPosition(nJobs, "uniform");
        int posB = random.getRandomPosition(nJobs, "uniform");
        while (posB == posA)
            posB = random.getRandomPosition(nJobs, "uniform");

        swapJobs(aSol, posA, posB);
        aSol.setCosts(aSol.calcTotalCosts(nJobs));
    }

    public void adjacentSwap(PFSPSolution aSol, int nPairs)
    {
        for( int i = 0; i< nPairs; i++)
        {
            int pos = random.getRandomPosition(nJobs-1, "uniform");
            swapJobs(aSol, pos, pos+1);
        }
        aSol.setCosts(aSol.calcTotalCosts(nJobs));
    }

    public void randomDestructionConstruction(PFSPSolution aSol, int d)
    {
		ArrayList<PFSPJob> jobList = new ArrayList<PFSPJob>();
		for(int i = 0; i < d; i++ )
		{
            int pos = random.getRandomPosition(nJobs - i, "uniform");
			jobList.add(aSol.getJobs()[pos]);
            System.arraycopy(aSol.getJobs(), pos+1, aSol.getJobs(), pos, nJobs-1 - pos);
		}

		for( int i = 0; i < d; i++ )
		{
			aSol.getJobs()[nJobs - d + i] = jobList.get(i);
			aSol.improveByShiftingJobToLeft(nJobs - d + i);
		}
	}

    public void randomInsertion(PFSPSolution aSol)
    {
        int inPos = random.getRandomPosition(nJobs,"uniform");
        int endPos = random.getRandomPosition(nJobs, "uniform");
        while(inPos == endPos)
            endPos = random.getRandomPosition(nJobs, "uniform");

        this.insertion(inPos,endPos,aSol);
    }

    public void enhancedInsertion(PFSPSolution aSol)
    {
        int inPos = random.getRandomPosition(nJobs,"uniform");
        int endPos = random.getRandomPosition(nJobs, "uniform");
        while(inPos == endPos || endPos == nJobs-1)
            endPos = random.getRandomPosition(nJobs, "uniform");

        if(inPos < endPos)
        {
            int aux = endPos;
            endPos = inPos;
            inPos = aux;
        }
        this.insertion(inPos,endPos,aSol);

        aSol.improveByShiftingJobToLeft(endPos);

        aSol.setCosts(aSol.calcTotalCosts(nJobs));
    }

    public void insertion(int inPos, int endPos, PFSPSolution aSol)
    {
        int dif = endPos - inPos;

        PFSPJob inJob = aSol.getJobs()[inPos];
        if( dif > 0 )
        {
            System.arraycopy(aSol.getJobs(), inPos+1, aSol.getJobs(), inPos, dif);
        }
        else
        {
            dif = -dif;
            System.arraycopy(aSol.getJobs(), endPos, aSol.getJobs(), endPos+1, dif);
        }
        aSol.getJobs()[endPos]= inJob;
    }

    public void partialImprovement(PFSPSolution aSol)
    {
        int endPos = 0;
        PFSPJob auxJob = aSol.getJobs()[endPos];

        while(auxJob == aSol.getJobs()[endPos])
        {
            endPos = 1 + random.getRandomPosition(nJobs - 2, "uniform");
            auxJob = aSol.getJobs()[endPos];
            aSol.improveByShiftingJobToLeft(endPos);
        }
        aSol.setCosts(aSol.calcTotalCosts(nJobs));
    }
       
}