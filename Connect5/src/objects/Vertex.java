package objects;

public class Vertex
{
	private int dist;
	private Point point;
	private Point previous;
	private boolean isTaken;
		
	public Vertex(int thisX, int thisY, int dist, int preX, int preY, boolean isTaken)
	{
		this.dist = dist;
		this.point = new Point(thisX, thisY);
		this.previous = new Point(preX,preY);
		this.isTaken = isTaken;
	}
	public int getDist()
	{
		return dist;
	}
	public void setDist(int dist)
	{
		this.dist = dist;
	}

	public Point getPrevious()
	{
		return previous;
	}
	public void setPrevious(int x, int y)
	{
		previous = new Point(x,y);
	}

	public Point getPoint()
	{
		return point;
	}
	
	public boolean isTaken()
	{
		return isTaken;
	}
}
