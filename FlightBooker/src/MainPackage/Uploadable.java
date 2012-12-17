package MainPackage;

/**
 * 
 * Interface that all our serialisible objects are required to implement to be able to work with the database system.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * @version 1.0
 * 
 */
public interface Uploadable
{
	/**
	 * Accessor function for the ID field of this object.
	 * 
	 * @return the ID of the object.
	 */
	public int getID();

	/**
	 * Mutator function for the ID field of this object.
	 * 
	 * @param iID
	 *            - an Integer that the ID of this object is set to.
	 */
	public void setID( int iID );
}
