package MainPackage;

public class Plane 
{
	public enum PlaneType 
	{
		BOEING747, BOEING737, BOEING1337;
	}
	
	private int planeID;
	private Seat[][] seatArray;
	private String planeTypeString;
	
	public Plane(PlaneType planeType)
	{
		switch(planeType)
		{
			case BOEING747:
				planeTypeString = "Boeing 747";
				seatArray = new Seat[5][20];
				break;
			case BOEING1337:
				planeTypeString = "Boeing 1337";
				seatArray = new Seat[4][16];
				break;
			case BOEING737:
				planeTypeString = "Boeing 737";
				seatArray = new Seat[3][12];
				break;
		}
		
		for(int i=0; i<seatArray.length; i++)
		{
			for(int j=0; j<seatArray[i].length; j++)
			{
				seatArray[i][j] = new Seat(i, j);
			}
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
	
	public String getPlaneTypeString()
	{
		return planeTypeString;
	}
}
