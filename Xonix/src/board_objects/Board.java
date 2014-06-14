package board_objects;

import gui.GamePanel;

import java.awt.Color;

import sprites.PlayerSprite;

public class Board
{
	private Tile[][] tiles;
	private int numOfTiles = GamePanel.getNumOfTiles();
	
	public Board()
	{
		this.tiles = new Tile[this.numOfTiles][this.numOfTiles];
		int locX;
		int locY = 0;
		int size = PlayerSprite.getSize();
		
		for(int i = 0; i < this.numOfTiles; i++)
		{
			locX = 0;
			for(int j = 0; j < this.numOfTiles; j++)
			{
				if(locX == 0 || locX == size || locY == 0 || locY == size
						|| locX == size * (this.numOfTiles - 2)
						|| locX == size * (this.numOfTiles - 1)
						|| locY == size * (this.numOfTiles - 2)
						|| locY == size * (this.numOfTiles - 1))
				{
					this.tiles[i][j] = new Tile(locX, locY, Color.BLUE);
					this.tiles[i][j].setStatus(Tile.BLUE);
				}
				else
				{
					this.tiles[i][j] = new Tile(locX, locY, Color.BLACK);
					this.tiles[i][j].setStatus(Tile.BLACK);
				}
				locX += size;
			}
			locY += size;
		}
	}
	
	public Tile[][] getTiles()
	{
		return this.tiles;
	}
}
