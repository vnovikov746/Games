import java.awt.Color;
import java.awt.Graphics;

public class Tile
{
	private boolean dead;
	
	public Tile(boolean state)
	{
		super();
		this.dead = state;
	}
	
	public void draw(Graphics g, int x, int y, int width, int height)
	{
		if(!dead)
		{
			g.setColor(Color.WHITE);
			g.drawRect(x, y, width, height);
			g.setColor(Color.BLACK);
			g.fillRect(x, y, width - 1, height - 1);
		}
		else
		{
			g.setColor(Color.BLACK);
			g.drawRect(x, y, width, height);
			g.setColor(Color.WHITE);
			g.fillRect(x, y, width - 1, height - 1);
		}
	}
	
	public void setStatus(boolean status)
	{
		this.dead = status;
	}
	
	public boolean getStatus()
	{
		return dead;
	}
}
