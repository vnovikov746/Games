package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject
{
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	
	protected boolean flinching;
	protected long flinchingTimer;
	
	public Enemy(TileMap tm)
	{
		super(tm);
	}
	
	public boolean isDead()
	{
		return this.dead;
	}
	
	public int getDamage()
	{
		return this.damage;
	}
	
	public void hit(int damage)
	{
		if(this.dead || this.flinching)
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
		this.flinchingTimer = System.nanoTime();
	}
	
	public int getMaxHealth()
	{
		return this.maxHealth;
	}
	
	public void update()
	{	
		
	}
}
