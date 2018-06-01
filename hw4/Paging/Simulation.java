package Paging;
import java.util.*;

public class Simulation 
{
	public double hitRatioTot;
	public Page page;
	private String name = "";

	public Simulation(Page p, String n)
	{
		page = p;
		hitRatioTot = 0;
		name = n;
	}

	public String getName()
	{
		return name;
	}

	public double getHitRatio()
	{
		return hitRatioTot;
	}

	public void addProcess(ArrayList<Process> p)
	{
		page.setProc(p);
	}

}
