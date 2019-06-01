import java.util.Scanner;

public class gameExecution {
	
	public static void main(String[] args) {
		int exitchoice=0;
		Scanner sc1=new Scanner(System.in);
		do {
			
		int choice=-1;	
		
		System.out.print("\n\nWelcome to Daggers & Gold Sweeper Game\n");
		System.out.print("\nHow would you like to solve the game? \n1)Random Probing 2)Single Point Strategy :");		
		do {
			while(!sc1.hasNextInt())
			{
			System.out.print("Enter a valid integer choice: ");
			sc1.next();
			}
			choice=sc1.nextInt();
			if(!(choice==1||choice==2))
				System.out.print("Enter a valid integer choice between 1 and 2: ");
			
		}while(!(choice==1||choice==2)); 
		Agent ag1=null;
		switch(choice)
		{
		case 1:
			ag1=new Agent();
			ag1.startGameRandomProbe();break;
		case 2:
			ag1=new Agent();
			ag1.startGameSPS();break;
		}
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
