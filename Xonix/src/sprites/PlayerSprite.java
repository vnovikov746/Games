/**
 * 
 * Author: Vladimir Novikov, ID: 312669112.
 * 
 */

package sprites;

import gui.GamePanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import board_objects.Board;
import board_objects.Tile;

public class PlayerSprite extends Sprite
{
	private static final int SIZE = 8;
	private final int INSIDE = 4;
	private boolean isMoving, atRWall, atLWall, atUWall, atDWall, left, right,
			up, down;
	private int numOfTiles = GamePanel.getNumOfTiles(), tileSize = Tile
			.getTileSize();
	private Board board;
	
	public PlayerSprite(int x, int y, int w, int h, int dx, int dy,
			int onBoardX, int onBoardY, BufferedImage playerImage, Board board)
	{
		super(x, y, w, h, dx, dy, onBoardX, onBoardY, playerImage);
		
		this.board = board;
		
		this.isMoving = false;
		
		this.atLWall = false;
		this.atRWall = false;
		this.atUWall = false;
		this.atDWall = false;
		
		if(onBoardX == 0)
		{
			this.atLWall = true;
		}
		else if(onBoardX == this.numOfTiles - 1)
		{
			this.atRWall = true;
		}
		if(onBoardY == 0)
		{
			this.atUWall = true;
		}
		else if(onBoardY == this.numOfTiles - 1)
		{
			this.atDWall = true;
		}
		
		this.left = false;
		this.right = false;
		this.up = false;
		this.down = false;
	}
	
	public static int getSize()
	{
		return SIZE;
	}
	
	public int getInside()
	{
		return this.INSIDE;
	}
	
	@Override
	public int getOnBoardX()
	{
		return this.onBoardX;
	}
	
	@Override
	public int getOnBoardY()
	{
		return this.onBoardY;
	}
	
	@Override
	public void setImageDimensions()
	{
		this.imageWidth = this.imageHeight = SIZE;
	}
	
	@Override
	public void drawSprite(Graphics g)
	{
		g.setColor(Color.MAGENTA);
		g.fillRect(this.locX, this.locY, this.imageWidth, this.imageHeight);
		g.setColor(Color.GRAY);
		g.fillRect(this.locX + 2, this.locY + 2, this.INSIDE, this.INSIDE);
	}
	
	@Override
	public boolean updateSprite()
	{
		int numOfTiles = GamePanel.getNumOfTiles();
		if(!this.isMoving || (this.left && this.atLWall)
				|| (this.right && this.atRWall) || (this.up && this.atUWall)
				|| (this.down && this.atDWall))
		{
			return true;
		}
		
		if(this.left || this.right)
		{
			this.locX += this.dx;
			this.onBoardX += Math.signum(this.dx);
		}
		else if(this.up || this.down)
		{
			this.locY += this.dy;
			this.onBoardY += Math.signum(this.dy);
		}
		
		if(this.board.getTiles()[this.onBoardY][this.onBoardX].getStatus() == Tile.CYAN)
		{
			return false;
		}
		
		if(this.board.getTiles()[this.onBoardY][this.onBoardX].getStatus() == Tile.BLACK)
		{
			this.board.getTiles()[this.onBoardY][this.onBoardX]
					.setColor(Color.CYAN);
			this.board.getTiles()[this.onBoardY][this.onBoardX]
					.setStatus(Tile.CYAN);
			return true;
		}
		
		if(this.onBoardX == 0)
		{
			this.isMoving = false;
			this.atLWall = true;
		}
		else if(this.onBoardX == numOfTiles - 1)
		{
			this.isMoving = false;
			this.atRWall = true;
		}
		if(this.onBoardY == 0)
		{
			this.isMoving = false;
			this.atUWall = true;
		}
		else if(this.onBoardY == numOfTiles - 1)
		{
			this.isMoving = false;
			this.atDWall = true;
		}
		return true;
	}
	
	public void moveLeft()
	{
		if(this.atLWall)
		{
			return;
		}
		
		this.dx = -this.tileSize;
		
		this.isMoving = true;
		this.atRWall = false;
		this.left = true;
		this.right = false;
		this.up = false;
		this.down = false;
	}
	
	public void moveRight(int numOfTiles)
	{
		if(this.atRWall)
		{
			return;
		}
		
		this.dx = this.tileSize;
		
		this.isMoving = true;
		this.atLWall = false;
		this.left = false;
		this.right = true;
		this.up = false;
		this.down = false;
	}
	
	public void moveUp()
	{
		if(this.atUWall)
		{
			return;
		}
		
		this.dy = -this.tileSize;
		
		this.isMoving = true;
		this.atDWall = false;
		this.left = false;
		this.right = false;
		this.up = true;
		this.down = false;
	}
	
	public void moveDown(int numOfTiles)
	{
		if(this.atDWall)
		{
			return;
		}
		
		this.dy = this.tileSize;
		
		this.isMoving = true;
		this.atUWall = false;
		this.left = false;
		this.right = false;
		this.up = false;
		this.down = true;
	}
	
	public void stop()
	{
		if(this.board.getTiles()[this.onBoardY][this.onBoardX].getStatus() == Tile.BLUE)
		{
			this.isMoving = false;
		}
	}
}
