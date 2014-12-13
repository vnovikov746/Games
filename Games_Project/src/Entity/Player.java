package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import Entity.Weapons.Blade;
import Main.GamePanel;
import TileMap.TileMap;

public class Player extends MapObject
{
	// player stuff
	private int health;
	private int maxHealth;
	private int numOfBlades;
	private int maxBlades;
	private int numOfAvril;
	private int maxAvril;
	private boolean dead;
	private boolean restartLevel;
	private boolean flinching;
	private long flinchTimer;
	private int score;
	
	// razor blades
	private boolean firing;
	private int bladeCost;
	private int bladeDamage;
	private ArrayList<Blade> blades;
	
	// avril lavigne
	private boolean avrilActivated;
	private boolean avrilInUse;
	private int avrilCost;
	private long timeOfAvrilStart;
	private long timeOfAvrilStop;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {2, 3, 3, 4, 2}; // idle, left walk,
														// right walk, falling,
														// razorblade
														// (jumping is same as
														// idle)
														// SPRITES
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int RAZORBLADE = 4;
	
	private HashMap<String, AudioPlayer> sfx;
	
	public Player(TileMap tm)
	{
		super(tm);
		
		this.width = 30;
		this.height = 30;
		this.cwidth = 20;
		this.cheight = 20;
		
		this.moveSpeed = 0.3;
		this.maxSpeed = 1.6;
		this.stopSpeed = 0.4;
		this.fallSpeed = 0.30;
		this.maxFallSpeed = 4.0;
		this.jumpStart = -8.3;
		this.stopJumpSpeed = 0.3;
		
		this.facingRight = true;
		
		this.health = this.maxHealth = 3;
		this.dead = false;
		this.restartLevel = false;
		
		this.score = 0;
		
		this.maxBlades = 10;
		this.bladeCost = 1;
		this.bladeDamage = 2;
		this.blades = new ArrayList<Blade>();
		
		this.maxAvril = 5;
		this.avrilCost = 1;
		
		// load sprites
		try
		{
			BufferedImage spritesheet = ImageIO
					.read(this.getClass().getResourceAsStream(
							"/Sprites/Player/emoplayersprites.gif"));
			
			this.sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 5; i++)
			{
				BufferedImage[] bi = new BufferedImage[this.numFrames[i]];
				
				for(int j = 0; j < this.numFrames[i]; j++)
				{
					
					bi[j] = spritesheet.getSubimage(j * this.width, i
							* this.height, this.width, this.height);
				}
				
				this.sprites.add(bi);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		this.animation = new Animation();
		this.currentAction = IDLE;
		this.animation.setFrames(this.sprites.get(IDLE));
		this.animation.setDelay(400);
		
		this.sfx = new HashMap<String, AudioPlayer>();
		this.sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
		// TODO add avril songs
	}
	
	public int getCurrentAction()
	{
		return this.currentAction;
	}
	
	public int getHealth()
	{
		return this.health;
	}
	
	public void setHealth(int health)
	{
		this.health = health;
	}
	
	public boolean getDead()
	{
		return this.dead;
	}
	
	public void setDead(boolean dead)
	{
		this.dead = true;
	}
	
	public boolean getRestartLevel()
	{
		return this.restartLevel;
	}
	
	public int getScore()
	{
		return this.score;
	}
	
	public void setScore(int score)
	{
		this.score = score;
	}
	
	public int getMaxHealth()
	{
		return this.maxHealth;
	}
	
	public int getNumOfBlades()
	{
		return this.numOfBlades;
	}
	
	public void setNumOfBlades(int numOfBlades)
	{
		this.numOfBlades = numOfBlades;
	}
	
	public int getMaxBlades()
	{
		return this.maxBlades;
	}
	
	public void setFiring()
	{
		this.firing = true;
	}
	
	public int getNumOfAvril()
	{
		return this.numOfAvril;
	}
	
	public void setNumOfAvril(int numOfAvril)
	{
		this.numOfAvril = numOfAvril;
	}
	
	public int getMaxAvril()
	{
		return this.maxAvril;
	}
	
	public void setAvrilActivated()
	{
		this.avrilActivated = true;
	}
	
	public boolean getAvrilInUse()
	{
		return this.avrilInUse;
	}
	
	public void checkAttack(ArrayList<Enemy> enemies)
	{
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++)
		{
			Enemy e = enemies.get(i);
			
			// blades
			for(int j = 0; j < this.blades.size(); j++)
			{
				if(this.blades.get(j).intersects(e))
				{
					e.hit(this.bladeDamage);
					this.blades.get(j).setHit();
					if(e.isDead())
					{
						this.score += e.getMaxHealth() * 10;
					}
					break;
				}
			}
			
			if(this.avrilActivated && this.numOfAvril > 0)
			{
				this.timeOfAvrilStart = System.currentTimeMillis();
				e.setMoveSpeed(e.getMoveSpeed() - 0.4);
				e.setMaxSpeed(e.getMaxSpeed() - 0.4);
			}
			
			// crash enemy from above
			if(this.currentAction == Player.FALLING)
			{
				if(this.intersects(e))
				{
					if(e.getJumpOnDamage() != 0)
					{
						this.score += e.getMaxHealth() * 10;
						e.hit(e.getJumpOnDamage());
					}
				}
			}
			
			// check enemy collision
			else if(this.intersects(e))
			{
				this.hit(e.getDamage());
			}
		}
		
		if(this.avrilActivated && this.numOfAvril > 0)
		{
			this.numOfAvril -= this.avrilCost;
			this.avrilActivated = false;
			this.avrilInUse = true;
		}
	}
	
