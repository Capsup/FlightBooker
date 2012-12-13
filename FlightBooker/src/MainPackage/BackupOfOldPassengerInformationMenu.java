package MainPackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.*;

public class BackupOfOldPassengerInformationMenu {

	private JFrame frame;
	private JPanel mainPanel;

	private Passenger passenger;	

	private JTextField[] infoFields;
	private JButton okButton;
	private JButton cancelButton;
	private JButton editButton;
	private JButton inspectReservationButton;

	private boolean isEditing = false;

	public BackupOfOldPassengerInformationMenu(JFrame frame/*, Passenger passenger */)
	{
		//this.passenger = passenger;
		this.passenger = new Passenger(new Person("Jesper", "Nysteen", "male",
				"06-04-1991", "Denmark", "Danish", "Skaffervej 15, 3 tv", "31225525","3443542624654", 1), new Seat(1, 1));


		this.frame = frame;
		setupFrame();
		//setupFonts();

		makeContent();
		
		editOff();
	}

	private class actionListener implements ActionListener
	{
		@Override
		public void actionPerformed( ActionEvent e )
		{
			String command = e.getActionCommand();
			if( command == "OK") {
				System.out.println("OK");
				if(isEditing)
				{
					System.out.println("Commit changes");
					editOff();
				}
			}

			//SEND TIL DATABASE

			if( command == "Cancel")
			{
				System.out.println("Cancel");
				editOff();
			}			

			if( command == "Edit" && !isEditing ) {
				System.out.println("Edit");
				editOn();
			}
		}
	}

	private void setupFrame()
	{
		frame.setSize(300, 600);
		frame.setResizable(false);

		frame.setLocationRelativeTo(null);

		update();
	}

	private void makeContent()
	{
		Container contentPane = frame.getContentPane();
		contentPane.setLayout( null );

		JLabel titleLabel = new JLabel( "Enter passenger information" );
		titleLabel.setBounds( 10, 10, 200, 20 );

		frame.add( titleLabel );

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout( null );
		mainPanel.setBorder( BorderFactory.createEtchedBorder() );
		mainPanel.setBounds( 10, 40, frame.getWidth() - 25, frame.getHeight() - 40 * 2 - 20 );

		JLabel nameLabel = new JLabel( "Name: " );
		JLabel genderLabel = new JLabel( "Gender: " );
		JLabel birthLabel = new JLabel( "Date of birth: " );
		JLabel countryLabel = new JLabel( "Country: " );
		JLabel nationalityLabel = new JLabel( "Nationality: " );
		JLabel adressLabel = new JLabel( "Adress: " );
		JLabel phoneLabel = new JLabel( "Phone: " );
		JLabel passportLabel = new JLabel( "Passport Nr: " );

		nameLabel.setBounds( 10, 10, 85, 20 );
		genderLabel.setBounds( 10, 35, 85, 20 );
		birthLabel.setBounds( 10, 60, 85, 20 );
		countryLabel.setBounds( 10, 85, 85, 20 );
		nationalityLabel.setBounds( 10, 110, 85, 20 );
		adressLabel.setBounds( 10, 135, 85, 20 );
		phoneLabel.setBounds( 10, 160, 85, 20 );
		passportLabel.setBounds( 10, 185, 85, 20 );

		JTextField nameTextField = new JTextField(passenger.getPerson().getFirstName());
		JTextField genderTextField = new JTextField(passenger.getPerson().getSurName());
		JTextField birthTextField = new JTextField(passenger.getPerson().getDateOfBirth());
		JTextField countryTextField = new JTextField(passenger.getPerson().getCountry());
		JTextField nationaTextField = new JTextField(passenger.getPerson().getNationality());
		JTextField adressTextField = new JTextField(passenger.getPerson().getAdress());
		JTextField phoneTextField = new JTextField(passenger.getPerson().getPhone());
		JTextField passporTextField = new JTextField(passenger.getPerson().getPassportNumber());

		infoFields = new JTextField[]{nameTextField, genderTextField, birthTextField, countryTextField, 
				nationaTextField, adressTextField, phoneTextField, passporTextField};

		nameTextField.setBounds( 90, 10, 175, 20 );
		genderTextField.setBounds( 90, 35, 175, 20 );
		birthTextField.setBounds( 90, 60, 175, 20 );
		countryTextField.setBounds( 90, 85, 175, 20 );
		nationaTextField.setBounds( 90, 110, 175, 20 );
		adressTextField.setBounds( 90, 135, 175, 20 );
		phoneTextField.setBounds( 90, 160, 175, 20 );
		passporTextField.setBounds( 90, 185, 175, 20 );

		mainPanel.add( nameLabel );
		mainPanel.add( nameTextField );
		mainPanel.add( genderLabel );
		mainPanel.add( genderTextField );
		mainPanel.add( birthLabel );
		mainPanel.add( birthTextField );
		mainPanel.add( nameLabel );
		mainPanel.add( genderLabel );
		mainPanel.add( birthLabel );
		mainPanel.add( countryLabel );
		mainPanel.add( nationalityLabel );
		mainPanel.add( adressLabel );
		mainPanel.add( phoneLabel );
		mainPanel.add( passportLabel );
		mainPanel.add( nameTextField );
		mainPanel.add( genderTextField );
		mainPanel.add( birthTextField );
		mainPanel.add( countryTextField );
		mainPanel.add( nationaTextField );
		mainPanel.add( adressTextField );
		mainPanel.add( phoneTextField );
		mainPanel.add( passporTextField );

		JPanel infoButtonsPanel = new JPanel();
		infoButtonsPanel.setLayout(new FlowLayout());
		infoButtonsPanel.setBounds( 40, 210, 200, 40 );

		okButton = new JButton( "OK" );
		okButton.setActionCommand( "OK" );
		okButton.addActionListener( new actionListener() );

		cancelButton = new JButton( "Cancel" );
		cancelButton.setActionCommand( "Cancel" );
		cancelButton.addActionListener( new actionListener() );

		editButton = new JButton( "Edit" );
		editButton.setActionCommand( "Edit" );
		editButton.addActionListener( new actionListener() );

		infoButtonsPanel.add( okButton );
		infoButtonsPanel.add( cancelButton );
		infoButtonsPanel.add( editButton );

		String[] columns = {"Date of reservation","Destination","Passengers"};

		Object[][] reservationsData = {
				{new Date(System.currentTimeMillis()), "CPH",
					new Integer(5)}
		};

		JTable reservationTable = new JTable(reservationsData, columns);
		reservationTable.setBounds(40, 260, 200, 200);
		
		inspectReservationButton = new JButton( "Inspect " );
		

		mainPanel.add(infoButtonsPanel);
		mainPanel.add(reservationTable);

		frame.add( mainPanel );

	}

	private void editOn()
	{
		for(JTextField field : infoFields)
			field.setEditable(true);
		cancelButton.setEnabled(true);
		editButton.setEnabled(false);
		isEditing = true;
	}

	private void editOff()
	{
		for(JTextField field : infoFields)
			field.setEditable(false);
		cancelButton.setEnabled(false);
		editButton.setEnabled(true);
		isEditing = false;
	}

	private void update()
	{
		frame.setVisible(true);

	}

}
