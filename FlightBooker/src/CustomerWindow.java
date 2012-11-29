import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/*
 * CustomerWindow er vinduet hvor der kan søges efter kunder
 */
public class CustomerWindow extends JFrame {

	private static CustomerWindow instance = null;

	private JLabel customerID = new JLabel("CustomerID: " + "123123");
	private JTextField firstName = new JTextField("Black");
	private JTextField surName = new JTextField("Widow");
	private JTextField gender = new JTextField("Female");
	private JTextField dateOfBirth = new JTextField("06-04-1991");
	private JTextField country = new JTextField("Mother Russia");
	private JTextField nationality = new JTextField("Russian");
	private JTextField adress = new JTextField("SHIELD");
	private JTextField phoneNum = new JTextField("1351351345");
	private JTextField passNum = new JTextField("09283471325813");
	private JTextField[] infoFields;

	private boolean editing = false;

	private CustomerWindow()
	{
		//Laver en dialogboks, hvor der skal vælges kundetype
		new NewOrExistDialog();	

		//Dialogen skal åbne samme vindue, skal hente eksisterendes kundeinfo ind i vinduet
		setFrame();		
		makeContent();
		this.setVisible(true);
		this.pack();

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
				if(editing){
					editing = false;
					for(JTextField field : infoFields)
						field.setEditable(false);
				}

				else{
					System.out.println("Closing window");
					close();
				}

				//Send nye values til databasen

				for(JTextField field : infoFields)
					field.setEditable(false);
				break;

			case "Edit":
				System.out.println("Edit info");
				if(!editing){
					editing = true;
					for(JTextField field : infoFields)
						field.setEditable(true);
				}

				break;

			case "NewRes":
				System.out.println("New reservation");
				break;

			case "InspRes":
				System.out.println("Inspect reservation");
				break;

			case "SeeRes":
				System.out.println("See reservations");
				break;

			case "Cancel":
				System.out.println("Cancel");
				if(editing){
					editing = false;
					for(JTextField field : infoFields)
						field.setEditable(false);
				}					

				else{
					System.out.println("Closing window");
					close();
				}

				//SÆT TILBAGE TIL OPRINDELIGE VALUES!!!

				break;

			case "Exit":

				break;				
			}
		}
	}

	class NewOrExistDialog extends JOptionPane
	{
		public NewOrExistDialog()
		{
			JPanel popupPanel = new JPanel();
			ButtonGroup buttonMenu = new ButtonGroup();
			JRadioButtonMenuItem newCus = new JRadioButtonMenuItem("New Customer", false);
			JRadioButtonMenuItem exisCus = new JRadioButtonMenuItem("Existing Customer", true);

			buttonMenu.add(newCus);
			buttonMenu.add(exisCus);

			popupPanel.add(newCus);
			popupPanel.add(exisCus);			

			Object[] options = {newCus, exisCus};

			int n = JOptionPane.showOptionDialog(instance,
					popupPanel,
					"Customer type",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					null, null);

			/*			Object[] options = {"New customer",	"Existing customer"};
			int n = JOptionPane.showOptionDialog(instance,
					"New or existing customer?",
					"Customer type",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options, options);*/		


			//addWindowListener(new CloseListener());
			//Hvis der skal laves en ny kunde
			if(n == 0)
			{
				System.out.println("OK");
				//Her indsættes databasekode, der henter kundedata
			}			

			if(n == 2)
			{
				System.out.println("Cancel");
				close();
			}
		}
	}

	/*
	 * Sætter egenskaberne for CustomerWindow
	 */
	private void setFrame()
	{
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//this.setSize(800, 400);
		this.setResizable(false);
		this.setTitle("Customer: " + firstName.getText() + " " + 
				surName.getText() + " - " + customerID.getText());		

		//Tilføjer listener til rammen, der tjekker om vinduet lukkes
		this.addWindowListener(new CloseListener());
	}

	/*
	 * Laver CustomerWindows indhold
	 */
	private void makeContent()
	{
		Container contentPane = this.getContentPane();   

		//Laver labels, der identificerer infofelterne
		String[] fieldNames = {"First name: ", "Surname: ","Gender: ","Date of birth","Country: ",
				"Nationality: ", "Adress: ", "Phone number: ", "Passport number: "};

		//Laver infofelterne 
		infoFields = new JTextField[]{firstName, surName, gender, dateOfBirth, country, 
				nationality, adress, phoneNum, passNum};
		for(JTextField field : infoFields)
			field.setEditable(false);

		//Laver et JPanel til kundeinfo
		JPanel fieldsAndLabels = new JPanel();
		fieldsAndLabels.setLayout(new GridLayout(infoFields.length,2));

		//Indsætter labels og infofelter i fieldsAndLabels
		int nameNum = 0;
		int infoNum = 0;
		for(int i = 1; i <= (infoFields.length*2); i++){
			if(i % 2 == 0){
				fieldsAndLabels.add(infoFields[infoNum]);				
				infoNum++;
			}
			else{
				fieldsAndLabels.add(new JLabel(fieldNames[nameNum]));
				nameNum++;
			}
		}

		//Laver reservationstabellen
		JPanel reservationsOverview = new JPanel();
		reservationsOverview.setLayout(new BoxLayout(reservationsOverview, BoxLayout.Y_AXIS));

		String[] reservationLabelStrings = {"Depature date", "Departure airport",
				"Destination airport", "Number of passengers", "Reservation ID"};

		JPanel reservationLabels = new JPanel();
		reservationLabels.setLayout(new GridLayout(1,reservationLabelStrings.length+1,15,5));

		for(String label : reservationLabelStrings)
			reservationLabels.add(new JLabel(label));

		reservationsOverview.add(reservationLabels);

		TitledBorder reservationsBorder = BorderFactory.createTitledBorder("Reservations");		
		reservationsOverview.setBorder(reservationsBorder);

		//Gridlayout in a gridlayout til at lave et grid til reservationerne

		// 5x1 i et 

		//Laver knapper
		JButton bOK = new JButton("OK");
		JButton bEditInfo = new JButton("Edit info");
		JButton bNewReservation = new JButton("New reservation");
		JButton bNewReservation2 = new JButton("New reservation");
		JButton bInspectReservation = new JButton("Inspect reservation");
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

		bNewReservation2.addActionListener(listener);
		bNewReservation2.setActionCommand("NewRes");

		bInspectReservation.addActionListener(listener);
		bInspectReservation.setActionCommand("InspRes");
		bInspectReservation.setEnabled(false);

		bSeeReservations.addActionListener(listener);
		bSeeReservations.setActionCommand("SeeRes");

		bCancel.addActionListener(listener);
		bCancel.setActionCommand("Cancel");

		bExit.addActionListener(listener);
		bExit.setActionCommand("Exit");

		//Laver paneler til knapper
		JPanel buttons1 = new JPanel();
		buttons1.setLayout(new FlowLayout(1,15,15));

		JPanel buttons2 = new JPanel();
		buttons2.setLayout(new FlowLayout(1,15,15));		

		//Tilføjer knapper til panelerne
		buttons1.add(bOK);
		buttons1.add(bEditInfo);
		buttons1.add(bCancel);		

		buttons2.add(bNewReservation2);
		buttons2.add(bInspectReservation);

		//Laver paneler til tabs

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel,BoxLayout.Y_AXIS));
		if(customerID != null)
			infoPanel.add(customerID);
		infoPanel.add(fieldsAndLabels);
		infoPanel.add(buttons1);

		JPanel reservationPanel = new JPanel();
		reservationPanel.setLayout(new BorderLayout());
		reservationPanel.add(reservationsOverview,BorderLayout.CENTER);
		reservationPanel.add(buttons2,BorderLayout.SOUTH);

		TitledBorder customerBorder = BorderFactory.createTitledBorder("Customer details");
		infoPanel.setBorder(customerBorder);

		contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.X_AXIS));
		contentPane.add(infoPanel);   
		contentPane.add(reservationPanel);
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
