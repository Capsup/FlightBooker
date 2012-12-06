package MainPackage;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

@SuppressWarnings( "serial" )
class FindDialog extends JFrame
{
	public static int LABELOFFSET = 100;

	private JPanel buttonPanel;
	private ButtonGroup radioButtons;  
	private JRadioButton flightRadioButton, reservationRadioButton, customerRadioButton;
	private JLabel criteriaLabel1, criteriaLabel2, criteriaLabel3;
	private JTextField textField1, textField2, textField3;
	private JList<String> list;
	private JScrollPane scrollPane;
	private DefaultListModel<String> listItems;

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
				System.out.println("Enter pressed");
				if(flightRadioButton.isSelected())
				{
					CalculateResults();
					System.out.println("Flight search");
				}
				if(reservationRadioButton.isSelected())
				{
					CalculateResults();
					System.out.println("Reservation search");
				}
				if(customerRadioButton.isSelected())
				{
					CalculateResults();
					System.out.println("Customer search");
				}
			}
		}
	}

	public FindDialog()
	{
		Dimension dScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Container pane = getContentPane();
		SpringLayout layout = new SpringLayout();
		float fScale = ( dScreenSize.width / dScreenSize.height ) / ( 4 / 3 );

		pane.setLayout( layout );

		//this.setLocation( (int) ( dScreenSize.width * 0.5f - ( iSizeX * 0.5f )), (int) (dScreenSize.height * 0.5f - ( iSizeY * 0.5f )) );

		this.setSize( new Dimension( 800, 600 ) );
		this.setLocationRelativeTo( null );
		this.setVisible(true);
		this.requestFocusInWindow();


		criteriaLabel1 = new JLabel( "Criteria: " );
		pane.add( criteriaLabel1 );
		layout.putConstraint( SpringLayout.WEST, criteriaLabel1, ( int ) ( this.getWidth() / 2 - ( LABELOFFSET * fScale ) ), SpringLayout.WEST, pane );
		layout.putConstraint( SpringLayout.NORTH, criteriaLabel1, ( int ) ( 40 * fScale ), SpringLayout.NORTH, pane );

		textField1 = new JTextField("");
		pane.add( textField1 );
		layout.putConstraint( SpringLayout.WEST, textField1, ( int ) ( 10 * fScale ), SpringLayout.EAST, criteriaLabel1 );
		layout.putConstraint( SpringLayout.NORTH, textField1, 0, SpringLayout.NORTH, criteriaLabel1 );
		layout.putConstraint( SpringLayout.EAST, textField1, ( int ) ( -350 * fScale ), SpringLayout.EAST, pane );
		textField1.addKeyListener(new EnterListener());

		criteriaLabel2 = new JLabel( "Criteria: " );
		pane.add( criteriaLabel2 );
		layout.putConstraint( SpringLayout.WEST, criteriaLabel2, ( int ) ( this.getWidth() / 2 - ( LABELOFFSET * fScale ) ), SpringLayout.WEST, pane );
		layout.putConstraint( SpringLayout.NORTH, criteriaLabel2, ( int ) ( 10 * fScale ), SpringLayout.SOUTH, criteriaLabel1 );

		textField2 = new JTextField("");
		pane.add( textField2 );
		layout.putConstraint( SpringLayout.WEST, textField2, ( int ) ( 10 * fScale ), SpringLayout.EAST, criteriaLabel2 );
		layout.putConstraint( SpringLayout.NORTH, textField2, 0, SpringLayout.NORTH, criteriaLabel2 );
		layout.putConstraint( SpringLayout.EAST, textField2, ( int ) ( -350 * fScale ), SpringLayout.EAST, pane );
		textField2.addKeyListener(new EnterListener());

		criteriaLabel3 = new JLabel( "Criteria: " );
		pane.add( criteriaLabel3 );
		layout.putConstraint( SpringLayout.WEST, criteriaLabel3, ( int ) ( this.getWidth() / 2 - ( LABELOFFSET * fScale ) ), SpringLayout.WEST, pane );
		layout.putConstraint( SpringLayout.NORTH, criteriaLabel3, ( int ) ( 10 * fScale ), SpringLayout.SOUTH, criteriaLabel2 );

		textField3 = new JTextField("");
		pane.add( textField3 );
		layout.putConstraint( SpringLayout.WEST, textField3, ( int ) ( 10 * fScale ), SpringLayout.EAST, criteriaLabel3 );
		layout.putConstraint( SpringLayout.NORTH, textField3, 0, SpringLayout.NORTH, criteriaLabel3 );
		layout.putConstraint( SpringLayout.EAST, textField3, ( int ) ( -350 * fScale ), SpringLayout.EAST, pane );
		textField3.addKeyListener(new EnterListener());

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

		pane.add(buttonPanel);    	
		layout.putConstraint( SpringLayout.WEST, buttonPanel, ( int ) ( this.getWidth() / 3), SpringLayout.WEST, pane );
		layout.putConstraint( SpringLayout.NORTH, buttonPanel, ( int ) ( -40 * fScale ), SpringLayout.NORTH, criteriaLabel1 );

		listItems = new DefaultListModel<String>();

		//new String[] {  "test", "test1", "test2", "test3", "test4", "test5", "test6", "test7", "test8", "test9" }

		list = new JList<String>( listItems );

		//listItems.addElement( "test" );
		//list.setMinimumSize( new Dimension( 100, 100 ) );

		scrollPane = new JScrollPane();
		scrollPane.getViewport().add( list );
		pane.add( scrollPane );
		this.addKeyListener(new EnterListener());

		layout.putConstraint( SpringLayout.WEST, scrollPane, ( int ) ( 10 * fScale ), SpringLayout.WEST, pane );
		layout.putConstraint( SpringLayout.SOUTH, scrollPane, ( int ) ( -10 * fScale ) , SpringLayout.SOUTH, pane );
		layout.putConstraint( SpringLayout.EAST, scrollPane, ( int ) ( -10 * fScale ), SpringLayout.EAST, pane );



		//this.pack();
	}

	private void CalculateResults()
	{
		ResultSet results = Database.getInstance().executeQuery( "SELECT * FROM person" );
		String[] sCriterias = { textField1.getText().toLowerCase(),  textField2.getText().toLowerCase(), textField3.getText().toLowerCase() };
		listItems.clear();

		try
		{
			int iColumnCount = results.getMetaData().getColumnCount();

			while( results.next() )
			{
				String sToCheck = "";
				for( int i = 1; i <= iColumnCount; i++ )
				{
					sToCheck += results.getString(i) + "        ";
				}
				if( sToCheck.toLowerCase().contains( sCriterias[0] ) && sToCheck.toLowerCase().contains( sCriterias[1] ) && sToCheck.toLowerCase().contains( sCriterias[2] ) )
				{
					listItems.addElement( sToCheck );
				}
			}
		} 
		catch( SQLException e )
		{
			e.printStackTrace();
		}
	}
}