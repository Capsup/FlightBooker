package MainPackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

public class PassengerManagerMenu 
{
	private JFrame frame;
	private JPanel mainPanel;
	private JPanel mainPassengerPanel, passengerPanel;
	private JTextField[] passengerField;
	
	//WORKS:
	/*private DefaultListModel<String> listModel;
	private JList<String> list;*/
	/////
	//private ArrayList<Person> persons;
	private PersonData listModel;
	private JList<Person> list;
	
	private int passengerAmount = 5;
	
	private Reservation currentReservation;
	
	private class actionListener implements ActionListener
	{
		@Override
        public void actionPerformed( ActionEvent e )
        {
	        if( e.getActionCommand() == "edit" )
	        	new PassengerInformationEditor();
        }
	}
	
	private class keyListener implements KeyListener
	{

		@Override
        public void keyPressed( KeyEvent e )
        {
	        // TODO Auto-generated method stub
	        
        }

		@Override
        public void keyReleased( KeyEvent e )
        {
			//WORKS:
			listModel.clear();
			JTextField currentField = (JTextField) e.getComponent();
			if( !currentField.getText().equals( "" ) )
				calculateResults(currentField);
			if( currentField.getText().equals( "" ) )
				list.setVisible( false );
			////
			//calculateResults();
        }

		@Override
        public void keyTyped( KeyEvent e )
        {
	        // TODO Auto-generated method stub
	        
        }
	}
	
	private class mouseListener implements MouseListener
	{

		@Override
        public void mouseClicked( MouseEvent e )
        {
			Person selectedPerson = listModel.listPersons.get( list.getSelectedIndex() );
			
			if( selectedPerson != null )
			{
				JTextField currentField = (JTextField) e.getComponent();
				
				currentField.setText( selectedPerson.getFirstName() + " " + selectedPerson.getSurName() + " - " + selectedPerson.getPhone() );
			}
			
			list.setVisible( false );
			//System.out.println(list.getSelectedValue());
			//list.getSelectedIndex();
        }

		@Override
        public void mouseEntered( MouseEvent e )
        {
	        // TODO Auto-generated method stub
	        
        }

		@Override
        public void mouseExited( MouseEvent e )
        {
	        // TODO Auto-generated method stub
	        
        }

		@Override
        public void mousePressed( MouseEvent e )
        {

        }

		@Override
        public void mouseReleased( MouseEvent e )
        {
	        // TODO Auto-generated method stub
	        
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
			int index = listPersons.size()-1;
			listPersons.clear();
			if (index >= 0) 
			{
				fireIntervalRemoved(this, 0, index);
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
	        return( listPersons.get( index ).getFirstName() + " " + listPersons.get( index ).getSurName() );
        }

		@Override
        public int getSize()
        {
	        return listPersons.size();
        }
		
	}
	
	public PassengerManagerMenu(JFrame frame, Reservation currentReservation)
	{
		this.frame = frame;
		
		this.currentReservation = currentReservation;
		
		setupFrame();
		//setupFonts();
		
		makeContent();
	}
	

	/*public void calculateResults()
	{
//		JLabel testlLabel = new JLabel("wutwut");
//		testlLabel.setBounds( passengerField.getBounds().x, passengerField.getBounds().y + passengerField.getBounds().height + 5, 100, 20 );
		//testlLabel.setBounds( 130, 50, 50, 20 );
		

		
		//listModel.clear();
		ArrayList<Person> persons = Database.getInstance().Get( Person.class );
		for( Person person : persons )
        {
	        if( person.getFirstName().toLowerCase().startsWith( passengerField.getText() ) )
	        	listModel.addElement( person.getFirstName() );
        }
		
		list.setVisible( true );

    }*/
	
	public void calculateResults(JTextField currentField)
	{
		//listModel.addElement( Database.getInstance().Get( Person.class ).get( 0 ) );
		
		ArrayList<Person> persons = Database.getInstance().Get( Person.class );
		for( Person person : persons )
        {
	        if( person.getFirstName().toLowerCase().startsWith( currentField.getText() ) )
	        	listModel.addElement( person );
        }
		
		list.setVisible( true );
	}

	private void setupFrame()
	{
		frame.setSize(300, 600);
		frame.setResizable(false);
		
		frame.setLocationRelativeTo(null);
		
		update();
	}
	
	private void makeContent()
	{
		Container contentPane = frame.getContentPane();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		//Middle
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
		
		JLabel mainPassengerHeaderLabel = new JLabel("Main Passenger");
		//mainPassengerHeaderLabel.setFont(FontCollection.HEADER);
		JLabel mainPassengerLabel = new JLabel("Sir Martin Fagalot, 60249924");
		//mainPassengerLabel.setFont(FontCollection.NORMAL);
		JLabel additionalPassengerLabel = new JLabel("Additional Passengers");
		//additionalPassengerLabel.setFont(FontCollection.HEADER);
		
		middlePanel.add(mainPassengerHeaderLabel);
		middlePanel.add(mainPassengerLabel);
		middlePanel.add(additionalPassengerLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		//scrollPane.setPreferredSize(new Dimension(100,200));
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		mainPassengerPanel = new JPanel();
		mainPassengerPanel.setLayout(new BoxLayout(mainPassengerPanel, BoxLayout.Y_AXIS));
		
		//Hver passager skal have et suggestive panel hvor man skriver navn ind og så slår den resten
		//Jeg får parsed en reservation hvor der er x antal passagerer og de har et sæde og denne menu skal tildele de person objekter de rigtige værdier
		
		passengerField = new JTextField[currentReservation.getPassengers().length];
		
		passengerPanel = new JPanel();
		passengerPanel.setLayout(null);
		
		for( int i = 0; i < currentReservation.getPassengers().length; i++ )
        {
	        passengerField[i] = new JTextField();
	        passengerField[i].setBounds( 20, 50, 200, 25 );
	        passengerField[i].addKeyListener( new keyListener() );
	        
	        passengerPanel.add( passengerField[i] );
	        
	        //WORKS:
			/*listModel = new DefaultListModel<>();
			list = new JList<String>(listModel);
			list.setBounds( passengerField.getBounds().x, passengerField.getBounds().y + passengerField.getBounds().height + 1, 100, 100 );
			
			list.setVisible( false );
			
			passengerPanel.add( list );*/
			////////
	        
	        //listModel = new DefaultListModel<>();
	        
	        listModel = new PersonData();
	        list = new JList<>(listModel);
	        list.setBounds( passengerField[i].getBounds().x, passengerField[i].getBounds().y + passengerField[i].getBounds().height + 1, 200, 100 );
	        list.addMouseListener( new mouseListener() );
	        passengerPanel.add( list );
	        
	        list.setVisible( false );
	        
	        mainPassengerPanel.add( passengerPanel );
        }
		
		scrollPane.setViewportView(mainPassengerPanel);
		//scrollPane.add(mainPassengerPanel);
		
		middlePanel.add(scrollPane);
		
		mainPanel.add(middlePanel, BorderLayout.CENTER);
		
		contentPane.add(mainPanel);
		
	}
	
	private void update()
	{
		frame.setVisible(true);
	
	}
}
