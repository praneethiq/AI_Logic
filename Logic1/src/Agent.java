import java.util.Random;
import java.util.Scanner;

/**
 * This class defines all the functionalities of an agent and also communicates with the Game class
 * @author 180029539
 *
 */
public class Agent {
	
	
	private int levelchoice=0,length=0,totalcells=0;;
	private int guesses=0,daggers=0,lives=1;
	private int step=0;
	Game g1=null;
	String[][] agentmap=null;
	Scanner sc1=new Scanner(System.in);
    /**
     * This constructor helps user pick a world and creates a link between both agent and game class.
     * Also, initializes an empty grid to keep track of the knowledge base of the game
     */
    Agent()
    {
    	levelchoice=inputChoice();
    	g1=new Game(levelchoice);
	    agentmap=getMap(levelchoice);
		length=agentmap.length;
		daggers=g1.getTotalNoOfDaggers(g1.map);
		totalcells=(length-2)*(length-2);
		
    }
	/**
	 * This method helps the user pick the difficulty of the game
	 * @return the difficulty of the game
	 */
	public int inputChoice()
	{
		
		int choice=-1;
	System.out.print("\nEnter a level:\n1)Easy 2)Medium 3)Hard: ");	
	do {
		while(!sc1.hasNextInt())
		{
		System.out.print("Enter a valid integer choice: ");
		sc1.next();
		}
		choice=sc1.nextInt();
		if(choice<1||choice>3)
			System.out.print("Enter a valid integer choice between 1 and 3: ");
	}while(choice<1||choice>3); 
	return choice;
	}
	/**
	 * This method takes the difficulty of the map and initializes a board for storing the knowledge base
	 * @param choice difficulty of the map
	 * @return 		 map for storing the knowledge base 
	 */
	public String[][] getMap(int choice)
	{
		int length=0;
		if(choice==1)
			length=5;
		else if(choice==2)
			length=8;
		else
			length=12;
		String[][] initialmap=new String[length+2][length+2];
		for(int i=1;i<length+1;i++)
		{
			for(int j=1;j<length+1;j++)
				initialmap[i][j]="u";
		}
		return initialmap;
	}
	/**
	 * displays the map to show the current state of the game
	 * @param map the map to be displayed
	 */
	public void displayMap(String[][] map)
	{
		
       for(int i=1;i<length-1;i++)
       {
    	   for(int j=1;j<length-1;j++)
    	   {
    		   System.out.print(" "+map[i][j]+" ");
    	   }
       System.out.println();
       }  
	
	}
	/**
	 * Initializes random probing and continues until game is finished
	 */
	public void startGameRandomProbe()
    {
   	 agentmap[1][1]=probeCell(1,1);
   	 do {
   		 
   		 if(getNoOfUnexploredCells()>0&&lives>0)
   		 {
   			 randomProbe();
   			 guesses++;
   		 }
    	}while(lives>0&&daggers<(getNoOfUnexploredCells()+getNoOfMarkedCells()+getNoOfDaggerCells()));
	 	getStatusOfGame();
    }
	
