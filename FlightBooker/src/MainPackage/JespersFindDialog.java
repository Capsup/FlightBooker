package MainPackage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
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

public class JespersFindDialog extends JFrame {

	private JRadioButton flightRadioButton;
	private JRadioButton reservationRadioButton;
	private JRadioButton customerRadioButton;
	private ButtonGroup radioButtons;
	private JPanel buttonPanel;

	private JTextField textField1;
	private JTextField textField2;
	private JTextField textField3;

	private JTable table;
	private String[] columns;
	private Object[][] tableData;
	private DefaultTableModel tableModel;
	private JScrollPane tableScrollPane;
	private JPanel reservationsTablePanel;

	private ArrayList<?> listItems;

	private JButton searchButton;
	private JButton inspectReservationButton;
	private JButton closeButton;

	public JespersFindDialog()
	{

		makeContent();
		setupFrame();

	}

	class EnterListener extends KeyAdapter
	{
		private EnterListener()
		{
		}

		@Override
		public void keyTyped(KeyEvent e)
		{
			int key = e.getKeyChar();
			if(key == KeyEvent.VK_ENTER){
				String selectedAction = radioButtons.getSelection().getActionCommand();
				System.out.println(selectedAction);
				makeTableData(selectedAction);
			}
		}

	}

	private class ButtonActionListener implements ActionListener
	{
		@Override
		public void actionPerformed( ActionEvent e )
		{
			String command = e.getActionCommand();
			String selectedAction = radioButtons.getSelection().getActionCommand();
			
			if( command == "Search" || command == "Flight" || command == "Reservation" || command == "Person") {
				makeTableData(selectedAction);
			}

			if( command == "Inspect") {
				System.out.println("Inspect");
				int chosenObjectIncorrectRow = table.getSelectionModel().getAnchorSelectionIndex();
				
				if(chosenObjectIncorrectRow >= 0 && listItems != null){
					int chosenObjectActualRow = table.convertRowIndexToModel(chosenObjectIncorrectRow);
					int chosenObjectID = (int) tableModel.getValueAt(chosenObjectActualRow, 0);
					System.out.println(chosenObjectID);
					
					if(selectedAction == "Flight")
					{
						Flight flight = Database.getInstance().Get(chosenObjectID, Flight.class);
						new FlightInfoMenu(flight);
					}
					
					if(selectedAction == "Reservation")
					{
						int chosenObjectID2 = (int) tableModel.getValueAt(chosenObjectActualRow, 1);
						
						//Reservation reservation = Database.getInstance().Get( Database.getInstance().Get(chosenObjectID, Reservation.class).getFlight().getID(), Flight.class ).getReservations()[chosenObjectID-1]; 
						Reservation reservation = Database.getInstance().Get(chosenObjectID, Flight.class).getReservations()[chosenObjectID2]; 
						//reservation.setFlight( Database.getInstance().Get( reservation.getFlight().getID(), Flight.class ));
						//Database.getInstance().Replace( reservation.getFlight().getID(), reservation.getFlight() );
						new ReservationInfoMenu(new JFrame(), reservation, false);
					}
					
					if(selectedAction == "Person")
					{
					
						Person person = Database.getInstance().Get(chosenObjectID, Person.class);
						new PassengerInformationMenu(new JFrame(), person);
					}
				}
			}

			if( command == "Close") {
				System.out.println("Close");
				dispose();
			}
		}
	}

	private void setupFrame()
	{
		this.setMinimumSize(new Dimension(500,300));
		this.setPreferredSize(new Dimension(800,600));
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo( null );
	}

