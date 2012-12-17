package MainPackage;

import java.io.Serializable;

/**
 * 
 * This class represents our Plane datatype for the system.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * @version 1.0
 * 
 */
public class Plane implements Serializable, Uploadable
{
	// A simple enum of the different PlaneTypes that we have in our system.
	public enum PlaneType
	{
		BOEING747, BOEING737, BOEING1337;
	}

	private int planeID;
	private Seat[][] seatArray;
	private String planeTypeString;

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
	 * Constructor for a plane. Initialises the plane's fields to properly match that of the specified flight type.
	 * 
	 * @param planeType
	 *            - an Planetype specifying which kind of PlaneType this plane is.
	 */
	public Plane( PlaneType planeType )
	{
		switch( planeType )
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

		for( int i = 0; i < seatArray.length; i++ )
		{
			for( int j = 0; j < seatArray[i].length; j++ )
			{
				seatArray[i][j] = new Seat( i, j );
			}
		}
	}

	/**
	 * Accessor function for the seatArray of this plane.
	 * 
	 * @return the seatArray of the plane.
	 */
	public Seat[][] getSeatArray()
	{
		return seatArray;
	}

	/**
	 * Accessor function for the amount of seats in the seatArray.
	 * 
	 * @return an Integer representing the amount of seats in the plane's seatArray.
	 */
	public int getSeatAmount()
	{
		int count = 0;

		for( int i = 0; i < seatArray.length; i++ )
		{
			count += seatArray[i].length;
		}

		return count;
	}

	/**
	 * Static accessor function for the Enum PlaneType.
	 * 
	 * @return an PlaneType array containign the values of the PlaneType enum.
	 */
	public static PlaneType[] planeTypes()
	{
		return PlaneType.values();
	}

	/**
	 * Accessor function for the plane's type string.
	 * 
	 * @return a String with the plane's planeTypeString.
	 */
	public String getPlaneTypeString()
	{
		return planeTypeString;
	}
}