	public void hit(int damage)
	{
		if(this.flinching)
		{
			return;
		}
		this.health -= damage;
		if(this.health < 0)
		{
			this.health = 0;
		}
		if(this.health == 0)
		{
			this.dead = true;
		}
		this.flinching = true;
		this.flinchTimer = System.nanoTime();
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
		else
		{
			if(this.dx > 0)
			{
				this.dx -= this.stopSpeed;
				if(this.dx < 0)
				{
					this.dx = 0;
				}
			}
			else if(this.dx < 0)
			{
				this.dx += this.stopSpeed;
				if(this.dx > 0)
				{
					this.dx = 0;
				}
			}
		}
		
		// cannot move while attacking, except in air
		if(this.currentAction == RAZORBLADE && !(this.jumping || this.falling))
		{
			this.dx = 0;
		}
		
		// jumping
		if(this.jumping && !this.falling)
		{
			this.sfx.get("jump").play();
			this.dy = this.jumpStart;
			this.falling = true;
		}
		
		// falling
		if(this.falling)
		{
			this.dy += this.fallSpeed;
			
			if(this.dy > 0)
			{
				this.jumping = false;
			}
			if(this.dy < 0 && !this.jumping)
			{
				this.dy += this.stopJumpSpeed;
			}
			
			if(this.dy > this.maxFallSpeed)
			{
				this.dy = this.maxFallSpeed;
			}
		}
	}
	
	public void update(ArrayList<Enemy> enemies)
	{
		// update position
		this.getNextPosition();
		
		if(this.gety() >= GamePanel.HEIGHT - (this.cheight * 2))
		{
			this.restartLevel = true;
		}
		
		this.checkTileMapCollision();
		
		// add collectible item if collected
		if(this.getBladeCollected())
		{
			this.numOfBlades += 1;
			if(this.numOfBlades > this.maxBlades)
			{
				this.numOfBlades = this.maxBlades;
			}
			this.setBladeCollected(false);
		}
		if(this.getAvrilCollected())
		{
			this.numOfAvril += 1;
			if(this.numOfAvril > this.maxAvril)
			{
				this.numOfAvril = this.maxAvril;
			}
			this.setAvrilCollected(false);
		}
		if(this.getBloodDropCollected())
		{
			this.score += 100;
			this.setBloodDropCollected(false);
		}
		
		this.setPosition(this.xtemp, this.ytemp);
		
		// check attack has stopped
		if(this.avrilInUse)
		{
			this.timeOfAvrilStop = System.currentTimeMillis();
			if((this.timeOfAvrilStop - this.timeOfAvrilStart) > 5000)
			{
				for(int i = 0; i < enemies.size(); i++)
				{
					Enemy e = enemies.get(i);
					e.setMoveSpeed(e.getMoveSpeed() + 0.4);
					e.setMaxSpeed(e.getMaxSpeed() + 0.4);
				}
				this.avrilInUse = false;
			}
		}
		
		if(this.currentAction == RAZORBLADE)
		{
			if(this.animation.hasPlayedOnce())
			{
				this.firing = false;
			}
		}
		
		// razor blade attack
		if(this.firing && this.currentAction != RAZORBLADE)
		{
			if(this.numOfBlades >= this.bladeCost)
			{
				this.numOfBlades -= this.bladeCost;
				Blade b = new Blade(this.tileMap, this.facingRight);
				b.setPosition(this.x, this.y);
				this.blades.add(b);
			}
		}
		
		// update blades
		for(int i = 0; i < this.blades.size(); i++)
		{
			this.blades.get(i).update();
			if(this.blades.get(i).shouldRemove())
			{
				this.blades.remove(i);
				i--;
			}
		}
		
		// check done flinching
		if(this.flinching)
		{
			long elapsed = (System.nanoTime() - this.flinchTimer) / 1000000;
			if(elapsed > 1000)
			{
				this.flinching = false;
			}
		}
		
		// set animation
		if(this.firing)
		{
			if(this.currentAction != RAZORBLADE)
			{
				this.currentAction = RAZORBLADE;
				this.animation.setFrames(this.sprites.get(RAZORBLADE));
				this.animation.setDelay(100);
				this.width = 30;
			}
		}
		else if(this.dy > 0)
		{
			if(this.currentAction != FALLING)
			{
				this.currentAction = FALLING;
				this.animation.setFrames(this.sprites.get(FALLING));
				this.animation.setDelay(100);
				this.width = 30;
			}
		}
		else if(this.dy < 0)
		{
			if(this.currentAction != JUMPING)
			{
				this.currentAction = JUMPING;
				this.animation.setFrames(this.sprites.get(JUMPING));
				this.animation.setDelay(-1);
				this.width = 30;
			}
		}
		else if(this.left || this.right)
		{
			if(this.currentAction != WALKING)
			{
				this.currentAction = WALKING;
				this.animation.setFrames(this.sprites.get(WALKING));
				this.animation.setDelay(100);
				this.width = 30;
			}
		}
		else
		{
			if(this.currentAction != IDLE)
			{
				this.currentAction = IDLE;
				this.animation.setFrames(this.sprites.get(IDLE));
				this.animation.setDelay(1000);
				this.width = 30;
			}
		}
		
		this.animation.update();
		
		// set direction
		if(this.currentAction != RAZORBLADE)
		{
			if(this.right)
			{
				this.facingRight = true;
			}
			if(this.left)
			{
				this.facingRight = false;
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		this.setMapPosition();
		
		// draw blades
		for(int i = 0; i < this.blades.size(); i++)
		{
			this.blades.get(i).draw(g);
		}
		
		// draw player
		if(this.flinching)
		{
			long elapsed = (System.nanoTime() - this.flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0)
			{
				return;
			}
		}
		
		super.draw(g);
	}
}
