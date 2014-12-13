import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Buttons extends JPanel implements ActionListener
{
	private static final long serialVersionUID = -8967285174552469101L;
	private JButton btnStop, btnStart, btnRandom, btnPause;
	protected static boolean STOP, START, RANDOM, PAUSE;
	
	public Buttons()
	{
		super();
		this.setPreferredSize(new Dimension(GamePanel.WIDTH, 27));
		this.setLayout(new GridLayout(0, 4));
		btnRandom = new JButton("RANDOM");
		btnStart = new JButton("START");
		btnStop = new JButton("STOP");
		btnPause = new JButton("PAUSE");
		btnRandom.setBackground(Color.CYAN);
		btnStart.setBackground(Color.MAGENTA);
		btnPause.setBackground(Color.CYAN);
		btnStop.setBackground(Color.MAGENTA);
		btnRandom.addActionListener(this);
		btnStart.addActionListener(this);
		btnStop.addActionListener(this);
		btnPause.addActionListener(this);
		this.add(btnRandom);
		this.add(btnStart);
		this.add(btnPause);
		this.add(btnStop);
		RANDOM = START = STOP = PAUSE = false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == btnRandom)
		{
			RANDOM = true;
		}
		else if(e.getSource() == btnStart)
		{
			START = true;
		}
		else if(e.getSource() == btnStop)
		{
			STOP = true;
		}
		else if(e.getSource() == btnPause)
		{
			PAUSE = true;
		}
	}
}
