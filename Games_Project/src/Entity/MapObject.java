package Entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Random;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

public abstract class MapObject
{
	// tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	// position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	// dimensions
	protected int width;
	protected int height;
	
	// collision box
	protected int cwidth;
	protected int cheight;
	
	// collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	protected boolean changeableCollision;
	protected boolean collectibleCollision;
	
	// animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	// movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	// Collectible tiles items
	protected final int RAZORBLADE = 0;
	protected final int AVRILLAVIGNE = 1;
	protected final int BLOODDROP = 2;
	protected boolean bladeCollected;
	protected boolean avrilCollected;
	protected boolean bloodDropCollected;
	protected HashMap<Point, Integer> collectibleItems;
	
	// constructor
	public MapObject(TileMap tm)
	{
		this.tileMap = tm;
		this.tileSize = tm.getTileSize();
		this.collectibleItems = new HashMap<Point, Integer>();
	}
	
	public boolean intersects(MapObject o)
	{
		Rectangle r1 = this.getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	public Rectangle getRectangle()
	{
		return new Rectangle((int)this.x - this.cwidth, (int)this.y
				- this.cheight, this.cwidth, this.cheight);
	}
	
	public void calculateCorners(double x, double y)
	{
		int leftTile = (int)(x - this.cwidth / 2) / this.tileSize;
		int rightTile = (int)(x + this.cwidth / 2 - 1) / this.tileSize;
		int topTile = (int)(y - this.cheight / 2) / this.tileSize;
		int bottomTile = (int)(y + this.cheight / 2 - 1) / this.tileSize;
		
		int tl = this.tileMap.getType(topTile, leftTile);
		int tr = this.tileMap.getType(topTile, rightTile);
		int bl = this.tileMap.getType(bottomTile, leftTile);
		int br = this.tileMap.getType(bottomTile, rightTile);
		
		this.topLeft = (tl == Tile.BLOCKED || tl == Tile.CHANGEABLE || tl == Tile.COLLECTIBLE);
		this.topRight = (tr == Tile.BLOCKED || tr == Tile.CHANGEABLE || tr == Tile.COLLECTIBLE);
		this.bottomLeft = (bl == Tile.BLOCKED || bl == Tile.CHANGEABLE || bl == Tile.COLLECTIBLE);
		this.bottomRight = (br == Tile.BLOCKED || br == Tile.CHANGEABLE || br == Tile.COLLECTIBLE);
		
		this.changeableCollision = (tl == Tile.CHANGEABLE && this.topLeft)
				&& (tr == Tile.CHANGEABLE && this.topRight);
		this.collectibleCollision = ((bl == Tile.COLLECTIBLE && this.bottomLeft) || (br == Tile.COLLECTIBLE && this.bottomRight))
				|| ((tl == Tile.COLLECTIBLE && this.topLeft) && (bl == Tile.COLLECTIBLE && this.bottomLeft))
				|| ((tr == Tile.COLLECTIBLE && this.topRight) && (br == Tile.COLLECTIBLE && this.bottomRight));
	}
	
	public void checkTileMapCollision()
	{
		this.currCol = (int)this.x / this.tileSize;
		this.currRow = (int)this.y / this.tileSize;
		
		this.xdest = this.x + this.dx;
		this.ydest = this.y + this.dy;
		
		this.xtemp = this.x;
		this.ytemp = this.y;
		
		this.calculateCorners(this.x, this.ydest); // collision up or down
		if(this.dy < 0)
		{
			if(this.changeableCollision)
			{
				this.tileMap.setTile(this.currRow - 1, this.currCol, 49);
				Random ran = new Random();
				int randomNum = Math.abs(ran.nextInt()) % 3;// TODO MAYBE CHANGE
															// PROPABILITY
				if(randomNum == 0)
				{
					this.tileMap.setTile(this.currRow - 2, this.currCol, 21);
					this.collectibleItems.put(new Point(this.currRow - 2,
							this.currCol), this.AVRILLAVIGNE);
				}
				else if(randomNum == 1)
				{
					this.tileMap.setTile(this.currRow - 2, this.currCol, 22);
					this.collectibleItems.put(new Point(this.currRow - 2,
							this.currCol), this.RAZORBLADE);
				}
				else
				{
					this.tileMap.setTile(this.currRow - 2, this.currCol, 23);
					this.collectibleItems.put(new Point(this.currRow - 2,
							this.currCol), this.BLOODDROP);
				}
			}
			else if(this.topLeft || this.topRight)
			{
				this.dy = 0;
				this.ytemp = this.currRow * this.tileSize + this.cheight / 2;
			}
			else
			{
				this.ytemp += this.dy;
			}
		}
		if(this.dy > 0)
		{
			if(this.collectibleCollision)
			{
				if(this.collectibleItems.containsKey(new Point(
						this.currRow + 1, this.currCol)))
				{
					this.tileMap.setTile(this.currRow + 1, this.currCol, 0);
					if(this.collectibleItems.get(
							new Point(this.currRow + 1, this.currCol)).equals(
							this.AVRILLAVIGNE))
					{
						this.avrilCollected = true;
					}
					else if(this.collectibleItems.get(
							new Point(this.currRow + 1, this.currCol)).equals(
							this.RAZORBLADE))
					{
						this.bladeCollected = true;
					}
					else if(this.collectibleItems.get(
							new Point(this.currRow + 1, this.currCol)).equals(
							this.BLOODDROP))
					{
						this.bloodDropCollected = true;
					}
					this.collectibleItems.remove(new Point(this.currRow + 1,
							this.currCol));
				}
			}
			else if(this.bottomLeft || this.bottomRight)
			{
				this.dy = 0;
				this.falling = false;
				this.ytemp = (this.currRow + 1) * this.tileSize - this.cheight
						/ 2;
			}
			else
			{
				this.ytemp += this.dy;
			}
		}
		
		this.calculateCorners(this.xdest, this.y);
		if(this.dx < 0)
		{
			if(this.collectibleCollision)
			{
				if(this.collectibleItems.containsKey(new Point(this.currRow,
						this.currCol - 1)))
				{
					this.tileMap.setTile(this.currRow, this.currCol - 1, 0);
					if(this.collectibleItems.get(
							new Point(this.currRow, this.currCol - 1)).equals(
							this.AVRILLAVIGNE))
					{
						this.avrilCollected = true;
					}
					else if(this.collectibleItems.get(
							new Point(this.currRow, this.currCol - 1)).equals(
							this.RAZORBLADE))
					{
						this.bladeCollected = true;
					}
					else if(this.collectibleItems.get(
							new Point(this.currRow, this.currCol - 1)).equals(
							this.BLOODDROP))
					{
						this.bloodDropCollected = true;
					}
					this.collectibleItems.remove(new Point(this.currRow,
							this.currCol - 1));
				}
			}
			else if(this.topLeft || this.bottomLeft)
			{
				this.dx = 0;
				this.xtemp = this.currCol * this.tileSize + this.cwidth / 2;
			}
			else
			{
				this.xtemp += this.dx;
			}
		}
		if(this.dx > 0)
		{
			if(this.collectibleCollision)
			{
				if(this.collectibleItems.containsKey(new Point(this.currRow,
						this.currCol + 1)))
				{
					this.tileMap.setTile(this.currRow, this.currCol + 1, 0);
					if(this.collectibleItems.get(
							new Point(this.currRow, this.currCol + 1)).equals(
							this.AVRILLAVIGNE))
					{
						this.avrilCollected = true;
					}
					else if(this.collectibleItems.get(
							new Point(this.currRow, this.currCol + 1)).equals(
							this.RAZORBLADE))
					{
						this.bladeCollected = true;
					}
					else if(this.collectibleItems.get(
							new Point(this.currRow, this.currCol + 1)).equals(
							this.BLOODDROP))
					{
						this.bloodDropCollected = true;
					}
					this.collectibleItems.remove(new Point(this.currRow,
							this.currCol + 1));
				}
			}
			else if(this.topRight || this.bottomRight)
			{
				this.dx = 0;
				this.xtemp = (this.currCol + 1) * this.tileSize - this.cwidth
						/ 2;
			}
			else
			{
				this.xtemp += this.dx;
			}
		}
		
		if(!this.falling)
		{
			this.calculateCorners(this.x, this.ydest + 1);
			if(!this.bottomLeft && !this.bottomRight)
			{
				this.falling = true;
			}
		}
	}
	
	public void setMoveSpeed(double speed)
	{
		this.moveSpeed = speed;
	}
	
	public void setMaxSpeed(double speed)
	{
		this.maxSpeed = speed;
	}
	
	public double getMoveSpeed()
	{
		return this.moveSpeed;
	}
	
	public double getMaxSpeed()
	{
		return this.maxSpeed;
	}
	
	public boolean getBladeCollected()
	{
		return this.bladeCollected;
	}
	
	public boolean getAvrilCollected()
	{
		return this.avrilCollected;
	}
	
	public boolean getBloodDropCollected()
	{
		return this.bloodDropCollected;
	}
	
	public void setBladeCollected(boolean b)
	{
		this.bladeCollected = b;
	}
	
	public void setAvrilCollected(boolean b)
	{
		this.avrilCollected = b;
	}
	
	public void setBloodDropCollected(boolean b)
	{
		this.bloodDropCollected = b;
	}
	
	public int getx()
	{
		return (int)this.x;
	}
	
	public int gety()
	{
		return (int)this.y;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public int getCWidth()
	{
		return this.cwidth;
	}
	
	public int getCHeight()
	{
		return this.cheight;
	}
	
	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setVector(double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setMapPosition()
	{
		this.xmap = this.tileMap.getX();
		this.ymap = this.tileMap.getY();
	}
	
	public void setLeft(boolean b)
	{
		this.left = b;
	}
	
	public void setRight(boolean b)
	{
		this.right = b;
	}
	
	public void setUp(boolean b)
	{
		this.up = b;
	}
	
	public void setDown(boolean b)
	{
		this.down = b;
	}
	
	public void setJumping(boolean b)
	{
		this.jumping = b;
	}
	
	public boolean notOnScreen()
	{
		return this.x + this.xmap + this.width < 0
				|| this.x + this.xmap - this.width > GamePanel.WIDTH
				|| this.y + this.ymap + this.height < 0
				|| this.y + this.ymap - this.height > GamePanel.HEIGHT;
	}
	
	public void draw(Graphics2D g)
	{
		if(this.facingRight)
		{
			g.drawImage(this.animation.getImage(),
					(int)(this.x + this.xmap - this.width / 2), (int)(this.y
							+ this.ymap - this.height / 2), null);
		}
		else
		{
			g.drawImage(this.animation.getImage(), (int)(this.x + this.xmap
					- this.width / 2 + this.width),
					(int)(this.y + this.ymap - this.height / 2), -this.width,
					this.height, null);
		}
	}
}
