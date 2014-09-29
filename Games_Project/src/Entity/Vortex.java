package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Vortex extends MapObject
{
	private BufferedImage[] sprites;
	
	public Vortex(TileMap tm)
	{
		super(tm);
		
		this.width = 30;
		this.height = 30;
		this.cwidth = 20;
		this.cheight = 20;
		
		// load sprites
		try
		{
			BufferedImage spritesheet = ImageIO.read(this.getClass()
					.getResourceAsStream("/Sprites/Vortex/vortex.gif"));
			
			this.sprites = new BufferedImage[4];
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
