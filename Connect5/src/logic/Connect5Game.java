package logic;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Vector;

import objects.Board;
import objects.Point;
import objects.Tile;
import objects.Vertex;


public class Connect5Game 
{	
	private final int connect = 5;
	
	private Board board;
	private int steps;
	private int score;
	private int howManyInRow;
	private int numOfTiles;
	private Vector<Point> rowToErase;
	private Vector<Point> colToErase;
	private Vector<Point> rightDiagToErase;
	private Vector<Point> leftDiagToErase;
	private Vertex[][] verticies;
	
	public Connect5Game(int numOfTiles, int numOfShapes, String shapeType)
	{
		rowToErase = new Vector<Point>();
		colToErase = new Vector<Point>();
		rightDiagToErase = new Vector<Point>();
		leftDiagToErase = new Vector<Point>();		
		verticies = new Vertex[numOfTiles][numOfTiles];
		this.numOfTiles = numOfTiles;
		
		startNewGame(numOfTiles, numOfShapes, shapeType);
	}
	
	public void startNewGame(int numOfTiles, int numOfShapes, String shapeType)
	{
		board = new Board(numOfTiles, numOfShapes, shapeType);

		steps = 0;
		score = 0;
		howManyInRow = 0;		
	}
	
	public int getNumOfTiles()
	{
		return numOfTiles;
	}
	
