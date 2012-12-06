package MainPackage;
import java.util.Date;

public class Flight 
{
	private Date flyDate;
	private Plane plane;
	
	private Seat[][] seatArray;
	
	public Flight(Date flyDate, Plane plane)
	{
		this.flyDate = flyDate;
		
		this.plane = plane;
		
		this.seatArray = plane.getSeatArray();
	}
	
	public boolean checkSeatReservation(int x, int y)
	{
		return seatArray[x][y].isBooked();
	}
	
	public void changePlane(Plane newPlane)
	{
		if(newPlane.getSeatAmount() >= plane.getSeatAmount())
			plane = newPlane;
		else
			System.out.println("The new plane needs to be of same size or bigger");
	}
	
	public Seat getSeat(int x, int y)
	{
		if(seatArray[x][y] != null)
		{
			return seatArray[x][y];
		}
		else 
		{
			System.out.println("Seat does not exist");
			
			return null;
		}
		
	}
	
	public Date getDate()
	{
		return flyDate;
	}
	
	public Plane getPlane()
	{
		return plane;
	}
	
	
}
