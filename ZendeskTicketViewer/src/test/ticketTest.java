package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.Test;
import ticketviewer.Ticket;
import ticketviewer.ticketViewer;

class ticketTest {

	//handle the API being unavailable and response invalid
	@org.junit.jupiter.api.Test
	void testApiConnect() throws IOException {
		HttpURLConnection connect = null;
		Ticket ticket = new Ticket();
		URL url = null;
		// create connection to test
		try {
			url = new URL("https://zcchal080ucsd.zendesk.com/api/v2/tickets.json/");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connect = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String credentials = ("hal080@ucsd.edu" + ":" + "password");

		String encodeBytes = Base64.getEncoder().encodeToString((credentials).getBytes());

		connect.setRequestMethod("GET");

		connect.setRequestProperty("Authorization", "Basic " + encodeBytes);

		connect.setRequestMethod("GET");

		connect.setRequestProperty("Basic", "application/json");

		int status = connect.getResponseCode();

		assertEquals(200, status);
	}


	//test if the request for all the tickets in account is successful
	@org.junit.jupiter.api.Test
	void testLoadAllTickets() {
		String user={username};//please replace {username} with my zendesk username here
		String psw={password};//please replace {password} with my zendesk password here
		String credientials=user+ ":"+psw;
		ticketViewer test = new ticketViewer(user,psw);
		Ticket ticket=test.connectApi(credientials);
		List<Ticket> tickets=test.getTicketDatabase(ticket);
		assertEquals(tickets.size(),100);
	}

}

