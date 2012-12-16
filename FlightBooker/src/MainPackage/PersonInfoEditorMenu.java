package MainPackage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * This editor enables the user to edit a persons details.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * 
 */
public class PersonInfoEditorMenu extends JFrame
{
	private JTextFieldUpgraded fieldUpgraded;

	private Person person;

	private JTextField nameTextField, genderTextField, birthTextField, countryTextField, nationaTextField, adressTextField, phoneTextField,
	        passporTextField;

	boolean isNew;

	private class actionListener implements ActionListener
	{
		@Override
		public void actionPerformed( ActionEvent e )
		{
			if( e.getActionCommand() == "OK" )
			{

				// If any of the JTextFields contains no data at all
				if( nameTextField.getText().equals( "" ) || genderTextField.getText().equals( "" ) || birthTextField.getText().equals( "" )
				        || countryTextField.getText().equals( "" ) || nationaTextField.getText().equals( "" )
				        || adressTextField.getText().equals( "" ) || phoneTextField.getText().equals( "" ) || passporTextField.getText().equals( "" ) )
				{
					// Slap them with a Error Dialog.
					showErrorDialog();
					return;
				}

				//TODO: FIX THIS
				// Create a new person from all the data in fields. The name field is split up into first name and surname.
				Person updatedPerson;
				if( nameTextField.getText().split( " " ).length > 1 )
				{
					String[] sNameSplit = nameTextField.getText().split( " " );
					
					String sSurName = "";
					for( int i = 1; i < sNameSplit.length; i++ )
                    {
	                    sSurName += i == 1 ? "" : " " + sNameSplit[i];
                    }
					
					updatedPerson = new Person( sNameSplit[0], sSurName,
					        genderTextField.getText(), birthTextField.getText(), countryTextField.getText(), nationaTextField.getText(),
					        adressTextField.getText(), phoneTextField.getText(), passporTextField.getText(), person.getID() );
				}
				else
					updatedPerson = new Person( nameTextField.getText(), "",
					        genderTextField.getText(), birthTextField.getText(), countryTextField.getText(), nationaTextField.getText(),
					        adressTextField.getText(), phoneTextField.getText(), passporTextField.getText(), person.getID() );
					
				// Create a new person from all the data in fields. The name field is split up into first name and surname.
				/*Person updatedPerson = new Person( nameTextField.getText().split( " " )[0], nameTextField.getText().split( " " )[1],
				        genderTextField.getText(), birthTextField.getText(), countryTextField.getText(), nationaTextField.getText(),
				        adressTextField.getText(), phoneTextField.getText(), passporTextField.getText(), person.getID() );*/
				updatedPerson.setReservations( person.getReservations() );
					
					
				if( fieldUpgraded != null )
					fieldUpgraded.setPerson( updatedPerson );

				// If we're creating a new person, we save that person in the database as a new person.
				if( isNew )
					Database.getInstance().Add( updatedPerson );
				else
					// Otherwise, we replace the current person with this new up-to-date one...
					Database.getInstance().Replace( person.getCustomerID(), updatedPerson );

				// Remember to always clean up your garbage kids!
				PersonInfoEditorMenu.this.dispose();
			}

			else if( e.getActionCommand() == "Cancel" )
				PersonInfoEditorMenu.this.dispose();
		}
	}

	public PersonInfoEditorMenu()
	{
		setupFrame();
		makeContent();
	}

	public PersonInfoEditorMenu( Person person )
	{
		// this.fieldUpgraded = fieldUpgraded;
		this.person = person;
		setupFrame();
		makeContent();
	}

	/**
	 * This constructor is called with a Person object and JTextFieldUpgraded. This allows this window to update the information in the field.
	 * 
	 * @param person
	 * @param fieldUpgraded
	 */
	public PersonInfoEditorMenu( Person person, JTextFieldUpgraded fieldUpgraded )
	{
		this.fieldUpgraded = fieldUpgraded;
		if( person != null )
		{
			this.person = person;
			isNew = false;
		}
		else
		{
			this.person = new Person( "", "", "", "", "", "", "", "", "", Database.getInstance().GetID( Person.class ) );
			isNew = true;
		}
		setupFrame();
		makeContent();
	}

