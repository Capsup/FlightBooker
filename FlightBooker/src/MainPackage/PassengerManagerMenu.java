package MainPackage;

import java.awt.*;

import javax.swing.*;

public class PassengerManagerMenu 
{
	private JFrame frame;
	private JPanel mainPanel;
	
	private Font headerFont;
	private Font normalFont;
	
	public PassengerManagerMenu(JFrame frame)
	{
		this.frame = frame;
		
		setupFrame();
		setupFonts();
		
		makeContent();
	}
	
	private void setupFrame()
	{
		frame.setSize(400, 600);
		frame.setResizable(false);
	}
	
	private void makeContent()
	{
		Container contentPane = frame.getContentPane();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		//Top
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		
		JLabel mainPassengerHeader = new JLabel("Main Menu");
		mainPassengerHeader.setFont(headerFont);
		topPanel.add(mainPassengerHeader);
		
		//Center
		JPanel centerPanel = new JPanel();
		SpringLayout springLayout = new SpringLayout();
		
		contentPane.setLayout( springLayout );
		
		
		
		/*JLabel mainPassenger = new JLabel("Sir Martin Fagalot, 60249924");
		mainPassenger.setFont(normalFont);
		topPanel.add(mainPassenger);*/
		
		
		mainPanel.add(topPanel, BorderLayout.NORTH);
		
		contentPane.add(mainPanel);
		
	}
	
	private void addCustomer( SpringLayout layout )
	{
		
	}
	
	private void setupFonts()
	{
		headerFont = new Font("TimesRoman", Font.BOLD, 16);
		normalFont = new Font("TimesRoman", Font.PLAIN, 16);
		
	}
}
