import java.util.Scanner;
import org.logicng.io.parsers.ParserException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;

public class gameExecution {
	
	public static void main(String[] args) throws ParserException, ContradictionException, TimeoutException {
		int exitchoice=0;
		Scanner sc1=new Scanner(System.in);
		do {
			
			System.out.println("\n\nWelcome to Daggers & Gold Sweeper Game");
		
			Agent ag1=new Agent();
			ag1.startGameATS();
		
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
