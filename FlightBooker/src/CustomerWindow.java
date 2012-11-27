import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/*
 * CustomerWindow er vinduet hvor der kan søges efter kunder
 */
public class CustomerWindow extends JFrame {

	private static CustomerWindow instance = null;

	private CustomerWindow()
	{
		//Laver en dialogboks, hvor der skal vælges kundetype
		new NewOrExistDialog();	
		//Dialogen skal åbne samme vindue, skal hente eksisterendes kundeinfo ind i vinduet

		setFrame();		
		makeContent();
		this.setVisible(true);
		//this.pack();


		//Spawner rammen midt på skærmen
		this.setLocationRelativeTo(null);
	}

	class CloseListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent e) { 
			close(); 
		}
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			switch(event.getActionCommand())
			{
			case "OK": 	
				System.out.println("OK");
				break;

			case "Edit":
				System.out.println("Edit info");
				new FindWindow();
				break;
				
			case "NewRes":
				System.out.println("New reservation");
				break;
				
			case "SeeRes":
				System.out.println("See reservations");
				break;
				
			case "Cancel":
				System.out.println("Cancel");				
				
			case "Exit":
				System.out.println("Exit");
				System.exit(EXIT_ON_CLOSE);
				break;
			}
		}
	}

	class NewOrExistDialog extends JOptionPane
	{
		public NewOrExistDialog()
		{
			Object[] options = {"New customer",	"Existing customer"};
			int n = JOptionPane.showOptionDialog(instance,
					"New or existing customer?",
					"Customer type",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options, options);

			//addWindowListener(new CloseListener());
			//Hvis der skal laves en ny kunde
			if(n == 1)
			{
				System.out.println("Existing customer");
				//Her indsættes databasekode, der henter kundedata
			}			
		}

	}

	/*
	 * Sætter egenskaberne for CustomerWindow
	 */
	private void setFrame()
	{
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(300, 400);
		this.setResizable(false);
		this.setTitle("Customer" /* Name and id */);

		//Tilføjer listener til rammen, der tjekker om vinduet lukkes
		this.addWindowListener( 
				new WindowAdapter() { 
					public void windowClosing(WindowEvent e) { 
						close(); 
					} 
				} 
				);
	}

	/*
	 * Laver CustomerWindows indhold
	 */
	private void makeContent()
	{
		Container contentPane = this.getContentPane();    
		this.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		//Laver labels, der identificerer infofelterne
		String[] fieldNames = {"First name: ", "Surname: ","Gender: ","Date of birth","Country: ",
				"Nationality: ", "Adress: ", "Phone number: ", "Passport number: "};
		for(String field : fieldNames)
			new JLabel(field);
		
		//Laver infofelterne 
		String[] infoFields = {"Black", "Widow","Female","06-04-1991","Mother Russia", "Russian",
				"Avengers", "SEXYHOTNESS-2190", "NO"};
		for(String field : infoFields)
			new JLabel(field);

		//Laver et JPanel kaldet fieldsAndLabels
		JPanel fieldsAndLabels = new JPanel();
		fieldsAndLabels.setLayout(new GridLayout(infoFields.length,2));
		
		//Indsætter labels og infofelter i fieldsAndLabels
		int nameNum = 0;
		int infoNum = 0;
		for(int i = 1; i <= (infoFields.length*2); i++){
			if(i % 2 == 0){
				fieldsAndLabels.add(new JTextField(infoFields[infoNum]));
				infoNum++;
			}
			else{
				fieldsAndLabels.add(new JLabel(fieldNames[nameNum]));
				nameNum++;
			}
		}


		//Laver et JPanel kaldet buttons
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout(1,15,15));

		//Laver knapper
		JButton bOK = new JButton("OK");
		JButton bEditInfo = new JButton("Edit info");
		JButton bNewReservation = new JButton("New reservation");
		JButton bSeeReservations = new JButton("See reservations");
		JButton bCancel = new JButton("Cancel");
		JButton bExit = new JButton("Exit");

		//Tilføjer listeners til knapperne
		ButtonListener listener = new ButtonListener();
		
		bOK.addActionListener(listener);
		bOK.setActionCommand("OK");
		
		bEditInfo.addActionListener(listener);
		bEditInfo.setActionCommand("Edit");
		
		bNewReservation.addActionListener(listener);
		bNewReservation.setActionCommand("NewRes");
		
		bSeeReservations.addActionListener(listener);
		bSeeReservations.setActionCommand("SeeRes");
		
		bCancel.addActionListener(listener);
		bCancel.setActionCommand("Cancel");
		
		bExit.addActionListener(listener);
		bExit.setActionCommand("Exit");
		
		//Tilføjer knapper til Jpanelet
		buttons.add(bOK);
		buttons.add(bEditInfo);
		buttons.add(bNewReservation);
		buttons.add(bSeeReservations);
		buttons.add(bCancel);		
		buttons.add(bExit);
		

		contentPane.add(fieldsAndLabels);
		contentPane.add(buttons);        
	}

	/*
	 * Returnerer en instans af CustomerWindow, hvis et ikke allerede findes
	 */
	public static CustomerWindow getInstance() {
		if(instance == null) {
			instance = new CustomerWindow();
		}
		return instance;
	}

	/*
	 * Fjerner rammen og sætter instance til null
	 */
	private void close()
	{
		instance.dispose();
		instance = null;
	}

}
