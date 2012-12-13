package MainPackage;

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
		int iID = Database.getInstance().Add( new Person( "Black", "Widow", "Female", "06-04-1991", "Mother Russia", "Russian", "SHIELD", "1351351345", "9285813", 123123 ) );
		// Passenger passenger = ( Passenger ) Database.getInstance().Get( iID );
		
		Passenger[] passengers = ( Passenger[] ) Database.getInstance().Get( "Passenger" );
		
		for( int i = 0; i < passengers.length; i++ )
        {
	        System.out.println(passengers[i]);
        }
	}
}