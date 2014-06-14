import gui.GamePanel;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Tester
{	
	private static final int size = 600;
	private static final int numOfTiles = 9;
	private static final int numOfShapes =5;
	private static final String shapeType = "Ball";

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Tester");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(size,size);
		JPanel p = new GamePanel(numOfTiles, numOfShapes, shapeType);
		frame.add(p);
		frame.setVisible(true);
	}
}
