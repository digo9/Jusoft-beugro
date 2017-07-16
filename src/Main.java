/*
 * @author Szabo Gyorgy
 */
//import java.io.FileInputStream;
import java.io.IOException;
//import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
//import java.util.MissingResourceException;
//import java.util.Properties;
import java.util.ResourceBundle;
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
	
	public enum userChoice{
		// We store the available menu choices in an enum.
		invalid, geocode, reverseGeocode, exit;
	}
	
	public static void main(String[] args) throws NumberFormatException, ApiException, InterruptedException, IOException {
		Scanner input;
		String inputString = "";
		int inputInt = 0;
		userChoice choice = null;
		
		/*
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationMessages_hu_HU");
        System.out.println(bundle.getString("CountryName"));
		*/
		
		// Reading the API key out from the config.properties file.
		//Properties prop = new Properties();
		//InputStream propInput = null;
		
		//try {
			//propInput = new FileInputStream("resources/config.properties");
			//prop.load(propInput);
		//} catch (IOException ioe) {
			//System.err.println("Ervenytelen API kulcs!");
			//System.exit(1);
		//}
		ResourceBundle bundle = ResourceBundle.getBundle("config");
		GeoApiContext context = new GeoApiContext().setApiKey(bundle.getString("apikey"));
		// Giving the API the key.
		//GeoApiContext context = new GeoApiContext().setApiKey(prop.getProperty("apikey"));
		
		
		while(choice != userChoice.exit) {
			input = new Scanner(System.in);
			System.out.println("Ird be a kivant muvelet sorszamat:");
			System.out.println("1 - Koordinatak lekerese foldralyzi nev alapjan");
			System.out.println("2 - Foldralyzi nev lekerese koordinatak alapjan");
			System.out.println("3 - Kilepes");
			try{ 
			// These methods would only throw exceptions for invalid inputs.
				inputString = input.nextLine();
				inputInt = Integer.parseInt(inputString);
				choice = userChoice.values()[inputInt];
			}catch(Exception e) {
			// We give "choice" the value "invalid" to trigger the default label in the switch statement.
				choice = userChoice.invalid;
			}
			try {
				switch(choice) {
				case geocode:
				// For a location input, "case geocode" gives coordinates as an output.
					System.out.println("1 - Ird be a foldrajzi nevet!");
					String inputLocation = input.nextLine();
					// Using the API's geocoding service.
					GeocodingResult[] resultsGeo =  GeocodingApi.geocode(context, inputLocation).await();
					// Writing out every result of the geocoding.
					for(int i=0; i<resultsGeo.length; i++) {
						System.out.println((i+1) + ". találat: " + resultsGeo[i].formattedAddress);
						System.out.println(resultsGeo[i].geometry.location);
					}
					break;
				case reverseGeocode:
				// For coordinates as input, "case reverseGeocode" gives a location as an output.
					System.out.println("2 - Ird be a szelesseget! (pl.: 46,53097790)");
					double latitude = input.nextDouble();
					System.out.println("Ird be a hosszusagot! (pl.: 20,96994310)");
					double longitude = input.nextDouble();
					LatLng latLng = new LatLng(latitude, longitude); // Merging "latitude" and "longitude" into one variable.
					// Using the API's reverse geocoding service.
					GeocodingResult[] resultsReverseGeo =  GeocodingApi.reverseGeocode(context, latLng).await();
					System.out.println(resultsReverseGeo[0].formattedAddress);
					break;
				case exit:
				// Closing the program and the scanner as well.
					System.out.println("Kilepes");
					input.close();
					break;
				default:
				// Everything else is considered as a wrong input.
					System.err.println("Ervenytelen bemenet!");
					choice = null;
					break;
				}
			/*}catch(MissingResourceException mre) {
				// Thrown by the ResourceBundle when the 
				System.err.println("Az API kulcs kiolvasasa sikertelen! Lekerdezes elutasitva!");
			*/}catch(InputMismatchException ime) {
				// Thrown by the Scanner when e.g. the user gives a location as a coordinate.
				System.err.println("Ervenytelen bemenet!");
			}catch(UnknownHostException uhe) {
				System.err.println("Nincs internet eleres!");
			}catch(ArrayIndexOutOfBoundsException aie){
				System.err.println("Nem ertelmezett bemenet!");
			}catch(ZeroResultsException zre) {
				System.out.println("Nincs talalat.");
			}catch(OverQueryLimitException oqle) {
				System.err.println("Tul sok lekerdezes erkezett, probald meg kesobb!");
			}catch(RequestDeniedException rde) {
				System.err.println("Lekerdezes elutasitva!");
			}catch(InvalidRequestException ire) {
				System.err.println("Hianyos lekerdezes!");
			}catch(UnknownErrorException uee) {
				System.err.println("Szerver oldali hiba!");
				System.out.println("Az ujboli lekerdezes megoldhatja a prolemat.");
			}finally {
			// Printing out a blank line to make the console more readable.
				System.out.println();
			}
		}
	}
}
