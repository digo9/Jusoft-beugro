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
		int inputInt = 0;
		
		final String API_KEY = "AIzaSyBYLh5oDyGX9CFGu5lcFx76YVc51084bz4";
		GeoApiContext context = new GeoApiContext().setApiKey(API_KEY);
		
		while(inputInt != 3) {
			System.out.println("Ird be a kivant muvelet sorszamat:");
			System.out.println("1 - Koordinatak lekerese foldralyzi nev alapjan");
			System.out.println("2 - Foldralyzi nev lekerese koordinatak alapjan");
			System.out.println("3 - Kilepes");
			try{ // If the parseInt method fails, the Scanner throws a Number Format Exception.
				inputString = input.nextLine();
				inputInt = Integer.parseInt(inputString);
			}catch(NumberFormatException nfe) {
				inputInt = 0; // We give "inputInt" a value other than 1, 2 or 3, to trigger the default label in the switch statement.
			}
			switch(inputInt) {
			case 1:
			// For a location input, "case 1" gives coordinates as an output.
				System.out.println("1 - Ird be a foldrajzi nevet!");
				String inputLocation = input.nextLine();
				GeocodingResult[] resultsGeo =  GeocodingApi.geocode(context, inputLocation).await();
				System.out.println(resultsGeo[0].geometry.location);
				
				break;
			case 2:
			// For coordinates as input, "case 2" gives a location as an output.
				System.out.println("2 - Ird be a szelesseget!");
				double latitude = input.nextDouble();
				System.out.println("Ird be a hosszusagot!");
				double longitude = input.nextDouble();
				LatLng latLng = new LatLng(latitude, longitude); // Merging "latitude" and "longitude" into one variable.
				GeocodingResult[] resultsReverseGeo =  GeocodingApi.reverseGeocode(context, latLng).await();
				System.out.println(resultsReverseGeo[0].formattedAddress);
				
				break;
			case 3:
			// Closing the program and the scanner as well.
				System.out.println("Kilepes");
				input.close();
				break;
			default:
				System.err.println("Ervenytelen bemenet!");
				break;
			}
		}
	}
}
