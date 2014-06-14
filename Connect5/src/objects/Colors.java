package objects;
import java.awt.Color;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;


public class Colors 
{
	private static final int numOfColors = 3;
	
	public static final Map<Integer, Color> optionalColors;
	static
	{
		Hashtable<Integer, Color> tmpMap = new Hashtable<Integer, Color>();
		tmpMap.put(1, Color.RED);
		tmpMap.put(2, Color.GREEN);
		tmpMap.put(3, Color.BLUE);
		
		optionalColors = Collections.unmodifiableMap(tmpMap);
	}
	
	public static Color getRandomColor()
	{
		Random ran = new Random();
		int randomNum = ran.nextInt(numOfColors)+1;
		return optionalColors.get(randomNum);
	}
}
