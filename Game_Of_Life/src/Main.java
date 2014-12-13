import javax.swing.JFrame;

public class Main
{
	public static void main(String args[])
	{
		JFrame frame = new JFrame("Game Of Life");
		frame.setLocation(500, 0);
		frame.setContentPane(new GamePanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.pack();
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
	}
}
