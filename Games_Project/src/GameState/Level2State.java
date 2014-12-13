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
import Entity.Vortex;
import Entity.Enemies.Skeleton;
import Main.GamePanel;
import TileMap.Background;
import TileMap.Tile;
import TileMap.TileMap;

public class Level2State extends GameState
{
	private final int NUMOFENEMIES = 10;
	
	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private Vortex vortex;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	
	private AudioPlayer bgMusic;
	
	public Level2State(GameStateManager gsm, int health, int blades, int avril,
			int score)
	{
		this.gsm = gsm;
		this.init(health, blades, avril, score);
	}
	
	@Override
	public void init(int health, int blades, int avril, int score)
	{
		this.tileMap = new TileMap(30);
		this.tileMap.loadTiles("/Tilesets/tileset1.gif");
		this.tileMap.loadMap("/Maps/level2.map");
		this.tileMap.setPosition(0, 0);
		this.tileMap.setTween(1);
		
		this.bg = new Background("/Backgrounds/Lava.gif", 0.1);
		
		this.player = new Player(this.tileMap);
		this.player.setPosition(100, 100);
		this.player.setHealth(health);
		this.player.setNumOfAvril(avril);
		this.player.setNumOfBlades(blades);
		this.player.setScore(score);
		
		this.vortex = new Vortex(this.tileMap);
		this.vortex.setPosition(4750, 260);
		
		this.populateEnemies();
		
		this.explosions = new ArrayList<Explosion>();
		
		this.hud = new HUD(this.player);
		
		this.bgMusic = new AudioPlayer("/Music/MEOW.mp3");
		this.bgMusic.loop();
	}
	
	private void populateEnemies()
	{
		this.enemies = new ArrayList<Enemy>();
		
		Skeleton s;
		Point[] points = new Point[this.NUMOFENEMIES];
		for(int i = 0; i < this.NUMOFENEMIES; i++)
		{
			points[i] = new Point(4100 + (i * 60), 260);
		}
		for(int i = 0; i < points.length; i++)
		{
			s = new Skeleton(this.tileMap);
			s.setPosition(points[i].x, points[i].y);
			if(this.tileMap.getType(s.gety() / 30, s.getx() / 30) != Tile.BLOCKED)
			{
				this.enemies.add(s);
			}
		}
	}
	
	@Override
	public void update()
	{
		// update player
		this.player.update(this.enemies);
		this.tileMap.setPosition(GamePanel.WIDTH / 2 - this.player.getx(),
				GamePanel.HEIGHT / 2 - this.player.gety());
		
		// check player-vortex intersection (go to next level)
		if(this.player.intersects(this.vortex))
		{
			int health = this.player.getHealth();
			int blades = this.player.getNumOfBlades();
			int avril = this.player.getNumOfAvril();
			int score = this.player.getScore();
			
			this.bgMusic.stop();
			this.gsm.setState(GameStateManager.LEVEL3STATE, health, blades,
					avril, score);
		}
		
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
			this.bgMusic.stop();
			this.gsm.setState(GameStateManager.DEADSTATE, 0, 0, 0, 0);
		}
		
		// check if fall into lava
		if(this.player.getRestartLevel())
		{
			int health = this.player.getHealth();
			int blades = this.player.getNumOfBlades();
			int avril = this.player.getNumOfAvril();
			int score = this.player.getScore();
			
			if(health - 1 == 0)
			{
				this.player.setDead(true);
				this.bgMusic.stop();
				this.gsm.setState(GameStateManager.DEADSTATE, 0, 0, 0, 0);
			}
			
			else
			{
				this.bgMusic.stop();
				this.init(health - 1, blades, avril, score);
			}
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
		
		// update vortex
		this.vortex.update();
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
		
		// draw vortex
		this.vortex.draw(g);
		
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
