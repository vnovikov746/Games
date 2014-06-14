package logic;

import gui.GamePanel;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import sprites.EnemySprite;
import board_objects.Board;
import board_objects.Tile;

public class Enemies
{
	private EnemySprite[] enemies;
	private final int numOfEnemies = 3;
	
	public Enemies(BufferedImage enemyImage, Board board)
	{
		Random ran = new Random();
		int tileSize = Tile.getTileSize();
		int numOfTiles = GamePanel.getNumOfTiles();
		int onBoardX = 0;
		int onBoardY = 0;
		
		this.enemies = new EnemySprite[this.numOfEnemies];
		
		for(int i = 0; i < this.numOfEnemies; i++)
		{
			onBoardX = (ran.nextInt(numOfTiles - 7) + 3);
			onBoardY = (ran.nextInt(numOfTiles - 7) + 3);
			this.enemies[i] = new EnemySprite(onBoardX * tileSize, onBoardY
					* tileSize, tileSize, tileSize, tileSize, tileSize,
					onBoardX, onBoardY, enemyImage, board);
			
			board.getTiles()[onBoardY][onBoardX].setStatus(Tile.ENEMY);
		}
	}
	
	public EnemySprite[] getEnemies()
	{
		return this.enemies;
	}
	
	public boolean updateEnemies()
	{
		for(EnemySprite enemy : this.enemies)
		{
			if(!enemy.updateSprite())
			{
				return false;
			}
		}
		return true;
	}
	
	public void drawEnemies(Graphics g)
	{
		for(EnemySprite enemy : this.enemies)
		{
			enemy.drawSprite(g);
		}
	}
}
