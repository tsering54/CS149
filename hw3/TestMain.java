import java.util.ArrayList;
import java.util.Scanner;

public class TestMain {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out
				.print("Enter number of customers to create and assign to ticket sellers:  ");
		int amtCustomers = in.nextInt();
		in.close();

		SeatMatrix matrix = new SeatMatrix(); // 2-D Array of Customer objects,
												// null if seat is empty
		ArrayList<Thread> threads = new ArrayList<Thread>(); // Maintains all
																// the Seller
																// Threads

		// Creates a thread for 1 High-priority seller
		String sellerType = "H";
		Thread hps = new Thread(new Seller(sellerType, sellerType,
				amtCustomers, matrix)); // High-priority seller
		threads.add(hps);

		// Creates 3 different threads for 3 Medium-priority seller
		sellerType = "M";
		for (int i = 1; i <= 3; i++) {
			Thread mps = new Thread(new Seller(sellerType + i, sellerType,
					amtCustomers, matrix));
			threads.add(mps);
		}

		// Creates 6 different threads for 6 Low-priority sellers
		sellerType = "L";
		for (int i = 1; i <= 6; i++) {
			Thread lps = new Thread(new Seller(sellerType + i, sellerType,
					amtCustomers, matrix));
			threads.add(lps);
		}
		
		for (Thread t : threads) {
			t.start();
		}
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}