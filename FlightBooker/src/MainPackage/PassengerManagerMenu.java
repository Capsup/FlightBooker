package MainPackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.*;

public class PassengerManagerMenu 
{
	private JFrame frame;
	private JPanel mainPanel;
	private JPanel mainPassengerPanel, passengerPanel;
	private JTextField passengerField;
	
	private int passengerAmount = 5;
	
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
	        calculateResults();
	        
        }

		@Override
        public void keyTyped( KeyEvent e )
        {
	        // TODO Auto-generated method stub
	        
        }
	}
	
	public PassengerManagerMenu(JFrame frame, Reservation currentReservation)
	{
		this.frame = frame;
		
		setupFrame();
		//setupFonts();
		
		makeContent();
	}
	

	public void calculateResults()
	{
//		JLabel testlLabel = new JLabel("wutwut");
//		testlLabel.setBounds( passengerField.getBounds().x, passengerField.getBounds().y + passengerField.getBounds().height + 5, 100, 20 );
		//testlLabel.setBounds( 130, 50, 50, 20 );
		
		DefaultListModel<String> listModel = new DefaultListModel<>();
		JList<String> list = new JList<String>(listModel);
		list.setBounds( passengerField.getBounds().x, passengerField.getBounds().y + passengerField.getBounds().height + 1, 100, 100 );
		
		
		
		passengerPanel.add( list );
		passengerPanel.repaint();
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
		scrollPane.setPreferredSize(new Dimension(100,200));
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		middlePanel.add(scrollPane);
		
		mainPassengerPanel = new JPanel();
		mainPassengerPanel.setLayout(new BoxLayout(mainPassengerPanel, BoxLayout.Y_AXIS));
		
//		for(int i=0; i<passengerAmount; i++)
//		{
//			JPanel passengerPanel = new JPanel();
//			passengerPanel.setLayout(new BoxLayout(passengerPanel,BoxLayout.X_AXIS));
//			passengerPanel.setAlignmentX(Box.LEFT_ALIGNMENT);
//			
//			JLabel passengerLabel = new JLabel("Jesper den sexede demon");
//			JButton editButton = new JButton("Edit");
//			editButton.setActionCommand( "edit" );
//			editButton.addActionListener( new actionListener() );
//			JButton deleteButton = new JButton("-");
//			
//			passengerPanel.add(passengerLabel);
//			passengerPanel.add(Box.createRigidArea(new Dimension(20,0)));
//			passengerPanel.add(editButton);
//			passengerPanel.add(Box.createRigidArea(new Dimension(20,0)));
//			passengerPanel.add(deleteButton);
//			passengerPanel.add(Box.createRigidArea(new Dimension(0,30)));
//			
//			mainPassengerPanel.add(passengerPanel);
//		}
		
		//Hver passager skal have et suggestive panel hvor man skriver navn ind og så slår den resten
		//Jeg får parsed en reservation hvor der er x antal passagerer og de har et sæde og denne menu skal tildele de person objekter de rigtige værdier
		
		for( int i = 0; i < 1; i++ )
        {
	        passengerPanel = new JPanel();
	        passengerPanel.setLayout( null );
	        
	        passengerField = new JTextField();
	        passengerField.setBounds( 20, 50, 100, 25 );
	        passengerField.addKeyListener( new keyListener() );
	        
	        passengerPanel.add( passengerField );
	        
	        mainPassengerPanel.add( passengerPanel );
        }
		
		scrollPane.setViewportView(mainPassengerPanel);
		
		mainPanel.add(middlePanel, BorderLayout.CENTER);
		
		contentPane.add(mainPanel);
		
	}
	
	private void update()
	{
		frame.setVisible(true);
	
	}
}
