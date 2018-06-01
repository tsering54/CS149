public class SeatMatrix {
	private Customer[][] seatMatrix;
	int clock = 0; // Represents minutes in an hour
	private int highStart = 0; // High-priority starts selling from row 1 (-1)
	private int medStart = 4; // Med-priority starts selling from row 5 (-1)
	private int lowStart = 9; // Low-priority starts selling from row 10 (-1)

	public SeatMatrix() {
		seatMatrix = new Customer[10][10];
		setMatrix(); // Sets every seat in the matrix to "null"
	}

	/**
	 * Initializes all spaces in 10x10 2-D Array to null
	 */
	public void setMatrix() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				seatMatrix[i][j] = null;
			}
		}
	}

	/**
	 * Getter method for seatMatrix
	 * 
	 * @return - Customer[][] seatMatrix
	 */
	public Customer[][] getMatrix() {
		return seatMatrix;
	}

	/**
	 * Checks to see if seatMatrix is filled with customers or there is at least
	 * one empty seat
	 * 
	 * @return - True if seatMatrix is full, false if there is an empty seat
	 */
	public synchronized boolean isFull() {
		boolean isFull = true;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (seatMatrix[i][j] == null) {
					isFull = false;
				}
			}
		}
		return isFull;
	}

	/**
	 * Used by high-priority seller to find the next empty seat and reserve it
	 * for the Customer
	 */
	public int[] reserveHigh() {
		int[] seatLocation = new int[2];
		// Iterate through the matrix starting with row 1 (-1)
		outterloop: for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				// If the seat is empty, reserve it
				if (seatMatrix[i][j] == null) {
					seatLocation[0] = i;
					seatLocation[1] = j;
					break outterloop;
				}
			}

		}
		return seatLocation;
	}

	/**
	 * Used by medium-priority seller to find and reserve a seat
	 */
	public int[] reserveMed() {
		int[] seatLocation = new int[2];
		int iteration = 0;
		int rowIndex = 4;
		int rowIndexBottom = 4;
		int rowIndexTop = 4;
		boolean seatFound = false;
		while (!seatFound) {
			for (int j = 0; j < 10; j++) {
				// If the seat is empty, reserve it
				if (seatMatrix[rowIndex][j] == null) {
					seatLocation[0] = rowIndex;
					seatLocation[1] = j;
					seatFound = true;
					break;
				}
			}
			rowIndexTop++;
			rowIndexBottom--;
			if (iteration % 2 == 0) {
				rowIndex = rowIndexTop;
			} else {
				rowIndex = rowIndexBottom;
			}
		}

		return seatLocation;
	}

	/**
	 * Used by low-priority seller to find and reserve a seat Returns seat
	 * location
	 */
	public int[] reserveLow() {
		int[] seatLocation = new int[2];
		// Iterate through the matrix backwards starting with row 10 (-1)
		outterloop: for (int i = 9; i >= 0; i--) {
			for (int j = 9; j >= 0; j--) {
				if (seatMatrix[i][j] == null) {
					seatLocation[0] = i;
					seatLocation[1] = j;
					break outterloop;
				}
			}
		}
		return seatLocation;
	}

	public synchronized void reserveSeat(Seller seller, Customer customer) {
		int[] seatLocation = new int[2];
		if (seller.getSellerType().equals("H")) {
			seatLocation = reserveHigh();
		} else if (seller.getSellerType().equals("M")) {
			seatLocation = reserveMed();
		} else if (seller.getSellerType().equals("L")) {
			seatLocation = reserveLow();
		}
		assignSeat(seatLocation[0], seatLocation[1], customer);
	}

	public synchronized void assignSeat(int row, int col, Customer customer) {
		this.seatMatrix[row][col] = customer;
	}

	public synchronized void addTime(int timeElapsed) {
		this.clock += timeElapsed;
	}

	public synchronized void printMatrix() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (seatMatrix[i][j] == null) {
					// Seat is empty
					System.out.print("---,");
				} else {
					System.out.print(seatMatrix[i][j].toString() + ",");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}