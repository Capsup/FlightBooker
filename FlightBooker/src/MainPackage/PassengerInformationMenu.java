package MainPackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;

public class PassengerInformationMenu {

	private JFrame frame;
	private JPanel mainPanel;

	private Passenger passenger;	

	private JButton editButton;
	private JButton inspectReservationButton;
	private JButton closeButton;

	private JTable reservationsTable;

	public PassengerInformationMenu(JFrame frame/*, Passenger passenger */)
	{
		//this.passenger = passenger;
		update();

		this.frame = frame;
		setupFrame();
		//setupFonts();

		makeContent();
		frame.setVisible(true);
	}

	private class actionListener implements ActionListener
	{
		@Override
		public void actionPerformed( ActionEvent e )
		{
			String command = e.getActionCommand();

			if( command == "Edit information") {
				System.out.println("Edit information");
				new PassengerInformationEditor(passenger.getPerson());
			}

			if( command == "Inspect reservation") {
				System.out.println("Inspect reservation");
				System.out.println(reservationsTable.getSelectionModel().getAnchorSelectionIndex());
			}

			if( command == "Close") {
				System.out.println("Close");
				frame.dispose();
			}

		}
	}


	private void setupFrame()
	{
		frame.setSize(500, 700);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		frame.addWindowListener( 
				new WindowAdapter() { 
					public void windowActivated(WindowEvent e) {
						update();
					} 
				} 
				);
	}

	private void makeContent()
	{
		Container contentPane = frame.getContentPane();
		contentPane.setLayout( null );

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setBounds( 10, 40, frame.getWidth() - 25, frame.getHeight() - 40 * 2 - 20 );

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

		JTextField nameTextField = new JTextField(passenger.getPerson().getFirstName());
		JTextField genderTextField = new JTextField(passenger.getPerson().getSurName());
		JTextField birthTextField = new JTextField(passenger.getPerson().getDateOfBirth());
		JTextField countryTextField = new JTextField(passenger.getPerson().getCountry());
		JTextField nationaTextField = new JTextField(passenger.getPerson().getNationality());
		JTextField adressTextField = new JTextField(passenger.getPerson().getAdress());
		JTextField phoneTextField = new JTextField(passenger.getPerson().getPhone());
		JTextField passporTextField = new JTextField(passenger.getPerson().getPassportNumber());

		JTextField[] infoFields = new JTextField[]{nameTextField, genderTextField, birthTextField, countryTextField, 
				nationaTextField, adressTextField, phoneTextField, passporTextField};

		for(JTextField field : infoFields)
			field.setEditable(false);

		JPanel infoFieldsPanel = new JPanel(new GridLayout(infoFields.length, 1));

		for(JTextField field : infoFields)
			infoFieldsPanel.add(field);

		JPanel passengerButtonPanel = new JPanel();
		passengerButtonPanel.setLayout(new FlowLayout());

		editButton = new JButton( "Edit information" );
		editButton.setActionCommand( "Edit information" );
		editButton.addActionListener( new actionListener() );

		passengerButtonPanel.add( editButton );		

		JPanel passengerInfoPanel = new JPanel(new BorderLayout());
		passengerInfoPanel.add(infoLabelsPanel, BorderLayout.WEST);
		passengerInfoPanel.add(infoFieldsPanel, BorderLayout.CENTER);
		passengerInfoPanel.add(passengerButtonPanel, BorderLayout.SOUTH);
		TitledBorder passengerTitleBorder = BorderFactory.createTitledBorder("Passenger details");
		passengerInfoPanel.setBorder(passengerTitleBorder);

		//Definerer data til reservationsTable
		String[] columns = {"Date of reservation","Destination","Passengers"};

		Object[][] reservationsData = makeReservationData();

		//Laver reservationsTable
		reservationsTable = new JTable(reservationsData, columns);

		//Sætter reservationsTable's specifikke egenskaber
		reservationsTable.setColumnSelectionAllowed(false);
		reservationsTable.setCellSelectionEnabled(false);
		reservationsTable.setRowSelectionAllowed(true);
		reservationsTable.getTableHeader().setReorderingAllowed(false);
		reservationsTable.setFillsViewportHeight(true);

		//Tilføjer en sorteringsfunktion til tablen
		reservationsTable.setAutoCreateRowSorter(true);

		//Tilføjer et scrollpane til reservationsTable
		JScrollPane reservationsScrollPane = new JScrollPane(reservationsTable);
		reservationsScrollPane.setPreferredSize(new Dimension(mainPanel.getWidth()-10,200));

		//Tilføjer en border til reservationsTablen
		TitledBorder reservationsTitleBorder = BorderFactory.createTitledBorder("Passenger reservations");
		reservationsScrollPane.setBorder(reservationsTitleBorder);

		JPanel reservationsButtonPanel = new JPanel();
		reservationsButtonPanel.setLayout(new FlowLayout());

		inspectReservationButton = new JButton( "Inspect reservation" );
		inspectReservationButton.setActionCommand( "Inspect reservation" );
		inspectReservationButton.addActionListener( new actionListener() );

		closeButton = new JButton( "Close" );
		closeButton.setActionCommand( "Close" );
		closeButton.addActionListener( new actionListener() );

		reservationsButtonPanel.add( inspectReservationButton );
		reservationsButtonPanel.add( closeButton );

		mainPanel.add(passengerInfoPanel);
		mainPanel.add(reservationsScrollPane);
		mainPanel.add(reservationsButtonPanel);

		frame.add( mainPanel );
	}

	private void update()
	{
		System.out.println("Update");
		//database passenger.getPerson() for at opdatere ændrede persondata
		this.passenger = Database.getInstance().Get(1, Passenger.class);
	}

	private Object[][] makeReservationData()
	{
		Object[][] reservationsData = {
				{new Date(34567887), "CPH",
					new Integer(5)}, 
					{new Date(System.currentTimeMillis()), "ROFL",
						new Integer(1)},
						{new Date(System.currentTimeMillis()), "LOL",
							new Integer(3)},
							{new Date(System.currentTimeMillis()), "JFK",
								new Integer(1)},
								{new Date(System.currentTimeMillis()), "JFK",
									new Integer(1)},
									{new Date(System.currentTimeMillis()), "JFK",
										new Integer(1)},
										{new Date(System.currentTimeMillis()), "JFK",
											new Integer(1)},
											{new Date(System.currentTimeMillis()), "JFK",
												new Integer(1)},
												{new Date(System.currentTimeMillis()), "JFK",
													new Integer(1)},
													{new Date(System.currentTimeMillis()), "JFK",
														new Integer(1)},
														{new Date(System.currentTimeMillis()), "JFK",
															new Integer(1)},
															{new Date(System.currentTimeMillis()), "JFK",
																new Integer(1)},
																{new Date(System.currentTimeMillis()), "JFK",
																	new Integer(1)},
																	{new Date(System.currentTimeMillis()), "JFK",
																		new Integer(1)},
		};
		return reservationsData;
	}
}
