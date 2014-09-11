package GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Audio.AudioPlayer;
import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Entity.Enemies.Conformist;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1State extends GameState
{
	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	
	private AudioPlayer bgMusic;
	
	public Level1State(GameStateManager gsm)
	{
		this.gsm = gsm;
		this.init();
	}
	
	@Override
	public void init()
	{
		this.tileMap = new TileMap(30);
		this.tileMap.loadTiles("/Tilesets/tileset1.gif");
		this.tileMap.loadMap("/Maps/level1.map");
		this.tileMap.setPosition(0, 0);
		this.tileMap.setTween(1);
		
		this.bg = new Background("/Backgrounds/Lava.gif", 0.1);
		
		this.player = new Player(this.tileMap);
		this.player.setPosition(100, 100);
		this.player.setNumOfAvril(5);
		this.player.setNumOfBlades(5);
		
		this.populateEnemies();
		
		this.explosions = new ArrayList<Explosion>();
		
		this.hud = new HUD(this.player);
		
		this.bgMusic = new AudioPlayer("/Music/cows.mp3");
		this.bgMusic.loop();
	}
	
	private void populateEnemies()
	{
		this.enemies = new ArrayList<Enemy>();
		
		Conformist c;
		Point[] points = new Point[] {new Point(200, 260), new Point(260, 260),
				new Point(320, 260), new Point(380, 260), new Point(500, 260),
				new Point(560, 260), new Point(620, 260), new Point(680, 260)};
		for(int i = 0; i < points.length; i++)
		{
			c = new Conformist(this.tileMap);
			c.setPosition(points[i].x, points[i].y);
			this.enemies.add(c);
		}
	}
	
	@Override
	public void update()
	{
		// update player
		this.player.update(this.enemies);
		this.tileMap.setPosition(GamePanel.WIDTH / 2 - this.player.getx(),
				GamePanel.HEIGHT / 2 - this.player.gety());
		
		// check players death
		if(this.player.getDead())
		{
			try
			{
				Thread.sleep(500);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			this.gsm.setState(GameStateManager.MENUSTATE); // TODO CHANGE TO
															// DEAD STATE
		}
		
		// set background
		this.bg.setPosition(this.tileMap.getX(), this.tileMap.getY());
		
		// attack enemies
		this.player.checkAttack(this.enemies);
		
		// update all enemies
		for(int i = 0; i < this.enemies.size(); i++)
		{
			Enemy e = this.enemies.get(i);
			e.update();
			if(e.isDead())
			{
				this.enemies.remove(i);
				i--;
				this.explosions.add(new Explosion(e.getx(), e.gety()));
			}
		}
		
		// update explosions
		for(int i = 0; i < this.explosions.size(); i++)
		{
			this.explosions.get(i).update();
			if(this.explosions.get(i).shouldRemove())
			{
				this.explosions.remove(i);
				i--;
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		// draw bg
		this.bg.draw(g);
		
		// draw tilemap
		this.tileMap.draw(g);
		
		// draw player
		this.player.draw(g);
		
		// draw enemies
		for(int i = 0; i < this.enemies.size(); i++)
		{
			this.enemies.get(i).draw(g);
		}
		
		// draw explosions
		for(int i = 0; i < this.explosions.size(); i++)
		{
			this.explosions.get(i).setMapPosition((int)this.tileMap.getX(),
					(int)this.tileMap.getY());
			this.explosions.get(i).draw(g);
		}
		
		// draw hud
		this.hud.draw(g);
	}
	
	@Override
	public void keyPressed(int k)
	{
		if(k == KeyEvent.VK_LEFT)
		{
			this.player.setLeft(true);
		}
		if(k == KeyEvent.VK_RIGHT)
		{
			this.player.setRight(true);
		}
		if(k == KeyEvent.VK_UP)
		{
			this.player.setUp(true);
		}
		if(k == KeyEvent.VK_DOWN)
		{
			this.player.setDown(true);
		}
		if(k == KeyEvent.VK_W)
		{
			this.player.setJumping(true);
		}
		if(k == KeyEvent.VK_F)
		{
			this.player.setFiring();
		}
		if(k == KeyEvent.VK_D)
		{
			if(!this.player.getAvrilInUse())
			{
				this.player.setAvrilActivated();
			}
		}
	}
	
	@Override
	public void keyReleased(int k)
	{
		if(k == KeyEvent.VK_LEFT)
		{
			this.player.setLeft(false);
		}
		if(k == KeyEvent.VK_RIGHT)
		{
			this.player.setRight(false);
		}
		if(k == KeyEvent.VK_UP)
		{
			this.player.setUp(false);
		}
		if(k == KeyEvent.VK_DOWN)
		{
			this.player.setDown(false);
		}
		if(k == KeyEvent.VK_W)
		{
			this.player.setJumping(false);
		}
	}
}
