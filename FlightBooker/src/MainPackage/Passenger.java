package MainPackage;

import java.io.Serializable;

/**
 * This class represents our datatype for the Passengers in our system.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 *
 */
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
	
	/**
	 * Creates a new Passenger
	 * @param person The Person of the Passenger
	 * @param seat The Seat of the Passenger
	 */
	public Passenger(Person person, Seat seat)
	{
		this.person = person;
		this.seat = seat;
	}
	
	/**
	 * Gets the Passenger's seat
	 * @return the Seat of the Passenger
	 */
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
	
	/**
	 * Sets the seat of the Passenger
	 * @param seat The Seat of the Passenger
	 */
	public void setSeat(Seat seat)
	{
		this.seat = seat;
	}
	
	/**
	 * Gets the Person of the Passenger
	 * @return the Person of the Passenger
	 */
	public Person getPerson()
	{
		return person;
	}
	
	/**
	 * Sets the Person of the Passenger
	 * @param person the Person of the Passenger
	 */
	public void setPerson( Person person )
	{
		this.person = person;
	}
}
