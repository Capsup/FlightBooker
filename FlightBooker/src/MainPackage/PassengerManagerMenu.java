package MainPackage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * 
 * This class is the class that controls the window where the user inputs the details of all the persons of the current reservation.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * 
 */
public class PassengerManagerMenu
{
	private JFrame frame;
	private JPanel mainPanel;
	private JPanel mainPassengerPanel, passengerPanel;
	private JTextFieldUpgraded[] passengerField;
	private JTextFieldUpgraded currentField;

	private boolean isNew;

	private PersonData listModel;
	private JList<Person> list;

	private Reservation currentReservation;

	private class actionListener implements ActionListener
	{
		JTextFieldUpgraded textField;

		public actionListener( JTextFieldUpgraded textField )
		{
			this.textField = textField;
		}

		@Override
		public void actionPerformed( ActionEvent e )
		{
			if( e.getActionCommand() == "edit" )
			{
				new PersonInfoEditorMenu( textField.getPerson(), textField );
			}
		}
	}

	private class okListener implements ActionListener
	{
		@Override
		public void actionPerformed( ActionEvent e )
		{
			boolean bSuccess = true;
			for( JTextFieldUpgraded textFieldUpgraded : passengerField )
			{
				String sText = textFieldUpgraded.getText();
				if( sText.equals( "" ) )
					bSuccess = false;
				else
				{
					if( textFieldUpgraded.getPerson() == null )
						bSuccess = false;
				}
			}

			if( bSuccess )
			{
				Passenger[] passengers = currentReservation.getPassengers();

				for( int i = 0; i < passengerField.length; i++ )
				{
					passengers[i].setPerson( passengerField[i].getPerson() );
				}

				currentReservation.setOwner( passengerField[0].getPerson() );

				frame.remove( mainPanel );

				new ReservationInfoMenu( frame, currentReservation, isNew );
			}
			else
			{
				showErrorDialog();
			}
		}
	}

	/**
	 * 
	 * This class allows the JTextFields to respond to any keyPressed events.
	 * 
	 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
	 * 
	 */
	private class keyListener implements KeyListener
	{

		@Override
		public void keyPressed( KeyEvent e )
		{

		}

		@Override
		public void keyReleased( KeyEvent e )
		{
			// Clear the current list.
			listModel.clear();

			// Set the currentField to the JTextFieldUpgraded that fired this event.
			currentField = ( JTextFieldUpgraded ) e.getComponent();

			// If this field's text contains anything, we want the suggestive list to show up.
			if( !currentField.getText().equals( "" ) )
				calculateResults( currentField );

			// However if it doesn't contain anything, make sure the list is hidden.
			if( currentField.getText().equals( "" ) )
				list.setVisible( false );
		}

		@Override
		public void keyTyped( KeyEvent e )
		{

		}
	}

	private class mouseListener implements MouseListener
	{

		@Override
		public void mouseClicked( MouseEvent e )
		{
			if( list.getSelectedIndex() == -1 )
				return;

			Person selectedPerson = listModel.listPersons.get( list.getSelectedIndex() );

			if( selectedPerson != null )
				currentField.setPerson( selectedPerson );

			list.setVisible( false );
		}

		@Override
		public void mouseEntered( MouseEvent e )
		{

		}

		@Override
		public void mouseExited( MouseEvent e )
		{

		}

		@Override
		public void mousePressed( MouseEvent e )
		{

		}

		@Override
		public void mouseReleased( MouseEvent e )
		{

		}
	}

	/**
	 * 
	 * This class makes it easy for us to display the information we want in the list. The list calls these functions automatically and we just return
	 * the correct data.
	 * 
	 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
	 * 
	 */
	private class PersonData extends AbstractListModel
	{
		// Keep a list of the persons in the database.
		ArrayList<Person> listPersons;

		public PersonData( ArrayList<Person> persons )
		{
			this.listPersons = persons;
		}

		/**
		 * Clears the data of this datamodel.
		 */
		public void clear()
		{
			int index = listPersons.size() - 1;
			listPersons.clear();
			if( index >= 0 )
			{
				fireIntervalRemoved( this, 0, index );
			}
		}

		public PersonData()
		{
			this.listPersons = new ArrayList<Person>();
		}

		/**
		 * Adds the given person to the list.
		 * 
		 * @param person
		 *            - the Person object to be added to the list.
		 */
		public void addElement( Person person )
		{
			int index = listPersons.size();
			listPersons.add( person );
			fireIntervalAdded( this, index, index );
		}

		@Override
		public Object getElementAt( int index )
		{
			// When the list attempts to get the element at a specified index, we return the data that we want to actually display. This makes it
			// simple to make the list display the data we want but still allows us to have the list contain Person objects instead of Strings.
			return( listPersons.get( index ).getFirstName() + " " + listPersons.get( index ).getSurName() + " - " + listPersons.get( index )
			        .getPhone() );
		}

