import java.util.*;

public class Process {

	private int name;
	private float arrivalTime;
	private float runtime;
	private int priority;
	private float completionTime;
	private float timeLeft;
	private float beginTime;

	public Process(int name, float arrivalTime, float runtime, int priority) {
		this.name = name;
		this.arrivalTime = arrivalTime;
		this.runtime = runtime;
		this.priority = priority;
		this.timeLeft = runtime;
	}

	public float getRunTime() {
		return runtime;
	}

	public float getArrivalTime() {
		return arrivalTime;
	}

	public int getName() {
		return name;
	}

	static public ArrayList<Process> generateProcesses(int amtOfProcesses) {
		// Create a list of generated processes
		ArrayList<Process> pList = new ArrayList<Process>();
		for (int i = 0; i < amtOfProcesses; i++) {
			// Generate random numbers for arrival time, expected runtime, and
			// priority
			Random r = new Random();
			float arrivalTime = r.nextFloat() * 99;
			float runtime = r.nextFloat() * (float) 9.9;
			int priority = r.nextInt(4) + 1;

			Process p = new Process(i + 1, arrivalTime, runtime, priority);
			pList.add(p);
			System.out.println("Process " + p.name + " created with [Arrival Time: " + p.arrivalTime
					+ " | Expected Run Time: " + p.runtime + " | Priority: " + p.priority + "]");
		}
		Collections.sort(pList, new SortByArrival());

		return pList;

	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter amount of processes to generate: ");
		int amtOfProcesses = in.nextInt();
		in.close();

		// Run each algorithm five times
		for (int i = 0; i < 5; i++) {
			ArrayList<Process> generatedProcessQueue = generateProcesses(amtOfProcesses);
			FCFS(generatedProcessQueue);
			SJF(generatedProcessQueue);
			// SRT(generatedProcessQueue);
			RR(generatedProcessQueue);
			HPF(generatedProcessQueue);
			PHPF(generatedProcessQueue);
		}

	}

	static private void FCFS(ArrayList<Process> pList) {
		// Variables to calculate averages
		float turnAroundTimeSum = 0;
		float waitingTimeSum = 0;
		float responseTimeSum = 0;

		// Temp variables
		float processCount = 0;
		float currentTime = pList.get(0).arrivalTime;

		// Iterate through queue
		for (Process p : pList) {
			float beginTime = currentTime; // Time when process begins executing
											// (a.k.a wait time & respond time)
			float endTime = beginTime + p.runtime; // Time when process
													// ends execution (a.k.a
													// turnaround time)
			currentTime = endTime;

			// To calculate time averages
			waitingTimeSum += beginTime;
			responseTimeSum += beginTime;
			turnAroundTimeSum += endTime;
			processCount++;

			// Break once quanta is over 99
			if (currentTime > 99) {
				break;
			}
		}
		printTimeChart(pList);
		// Print run time statistics
		System.out.println("Average turn around time: " + turnAroundTimeSum / processCount);
		System.out.println("Average waiting time: " + waitingTimeSum / processCount);
		System.out.println("Average response time: " + responseTimeSum / processCount);
		System.out.println("Throughput: " + processCount / turnAroundTimeSum);
	}

