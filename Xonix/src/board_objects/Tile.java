package board_objects;

import java.awt.Color;
import java.awt.Graphics;

import sprites.PlayerSprite;

public class Tile
{
	public static int ENEMY = 0, BLUE = 1, BLACK = 2, CYAN = 3;
	private static int tileSize = PlayerSprite.getSize();
	private int locX;
	private int locY;
	private Color color;
	private int status;
	
	public Tile(int locX, int locY, Color color)
	{
		this.locX = locX;
		this.locY = locY;
		this.color = color;
	}
	
	public static int getTileSize()
	{
		return tileSize;
	}
	
	public int getLocX()
	{
		return this.locX;
	}
	
	public int getLocY()
	{
		return this.locY;
	}
	
	public Color getColor()
	{
		return this.color;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public int getStatus()
	{
		return this.status;
	}
	
	public void setStatus(int status)
	{
		this.status = status;
	}
	
	public void drawTile(Graphics g)
	{
		g.setColor(this.color);
		g.fillRect(this.locX, this.locY, tileSize, tileSize);
		// g.setColor(Color.WHITE);
		// g.drawRect(this.locX, this.locY, tileSize, tileSize);
	}
}
