package MainPackage;

import java.awt.Dimension;
import java.io.Serializable;

/**
 * 
 * This class represents our datatype for the Seats in our system.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * @version 1.0
 * 
 */
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

	/**
	 * Constructor for Seat that sets its position to the values specified.
	 * 
	 * @param x
	 *            - an Integer that specifies this seat's x position.
	 * @param y
	 *            - an Integer that specifies this seat's y position.
	 */
	public Seat( int x, int y )
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Accessor function for the booked field of this seat.
	 * 
	 * @return the booked Boolean of the Seat.
	 */
	public boolean isBooked()
	{
		return booked;
	}

	/**
	 * Mutator function for the booked field of this seat.
	 * 
	 * @param value
	 *            - a boolean that sets the booked field of this seat.
	 */
	public void changeBookingStatus( boolean value )
	{
		booked = value;
	}

	/**
	 * Accessor function to get the position of this seat.
	 * 
	 * @return the position of this seat as a Dimension.
	 */
	public Dimension getPosition()
	{
		return new Dimension( x, y );
	}
}
