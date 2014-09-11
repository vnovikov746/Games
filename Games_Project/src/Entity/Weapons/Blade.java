package Entity.Weapons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.MapObject;
import TileMap.TileMap;

public class Blade extends MapObject
{
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	
	private BufferedImage[] hitSprites;
	
	public Blade(TileMap tm, boolean right)
	{
		super(tm);
		
		this.facingRight = right;
		
		this.moveSpeed = 3.8;
		if(right)
		{
			this.dx = this.moveSpeed;
		}
		else
		{
			this.dx = -this.moveSpeed;
		}
		
		this.width = 30;
		this.height = 30;
		this.cwidth = 14;
		this.cheight = 14;
		
		// load sprites
		try
		{
			
			BufferedImage spritesheet = ImageIO.read(this.getClass()
					.getResourceAsStream(
							"/Sprites/Player/small-blade-flying.gif"));
			
			this.sprites = new BufferedImage[8];
			for(int i = 0; i < this.sprites.length; i++)
			{
				this.sprites[i] = spritesheet.getSubimage(i * this.width, 0,
						this.width, this.height);
			}
			
			this.hitSprites = new BufferedImage[2];
			for(int i = 0; i < this.hitSprites.length; i++)
			{
				this.hitSprites[i] = spritesheet.getSubimage(i * this.width,
						this.height, this.width, this.height);
			}
			
			this.animation = new Animation();
			this.animation.setFrames(this.sprites);
			this.animation.setDelay(70);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setHit()
	{
		if(this.hit)
		{
			return;
		}
		this.hit = true;
		this.animation.setFrames(this.hitSprites);
		this.animation.setDelay(70);
		this.dx = 0;
	}
	
	public boolean shouldRemove()
	{
		return this.remove;
	}
	
	public void update()
	{
		this.checkTileMapCollision();
		this.setPosition(this.xtemp, this.ytemp);
		
		if(this.dx == 0 && !this.hit)
		{
			this.setHit();
		}
		
		this.animation.update();
		if(this.hit && this.animation.hasPlayedOnce())
		{
			this.remove = true;
		}
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		this.setMapPosition();
		
		super.draw(g);
	}
}
