package MainPackage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Enables the user to search the database for reservations, persons and flights.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * @version 1.0
 */
public class FindMenu extends JFrame
{

	// Radio buttons and their panel
	private JRadioButton flightRadioButton;
	private JRadioButton reservationRadioButton;
	private JRadioButton customerRadioButton;
	private ButtonGroup radioButtons;
	private JPanel buttonPanel;

	// The criteria textfields
	private JTextField textField1;
	private JTextField textField2;
	private JTextField textField3;

	// The search results table, its data and its scrollpane
	private JTable table;
	private String[] columns;
	private Object[][] tableData;
	private DefaultTableModel tableModel;
	private JScrollPane tableScrollPane;
	private JPanel reservationsTablePanel;

	// The list of items returned from the database
	private ArrayList<?> listItems;

	// The buttons at the bottom of the window
	private JButton searchButton;
	private JButton inspectReservationButton;
	private JButton closeButton;

	/**
	 * Creates a new FindMenu
	 */
	public FindMenu()
	{
		makeContent();
		setupFrame();
	}

	/**
	 * Listens for if the Enter-key is pressed - if it is, the makeTableData is called according to which radiobutton is selected.
	 */
	class EnterListener extends KeyAdapter
	{
		private EnterListener()
		{
		}

		@Override
		public void keyTyped( KeyEvent e )
		{
			// Gets which key on the keyboard is pressed
			int key = e.getKeyChar();
			if( key == KeyEvent.VK_ENTER )
			{
				// Checks which one of the radiobuttons is selected, then calls the method makeTableData with the radiobuttons action.
				String selectedAction = radioButtons.getSelection().getActionCommand();
				makeTableData( selectedAction );
			}
		}

	}

	/**
	 * Listens for if any button, with an added ButtonActionListener is clicked, then takes action accordingly to which button is pressed.
	 * 
	 */
	private class ButtonActionListener implements ActionListener
	{
		@Override
		public void actionPerformed( ActionEvent e )
		{
			// Gets the actual buttons command and checks which of the radio buttons which is selected
			String command = e.getActionCommand();
			String selectedAction = radioButtons.getSelection().getActionCommand();

			// If the Search button or any of the radio buttons are clicked
			if( command == "Search" || command == "Flight" || command == "Reservation" || command == "Person" )
			{
				makeTableData( selectedAction );
			}

			// If the Inspect button is clicked
			if( command == "Inspect" )
			{

				// Gets the row of the table, which is chosen
				int chosenObjectIncorrectRow = table.getSelectionModel().getAnchorSelectionIndex();

				// If a row is actually marked and there are some database results
				if( chosenObjectIncorrectRow >= 0 && listItems != null )
				{

					// Converts the marked to the actually wanted row, just in case the user sorted the data by clicking the column names.
					int chosenObjectActualRow = table.convertRowIndexToModel( chosenObjectIncorrectRow );

					// Gets the ID of the object by getting the value of the first column of the chosen row
					int chosenObjectID = ( int ) tableModel.getValueAt( chosenObjectActualRow, 0 );

					if( selectedAction == "Flight" )
					{
						Flight flight = Database.getInstance().Get( chosenObjectID, Flight.class );
						new FlightInfoMenu( flight );
					}

					if( selectedAction == "Reservation" )
					{
						int reservationID = ( int ) tableModel.getValueAt( chosenObjectActualRow, 1 );
						Reservation reservation = Database.getInstance().Get( chosenObjectID, Flight.class ).getReservations()[reservationID];

						for( Passenger passenger : reservation.getPassengers() )
						{
							passenger.setPerson( Database.getInstance().Get( passenger.getPerson().getID(), Person.class ) );
						}

						reservation.setOwner( Database.getInstance().Get( reservation.getOwner().getID(), Person.class ) );
						new ReservationInfoMenu( new JFrame(), reservation, false );
					}

					if( selectedAction == "Person" )
					{
						Person person = Database.getInstance().Get( chosenObjectID, Person.class );
						new PersonInfoMenu( new JFrame(), person );
					}
				}
			}

			if( command == "Close" )
			{
				System.out.println( "Close" );
				dispose();
			}
		}
	}

