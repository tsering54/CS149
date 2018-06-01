package Paging;

import java.util.*;
import java.util.Random;

public class Main 
{
	public static class SortByArrival implements Comparator<Process> {
		public int compare(Process x, Process y)
		{
			if (x.getArrivalTime() == y.getArrivalTime())
			{
				return 0;
			} 
			else if (x.getArrivalTime() > y.getArrivalTime())
			{
				return 1;
			} 
			else 
			{
				return -1;
			}
		}
	}
	
	public static void main(String[] args)
	{
		//Generate the workload
		Random r = new Random();
		ArrayList<Process> pList = new ArrayList<Process>();
		for (int i = 0; i < 150; i++) {
			int size = r.nextInt(3);
			switch (size) {
				case 0: size = 5; break;
				case 1: size = 11; break;
				case 2: size = 17; break;
				case 3: size = 31; break;
			}
			
			int serviceDuration = r.nextInt(5)+1;
			int arrivalTime = r.nextInt(60);
			pList.add(new Process(Integer.toString(i), size, arrivalTime, serviceDuration));
		}
		//Represents workload as sorted queue based on arrival time
		Collections.sort(pList, new SortByArrival());
		
		//Run the simulation 5 times, 1 minute each, using the different
		// replacement algorithms
		//IMPLEMENT CLOCK DO THIS

		Simulation[] simulation = new Simulation[]
				{
						new Simulation(new FIFO(), "FIFO Algorithm"),
						new Simulation(new LRU(), "LRU Algorithm"),
						new Simulation(new LFU(), "LFU Algorithm"),
						new Simulation(new MFU(), "MFU Algorithm"),
						new Simulation(new RandomAlgorithm(), "Randomly Generating Algorithm")
				};
		
		for(Simulation sim : simulation) //Runs the simulation 5 times
		{
			sim.addProcess(pList);
			System.out.println("Starting paging simulation using " + sim.getName());
			sim.page.run();
			double hitRate = sim.page.getHitRatio();
			sim.hitRatioTot = sim.hitRatioTot + hitRate;
			System.out.println();
			System.out.println("Hit Ratio: " + hitRate);
			System.out.println("Ending pagiging simulation using " + sim.getName());
			System.out.println();
			sim.page.restart();
		}

		double hitRatioTot = 0;
		double avgHitRatio = 0;
		double swappedTot = 0;
		double avgSwapped = 0;

		//Collect and save the statistics and exits
		//DO THIS

		//Compute the hit/miss ratio of pages referenced by the
		// running jobs for each run through the simulation
		for(Simulation s : simulation)
		{
			hitRatioTot += s.getHitRatio();
			swappedTot += s.page.getSwapped();
		}

		//Average hit/miss ratio of the 5 runs
		avgHitRatio = hitRatioTot / 5;
		System.out.println("Average Hit Ratios over 5 runs: " +  avgHitRatio);

		//For each replacement algorithm, print the avg num of procs
		// (over the 5 runs) that were successfully swapped in
		//CHECK THIS
		avgSwapped = swappedTot / 5;
		System.out.println("Average Successfuly Swapped in: " + avgSwapped);
		

	}
}
