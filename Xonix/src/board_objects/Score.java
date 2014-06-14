package board_objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Score
{
	public static void drawScore(Graphics g, String str, int x, int y)
	{
		g.setColor(Color.BLACK);
		g.setFont(new Font(Font.SERIF, Font.BOLD, 18));
		g.drawString(str, x, y);
	}
}