	private void setupFrame()
	{
		this.setMinimumSize( new Dimension( 500, 300 ) );
		this.setPreferredSize( new Dimension( 800, 600 ) );
		this.pack();
		this.setVisible( true );
		this.setTitle( "Find Menu" );
		this.setLocationRelativeTo( null );
	}

	private void makeContent()
	{
		Container contentPane = this.getContentPane();
		contentPane.setLayout( new BoxLayout( contentPane, BoxLayout.PAGE_AXIS ) );

		flightRadioButton = new JRadioButton( "Flight", false );
		flightRadioButton.setActionCommand( "Flight" );
		flightRadioButton.setSelected( true );
		flightRadioButton.addKeyListener( new EnterListener() );
		flightRadioButton.addActionListener( new ButtonActionListener() );

		reservationRadioButton = new JRadioButton( "Reservation", false );
		reservationRadioButton.setActionCommand( "Reservation" );
		reservationRadioButton.addKeyListener( new EnterListener() );
		reservationRadioButton.addActionListener( new ButtonActionListener() );

		customerRadioButton = new JRadioButton( "Person", false );
		customerRadioButton.setActionCommand( "Person" );
		customerRadioButton.addKeyListener( new EnterListener() );
		customerRadioButton.addActionListener( new ButtonActionListener() );

		// Adds radiobuttons to a ButtonGroup to make sure, that only one radiobutton can be selected
		radioButtons = new ButtonGroup();
		radioButtons.add( flightRadioButton );
		radioButtons.add( reservationRadioButton );
		radioButtons.add( customerRadioButton );

		buttonPanel = new JPanel();
		buttonPanel.add( flightRadioButton );
		buttonPanel.add( reservationRadioButton );
		buttonPanel.add( customerRadioButton );

		JLabel criteriaLabel1 = new JLabel( "Criteria: " );
		JLabel criteriaLabel2 = new JLabel( "Criteria: " );
		JLabel criteriaLabel3 = new JLabel( "Criteria: " );

		JPanel criteriaLabelPanel = new JPanel( new GridLayout( 3, 1 ) );
		criteriaLabelPanel.add( criteriaLabel1 );
		criteriaLabelPanel.add( criteriaLabel2 );
		criteriaLabelPanel.add( criteriaLabel3 );

		textField1 = new JTextField( "" );
		textField1.addKeyListener( new EnterListener() );

		textField2 = new JTextField( "" );
		textField2.addKeyListener( new EnterListener() );

		textField3 = new JTextField( "" );
		textField3.addKeyListener( new EnterListener() );

		JPanel criteriaFieldsPanel = new JPanel( new GridLayout( 3, 1 ) );
		// criteriaFieldsPanel.setPreferredSize(new Dimension(300,50));
		criteriaFieldsPanel.add( textField1 );
		criteriaFieldsPanel.add( textField2 );
		criteriaFieldsPanel.add( textField3 );

		JPanel criteriaPanel = new JPanel( new BorderLayout() );
		criteriaPanel.setMaximumSize( new Dimension( 300, 80 ) );
		criteriaPanel.add( criteriaLabelPanel, BorderLayout.WEST );
		criteriaPanel.add( criteriaFieldsPanel, BorderLayout.CENTER );
		TitledBorder criteriaTitleBorder = BorderFactory.createTitledBorder( "Enter search criteria: " );
		criteriaPanel.setBorder( criteriaTitleBorder );

		columns = new String[] { "Columns" };
		tableData = new Object[][] { {} };
		tableModel = new DefaultTableModel( tableData, columns );
		table = new JTable( tableModel );

		tableScrollPane = new JScrollPane( table );
		tableScrollPane.setPreferredSize( new Dimension( 750, 300 ) );
		tableScrollPane.setViewportView( table );
		tableModel.removeRow( 0 );

		makeTableData( "None" );

		reservationsTablePanel = new JPanel();
		reservationsTablePanel.add( tableScrollPane );

		searchButton = new JButton( "Search" );
		searchButton.setActionCommand( "Search" );
		searchButton.addActionListener( new ButtonActionListener() );

		inspectReservationButton = new JButton( "Inspect" );
		inspectReservationButton.setActionCommand( "Inspect" );
		inspectReservationButton.addActionListener( new ButtonActionListener() );

		closeButton = new JButton( "Close" );
		closeButton.setActionCommand( "Close" );
		closeButton.addActionListener( new ButtonActionListener() );

		JPanel bottomsButtonPanel = new JPanel( new FlowLayout() );
		bottomsButtonPanel.add( searchButton );
		bottomsButtonPanel.add( inspectReservationButton );
		bottomsButtonPanel.add( closeButton );

		this.addKeyListener( new EnterListener() );

		contentPane.add( buttonPanel );
		contentPane.add( criteriaPanel );
		contentPane.add( reservationsTablePanel );
		contentPane.add( bottomsButtonPanel );
	}

