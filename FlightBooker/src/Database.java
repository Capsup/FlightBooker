
public class Database
{
	private static Database instance;
	
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
	
	public void executeQuery( String sQuery )
	{
		
	}
}