	private void setupFrame()
	{
		this.setSize( 300, 320 );
		this.setResizable( false );
		this.setTitle( "Person Info Editor Menu" );

		this.setLocationRelativeTo( null );

		update();
	}

	private void makeContent()
	{
		Container contentPane = this.getContentPane();
		contentPane.setLayout( new BoxLayout( contentPane, BoxLayout.PAGE_AXIS ) );

		JLabel nameLabel = new JLabel( "Name: " );
		JLabel genderLabel = new JLabel( "Gender: " );
		JLabel birthLabel = new JLabel( "Date of birth: " );
		JLabel countryLabel = new JLabel( "Country: " );
		JLabel nationalityLabel = new JLabel( "Nationality: " );
		JLabel adressLabel = new JLabel( "Adress: " );
		JLabel phoneLabel = new JLabel( "Phone: " );
		JLabel passportLabel = new JLabel( "Passport Nr: " );

		JLabel[] labels = { nameLabel, genderLabel, birthLabel, countryLabel, nationalityLabel, adressLabel, phoneLabel, passportLabel };
		JPanel infoLabelsPanel = new JPanel( new GridLayout( labels.length, 1 ) );

		// For each label in the labels array, add them to the panel. Somebody thought they were smart and wanted to save some typing...
		for( JLabel label : labels )
			infoLabelsPanel.add( label );

		if( !person.getFirstName().equals( "" ) )
			nameTextField = new JTextField( person.getFirstName() + " " + person.getSurName() );
		else
			nameTextField = new JTextField( "" );

		genderTextField = new JTextField( person.getGender() );
		birthTextField = new JTextField( person.getDateOfBirth() );
		countryTextField = new JTextField( person.getCountry() );
		nationaTextField = new JTextField( person.getNationality() );
		adressTextField = new JTextField( person.getAdress() );
		phoneTextField = new JTextField( person.getPhone() );
		passporTextField = new JTextField( person.getPassportNumber() );

		JTextField[] infoFields = new JTextField[] { nameTextField, genderTextField, birthTextField, countryTextField, nationaTextField,
		        adressTextField, phoneTextField, passporTextField };

		JPanel infoFieldsPanel = new JPanel( new GridLayout( infoFields.length, 1 ) );

		for( JTextField field : infoFields )
			infoFieldsPanel.add( field );

		JPanel passengerInfoPanel = new JPanel( new BorderLayout() );
		passengerInfoPanel.add( infoLabelsPanel, BorderLayout.WEST );
		passengerInfoPanel.add( infoFieldsPanel, BorderLayout.CENTER );
		TitledBorder passengerTitleBorder = BorderFactory.createTitledBorder( "Passenger details" );
		passengerInfoPanel.setBorder( passengerTitleBorder );

		ActionListener listener = new actionListener();

		JButton okButton = new JButton( "OK" );
		okButton.setActionCommand( "OK" );
		okButton.addActionListener( listener );

		JButton cancelButton = new JButton( "Cancel" );
		cancelButton.setBounds( 135, 263, 85, 25 );
		cancelButton.setActionCommand( "Cancel" );
		cancelButton.addActionListener( listener );

		JPanel buttonsPanel = new JPanel( new FlowLayout() );
		buttonsPanel.add( okButton );
		buttonsPanel.add( cancelButton );

		add( passengerInfoPanel );
		add( buttonsPanel );

	}

	private void update()
	{
		this.setVisible( true );
	}

	/**
	 * Shows an error dialog to user, indicating that he probably broke the system and needs to perform actions accordingly. ( Or more likely, he
	 * forgot to fill out all fields... )
	 */
	private void showErrorDialog()
	{
		JOptionPane.showMessageDialog( this, "Please fill out all fields", "Error", JOptionPane.ERROR_MESSAGE );
	}
}