		@Override
		public int getSize()
		{
			return listPersons.size();
		}

	}

	public PassengerManagerMenu( JFrame frame, Reservation currentReservation, boolean isNew )
	{
		this.frame = frame;

		this.currentReservation = currentReservation;
		this.isNew = isNew;

		setupFrame();
		// setupFonts();

		makeContent();
	}

	public void calculateResults( JTextField currentField )
	{
		ArrayList<Person> persons = Database.getInstance().Get( Person.class );
		for( Person person : persons )
		{
			if( person.getFirstName().toLowerCase().startsWith( currentField.getText() )
			        || person.getSurName().toLowerCase().startsWith( currentField.getText() )
			        || person.getPhone().toLowerCase().startsWith( currentField.getText() )
			        || ( person.getFirstName() + " " + person.getSurName() ).toLowerCase().startsWith( currentField.getText() ) )
				listModel.addElement( person );
		}

		list.setBounds( currentField.getBounds().x, currentField.getBounds().y + currentField.getBounds().height + 1, currentField.getWidth(), 100 );

		if( listModel.getSize() == 0 )
			listModel.addElement( new Person( "", "- No persons found", "", "", "", "", "", "", "", -1 ) );

		list.setVisible( true );
	}

	private void setupFrame()
	{
		frame.setSize( 300, 600 );
		frame.setResizable( false );
		frame.setTitle( "Passenger Manager Menu" );

		update();
	}

	private void makeContent()
	{
		Container contentPane = frame.getContentPane();

		mainPanel = new JPanel();
		mainPanel.setLayout( new BorderLayout() );

		// Middle
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout( new BoxLayout( middlePanel, BoxLayout.Y_AXIS ) );

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder( BorderFactory.createEmptyBorder() );

		mainPassengerPanel = new JPanel();
		mainPassengerPanel.setLayout( new BoxLayout( mainPassengerPanel, BoxLayout.Y_AXIS ) );

		passengerField = new JTextFieldUpgraded[currentReservation.getPassengers().length];

		passengerPanel = new JPanel();
		passengerPanel.setLayout( null );

		// Add a JTextFieldUpgraded for each passenger in the current reservation, to the panel
		for( int i = 0; i < currentReservation.getPassengers().length; i++ )
		{
			listModel = new PersonData();
			list = new JList<>( listModel );

			list.addMouseListener( new mouseListener() );
			passengerPanel.add( list );
			passengerPanel.setComponentZOrder( list, 0 );
			list.setVisible( false );

			passengerField[i] = new JTextFieldUpgraded();

			// If the passenger already has a person object associated, display the person object.
			if( currentReservation.getPassengers()[i].getPerson() != null )
				passengerField[i].setPerson( currentReservation.getPassengers()[i].getPerson() );

			// Set the bounds of the current passengerField correctly using AbsoluteLayout.
			passengerField[i].setBounds( 20, ( i + 1 ) * 50, 175, 25 );
			passengerField[i].addKeyListener( new keyListener() );

			passengerPanel.add( passengerField[i] );

			JButton editButton = new JButton( "New/Edit" );
			editButton.setBounds( 200, ( i + 1 ) * 50, 75, 25 );
			editButton.setActionCommand( "edit" );
			editButton.addActionListener( new actionListener( passengerField[i] ) );
			passengerPanel.add( editButton );

			mainPassengerPanel.add( passengerPanel );
		}

		// Add some descriptive labels
		JLabel mainPassengerHeaderLabel = new JLabel( "Reservation owner" );
		mainPassengerHeaderLabel.setBounds( 20, 30, 175, 25 );
		passengerPanel.add( mainPassengerHeaderLabel );

		if( currentReservation.getPassengers().length > 1 )
		{
			JLabel additionalPassengerLabel = new JLabel( "Additional Passengers" );
			additionalPassengerLabel.setBounds( 20, 50 * 2 - 20, 175, 25 );
			passengerPanel.add( additionalPassengerLabel );
		}

		scrollPane.setViewportView( mainPassengerPanel );

		middlePanel.add( scrollPane );

		mainPanel.add( middlePanel, BorderLayout.CENTER );

		JButton button = new JButton( "OK" );
		button.addActionListener( new okListener() );

		mainPanel.add( button, BorderLayout.SOUTH );

		contentPane.add( mainPanel );
	}

	private void update()
	{
		frame.setVisible( true );
	}

	private void showErrorDialog()
	{
		JOptionPane.showMessageDialog( frame, "Please make sure all fields contains a valid person", "Error", JOptionPane.ERROR_MESSAGE );
	}
}
