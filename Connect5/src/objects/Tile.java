package objects;
import java.awt.Graphics;

import javax.swing.JButton;


public class Tile extends JButton
{
	private static final long serialVersionUID = 1L;

	public static boolean isClicked = false; 

	private Shape shape;
	private boolean withShape;
	private String shapeType;
	private Point place;
	
	public Tile(int x, int y)
	{
		super();
		this.withShape = false;
		place = new Point(x, y);
	}
	
	public Point getPlace()
	{
		return place;
	}

	public Shape getShape()
	{
		return shape;
	}
	public void setShape(String shapeType)
	{
		addShape(shapeType);
	}
	
	public String getShapeType()
	{
		return shapeType;
	}
	
	
	public boolean getWithShape()
	{
		return withShape;
	}
	public void setWithShape(boolean withShape)
	{
		this.withShape = withShape;
	}
		
	public void addShape(String shapeType)
	{
		switch(shapeType)
		{
			case "Ball":
			{
				this.shapeType = "Ball";
				shape = new Ball();
				break;
			}
			///////other cases
		}
		this.withShape = true;
	}

	public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        if(withShape)
        {
        	shape.setStart(new Point((int)(getHorizontalAlignment()+(int)(getWidth()*0.05)), (int)(getVerticalAlignment()+(int)(getHeight()*0.05))));
        	shape.setWidth((int)(getWidth()*0.9));
        	shape.setHeight((int)(getHeight()*0.9));
        	shape.drawShape(g);
        }
    }
}
