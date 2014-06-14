/**
 * 
 * Author: Vladimir Novikov, ID: 312669112.
 * 
 */

package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

import logic.XonixGame;
import board_objects.Score;
import board_objects.Tile;

public class GamePanel extends JPanel implements Runnable
{
	private static final long serialVersionUID = -7107225064475417807L;
	
	private static final int PWIDTH = 560, PHEIGHT = 600, PERIOD = 40,
			NUM_OF_TILES = 70;
	
	private Image bgImage = null;
	private BufferedImage dbImg = null;
	private BufferedImage enemyImage = null;
	private BufferedImage playerImage = null;
	private boolean running;
	private boolean isGameOver;
	private int playerStatus;
	private int prevPlayerStatus;
	
	protected static XonixGame xonixGame;
	Graphics dbg;
	
	public GamePanel() throws IOException
	{
		this.setFocusable(true);
		this.requestFocusInWindow();
		
		// File imgFile = new File(".");
		// String str = imgFile.getAbsolutePath() + "//pacman.jpg";
		// imgFile = new File(str);
		// this.enemyImage = ImageIO.read(imgFile);
		
		// this.bgImage = Toolkit.getDefaultToolkit().getImage(
		// (new File(".").getAbsolutePath() + "//wallpaper.jpg"));
		
		xonixGame = new XonixGame(this.enemyImage, this.playerImage);
		
		this.isGameOver = false;
		this.playerStatus = Tile.BLUE;
		this.prevPlayerStatus = Tile.BLUE;
		
		this.addKeyListener(new Listener());
		this.dbImg = new BufferedImage(PWIDTH, PHEIGHT, BufferedImage.OPAQUE);
		this.dbg = this.dbImg.createGraphics();
	}
	
	public static int getPwidth()
	{
		return PWIDTH;
	}
	
	public static int getPheight()
	{
		return PHEIGHT;
	}
	
	public static int getNumOfTiles()
	{
		return NUM_OF_TILES;
	}
	
	@Override
	public void run()
	{
		long before, sleepTime;
		before = System.currentTimeMillis();
		this.running = true;
		
		while(this.running)
		{
			this.gameUpdate();
			this.gameRender();
			this.paintScreen(); // active rendering
			
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
	
	public void gameRender()
	{
		this.dbg.setColor(Color.WHITE);
		this.dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
		this.dbg.drawImage(this.bgImage, 0, 0, this);
		
		// draw game elements
		xonixGame.drawBoard(this.dbg);
		xonixGame.drawPlayer(this.dbg);
		xonixGame.drawEnemies(this.dbg);
		Score.drawScore(this.dbg, "Lives: " + xonixGame.getLives()
				+ "      Score: " + (int)XonixGame.percentOfBlue
				+ "%      Target: 80%", 120, 590);
		
		if(this.isGameOver)
		{
			this.gameOverMessage(this.dbg);
			this.running = false;
		}
	}
	
	public void paintScreen()
	{
		Graphics g;
		try
		{
			g = this.getGraphics();
			if(g != null && this.dbImg != null)
			{
				g.drawImage(this.dbImg, 0, 0, null);
			}
		}
		catch(Exception e)
		{
			System.out.println("Graphics error");
			e.printStackTrace();
		}
	}
	
	public void gameUpdate()
	{
		xonixGame.updateSprites();
		if(xonixGame.getGameOver())
		{
			this.dbg.setColor(Color.WHITE);
			this.dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
			Score.drawScore(this.dbg, "You LOOSE, you closed only: "
					+ (int)XonixGame.percentOfBlue + "%", 150, 270);
			this.paintScreen(); // active rendering
			while(true)
			{
				;
			}
		}
		else if((int)XonixGame.percentOfBlue >= 80)
		{
			this.dbg.setColor(Color.WHITE);
			this.dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
			Score.drawScore(this.dbg, "You WON!! Your closed: "
					+ (int)XonixGame.percentOfBlue + "%", 150, 270);
			this.paintScreen(); // active rendering
			while(true)
			{
				;
			}
		}
		this.prevPlayerStatus = this.playerStatus;
		this.playerStatus = xonixGame.getBoard().getTiles()[xonixGame
				.getPlayer().getOnBoardY()][xonixGame.getPlayer().getOnBoardX()]
				.getStatus();
		if(GamePanel.this.playerStatus == Tile.BLUE
				&& GamePanel.this.prevPlayerStatus == Tile.CYAN
				&& !xonixGame.getStrike())
		{
			xonixGame.fillBlue();
			xonixGame.getPlayer().stop();
		}
		xonixGame.setStrike(false);
	}
	
	private class Listener extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			if(e.getKeyCode() == KeyEvent.VK_LEFT)
			{
				GamePanel.xonixGame.getPlayer().moveLeft();
			}
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				GamePanel.xonixGame.getPlayer().moveRight(NUM_OF_TILES);
			}
			else if(e.getKeyCode() == KeyEvent.VK_UP)
			{
				GamePanel.xonixGame.getPlayer().moveUp();
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN)
			{
				GamePanel.xonixGame.getPlayer().moveDown(NUM_OF_TILES);
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e)
		{
			GamePanel.xonixGame.getPlayer().stop();
		}
	}
	
	// only start the animation once the JPanel has been added to the JFrame
	@Override
	public void addNotify()
	{
		super.addNotify(); // creates the peer
		this.startGame(); // start the thread
	}
	
	public void startGame()
	{
		(new Thread(this)).start();
	}
	
	public void gameOver()
	{
		this.isGameOver = true;
	}
	
	private void gameOverMessage(Graphics g)
	{
		g.setFont(new Font("Arial", Font.PLAIN, 30));
		g.setColor(Color.WHITE);
		g.drawString("Game Over", PWIDTH / 2 - 30, PHEIGHT / 2);
	}
}