	/**
	 * Executes Single point strategy and exits on completion of the game
	 */
	public void startGameSPS()
    {
		int updatedcells=0;
	
   	 agentmap[1][1]=probeCell(1,1);
   	 do {
   		updatedcells=0;
   		 for(int i=1;i<length-1;i++)
   		 {
   			 if(lives<1)break;
   			 for(int j=1;j<length-1;j++)
   			 {
   				if(lives<1)break;
   				if(getNoOfUnexploredNeighbours(i,j)>0)
	 				{
					 
	 					if(agentmap[i][j]=="0"||agentmap[i][j]=="g")   					
	 						updatedcells+=probeAllNeighbours(i,j);
	 						
	 					else if(agentmap[i][j]!="0"&&isCellANumber(i,j))
	 					{
	 						int value=Integer.parseInt(agentmap[i][j]);
	 							
	 						if(value==(getNoOfDaggerNeighbours(i,j)+getNoOfMarkedNeighbours(i,j)))
	 							updatedcells+=probeAllNeighbours(i,j);
						
	 						if(value==(getNoOfUnexploredNeighbours(i,j)+getNoOfDaggerNeighbours(i,j)+getNoOfMarkedNeighbours(i,j)))
	 							updatedcells+=markAllUnexploredNeighbours(i,j);
   					}
   					
   				 }
   			
   			 }
   			 
   		 }
   		 
   		 if(updatedcells==0&&getNoOfUnexploredCells()>0&&lives>0)
   		 {
   			 randomProbe();
   			 updatedcells++;
   			 guesses++;
   		 }
 	}while(lives>0&&daggers<(getNoOfUnexploredCells()+getNoOfMarkedCells()+getNoOfDaggerCells()));
   	getStatusOfGame();
}
 	/**
 	 * Gets the final statistics of the game like board position, no of daggers marked,
 	 * no of daggers probed, no of gold found, total daggers, uncovered cells, win/loss, guesses made 
 	 */
 	private void getStatusOfGame() {
 		
 		System.out.print("\n\nYour Final Board Position is: \n");
 	 	displayMap(agentmap);
 	 	System.out.print("\nTotal Cells: "+totalcells);
 	 	System.out.print("\nUncovered Cells: "+(totalcells-getNoOfUnexploredCells()));
 	 	System.out.print("\nTotal Daggers: "+daggers);
 	 	System.out.print("\nTotal Daggers Uncovered: "+getNoOfDaggerCells());
 	 	System.out.print("\nTotal Cells Marked as Daggers: "+getNoOfMarkedCells());
 	 	System.out.print("\nTotal Gold found: "+getNoOfGoldCells());
  
 	 	if(lives>0&&daggers==(getNoOfUnexploredCells()+getNoOfMarkedCells()+getNoOfDaggerCells()))
 	 	{
 		 System.out.print("\n\nYou Won the game after making "+guesses +" guesses.");
 		 System.out.print("\nFeeling Excited for one more game. \n1)Yes 2)No :");
 	 	}
 	 	else
 	 	{
 		System.out.print("\n\nYou Lost the game after making "+guesses +" guesses.");
 		System.out.print("\nDont feel bad.Wanna try again. \n1)Yes 2)No :");
 	 	}
 		
 	}
 	/**
 	 * probes the cell at given coordinates
 	 * @param x X-coordinate
 	 * @param y Y-coordinate
 	 * @return  the contents of the cell
 	 */
 	public String probeCell(int x,int y)
	{
		System.out.print("\n\nStep "+step+":\nReveal ("+x+","+y+") -> "+g1.map[x-1][y-1]);step++;
		if(g1.map[x-1][y-1]=="g")
		{
			lives++;
			System.out.print("\nGoldmine in ("+x+","+y+"), new life count: "+lives+".");
		}
		if(g1.map[x-1][y-1]=="d")
		{
			lives--;
			System.out.print("\nDagger in ("+x+","+y+"), new life count: "+lives+".");
		}
		
	return g1.map[x-1][y-1];
	}
	/**
	 * Calculates the number of unexplored neighbours of the current cell
	 * @param x X-Coordinate
	 * @param y Y-Coordinate
	 * @return  number of unexplored neighbours
	 */
	public int getNoOfUnexploredNeighbours(int x, int y)
	{
		int counter=0;
		for(int i=x-1;i<=x+1;i++)
		{
			for(int j=y-1;j<=y+1;j++)
			{
				if(agentmap[i][j]!=null&&agentmap[i][j]=="u")
					counter++;
					
			}
		}
		return counter;
	}
	/**
	 * Marks all the unexplored neighbours of a cell
	 * @param x X-Coordinate
	 * @param y Y-Coordinate
	 * @return  number of cells marked
	 */
	public int markAllUnexploredNeighbours(int x, int y) {
		{
			int counter=0;
			for(int i=x-1;i<=x+1;i++)
			{
				for(int j=y-1;j<=y+1;j++)
				{
					if(agentmap[i][j]!=null&&agentmap[i][j]=="u")
					{
						agentmap[i][j]="m";
						System.out.print("\n\nStep "+(step++)+":\nMarking the presence of dagger in ("+i+","+j+").");
						counter++;
					}
						
				}
			}
			return counter;
			
		}
		
	}

