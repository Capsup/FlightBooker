package MainPackage;

import javax.swing.JTextField;

public class JTextFieldUpgraded extends JTextField
{
	Person myPerson;
	
	public JTextFieldUpgraded()
	{
		
	}
	
	public JTextFieldUpgraded( Person person )
	{
		myPerson = person;
	}
	
	public void setPerson( Person person )
	{
		myPerson = person;
		
		this.setText( person.getFirstName() + " " + person.getSurName() + " - " + person.getPhone() );
	}
	
	public Person getPerson()
	{
		String[] sInfo = this.getText().split( " " );
		
		//boolean bSuccess = true;
		if( myPerson == null || !(myPerson.getFirstName().equalsIgnoreCase( sInfo[0] ) ) || !( myPerson.getSurName().equalsIgnoreCase( sInfo[1] ) ) || !( myPerson.getPhone().equalsIgnoreCase( sInfo[3] ) )  )
			return new Person( "", "", "", "", "", "", "", "", "", 0 );
			
		return myPerson;
	}
	
}
