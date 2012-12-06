package MainPackage;

import java.awt.Dimension;
 
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
	
	public void changeBookingStatus(boolean value)
	{
		booked = value;
	}
	
	public Dimension getPosition()
	{
		return new Dimension(x, y);
	}
}