	/**
	 * Calculates the number of marked neighbours of the current cell
	 * @param x X-Coordinate
	 * @param y Y-Coordinate
	 * @return  number of marked neighbours
	 */
	public int getNoOfMarkedNeighbours(int x, int y)
	{
		int counter=0;
		for(int i=x-1;i<=x+1;i++)
		{
			for(int j=y-1;j<=y+1;j++)
			{
				if(agentmap[i][j]!=null&&(agentmap[i][j]=="m"))
					counter++;
					
			}
		}
		return counter;
	}
	/**
	 * Calculates the number of dagger neighbours of the current cell
	 * @param x X-Coordinate
	 * @param y Y-Coordinate
	 * @return number of dagger neighbours
	 */
	public int getNoOfDaggerNeighbours(int x, int y)
	{
		int counter=0;
		for(int i=x-1;i<=x+1;i++)
		{
			for(int j=y-1;j<=y+1;j++)
			{
				if(agentmap[i][j]!=null&&(agentmap[i][j]=="d"))
					counter++;
					
			}
		}
		return counter;
	}
	/**
	 * Probes all the unexplored neighbours of a cell
	 * @param x X-Coordinate
	 * @param y Y-Coordinate
	 * @return  number of cells probed
	 */
	public int probeAllNeighbours(int x,int y)
	{
		int counter=0;
		for(int i=x-1;i<=x+1;i++)
		{
			for(int j=y-1;j<=y+1;j++)
			{
				if(agentmap[i][j]!=null&&agentmap[i][j]=="u")
				{
					agentmap[i][j]=probeCell(i,j);
					counter++;
				}
					
			}
		}
		return counter;
		
	}
	/**
	 * Calculates the number of unexplored cells in the current game
	 * @return number of unexplored cells
	 */
	public int getNoOfUnexploredCells()
	{
		int counter=0;
		for(int i=1;i<length-1;i++)
	       {
	    	   for(int j=1;j<length-1;j++)
	    	   {
	    		   if(agentmap[i][j]=="u")
	    			   counter++;
	    	   }
	       }
		return counter; 
	}
	/**
	 * Calculates the number of marked cells in the current game
	 * @return number of marked cells
	 */
	public int getNoOfMarkedCells()
	{
		int counter=0;
		for(int i=1;i<length-1;i++)
	       {
	    	   for(int j=1;j<length-1;j++)
	    	   {
	    		   if(agentmap[i][j]=="m")
	    			   counter++;
	    	   }
	       }
		return counter; 
	}
	/**
	 * Calculates the number of gold probed in the current game
	 * @return number of gold
	 */
	public int getNoOfGoldCells()
	{
		int counter=0;
		for(int i=1;i<length-1;i++)
	       {
	    	   for(int j=1;j<length-1;j++)
	    	   {
	    		   if(agentmap[i][j]=="g")
	    			   counter++;
	    	   }
	       }
		return counter; 
	}
	/**
	 * Calculates the number of daggers probed in the current game
	 * @return number of daggers
	 */
	public int getNoOfDaggerCells()
	{
		int counter=0;
		for(int i=1;i<length-1;i++)
	       {
	    	   for(int j=1;j<length-1;j++)
	    	   {
	    		   if(agentmap[i][j]=="d")
	    			   counter++;
	    	   }
	       }
		return counter; 
	}
	/**
	 * Returns if a given cell contains a number or not
	 * @param x X-Coordinate
	 * @param y Y-Coordinate
	 * @return true or false
	 */
	public boolean isCellANumber(int x,int y)
	{
		return (agentmap[x][y]!="u"&&agentmap[x][y]!="g"&&agentmap[x][y]!="d"&&agentmap[x][y]!="m");
	}
	/**
	 * Randomly probes a cell
	 */
	public void randomProbe()
	{
		while(true)
		{
			Random ran = new Random();
			int x=ran.nextInt(length-1) + 1;
			int y=ran.nextInt(length-1) + 1;
			if(agentmap[x][y]=="u")
			{
				System.out.print("\n\nUsing Random Probing on ("+x+","+y+")");
				agentmap[x][y]=probeCell(x,y);break;
			}
		}
	}
	
	
}

