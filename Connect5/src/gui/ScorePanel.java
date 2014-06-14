package gui;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScorePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private JLabel score, moves;
	
	public ScorePanel()
	{
		score = new JLabel("Score:");
		score.setSize(5, 5);
		score.setBackground(Color.RED);
		moves = new JLabel("Moves:");
		moves.setBackground(Color.RED);
		setBackground(Color.CYAN);
		add(score);
		add(moves);
	}
}
