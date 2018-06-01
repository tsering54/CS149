package Paging;
import java.util.*;

public class Process {
	private int arrivalTime;
	private int size;
	private int serviceDuration;
	private String name;
	List<Integer> pageNumber = new ArrayList<>();
	
	
	public Process (String name, int size, int arrivalTime, int serviceDuration) {
		this.name = name; 
		this.size = size; 
		this.arrivalTime = arrivalTime; 
		this.serviceDuration = serviceDuration;
	}
	
	public Process () {};
	
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
	
	public List getProcessPageNumber(){ 
		return pageNumber;
		}	
	
	
	// set arrival time
	public void setArrivalTime(int arr){
		arrivalTime = arr;
	}
	
	// generate random service duration from 1 - 5
	public void setServiceDuration (int n){
		serviceDuration = n;
	}
	
	//generate random process size in pages 5, 11, 17, 31 MB
	public void setProcessSize(int n){
		size = n;
	}
	
	public void setProcessPageNumber(int size){
		for(int i = 0; i < size; i++){
			pageNumber.add(i);
		}
	}
	
	// convert process names to official names: 26 upper letters, 26 lower letters, and numbers up to (150 - 26 - 26) processes*/
	public void setProcessName(int i){
		String names ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		String convertedName;
		if (i >= 0 && i <= 51){
			convertedName = names.substring(i, i+1); //convert to A-Za-z
		}
		else{
			convertedName = Integer.toString(i - 52); //1 to total processes
		}
		name = convertedName;
	}
	
	public String toString() {
		return "Process: "+ name +" [Process Size: "+ size+" Arrival Time: "+ arrivalTime +
					", Service Duration: " + serviceDuration +"]" ;
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