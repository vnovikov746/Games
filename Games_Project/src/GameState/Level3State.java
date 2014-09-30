package GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Audio.AudioPlayer;
import Entity.Cthulhu;
import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Entity.Enemies.Conformist;
import Main.GamePanel;
import TileMap.Background;
import TileMap.Tile;
import TileMap.TileMap;

public class Level3State extends GameState
{
	private final int NUMOFENEMIES = (4500 / 60);
	
	private Cthulhu cthulu;
	
	// map states
	private final int MAP_A = 0;
	private final int MAP_B = 1;
	private final int MAP_C = 2;
	private int mapState;
	private int prevMapState;
	
	private Level3Helper levelHelper;
	private Thread moveMapThread;
	
	private TileMap tileMap;
	
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	
	private AudioPlayer bgMusic;
	
	public Level3State(GameStateManager gsm, int health, int blades, int avril,
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
		this.tileMap.loadMap("/Maps/level3b.map");
		this.tileMap.setPosition(0, 0);
		this.tileMap.setTween(1);
		
		this.prevMapState = this.mapState = this.MAP_A;
		
		this.bg = new Background("/Backgrounds/Lava.gif", 0.1);
		
		this.levelHelper = new Level3Helper();
		this.moveMapThread = new Thread(this.levelHelper);
		this.moveMapThread.start();
		
		this.player = new Player(this.tileMap);
		this.player.setPosition(100, 100);
		this.player.setHealth(health);
		this.player.setNumOfAvril(avril);
		this.player.setNumOfBlades(blades);
		this.player.setScore(score);
		
		this.cthulu = new Cthulhu(this.tileMap);
		this.cthulu.setPosition(4680, 175);
		
		this.populateEnemies();
		
		this.explosions = new ArrayList<Explosion>();
		
		this.hud = new HUD(this.player);
		
		this.bgMusic = new AudioPlayer("/Music/chimpnology.mp3");
		this.bgMusic.loop();
	}
	
	private void populateEnemies()
	{
		this.enemies = new ArrayList<Enemy>();
		
		Conformist c;
		Point[] points = new Point[this.NUMOFENEMIES];
		for(int i = 0; i < this.NUMOFENEMIES; i++)
		{
			points[i] = new Point(200 + (i * 60), 100);
		}
		for(int i = 0; i < points.length; i++)
		{
			c = new Conformist(this.tileMap);
			c.setPosition(points[i].x, points[i].y);
			if(this.tileMap.getType(c.gety() / 30, c.getx() / 30) != Tile.BLOCKED)
			{
				this.enemies.add(c);
			}
		}
	}
	
	@Override
	public void update()
	{
		// if got to cthulhu
		if(this.player.getx() >= 4570)
		{
			this.player.setPosition(4570, 100);
			this.bgMusic.stop();
			this.gsm.setState(GameStateManager.CTHULHUDIALOG, 0, 0, 0, 0);
		}
		
		// move map
		if(this.levelHelper.getMoveMap())
		{
			if(this.mapState == this.MAP_A || this.mapState == this.MAP_C)
			{
				this.prevMapState = this.mapState;
				this.tileMap.loadMap("/Maps/level3b.map");
				this.mapState = this.MAP_B;
			}
			else
			{
				if(this.prevMapState == this.MAP_A)
				{
					this.prevMapState = this.mapState;
					this.tileMap.loadMap("/Maps/level3c.map");
					this.mapState = this.MAP_C;
				}
				else if(this.prevMapState == this.MAP_C)
				{
					this.prevMapState = this.mapState;
					this.tileMap.loadMap("/Maps/level3a.map");
					this.mapState = this.MAP_A;
				}
			}
			this.levelHelper.setMoveMap(false);
		}
		
		// update cthulhu
		this.cthulu.update();
		
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
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		// draw bg
		this.bg.draw(g);
		
		// draw tileMap
		this.tileMap.draw(g);
		
		// draw player
		this.player.draw(g);
		
		// draw cthulhu
		this.cthulu.draw(g);
		
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
