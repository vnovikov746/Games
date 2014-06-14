/**
 * 
 * Author: Vladimir Novikov, ID: 312669112.
 * 
 */

package logic;

import gui.GamePanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;

import sprites.PlayerSprite;
import board_objects.Board;
import board_objects.Tile;

public class XonixGame
{
	private final int NUM_OF_TILES = GamePanel.getNumOfTiles();
	private final int NUM_OF_EDGES = (((this.NUM_OF_TILES - 4) * 8) + 16);
	private final int NUM_OF_TILES_WITHOUT_EDGES = (this.NUM_OF_TILES * this.NUM_OF_TILES)
			- this.NUM_OF_EDGES;
	
	private int lives = 3;
	
	private Enemies enemies;
	private PlayerSprite player;
	private Board board;
	private int[][] boardStatus; // 0 - enemy, 1 - blue, 2 - black, 3 - cyan.
	private boolean gameOver;
	private boolean strike;
	private int newBlueTiles;
	private BufferedImage playerImage;
	
	public static float percentOfBlue;
	
	public XonixGame(BufferedImage enemyImage, BufferedImage playerImage)
			throws IOException
	{
		this.playerImage = playerImage;
		this.newBlueTiles = 0;
		XonixGame.percentOfBlue = 0;
		this.gameOver = false;
		this.strike = false;
		
		this.board = new Board();
		
		this.setPlayerOnBoard();
		
		this.enemies = new Enemies(enemyImage, this.board);
		
		this.boardStatus = new int[this.NUM_OF_TILES][this.NUM_OF_TILES];
	}
	
	private void setPlayerOnBoard()
	{
		int onBoardX = 0;
		int onBoardY = 0;
		Random ran = new Random();
		int tileSize = Tile.getTileSize();
		int playerLocX = 0;
		int playerLocY = 0;
		int playerSector = ran.nextInt(4);
		switch(playerSector)
		{
			case 0:
				onBoardX = ran.nextInt(2);
				onBoardY = ran.nextInt(this.NUM_OF_TILES);
				break;
			case 1:
				onBoardX = ran.nextInt(this.NUM_OF_TILES);
				onBoardY = ran.nextInt(2);
				break;
			case 2:
				onBoardX = ran.nextInt(this.NUM_OF_TILES);
				onBoardY = (this.NUM_OF_TILES - 2 + ran.nextInt(2));
				break;
			case 3:
				onBoardX = (this.NUM_OF_TILES - 2 + ran.nextInt(2));
				onBoardY = ran.nextInt(this.NUM_OF_TILES);
				break;
		}
		playerLocX = onBoardX * tileSize;
		playerLocY = onBoardY * tileSize;
		
		this.player = new PlayerSprite(playerLocX, playerLocY, tileSize,
				tileSize, tileSize, tileSize, onBoardX, onBoardY,
				this.playerImage, this.board);
		this.board.getTiles()[onBoardY][onBoardX].setStatus(Tile.BLUE);
	}
	
	public int getLives()
	{
		return this.lives;
	}
	
	public Enemies getEnemies()
	{
		return this.enemies;
	}
	
	public PlayerSprite getPlayer()
	{
		return this.player;
	}
	
	public Board getBoard()
	{
		return this.board;
	}
	
	public boolean getGameOver()
	{
		return this.gameOver;
	}
	
	public boolean getStrike()
	{
		return this.strike;
	}
	
	public void setStrike(boolean strike)
	{
		this.strike = strike;
	}
	
	private void initializeBoardStatus()
	{
		for(int i = 0; i < this.NUM_OF_TILES; i++)
		{
			for(int j = 0; j < this.NUM_OF_TILES; j++)
			{
				this.boardStatus[i][j] = this.board.getTiles()[i][j]
						.getStatus();
			}
		}
	}
	
	// public void movePlayer
	public void fillBlue()
	{
		this.newBlueTiles = 0;
		Stack<Point> enemyStack = new Stack<Point>();
		this.initializeBoardStatus();
		
		// Initialize stack of enemies
		for(int i = 0; i < this.NUM_OF_TILES; i++)
		{
			for(int j = 0; j < this.NUM_OF_TILES; j++)
			{
				if(this.boardStatus[i][j] == Tile.ENEMY)
				{
					enemyStack.push(new Point(i, j));
				}
			}
		}
		
		// set the status of all black neighbors to be 0
		while(!enemyStack.empty())
		{
			Point p = enemyStack.pop();
			for(int i = p.x - 1; i < p.x + 2; i++)
			{
				for(int j = p.y - 1; j < p.y + 2; j++)
				{
					if(i >= 0 && j >= 0 && i < this.NUM_OF_TILES
							&& j < this.NUM_OF_TILES)
					{
						if(this.boardStatus[i][j] == Tile.BLACK)
						{
							this.boardStatus[i][j] = Tile.ENEMY;
							enemyStack.push(new Point(i, j));
						}
					}
				}
			}
		}
		
		// paint all tiles in blue except tiles with status zero (=emeny)
		for(int i = 0; i < this.NUM_OF_TILES; i++)
		{
			for(int j = 0; j < this.NUM_OF_TILES; j++)
			{
				if(this.boardStatus[i][j] != Tile.ENEMY)
				{
					this.board.getTiles()[i][j].setColor(Color.BLUE);
					this.board.getTiles()[i][j].setStatus(Tile.BLUE);
					this.newBlueTiles++;
				}
			}
		}
		this.newBlueTiles -= this.NUM_OF_EDGES;
		XonixGame.percentOfBlue = ((float)this.newBlueTiles / (float)this.NUM_OF_TILES_WITHOUT_EDGES) * 100.0f;
	}
	
	public void drawPlayer(Graphics g)
	{
		this.player.drawSprite(g);
	}
	
	public void drawBoard(Graphics g)
	{
		for(Tile[] tRow : this.board.getTiles())
		{
			for(Tile t : tRow)
			{
				t.drawTile(g);
			}
		}
	}
	
	public void drawEnemies(Graphics g)
	{
		this.enemies.drawEnemies(g);
	}
	
	public void updateSprites()
	{
		if(!this.player.updateSprite() || !this.enemies.updateEnemies())
		{
			this.strike = true;
			this.lives--;
			if(this.lives == 0)
			{
				this.gameOver = true;
			}
			else
			{
				for(int i = 0; i < this.NUM_OF_TILES; i++)
				{
					for(int j = 0; j < this.NUM_OF_TILES; j++)
					{
						if(this.board.getTiles()[i][j].getStatus() == Tile.CYAN)
						{
							this.board.getTiles()[i][j].setStatus(Tile.BLACK);
							this.board.getTiles()[i][j].setColor(Color.BLACK);
						}
					}
				}
				try
				{
					Thread.sleep(100);
				}
				catch(InterruptedException e)
				{}
				
				this.setPlayerOnBoard();
			}
		}
	}
}
