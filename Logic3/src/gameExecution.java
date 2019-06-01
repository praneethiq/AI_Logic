import java.util.Scanner;
import org.logicng.io.parsers.ParserException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;

public class gameExecution {
	
	public static void main(String[] args) throws ParserException, ContradictionException, TimeoutException {
		
		int exitchoice=0;
		Scanner sc1=new Scanner(System.in);
		do {
			
			System.out.println("\n\nWelcome to Daggers & Gold Sweeper CPU VS User Game");
		
			Agent ag1=new Agent();
			ag1.startGameATS();
			if(ag1.usergold>ag1.cpugold)
				System.out.println("\nUser Won");
			else if(ag1.usergold<ag1.cpugold)
				System.out.println("\nCPU Won");
			else
				System.out.println("\nIt is a tie");
			 System.out.print("\nFeeling Excited for one more game. \n1)Yes 2)No :");
		   	 	
			do {
				
				while(!sc1.hasNextInt())
				{
					System.out.print("Enter a valid integer choice: ");
					sc1.next();
				}
			exitchoice=sc1.nextInt();
			
			if(!(exitchoice==1||exitchoice==2))
				System.out.print("Enter a valid integer choice between 1 and 2: ");
			
			}while(!(exitchoice==1||exitchoice==2)); 
		
		}while(!(exitchoice==2)); 
		sc1.close();	
		System.out.println("\nBubbyee..Hope you come back to have more fun...");
	}
}
