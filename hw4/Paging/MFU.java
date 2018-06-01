package Paging;

/**
 * Most Frequently used paging algorithm
 */
public class MFU extends Page
{
	//Returns the index of the most frequently used page
	public int getPage()
	{
		int i = 0;
		int mostFrequentlyUsed = 0;
		for (Page page : getMemory())
		{
			//Find a new most frequently used
			if(mostFrequentlyUsed < page.pageUsage)
			{
				mostFrequentlyUsed = page.pageUsage;
				i++;
			}
		}
		getMemory().get(i).pageUsage = 0;
		return i;
	}
}