
public class Seat 
{
	private int x;
	private int y;
	
	private boolean booked;
	
	public Seat(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean isBooked()
	{
		return booked;
	}
}
