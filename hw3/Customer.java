import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Customer 
{
	private int arrivalTime;
	private String name = "";
	private static final int MAX_ARRIVAL_TIME = 59;
	
	/**
	 * Constructor for Customer will initialize the arrivalTime of this Customer with a random integer from 0-59 (representing 
	 * hours in a minute)
	 */
	public Customer() {
		this.arrivalTime = genArrivalTime();
	}
	
	public Customer(String name) {
		this.name = name;
		this.arrivalTime = genArrivalTime();
	}
	
	public void setName(String n)
	{
		name = n;
	}
	
	public String getName()
	{
		return name;
	}
	
	/**
	 * Generate a random integer from 0 -59 representing minutes in an hour
	 * @return - Integer from 0-59
	 */
	public int genArrivalTime()
	{
		Random r = new Random();
		int arrivalTime = r.nextInt() % (MAX_ARRIVAL_TIME + 1);
		return arrivalTime;
	}
	
	public int getArrivalTime() {return this.arrivalTime;}
	
	/**
	 * Generates an ArrayList of Customer objects sorted by arrival time
	 * @param n - Number of customers to generate
	 * @return - Array of randomly generated customers
	 */
	public static ArrayList<Customer> genCustomers(int n, String sellerType) 
	{
		ArrayList<Customer> customers = new ArrayList<Customer>();
		for(int i = 0; i < n; i++) { customers.add(new Customer(sellerType + "-" + i)); }
		Collections.sort(customers, new CustomerSortByArrivalTime());
		return customers;
	}
	
	public String toString() {return this.name;}
	
	/**
	 * CustomerSortByArrivalTime is a utility class that implements Comparator<Customer> to sort Customers by arrival time
	 * Can be used in Combination with Collections.sort() and any List<Customer>
	 */
	public static class CustomerSortByArrivalTime implements Comparator<Customer> {
		public int compare(Customer x, Customer y)
		{
			if (x.arrivalTime == y.arrivalTime)
			{
				return 0;
			} 
			else if (x.arrivalTime > y.arrivalTime)
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