package MainPackage;

import java.awt.Dimension;
import java.io.Serializable;
 
public class Seat implements Serializable, Uploadable
{
	private int x;
	private int y;
	
	private boolean booked;
	
	private int id;
	
	@Override
    public int getID()
    {
	    return id;
    }

	@Override
    public void setID( int iID )
    {
	    id = iID;
    }
	
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
