package MainPackage;

import java.io.Serializable;

public class Passenger implements Serializable
{
    private Person person;
	private Seat seat;
	
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
