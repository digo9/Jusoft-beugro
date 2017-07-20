/*
 * @author Szabo Gyorgy
 */
//import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Locale;
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
		// Scanner will be used for input reading.
		Scanner input;
		// Initializing the global variables.
		String inputString = "";
		int inputInt = 0;
		userChoice choice = null;
		
		// Reading the API key out from config.properties file.
		ResourceBundle configBundle = ResourceBundle.getBundle("config");
		// Giving the API the key.
		GeoApiContext context = new GeoApiContext().setApiKey(configBundle.getString("apikey"));
		
		// Deciding which language file to use by the program arguments.
		String language = new String(args[0]);
		String country = new String(args[1]);
		Locale currentLocale = new Locale(language, country);
		ResourceBundle bundle = ResourceBundle.getBundle("language", currentLocale);

		while(choice != userChoice.exit) {
			// Using the Scanner to read inputs. 
			input = new Scanner(System.in);
			System.out.println(bundle.getString("menu"));
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
					System.out.println(bundle.getString("choice1"));
					String inputLocation = input.nextLine();
					// Using the API's geocoding process.
					GeocodingResult[] resultsGeo =  GeocodingApi.geocode(context, inputLocation).await();
					// Writing out every result of the geocoding.
					for(int i=0; i<resultsGeo.length; i++) {
						System.out.println(bundle.getString("whichIs") + (i+1) + ": " + resultsGeo[i].formattedAddress);
						System.out.println(resultsGeo[i].geometry.location);
					}
					break;
				case reverseGeocode:
				// For coordinates as inputs, "case reverseGeocode" gives a location as an output.
					System.out.println(bundle.getString("choice2"));
					double latitude = input.nextDouble();
					double longitude = input.nextDouble();
					// Merging "latitude" and "longitude" into one variable.
					LatLng latLng = new LatLng(latitude, longitude);
					// Using the API's reverse geocoding process.
					GeocodingResult[] resultsReverseGeo =  GeocodingApi.reverseGeocode(context, latLng).await();
					System.out.println(resultsReverseGeo[0].formattedAddress);
					break;
				case exit:
				// Closing the program and the scanner as well.
					System.out.println(bundle.getString("choice3"));
					input.close();
					break;
				default:
				// Everything else is considered as a wrong input.
					System.err.println(bundle.getString("wrongInput"));
					choice = null;
					break;
				}
			}catch(InputMismatchException ime) {
				// Thrown by the Scanner when e.g. the user gives a location as a coordinate.
				System.err.println(bundle.getString("InputMismatchExceptionMessage"));
			}catch(UnknownHostException uhe) {
				System.err.println(bundle.getString("UnknownHostExceptionMessage"));
			}catch(ArrayIndexOutOfBoundsException aie){
				System.err.println(bundle.getString("ArrayIndexOutOfBoundsExceptionMessage"));
			}catch(ZeroResultsException zre) {
				System.out.println(bundle.getString("ZeroResultsExceptionMessage"));
			}catch(OverQueryLimitException oqle) {
				System.err.println(bundle.getString("OverQueryLimitExceptionMessage"));
			}catch(RequestDeniedException rde) {
				System.err.println(bundle.getString("RequestDeniedExceptionMessage"));
			}catch(InvalidRequestException ire) {
				System.err.println(bundle.getString("InvalidRequestExceptionMessage"));
			}catch(UnknownErrorException uee) {
				System.err.println(bundle.getString("UnknownErrorExceptionMessage1"));
				System.out.println(bundle.getString("UnknownErrorExceptionMessage2"));
			}finally {
			// Printing out a blank line to make the console more readable.
				System.out.println();
			}
		}
	}
}
