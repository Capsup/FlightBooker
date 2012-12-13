package MainPackage;

import javax.swing.JFrame;
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
		//int iID = Database.getInstance().Add( new Person( "Black", "Widow", "Female", "06-04-1991", "Mother Russia", "Russian", "SHIELD", "1351351345", "9285813", 123123 ) );
		// Passenger passenger = ( Passenger ) Database.getInstance().Get( iID );
		
		/*Object[] objects = Database.getInstance().Get( "Person" );
		Person[] passengers = Arrays.asList( objects ).toArray( new Person[objects.length] );*/
//		
//		ArrayList<Person> persons = Database.getInstance().Get( Person.class );
//		
//		for( int i = 0; i < persons.size(); i++ )
//        {
//	        System.out.println(persons.get( i ).getFirstName());
//        }
	}
}