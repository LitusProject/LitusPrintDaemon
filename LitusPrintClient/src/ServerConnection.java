import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class ServerConnection {

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private String id;

	public ServerConnection(Socket socket, String id) {
		this.id = id;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendGreeting() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("command", "CONNECT");
			
			Properties prop = new Properties();
			prop.load(new FileInputStream("key.properties"));
			map.put("key", prop.getProperty("key"));

			out.write(JSONSerializer.toJSON(map).toString() + "\n");
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startListening() {
		while (true) {
			try {
				String s = in.readLine();
				if (s != null) {
					Properties prop = new Properties();
					prop.load(new FileInputStream("key.properties"));
					
					JSONObject object = JSONObject.fromObject(s);
					
					if (object.getString("key").equals(prop.getProperty("key"))) {
						try {
							Ticket ticket = Ticket.fromJson(s);

							if (ticket.getType() == 1) {
								Printer.getInstance().printWaitingTicket(ticket);
							} else if (ticket.getType() == 2) {
								Printer.getInstance().printCollectTicket(ticket);
							} else if (ticket.getType() == 3) {
								Printer.getInstance().printBillTicket(ticket);
							}

						} catch (Exception e) {
							System.out.println("> Error: " + e.getMessage());
							System.out.println("> Received string: '"+s+"'");
							System.out.println("> Server does not use the LPS protocol, disconnecting ...");
							e.printStackTrace();
							socket.close();
						}
					} else {
						System.out.println("> Error: wrong authentication key");
						System.out.println("> Received string: '"+s+"'");
						System.out.println("> Server gave wrong authentication key, disconnecting ...");
						socket.close();
					}
				}
			}

			catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
