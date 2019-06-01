import java.util.*;

import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.sat4j.core.VecInt;
import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
/**
 * This class defines all the functionalities of an agent and also communicates with the Game class
 * @author 180029539
 *
 */
public class Agent {
	
	private int levelchoice=0,length=0,totalcells=0;;
	private int guesses=0,daggers=0,lives=1;
	private int step=0,index=1;
	private Game g1=null;
	private String[][] agentmap=null;
	private HashMap<String, Integer> hmap = new HashMap<String, Integer>();
	private Scanner sc1=new Scanner(System.in);
	public int cpugold=0,usergold=0;
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
	private int inputChoice()
	{	
		int choice=-1;
		
		System.out.print("\nEnter a level:\n1)Easy 2)Medium 3)Hard: ");	
	
		do 
		{
			
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
	 * Executes Satisfiability Testing Strategy and exits on completion of the game
	 */
	public void startGameATS() throws ParserException, ContradictionException, TimeoutException
    {
		int updatedcells=0;
   	 	probeCell(1,1);
   	 	do 
   	 	{
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
   	 			
   	 			if(useSatSolver())
   	 				updatedcells++;
 
   	 			else
   	 			{
   	 				randomProbe();
   	 				updatedcells++;
   	 				guesses++;
   	 			}
   	 		
   	 		}
   	 	
   	 	}while((lives>0&&daggers<(getNoOfUnexploredCells()+getNoOfMarkedCells()+getNoOfDaggerCells())));
   
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
    
		
	}
	/**
 	 * probes the cell at given coordinates
 	 * @param x X-coordinate
 	 * @param y Y-coordinate
 	 * @return  the contents of the cell
 	 */
	public void probeCell(int x,int y)
	{
		System.out.print("\n\nStep "+step+":\nReveal ("+x+","+y+") -> "+g1.map[x-1][y-1]);step++;
		if(g1.map[x-1][y-1]=="g")
		{
			lives++;cpugold++;
			System.out.print("\nGoldmine in ("+x+","+y+"), new life count: "+lives+".");
		}
		if(g1.map[x-1][y-1]=="d")
		{
			lives--;
			System.out.print("\nDagger in ("+x+","+y+"), new life count: "+lives+".");
		}
		agentmap[x][y]=g1.map[x-1][y-1];
	userProbe();	
	}
	private void userProbe() {
		System.out.println("\nCurrent Status of board");
		displayMap(agentmap);
		System.out.print("\nEnter x axis of cell to probe: ");
		int x=sc1.nextInt();
		System.out.print("\nEnter y axis of cell to probe :");
		int y=sc1.nextInt();
		System.out.print("\n\nStep "+step+":\nReveal ("+x+","+y+") -> "+g1.map[x-1][y-1]);step++;
		if(g1.map[x-1][y-1]=="g")
		{
			lives++;usergold++;
			System.out.print("\nGoldmine in ("+x+","+y+"), new life count: "+lives+".");
		}
		if(g1.map[x-1][y-1]=="d")
		{
			lives--;
			System.out.print("\nDagger in ("+x+","+y+"), new life count: "+lives+".");
		}
		agentmap[x][y]=g1.map[x-1][y-1];
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
					probeCell(i,j);
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
				probeCell(x,y);
				break;
			}
		}
	}
	
	/**
	 * Takes the KBU and probes the cell if satisfiability fails
	 * @return probed or not
	 * @throws ParserException
	 * @throws ContradictionException
	 * @throws TimeoutException
	 */
	public boolean useSatSolver() throws ParserException, ContradictionException, TimeoutException
	{
		
		String KBU=createKBU();
		//System.out.println();
		//displayMap(agentmap);
		//System.out.println();
		//System.out.println(KBU);
		for(int i=1;i<length-1;i++)
  		 {
  			 for(int j=1;j<length-1;j++)
  			 {
                if(agentmap[i][j]=="u")
                {
                	String clearCheck=KBU+"&(D"+i+"X"+j+")";
                	if(!checkSatisfiability(clearCheck))
                	{
                		System.out.print("\n\nUsing SAT Solver to unlock ("+i+","+j+").");	
                		probeCell(i,j);
                		return true;
                	}
                }
  			 }
  			 
  		 }
		return false;
		
	}
	
	/**
	 * Creates Knowledge base of the current state of the game
	 * @return Knowledge base in String format
	 */
	public String createKBU()
	{
		String KBU="(";
		String tempFormula=null;
		for(int i=1;i<length-1;i++)
 		 {
 			 for(int j=1;j<length-1;j++)
 			 {
               if(isCellANumber(i,j)&&getNoOfUnexploredNeighbours(i,j)>0)
               {
               		tempFormula=getFormulaForCell(i,j);
               		KBU=KBU+tempFormula+"&";
               }
 			 }
 			 
 		 }
		return (KBU.substring(0,(KBU.length()-1))+")");
	}
	
	/**
	 * Gets individual formula for the unexplored neighbours of a cell 
	 * based on its value, marked neighbours and dagger neighbours
	 * @param x X-Coordinate
	 * @param y Y-Coordinate
	 * @return Knowledge base of the neighbours of a particular cell
	 */
	public String getFormulaForCell(int x,int y)
	{
		String cellFormula="(";
		String[] unvisitedNeighbours=getUnexploredNeighbours(x,y);
		String[] temp=new String[unvisitedNeighbours.length];
		int noOfUnexploredNeighbours=getNoOfUnexploredNeighbours(x,y);
		int negatives=noOfUnexploredNeighbours-((Integer.parseInt(agentmap[x][y]))-getNoOfMarkedNeighbours(x,y)-getNoOfDaggerNeighbours(x,y));
		
		for(int i=0;i<(1 << noOfUnexploredNeighbours);i++) 
		{ 
			int	num =1<<(noOfUnexploredNeighbours-1); 
				for(int j=0;j<noOfUnexploredNeighbours;j++) 
				{
					if ((i & num) > 0) 	 
						temp[j]=unvisitedNeighbours[j] ;
					else
						temp[j]="~"+unvisitedNeighbours[j] ; 

					num = num >> 1; 
				}
				
				String tempString = String.join("&",temp);
				int count = tempString.length() - tempString.replace("~", "").length();
				if(count==negatives)
				{
					cellFormula=cellFormula+"("+tempString+")|";
				}
		}
		
	 return (cellFormula.substring(0,(cellFormula.length()-1))+")");	
	}
	
	/**
	 * Returns an array of all the unexplored neighbours of a cell
	 * @param x X-Coordinate
	 * @param y Y-Coordinate
	 * @return array of neighbours
	 */
	public String[] getUnexploredNeighbours(int x,int y)
	{
		String[] neighbours=new String[getNoOfUnexploredNeighbours(x,y)];
		int pos=0;
		for(int i=x-1;i<=x+1;i++)
		{
			for(int j=y-1;j<=y+1;j++)
			{
				if(agentmap[i][j]!=null&&agentmap[i][j]=="u")
					neighbours[pos++]="D"+Integer.toString(i)+"X"+Integer.toString(j);
			}
		}
		return neighbours;
	}
	
	/**
	 * invokes SAT Solver on a propositional formula and returns whether satisfiable or not
	 * @param str input propositional formula
	 * @return satisfiable or not
	 * @throws ParserException
	 * @throws TimeoutException
	 * @throws ContradictionException
	 */
	public boolean checkSatisfiability(String str) throws ParserException, TimeoutException, ContradictionException
	{
		int counter=0,k=0;
		
		final FormulaFactory f = new FormulaFactory();
		final PropositionalParser p = new PropositionalParser(f);
		
		final Formula formula=p.parse(str);
		final Formula cnf = formula.cnf();
		
		Iterator<Formula> iterator=cnf.iterator();
		while(iterator.hasNext()) {
			iterator.next();
			counter++;
		}
		
		Formula[] clauseArray=new Formula[counter];
		Iterator<Formula> iterator1=cnf.iterator();
		while(iterator1.hasNext())
			clauseArray[k++]=iterator1.next();
		
		final int MAXVAR = 1000;
		final int NBCLAUSES = counter;
		
		ISolver solver = SolverFactory.newDefault();
		solver.newVar(MAXVAR);
		solver.setExpectedNumberOfClauses(NBCLAUSES);
		
		for (int i=0;i<NBCLAUSES;i++) {
			int [] clause = clauseToIntArray(clauseArray[i]);
			solver.addClause(new VecInt(clause));
		}

		IProblem problem = solver;
		
		if (problem.isSatisfiable())
		return true;
		else 
		return false;
	}
	
	/**
	 * converts a given formula into an integer array
	 * @param clause the formula to be converted
	 * @return       array of integers equivalent to the input formula
	 */
	public int[] clauseToIntArray(Formula clause)
	{
		int literalCount=0,sign=1;
		
		for (Iterator<Literal> iterator = clause.literals().iterator(); iterator.hasNext();) {
			iterator.next();
			literalCount++;
		}
		
		int[] clauseArray=new int[literalCount--];
		for(Literal literal1:clause.literals())
		{
			sign=1;
			String tempstring=literal1.toString();
			
			if(tempstring.charAt(0)=='~') 
			{
				tempstring=tempstring.substring(1);
				sign=-1;
			}
			
			if(hmap.get(tempstring)==null)
				hmap.put(tempstring,index++);

			clauseArray[literalCount--]=(sign*hmap.get(tempstring));
		}
		return clauseArray;
	}
	
}

