import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;


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
	
	public ResultSet executeQuery( String sQuery )
	{
		try
		{
    		if( !connection.isValid( 1 ))
    			connectToDatabase();
    		Statement statement = connection.createStatement();
    		boolean bSuccess = statement.execute( sQuery );
    		
    		if( bSuccess )
    			return( statement.getResultSet() );
		}
		catch( SQLException e )
		{
			System.out.println( "Query failed to execute. Error: " + e );
		}
		return null;
	}
	
	public boolean AddValue( String sTable, String... args )
	{
		//String[] sVarArgs = args;
		ResultSet result = executeQuery( "SELECT * FROM " + sTable );
		
		if( result == null )
		{
			System.out.println( "Selection query failed." );
			return false;
		}
		
		try
        {
	        if( args.length != result.getMetaData().getColumnCount() )
	        {
	        	throw new InvalidParameterException( "Amount of arguments not equal to amount of columns." );
	        }
        } 
		catch( SQLException e )
        {
	        System.out.println( "Database error: " + e );
	        return false;
        }
		
		StringBuilder sStr = new StringBuilder();
		
		for( int i = 0; i < args.length; i++ )
			sStr.append( i == 0 ? "" : ", " ).append( "'" ).append( args[i] ).append("'");
		
		result = executeQuery( "INSERT INTO " + sTable + " VALUES(" + sStr.toString() + ")" );
		
		try
        {
	        if( result.next() )
	        {
	        	return true;
	        }
        } 
		catch( SQLException e )
        {
	        System.out.println( "Insertion into database failed. Error: " + e);
	        return false;
        }
		return false;
	}
	
	public void UpdateValue( String sTable, String... args )
	{
		
	}
	
	
}
