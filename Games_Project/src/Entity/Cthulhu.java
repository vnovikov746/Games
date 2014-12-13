package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Cthulhu extends MapObject
{
	private BufferedImage[] sprites;
	
	public Cthulhu(TileMap tm)
	{
		super(tm);
		
		this.width = 101;
		this.height = 171;
		this.cwidth = 20;
		this.cheight = 20;
		
		// load sprites
		try
		{
			BufferedImage spritesheet = ImageIO.read(this.getClass()
					.getResourceAsStream("/Sprites/Enemies/cthulhu.gif"));
			
			this.sprites = new BufferedImage[1];
			for(int i = 0; i < this.sprites.length; i++)
			{
				this.sprites[i] = spritesheet.getSubimage(i * this.width, 0,
						this.width, this.height);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		this.animation = new Animation();
		this.animation.setFrames(this.sprites);
		this.animation.setDelay(300);
		
		this.left = true;
		this.facingRight = false;
	}
	
	public void update()
	{
		this.animation.update();
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		this.setMapPosition();
		super.draw(g);
	}
}
