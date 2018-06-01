import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.LinkedList;

public class Seller extends Thread {
	private SeatMatrix matrix;
	private Queue<Customer> customers; // Queue of customers
	private String sellerType; // High priority "H", medium-priority "M" or
								// low-priority "L"
	private String name; // Name of ticket seller

	/**
	 * Seller.java Class Constructor
	 * 
	 * @param name
	 *            - Name of Seller (H, M1, M2, ... L1, L2.. etc)
	 * @param sellerType
	 *            - "H", "M" or "L" for high, medium and low priority
	 * @param numCustomers
	 *            - Number of customers this seller is in charge of handling
	 */
	public Seller(String name, String sellerType, int numCustomers,
			SeatMatrix matrix) {
		this.name = name;
		this.sellerType = sellerType;
		this.customers = new LinkedList<Customer>();
		this.matrix = matrix;

		// Adds user-given number of Customers to this seller's Queue
		// Note: Customer.genCustomers generates an ArrayList of Customers that
		// is sorted by Arrival Time (see Customer.java)
		ArrayList<Customer> customerArr = Customer.genCustomers(numCustomers,
				sellerType);
		for (Customer c : customerArr) {
			this.customers.add(c);
		}
	}

	public String getSellerName() {
		return name;
	}

	public String getSellerType() {
		return sellerType;
	}

	public synchronized void sell() {
        while (!this.matrix.isFull() || !this.customers.isEmpty() || matrix.clock <= 60) {
            if (matrix.clock > 60) {
                System.out.println("Over 60 minutes");
                return;
            } else {
                if (matrix.clock < 10) {
                    System.out.println("Time 00:0" + matrix.clock);
                }
                else {
                    System.out.println("Time 00:" + matrix.clock);
                }
            }
            // Complete ticket purchase using the required amount of time
            int[] highPrioritySaleTimes = new int[] { 1, 2 };
            int[] mediumPrioritySaleTimes = new int[] { 2, 3, 4 };
            int[] lowPrioritySaleTimes = new int[] { 4, 5, 6, 7 };

            int timeElapsed = 0;

            if (matrix.isFull()) {
                System.out.println("Matrix is full");
                return;
            }
            // Finds a seat then synchronously assigns the seat to next Customer
            else {
                matrix.reserveSeat(this, this.customers.remove());
            }

            // Determines the time elapsed according to seller type
            if (sellerType.equals("H")) {
                timeElapsed = getRandomSaleTime(highPrioritySaleTimes);
            } else if (sellerType.equals("M")) {
                timeElapsed = getRandomSaleTime(mediumPrioritySaleTimes);
            } else if (sellerType.equals("L")) {
                timeElapsed = getRandomSaleTime(lowPrioritySaleTimes);
            }

            // Adds time to the global clock
            matrix.addTime(timeElapsed);
            if(this.matrix.clock <= 60){
            matrix.printMatrix();
            }
        }
    }

	public synchronized static int getRandomSaleTime(int[] array) {
		int rnd = new Random().nextInt(array.length);
		return array[rnd];
	}

	// Abstract method from Runnable(), must be implemented
	public void run() {
		sell();
		return;
	}
}