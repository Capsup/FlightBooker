package MainPackage;

import java.awt.List;
import java.util.ArrayList;

import MainPackage.Plane.PlaneType;

public class Airport
{
	public enum AirportType
	{
		COPENHAGEN, STOCKHOLM, MALM�, AALBORG, BERLIN, NEWYORK, TOKYO, R�NNE;
	}
	
	private String airportName;
	
	public Airport(AirportType airport)
	{
		switch(airport)
		{
			case COPENHAGEN:
				airportName = "Copenhagen";
				break;
			case STOCKHOLM:
				airportName = "Stockholm";
				break;
			case MALM�:
				airportName = "Malm�";
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
			case R�NNE:
				airportName = "R�nne";
				break;
				
		}
	}
	
	public String getName()
	{
		return airportName;
	}
	
	public static String[] getDestinations()
	{
		ArrayList<String> stringArray = new ArrayList<String>();
		
		Airport currentAirport;
		
		for(AirportType airportType : AirportType.values()) 
		{
			currentAirport = new Airport(airportType);

			stringArray.add(currentAirport.getName());
		}
		
		String[] returnArray = stringArray.toArray(new String[0]);
				
		return returnArray;
	}
}
