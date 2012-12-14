package MainPackage;

import java.awt.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.rmi.activation.Activator;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;

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
			connection = DriverManager.getConnection( "jdbc:mysql://mysql.itu.dk/FlightBooker", "flightbooker", "flightbooking" );

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

			// DEBUG:
			/*
			 * if( sQuery.contains( "UPDATE" ) ) {
			 * 
			 * @SuppressWarnings( "unused" ) boolean t = true; }
			 */

			if( sQuery.contains( "SELECT" ) )
			{
				boolean bSuccess = statement.execute( sQuery );

				if( bSuccess )
					return statement.getResultSet();
			} else
			{
				int iRowsAffected = statement.executeUpdate( sQuery );

				if( iRowsAffected > 0 )
					return( statement.getResultSet() );
				else
				{
					System.out.println( "Query executed, but no rows were affected." );
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

		result = executeQuery( "INSERT INTO " + sTable + "(" + sColumnsBuilder.toString() + ")" + " VALUES(" + sValuesBuilder.toString() + ")" );

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

		for( int i = 0; i < args.length; i += 2 )
			sValuesBuilder.append( i == 0 ? "" : ", " ).append( args[i] ).append( "=" ).append( "'" ).append( args[i + 1] ).append( "'" );

		ResultSet result = executeQuery( "UPDATE " + sTable + " SET " + sValuesBuilder.toString() + " WHERE " + "id='" + ID + "'" );

		if( result != null )
		{
			System.out.println( "Successfully updated rows." );
			return true;
		}

		return false;
	}

	public boolean DeleteValue( String sTable, String... args )
	{
		if( args.length % 2 != 0 )
		{
			throw new InvalidParameterException( "Arguments are not in pairs." );
		}

		String sValue = "";

		for( int i = 0; i < args.length; i += 2 )
			sValue = ( i == 0 ? "" : " AND " ) + args[i] + "=" + "'" + args[i + 1] + "'";

		ResultSet result = executeQuery( "DELETE FROM " + sTable + " WHERE " + sValue );

		if( result != null )
		{
			System.out.println( "Values succesfully deleted." );
			return true;
		}

		return false;
	}

	// /////////AMAZING CODE BELOW\\\\\\\\\\\
	public int Add( Uploadable uploadable )
	{
		try
		{
			if( connection == null || !connection.isValid( 1 ) )
				connectToDatabase();

			//PreparedStatement statement = connection.prepareStatement( "INSERT INTO testtable(object) VALUES(?)", Statement.RETURN_GENERATED_KEYS );
			PreparedStatement statement = connection.prepareStatement( "INSERT INTO " + uploadable.getClass().getSimpleName().toLowerCase() + "(id, object) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS );
			
			//if( object instanceof Person )
			//	statement.setLong( 1, ((Person) object).getCustomerID() );
			statement.setLong( 1, uploadable.getID() );
			statement.setObject( 2, uploadable );
			statement.executeUpdate();

			ResultSet rSet = statement.getGeneratedKeys();

			int id = 0;
			if( rSet.next() )
				id = rSet.getInt( 1 );
			
			//statement.setObject( 1, x )

			statement.close();

			return id;
		} 
		catch( SQLException e )
		{
			e.printStackTrace();
		}

		return 0;
	}
	
	public boolean Replace( int iID, Object object )
	{
		try
        {
			if( connection == null || !connection.isValid( 1 ) )
				connectToDatabase();
			
			PreparedStatement statement = connection.prepareStatement( "UPDATE " + object.getClass().getSimpleName().toLowerCase() + " SET object = ? WHERE id = ?" );
			statement.setObject( 1, object );
			statement.setLong( 2, iID );
		
			int iRowsAffected = statement.executeUpdate();
			if( iRowsAffected > 1 )
			{
				System.out.println("Query executed, however more than one row was updated?");
				return true;
			}
			else if(iRowsAffected == 1) 
			{
				return true;
			}
			else 
			{
				System.out.println("Query executed but no values were updated.");
				return false;
			}
        } 
		catch( Exception e )
        {
			e.printStackTrace();
        }
		
		return false;
	}

	public Object Get( int iID )
	{
		try
		{
			if( connection == null || !connection.isValid( 1 ) )
				connectToDatabase();

			PreparedStatement statement = connection.prepareStatement( "SELECT object FROM testtable WHERE id = ?" );
			statement.setLong( 1, iID );

			ResultSet rSet = statement.executeQuery();
			Object object = null;

			if( rSet.next() )
				object = new ObjectInputStream( rSet.getBlob( "object" ).getBinaryStream() ).readObject();

			statement.close();

			return object;
		} 
		catch( SQLException | ClassNotFoundException | IOException e )
		{
			e.printStackTrace();
		}

		return null;
	}
	
	public <T extends Object> T Get( int iID, Class<T> type )
	{
		try
		{
			if( connection == null || !connection.isValid( 1 ) )
				connectToDatabase();

			PreparedStatement statement = connection.prepareStatement( "SELECT object FROM " + type.getSimpleName().toLowerCase() + " WHERE id = ?" );
			statement.setLong( 1, iID );

			ResultSet rSet = statement.executeQuery();
			Object object = null;

			if( rSet.next() )
				object = new ObjectInputStream( rSet.getBlob( "object" ).getBinaryStream() ).readObject();

			statement.close();

			return type.cast( object );
		} 
		catch( SQLException | ClassNotFoundException | IOException e )
		{
			e.printStackTrace();
		}

		return null;
	}
	
	public <T extends Object> ArrayList<T> Get( Class<T> type )
	{
		try
		{
			if( connection == null || !connection.isValid( 1 ) )
				connectToDatabase();

			PreparedStatement statement = connection.prepareStatement( "SELECT object FROM " + type.getSimpleName().toLowerCase() );

			ResultSet rSet = statement.executeQuery();
			
			ArrayList<T> object = new ArrayList<>();
			
			while( rSet.next() )
			{
				object.add( type.cast( new ObjectInputStream( rSet.getBlob( "object" ).getBinaryStream() ).readObject() ) );
			}

			statement.close();

			return object;
		} 
		catch( SQLException | ClassNotFoundException | IOException e )
		{
			e.printStackTrace();
		}

		return null;
	}
	
	public int GetID( Class<?> type )
	{
		try
        {
	        if( connection == null || !connection.isValid( 1 ) )
	        	connectToDatabase();
	        
	        ResultSet rSet = executeQuery( "SELECT id FROM " + type.getSimpleName().toLowerCase() );
	        
	        int iRowCount = 0;
	        while( rSet.next() )
	        	iRowCount++;
	        
	        return iRowCount+1;
        } 
		catch( Exception e )
        {
	        e.printStackTrace();
        }
		
		return -1;
	}
	// //////////AMAZING CODE ABOVE\\\\\\\\\\\\
}
