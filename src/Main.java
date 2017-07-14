/*
 * @author Szabo Gyorgy
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.InvalidRequestException;
import com.google.maps.errors.OverQueryLimitException;
import com.google.maps.errors.RequestDeniedException;
import com.google.maps.errors.UnknownErrorException;
import com.google.maps.errors.ZeroResultsException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class Main {
	public static void main(String[] args) throws NumberFormatException, ApiException, InterruptedException, IOException {
		Scanner input = new Scanner(System.in);
		String inputString = "";
		int inputInt = 0;
		
		// Reading the API key out of the config.properties file located in the resources folder.
		Properties prop = new Properties();
		InputStream propInput = null;
		try {
			propInput = new FileInputStream("resources/config.properties");
			prop.load(propInput);
		} catch (IOException ioe) {
			System.err.println("Ervenytelen API kulcs!");
			System.exit(1);
		}
		// Giving the API the key from the config.
		GeoApiContext context = new GeoApiContext().setApiKey(prop.getProperty("apikey"));
		
		// Starting program cycle.
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
			try {
				switch(inputInt) {
				case 1:
				// For a location input, "case 1" gives coordinates as an output.
					inputInt = 1;
					System.out.println("1 - Ird be a foldrajzi nevet!");
					String inputLocation = input.nextLine();
					GeocodingResult[] resultsGeo =  GeocodingApi.geocode(context, inputLocation).await();
					System.out.println(resultsGeo[0].geometry.location);
					
					break;
				case 2:
				// For coordinates as input, "case 2" gives a location as an output.
					inputInt = 2;
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
				// Everything else is considered as a wrong input.
					System.err.println("Ervenytelen bemenet!");
					break;
				}
			}catch(InputMismatchException ime) {
				// Thrown by the scanner when e.g. the user gives a location as a coordinate.
				System.err.println("Ervenytelen bemenet!");
			}catch(UnknownHostException uhe) {
				System.err.println("Nincs internet eleres!");
			}catch(ArrayIndexOutOfBoundsException aie){ 
				// TODO
				System.err.println("Error");
			}catch(ZeroResultsException zre) {
				System.out.println("Nincs talalat.");
			}catch(OverQueryLimitException oqle) {
				System.err.println("Tul sok lekerdezes erkezett, probald meg kesobb!");
			}catch(RequestDeniedException rde) {
				System.err.println("Lekerdezes elutasitva!");
			}catch(InvalidRequestException ire) {
				System.err.println("Hianyos lekerdezes!");
			}catch(UnknownErrorException uee) {
				// TODO: make it possible to resend the query.
				System.err.println("Szerver oldali hiba!");
				System.out.println("Az ujboli lekerdezes megoldhatja a prolemat.");
			}
		}
	}
}