	private void makeContent()
	{
		Container contentPane = this.getContentPane();
		contentPane.setLayout( new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

		flightRadioButton = new JRadioButton("Flight",false);
		flightRadioButton.setActionCommand("Flight");
		flightRadioButton.setSelected(true);
		flightRadioButton.addKeyListener(new EnterListener());
		flightRadioButton.addActionListener(new ButtonActionListener());

		reservationRadioButton = new JRadioButton("Reservation",false);
		reservationRadioButton.setActionCommand("Reservation");
		reservationRadioButton.addKeyListener(new EnterListener());
		reservationRadioButton.addActionListener(new ButtonActionListener());

		customerRadioButton = new JRadioButton("Person",false);
		customerRadioButton.setActionCommand("Person");
		customerRadioButton.addKeyListener(new EnterListener());
		customerRadioButton.addActionListener(new ButtonActionListener());

		radioButtons = new ButtonGroup();
		radioButtons.add(flightRadioButton);
		radioButtons.add(reservationRadioButton);
		radioButtons.add(customerRadioButton);    	

		buttonPanel = new JPanel();
		buttonPanel.add(flightRadioButton);
		buttonPanel.add(reservationRadioButton);	
		buttonPanel.add(customerRadioButton);

		JLabel criteriaLabel1 = new JLabel( "Criteria: " );
		JLabel criteriaLabel2 = new JLabel( "Criteria: " );
		JLabel criteriaLabel3 = new JLabel( "Criteria: " );

		JPanel criteriaLabelPanel = new JPanel(new GridLayout(3, 1));
		criteriaLabelPanel.add(criteriaLabel1);
		criteriaLabelPanel.add(criteriaLabel2);
		criteriaLabelPanel.add(criteriaLabel3);

		textField1 = new JTextField("");
		textField1.addKeyListener(new EnterListener());

		textField2 = new JTextField("");
		textField2.addKeyListener(new EnterListener());

		textField3 = new JTextField("");
		textField3.addKeyListener(new EnterListener());

		JPanel criteriaFieldsPanel = new JPanel(new GridLayout(3, 1));
		//criteriaFieldsPanel.setPreferredSize(new Dimension(300,50));
		criteriaFieldsPanel.add(textField1);
		criteriaFieldsPanel.add(textField2);
		criteriaFieldsPanel.add(textField3);

		JPanel criteriaPanel = new JPanel(new BorderLayout());
		criteriaPanel.setMaximumSize(new Dimension(300,80));
		criteriaPanel.add(criteriaLabelPanel, BorderLayout.WEST);
		criteriaPanel.add(criteriaFieldsPanel, BorderLayout.CENTER);
		TitledBorder criteriaTitleBorder = BorderFactory.createTitledBorder("Enter search criteria: ");
		criteriaPanel.setBorder(criteriaTitleBorder);

		columns = new String[]{"Columns"};
		tableData = new Object[][]{{}};
		tableModel = new DefaultTableModel(tableData,columns);
		table = new JTable(tableModel);

		tableScrollPane = new JScrollPane(table);
		tableScrollPane.setMaximumSize(new Dimension(1000,1000));
//		tableScrollPane.setMinimumSize(new Dimension(700,100));
//		tableScrollPane.setMaximumSize(new Dimension(1000,1000));
//		tableScrollPane.setPreferredSize(new Dimension(750,300));
		tableScrollPane.setViewportView(table);
		tableModel.removeRow(0);

		makeTableData("None");

		reservationsTablePanel = new JPanel();
		reservationsTablePanel.add(tableScrollPane);

		searchButton = new JButton("Search");
		searchButton.setActionCommand("Search");
		searchButton.addActionListener(new ButtonActionListener());

		inspectReservationButton = new JButton("Inspect");
		inspectReservationButton.setActionCommand("Inspect");
		inspectReservationButton.addActionListener(new ButtonActionListener());

		closeButton = new JButton("Close");
		closeButton.setActionCommand("Close");
		closeButton.addActionListener(new ButtonActionListener());

		JPanel bottomsButtonPanel = new JPanel(new FlowLayout());
		bottomsButtonPanel.add(searchButton);
		bottomsButtonPanel.add(inspectReservationButton);
		bottomsButtonPanel.add(closeButton);

		this.addKeyListener(new EnterListener());

		contentPane.add(buttonPanel);
		contentPane.add(criteriaPanel);
		contentPane.add(reservationsTablePanel);
		contentPane.add(bottomsButtonPanel);
	}

	private void makeTableData(String tableToSearch)
	{	
		//Hvis metoden bliver kaldt med noget at søge efter
		if(tableToSearch != "None") {
			try {
				if( !tableToSearch.equals( "Reservation" ) )
					listItems = Database.getInstance().Get(Class.forName("MainPackage."+tableToSearch));
				else {
					ArrayList<Flight> flights = Database.getInstance().Get(Flight.class);
					ArrayList<Reservation> reservations = new ArrayList<>();
					
					for (Flight flight : flights) 
					{
						if(flight.getReservations() != null)
						{
							Reservation[] innerReservations = flight.getReservations();
							
							for (Reservation reservation : innerReservations) {
								reservations.add(reservation);
							}
						}
					}
					
					listItems = reservations;
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if(tableToSearch.equals("Person")) {						
				columns = new String[]{"Person ID","First name","Surname","Phone","Country"};
				tableModel.setColumnIdentifiers(columns);
				tableData = new Object[listItems.size()][columns.length];
				for (int i = 0; i < listItems.size(); i++) {
					Object object = listItems.get(i);
					Person person = (Person) object;{
						for (int j = 0; j < columns.length; j++) {
							if(j%columns.length == 0)
								tableData[i][j] = person.getID();
							if(j%columns.length == 1)
								tableData[i][j] = person.getFirstName();							
							if(j%columns.length == 2)
								tableData[i][j] = person.getSurName();	
							if(j%columns.length == 3)
								tableData[i][j] = person.getPhone();
							if(j%columns.length == 4)
								tableData[i][j] = person.getCountry();
						}
					}
				}	
			}

			if(tableToSearch.equals("Reservation")) {
				columns = new String[] {"Flight ID", "Reservation ID","Reservation maker","Depature", "Destination","Time of depature", "Number of passengers","Time of creation"};
				tableModel.setColumnIdentifiers(columns);
				tableData = new Object[listItems.size()][columns.length];
				for (int i = 0; i < listItems.size(); i++) {
					Object object = listItems.get(i);
					Reservation reservation = (Reservation) object;
					for (int j = 0; j < columns.length; j++) {
						if(j%columns.length == 0)
							tableData[i][j] = reservation.getFlight().getID();
						if(j%columns.length == 1)
							tableData[i][j] = reservation.getCurrentFlightReservationIndex();
						if(j%columns.length == 2)
							tableData[i][j] = reservation.getOwner().getFirstName() + " " + reservation.getOwner().getSurName();
						if(j%columns.length == 3)
							tableData[i][j] = reservation.getFlight().getOrigin().getName();
						if(j%columns.length == 4)
							tableData[i][j] = reservation.getFlight().getDestination().getName();
						if(j%columns.length == 5)
							tableData[i][j] = reservation.getFlight().getDate().getTime();
						if(j%columns.length == 6)
							tableData[i][j] = reservation.getPassengers().length;
						if(j%columns.length == 7)
							tableData[i][j] = reservation.getReservationDate().getTime();
					}
				}
			}

			if(tableToSearch.equals("Flight")) {
				columns = new String[] {"Flight ID", "Destination","Date of depature","Available seats"};
				tableModel.setColumnIdentifiers(columns);
				tableData = new Object[listItems.size()][columns.length];
				for (int i = 0; i < listItems.size(); i++) {
					Object object = listItems.get(i);
					Flight flight = (Flight) object;
					for (int j = 0; j < columns.length; j++) {
						if(j%columns.length == 0)
							tableData[i][j] = flight.getID();
						if(j%columns.length == 1)
							tableData[i][j] = flight.getDestination().getName();
						if(j%columns.length == 2)
							tableData[i][j] = flight.getDate().getTime();
						if(j%columns.length == 3)
							tableData[i][j] = flight.getSeatsLeft();
					}
				}
			}
			calculateResults();

		}

		//Hvis metoden ikke får noget at søge efter
		else {
			noSearchResults();
		}

		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(false);
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);

	}

	private void calculateResults()
	{
		//Resetter tabellens indhold
		tableModel.setRowCount(0);

		String[] sCriterias = { textField1.getText().toLowerCase(),  textField2.getText().toLowerCase(), textField3.getText().toLowerCase() };

		for (int i = 0; i < tableData.length; i++) {
			for (int j = 0; j < columns.length; j++) {
				if(tableData[i][j].toString().toLowerCase().contains( sCriterias[0] ) && tableData[i][j].toString().toLowerCase().contains( sCriterias[1] ) && tableData[i][j].toString().toLowerCase().contains( sCriterias[2] ))
				{
					tableModel.addRow(tableData[i]);
					j = columns.length;
				}
			}

		}
		if(tableModel.getRowCount() == 0)
			noSearchResults();
	}

	private void noSearchResults()
	{
		tableModel.setColumnIdentifiers(columns);
		tableData = new Object[][]{{"No results"}};
		tableModel.addRow(tableData[0]);
	}
}