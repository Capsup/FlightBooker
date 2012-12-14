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
		return myPerson;
	}
	
}
