package ticketviewer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.Test;

class ticketTest {

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


	@org.junit.jupiter.api.Test
	void testRequestAll() {
		ticketViewer test = new ticketViewer();
		Ticket ticket = new Ticket();
		List<Ticket> tickets = new ArrayList<Ticket>();
		ticket.setId(1);
		ticket.setSubject("Subject");
		ticket.setStatus("Open");

		tickets.add(ticket);
		assertEquals(ticket.getSubject(), "Subject");
		assertEquals(ticket.getId(), 1);
		assertEquals(ticket.getStatus(), "Open");
	}

}

