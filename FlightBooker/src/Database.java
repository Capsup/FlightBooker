import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;


public class Database
{
	private static Database instance;
	private Connection connection;
	
	private Database()
	{
		instance = this;
	}
	
	public static Database getInstance()
	{
		if( instance != null )
			return( instance );
		return( new Database() );
	}
	
	private void connectToDatabase()
	{
		try
        {
			//Load the driver
	        Class.forName("com.mysql.jdbc.Driver");
	        //Get instance of connection
	        connection = DriverManager.getConnection( "jdbc:mysql://mysql.itu.dk/FlightBooker", "flightbooker", "flightbooking" );
        } 
		catch( ClassNotFoundException e )
        {
	        System.out.println( "Driver could not be loaded. Error: " + e );
        }
		catch( SQLException e )
		{
			System.out.println( "Connection could not be established. Error: " + e );
		}
	}
	
	public void executeQuery( String sQuery )
	{
		
	}
}
