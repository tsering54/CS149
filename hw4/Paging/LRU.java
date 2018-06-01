package Paging;

/**
 * Least Recently Used Algorithm
 */
public class LRU extends Page
{
	public int getPage()
	{
		int first = -1;
		int count = 0;
		int i = count;
		for (Page page: getMemory())
		{
			
			if(first == -1 || page.previousReferences < first)
			{
				i = count;
				first = page.previousReferences;
			}
			count++;
		}
		return i;
	}
}
