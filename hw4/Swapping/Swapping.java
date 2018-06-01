package Swapping;
import java.util.*;

public class Swapping {
    private static final int MEMORY = 100;// Main Memory has 100 MB
    private static final int TIME = 60; // Max Time of 60 Seconds
	
	public static void main(String[] args) {
		//Create 5 Process Arrays
	
		ArrayList<Process> p1 = new ArrayList<Process>();
		ArrayList<Process> p2 = new ArrayList<Process>();
		ArrayList<Process> p3 = new ArrayList<Process>();
		ArrayList<Process> p4 = new ArrayList<Process>();
		ArrayList<Process> p5 = new ArrayList<Process>();
		
		//Generates 150 Random Processes
		for(int i=0; i<150; i++)
		{
		    Process n = new Process(i);
		    p1.add(n);
		    p2.add(n);
		    p3.add(n);
		    p4.add(n);
		    p5.add(n);
		}

		System.out.println("===First Fit===");
		//First Fit Memory Management Algorithm
		FirstFit ff1 = new FirstFit(deepCopy(p1), MEMORY, TIME);
		FirstFit ff2 = new FirstFit(deepCopy(p2), MEMORY, TIME);
		FirstFit ff3 = new FirstFit(deepCopy(p3), MEMORY, TIME);
		FirstFit ff4 = new FirstFit(deepCopy(p4), MEMORY, TIME);
		FirstFit ff5 = new FirstFit(deepCopy(p5), MEMORY, TIME);
	
		//Returns # of Successful Swaps
		int ffp1 = ff1.run();
		int ffp2 = ff2.run(); 
		int ffp3 = ff3.run(); 
		int ffp4 = ff4.run(); 
		int ffp5 = ff5.run();
		
		System.out.println("===Next Fit===");
		
		//Next Fit Memory Management Algorithm		
		NextFit nf1 = new NextFit(deepCopy(p1), MEMORY, TIME);
		NextFit nf2 = new NextFit(deepCopy(p2), MEMORY, TIME);
		NextFit nf3 = new NextFit(deepCopy(p3), MEMORY, TIME);
		NextFit nf4 = new NextFit(deepCopy(p4), MEMORY, TIME);
		NextFit nf5 = new NextFit(deepCopy(p5), MEMORY, TIME);
		
		//Returns # of Successful Swaps
		int nfp1 = nf1.run(); 
		int nfp2 = nf2.run();
		int nfp3 = nf3.run();
		int nfp4 = nf4.run();
		int nfp5 = nf5.run();
		
		System.out.println("===Best Fit===");
		
		//Best Fit Memory Management Algorithm
		BestFit bf1 = new BestFit(deepCopy(p1), MEMORY, TIME);
		BestFit bf2 = new BestFit(deepCopy(p2), MEMORY, TIME);
		BestFit bf3 = new BestFit(deepCopy(p3), MEMORY, TIME);
		BestFit bf4 = new BestFit(deepCopy(p4), MEMORY, TIME);
		BestFit bf5 = new BestFit(deepCopy(p5), MEMORY, TIME);
		
		//Returns # of Successful Swaps
		int bfp1 = bf1.run();
		int bfp2 = bf2.run();
		int bfp3 = bf3.run();
		int bfp4 = bf4.run();
		int bfp5 = bf5.run();
		
		System.out.println("===Averages===");
		System.out.println("First Fit : Average Number of Process Successfully Swapped: " + (ffp1+ffp2+ffp3+ffp4+ffp5) / 5.0);
		System.out.println("Next  Fit : Average Number of Process Successfully Swapped: " + (nfp1+nfp2+nfp3+nfp4+nfp5) / 5.0);
		System.out.println("Best  Fit : Average Number of Process Successfully Swapped: " + (bfp1+bfp2+bfp3+bfp4+bfp5) / 5.0);
	}
	
	static class SortByArrival implements Comparator<Process> {
		public int compare(Process a, Process b) {
			if (a.getArrivalTime() == b.getArrivalTime())
			{
				return 0;
			} 
			else if (a.getArrivalTime() > b.getArrivalTime())
			{
				return 1;
			} 
			else 
			{
				return -1;
			}
		}
	}
	
	//Copies List of Processes
    public static ArrayList<Process> deepCopy(ArrayList<Process> list) {
    		ArrayList<Process> clone = new ArrayList<Process>();	
    		for(Process p: list) {   
    			clone.add(p.createClone());   
    		}    	
    		Collections.sort(clone, new SortByArrival());
    		return clone;
    }
}