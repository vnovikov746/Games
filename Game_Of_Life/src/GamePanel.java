import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, MouseListener
{
	private static final long serialVersionUID = -4013295827861204335L;
	
	protected static final int WIDTH = 1000;
	protected static final int HEIGHT = 1000;
	
	private Board board;
	private Buttons btns;
	
	private int PERIOD = 10;
	private boolean running;
	
	private Graphics g;
	private BufferedImage img;
	
	private Thread thread;
	
	public GamePanel()
	{
		super();
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT + 27));
		this.setFocusable(true);
		this.requestFocus();
		this.setLayout(new BorderLayout());
		this.addMouseListener(this);
	}
	
	private void init()
	{
		this.btns = new Buttons();
		this.add(btns, BorderLayout.SOUTH);
		
		this.board = new Board();
		
		this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.OPAQUE);
		this.g = this.img.createGraphics();
		
		this.running = true;
		
		gameRender();
		paintScreen();
	}
	
	@Override
	public void run()
	{
		this.init();
		
		long before, sleepTime;
		before = System.currentTimeMillis();
		this.running = true;
		
		while(this.running)
		{
			gameRender();
			paintScreen();
			
			if(Buttons.RANDOM)
			{
				Random ran = new Random();
				board.clearBoard();
				board.randomBoard(ran.nextInt() % 50);
				this.gameRender();
				this.paintScreen();
				Buttons.RANDOM = false;
				Buttons.START = false;
			}
			else if(Buttons.STOP)
			{
				board.clearBoard();
				this.gameRender();
				this.paintScreen();
				Buttons.START = false;
				Buttons.STOP = false;
			}
			else if(Buttons.PAUSE)
			{
				Buttons.START = false;
				Buttons.PAUSE = false;
			}
			else if(Buttons.START)
			{
				this.gameUpdate();
				
				sleepTime = PERIOD - before;
				if(sleepTime <= 0)
				{
					sleepTime = 50;
				}
				try
				{
					Thread.sleep(sleepTime);
				}
				catch(InterruptedException e)
				{}
				
				before = System.currentTimeMillis();
			}
		}
	}
	
	@Override
	public void addNotify()
	{
		super.addNotify();
		if(this.thread == null)
		{
			this.thread = new Thread(this);
			this.thread.start();
		}
	}
	
	public void gameUpdate()
	{
		board.update();
	}
	
	public void gameRender()
	{
		this.board.draw(this.g);
	}
	
	public void paintScreen()
	{
		Graphics g2;
		try
		{
			g2 = this.getGraphics();
			if(g2 != null && this.img != null)
			{
				g2.drawImage(this.img, 0, 0, null);
			}
		}
		catch(Exception e)
		{
			System.out.println("Graphics error");
			e.printStackTrace();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		int cellX = e.getX() / Board.RESOLUTION;
		int cellY = e.getY() / Board.RESOLUTION;
		if(board.getTiles()[cellX][cellY].getStatus() == false)
		{
			board.getTiles()[cellX][cellY].setStatus(true);
		}
		else
		{
			board.getTiles()[cellX][cellY].setStatus(false);
		}
		gameRender();
		paintScreen();
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{}
	
	@Override
	public void mouseExited(MouseEvent e)
	{}
	
	@Override
	public void mousePressed(MouseEvent e)
	{}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{}
}
