package MainPackage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class PassengerInformationEditor extends JFrame
{

	private Person person;

	private class actionListener implements ActionListener
	{
		@Override
		public void actionPerformed( ActionEvent e )
		{
			if( e.getActionCommand() == "OK" ){
				System.out.println( "OK - Commit changes" );
				//if anything has changed, commit changes
				getInstance().dispose();
			}

			else if( e.getActionCommand() == "Cancel" )
				getInstance().dispose();
		}
	}

	public PassengerInformationEditor getInstance()
	{
		return this;
	}

	public PassengerInformationEditor()
	{
		setupFrame();
		makeContent();
	}

	public PassengerInformationEditor(Person person)
	{
		this.person = person;
		setupFrame();
		makeContent();
	}

	private void setupFrame()
	{
		this.setSize( 300, 320 );
		this.setResizable( false );
		this.setTitle("Edit Passenger Information");

		this.setLocationRelativeTo( null );

		update();
	}

	private void makeContent()
	{
		Container contentPane = this.getContentPane();
		contentPane.setLayout( new BoxLayout(contentPane, BoxLayout.PAGE_AXIS) );

		JLabel nameLabel = new JLabel( "Name: " );
		JLabel genderLabel = new JLabel( "Gender: " );
		JLabel birthLabel = new JLabel( "Date of birth: " );
		JLabel countryLabel = new JLabel( "Country: " );
		JLabel nationalityLabel = new JLabel( "Nationality: " );
		JLabel adressLabel = new JLabel( "Adress: " );
		JLabel phoneLabel = new JLabel( "Phone: " );
		JLabel passportLabel = new JLabel( "Passport Nr: " );

		JLabel[] labels = {nameLabel, genderLabel, birthLabel, countryLabel, nationalityLabel, adressLabel, phoneLabel, passportLabel};
		JPanel infoLabelsPanel = new JPanel(new GridLayout(labels.length, 1));

		for(JLabel label : labels)
			infoLabelsPanel.add(label);

		JTextField nameTextField = new JTextField(person.getFirstName());
		JTextField genderTextField = new JTextField(person.getSurName());
		JTextField birthTextField = new JTextField(person.getDateOfBirth());
		JTextField countryTextField = new JTextField(person.getCountry());
		JTextField nationaTextField = new JTextField(person.getNationality());
		JTextField adressTextField = new JTextField(person.getAdress());
		JTextField phoneTextField = new JTextField(person.getPhone());
		JTextField passporTextField = new JTextField(person.getPassportNumber());

		JTextField[] infoFields = new JTextField[]{nameTextField, genderTextField, birthTextField, countryTextField, 
				nationaTextField, adressTextField, phoneTextField, passporTextField};

		JPanel infoFieldsPanel = new JPanel(new GridLayout(infoFields.length, 1));

		for(JTextField field : infoFields)
			infoFieldsPanel.add(field);

		JPanel passengerInfoPanel = new JPanel(new BorderLayout());
		passengerInfoPanel.add(infoLabelsPanel, BorderLayout.WEST);
		passengerInfoPanel.add(infoFieldsPanel, BorderLayout.CENTER);
		TitledBorder passengerTitleBorder = BorderFactory.createTitledBorder("Passenger details");
		passengerInfoPanel.setBorder(passengerTitleBorder);

		ActionListener listener = new actionListener();
		
		JButton okButton = new JButton( "OK" );
		okButton.setActionCommand( "OK" );
		okButton.addActionListener(listener);

		JButton cancelButton = new JButton( "Cancel" );
		cancelButton.setBounds( 135, 263, 85, 25 );
		cancelButton.setActionCommand( "Cancel" );
		cancelButton.addActionListener(listener);

		JPanel buttonsPanel = new JPanel(new FlowLayout());
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);

		add( passengerInfoPanel );
		add( buttonsPanel);

	}

	private void update()
	{
		this.setVisible( true );
	}

	private void addPassenger()
	{

	}
}
