package MainPackage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * PassengerInformationMenu presents an overview of a persons details and reservations. The user can click one of three buttons found in the menu.
 * 
 * Clicking "Edit" opens a new menu, through which the user change a persons details. Clicking "Inspect" opens a new menu, through which the user can
 * inspect the details of the chosen reservation. Clicking "Close" closes the menu.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * 
 */
public class PersonInfoMenu
{

	private JFrame frame;
	private JPanel mainPanel;

	private Person person;

	JTextField nameTextField;
	JTextField genderTextField;
	JTextField birthTextField;
	JTextField countryTextField;
	JTextField nationaTextField;
	JTextField adressTextField;
	JTextField phoneTextField;
	JTextField passporTextField;

	private JButton editButton;
	private JButton inspectReservationButton;
	private JButton closeButton;

	private JTable reservationsTable;
	private String[] columns;
	private Object[][] tableData;
	private DefaultTableModel tableModel;
	private JScrollPane tableScrollPane;
	private JPanel reservationsTablePanel;

	private Reservation[] listItems;

	/**
	 * Creates a new PassengerInformationMenu
	 * 
	 * @param frame
	 *            The JFrame to be used
	 * @param person
	 *            The person to show details of.
	 */
	public PersonInfoMenu( JFrame frame, Person person )
	{
		this.person = person;
		this.frame = frame;
		setupFrame();
		makeContent();

		// System.out.println("FUCKING SHIT"+person.getReservations().length);
	}

	/**
	 * actionListener makes it possible to do something, when a button is clicked. It is notified when a button with an added actionListener is
	 * clicked, and the button's ActionCommand decides what action to take.
	 */
	private class actionListener implements ActionListener
	{
		@Override
		public void actionPerformed( ActionEvent e )
		{
			String command = e.getActionCommand();

			if( command == "Edit information" )
			{
				System.out.println( "Edit information" );
				new PersonInfoEditorMenu( person );
			}

			if( command == "Inspect reservation" )
			{
				System.out.println( "Inspect reservation" );
				System.out.println( reservationsTable.getSelectionModel().getAnchorSelectionIndex() );

				int chosenObjectIncorrectRow = reservationsTable.getSelectionModel().getAnchorSelectionIndex();
				if( chosenObjectIncorrectRow >= 0 && listItems != null )
				{
					{
						if( chosenObjectIncorrectRow >= 0 )
						{
							int chosenObjectActualRow = reservationsTable.convertRowIndexToModel( chosenObjectIncorrectRow );
							int chosenObjectID = ( int ) reservationsTable.getValueAt( chosenObjectActualRow, 0 );
							System.out.println( chosenObjectID );

							Reservation reservationToFind = Database.getInstance().Get( chosenObjectID, Reservation.class );

							Reservation[] reservations = Database.getInstance().Get( reservationToFind.getFlight().getID(), Flight.class )
							        .getReservations();

							for( int i = 0; i < reservations.length; i++ )
							{
								if( reservations[i].getID() == reservationToFind.getID() )
								{
									reservationToFind = reservations[i];
								}
							}

							// Reservation reservation = Database.getInstance().Get(reservationToFind.getFlight().getID(), Flight.class
							// ).getReservations()[reservationToFind.getCurrentFlightReservationIndex()];
							Reservation reservation = reservationToFind;
							new ReservationInfoMenu( new JFrame(), reservation, false );
						}
					}
				}
			}
			if( command == "Close" )
			{
				System.out.println( "Close" );
				frame.dispose();
			}

		}
	}

	private void setupFrame()
	{
		frame.setSize( 800, 700 );
		frame.setResizable( false );
		frame.setLocationRelativeTo( null );
		frame.setTitle( "Passenger Info Menu" );
		frame.setVisible( true );

		frame.addWindowListener( new WindowAdapter()
		{
			public void windowActivated( WindowEvent e )
			{
				if( person != null )
				{
					updatePerson();
					updatePersonTextFields();
				}
			}
		} );
	}

