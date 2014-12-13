package GameState;

import java.awt.Graphics2D;

import TileMap.Background;

public class CthulhuDialog extends GameState
{
	private Background bg;
	
	private int dialogPart;
	
	public CthulhuDialog(GameStateManager gsm)
	{
		this.gsm = gsm;
	}
	
	@Override
	public void init(int health, int blades, int avril, int score)
	{
		this.dialogPart = 1;
		
		this.bg = new Background("/CthulhuDialog/dialog" + this.dialogPart
				+ ".gif", 0);
		this.bg.setVector(0, 0);
	}
	
	@Override
	public void update()
	{
		try
		{
			Thread.sleep(4000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		this.dialogPart++;
		
		if(this.dialogPart > 10)
		{
			this.gsm.setState(GameStateManager.WINSTATE, 0, 0, 0, 0);
		}
		
		this.bg = new Background("/CthulhuDialog/dialog" + this.dialogPart
				+ ".gif", 0);
		this.bg.setVector(0, 0);
		this.bg.update();
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		this.bg.draw(g);
	}
	
	@Override
	public void keyPressed(int k)
	{}
	
	@Override
	public void keyReleased(int k)
	{}
}
