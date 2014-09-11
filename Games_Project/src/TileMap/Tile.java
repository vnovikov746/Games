package TileMap;

import java.awt.image.BufferedImage;

public class Tile
{
	private BufferedImage image;
	private int type;
	
	// tile types
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	public static final int CHANGEABLE = 2;
	public static final int COLLECTIBLE = 3;
	
	public Tile(BufferedImage image, int type)
	{
		this.image = image;
		this.type = type;
	}
	
	public BufferedImage getImage()
	{
		return this.image;
	}
	
	public int getType()
	{
		return this.type;
	}
}
