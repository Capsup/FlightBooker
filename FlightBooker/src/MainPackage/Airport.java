package MainPackage;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * This class represents our datatype for the Airports in our system.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * @version 1.0
 * 
 */
public class Airport implements Serializable, Uploadable
{

	// A simple enum of the airports that we have in the system currently.
	public enum AirportType
	{
		COPENHAGEN, STOCKHOLM, MALMÖ, AALBORG, BERLIN, NEWYORK, TOKYO, RØNNE;
	}

	private String airportName;

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
	 * Constructor for Airport.
	 * 
	 * @param airport
	 *            - the type for the Airport as specified in the Enum.
	 */
	public Airport( AirportType airport )
	{
		switch( airport )
		{
			case COPENHAGEN:
				airportName = "Copenhagen";
			break;
			case STOCKHOLM:
				airportName = "Stockholm";
			break;
			case MALMÖ:
				airportName = "Malmö";
			break;
			case AALBORG:
				airportName = "Aalborg";
			break;
			case BERLIN:
				airportName = "Berlin";
			break;
			case NEWYORK:
				airportName = "New York";
			break;
			case TOKYO:
				airportName = "Tokyo";
			break;
			case RØNNE:
				airportName = "Rønne";
			break;
		}
	}

	/**
	 * Accessor function for the name field.
	 * 
	 * @return the name field of this object.
	 */
	public String getName()
	{
		return airportName;
	}

	/**
	 * Static accessor function for the Enum AirportType.
	 * 
	 * @return an array consisting of the values of the Enum.
	 */
	public static AirportType[] getAirportTypes()
	{
		return AirportType.values();
	}

	/**
	 * Static accessor function that returns the name of all airports as a string.
	 * 
	 * @return String array of airport names.
	 */
	public static String[] getDestinations()
	{
		ArrayList<String> stringArray = new ArrayList<String>();

		Airport currentAirport;

		for( AirportType airportType : AirportType.values() )
		{
			currentAirport = new Airport( airportType );

			stringArray.add( currentAirport.getName() );
		}

		String[] returnArray = stringArray.toArray( new String[0] );

		return returnArray;
	}
}
