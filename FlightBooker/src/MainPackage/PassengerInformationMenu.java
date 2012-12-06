package MainPackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.*;

public class PassengerInformationMenu {

	private JFrame frame;
	private JPanel mainPanel;

	private JTextField[] infoFields;

	private boolean isEditing = false;

	public PassengerInformationMenu(JFrame frame)
	{
		this.frame = frame;
		setupFrame();
		//setupFonts();

		makeContent();
	}

	private class actionListener implements ActionListener
	{
		@Override
		public void actionPerformed( ActionEvent e )
		{
			String command = e.getActionCommand();
			if( command == "OK")
				System.out.println("OK");
			//SEND TIL DATABASE
			
			if( command == "Cancel")
			{
				System.out.println("Cancel");
				editOff();
			}			

			if( command == "Edit" )
				editOn();
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

		JLabel nameLabel = new JLabel( "Name:" );
		JLabel genderLabel = new JLabel( "Gender:" );
		JLabel birthLabel = new JLabel( "Date of birth:" );
		JLabel countryLabel = new JLabel( "Country:" );
		JLabel nationalityLabel = new JLabel( "Nationality" );
		JLabel addressLabel = new JLabel( "Address:" );
		JLabel phoneLabel = new JLabel( "Phone:" );
		JLabel passportLabel = new JLabel( "Passport Nr:" );

		nameLabel.setBounds( 10, 10, 85, 20 );
		genderLabel.setBounds( 10, 35, 85, 20 );
		birthLabel.setBounds( 10, 60, 85, 20 );
		countryLabel.setBounds( 10, 85, 85, 20 );
		nationalityLabel.setBounds( 10, 110, 85, 20 );
		addressLabel.setBounds( 10, 135, 85, 20 );
		phoneLabel.setBounds( 10, 160, 85, 20 );
		passportLabel.setBounds( 10, 185, 85, 20 );

		JTextField nameTextField = new JTextField();
		JTextField genderTextField = new JTextField();
		JTextField birthTextField = new JTextField();
		JTextField countryTextField = new JTextField();
		JTextField nationaTextField = new JTextField();
		JTextField addressTextField = new JTextField();
		JTextField phoneTextField = new JTextField();
		JTextField passporTextField = new JTextField();

		infoFields = new JTextField[]{nameTextField, genderTextField, birthTextField, countryTextField, 
				nationaTextField, addressTextField, phoneTextField, passporTextField};

		editOff();

		nameTextField.setBounds( 90, 10, 175, 20 );
		genderTextField.setBounds( 90, 35, 175, 20 );
		birthTextField.setBounds( 90, 60, 175, 20 );
		countryTextField.setBounds( 90, 85, 175, 20 );
		nationaTextField.setBounds( 90, 110, 175, 20 );
		addressTextField.setBounds( 90, 135, 175, 20 );
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
		mainPanel.add( addressLabel );
		mainPanel.add( phoneLabel );
		mainPanel.add( passportLabel );
		mainPanel.add( nameTextField );
		mainPanel.add( genderTextField );
		mainPanel.add( birthTextField );
		mainPanel.add( countryTextField );
		mainPanel.add( nationaTextField );
		mainPanel.add( addressTextField );
		mainPanel.add( phoneTextField );
		mainPanel.add( passporTextField );

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		buttonsPanel.setBounds( 40, 210, 200, 40 );

		JButton okButton = new JButton( "OK" );
		okButton.setActionCommand( "OK" );
		okButton.addActionListener( new actionListener() );

		JButton cancelButton = new JButton( "Cancel" );
		cancelButton.setActionCommand( "Cancel" );
		cancelButton.addActionListener( new actionListener() );

		JButton editButton = new JButton( "Edit" );
		editButton.setActionCommand( "Edit" );
		editButton.addActionListener( new actionListener() );

		buttonsPanel.add( okButton );
		buttonsPanel.add( cancelButton );
		buttonsPanel.add( editButton );

		String[] columns = {"Date of reservation","Destination","Passengers"};

		Object[][] reservationsData = {
				{new Date(System.currentTimeMillis()), "CPH",
					new Integer(5)}
		};

		JTable reservationTable = new JTable(reservationsData, columns);
		reservationTable.setBounds(40, 260, 200, 200);

		mainPanel.add(buttonsPanel);
		mainPanel.add(reservationTable);

		frame.add( mainPanel );

	}

	private void editOn()
	{
		for(JTextField field : infoFields)
			field.setEditable(true);
		isEditing = true;
	}

	private void editOff()
	{
		for(JTextField field : infoFields)
			field.setEditable(false);
		isEditing = false;
	}

	private void update()
	{
		frame.setVisible(true);

	}

}
