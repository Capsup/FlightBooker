package MainPackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.*;

public class PassengerManagerMenu 
{
	private JFrame frame;
	private JPanel mainPanel;
	
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
	
	public PassengerManagerMenu(JFrame frame)
	{
		this.frame = frame;
		
		setupFrame();
		//setupFonts();
		
		makeContent();
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
		
		JPanel mainPassengerPanel = new JPanel();
		mainPassengerPanel.setLayout(new BoxLayout(mainPassengerPanel, BoxLayout.Y_AXIS));
		
		for(int i=0; i<passengerAmount; i++)
		{
			JPanel passengerPanel = new JPanel();
			passengerPanel.setLayout(new BoxLayout(passengerPanel,BoxLayout.X_AXIS));
			passengerPanel.setAlignmentX(Box.LEFT_ALIGNMENT);
			
			JLabel passengerLabel = new JLabel("Jesper den sexede demon");
			JButton editButton = new JButton("Edit");
			editButton.setActionCommand( "edit" );
			editButton.addActionListener( new actionListener() );
			JButton deleteButton = new JButton("-");
			
			passengerPanel.add(passengerLabel);
			passengerPanel.add(Box.createRigidArea(new Dimension(20,0)));
			passengerPanel.add(editButton);
			passengerPanel.add(Box.createRigidArea(new Dimension(20,0)));
			passengerPanel.add(deleteButton);
			passengerPanel.add(Box.createRigidArea(new Dimension(0,30)));
			
			mainPassengerPanel.add(passengerPanel);
		}
		
		
		
//		for( int i = 0; i < 1; i++ )
//        {
//	        JPanel passengerPanel = new JPanel();
//	        passengerPanel.setLayout( null );
//	        
//	        JTextField passengerField = new JTextField();
//	        passengerField.setBounds( 20, 50, 100, 100 );
//	        
//        }
		
		scrollPane.setViewportView(mainPassengerPanel);
		
		mainPanel.add(middlePanel, BorderLayout.CENTER);
		
		contentPane.add(mainPanel);
		
	}
	
	private void update()
	{
		frame.setVisible(true);
	
	}
}
