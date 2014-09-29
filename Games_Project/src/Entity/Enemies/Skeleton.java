package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;

public class Skeleton extends Enemy
{
	private BufferedImage[] sprites;
	
	public Skeleton(TileMap tm)
	{
		super(tm);
		
		this.moveSpeed = 1.0;
		this.maxSpeed = 1.0;
		this.fallSpeed = 0.2;
		this.maxFallSpeed = 10.0;
		
		this.width = 30;
		this.height = 30;
		this.cwidth = 20;
		this.cheight = 20;
		
		this.health = this.maxHealth = 2;
		this.damage = 1;
		this.jumpOnDamage = 0;
		
		// load sprites
		try
		{
			
			BufferedImage spritesheet = ImageIO.read(this.getClass()
					.getResourceAsStream("/Sprites/Enemies/Skeleton.gif"));
			
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
	
	private void getNextPosition()
	{
		// movement
		if(this.left)
		{
			this.dx -= this.moveSpeed;
			if(this.dx < -this.maxSpeed)
			{
				this.dx = -this.maxSpeed;
			}
		}
		else if(this.right)
		{
			this.dx += this.moveSpeed;
			if(this.dx > this.maxSpeed)
			{
				this.dx = this.maxSpeed;
			}
		}
		
		// falling
		if(this.falling)
		{
			this.dy += this.fallSpeed;
		}
	}
	
	@Override
	public void update()
	{
		// update position
		this.getNextPosition();
		
		super.update();
		
		this.checkTileMapCollision();
		this.setPosition(this.xtemp, this.ytemp);
		
		// check flinching
		if(this.flinching)
		{
			long elapsed = (System.nanoTime() - this.flinchingTimer) / 1000000;
			if(elapsed > 400)
			{
				this.flinching = false;
			}
		}
		
		// if it hits a wall, go other direction
		if(this.right && this.dx == 0)
		{
			this.right = false;
			this.left = true;
			this.facingRight = false;
		}
		else if(this.left && this.dx == 0)
		{
			this.right = true;
			this.left = false;
			this.facingRight = true;
		}
		
		// update animation
		this.animation.update();
		
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		this.setMapPosition();
		
		super.draw(g);
	}
}