	static private void SJF(ArrayList<Process> processes) {
		ArrayList<Process> pList = new ArrayList<Process>(processes); // Copy of
																		// sorted
																		// process
																		// list
		Collections.sort(pList, new SortByRuntime());
		ArrayList<Process> pQueue = new ArrayList<Process>();
		ArrayList<Process> pCompleted = new ArrayList<Process>();
		String timeChart = "";
		float turnaroundSum = 0;
		float waitingTimeSum = 0;
		float responseTimeSum = 0;
		int quanta = 0;

		// while the pQueue and pList still contain process elements
		while (!(pQueue.isEmpty() && pList.isEmpty())) {

			// if the q is empty, pList still contains elements, and quanta is
			// still below 99
			if (pQueue.isEmpty() && !pList.isEmpty() && quanta <= 99) {

				// add the first element of pList into the queue
				pQueue.add(pList.remove(0));
				Collections.sort(pQueue, new SortByRuntime());

				// while first element in queue's arrival time is bigger than
				// the time slice
				while (pQueue.get(0).arrivalTime > quanta) {
					timeChart += "|Q" + (quanta) + ": " + "wait |";
					quanta++;
				}
			}

			// if current quanta is below 99 and new processes arrive, add to
			// the queue
			if (quanta <= 99) {
				while (!pList.isEmpty() && pList.get(0).arrivalTime < quanta) {
					pQueue.add(pList.remove(0));
					Collections.sort(pQueue, new SortByRuntime());
				}
			}

			// if the queue contains elements, execute the first one
			if (!pQueue.isEmpty()) {
				Collections.sort(pQueue, new SortByRuntime());
				pQueue.get(0).timeLeft -= 1;

				// sets the begin time of the process if it just started
				if (pQueue.get(0).beginTime == 0.0f) {
					pQueue.get(0).beginTime = quanta;
				}

				if (quanta <= 99) {
					// for table formatting purposes
					if (quanta % 10 == 0) {
						timeChart += ("\n" + "|Q" + quanta + ": " + pQueue.get(0).name + " |");
					} else {
						timeChart += "|Q" + quanta + ": " + pQueue.get(0).name + " |";
					}

					// add process back into queue if it is not yet finished
					// executing
					if (pQueue.get(0).timeLeft > 0) {
						pQueue.add(pQueue.remove(0));
						Collections.sort(pQueue, new SortByRuntime());
					} else {
						pQueue.get(0).completionTime = quanta + 1;
						pCompleted.add(pQueue.remove(0));
					}

				} else {
					break;
				}
			}
			quanta++;
		}
		System.out.println("Output for SJF:");
		System.out.println(timeChart);

		for (Process p : pCompleted) {
			waitingTimeSum += p.completionTime + (p.runtime - p.arrivalTime);
			turnaroundSum += p.runtime + (p.completionTime + (p.runtime - p.arrivalTime));
			responseTimeSum += p.beginTime - p.arrivalTime;
		}

		// Statistics
		System.out.println("Average Turnaround Time: " + turnaroundSum / pCompleted.size());
		System.out.println("Average Waiting Time: " + waitingTimeSum / pCompleted.size());
		System.out.println("Average Response Time: " + responseTimeSum / pCompleted.size());
		System.out.println("Throughput: " + pCompleted.size() / turnaroundSum + "\n");
	}

	static private void SRT(ArrayList<Process> processes) {
		ArrayList<Process> pList = new ArrayList<Process>(processes);
		// Initialize ArrayList<Process> activeProcesses
		ArrayList<Process> activeProcesses = new ArrayList<Process>();
		float turnAroundTimeSum = 0;
		float waitingTimeSum = 0;
		float responseTimeSum = 0;

		// Start first Process
		activeProcesses.add(pList.get(0));
		pList.remove(0);
		Process currentProcess = activeProcesses.get(0);

		// Create array of arrival times
		float[] arrivalTime = new float[pList.size()];
		for (int i = 0; i < pList.size(); i++) {
			arrivalTime[i] = pList.get(i).arrivalTime;
		}

		int arrivalIndex = 0;
		float beginTime = currentProcess.arrivalTime;
		float endTime = -1;
		float nextArrivalTime = 0;

		while (arrivalIndex < arrivalTime.length) {
			Collections.sort(activeProcesses, new Process.SortByRuntime());
			currentProcess = activeProcesses.get(0);

			// For calculating averages
			currentProcess.beginTime = beginTime;
			responseTimeSum += currentProcess.beginTime - currentProcess.arrivalTime;

			nextArrivalTime = arrivalTime[arrivalIndex];
			endTime = Math.min(beginTime + currentProcess.runtime, nextArrivalTime); // End
																						// time
																						// of
																						// current
																						// process
																						// is
																						// either
																						// when
																						// it
																						// is
																						// finish
																						// executing
																						// or
																						// new
																						// process
																						// is
																						// added
																						// (preemptive
																						// condition)
			// If current process finished executing
			if (endTime == (beginTime + currentProcess.runtime)) {
				// For calculating average
				currentProcess.completionTime = endTime;
				turnAroundTimeSum += (currentProcess.completionTime - currentProcess.arrivalTime);
				waitingTimeSum += ((currentProcess.completionTime - currentProcess.arrivalTime)
						- currentProcess.runtime);
				// Resume
				currentProcess.runtime = 0;
				activeProcesses.remove(currentProcess);
				activeProcesses.add(pList.get(0));
				pList.remove(0);
			} else {
				// If current process was interrupted
				currentProcess.runtime = currentProcess.runtime - (nextArrivalTime - beginTime); // Subtract
																									// elapsed
																									// time
																									// from
																									// runtime
																									// of
																									// current
																									// Process
				activeProcesses.add(pList.get(0));
				pList.remove(0);
			}
			System.out.println("[" + beginTime + "---" + currentProcess.name + "---" + endTime + "]");
			arrivalIndex++;
			beginTime = endTime;
		}
		System.out.println("Output for SRT:");
		// Run Shortest Job First (non-preemptive)
		while (!activeProcesses.isEmpty()) {
			currentProcess = activeProcesses.get(0);
			endTime = beginTime + currentProcess.runtime;
			System.out.println("[" + beginTime + "---" + currentProcess.name + "---" + endTime + "]");
			activeProcesses.remove(0);
			beginTime = endTime;
		}
		// Calculate and print averages
		float processCount = arrivalTime.length;
		float totalTime = 0;
		for (float t : arrivalTime) {
			totalTime += t;
		}
		System.out.println("Average turn around time: " + turnAroundTimeSum / processCount);
		System.out.println("Average waiting time: " + waitingTimeSum / processCount);
		System.out.println("Average response time: " + responseTimeSum / processCount);
		System.out.println("Throughput: " + processCount / totalTime);
	}

