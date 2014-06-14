/**
 * 
 * Author: Vladimir Novikov, ID: 312669112.
 * 
 */

package sprites;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Sprite
{
	private BufferedImage bImage;
	protected int imageWidth, imageHeight; // image dimensions
			
	protected int locX, locY, dx, dy, onBoardX, onBoardY;
	protected int pWidth, pHeight; // panel's dimensions
			
	public Sprite(int x, int y, int w, int h, int dx, int dy, int onBoardX,
			int onBoardY, BufferedImage img)
	{
		this.locX = x;
		this.locY = y;
		this.dx = dx;
		this.dy = dy;
		this.pWidth = w;
		this.pHeight = h;
		this.onBoardX = onBoardX;
		this.onBoardY = onBoardY;
		this.bImage = img;
		if(this.bImage != null)
		{
			this.imageWidth = this.bImage.getWidth(null);
			this.imageHeight = this.bImage.getHeight(null);
		}
		else
		{
			this.setImageDimensions();
		}
	}
	
	public Rectangle getBoundingBox()
	{
		return new Rectangle(this.getLocX(), this.getLocY(), this.imageWidth,
				this.imageHeight);
	}
	
	public int getLocX()
	{
		return this.locX;
	}
	
	public int getLocY()
	{
		return this.locY;
	}
	
	public int getDx()
	{
		return this.dx;
	}
	
	public int getDy()
	{
		return this.dy;
	}
	
	public int getImageWidth()
	{
		return this.imageWidth;
	}
	
	public int getImageHeight()
	{
		return this.imageHeight;
	}
	
	public boolean updateSprite()
	{
		this.locX += this.dx;
		this.locY += this.dy;
		return true;
	}
	
	public int getOnBoardX()
	{
		return this.onBoardX;
	}
	
	public int getOnBoardY()
	{
		return this.onBoardY;
	}
	
	public abstract void setImageDimensions();
	
	public abstract void drawSprite(Graphics g);
}
