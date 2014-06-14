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

public class EnemySprite extends Sprite
{
	private static final int SIZE = 8;
	private BufferedImage enemyImage;
	private Board board;
	
	public EnemySprite(int x, int y, int w, int h, int dx, int dy,
			int onBoardX, int onBoardY, BufferedImage enemyImage, Board board)
	{
		super(x, y, w, h, dx, dy, onBoardX, onBoardY, enemyImage);
		this.enemyImage = enemyImage;
		this.board = board;
	}
	
	public static int getSize()
	{
		return SIZE;
	}
	
	@Override
	public void setImageDimensions()
	{
		this.imageWidth = this.imageHeight = SIZE;
	}
	
	@Override
	public void drawSprite(Graphics g)
	{
		if(this.enemyImage == null)
		{
			g.setColor(Color.WHITE);
			g.fillOval(this.locX, this.locY, this.imageWidth, this.imageHeight);
		}
		else
		{
			g.drawImage(this.enemyImage, this.locX, this.locY, null);
		}
	}
	
	@Override
	public boolean updateSprite()
	{
		int numOfTiles = GamePanel.getNumOfTiles();
		
		this.bounceAtBlueWall(numOfTiles);
		
		this.board.getTiles()[this.onBoardY][this.onBoardX]
				.setStatus(Tile.BLACK);
		
		if(this.onBoardX >= 1 && this.onBoardX < numOfTiles - 1)
		{
			this.locX += this.dx;
			this.onBoardX += (int)Math.signum(this.dx);
		}
		if(this.onBoardY >= 1 && this.onBoardY < numOfTiles - 1)
		{
			this.locY += this.dy;
			this.onBoardY += (int)Math.signum(this.dy);
		}
		
		if(this.board.getTiles()[this.onBoardY][this.onBoardX].getStatus() == Tile.CYAN)
		{
			return false;
		}
		
		this.board.getTiles()[this.onBoardY][this.onBoardX]
				.setStatus(Tile.ENEMY);
		
		return true;
	}
	
	private void bounceAtBlueWall(int numOfTiles)
	{
		if((this.onBoardY + 1 < numOfTiles && this.board.getTiles()[this.onBoardY + 1][this.onBoardX]
				.getStatus() == Tile.BLUE)
				|| (this.onBoardY - 1 >= 0 && this.board.getTiles()[this.onBoardY - 1][this.onBoardX]
						.getStatus() == Tile.BLUE))
		{
			this.dy = -this.dy;
		}
		if((this.onBoardX + 1 < numOfTiles && this.board.getTiles()[this.onBoardY][this.onBoardX + 1]
				.getStatus() == Tile.BLUE)
				|| (this.onBoardX - 1 >= 0 && this.board.getTiles()[this.onBoardY][this.onBoardX - 1]
						.getStatus() == Tile.BLUE))
		{
			this.dx = -this.dx;
		}
	}
}