	public int getSteps()
	{
		return steps;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public Board getBoard()
	{
		return board;
	}
	
	public boolean isGameOver()
	{
		if(board.getNumOfShapes() == numOfTiles)
		{
			return true;
		}
		return false;
	}

	public boolean isLine(int row, int col)
	{
		rowToErase.clear();
		colToErase.clear();
		rightDiagToErase.clear();
		leftDiagToErase.clear();
		rowToErase.add(new Point(row,col));
		colToErase.add(new Point(row,col));
		rightDiagToErase.add(new Point(row,col));
		leftDiagToErase.add(new Point(row,col));
		//check row
		for(int j = col+1; j < numOfTiles; j++)
		{
			if(board.getTiles()[row][j].getWithShape() && sameColor(row, col, row, j))
			{
				rowToErase.add(new Point(row,j));
			}
			else
			{
				break;
			}
		}
		
		for(int j = col-1; j >= 0; j--)
		{
			if(board.getTiles()[row][j].getWithShape() && sameColor(row, col, row, j))
			{
				rowToErase.add(new Point(row,j));
			}
			else
			{
				break;
			}
		}

		//check column
		for(int i = row+1; i < numOfTiles; i++)
		{
			if(board.getTiles()[i][col].getWithShape() && sameColor(row, col, i, col))
			{
				colToErase.add(new Point(i,col));
			}
			else
			{
				break;
			}
		}
		
		for(int i = row-1; i >= 0; i--)
		{
			if(board.getTiles()[i][col].getWithShape() && sameColor(row, col, i, col))
			{
				colToErase.add(new Point(i,col));
			}
			else
			{
				break;
			}
		}

		//check down right (left diagonal)
		for(int i = row+1, j = col+1; i < numOfTiles && j < numOfTiles; i++, j++)
		{
			if(board.getTiles()[i][j].getWithShape() && sameColor(row, col, i, j))
			{
				leftDiagToErase.add(new Point(i,j));
			}
			else
			{
				break;
			}
		}
		
		//check up left (left diagonal)
		for(int i = row-1, j = col-1; i >= 0 && j >= 0; i--, j--)
		{
			if(board.getTiles()[i][j].getWithShape() && sameColor(row, col, i, j))
			{
				leftDiagToErase.add(new Point(i,j));
			}
			else
			{
				break;
			}
		}
		
		//check down left (right diagonal)
		for(int i = row+1, j = col-1; i < numOfTiles && j >= 0; i++, j--)
		{
			if(board.getTiles()[i][j].getWithShape() && sameColor(row, col, i, j))
			{
				rightDiagToErase.add(new Point(i,j));
			}
			else
			{
				break;
			}
		}
		
		//check up right (right diagonal)
		for(int i = row-1, j = col+1; i >= 0 && j < numOfTiles; i--, j++)
		{
			if(board.getTiles()[i][j].getWithShape() && sameColor(row, col, i, j))
			{
				rightDiagToErase.add(new Point(i,j));
			}
			else
			{
				break;
			}
		}
		if(rowToErase.size() > connect-1 || colToErase.size() > connect-1 || leftDiagToErase.size() > connect-1 || rightDiagToErase.size() > connect-1)
		{
			return true;
		}
		return false;
	}
	
	public boolean sameColor(int row1, int col1, int row2, int col2)
	{
		return (board.getTiles()[row2][col2].getWithShape() && board.getTiles()[row1][col1].getShape().getColor().equals(board.getTiles()[row2][col2].getShape().getColor()));
	}
		
	public void moveStep(int sourceRow, int sourceCol, int destRow, int destCol)
	{
		if(isClearPath(sourceRow, sourceCol, destRow, destCol))
		{			
			moveTile(sourceRow, sourceCol, destRow, destCol);
			
			if(isLine(destRow, destCol))
			{
				removeLine();
				score += howManyInRow;
			}
			addShapes();
			steps++;
		}
	}
	
	public void removeLine()
	{
		howManyInRow = 0;
		
		if(rowToErase.size() > connect-1)
		{
			howManyInRow += rowToErase.size();
			for(int i = 0; i < rowToErase.size(); i++)
			{
				board.getTiles()[rowToErase.elementAt(i).getX()][rowToErase.elementAt(i).getY()].setShape("");				
				board.getTiles()[rowToErase.elementAt(i).getX()][rowToErase.elementAt(i).getY()].setWithShape(false);				
			}
		}
		if(colToErase.size() > connect-1)
		{
			howManyInRow += colToErase.size();
			for(int i = 0; i < colToErase.size(); i++)
			{
				board.getTiles()[colToErase.elementAt(i).getX()][colToErase.elementAt(i).getY()].setShape("");				
				board.getTiles()[colToErase.elementAt(i).getX()][colToErase.elementAt(i).getY()].setWithShape(false);				
			}
			
		}
		if(leftDiagToErase.size() > connect-1)
		{
			howManyInRow += leftDiagToErase.size();
			for(int i = 0; i < leftDiagToErase.size(); i++)
			{
				board.getTiles()[leftDiagToErase.elementAt(i).getX()][leftDiagToErase.elementAt(i).getY()].setShape("");				
				board.getTiles()[leftDiagToErase.elementAt(i).getX()][leftDiagToErase.elementAt(i).getY()].setWithShape(false);				
			}			
		}
		if(rightDiagToErase.size() > connect-1)
		{
			howManyInRow += rightDiagToErase.size();
			for(int i = 0; i < rightDiagToErase.size(); i++)
			{
				board.getTiles()[rightDiagToErase.elementAt(i).getX()][rightDiagToErase.elementAt(i).getY()].setShape("");				
				board.getTiles()[rightDiagToErase.elementAt(i).getX()][rightDiagToErase.elementAt(i).getY()].setWithShape(false);				
			}						
		}
	}	
	
	public void addShapes()
	{
		Random ran = new Random();
		int shapesToAdd = ran.nextInt(4)+1;

		board.addShapes(shapesToAdd, "Ball");
		
		for(Tile[] tRow : board.getTiles())
		{
			for(Tile t : tRow)
			{
				if(t.getWithShape() && isLine(t.getPlace().getX(), t.getPlace().getY()))
				{
					removeLine();
					score += howManyInRow;
				}
			}
		}
	}

	public void moveTile(int sourceRow, int sourceCol, int destRow, int destCol)
	{
		board.getTiles()[destRow][destCol].setShape(board.getTiles()[sourceRow][sourceCol].getShape().getType());
		board.getTiles()[destRow][destCol].getShape().setColor(board.getTiles()[sourceRow][sourceCol].getShape().getColor());
		board.getTiles()[sourceRow][sourceCol].setShape("");
		board.getTiles()[sourceRow][sourceCol].setWithShape(false);
	}

	public boolean isClearPath(int sourceRow, int sourceCol, int destRow, int destCol)
	{
		if(retrievePath(sourceRow, sourceCol, destRow, destCol) != null)
		{
			return true;
		}
		return false;
	}
	
	public void DijkstraMinPath(int sourceRow, int sourceCol)
	{		
		PriorityQueue<Vertex> PQ = new PriorityQueue<Vertex>(numOfTiles*numOfTiles, new Comparator<Vertex>() 
		{
			@Override
			public int compare(Vertex v, Vertex u)
			{
				return v.getDist()-u.getDist();
			}
		});
		PriorityQueue<Vertex> priorityChangerQueue = new PriorityQueue<Vertex>(numOfTiles*numOfTiles, new Comparator<Vertex>() 
				{
					@Override
					public int compare(Vertex v, Vertex u)
					{
						return v.getDist()-u.getDist();
					}
				});

		verticies = new Vertex[numOfTiles][numOfTiles];
		boolean isTaken;
		int dist = 20000;
		
		for(int i = 0; i < numOfTiles; i++)
		{
			for(int j = 0; j < numOfTiles; j++, dist++)
			{
				isTaken = false;
				if(board.getTiles()[i][j].getWithShape())
				{
					isTaken = true;
				}
				if(i == sourceRow && j == sourceCol)
				{
					verticies[i][j] = new Vertex(i,j,0,i,j,isTaken);
				}
				else
				{
					verticies[i][j] = new Vertex(i,j,dist,-1,-1,isTaken);			
				}
				PQ.add(verticies[i][j]);
			}
		}
		
		Vertex u;
		int tmpDist;
		int x;
		int y;
		while(!PQ.isEmpty())
		{
			u = PQ.remove();
			x = u.getPoint().getX();
			y = u.getPoint().getY();
			tmpDist = u.getDist()+1;
			int i;
			int j;
			
			i = x-1;
			j = y;
			if(i >= 0 && i < numOfTiles && j >= 0 && j < numOfTiles && !verticies[i][j].isTaken())
			{
				if(tmpDist <= verticies[i][j].getDist())
				{
					verticies[i][j].setDist(tmpDist);
					verticies[i][j].setPrevious(x, y);
				}
			}						
			
			i = x;
			j = y-1;
			if(i >= 0 && i < numOfTiles && j >= 0 && j < numOfTiles && !verticies[i][j].isTaken())
			{
				if(tmpDist <= verticies[i][j].getDist())
				{
					verticies[i][j].setDist(tmpDist);
					verticies[i][j].setPrevious(x, y);
				}
			}						

			i = x;
			j = y+1;
			if(i >= 0 && i < numOfTiles && j >= 0 && j < numOfTiles && !verticies[i][j].isTaken())
			{
				if(tmpDist <= verticies[i][j].getDist())
				{
					verticies[i][j].setDist(tmpDist);
					verticies[i][j].setPrevious(x, y);
				}
			}

			i = x+1;
			j = y;
			if(i >= 0 && i < numOfTiles && j >= 0 && j < numOfTiles && !verticies[i][j].isTaken())
			{
				if(tmpDist <= verticies[i][j].getDist())
				{
					verticies[i][j].setDist(tmpDist);
					verticies[i][j].setPrevious(x, y);
				}
			}
			while(!PQ.isEmpty())
			{
				priorityChangerQueue.add(PQ.remove());
			}
			while(!priorityChangerQueue.isEmpty())
			{
				PQ.add(priorityChangerQueue.remove());
			}
		}
	}

	//Retrieve path
	public Vector<Point> retrievePath(int sourceRow, int sourceCol, int destRow, int destCol)
	{
		Vector<Point> path = null;
		
		if((sourceRow != destRow || sourceCol != destCol) && !board.getTiles()[destRow][destCol].getWithShape())
		{
			path = new Vector<Point>();
			int i = destRow;
			int j = destCol; 
			int tempI;
			while((i != sourceRow || j != sourceCol) && (verticies[i][j].getPrevious().getX() != -1))
			{
				path.add(verticies[i][j].getPoint());
				tempI = i;
				i = verticies[i][j].getPrevious().getX();
				j = verticies[tempI][j].getPrevious().getY();
			}
			if(i != sourceRow || j != sourceCol)
			{
				path = null;
			}
		}
		return path;
	}	
}