	static private void RR(ArrayList<Process> processes) {

		ArrayList<Process> pList = new ArrayList<Process>(processes); // Copy of
																		// sorted
																		// process
																		// list
		ArrayList<Process> pQueue = new ArrayList<Process>();
		ArrayList<Process> pCompleted = new ArrayList<Process>();
		String timeChart = "";
		float turnaroundSum = 0;
		float waitingTimeSum = 0;
		float responseTimeSum = 0;
		int quanta = 0;

		// while the pQueue and pList still contain process elements
		while (!(pQueue.isEmpty() && pList.isEmpty())) {

			// if the q is empty, pList still contains elements, and quanta is
			// still below 99
			if (pQueue.isEmpty() && !pList.isEmpty() && quanta <= 99) {

				// add the first element of pList into the queue
				pQueue.add(pList.remove(0));

				// while first element in queue has not arrived
				while (pQueue.get(0).arrivalTime > quanta) {
					timeChart += "|Q" + (quanta) + ": " + "wait |";
					quanta++;
				}
			}

			// if current quanta is below 99 and new processes arrive, add to
			// the queue
			if (quanta <= 99) {
				while (!pList.isEmpty() && pList.get(0).arrivalTime < quanta) {
					pQueue.add(pList.remove(0));
				}
			}

			// if the queue contains elements, execute the first one
			if (!pQueue.isEmpty()) {
				pQueue.get(0).timeLeft -= 1;

				// sets the begin time of the process if it just started
				if (pQueue.get(0).beginTime == 0.0f) {
					pQueue.get(0).beginTime = quanta;
				}

				if (quanta <= 99) {
					// for table formatting purposes
					if (quanta % 10 == 0) {
						timeChart += ("\n" + "|Q" + quanta + ": " + pQueue.get(0).name + " |");
					} else {
						timeChart += "|Q" + quanta + ": " + pQueue.get(0).name + " |";
					}

					// add process back into queue if it is not yet finished
					// executing
					if (pQueue.get(0).timeLeft > 0) {
						pQueue.add(pQueue.remove(0));
					} else {
						pQueue.get(0).completionTime = quanta + 1;
						pCompleted.add(pQueue.remove(0));
					}

				} else {
					break;
				}
			}
			quanta++;
		}
		System.out.println("Output for RR:");
		System.out.println(timeChart);

		for (Process p : pCompleted) {
			waitingTimeSum += p.completionTime - (p.arrivalTime + p.runtime);
			turnaroundSum += p.completionTime - p.arrivalTime;
			responseTimeSum += p.beginTime - p.arrivalTime;
		}

		// Statistics
		System.out.println("Average Turnaround Time: " + turnaroundSum / pCompleted.size());
		System.out.println("Average Waiting Time: " + waitingTimeSum / pCompleted.size());
		System.out.println("Average Response Time: " + responseTimeSum / pCompleted.size());
		System.out.println("Throughput: " + pCompleted.size() / turnaroundSum);
	}

	static private void HPF(ArrayList<Process> pList) {
		float turnAroundSum = 0, waitingTimeSum = 0, responseTimeSum = 0;
		float processCount = 0;
		String timeChart = "";
		ArrayList<Process> p = new ArrayList<Process>(pList);
		Collections.sort(p, new SortByPriority());
		LinkedList<Process> processQueue = new LinkedList<Process>();
		for (int i = 0; i < p.size(); i++) {
			processQueue.add(p.get(i));
		}
		float currentTime = processQueue.peek().arrivalTime;
		ArrayList<LinkedList<Process>> multi = new ArrayList<LinkedList<Process>>();
		for (int i = 0; i < 4; i++) // 4 priority queues
		{
			multi.add(processQueue);
		}
		for (int i = 0; i < multi.size(); i++) {
			for (Process pr : multi.get(i)) {
				float beginTime = currentTime;
				float endtime = beginTime + pr.runtime;
				currentTime = endtime;
				waitingTimeSum += beginTime;
				responseTimeSum += beginTime;
				turnAroundSum += endtime;
				processCount++;
				if (currentTime <= 99) {
					// for table formatting purposes
					if (currentTime % 10 == 0) {
						timeChart += ("\n" + "|Q" + currentTime + ": " + pr.name + " |");
					} else {
						timeChart += "|Q" + currentTime + ": " + pr.name + " |";
					}
				} else {
					break;
				}
			}
		}
		System.out.println("Output for HPF:");
		System.out.println(timeChart);
		System.out.println("Average turn around time: " + turnAroundSum / processCount);
		System.out.println("Average waiting time: " + waitingTimeSum / processCount);
		System.out.println("Average response time: " + responseTimeSum / processCount);
		System.out.println("Throughput: " + processCount / turnAroundSum);
	}

