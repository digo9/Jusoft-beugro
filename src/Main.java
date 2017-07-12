import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws NumberFormatException {
		Scanner input = new Scanner(System.in);
		String inputString = "0";
		int inputSzam = 0;
		while(inputSzam != 3) {
			System.out.println("Ird be a kivant muvelet sorszamat:");
			System.out.println("1 - Koordinatak lekerese foldralyzi nev alapjan");
			System.out.println("2 - Foldralyzi nev lekerese koordinatak alapjan");
			System.out.println("3 - Kilepes");
			try{
				inputString = input.nextLine();
				inputSzam = Integer.parseInt(inputString);
			}catch(NumberFormatException nfe) {
				inputSzam = 4;
			}
			if(inputSzam == 1) {
				System.out.println("1-es muvelet");
			}else if(inputSzam == 2) {
				System.out.println("2-es muvelet");
			}else if(inputSzam == 3) {
				System.out.println("Kilepes");
				input.close();
			}else if(inputSzam < 1 || inputSzam > 3){
				System.err.println("Ervenytelen bemenet!");
			}
		}
	}
}
