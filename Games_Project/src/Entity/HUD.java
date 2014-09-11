package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class HUD
{
	private Player player;
	
	private BufferedImage image;
	private Font font;
	
	public HUD(Player p)
	{
		this.player = p;
		try
		{
			this.image = ImageIO.read(this.getClass().getResourceAsStream(
					"/HUD/hud_4.gif"));
			
			this.font = new Font("Arial", Font.PLAIN, 14);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g)
	{
		g.drawImage(this.image, 0, 10, null);
		g.setFont(this.font);
		g.setColor(Color.WHITE);
		g.drawString(
				this.player.getHealth() + "/" + this.player.getMaxHealth(), 30,
				25);
		g.drawString(
				this.player.getNumOfBlades() + "/" + this.player.getMaxBlades(),
				30, 45);
		g.drawString(
				this.player.getNumOfAvril() + "/" + this.player.getMaxAvril(),
				30, 66);
		g.drawString("" + this.player.getScore(), 30, 87);
	}
}
