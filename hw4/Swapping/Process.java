package Swapping;
import java.util.*;

//Process Class 
public class Process{
	
	private static Random random = new Random();
	private int[] processSize = {5, 11, 17, 31}; //Process Size in Pages (5, 11, 17, 31 MB)
    private int[] processServiceDuration = {1, 2, 3, 4, 5}; // Service Duration (1, 2, 3, 4, 5 Seconds)
	
	private int arrivalTime; //Arrival Time
	private int size; //Process Size
	private int serviceDuration; // Service Duration
	private String name; // Process Name
	private int serviceTime;
	List<Integer> pageNumber = new ArrayList<>();
	
	public Process() {
		this.name = "";
		this.size = 0;
		this.arrivalTime = random.nextInt(60);
		this.serviceDuration = 0;
		this.serviceTime = 0;
		}
	
	public Process(int i) {
		setProcessName(i);
		this.size = 0;
		this.arrivalTime = 0;
		this.serviceDuration = 0;
		this.serviceTime = 0;
	}
	
	public Process (String name, int size, int arrivalTime, int serviceDuration) {
		this.name = name; 
		this.size = size; 
		this.arrivalTime = arrivalTime; 
		this.serviceDuration = serviceDuration;
	}
	
	public String getName(){ 
		return name;
		}
	
	public int getArrivalTime(){
		return arrivalTime;
		}
	
	public int getSize(){ 
		return size;
		}	
	
	public int getServiceDuration(){
		return serviceDuration;
	}	
	
	public int getServiceTime() {
		return serviceTime;
	}
	
	public List getProcessPageNumber(){  
		return pageNumber;
	}	
	
	// Set arrival time
	public void setArrivalTime(int arrivalTime){
		this.arrivalTime = random.nextInt(60);
	}
	
	// Generate random service duration from 1 - 5 Seconds
	public void setServiceDuration (int serviceDuration){
		this.serviceDuration = processServiceDuration[random.nextInt(processServiceDuration.length)];
	}
	
	//Generate random process size in pages 5, 11, 17, 31 MB
	public void setProcessSize(int size){
		this.size = processSize[random.nextInt(processSize.length)];
	}
	
	// Convert process names to official names: 26 upper letters, 26 lower letters, and numbers up to (150 - 26 - 26) processes*/
	public void setProcessName(int i){
		String names ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String convertedName;
		if (i >= 0 && i <= 61){
			convertedName = names.substring(i, i+1); //convert to A-Za-z
		}
		else{
			convertedName = Integer.toString(i - 62); // 1 to total processes
		}
		name = convertedName;
	}

	public void setProcessPageNumber(int size){ 
		for(int i = 0; i < size; i++){
			pageNumber.add(i);
		}
	}
	
	public void decrementDuration() {
		serviceDuration --;
	}
	
	public void incrementDurationTime() {
		serviceTime ++;
	}
	
	public Process createClone() {
		Process clone = new Process();
		clone.setArrivalTime(arrivalTime);
		clone.setProcessSize(size);
		clone.setServiceDuration(serviceDuration);
		clone.name = name;
		return clone;
	}
	
	public String toString() {
		return "[Process Name: "+ name +" Process Size: "+ size + "MB, Service Duration: " + serviceDuration +"secs]" ;
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
}