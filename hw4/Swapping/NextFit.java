package Swapping;
import java.util.ArrayList;

public class NextFit {
	private ArrayList<Process> process;
	private int memory;
	private int time;
	private Process[] proc;
	private int memCounter;
	private int nextEmpty;
	
	NextFit(ArrayList<Process> process, int memory, int time) {

		this.process = process;
		this.memory = memory;
		this.time = time;
		proc = new Process[memory];
		memCounter = 0;
		nextEmpty = 0;
	}
	
	public int run() {
		memCounter = 0;
		for(int i = 0; i < time; i++) {
			Process p = process.get(memCounter);
			
			int empty = findEmpty(p.getSize()); 

			//Add Process to the empty memory
			if(empty >= 0) {
				addProcess(p, empty);
				MemoryMap.printMemoryMap(proc);
				memCounter++;
			}
			
			//Remove Process that are Completed
			boolean remove = false;
			remove = complete();
			if(remove) {	
				MemoryMap.printMemoryMap(proc);	
			}
			
			//Subtract the duration time until 0, to complete process
			durationTime();			
		}		
		
		System.out.println("");
		return memCounter;
	}

	//Find the Next Empty Memory Space
	public int findEmpty(int size) {
		int start = 0;
		int end = -1;
		
		if(nextEmpty == 100){
			nextEmpty = 0;
		}
		
		for(int i = nextEmpty; i < memory; i++) {
			if (proc[i] == null) {
				if (start > end)
				{	
					start = i;
					end = i;
				}
				else
				{	end = i;	}
			}
			else {
				start = i;
			}
				
			if ((end - start + 1) >= size) {
				nextEmpty = end + 1;
				return start;
			}
			
			if (i == memory - 1) {
				i = -1;
				start = 0;
				end = -1;
			}
			
			if (i == nextEmpty - 1) {
				i = memory - 1;
			}
		}
		return -1;
	}
	
	//A process is completed when duration is 0
	//Remove the process
	public boolean complete() {
		Process q = new Process();
		boolean remove = false;
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
						remove = true;
						proc[i] = null;
					}
					else if (p.getServiceDuration() == 0) {	
						proc[i] = null;
					} 
				}
			}
		}
		return remove;
	}
	
	//Adds the process to memory map
	public void addProcess( Process p, int start) {
		int i = start;
		for (; start < (i + p.getSize()); start++) {
			proc[start] = p;
		}
		
		MemoryMap.printEnterProcess(p);
	}
	
	//Subtract the duration time until 0, to complete process
	public void durationTime() {
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