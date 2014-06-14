/**
 * 
 * Author: Vladimir Novikov, ID: 312669112.
 * 
 */

package main;

import gui.GamePanel;

import java.io.IOException;

import javax.swing.JFrame;

public class Xonix
{
	private final static int width = 576;
	private final static int height = 638;
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Xonix");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		try
		{
			GamePanel p = new GamePanel();
			frame.add(p);
			frame.setVisible(true);
		}
		catch(IOException e)
		{
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
	}
}