	/**
	 * Generates a list of unfiltered data from a certain database table. The method uses the Database-class to generate an unsorted list of all the
	 * objects in the specified database table. When the method has finished and the list of data isn't empty, it calls the calculateResults method to
	 * filter the list. Otherwise, it calls the noSearchResults method.
	 * 
	 * @param tableToSearch
	 *            Which table in the database to search
	 */
	private void makeTableData( String tableToSearch )
	{
		// If the method is called with something to search for
		if( tableToSearch != "None" )
		{
			try
			{
				// If the reservations table is not to be searched, simply get the list of items from the database
				if( !tableToSearch.equals( "Reservation" ) )
					listItems = Database.getInstance().Get( Class.forName( "MainPackage." + tableToSearch ) );

				// If the reservations table IS to be searched, this list of items is a bit more complicated to get
				else
				{
					// An ArrayList of flights is collected from the database
					ArrayList<Flight> flights = Database.getInstance().Get( Flight.class );
					ArrayList<Reservation> reservations = new ArrayList<>();

					// For every flight in the ArrayList, get it's reservations, go through every single reservation and update this reservation's
					// owner, then add this reservation to the list of items.
					for( Flight flight : flights )
					{
						if( flight.getReservations() != null )
						{
							Reservation[] innerReservations = flight.getReservations();

							for( Reservation reservation : innerReservations )
							{
								reservation.setOwner( Database.getInstance().Get( reservation.getOwner().getID(), Person.class ) );
								reservations.add( reservation );
							}
						}
					}

					listItems = reservations;
				}

			}
			catch( ClassNotFoundException e )
			{
				e.printStackTrace();
			}

			// Fill the tableData with persons
			if( tableToSearch.equals( "Person" ) )
			{
				columns = new String[] { "Person ID", "First name", "Surname", "Phone", "Country" };
				tableModel.setColumnIdentifiers( columns );
				tableData = new Object[listItems.size()][columns.length];
				for( int i = 0; i < listItems.size(); i++ )
				{
					Object object = listItems.get( i );
					Person person = ( Person ) object;
					{
						for( int j = 0; j < columns.length; j++ )
						{
							if( j == 0 )
								tableData[i][j] = person.getID();
							if( j == 1 )
								tableData[i][j] = person.getFirstName();
							if( j == 2 )
								tableData[i][j] = person.getSurName();
							if( j == 3 )
								tableData[i][j] = person.getPhone();
							if( j == 4 )
								tableData[i][j] = person.getCountry();
						}
					}
				}
			}

			// Fill the tableData with reservations
			if( tableToSearch.equals( "Reservation" ) )
			{
				columns = new String[] { "Flight ID", "Reservation ID", "Reservation maker", "Depature", "Destination", "Time of depature",
				        "Number of passengers", "Time of creation" };
				tableModel.setColumnIdentifiers( columns );
				tableData = new Object[listItems.size()][columns.length];
				for( int i = 0; i < listItems.size(); i++ )
				{
					Object object = listItems.get( i );
					Reservation reservation = ( Reservation ) object;
					for( int j = 0; j < columns.length; j++ )
					{
						if( j == 0 )
							tableData[i][j] = reservation.getFlight().getID();
						if( j == 1 )
							tableData[i][j] = reservation.getCurrentFlightReservationIndex();
						if( j == 2 )
							tableData[i][j] = reservation.getOwner().getFirstName() + " " + reservation.getOwner().getSurName();
						if( j == 3 )
							tableData[i][j] = reservation.getFlight().getOrigin().getName();
						if( j == 4 )
							tableData[i][j] = reservation.getFlight().getDestination().getName();
						if( j == 5 )
							tableData[i][j] = reservation.getFlight().getDate().getTime();
						if( j == 6 )
							tableData[i][j] = reservation.getPassengers().length;
						if( j == 7 )
							tableData[i][j] = reservation.getReservationDate().getTime();
					}
				}
			}

			// Fill the tableData with flights
			if( tableToSearch.equals( "Flight" ) )
			{
				columns = new String[] { "Flight ID", "Destination", "Date of depature", "Available seats" };
				tableModel.setColumnIdentifiers( columns );
				tableData = new Object[listItems.size()][columns.length];
				for( int i = 0; i < listItems.size(); i++ )
				{
					Object object = listItems.get( i );
					Flight flight = ( Flight ) object;
					for( int j = 0; j < columns.length; j++ )
					{
						if( j == 0 )
							tableData[i][j] = flight.getID();
						if( j == 1 )
							tableData[i][j] = flight.getDestination().getName();
						if( j == 2 )
							tableData[i][j] = flight.getDate().getTime();
						if( j == 3 )
							tableData[i][j] = flight.getSeatsLeft();
					}
				}
			}
			// Filter the results according to the search criterias and add them to the table
			calculateResults();

		}

		// If the method is called with nothing to find
		else
		{
			noSearchResults();
		}

		// Sets up the tables properties and adds a sorting function
		table.setColumnSelectionAllowed( false );
		table.setCellSelectionEnabled( false );
		table.setRowSelectionAllowed( true );
		table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		table.getTableHeader().setReorderingAllowed( false );
		table.setFillsViewportHeight( true );
		table.setAutoCreateRowSorter( true );
	}

