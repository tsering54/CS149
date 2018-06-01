package Paging;
import java.util.Random;

//Random pick algorithm
public class RandomAlgorithm extends Page
{
	//Gets a random page to evict from the page table
	public int getPage()
	{
		Random r = new Random();
		return r.nextInt(4); //4 memory frames
	}
}
