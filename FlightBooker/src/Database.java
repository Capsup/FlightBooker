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
			// Load the driver
			Class.forName( "com.mysql.jdbc.Driver" );
			// Get instance of connection
			connection = DriverManager.getConnection( "jdbc:mysql://mysql.itu.dk/FlightBooker", "flightbooker",
			        "flightbooking" );
		} catch( ClassNotFoundException e )
		{
			System.out.println( "Driver could not be loaded. Error: " + e );
		} catch( SQLException e )
		{
			System.out.println( "Connection could not be established. Error: " + e );
		}
	}

	public ResultSet executeQuery( String sQuery )
	{
		try
		{
			if( connection == null || !connection.isValid( 1 ) )
				connectToDatabase();
			Statement statement = connection.createStatement();
			if( sQuery.contains( "UPDATE" ) )
			{
				@SuppressWarnings( "unused" )
				boolean t = true;
			}
			if( sQuery.contains( "SELECT" ) )
			{
				boolean bSuccess = statement.execute( sQuery );
				
				if( bSuccess )
					return statement.getResultSet();
			}
			else
			{
				int iRowsAffected = statement.executeUpdate( sQuery );

				if( iRowsAffected > 0 )
					return( statement.getResultSet() );
				else
				{
					System.out.println("Query executed, but no rows were affected.");
					return null;
				}
			}			
		} catch( SQLException e )
		{
			System.out.println( "Query failed to execute. Error: " + e );
		}

		return null;
	}

	public boolean AddValue( String sTable, String... args )
	{
		// String[] sVarArgs = args;
		ResultSet result = executeQuery( "SELECT * FROM " + sTable );

		if( result == null )
		{
			System.out.println( "Selection query failed." );
			return false;
		}
		
		int iColumnAmount = 0;

		try
		{
			for( int i = 1; i <= result.getMetaData().getColumnCount(); i++ )
			{
				if( !result.getMetaData().isAutoIncrement( i ) )
					iColumnAmount++;
			}
			if( args.length != ( iColumnAmount ) )
			{
				throw new InvalidParameterException( "Amount of arguments not equal to amount of columns." );
			}
		} catch( SQLException e )
		{
			System.out.println( "Database error: " + e );
			return false;
		}

		StringBuilder sValuesBuilder = new StringBuilder();
		StringBuilder sColumnsBuilder = new StringBuilder();

		for( int i = 0; i < args.length; i++ )
			sValuesBuilder.append( i == 0 ? "" : ", " ).append( "'" ).append( args[i] ).append( "'" );

		try
		{
			for( int i = 1; i <= result.getMetaData().getColumnCount(); i++ )
			{
				if( !result.getMetaData().isAutoIncrement( i ) )
					sColumnsBuilder.append( sColumnsBuilder.toString().equals( "" ) ? "" : ", " ).append( result.getMetaData().getColumnName( i ) );
			}
		} catch( SQLException e )
		{
			System.out.println( "Column data retrieval failed. Error: " + e );
		}

		result = executeQuery( "INSERT INTO " + sTable + "(" + sColumnsBuilder.toString() + ")" + " VALUES("
		        + sValuesBuilder.toString() + ")" );

		try
		{
			if( result != null && result.next() )
			{
				System.out.println( "Value successfully added." );
				return true;
			}
		} catch( SQLException e )
		{
			System.out.println( "Insertion into database failed. Error: " + e );
			return false;
		}
		return false;
	}

	
	
	public boolean UpdateValue( String sTable, int ID, String... args )
	{
		if( args.length % 2 != 0 )
		{
			throw new InvalidParameterException( "Arguments are not in pairs." );
		}
		
		StringBuilder sValuesBuilder = new StringBuilder();
		
		/*for( int i = 0; i < args.length; i+=2 )
			sValuesBuilder.append( i == 0 ? "" : " AND " ).append( args[i] ).append( "=" ).append( "'" ).append( args[i+1] ).append( "'" );*/
		
		for( int i = 0; i < args.length; i+=2 )
			sValuesBuilder.append( i == 0 ? "" : ", " ).append( args[i] ).append( "=" ).append( "'" ).append( args[i+1] ).append( "'" );
		
		ResultSet result = executeQuery( "UPDATE " + sTable + " SET " + sValuesBuilder.toString() + " WHERE " + "id='" + ID + "'" );
		
		if( result != null )
		{
			System.out.println("Successfully updated rows.");
			return true;
		}
		
		return false;
	}
	
	/*UpdateValue( "persons", "5", "name", "Jørgen", "age", "78", "reservations", "1")
	
	void UpdateValue(String tableName, String currentId, String... args)
	{
		UPDATE persons SET 'name=jørgen' WHERE id=currentId;
	}*/
	
	public boolean DeleteValue( String sTable, String... args )
	{
		if( args.length % 2 != 0 )
		{
			throw new InvalidParameterException( "Arguments are not in pairs." );
		}
		
		StringBuilder sValuesBuilder = new StringBuilder();
		
		for( int i = 0; i < args.length; i+=2 )
			sValuesBuilder.append( i == 0 ? "" : " AND " ).append( args[i] ).append( "=" ).append( "'" ).append( args[i+1] ).append( "'" );
		
		ResultSet result = executeQuery( "DELETE FROM " + sTable + " WHERE " + sValuesBuilder );
		
		if( result != null )
		{
			System.out.println("Values succesfully deleted.");
			return true;
		}
		
		return false;
	}
}