	/**
	 * Filters the search results according to the criterias given in the textfields, and adds the results to the table. If no results is added to the
	 * table, the noSearchResults method is called.
	 */
	private void calculateResults()
	{
		// Removes the current contents of the table
		tableModel.setRowCount( 0 );

		String[] sCriterias = { textField1.getText().toLowerCase(), textField2.getText().toLowerCase(), textField3.getText().toLowerCase() };

		int height = 0;

		// Filters the search results and adds these to the table, if search is a match
		for( int i = 0; i < tableData.length; i++ )
		{
			for( int j = 0; j < columns.length; j++ )
			{
				if( tableData[i][j].toString().toLowerCase().contains( sCriterias[0] )
				        && tableData[i][j].toString().toLowerCase().contains( sCriterias[1] )
				        && tableData[i][j].toString().toLowerCase().contains( sCriterias[2] ) )
				{
					tableModel.addRow( tableData[i] );
					j = columns.length;
				}
			}

		}

		height = tableModel.getRowCount() * 17;
		table.setPreferredSize( new Dimension( 300, height ) );

		if( tableModel.getRowCount() == 0 )
			noSearchResults();
	}

	/**
	 * noSearchResults changes the table to have no results shown.
	 */
	private void noSearchResults()
	{
		tableModel.setColumnIdentifiers( columns );
		tableData = new Object[][] { { "No results" } };
		tableModel.addRow( tableData[0] );
	}
}
