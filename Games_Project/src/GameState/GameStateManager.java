package GameState;

public class GameStateManager
{
	private GameState[] gameStates;
	private int currentState;
	
	public static final int NUMGAMESTATES = 2;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	
	public GameStateManager()
	{
		this.gameStates = new GameState[NUMGAMESTATES];
		
		this.currentState = MENUSTATE;
		this.loadState(this.currentState);
	}
	
	private void loadState(int state)
	{
		if(state == MENUSTATE)
		{
			this.gameStates[state] = new MenuState(this);
		}
		if(state == LEVEL1STATE)
		{
			this.gameStates[state] = new Level1State(this);
		}
	}
	
	private void unloadState(int state)
	{
		this.gameStates[state] = null;
	}
	
	public void setState(int state)
	{
		this.unloadState(this.currentState);
		this.currentState = state;
		this.loadState(this.currentState);
		// gameStates[currentState].init();
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
