import java.io.IOException;
import java.util.Scanner;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class Main {
	
	public static void main(String[] args) throws NumberFormatException, ApiException, InterruptedException, IOException {
		Scanner input = new Scanner(System.in);
		String inputString = "0";
		int inputSzam = 0;
		
		final String API_KEY = "AIzaSyBYLh5oDyGX9CFGu5lcFx76YVc51084bz4";
		GeoApiContext context = new GeoApiContext().setApiKey(API_KEY);
		
		while(inputSzam != 3) {
			System.out.println("Ird be a kivant muvelet sorszamat:");
			System.out.println("1 - Koordinatak lekerese foldralyzi nev alapjan");
			System.out.println("2 - Foldralyzi nev lekerese koordinatak alapjan");
			System.out.println("3 - Kilepes");
			try{
				inputString = input.nextLine();
				inputSzam = Integer.parseInt(inputString);
			}catch(NumberFormatException nfe) {
				inputSzam = 0;
			}
			//folrajziNev próba: "1600 Amphitheatre Parkway Mountain View, CA 94043"
			if(inputSzam == 1) {
				System.out.println("1 - Ird be a foldrajzi nevet!");
				String foldrajziNev = input.nextLine();
				GeocodingResult[] results =  GeocodingApi.geocode(context, foldrajziNev).await();
				System.out.println(results[0].formattedAddress);
				
			}else if(inputSzam == 2) {
				System.out.println("2 - Ird be a szelesseget!");
				double szelesseg = input.nextDouble();
				System.out.println("Ird be a hosszusagot!");
				double hosszusag = input.nextDouble();
				LatLng latLng = new LatLng(hosszusag, szelesseg);
				GeocodingResult[] results =  GeocodingApi.reverseGeocode(context, latLng).await();
				System.out.println(results[0].formattedAddress);
				
			}else if(inputSzam == 3) {
				System.out.println("Kilepes");
				input.close();
			}else {
				System.err.println("Ervenytelen bemenet!");
			}
		}
	}
}
