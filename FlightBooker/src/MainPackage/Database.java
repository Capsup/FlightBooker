package MainPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * 
 * The Database class allows for a simple interface between the program and the database. It allows communication between the program and the database
 * in a uniform way.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * @version 1.0
 * 
 */

public class Database
{
	private static Database instance;
	private Connection connection;

	/**
	 * This class is a singleton therefore the constructor is private.
	 */
	private Database()
	{
		instance = this;
	}

	/**
	 * Gets a reference to the current instance of the Database.
	 * 
	 * @return a Database instance
	 */
	public static Database getInstance()
	{
		if( instance != null )
			return( instance );

		return( new Database() );
	}

	/**
	 * Makes the connection object contain an actual connection to the Database server using the JDBC MySQL driver. Should not be called outside the
	 * class as all Database functions validates this on their own, therefore it is private.
	 */
	private void connectToDatabase()
	{
		try
		{
			// Load the driver
			Class.forName( "com.mysql.jdbc.Driver" );

			// Get instance of connection
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

	/**
	 * Executes the given query on the MySQL database that has been connected to. The function expects the input to be a valid query, otherwise an
	 * exception is thrown.
	 * 
	 * @param sQuery
	 *            - the query to actually execute as a string
	 * @return a ResultSet containing the results of the query that was executed
	 */
	public ResultSet executeQuery( String sQuery )
	{
		try
		{
			// If connection isn't valid, we better get a valid one!
			if( connection == null || !connection.isValid( 1 ) )
				connectToDatabase();

			Statement statement = connection.createStatement();

			// If this query is a SELECT operation, use the normal execute function on the statement.
			if( sQuery.contains( "SELECT" ) )
			{
				boolean bSuccess = statement.execute( sQuery );

				if( bSuccess )
					return statement.getResultSet();
			}
			// Otherwise, use the executeUpdate which returns the number of affected rows. This allows us to tell the user whether or not there was
			// actually any rows affected by our executed query.
			else
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
		}
		catch( SQLException e )
		{
			System.out.println( "Query failed to execute. Error: " + e );
		}

		return null;
	}

	/**
	 * Adds a row of data to the given table using the values in the Vararg array.
	 * 
	 * @param sTable
	 *            - a string containing the name of the table to perform the operation on.
	 * @param args
	 *            - a Vararg array containing all the values to add. The length must correspond to the amount of columns in the specified table.
	 * @return a Boolean indicating whether or not the operation succeeded.
	 */
	public boolean AddValue( String sTable, String... args )
	{
		// Execute a select query to make sure the given table actually exists.
		ResultSet result = executeQuery( "SELECT COUNT(*) FROM " + sTable );

		// If not, tell our user the query failed and hope he enters the proper table name next time!
		if( result == null )
		{
			System.out.println( "Selection query failed." );
			return false;
		}

		int iColumnAmount = 0;

		// We want to know the amount of columns so we can make sure the user has put enough variables into the Vararg array.
		try
		{
			for( int i = 1; i <= result.getMetaData().getColumnCount(); i++ )
			{
				// If the column is one that is auto incremented, we don't want to count it as a column that needs data inputted.
				if( !result.getMetaData().isAutoIncrement( i ) )
					iColumnAmount++;
			}
			// Better check if the user has put in the correct amount of arguments.
			if( args.length != ( iColumnAmount ) )
			{
				// We can now safely assume that the user is a monkey totally oblivious to how many rows his SQL table consists of. Better throw some
				// exceptions at him.
				throw new InvalidParameterException( "Amount of arguments not equal to amount of columns." );
			}
		}
		catch( SQLException e )
		{
			System.out.println( "Database error: " + e );
			return false;
		}

		// Might as well use StringBuilders ourself as that is what Java does behind the scenes when using the concatenation operator on strings.
		// Micro optimisation for the win.
		StringBuilder sValuesBuilder = new StringBuilder();
		StringBuilder sColumnsBuilder = new StringBuilder();

		// Comma separate every single value into the string.
		for( int i = 0; i < args.length; i++ )
			sValuesBuilder.append( i == 0 ? "" : ", " ).append( "'" ).append( args[i] ).append( "'" );

		try
		{
			// Put each of the column names into the query aswell so the values end up in the right columns.
			for( int i = 1; i <= result.getMetaData().getColumnCount(); i++ )
			{
				if( !result.getMetaData().isAutoIncrement( i ) )
					sColumnsBuilder.append( sColumnsBuilder.toString().equals( "" ) ? "" : ", " ).append( result.getMetaData().getColumnName( i ) );
			}
		}
		catch( SQLException e )
		{
			System.out.println( "Column data retrieval failed. Error: " + e );
		}

		// Execute that query!
		result = executeQuery( "INSERT INTO " + sTable + "(" + sColumnsBuilder.toString() + ")" + " VALUES(" + sValuesBuilder.toString() + ")" );

		try
		{
			if( result != null && result.next() )
			{
				// Ohhh yeah, query executed successfully.
				System.out.println( "Value successfully added." );
				return true;
			}
		}
		catch( SQLException e )
		{
			// Something went wrong with the query. Let the user know and return false aswell.
			System.out.println( "Insertion into database failed. Error: " + e );
			return false;
		}
		return false;
	}

	/**
	 * Updates the values in the columns to the provided values.
	 * 
	 * @param sTable
	 *            - a String containing the name of the table to perform the operation on.
	 * @param ID
	 *            - an integer specifying exactly which row in the table to perform this operation on.
	 * @param args
	 *            - a Vararg array containing the names of the columns and also which value to add. This functions assumes that the Vararg array uses
	 *            the following format: "ColumnName, ValueToSet, ColumnName, ValueToSet, ColumnName ..."
	 * @return a Boolean indicating whether or not the operation succeeded.
	 */
	public boolean UpdateValue( String sTable, int ID, String... args )
	{
		// The Vararg array must be of the proper format. The column name first and the value to put in there next.
		if( args.length % 2 != 0 )
		{
			throw new InvalidParameterException( "Arguments are not in pairs." );
		}

		StringBuilder sValuesBuilder = new StringBuilder();

		// Add each column name and the value to put in here to the string.
		for( int i = 0; i < args.length; i += 2 )
			sValuesBuilder.append( i == 0 ? "" : ", " ).append( args[i] ).append( "=" ).append( "'" ).append( args[i + 1] ).append( "'" );

		ResultSet result = executeQuery( "UPDATE " + sTable + " SET " + sValuesBuilder.toString() + " WHERE id='" + ID + "'" );

		if( result != null )
		{
			System.out.println( "Successfully updated rows." );
			return true;
		}

		return false;
	}

	/**
	 * Deletes all values in the specified table where the values in Vararg array matches.
	 * 
	 * @param sTable
	 *            - a String containing the name of the table to perform the operation on.
	 * @param args
	 *            - a Vararg array containing the names of the columns and also what value this field should equal to. This functions assumes that the
	 *            Vararg array uses the following format: "ColumnName, ValueToEqual, ColumnName, ValueToEqual, ColumnName ..."
	 * @return a Boolean indicating whether or not the operation succeeded.
	 */
	public boolean DeleteValue( String sTable, String... args )
	{
		if( args.length % 2 != 0 )
		{
			throw new InvalidParameterException( "Arguments are not in pairs." );
		}

		StringBuilder sValuesBuilder = new StringBuilder();

		for( int i = 0; i < args.length; i += 2 )
			sValuesBuilder.append( i == 0 ? "" : " AND " ).append( args[i] ).append( "='" ).append( args[i + 1] ).append( "'" );

		// Same happens as in Update, except we delete the rows where this matches instead of updating them.
		ResultSet result = executeQuery( "DELETE FROM " + sTable + " WHERE " + sValuesBuilder.toString() );

		if( result != null )
		{
			System.out.println( "Values succesfully deleted." );
			return true;
		}

		return false;
	}

	/**
	 * Uploads the given object to the database. The Object is serialized using Java's builtin serializing system and is placed into it's respective
	 * table on the database.
	 * 
	 * @param uploadable
	 *            - the object implementing the Uploadable interface to upload to the database
	 * @return the generated id(s) of the operation
	 */
	public int Add( Uploadable uploadable )
	{
		try
		{
			// If connection isn't valid, we better get a valid one!
			if( connection == null || !connection.isValid( 1 ) )
				connectToDatabase();

			// Prepare the statement so that we can put in data at the correct locations.
			PreparedStatement statement = connection.prepareStatement( "INSERT INTO " + uploadable.getClass().getSimpleName().toLowerCase()
			        + "(id, object) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS );

			// Set the id of the statement to the object's ID.
			statement.setLong( 1, uploadable.getID() );
			// Set the actual object data to the object that we have been parsed. Java automatically serializes the data from here on.
			statement.setObject( 2, uploadable );
			// Execute that query!
			statement.executeUpdate();

			// Get the generated keys. We're interested in returning the ID that was generated for this object. Someone might be interested in this
			// information.
			ResultSet rSet = statement.getGeneratedKeys();

			int id = 0;
			if( rSet.next() )
				id = rSet.getInt( 1 );

			// Close the statement and release its resources.
			statement.close();

			return id;
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * Replaces the serialized object in the data with the provided object.
	 * 
	 * @param iID
	 *            - the ID of the object to be replaced.
	 * @param object
	 *            - the object that will be put into the database instead.
	 * @return a Boolean indicating whether or not the operation succeeded.
	 */
	public boolean Replace( int iID, Object object )
	{
		try
		{
			if( connection == null || !connection.isValid( 1 ) )
				connectToDatabase();

			PreparedStatement statement = connection.prepareStatement( "UPDATE " + object.getClass().getSimpleName().toLowerCase()
			        + " SET object = ? WHERE id = ?" );
			statement.setObject( 1, object );
			statement.setLong( 2, iID );

			int iRowsAffected = statement.executeUpdate();
			// Check how many rows were affected. If more than one was affected it means that there is more than one object in the database with the
			// same ID..? Which is weird!
			if( iRowsAffected > 1 )
			{
				System.out.println( "Query executed, however more than one row was updated?" );
				return true;
			}
			else if( iRowsAffected == 1 )
			{
				return true;
			}
			else
			{
				System.out.println( "Query executed but no values were updated." );
				return false;
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return false;
	}

	@Deprecated
	/**
	 * Returns the object that is in the testtable with the given ID.
	 * 
	 * @param iID
	 *            - the ID of the object to return.
	 * @return the unserialized object with the specified ID.
	 */
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

			// If the query returned a object, deserialize that shit!
			if( rSet.next() )
				object = new ObjectInputStream( rSet.getBlob( "object" ).getBinaryStream() ).readObject();

			statement.close();

			// And return it.
			return object;
		}
		catch( SQLException | ClassNotFoundException | IOException e )
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Returns the object from the database with the given ID of the given type.
	 * 
	 * @param iID
	 *            - the ID of the object to return.
	 * @param type
	 *            - the class of the object that is to be returned.
	 * @return the object found with the ID casted to the proper type.
	 */
	public <T extends Object> T Get( int iID, Class<T> type )
	{
		try
		{
			if( connection == null || !connection.isValid( 1 ) )
				connectToDatabase();

			// Select the object from the given type's table where the ID is equal to the given ID.
			PreparedStatement statement = connection.prepareStatement( "SELECT object FROM " + type.getSimpleName().toLowerCase() + " WHERE id = ?" );
			// Slap that ID into the statement!
			statement.setLong( 1, iID );

			// Execute the query.
			ResultSet rSet = statement.executeQuery();
			Object object = null;

			// If we get any results back, deserialize the object that was returned.
			if( rSet.next() )
				object = new ObjectInputStream( rSet.getBlob( "object" ).getBinaryStream() ).readObject();

			statement.close();

			// And return it.
			return type.cast( object );
		}
		catch( SQLException | ClassNotFoundException | IOException e )
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Returns all objects from the database with the given class type.
	 * 
	 * @param type
	 *            - the class of the object to be returned.
	 * @return an ArrayList filled with all objects that exists in the database with the given type.
	 */
	public <T extends Object> ArrayList<T> Get( Class<T> type )
	{
		try
		{
			if( connection == null || !connection.isValid( 1 ) )
				connectToDatabase();

			PreparedStatement statement = connection.prepareStatement( "SELECT object FROM " + type.getSimpleName().toLowerCase() );

			ResultSet rSet = statement.executeQuery();

			ArrayList<T> object = new ArrayList<>();

			// While there is objects left in the ResultSet, add them to the ArrayList.
			while( rSet.next() )
			{
				// Deserialize the object that was returned from the database and cast it to the type that was put into the function.
				object.add( type.cast( new ObjectInputStream( rSet.getBlob( "object" ).getBinaryStream() ).readObject() ) );
			}

			statement.close();

			// Return the ArrayList!
			return object;
		}
		catch( SQLException | ClassNotFoundException | IOException e )
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Calculates the ID of the next object in table of the database.
	 * 
	 * @param type
	 *            - the class of the object to be returned.
	 * @return the ID of the next object in the table of the database.
	 */
	public int GetID( Class<?> type )
	{
		try
		{
			if( connection == null || !connection.isValid( 1 ) )
				connectToDatabase();

			// Execute a query that calculates the amount of IDs in the database.
			ResultSet rSet = executeQuery( "SELECT COUNT(id) FROM " + type.getSimpleName().toLowerCase() );

			// If we get any result, add 1 to that and return it.
			if( rSet.next() )
				return( rSet.getInt( 1 ) + 1 );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return -1;
	}
}
