package objects;
import java.awt.Color;
import java.awt.Graphics;

public abstract class Shape
{
	protected String type;
	private Color color;
	private Point start;
	private int height;
	private int width;
	
	public Shape()
	{
		this.color = Colors.getRandomColor();
	}
	
	public String getType()
	{
		return type;
	}
	
	public Color getColor()
	{
		return color;
	}
	public void setColor(Color color)
	{
		this.color = color;
	}
	public void changeRandomColor()
	{
		this.color = Colors.getRandomColor();
	}
	
	public Point getStart()
	{
		return start;
	}
	public void setStart(Point start)
	{
		this.start = start;
	}
	
	public int getHeight()
	{
		return height;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getWidth()
	{
		return width;
	}
	public void setWidth(int width)
	{
		this.width = width;
	}

	public abstract void drawShape(Graphics g);
}
