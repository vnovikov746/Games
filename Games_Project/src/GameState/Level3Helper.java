package GameState;

public class Level3Helper implements Runnable
{
	private boolean moveMap;
	
	public Level3Helper()
	{
		this.moveMap = false;
	}
	
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				Thread.sleep(1000);
				Level3Helper.this.moveMap = !Level3Helper.this.moveMap;
			}
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean getMoveMap()
	{
		return this.moveMap;
	}
	
	public void setMoveMap(boolean moveMap)
	{
		this.moveMap = moveMap;
	}
}
