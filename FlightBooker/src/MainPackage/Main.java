package MainPackage;

import java.util.ArrayList;
import java.util.Arrays;

public class Main
{
	/**
	 * @param args
	 */

	public static int SIZE_X = 800;
	public static int SIZE_Y = 600;

	public static void main( String[] args )
	{
		new MainMenu();
		//Database.getInstance().Add( new Passenger(new Person("Osama bin", "Laden", "male",
		//		"03-21-1967", "Afghanistan", "Afghan", "N/A", "N/A","N/A", 3), new Seat(0, 0)) );
		/*Passenger passenger = ( Passenger ) Database.getInstance().Get( iID );
		Person person = Database.getInstance().Get( iID, Person.class );*/
		/*Flight[] displayedFlights = new Flight[5];
		
		displayedFlights[0] = new Flight(Calendar.getInstance(), new Plane(PlaneType.BOEING747), new Airport(AirportType.COPENHAGEN),  new Airport(AirportType.MALMÖ));
		displayedFlights[1] = new Flight(Calendar.getInstance(), new Plane(PlaneType.BOEING747), new Airport(AirportType.STOCKHOLM),  new Airport(AirportType.COPENHAGEN));
		displayedFlights[2] = new Flight(Calendar.getInstance(), new Plane(PlaneType.BOEING747), new Airport(AirportType.RØNNE),  new Airport(AirportType.COPENHAGEN));
		displayedFlights[3] = new Flight(Calendar.getInstance(), new Plane(PlaneType.BOEING747), new Airport(AirportType.COPENHAGEN),  new Airport(AirportType.RØNNE));
		displayedFlights[4] = new Flight(Calendar.getInstance(), new Plane(PlaneType.BOEING747), new Airport(AirportType.RØNNE),  new Airport(AirportType.STOCKHOLM));
		
		for( int i = 0; i < displayedFlights.length; i++ )
        {
	        Database.getInstance().Add( displayedFlights[i] );
        }*/
	}
}
