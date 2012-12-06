package MainPackage;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PassengerInformationEditor extends JFrame
{
	private class actionListener implements ActionListener
	{
		@Override
		public void actionPerformed( ActionEvent e )
		{
		    if( e.getActionCommand() == "ok" )
		    	System.out.println("WAZZUP");
		    else if( e.getActionCommand() == "cancel" )
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

	private void setupFrame()
	{
		this.setSize( 300, 320 );
		this.setResizable( false );

		this.setLocationRelativeTo( null );

		update();
	}

	private void makeContent()
	{
		Container contentPane = this.getContentPane();
		contentPane.setLayout( null );

		JLabel titleLabel = new JLabel( "Enter passenger information" );
		titleLabel.setBounds( 10, 10, 200, 20 );

		add( titleLabel );

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout( null );
		mainPanel.setBorder( BorderFactory.createEtchedBorder() );
		mainPanel.setBounds( 10, 40, this.getWidth() - 25, this.getHeight() - 40 * 2 - 20 );

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

		JButton okButton = new JButton( "Ok" );
		okButton.setBounds( 75, 263, 50, 25 );
		okButton.setActionCommand( "ok" );
		okButton.addActionListener( new actionListener() );

		JButton cancelButton = new JButton( "Cancel" );
		cancelButton.setBounds( 135, 263, 85, 25 );
		cancelButton.setActionCommand( "cancel" );
		cancelButton.addActionListener( new actionListener() );

		add( okButton );
		add( cancelButton );

		add( mainPanel );
	}

	private void update()
	{
		this.setVisible( true );
	}
	
	private void addPassenger()
	{
		
	}
}
