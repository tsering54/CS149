package Paging;
import java.util.*;

public class Page 
{
	private List<Page> disk;
	private List<Page> memory;
	private ArrayList<Process> pList;
	private Queue<Page> queue;
	private int swapped = 0;
	private int pageNumber;
	protected int previousReferences = -1;
	Page[] pages;
	protected int pageUsage; //Instance variable for Algorithm calculation
	protected int pageReferences = 0;
	private boolean exists;
	private double hits = 0;

	public Page(int pageNumber)
	{
		this.pageNumber = pageNumber;
		exists = false;
		pageUsage = 0;
	}

	public Page()
	{
		// When starting, there are only 4 free pages Physical memory frames
		memory = new ArrayList<>(4); 
		// There are always 11 page frames available on disk
		disk = new ArrayList<>(11);
		queue = new LinkedList<>();
	}
	
	public void setProc(ArrayList<Process> p)
	{
		pList = p;
	}

	public int getPage() { return 0; }

	public double getHitRatio()
	{
		double hitRatio = hits/pageReferences;
		return hitRatio;
	}

	public int getSwapped()
	{
		return swapped;
	}

	public int getPN()
	{
		return pageNumber;
	}

	public List<Page> getDisk()
	{
		return disk;
	}

	public List<Page> getMemory()
	{
		return memory;
	}

	public Queue<Page> getQueue()
	{
		return queue;
	}

	public int getPageReferences()
	{
		return pageReferences;
	}

	public void restart()
	{
		pageReferences = 0;
		hits = 0;
		memory = new ArrayList<>(4);
		disk = new ArrayList<>(11);
		queue = new LinkedList<>();
	}

	//Runs the paging algorithm
	//For each page reference (of 100), print
	// <time stamp (s), proc name, page referenced, if the page is in the memory
	// which proc/page number will be evicted (if needed)>
	public void run()
	{
		Integer[] mem = new Integer[4];
		disk = getDisk();
		pages = new Page[10]; //Number of processes
		for(int i = 0; i < 10; ++i)
		{
			pages[i] = new Page(i);
			disk.add(i, pages[i]);
		}
		int pNumber;
		//While number of references is less than max references (100MB)
		int processNumber = 0;
		int time = pList.get(processNumber).getArrivalTime();
		while (pageReferences < 100)
		{
			System.out.println();
			int counter = 0;
			for (Page page: memory)
			{
				if (counter > 3)
				{
					break;
				}
				mem[counter] = page.pageNumber;
				counter++;
			}
			String s = "";
			for(int i = 0; i < mem.length; i++)
			{
				if (mem[i] != null)
				{
					s = s +  mem[i] + " ";
				}
				else
				{
					s = s + "  ";
				}
			}
			
			pNumber = nextPageReference();
			int pageRef = pageReferences + 1;
			String ref = "";
			if (pageRef < 10)
			{
				ref = ref + pageRef + ".  ";
			}
			else
			{
				ref = ref + pageRef + ". ";
			}
			System.out.print(ref + "[Time Stamp:] " + time + " [Name:] " + pList.get(processNumber).getName() + " [Memory Table:] " + s + " [Page Referenced:] " + pNumber);
			time = time + pList.get(processNumber).getServiceDuration();
			processNumber++;
			if(pages[pNumber].exists)
			{
				++hits;
				//Updates the page in the mem
				pages[pNumber].previousReferences = pageReferences;
			}
			else //Gets page and evicts
			{
				for (int i = 0; i < disk.size(); ++i)
				{
					Page p = disk.get(i);
					//If the page is found
					if (pNumber == p.pageNumber)
					{
						swap(p);
					}
				}
			}
			++pageReferences;
			if(this instanceof MFU || this instanceof LFU)
			{
				for (Page page : memory)
				{
					page.pageUsage++;
				}
			}
		}
	}


	//Swaps page into memory
	public void swap(Page p)
	{
		int evict = -1;
		p.exists = true;
		p.previousReferences = pageReferences;

		//If the memory is not full
		if(memory.size() < 4)
		{
			//Use it
			queue.add(p);
			evict = memory.size();
			memory.add(evict, p);
		}
		else //If the memory is full
		{
			//Find a page to replace the disk page
			evict = getPage();
			Page mem = memory.get(evict);
			
			//Reset mem for next run
			mem.previousReferences = -1;
			mem.exists = false;
			mem.pageUsage = 0;

			queue.remove(mem);
			System.out.print(" [Evicted:] " + mem.pageNumber);

			//Moves the memory page to disk
			for (int i = 0; i < disk.size(); ++i)
			{
				if (disk.get(i).pageNumber == p.pageNumber)
				{
					disk.set(i, mem);
				}
				memory.set(evict, p);
				queue.add(p);
			}
		}
		swapped++;
		System.out.print(" [Page Swapped:] " + p.pageNumber);
	}

	//After referencing page i, you need to find the next reference
	public int nextPageReference()
	{
		int currentPage = pageNumber;
		//Generates a random number "r" from 0 - 10
		Random ran = new Random();
		int r = ran.nextInt(10);
		//No page is referenced yet, set a random page
		if (currentPage == -1)
		{
			currentPage = r;
		}
		else
		{
			//If 0 <= r < 7, generate random change to be -1, 0, or +1
			if (r >= 0 || r < 7)
			{
				r = ran.nextInt(3) - 1;
			}
			//If 7 <= r <= 10, generate a new page reference "j"
			// 2 <= (delta) i <= 9
			// 0 <= j <= i-2 or i+2 <= j <= 10
			if (r >= 7 || r <= 10)
			{
				r = ran.nextInt(7) + 2;
			}
		}
		if(currentPage + r < 0)
		{
			currentPage = 10 - 1 + currentPage + r;
		}
		else
		{
			currentPage = (currentPage + r) % 10;
		}
		return currentPage;	
	}
}
