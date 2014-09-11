package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class TileMap
{
	// position
	private double x;
	private double y;
	
	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	private double tween;
	
	// map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	// tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	public TileMap(int tileSize)
	{
		this.tileSize = tileSize;
		this.numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		this.numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		this.tween = 0.07;
	}
	
	public void loadTiles(String s)
	{
		try
		{
			this.tileset = ImageIO.read(this.getClass().getResourceAsStream(s));
			this.numTilesAcross = this.tileset.getWidth() / this.tileSize;
			this.tiles = new Tile[2][this.numTilesAcross];
			
			BufferedImage subimage;
			for(int col = 0; col < this.numTilesAcross; col++)
			{
				subimage = this.tileset.getSubimage(col * this.tileSize, 0,
						this.tileSize, this.tileSize);
				if(col == 21 || col == 22 || col == 23)
				{
					this.tiles[0][col] = new Tile(subimage, Tile.COLLECTIBLE);
				}
				else
				{
					this.tiles[0][col] = new Tile(subimage, Tile.NORMAL);
				}
				subimage = this.tileset.getSubimage(col * this.tileSize,
						this.tileSize, this.tileSize, this.tileSize);
				if(col == 18)
				{
					this.tiles[1][col] = new Tile(subimage, Tile.CHANGEABLE);
				}
				else
				{
					this.tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadMap(String s)
	{
		try
		{
			InputStream in = this.getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			this.numCols = Integer.parseInt(br.readLine());
			this.numRows = Integer.parseInt(br.readLine());
			this.map = new int[this.numRows][this.numCols];
			this.width = this.numCols * this.tileSize;
			this.height = this.numRows * this.tileSize;
			
			this.xmin = GamePanel.WIDTH - this.width;
			this.xmax = 0;
			this.ymin = GamePanel.HEIGHT - this.height;
			this.ymax = 0;
			
			String delims = "\\s+";
			for(int row = 0; row < this.numRows; row++)
			{
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < this.numCols; col++)
				{
					this.map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setTile(int row, int col, int newTile)// change changeable tile
														// to blocked tile
	{
		this.map[row][col] = newTile;
	}
	
	public int getTileSize()
	{
		return this.tileSize;
	}
	
	public double getX()
	{
		return this.x;
	}
	
	public double getY()
	{
		return this.y;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public int getType(int row, int col)
	{
		int rc = this.map[row][col];
		int r = rc / this.numTilesAcross;
		int c = rc % this.numTilesAcross;
		return this.tiles[r][c].getType();
	}
	
	public void setTween(double d)
	{
		this.tween = d;
	}
	
	public void setPosition(double x, double y)
	{
		this.x += (x - this.x) * this.tween;
		this.y += (y - this.y) * this.tween;
		
		this.fixBounds();
		
		this.colOffset = (int)-this.x / this.tileSize;
		this.rowOffset = (int)-this.y / this.tileSize;
	}
	
	private void fixBounds()
	{
		if(this.x < this.xmin)
		{
			this.x = this.xmin;
		}
		if(this.y < this.ymin)
		{
			this.y = this.ymin;
		}
		if(this.x > this.xmax)
		{
			this.x = this.xmax;
		}
		if(this.y > this.ymax)
		{
			this.y = this.ymax;
		}
	}
	
	public void draw(Graphics2D g)
	{
		for(int row = this.rowOffset; row < this.rowOffset + this.numRowsToDraw; row++)
		{
			if(row >= this.numRows)
			{
				break;
			}
			
			for(int col = this.colOffset; col < this.colOffset
					+ this.numColsToDraw; col++)
			{
				if(col >= this.numCols)
				{
					break;
				}
				
				if(this.map[row][col] == 0)
				{
					continue;
				}
				
				int rc = this.map[row][col];
				int r = rc / this.numTilesAcross;
				int c = rc % this.numTilesAcross;
				
				g.drawImage(this.tiles[r][c].getImage(), (int)this.x + col
						* this.tileSize, (int)this.y + row * this.tileSize,
						null);
			}
		}
	}
}
