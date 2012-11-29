package MainPackage;

public class Plane 
{
	public enum PlaneType 
	{
		BOEING747, BOEING737;
	}
	
	private int planeID;
	private Seat[][] seatArray;
	
	public Plane(PlaneType planeType)
	{
		switch(planeType)
		{
			case BOEING747:
				seatArray = new Seat[6][10];
				break;
			case BOEING737:
				seatArray = new Seat[4][8];
				break;
		}
	}
	
	public Seat[][] getSeatArray()
	{
		return seatArray;
	}
	
	public int getSeatAmount()
	{
		int count = 0;
		
		for(int i=0; i<seatArray.length; i++)
		{
			count += seatArray[i].length;
		}
		
		return count;
	}
	
}
