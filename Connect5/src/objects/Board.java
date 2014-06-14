package objects;
import java.util.Random;

public class Board
{
	private Tile[][] tiles;
	private int numOfTilesInRow;
	private int numOfShapes;
	private int numOfTiles;
	
	public Board(int numOfTiles, int numOfShapes, String shapeType)
	{
		this.numOfTilesInRow = numOfTiles;
		this.numOfShapes = numOfShapes;
		this.numOfTiles = numOfTiles;
		
		tiles = new Tile[numOfTilesInRow][numOfTilesInRow];
		
		for(int i = 0; i < numOfTiles; i++)
		{
			for(int j = 0; j < numOfTiles; j++)
			{
				tiles[i][j] = new Tile(i,j);
			}
		}
		
		addShapes(numOfShapes,shapeType);
	}
	
	public Tile[][] getTiles()
	{
		return tiles;
	}

	public int getNumOfTilesInRow()
	{
		return numOfTilesInRow;
	}
	public void setNumOfTilesInRow(int numOfTilesInRow)
	{
		this.numOfTilesInRow = numOfTilesInRow;
	}

	public int getNumOfShapes()
	{
		return numOfShapes;
	}
	public void setNumOfShapes(int numOfShapes)
	{
		this.numOfShapes = numOfShapes;
	}
	
	public void changeShape(String shapeType)
	{
		for (Tile[] tileCol : tiles) 
		{
			for (Tile tile : tileCol)
			{
				tile.setShape(shapeType);
			}
		}
	}
	
	public void addShape(int i, int j, String shapeType)
	{
		tiles[i][j].addShape(shapeType);
		numOfShapes++;
	}
	
	public void addShapes(int shapesNum, String shapeType)
	{
		Random ran = new Random();
		int randomI;
		int randomJ;
		int times;
		
		for(int i = 0; i < shapesNum && numOfShapes < numOfTiles; i++)
		{
			times = 0;
			randomI = ran.nextInt(numOfTiles);
			randomJ = ran.nextInt(numOfTiles);
			while(tiles[randomI][randomJ].getWithShape() && times < numOfTiles*numOfTiles)
			{
				randomI = ran.nextInt(numOfTiles);
				randomJ = ran.nextInt(numOfTiles);
				times++;
			}
			if(times == numOfTiles*numOfTiles)
			{
				for(int j = 0; j < numOfTiles; j++)
				{
					for(int k = 0; k < numOfTiles; k++)
					{
						if(!tiles[i][j].getWithShape())
						{
							randomI = j;
							randomJ = k;
						}
					}
				}
			}
			tiles[randomI][randomJ].addShape(shapeType);
		}
	}
}
