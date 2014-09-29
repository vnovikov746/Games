package GameState;

public class GameStateManager
{
	private GameState[] gameStates;
	private int currentState;
	
	public static final int NUMGAMESTATES = 7;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int LEVEL2STATE = 2;
	public static final int LEVEL3STATE = 3;
	public static final int HELPSTATE = 4;
	public static final int DEADSTATE = 5;
	public static final int WINSTATE = 6;
	
	public GameStateManager()
	{
		this.gameStates = new GameState[NUMGAMESTATES];
		
		this.currentState = MENUSTATE;
		this.loadState(this.currentState, 0, 0, 0, 0);
	}
	
	private void loadState(int state, int health, int blades, int avril,
			int score)
	{
		if(state == MENUSTATE)
		{
			this.gameStates[state] = new MenuState(this);
		}
		if(state == HELPSTATE)
		{
			this.gameStates[state] = new HelpState(this);
		}
		if(state == LEVEL1STATE)
		{
			this.gameStates[state] = new Level1State(this, health, blades,
					avril, score);
		}
		if(state == LEVEL2STATE)
		{
			this.gameStates[state] = new Level2State(this, health, blades,
					avril, score);
		}
		if(state == LEVEL3STATE)
		{
			this.gameStates[state] = new Level3State(this, health, blades,
					avril, score);
		}
		if(state == DEADSTATE)
		{
			this.gameStates[state] = new DeadState(this);
		}
		if(state == WINSTATE)
		{
			this.gameStates[state] = new WinState(this);
		}
	}
	
	private void unloadState(int state)
	{
		this.gameStates[state] = null;
	}
	
	public void setState(int state, int health, int blades, int avril, int score)
	{
		this.unloadState(this.currentState);
		this.currentState = state;
		this.loadState(this.currentState, health, blades, avril, score);
	}
	
	public void update()
	{
		try
		{
			this.gameStates[this.currentState].update();
		}
		catch(Exception e)
		{}
	}
	
	public void draw(java.awt.Graphics2D g)
	{
		try
		{
			this.gameStates[this.currentState].draw(g);
		}
		catch(Exception e)
		{}
	}
	
	public void keyPressed(int k)
	{
		this.gameStates[this.currentState].keyPressed(k);
	}
	
	public void keyReleased(int k)
	{
		this.gameStates[this.currentState].keyReleased(k);
	}
}
