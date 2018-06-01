package Paging;

public class FIFO extends Page
{
	//Gets the index of the first page placed into the memory
	public int getPage()
	{
		int process = getQueue().poll().getPN();
		int index = 0;
		for (Page p : getMemory())
		{
			if(p.getPN() == process)
			{
				return index;
			}
			++index;
		}
		return 0;
	}
}
