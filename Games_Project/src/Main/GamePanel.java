package Main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import GameState.GameStateManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener
{
	// dimensions
	public static final int WIDTH = 640;
	public static final int HEIGHT = 320;
	public static final int SCALE = 2;
	
	// game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / this.FPS;
	
	// image
	private BufferedImage image;
	private Graphics2D g;
	
	// game state manager
	private GameStateManager gsm;
	
	public GamePanel()
	{
		super();
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		this.setFocusable(true);
		this.requestFocus();
	}
	
	@Override
	public void addNotify()
	{
		super.addNotify();
		if(this.thread == null)
		{
			this.thread = new Thread(this);
			this.addKeyListener(this);
			this.thread.start();
		}
	}
	
	private void init()
	{
		
		this.image = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		this.g = (Graphics2D)this.image.getGraphics();
		
		this.running = true;
		
		this.gsm = new GameStateManager();
		
	}
	
	@Override
	public void run()
	{
		
		this.init();
		
		long start;
		long elapsed;
		long wait;
		
		// game loop
		while(this.running)
		{
			
			start = System.nanoTime();
			
			this.update();
			this.draw();
			this.drawToScreen();
			
			elapsed = System.nanoTime() - start;
			
			wait = this.targetTime - elapsed / 1000000;
			if(wait < 0)
			{
				wait = 5;
			}
			
			try
			{
				Thread.sleep(wait);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void update()
	{
		this.gsm.update();
	}
	
	private void draw()
	{
		this.gsm.draw(this.g);
	}
	
	private void drawToScreen()
	{
		Graphics g2 = this.getGraphics();
		g2.drawImage(this.image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();
	}
	
	@Override
	public void keyTyped(KeyEvent key)
	{}
	
	@Override
	public void keyPressed(KeyEvent key)
	{
		this.gsm.keyPressed(key.getKeyCode());
	}
	
	@Override
	public void keyReleased(KeyEvent key)
	{
		this.gsm.keyReleased(key.getKeyCode());
	}
}
