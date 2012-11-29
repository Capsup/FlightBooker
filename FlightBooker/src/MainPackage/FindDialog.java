package MainPackage;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

@SuppressWarnings( "serial" )
class FindDialog extends JFrame
{
    public static int LABELOFFSET = 100;
    
    JLabel CriteriaLabel1, CriteriaLabel2, CriteriaLabel3;
    JTextField TextField1, TextField2, TextField3;
    JList<String> list;
    JScrollPane scrollPane;
    DefaultListModel<String> listItems;
    
    class ButtonListener implements ActionListener
    {
    	public ButtonListener()
    	{
    		
    	}
    	
    	@Override
        public void actionPerformed( ActionEvent e )
        {
    		CalculateResults();
        }
    }
    
    public FindDialog( Dimension dScreenSize, int iSizeX, int iSizeY )
    {
    	Container pane = getContentPane();
    	SpringLayout layout = new SpringLayout();
    	float fScale = ( dScreenSize.width / dScreenSize.height ) / ( 1600 / 1200 );
    	
    	pane.setLayout( layout );
    	
    	//this.setLocation( (int) ( dScreenSize.width * 0.5f - ( iSizeX * 0.5f )), (int) (dScreenSize.height * 0.5f - ( iSizeY * 0.5f )) );
    	
    	this.setSize( new Dimension( iSizeX, iSizeY ) );
    	this.setLocationRelativeTo( null );
    	
    	CriteriaLabel1 = new JLabel( "Criteria: " );
    	pane.add( CriteriaLabel1 );
    	layout.putConstraint( SpringLayout.WEST, CriteriaLabel1, ( int ) ( this.getWidth() / 2 - ( LABELOFFSET * fScale ) ), SpringLayout.WEST, pane );
    	layout.putConstraint( SpringLayout.NORTH, CriteriaLabel1, ( int ) ( 20 * fScale ), SpringLayout.NORTH, pane );
    	
    	TextField1 = new JTextField("");
    	pane.add( TextField1 );
    	layout.putConstraint( SpringLayout.WEST, TextField1, ( int ) ( 10 * fScale ), SpringLayout.EAST, CriteriaLabel1 );
    	layout.putConstraint( SpringLayout.NORTH, TextField1, 0, SpringLayout.NORTH, CriteriaLabel1 );
    	layout.putConstraint( SpringLayout.EAST, TextField1, ( int ) ( -350 * fScale ), SpringLayout.EAST, pane );
    	TextField1.addActionListener( new ButtonListener() );
    	
    	CriteriaLabel2 = new JLabel( "Criteria: " );
    	pane.add( CriteriaLabel2 );
    	layout.putConstraint( SpringLayout.WEST, CriteriaLabel2, ( int ) ( this.getWidth() / 2 - ( LABELOFFSET * fScale ) ), SpringLayout.WEST, pane );
    	layout.putConstraint( SpringLayout.NORTH, CriteriaLabel2, ( int ) ( 10 * fScale ), SpringLayout.SOUTH, CriteriaLabel1 );
    	
    	TextField2 = new JTextField("");
    	pane.add( TextField2 );
    	layout.putConstraint( SpringLayout.WEST, TextField2, ( int ) ( 10 * fScale ), SpringLayout.EAST, CriteriaLabel2 );
    	layout.putConstraint( SpringLayout.NORTH, TextField2, 0, SpringLayout.NORTH, CriteriaLabel2 );
    	layout.putConstraint( SpringLayout.EAST, TextField2, ( int ) ( -350 * fScale ), SpringLayout.EAST, pane );
    	TextField2.addActionListener( new ButtonListener() );
    	
    	CriteriaLabel3 = new JLabel( "Criteria: " );
    	pane.add( CriteriaLabel3 );
    	layout.putConstraint( SpringLayout.WEST, CriteriaLabel3, ( int ) ( this.getWidth() / 2 - ( LABELOFFSET * fScale ) ), SpringLayout.WEST, pane );
    	layout.putConstraint( SpringLayout.NORTH, CriteriaLabel3, ( int ) ( 10 * fScale ), SpringLayout.SOUTH, CriteriaLabel2 );
    	
    	TextField3 = new JTextField("");
    	pane.add( TextField3 );
    	layout.putConstraint( SpringLayout.WEST, TextField3, ( int ) ( 10 * fScale ), SpringLayout.EAST, CriteriaLabel3 );
    	layout.putConstraint( SpringLayout.NORTH, TextField3, 0, SpringLayout.NORTH, CriteriaLabel3 );
    	layout.putConstraint( SpringLayout.EAST, TextField3, ( int ) ( -350 * fScale ), SpringLayout.EAST, pane );
    	TextField3.addActionListener( new ButtonListener() );
    	
    	listItems = new DefaultListModel<String>();
    	
    	//new String[] {  "test", "test1", "test2", "test3", "test4", "test5", "test6", "test7", "test8", "test9" }
    	
    	list = new JList<String>( listItems );
    	
    	//listItems.addElement( "test" );
    	//list.setMinimumSize( new Dimension( 100, 100 ) );
    	
    	scrollPane = new JScrollPane();
    	scrollPane.getViewport().add( list );
    	pane.add( scrollPane );
    	
    	layout.putConstraint( SpringLayout.WEST, scrollPane, ( int ) ( 10 * fScale ), SpringLayout.WEST, pane );
    	layout.putConstraint( SpringLayout.SOUTH, scrollPane, ( int ) ( -10 * fScale ) , SpringLayout.SOUTH, pane );
    	layout.putConstraint( SpringLayout.EAST, scrollPane, ( int ) ( -10 * fScale ), SpringLayout.EAST, pane );
    	
    	
    	
    	//this.pack();
    }
    
    public void CalculateResults()
    {
    	ResultSet results = Database.getInstance().executeQuery( "SELECT * FROM person" );
    	String[] sCriterias = { TextField1.getText().toLowerCase(),  TextField2.getText().toLowerCase(), TextField3.getText().toLowerCase() };
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