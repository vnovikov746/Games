package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import TileMap.Background;

public class DeadState extends GameState
{
	private Background bg;
	
	private String option = "PRESS ENTER TO RETURN TO MENU";
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	
	public DeadState(GameStateManager gsm)
	{
		this.gsm = gsm;
		
		try
		{
			this.bg = new Background("/Backgrounds/dead_background.gif", 0);
			this.bg.setVector(0, 0);
			
			this.titleColor = new Color(255, 0, 0);
			this.titleFont = new Font("Century Gothic", Font.BOLD, 32);
			
			this.font = new Font("Arial", Font.BOLD, 20);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void init(int health, int blades, int avril, int score)
	{	
		
	}
	
	@Override
	public void update()
	{
		this.bg.update();
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		// draw bg
		this.bg.draw(g);
		
		// draw title
		g.setColor(this.titleColor);
		g.setFont(this.titleFont);
		g.drawString("YOU LOOSE BUT HEI, AT LEAST YOU DIED..", 10, 100);
		
		// draw menu options
		g.setFont(this.font);
		
		g.setColor(this.titleColor);
		
		g.drawString(this.option, 180, 170);
	}
	
	private void select()
	{
		this.gsm.setState(GameStateManager.MENUSTATE, 0, 0, 0, 0);
	}
	
	@Override
	public void keyPressed(int k)
	{
		if(k == KeyEvent.VK_ENTER)
		{
			this.select();
		}
	}
	
	@Override
	public void keyReleased(int k)
	{	
		
	}
}
