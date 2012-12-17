package MainPackage;

import javax.swing.JTextField;

/**
 * 
 * JTextFieldUpgraded is a class that we use in our PersonInfoEditorMenu. It allows the JTextField to have a person object attached so that it is easy
 * to update the JTextField to contain specific Person information.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * @version 1.0
 * 
 */
public class JTextFieldUpgraded extends JTextField
{
	Person myPerson;

	// Empty constructor
	public JTextFieldUpgraded()
	{
		// Forever alone
	}

	/**
	 * Non-empty constructor. Sets the persons field to specified person.
	 * 
	 * @param person
	 *            - the Person object to show information for
	 */
	public JTextFieldUpgraded( Person person )
	{
		myPerson = person;
	}

	/**
	 * Sets the person field to the specified person.
	 * 
	 * @param person
	 *            - the Person object to set the field to
	 */
	public void setPerson( Person person )
	{
		myPerson = person;

		// Set the text of this JTextField to the specified values of the Person object.
		this.setText( person.getFirstName() + " " + person.getSurName() + " - " + person.getPhone() );
	}

	/**
	 * Validates the data inside the TextField and makes sure it corresponds to the Person object assigned to the field. If not, returns null.
	 * @return the Person object assigned to this field.
	 */
	public Person getPerson()
	{
		String[] sInfo = this.getText().split( " " );

		String sSurName = "";
		//This allows the Persons to have more than just name + surname.
		for( int i = 1; i < sInfo.length; i++ )
        {
			if( sInfo[i].equalsIgnoreCase( "-" ) )
				break;
			
	        sSurName += ( i == 1 ? "" : " " ) + sInfo[i];
        }

		// If text in this JTextField does not equal to the actual values in our Person object, we return a null Person object.
		if( myPerson == null || !( myPerson.getFirstName().equalsIgnoreCase( sInfo[0] ) ) || !( myPerson.getSurName().equalsIgnoreCase( sSurName ) )
		        || !( myPerson.getPhone().equalsIgnoreCase( sInfo[sInfo.length-1] ) ) )
			return null;

		return myPerson;
	}
}
