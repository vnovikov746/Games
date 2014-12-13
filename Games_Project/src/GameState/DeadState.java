package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import TileMap.Background;

public class DeadState extends GameState
{
	private Background bg;
	
	private int currentChoice = 0;
	private String[] options = {"Level 1", "Level 2", "Level 3",
			"Watch ending", "Menu"};
	
	private Color titleColor;
	private Font titleFont;
	
	private Color fontColor;
	private Font font;
	
	public DeadState(GameStateManager gsm)
	{
		this.gsm = gsm;
		
		try
		{
			this.bg = new Background("/Backgrounds/dead_background.gif", 1);
			this.bg.setVector(0, 0);
			
			this.titleColor = new Color(255, 0, 0);
			this.titleFont = new Font("Century Gothic", Font.BOLD, 32);
			
			this.fontColor = new Color(87, 10, 254);
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
		g.drawString("YOU DIED", 250, 50);
		g.drawString("but now you can CHEAT!", 150, 90);
		g.drawString("choose level to start from", 150, 130);
		
		// draw menu options
		g.setFont(this.font);
		for(int i = 0; i < this.options.length; i++)
		{
			if(i == this.currentChoice)
			{
				g.setColor(this.titleColor);
			}
			else
			{
				g.setColor(this.fontColor);
			}
			g.drawString(this.options[i], 280, 170 + i * 30);
		}
	}
	
	private void select()
	{
		if(this.currentChoice == 0)
		{
			this.gsm.setState(GameStateManager.LEVEL1STATE, 3, 5, 5, 0);
		}
		if(this.currentChoice == 1)
		{
			this.gsm.setState(GameStateManager.LEVEL2STATE, 3, 5, 5, 0);
		}
		if(this.currentChoice == 2)
		{
			this.gsm.setState(GameStateManager.LEVEL3STATE, 3, 5, 5, 0);
		}
		if(this.currentChoice == 3)
		{
			this.gsm.setState(GameStateManager.CTHULHUDIALOG, 0, 0, 0, 0);
		}
		if(this.currentChoice == 4)
		{
			this.gsm.setState(GameStateManager.MENUSTATE, 0, 0, 0, 0);
		}
	}
	
	@Override
	public void keyPressed(int k)
	{
		if(k == KeyEvent.VK_ENTER)
		{
			this.select();
		}
		if(k == KeyEvent.VK_UP)
		{
			this.currentChoice--;
			if(this.currentChoice == -1)
			{
				this.currentChoice = this.options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN)
		{
			this.currentChoice++;
			if(this.currentChoice == this.options.length)
			{
				this.currentChoice = 0;
			}
		}
	}
	
	@Override
	public void keyReleased(int k)
	{	
		
	}
}
