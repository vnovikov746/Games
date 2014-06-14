package objects;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Ball extends Shape
{
	public Ball()
	{
		super();
		type = "Ball";
	}
	
	@Override
	public void drawShape(Graphics g) 
	{
		g.setColor(super.getColor());
		((Graphics2D)(g)).fillOval(super.getStart().getX(), super.getStart().getY(), super.getWidth(), super.getHeight());
	}
}
