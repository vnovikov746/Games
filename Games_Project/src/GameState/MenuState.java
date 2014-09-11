package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import TileMap.Background;

public class MenuState extends GameState
{
	private Background bg;
	
	private int currentChoice = 0;
	private String[] options = {"Start", "Help", "Quit"};
	
	private Color titleColor;
	private Font titleFont;
	
	private Color fontColor;
	private Font font;
	
	public MenuState(GameStateManager gsm)
	{
		this.gsm = gsm;
		
		try
		{
			this.bg = new Background("/Backgrounds/menu_background_pink.gif", 1);
			this.bg.setVector(-0.1, 0);
			
			this.titleColor = new Color(253, 37, 178);
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
	public void init()
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
		g.drawString("Emo Kid", 280, 100);
		
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
			g.drawString(this.options[i], 320, 140 + i * 30);
		}
	}
	
	private void select()
	{
		if(this.currentChoice == 0)
		{
			this.gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(this.currentChoice == 1)
		{
			// help
		}
		if(this.currentChoice == 2)
		{
			System.exit(0);
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
