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

	private class keyListener implements KeyListener
	{

		@Override
		public void keyPressed( KeyEvent e )
		{

		}

		@Override
		public void keyReleased( KeyEvent e )
		{
			// WORKS:
			listModel.clear();
			currentField = ( JTextFieldUpgraded ) e.getComponent();
			if( !currentField.getText().equals( "" ) )
				calculateResults( currentField );
			if( currentField.getText().equals( "" ) )
				list.setVisible( false );
			// //
			// calculateResults();
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
			{
				// JTextField currentField = (JTextField) e.getComponent();

				// currentField.setText( selectedPerson.getFirstName() + " " + selectedPerson.getSurName() + " - " + selectedPerson.getPhone() );
				currentField.setPerson( selectedPerson );
			}

			list.setVisible( false );
			// System.out.println(list.getSelectedValue());
			// list.getSelectedIndex();
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

	private class PersonData extends AbstractListModel
	{
		ArrayList<Person> listPersons;

		public PersonData( ArrayList<Person> persons )
		{
			this.listPersons = persons;
		}

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

		public void addElement( Person person )
		{
			int index = listPersons.size();
			listPersons.add( person );
			fireIntervalAdded( this, index, index );
		}

		@Override
		public Object getElementAt( int index )
		{
			return( listPersons.get( index ).getFirstName() + " " + listPersons.get( index ).getSurName() + " -  " + listPersons.get( index )
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

	/*
	 * public void calculateResults() { // JLabel testlLabel = new JLabel("wutwut"); // testlLabel.setBounds( passengerField.getBounds().x,
	 * passengerField.getBounds().y + passengerField.getBounds().height + 5, 100, 20 ); //testlLabel.setBounds( 130, 50, 50, 20 );
	 * 
	 * 
	 * 
	 * //listModel.clear(); ArrayList<Person> persons = Database.getInstance().Get( Person.class ); for( Person person : persons ) { if(
	 * person.getFirstName().toLowerCase().startsWith( passengerField.getText() ) ) listModel.addElement( person.getFirstName() ); }
	 * 
	 * list.setVisible( true );
	 * 
	 * }
	 */

	public void calculateResults( JTextField currentField )
	{
		// listModel.addElement( Database.getInstance().Get( Person.class ).get( 0 ) );

		ArrayList<Person> persons = Database.getInstance().Get( Person.class );
		for( Person person : persons )
		{
			if( person.getFirstName().toLowerCase().startsWith( currentField.getText() )
			        || person.getSurName().toLowerCase().startsWith( currentField.getText() )
			        || person.getPhone().toLowerCase().startsWith( currentField.getText() )
			        || ( person.getFirstName() + " " + person.getSurName() ).toLowerCase().startsWith( currentField.getText() ) )
				listModel.addElement( person );
		}
		/*
		 * //if( listModel.getSize() == 0 ) for( Person person : persons ) if( person.getSurName().toLowerCase().startsWith( currentField.getText() )
		 * && !listModel.listPersons.contains(person)) listModel.addElement( person );
		 * 
		 * //if( listModel.getSize() == 0 ) for( Person person : persons ) if( person.getPhone().toLowerCase().startsWith( currentField.getText() ) &&
		 * !listModel.listPersons.contains(person)) listModel.addElement( person );
		 */

		list.setBounds( currentField.getBounds().x, currentField.getBounds().y + currentField.getBounds().height + 1, currentField.getWidth(), 100 );
		// passengerPanel.setComponentZOrder( list, 5 );
		if( listModel.getSize() > 0 )
			list.setVisible( true );
	}

	private void setupFrame()
	{
		frame.setSize( 300, 600 );
		frame.setResizable( false );
		frame.setTitle( "Passenger Manager Menu" );

		// frame.setLocationRelativeTo(null);

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

		JLabel mainPassengerHeaderLabel = new JLabel( "Main Passenger" );
		// mainPassengerHeaderLabel.setFont(FontCollection.HEADER);
		// mainPassengerLabel.setFont(FontCollection.NORMAL);
		JLabel additionalPassengerLabel = new JLabel( "Additional Passengers" );
		// additionalPassengerLabel.setFont(FontCollection.HEADER);

		middlePanel.add( mainPassengerHeaderLabel );
		// middlePanel.add(additionalPassengerLabel);

		JScrollPane scrollPane = new JScrollPane();
		// scrollPane.setPreferredSize(new Dimension(100,200));
		scrollPane.setBorder( BorderFactory.createEmptyBorder() );

		mainPassengerPanel = new JPanel();
		mainPassengerPanel.setLayout( new BoxLayout( mainPassengerPanel, BoxLayout.Y_AXIS ) );

		// Hver passager skal have et suggestive panel hvor man skriver navn ind og så slår den resten
		// Jeg får parsed en reservation hvor der er x antal passagerer og de har et sæde og denne menu skal tildele de person objekter de rigtige
		// værdier

		passengerField = new JTextFieldUpgraded[currentReservation.getPassengers().length];

		passengerPanel = new JPanel();
		passengerPanel.setLayout( null );

		for( int i = 0; i < currentReservation.getPassengers().length; i++ )
		{
			listModel = new PersonData();
			list = new JList<>( listModel );
			// list.setBounds( passengerField[i].getBounds().x, passengerField[i].getBounds().y + passengerField[i].getBounds().height + 1, 200, 100
			// );
			list.addMouseListener( new mouseListener() );
			passengerPanel.add( list );
			passengerPanel.setComponentZOrder( list, 0 );
			list.setVisible( false );

			passengerField[i] = new JTextFieldUpgraded();

			if( currentReservation.getPassengers()[i].getPerson() != null )
				passengerField[i].setPerson( currentReservation.getPassengers()[i].getPerson() );

			passengerField[i].setBounds( 20, ( i + 1 ) * 50, 175, 25 );
			passengerField[i].addKeyListener( new keyListener() );

			passengerPanel.add( passengerField[i] );

			JButton editButton = new JButton( "edit" );
			editButton.setBounds( 200, ( i + 1 ) * 50, 75, 25 );
			editButton.setActionCommand( "edit" );
			editButton.addActionListener( new actionListener( passengerField[i] ) );
			passengerPanel.add( editButton );

			// WORKS:
			/*
			 * listModel = new DefaultListModel<>(); list = new JList<String>(listModel); list.setBounds( passengerField.getBounds().x,
			 * passengerField.getBounds().y + passengerField.getBounds().height + 1, 100, 100 );
			 * 
			 * list.setVisible( false );
			 * 
			 * passengerPanel.add( list );
			 */
			// //////

			// listModel = new DefaultListModel<>();

			// passengerPanel.setComponentZOrder( list, 5 );

			mainPassengerPanel.add( passengerPanel );
		}

		scrollPane.setViewportView( mainPassengerPanel );
		// scrollPane.add(mainPassengerPanel);

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
