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
	}
}