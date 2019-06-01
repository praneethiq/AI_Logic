import java.util.Random;

/**
 * This class contains the actual board and gives information to the agent when requested
 * @author 180029539
 *
 */
public class Game {

	int levelchoice=0;
	String[][] map=null;
	Random ran = new Random();
	int randomnumber = ran.nextInt(10) + 1; 	
	int daggers=0;
			
	/**
	 * Randomly selects a map with the given difficulty
	 * @param choice difficulty of the game
	 */
	Game(int choice)
	{
		levelchoice=choice;
		selectMap(levelchoice);
		System.out.println("(Your randomly generated board from world file is: "+randomnumber+")");
	}
	
	
	/**
	 * Randomizes either a easy or a medium or a hard map
	 * @param choice difficulty of the game
	 */
	public void selectMap(int choice)
	{
	switch(choice)
	{
	case 1:
		selectRandomEasy();break;
	case 2:
		selectRandomMedium();break;
	case 3:
		selectRandomHard();break;
	}
	}
	
	/**
	 * Randomly picks an easy map
	 */
	private void selectRandomEasy() {
		switch(randomnumber)
		{
		case 1:
			map=World.EASY1.map;break;
		case 2:
			map=World.EASY2.map;break;
		case 3:
			map=World.EASY3.map;break;
		case 4:
			map=World.EASY4.map;break;
		case 5:
			map=World.EASY5.map;break;
		case 6:
			map=World.EASY6.map;break;
		case 7:
			map=World.EASY7.map;break;
		case 8:
			map=World.EASY8.map;break;
		case 9:
			map=World.EASY9.map;break;
		case 10:
			map=World.EASY10.map;break;
		}
		
	}
	/**
	 * Randomly picks a medium map
	 */
	private void selectRandomMedium() {
		switch(randomnumber)
		{
		case 1:
			map=World.MEDIUM1.map;break;
		case 2:
			map=World.MEDIUM2.map;break;
		case 3:
			map=World.MEDIUM3.map;break;
		case 4:
			map=World.MEDIUM4.map;break;
		case 5:
			map=World.MEDIUM5.map;break;
		case 6:
			map=World.MEDIUM6.map;break;
		case 7:
			map=World.MEDIUM7.map;break;
		case 8:
			map=World.MEDIUM8.map;break;
		case 9:
			map=World.MEDIUM9.map;break;
		case 10:
			map=World.MEDIUM10.map;break;
		}
	
	}
	/**
	 * Randomly picks a hard map
	 */
	private void selectRandomHard() {
		switch(randomnumber)
		{
		case 1:
			map=World.HARD1.map;break;
		case 2:
			map=World.HARD2.map;break;
		case 3:
			map=World.HARD3.map;break;
		case 4:
			map=World.HARD4.map;break;
		case 5:
			map=World.HARD5.map;break;
		case 6:
			map=World.HARD6.map;break;
		case 7:
			map=World.HARD7.map;break;
		case 8:
			map=World.HARD8.map;break;
		case 9:
			map=World.HARD9.map;break;
		case 10:
			map=World.HARD10.map;break;
		}
	
	}
	/**
	 * Calculates the total number of daggers in the current game
	 * @param map map to be checked
	 * @return number of daggers
	 */
	public int getTotalNoOfDaggers(String[][] map)
	{
		int counter=0;
		for(int i=0;i<map.length;i++)
		{
			for(int j=0;j<map.length;j++)
			{
				if(map[i][j]=="d")
					counter++;
					
			}
		}
		return counter;
	}
}
