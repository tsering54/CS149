package Swapping;
//Memory Map
//	100 pages is defined as <AAbbbACCAAbbbbbbbbCCC.... ddddddddddd... 33333>

public class MemoryMap {

	//Prints the Memory Map
	public static void printMemoryMap(Process[] process) {
		System.out.print("          <");
		for(int i = 0; i < process.length + 1; i++)
		{	
			if (i == (process.length) ) {	
				System.out.println(">"); //End of 100 Pages
			}
			else if (process[i] == null) {	
				System.out.print("."); // If there is a hole (null) in a page -> print a dot (".")
			}
			else {	
				System.out.print(process[i].getName()); //Prints the processes name -> single character	
			}
		}
	}

	// Prints When a Process is Swapped In (Enters)
	public static void printEnterProcess(Process p) {
		if (p.getArrivalTime() < 10)
			System.out.println("[Time: 0" + p.getArrivalTime() + "] Enter: " + p.toString());		
		else
			System.out.println("[Time: " + p.getArrivalTime() + "] Enter: " + p.toString());		
	}
	
	// Prints When a Process is Complete (Exits)
	public static void printExitProcess(Process p) {
		if ((p.getArrivalTime() + p.getServiceTime()) < 10)
			System.out.println("[Time: 0" + (p.getArrivalTime() + p.getServiceTime()) + "] Exit : [Process " + p.getName() +"]");
		else
			System.out.println("[Time: " + (p.getArrivalTime() + p.getServiceTime()) + "] Exit : [Process " + p.getName() +"]");
	}
}