	static private void PHPF(ArrayList<Process> processes) {
		ArrayList<Process> pList = new ArrayList<Process>(processes); // Copy of
																		// sorted
																		// process
																		// list
		Collections.sort(pList, new SortByPriority());
		ArrayList<Process> pQueue = new ArrayList<Process>();
		ArrayList<Process> pCompleted = new ArrayList<Process>();
		String timeChart = "";
		float turnaroundSum = 0;
		float waitingTimeSum = 0;
		float responseTimeSum = 0;
		int quanta = 0;
		// while the pQueue and pList still contain process elements
		while (!(pQueue.isEmpty() && pList.isEmpty())) {
			// if the q is empty, pList still contains elements, and quanta is
			// still below 99
			if (pQueue.isEmpty() && !pList.isEmpty() && quanta <= 99) {
				// add the first element of pList into the queue
				pQueue.add(pList.remove(0));
				Collections.sort(pQueue, new SortByPriority());
				// while first element in queue's arrival time is bigger than
				// the time slice
				while (pQueue.get(0).arrivalTime > quanta) {
					timeChart += "|Q" + (quanta) + ": " + "wait |";
					quanta++;
				}
			}
			// if current quanta is below 99 and new processes arrive, add to
			// the queue
			if (quanta <= 99) {
				while (!pList.isEmpty() && pList.get(0).arrivalTime < quanta) {
					pQueue.add(pList.remove(0));
					Collections.sort(pQueue, new SortByPriority());
				}
			}
			// if the queue contains elements, execute the first one
			if (!pQueue.isEmpty()) {
				Collections.sort(pQueue, new SortByPriority());
				pQueue.get(0).timeLeft -= 1;
				// sets the begin time of the process if it just started
				if (pQueue.get(0).beginTime == 0.0f) {
					pQueue.get(0).beginTime = quanta;
				}
				if (quanta <= 99) {
					// for table formatting purposes
					if (quanta % 10 == 0) {
						timeChart += ("\n" + "|Q" + quanta + ": " + pQueue.get(0).name + " |");
					} else {
						timeChart += "|Q" + quanta + ": " + pQueue.get(0).name + " |";
					}
					// add process back into queue if it is not yet finished
					// executing
					if (pQueue.get(0).timeLeft > 0) {
						pQueue.add(pQueue.remove(0));
						Collections.sort(pQueue, new SortByPriority());
					} else {
						pQueue.get(0).completionTime = quanta + 1;
						pCompleted.add(pQueue.remove(0));
					}
				} else {
					break;
				}
			}
			quanta++;
		}
		System.out.println("Output for PHPF:");
		System.out.println(timeChart);
		for (Process pr : pCompleted) {
			waitingTimeSum += pr.completionTime - (pr.arrivalTime + pr.runtime);
			turnaroundSum += pr.completionTime - pr.arrivalTime;
			responseTimeSum += pr.beginTime - pr.arrivalTime;
		}
		// Statistics
		System.out.println("Average Turnaround Time: " + turnaroundSum / pCompleted.size());
		System.out.println("Average Waiting Time: " + waitingTimeSum / pCompleted.size());
		System.out.println("Average Response Time: " + responseTimeSum / pCompleted.size());
		System.out.println("Throughput: " + pCompleted.size() / turnaroundSum);
	}

	// print FCFS timechart
	static void printTimeChart(ArrayList<Process> queue) {
		float currentTime = 0;
		for (Process p : queue) {
			float beginTime = currentTime;
			float endTime = beginTime + p.getRunTime();
			int pName = p.getName();
			System.out.println("[" + currentTime + "---" + pName + "---" + endTime + "]");
			System.out.println();
			currentTime = endTime;
			if (currentTime > 99) {
				break;
			}
		}
	}

	// Comparator class to sort by arrival time
	static class SortByArrival implements Comparator<Process> {
		public int compare(Process a, Process b) {
			return Float.compare(a.arrivalTime, b.arrivalTime);
		}
	}

	static class SortByPriority implements Comparator<Process> {
		public int compare(Process a, Process b) {
			return Float.compare(a.priority, b.priority);
		}
	}

	static class SortByRuntime implements Comparator<Process> {
		public int compare(Process a, Process b) {
			return Float.compare(a.runtime, b.runtime);
		}
	}
}
