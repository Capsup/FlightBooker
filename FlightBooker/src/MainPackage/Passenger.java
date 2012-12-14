package MainPackage;

import java.io.Serializable;

public class Passenger implements Serializable, Uploadable
{
    private Person person;
	private Seat seat;
	
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
	
	public Passenger(Person person, Seat seat)
	{
		this.person = person;
		this.seat = seat;
	}
	
	public Seat getSeat()
	{
		if(seat != null)
		{
			return seat;
		}
		else 
		{
			return null;
		
		}
	}
	
	public void setSeat(Seat seat)
	{
		this.seat = seat;
	}
	
	public Person getPerson()
	{
		return person;
	}
}
