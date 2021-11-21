package ticketviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

public class ticketViewer {

	private static HttpURLConnection connection;

	public static void main(String[] args) {

		apiConnect();

	}

	// Establishes connection with Zendesk API and runs
	public static void apiConnect() {

		try {
			// Url connection to grab tickets
			URL url = new URL("https://zcchal080ucsd.zendesk.com/api/v2/tickets.json/");
			connection = (HttpURLConnection) url.openConnection();

			String credentials = ("hal080@ucsd.edu" + ":" + "password");
			// Encoding into base64 credentials for header to accept
			String encodeBytes = Base64.getEncoder().encodeToString((credentials).getBytes());

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Basic " + encodeBytes);

			InputStreamReader inputReader = new InputStreamReader(connection.getInputStream());
			BufferedReader bReader = new BufferedReader(inputReader);
			int status = connection.getResponseCode();

			// If we get status code 200, successfully connect
			if (status == connection.HTTP_OK) {

				Gson gson = new Gson();
				BufferedReader br = null;
				br = new BufferedReader(inputReader);
				//ticket is a single ticket object
				Ticket ticket = gson.fromJson(br, Ticket.class);
				String userChoice = "";

				// Main area: Where user chooses options
				while (!userChoice.equalsIgnoreCase("quit")) {

					// Calls for user input in Interface method
					userChoice = Interface();

					switch (userChoice) {
					//view all tickets
					case "1":
						requestAll(ticket);
						break;
					//view a specific ticket
					case "2":
						requestOne(ticket);
						break;
					//quit
					case "quit":
						break;

					default:
						System.out.println("Please input either: '1' , '2' , or 'q'  ");
						System.out.println("\n" + "\n");
						break;

					}
				}
				System.out.println("Thanks for using the viewer. Goodbye.");

			}
			// Exception handling
			else if (status == connection.HTTP_BAD_REQUEST) {
				System.out.println("The URL does not exist");
				throw new RuntimeException("HttpResponseCode: " + status);
			} else if (status == connection.HTTP_SERVER_ERROR) {
				System.out.println("error!");
				throw new RuntimeException("HttpResponseCode: " + status);
			} else if (status == connection.HTTP_FORBIDDEN) {
				System.out.println("forbidden URL!");
				throw new RuntimeException("HttpResponseCode: " + status);
			} else if (status == connection.HTTP_UNAVAILABLE) {
				System.out.println("unavailable URL!");
				throw new RuntimeException("HttpResponseCode: " + status);
			}

			bReader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Prints out 25 tickets each request
	public static void requestAll(Ticket ticket) {
		Scanner scan = new Scanner(System.in);
		int input = 0;
		try {
			if (ticket != null) {
				for (Ticket t : ticket.getTickets()) {

					System.out.println(t.toString());

					if (t.getId() % 25 == 0) {
						System.out.println("--------------------------------------");
						System.out.println("\n" + "\n");
						System.out.println("If you want to view next 25 tickets, please type '1' ");
						System.out.println("If you want to return to the main Menu, please type 2");
						System.out.println(" ");
						input = scan.nextInt();

						if (input == 2) {
							break;
						}
						if (input == 1) {

						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Main menu Interface: Grabs user input
	public static String Interface() {

		Scanner kb = new Scanner(System.in);
		String input = "";
		System.out.println("Select view options:");
		System.out.println("* Press 1 to view all tickets");
		System.out.println("* Press 2 to view a ticket");
		System.out.println("* Type 'quit' to quit");
		System.out.print("\n" + "\n");
		input = kb.next();

		return input;
	}

	// Displays one individual ticket via id
	public static void requestOne(Ticket ticket) {

		Scanner scanId = new Scanner(System.in);

		System.out.println("Enter ticket id: ");
		String id = scanId.nextLine();

		try {
			if (ticket != null) {
				for (Ticket t : ticket.getTickets()) {

					if (t.getId() == Integer.parseInt(id)) {
						System.out.println(t.toString());
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
