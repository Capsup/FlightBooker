package MainPackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;

public class PassengerInformationMenu {

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

	public PassengerInformationMenu(JFrame frame, Person person )
	{
		this.person = person;
		this.frame = frame;
		setupFrame();
		makeContent();
	}

	private class actionListener implements ActionListener
	{
		@Override
		public void actionPerformed( ActionEvent e )
		{
			String command = e.getActionCommand();

			if( command == "Edit information") {
				System.out.println("Edit information");
				new PassengerInformationEditor(person);
			}

			if( command == "Inspect reservation") {
				System.out.println("Inspect reservation");
				System.out.println(reservationsTable.getSelectionModel().getAnchorSelectionIndex());


				int chosenObjectIncorrectRow = reservationsTable.getSelectionModel().getAnchorSelectionIndex();
				if(chosenObjectIncorrectRow >= 0 && listItems != null){
					{
						if(chosenObjectIncorrectRow >= 0){
							int chosenObjectActualRow = reservationsTable.convertRowIndexToModel(chosenObjectIncorrectRow);
							int chosenObjectID = (int) reservationsTable.getValueAt(chosenObjectActualRow, 0);
							System.out.println(chosenObjectID);
							Reservation reservation = Database.getInstance().Get(chosenObjectID, Reservation.class);
							new ReservationInfoMenu(new JFrame(), reservation, false);
						}
					}
				}
			}
			if( command == "Close") {
				System.out.println("Close");
				frame.dispose();
			}

		}
	}


	private void setupFrame()
	{
		frame.setSize(800, 700);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Passenger Information");
		frame.setVisible(true);

		frame.addWindowListener( 
				new WindowAdapter() { 
					public void windowActivated(WindowEvent e) {
						if(person != null){
							updatePerson();
							updatePersonTextFields();
						}
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
		//mainPanel.setLayout(new FlowLayout());
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

		nameTextField = new JTextField();
		genderTextField = new JTextField();
		birthTextField = new JTextField();
		countryTextField = new JTextField();
		nationaTextField = new JTextField();
		adressTextField = new JTextField();
		phoneTextField = new JTextField();
		passporTextField = new JTextField();
		updatePersonTextFields();

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
		passengerInfoPanel.setPreferredSize(new Dimension(11,11));
		passengerInfoPanel.add(infoLabelsPanel, BorderLayout.WEST);
		passengerInfoPanel.add(infoFieldsPanel, BorderLayout.CENTER);
		passengerInfoPanel.add(passengerButtonPanel, BorderLayout.SOUTH);
		TitledBorder passengerTitleBorder = BorderFactory.createTitledBorder("Passenger details");
		passengerInfoPanel.setBorder(passengerTitleBorder);

		makeReservationTable();

		//Sætter reservationsTable's specifikke egenskaber
		reservationsTable.setColumnSelectionAllowed(false);
		reservationsTable.setCellSelectionEnabled(false);
		reservationsTable.setRowSelectionAllowed(true);
		reservationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

	private void updatePersonTextFields()
	{
		System.out.println("updatePersonTextFields");
		nameTextField.setText(person.getFirstName() + " " + person.getSurName());
		genderTextField.setText(person.getGender());
		birthTextField.setText(person.getDateOfBirth());
		countryTextField.setText(person.getCountry());
		nationaTextField.setText(person.getNationality());
		adressTextField.setText(person.getAdress());
		phoneTextField.setText(person.getPhone());
		passporTextField.setText(person.getPassportNumber());
	}

	private void updatePerson()
	{
		person = Database.getInstance().Get(person.getCustomerID(), Person.class);
	}

	private void makeReservationTable()
	{		
		columns = new String[]{" "};
		tableData = new Object[][]{{"No reservations found"}};
		tableModel = new DefaultTableModel(tableData,columns);
		reservationsTable = new JTable(tableModel);

		listItems = person.getReservations();

		if( listItems != null )
		{
			if(listItems.length > 0 ){
				tableModel.setRowCount(0);

				String[] columns = new String[] {"Reservation ID", "Reservation maker","Depature", "Destination","Time of depature", "Number of passengers","Time of creation"};
				tableModel.setColumnIdentifiers(columns);
				tableData = new Object[listItems.length][columns.length];
				for (int i = 0; i < listItems.length; i++) {
					Object object = listItems[i];
					Reservation reservation = (Reservation) object;
					for (int j = 0; j < columns.length; j++) {
						if(j%columns.length == 0)
							tableData[i][j] = reservation.getID();
						if(j%columns.length == 1)
							tableData[i][j] = reservation.getOwner().getFirstName() + " " + reservation.getOwner().getSurName();
						if(j%columns.length == 2)
							tableData[i][j] = reservation.getFlight().getOrigin().getName();
						if(j%columns.length == 3)
							tableData[i][j] = reservation.getFlight().getDestination().getName();
						if(j%columns.length == 4)
							tableData[i][j] = reservation.getFlight().getDate().getTime();
						if(j%columns.length == 5)
							tableData[i][j] = reservation.getPassengers().length;
						if(j%columns.length == 6)
							tableData[i][j] = reservation.getReservationDate().getTime();
					}
					tableModel.addRow(tableData[i]);
				}
			}
		}
	}
}
