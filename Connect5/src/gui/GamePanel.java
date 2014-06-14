package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import objects.Tile;
import logic.Connect5Game;
import objects.Point;

public class GamePanel extends JPanel implements ActionListener, MouseListener
{
	private static final long serialVersionUID = 1L;

	private Connect5Game conGame;
	private JLabel score, moves;
	private int clicked;
	private boolean mouseListenerActive;
	private int currentI;
	private int currentJ;
	private int numOfTiles;
	private int destRow;
	private int destCol;
	private JPanel bPanel;
	private JButton cmdStart;
	private int numOfShapes;
	private String shapeType;
	
	public GamePanel(int numOfTiles, int numOfShapes, String shapeType)
	{
		this.numOfTiles = numOfTiles;
		this.numOfShapes = numOfShapes;
		this.shapeType = shapeType;

		bPanel = new JPanel();
		bPanel.setLayout(new GridLayout(numOfTiles,numOfTiles));
		
		cmdStart = new JButton("START NEW GAME");
		cmdStart.addActionListener(this);
		        
		conGame = new Connect5Game(numOfTiles, numOfShapes, shapeType);

		for(int i = 0; i < numOfTiles; i++)
		{
			for(int j = 0; j < numOfTiles; j++)
			{
				conGame.getBoard().getTiles()[i][j].setBackground(Color.WHITE);
				conGame.getBoard().getTiles()[i][j].addActionListener(this);
				conGame.getBoard().getTiles()[i][j].addMouseListener(this);
				bPanel.add(conGame.getBoard().getTiles()[i][j]);
			}
		}

		currentI = 0;
		currentJ = 0;
		clicked = 0;

		JPanel sPanel = new JPanel();
		score = new JLabel("Score: 0");
		score.setSize(5, 5);
		score.setBackground(Color.RED);
		moves = new JLabel("Moves: 0");
		moves.setBackground(Color.RED);
		setBackground(Color.CYAN);
		sPanel.add(score);
		sPanel.add(moves);

		cmdStart.setBackground(Color.WHITE);

		setLayout(new BorderLayout());
		add(bPanel, BorderLayout.CENTER);
		add(sPanel, BorderLayout.SOUTH);
		add(cmdStart, BorderLayout.NORTH);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		bPanel.removeAll();
		for(int i = 0; i < numOfTiles; i++)
		{
			for(int j = 0; j < numOfTiles; j++)
			{
				bPanel.add(conGame.getBoard().getTiles()[i][j]);
			}
		}
		score.setText("Score: " + conGame.getScore());
		moves.setText("Moves: " + conGame.getSteps());		
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == cmdStart)
		{
			if(conGame.getSteps() != 0)
			{
				conGame.startNewGame(numOfTiles, numOfShapes, shapeType);
				
				for(int i = 0; i < numOfTiles; i++)
				{
					for(int j = 0; j < numOfTiles; j++)
					{
						conGame.getBoard().getTiles()[i][j].setBackground(Color.WHITE);
						conGame.getBoard().getTiles()[i][j].addActionListener(this);
						conGame.getBoard().getTiles()[i][j].addMouseListener(this);
						bPanel.add(conGame.getBoard().getTiles()[i][j]);
					}
				}

				currentI = 0;
				currentJ = 0;
				clicked = 0;
				
				repaint();				
			}
		}
		else
		{			
			if(clicked%2 == 0)
			{
				currentI = ((Tile)(e.getSource())).getPlace().getX();
				currentJ = ((Tile)(e.getSource())).getPlace().getY();
				if(conGame.getBoard().getTiles()[currentI][currentJ].getWithShape())
				{
					conGame.getBoard().getTiles()[currentI][currentJ].setBackground(Color.DARK_GRAY);
					conGame.DijkstraMinPath(currentI,currentJ);
					mouseListenerActive = true;
					clicked++;
				}
			}
			else
			{
				eraseAll(false);
				
				conGame.moveStep(currentI, currentJ, destRow, destCol);
				if(conGame.isGameOver())
				{
					/////////////////////////// add implementation of game over ///////////////////
				}
				
				repaint();
				
				mouseListenerActive = false;
				clicked = 0;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{		
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		if(mouseListenerActive)
		{
			drawPath(e);
		}
	}
	
	private void drawPath(MouseEvent e)
	{
		destRow = ((Tile)(e.getComponent())).getPlace().getX();
		destCol = ((Tile)(e.getComponent())).getPlace().getY();
		Vector<Point> path = conGame.retrievePath(currentI, currentJ, destRow, destCol);
		if(path != null)
		{
			for(Point p : path)
			{
				conGame.getBoard().getTiles()[p.getX()][p.getY()].setBackground(Color.GRAY);
			}			
		}
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		eraseAll(true);
	}

	private void eraseAll(boolean currentTile)
	{
		for(Tile[] tRow : conGame.getBoard().getTiles())
		{
			for(Tile t : tRow)
			{
				if(!(t.getPlace().getX() == currentI && t.getPlace().getY() == currentJ && currentTile))
				{					
					t.setBackground(Color.WHITE);
				}
			}
		}		
	}
	
	@Override
	public void mousePressed(MouseEvent e) 
	{
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{	
	}
}