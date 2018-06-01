package Swapping;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

// Best Fit
public class BestFit {

	private ArrayList<Process> process;
	private int memory;
	private int time;
	private Process[] proc;
	private int memCounter;

	BestFit( ArrayList<Process> process, int memory, int time) {

		this.process = process;
		this.memory = memory;
		this.time = time;
		proc = new Process[memory];
		memCounter = 0;
	}

	public int run() {
		memCounter = 0;

		for(int i = 0; i < time; i++) {
			Process p = process.get(memCounter);

			int empty = findEmpty(p.getSize()); // Find Empty Memory Space

			if(empty >= 0) {
				addProcess(p, empty); // Add Process
				MemoryMap.printMemoryMap(proc);
				memCounter++; 
			}

			boolean remove = false;
			remove = complete();
			if(remove) {	
				MemoryMap.printMemoryMap(proc);
			}

			// Subtract duration time until 0
			durationTime();
		}		

		System.out.println("");
		return memCounter;
	}

	public int findEmpty(int size) {
		int start = 0;
		int end = -1;

		Hashtable<Integer, Integer> best = new Hashtable<Integer, Integer>();

		for(int i = 0; i < memory; i++) {
			if(i == memory -1 && (end - start + 1) >= size) {
				best.put((end - start + 1), start);
			}
			else if (proc[i] == null) {
				if (start > end)
				{	
					start = i;
					end = i;
				}
				else
				{	
					end = i;	
				}
			}
			else {
				if((end - start + 1) >= size) {
					best.put((end - start + 1), start);
				}
				start = i;
			}
		}

		// if(best.isEmpty()) {
		// retrun best
		if(!best.isEmpty()) {
			return findBest(best);
		}
		return -1;

	}

	public void addProcess( Process p, int start) {
		int i = start;
		for (; start < (i + p.getSize()); start++) {
			proc[start] = p;
		}

		MemoryMap.printEnterProcess(p);
	}
	
	public static Integer findBest(Hashtable<Integer, Integer> best){
		ArrayList<Integer> keys = new ArrayList<Integer>(best.keySet());
		Collections.sort(keys);

		return best.get(keys.get(0));
	}

	public boolean complete() {
		Process q = new Process();
		boolean removed = false;
		for (int i = 0; i < memory; i++)
		{
			for(Process p: process)
			{
				if ( p.equals(proc[i]) )
				{
					if (p.getServiceDuration() == 0 && !p.equals(q))
					{	
						q = proc[i];
						MemoryMap.printExitProcess(proc[i]);
						removed = true;
						proc[i] = null;
					}
					else if (p.getServiceDuration() == 0) {
						proc[i] = null;	
					} 
				}
			}
		}
		return removed;
	}

	public  void durationTime() {
		for (int i = 0; i < memory; i++)
		{
			for(Process p: process)
			{
				if ( p.equals(proc[i]) )
				{
					if (i == 0) {	
						p.decrementDuration();
						p.incrementDurationTime();
					} 
					else if (proc[i]!= proc[i-1])	{	
						p.decrementDuration();
						p.incrementDurationTime();
					}
				}
			}
		}
	}

}