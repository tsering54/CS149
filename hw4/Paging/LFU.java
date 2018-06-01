package Paging;

/**
 * Least Frequently Used Algorithm
 *
 */
public class LFU extends Page
{
	public int getPage()
	{
		int counter = 0;
		int i = counter;
		int least = pageUsage;
		for (Page page : getMemory())
		{
			if (least > page.pageUsage)
			{
				least = page.pageUsage;
				i = counter;
			}
			counter++;
		}
		getMemory().get(i).pageUsage = 0;
		return i;
	}
}
