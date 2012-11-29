package MainPackage;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Main
{
	/**
	 * @param args
	 */
	
	public static int SIZE_X = 800;
	public static int SIZE_Y = 600;
	
	public static void main( String[] args )
	{
		Dimension dScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		FindDialog frame = new FindDialog( dScreenSize, SIZE_X, SIZE_Y );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		/*frame.setLocation( (int) ( dScreenSize.width * 0.5f - ( SIZE_X * 0.5f )), (int) (dScreenSize.height * 0.5f - ( SIZE_Y * 0.5f )) );
		frame.setSize( new Dimension( SIZE_X, SIZE_Y ) );*/
		frame.setVisible( true );
	}
}