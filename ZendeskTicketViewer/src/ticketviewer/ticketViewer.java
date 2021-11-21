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
	public static Ticket ticket;
	public static String username;
	public static String password;
	public static List<Ticket> allTickets;
	public static String credentials;

	private static HttpURLConnection connection;

	// Establishes connection with Zendesk API and runs
	public ticketViewer(String username, String password){
		this.username=username;
		this.password=password;
	}

	public static Ticket connectApi(String credentials){
		Ticket ticket=null;
		try {
			// Url connection to grab tickets
			URL url = new URL("https://zcchal080ucsd.zendesk.com/api/v2/tickets.json/");
			connection = (HttpURLConnection) url.openConnection();
			// Encoding into base64 credentials for header to accept
			String encodeBytes = Base64.getEncoder().encodeToString((credentials).getBytes());

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Basic " + encodeBytes);

			InputStreamReader inputReader = new InputStreamReader(connection.getInputStream());
			BufferedReader bReader = new BufferedReader(inputReader);
			int status = connection.getResponseCode();
			if (status == connection.HTTP_OK) {
				Gson gson = new Gson();
				BufferedReader br = null;
				br = new BufferedReader(inputReader);
				//ticket is a single ticket object
				ticket = gson.fromJson(br, Ticket.class);
			}
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
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ticket;
	}

	public static void route(Ticket ticket){
		if (ticket==null) {
			System.out.println("Cannot connect to the API");
			return;
		}
		String userChoice = "";

		// Main area: Where user chooses options
		while (!userChoice.equalsIgnoreCase("quit")) {

			// Calls for user input in Interface method
			userChoice = Menu();

			switch (userChoice) {
				//view all tickets
				case "1":
					requestAllTickets(ticket);
					break;
				//view a specific ticket
				case "2":
					requestOneTicket(ticket);
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

	public static List<Ticket> getTicketDatabase(Ticket ticket){
		List<Ticket> tickets=null;
		if (ticket==null){
			System.out.println("error, something wrong with the api connect!");
		}
		else{
			tickets=allTickets=ticket.getTickets();
		}
		return tickets;
	}



	// Prints out 25 tickets each request
	public static void requestAllTickets(Ticket ticket) {
		allTickets=getTicketDatabase(ticket);
		if (allTickets==null) return;
		Scanner scan = new Scanner(System.in);
		int input = 0;
		try {
			for (Ticket t : allTickets) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String authenticate(){
		Scanner au = new Scanner(System.in);
		String input1 = "";
		String input2 = "";
		System.out.println("please type your username: ");
		input1 = au.next();
		System.out.println("please type your password: ");
		input2=au.next();
		credentials=input1+":"+input2;
		return credentials;
	}

	//menu page
	public static String Menu() {

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
	//request for the detail of a single ticket
	public static void requestOneTicket(Ticket ticket) {

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

	public static void main(String[] args) {
		credentials = authenticate();
		route(connectApi(credentials));
	}

}