	private void makeContent()
	{
		Container contentPane = frame.getContentPane();
		contentPane.setLayout( null );

		mainPanel = new JPanel();
		mainPanel.setLayout( new BoxLayout( mainPanel, BoxLayout.PAGE_AXIS ) );
		// mainPanel.setLayout(new FlowLayout());
		mainPanel.setBounds( 10, 40, frame.getWidth() - 25, frame.getHeight() - 40 * 2 - 20 );

		JLabel nameLabel = new JLabel( "Name: " );
		JLabel genderLabel = new JLabel( "Gender: " );
		JLabel birthLabel = new JLabel( "Date of birth: " );
		JLabel countryLabel = new JLabel( "Country: " );
		JLabel nationalityLabel = new JLabel( "Nationality: " );
		JLabel adressLabel = new JLabel( "Adress: " );
		JLabel phoneLabel = new JLabel( "Phone: " );
		JLabel passportLabel = new JLabel( "Passport Nr: " );

		// Creates an array of all the labels just created
		JLabel[] labels = { nameLabel, genderLabel, birthLabel, countryLabel, nationalityLabel, adressLabel, phoneLabel, passportLabel };
		JPanel infoLabelsPanel = new JPanel( new GridLayout( labels.length, 1 ) );

		// Puts all these labels into a JPanel
		for( JLabel label : labels )
			infoLabelsPanel.add( label );

		nameTextField = new JTextField();
		genderTextField = new JTextField();
		birthTextField = new JTextField();
		countryTextField = new JTextField();
		nationaTextField = new JTextField();
		adressTextField = new JTextField();
		phoneTextField = new JTextField();
		passporTextField = new JTextField();
		// Set up the information in the text fields
		updatePersonTextFields();

		// Creates an array of all the text fields just created
		JTextField[] infoFields = new JTextField[] { nameTextField, genderTextField, birthTextField, countryTextField, nationaTextField,
		        adressTextField, phoneTextField, passporTextField };

		// Puts all these text fields into a JPanel
		for( JTextField field : infoFields )
			field.setEditable( false );

		// Create a new panel to contain the panels of the entire info section
		JPanel infoFieldsPanel = new JPanel( new GridLayout( infoFields.length, 1 ) );

		for( JTextField field : infoFields )
			infoFieldsPanel.add( field );

		JPanel passengerButtonPanel = new JPanel();
		passengerButtonPanel.setLayout( new FlowLayout() );

		editButton = new JButton( "Edit information" );
		editButton.setActionCommand( "Edit information" );
		editButton.addActionListener( new actionListener() );

		passengerButtonPanel.add( editButton );

		JPanel passengerInfoPanel = new JPanel( new BorderLayout() );
		passengerInfoPanel.setPreferredSize( new Dimension( 11, 11 ) );
		passengerInfoPanel.add( infoLabelsPanel, BorderLayout.WEST );
		passengerInfoPanel.add( infoFieldsPanel, BorderLayout.CENTER );
		passengerInfoPanel.add( passengerButtonPanel, BorderLayout.SOUTH );
		TitledBorder passengerTitleBorder = BorderFactory.createTitledBorder( "Passenger details" );
		passengerInfoPanel.setBorder( passengerTitleBorder );

		makeReservationTable();

		// Sets the reservationsTable's properties
		reservationsTable.setColumnSelectionAllowed( false );
		reservationsTable.setCellSelectionEnabled( false );
		reservationsTable.setRowSelectionAllowed( true );
		reservationsTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		reservationsTable.getTableHeader().setReorderingAllowed( false );
		reservationsTable.setFillsViewportHeight( true );

		// Adds a sorting function to the table
		reservationsTable.setAutoCreateRowSorter( true );

		// Adds a scrollpane to reservationsTable
		JScrollPane reservationsScrollPane = new JScrollPane( reservationsTable );
		reservationsScrollPane.setPreferredSize( new Dimension( mainPanel.getWidth() - 10, 200 ) );

		// Adds a border to the reservationsTable
		TitledBorder reservationsTitleBorder = BorderFactory.createTitledBorder( "Passenger reservations" );
		reservationsScrollPane.setBorder( reservationsTitleBorder );

		JPanel reservationsButtonPanel = new JPanel();
		reservationsButtonPanel.setLayout( new FlowLayout() );

		inspectReservationButton = new JButton( "Inspect reservation" );
		inspectReservationButton.setActionCommand( "Inspect reservation" );
		inspectReservationButton.addActionListener( new actionListener() );

		closeButton = new JButton( "Close" );
		closeButton.setActionCommand( "Close" );
		closeButton.addActionListener( new actionListener() );

		reservationsButtonPanel.add( inspectReservationButton );
		reservationsButtonPanel.add( closeButton );

		mainPanel.add( passengerInfoPanel );
		mainPanel.add( reservationsScrollPane );
		mainPanel.add( reservationsButtonPanel );

		frame.add( mainPanel );
	}

	/**
	 * Updates the current persons details
	 */
	private void updatePersonTextFields()
	{
		System.out.println( "updatePersonTextFields" );
		nameTextField.setText( person.getFirstName() + " " + person.getSurName() );
		genderTextField.setText( person.getGender() );
		birthTextField.setText( person.getDateOfBirth() );
		countryTextField.setText( person.getCountry() );
		nationaTextField.setText( person.getNationality() );
		adressTextField.setText( person.getAdress() );
		phoneTextField.setText( person.getPhone() );
		passporTextField.setText( person.getPassportNumber() );
	}

	/**
	 * Updates the person, by getting the same person from the database again
	 */
	private void updatePerson()
	{
		person = Database.getInstance().Get( person.getCustomerID(), Person.class );
	}

	/**
	 * Creates the table of reservations
	 */
	private void makeReservationTable()
	{
		columns = new String[] { " " };
		tableData = new Object[][] { { "No reservations found" } };
		tableModel = new DefaultTableModel( tableData, columns );
		reservationsTable = new JTable( tableModel );

		// Gets all of a Persons reservations and put them in a list
		listItems = person.getReservations();

		// If the Person do have reservations
		if( listItems != null )
		{
			if( listItems.length > 0 )
			{
				tableModel.setRowCount( 0 );

				// Creates the table an fills it with the Person's reservations
				String[] columns = new String[] { "Reservation ID", "Reservation maker", "Depature", "Destination", "Time of depature",
				        "Number of passengers", "Time of creation" };
				tableModel.setColumnIdentifiers( columns );
				tableData = new Object[listItems.length][columns.length];
				for( int i = 0; i < listItems.length; i++ )
				{
					Object object = listItems[i];
					Reservation reservation = ( Reservation ) object;
					for( int j = 0; j < columns.length; j++ )
					{
						if( j == 0 )
							tableData[i][j] = reservation.getID();
						if( j == 1 )
							tableData[i][j] = reservation.getOwner().getFirstName() + " " + reservation.getOwner().getSurName();
						if( j == 2 )
							tableData[i][j] = reservation.getFlight().getOrigin().getName();
						if( j == 3 )
							tableData[i][j] = reservation.getFlight().getDestination().getName();
						if( j == 4 )
							tableData[i][j] = reservation.getFlight().getDate().getTime();
						if( j == 5 )
							tableData[i][j] = reservation.getPassengers().length;
						if( j == 6 )
							tableData[i][j] = reservation.getReservationDate().getTime();
					}
					tableModel.addRow( tableData[i] );
				}
			}
		}
	}
}
