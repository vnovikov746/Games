import java.awt.Graphics;
import java.util.Random;

public class Board
{
	protected static final int RESOLUTION = 5;
	
	private Tile[][] tiles;
	private Tile[][] tmpTiles;
	
	private final boolean DEAD = true;
	
	public Board()
	{
		this.tiles = new Tile[GamePanel.WIDTH / RESOLUTION][GamePanel.HEIGHT
				/ RESOLUTION];
		this.tmpTiles = new Tile[GamePanel.WIDTH / RESOLUTION][GamePanel.HEIGHT
				/ RESOLUTION];
		clearBoard();
	}
	
	public void draw(Graphics g)
	{
		for(int i = 0; i < tiles.length; i++)
		{
			for(int j = 0; j < tiles.length; j++)
			{
				tiles[i][j].draw(g, RESOLUTION * i, RESOLUTION * j, RESOLUTION,
						RESOLUTION);
			}
		}
	}
	
	public void update()
	{
		int liveNeighbors = 0;
		int m2 = 0, n2 = 0;
		copyTiles(tiles, tmpTiles);
		
		for(int i = 0; i < tiles.length; i++)
		{
			for(int j = 0; j < tiles.length; j++)
			{
				for(int m = i - 1; m < i + 2; m++)
				{
					for(int n = j - 1; n < j + 2; n++)
					{
						if(m == -1 || m == tiles.length || n == -1
								|| n == tiles.length)
						{
							if(m == -1)
							{
								m2 = tiles.length - 1;
							}
							else if(m == tiles.length)
							{
								m2 = 0;
							}
							if(n == -1)
							{
								n2 = tiles.length - 1;
							}
							else if(n == tiles.length)
							{
								n2 = 0;
							}
							if(tiles[m2][n2].getStatus() != DEAD
									&& !(m == i && n == j))
							{
								liveNeighbors++;
							}
						}
						
						else
						{
							if(tiles[m][n].getStatus() != DEAD
									&& !(m == i && n == j))
							{
								liveNeighbors++;
							}
						}
					}
				}
				if(liveNeighbors < 2 || liveNeighbors > 3)
				{
					tmpTiles[i][j].setStatus(DEAD);
				}
				
				else if(tiles[i][j].getStatus() == DEAD && liveNeighbors == 3)
				{
					tmpTiles[i][j].setStatus(!DEAD);
				}
				liveNeighbors = 0;
			}
		}
		
		copyTiles(tmpTiles, tiles);
	}
	
	public void copyTiles(Tile[][] from, Tile[][] to)
	{
		for(int i = 0; i < to.length; i++)
		{
			for(int j = 0; j < from.length; j++)
			{
				to[i][j].setStatus(from[i][j].getStatus());
			}
		}
	}
	
	public Tile[][] getTiles()
	{
		return tiles;
	}
	
	public void randomBoard(int seed)
	{
		Random ran = new Random();
		for(int i = 0; i < tiles.length; i++)
		{
			for(int j = 0; j < tiles.length; j++)
			{
				if(ran.nextInt() % seed == 0)
				{
					tiles[i][j].setStatus(!DEAD);
					tmpTiles[i][j].setStatus(!DEAD);
				}
			}
		}
	}
	
	public void clearBoard()
	{
		for(int i = 0; i < tiles.length; i++)
		{
			for(int j = 0; j < tiles.length; j++)
			{
				tiles[i][j] = new Tile(this.DEAD);
				tmpTiles[i][j] = new Tile(this.DEAD);
			}
		}
	}
}
