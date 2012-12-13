package MainPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import MainPackage.Airport.AirportType;
import MainPackage.Plane.PlaneType;

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
//		Database.getInstance().Add( new Passenger(new Person("Jesper", "Nysteen", "male",
//				"06-04-1991", "Denmark", "Danish", "Skaffervej 15, 3 tv", "31225525","3443542624654", 5), new Seat(0, 0)) );
		/*Passenger passenger = ( Passenger ) Database.getInstance().Get( iID );
		Person person = Database.getInstance().Get( iID, Person.class );*/
	}
}
