package MainPackage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

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
	private Object[][] tableData;
	private DefaultTableModel tableModel;
	private JScrollPane tableScrollPane;
	private JPanel reservationsTablePanel;

	private ArrayList<?> listItems;

	public JespersFindDialog()
	{
		setupFrame();
		makeContent();
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
				if(flightRadioButton.isSelected())
				{
					calculateResults("Flight");
					System.out.println("Flight search");
				}
				if(reservationRadioButton.isSelected())
				{
					calculateResults("Reservation");
					System.out.println("Reservation search");
				}
				if(customerRadioButton.isSelected())
				{
					calculateResults("Person");
					System.out.println("Customer search");
				}
			}
		}
	}

	private void setupFrame()
	{
		this.setSize( new Dimension( 800, 600 ) );
		this.setLocationRelativeTo( null );
		this.setVisible(true);
		this.requestFocusInWindow();
	}

	private void makeContent()
	{
		Container contentPane = this.getContentPane();
		//contentPane.setLayout( new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		contentPane.setLayout( new FlowLayout());

		flightRadioButton = new JRadioButton("Flight",false);
		flightRadioButton.setActionCommand("Flight");
		flightRadioButton.setSelected(true);
		flightRadioButton.addKeyListener(new EnterListener());

		reservationRadioButton = new JRadioButton("Reservation",false);
		reservationRadioButton.setActionCommand("Reservation");
		reservationRadioButton.addKeyListener(new EnterListener());

		customerRadioButton = new JRadioButton("Customer",false);
		customerRadioButton.setActionCommand("Customer");
		customerRadioButton.addKeyListener(new EnterListener());

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
		criteriaPanel.setPreferredSize(new Dimension(300,80));
		criteriaPanel.add(criteriaLabelPanel, BorderLayout.WEST);
		criteriaPanel.add(criteriaFieldsPanel, BorderLayout.CENTER);
		TitledBorder criteriaTitleBorder = BorderFactory.createTitledBorder("Enter search criteria: ");
		criteriaPanel.setBorder(criteriaTitleBorder);

		makeTable("None");

		reservationsTablePanel = new JPanel();
		reservationsTablePanel.add(tableScrollPane);

		this.addKeyListener(new EnterListener());

		contentPane.add(buttonPanel);
		contentPane.add(criteriaPanel);
		contentPane.add(reservationsTablePanel);
	}

	private void calculateResults(String tableToSearch)
	{
		System.out.println("Calculate results");
		//ResultSet results = Database.getInstance().executeQuery( "SELECT * FROM " + tableToSearch );
		try {
			listItems = Database.getInstance().Get(Class.forName("MainPackage."+tableToSearch));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//ArrayList<Object>
		String[] sCriterias = { textField1.getText().toLowerCase(),  textField2.getText().toLowerCase(), textField3.getText().toLowerCase() };

		//		try {
		//			System.out.println("Try");
		//			for(Object object : listItems)
		//			{
		//				
		//			}
		//			
		//		} catch (SQLException e) {
		//			e.printStackTrace();
		//		}

		makeTable(tableToSearch);
	}

	private void makeTable(String tableToSearch)
	{

		//Resetter tabellen
		System.out.println(listItems);

		if(tableToSearch.equals("None") || listItems == null){
			System.out.println("Empty listItems");
			String[] columns = {"Search..."};
			tableData = new Object[][]{{"No results"}};
			table = new JTable(new DefaultTableModel(tableData, columns));
		}
		else {

			if(tableToSearch.equals("Person")) {						
				String[] columns = {"First name","Surname","Phone","Country"};
				tableData = new Object[listItems.size()][columns.length];
				for (Object object : listItems) {
					Person person = (Person) object;{
						for (int i = 0; i < listItems.size(); i++) {
							for (int j = 0; j < columns.length; j++) {
								if(j%columns.length == 0)
									tableData[i][j] = person.getFirstName();							
								if(j%columns.length == 1)
									tableData[i][j] = person.getSurName();	
								if(j%columns.length == 2)
									tableData[i][j] = person.getPhone();
								if(j%columns.length == 3)
									tableData[i][j] = person.getCountry();
							}
							System.out.println(tableData[0]);
							table = new JTable(new DefaultTableModel(tableData, columns));
						}			
					}
				}
			}

			if(tableToSearch.equals("Reservation")) {
				String[] columns = {"Reservation maker","Destination","Time of depature", "Number of passengers","Time of creation"};
				tableData = new Object[listItems.size()][columns.length];
				for (Object object : listItems) {
					Reservation reservation = (Reservation) object;
					for (int i = 0; i < listItems.size(); i++) {
						for (int j = 0; j < columns.length; j++) {
							if(j%columns.length == 0)
								tableData[i][j] = reservation.getOwner();
							if(j%columns.length == 1)
								tableData[i][j] = reservation.getFlight().getDestination();
							if(j%columns.length == 2)
								tableData[i][j] = reservation.getFlight().getDate();
							if(j%columns.length == 3)
								tableData[i][j] = reservation.getPassengers().length;
							if(j%columns.length == 4)
								tableData[i][j] = reservation.getReservationDate();
						}
					}			
				}
				table = new JTable(new DefaultTableModel(tableData, columns));
			}

			if(tableToSearch.equals("Flight")) {
				String[] columns = {"Destination","Date of depature","Available seats"};
				tableData = new Object[listItems.size()][columns.length];
				for (Object object : listItems) {
					Flight flight = (Flight) object;
					for (int row = 0; row < listItems.size(); row++) {
						for (int col = 0; col < columns.length; col++) {
							if(col%columns.length == 0)
								tableData[row][col] = flight.getDestination().getName();
							if(col%columns.length == 1)
								tableData[row][col] = flight.getDate();	
							if(col%columns.length == 2)
								tableData[row][col] = flight.getSeatsLeft();
						}
					}	
				}
				table = new JTable(new DefaultTableModel(tableData, columns));
			}
		}

		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(false);
		table.setRowSelectionAllowed(true);
		table.getTableHeader().setReorderingAllowed(false);
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);

		tableScrollPane = new JScrollPane(table);
		tableScrollPane.setPreferredSize(new Dimension(200,300));
		tableModel = (DefaultTableModel) table.getModel();

		updateTable();	
	}

	private void updateTable()
	{
		this.tableScrollPane.setViewportView(table);
		this.tableScrollPane.validate();
	}
	
	private void makeTestData()
	{
		listItems = new ArrayList<>();
		Person person1 = new Person("Jesper", "Nysteen", "Male", "06-04-1991", "Denmark"
				, "Danish", "Skaffervej 15, 3 tv", "31225525", "234234324234", 1);
		
		Passenger passenger1 = new Passenger(person1, new Seat(2, 2));
		
		Object passenger1Object = ( Object ) passenger1;
		
		//listItems.add(passenger1Object);
	}
}
