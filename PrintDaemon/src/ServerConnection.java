import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Properties;

import net.sf.json.JSONObject;


public class ServerConnection implements Runnable {

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private boolean running = true;

	public ServerConnection (Socket socket) {
		if (socket == null)
			throw new IllegalArgumentException("Socket may not be null!");
		this.socket = socket;

		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("["+(new Date()).toString()+"]: Could not open streams in server socket connection, closing the socket.");
			System.out.println("--> Cause: "+e.getMessage());
			try {
				socket.close();
			} catch (IOException e2) {
				System.out.println("["+(new Date()).toString()+"]: Could not close server socket connection.");
				System.out.println("--> Cause: "+e2.getMessage());
			}
		}
	}

	@Override
	public void run() {
		while (running) {
			try {
				String s = in.readLine();

				if (s != null) {
					JSONObject jsonObject = JSONObject.fromObject(s);
					String command = jsonObject.getString("command");

					Properties prop = new Properties();
					prop.loadFromXML(new FileInputStream("keys.xml"));
					JSONObject keys = JSONObject.fromObject(prop.getProperty("keys"));
					
					String id = jsonObject.getString("id");
					String organization = id.substring(0, id.indexOf("-"));
					
					if (jsonObject.getString("key") != null && jsonObject.getString("key").equals(keys.getString(organization))) {
						if (command != null && command.equals("PRINT")) {
							JSONObject object = jsonObject.getJSONObject("ticket");
							
							object.put("key", prop.getProperty("key"));
							
							System.out.println("["+(new Date()).toString()+"]: Looking for id: " + id);
							ClientConnection connection  = ConnectionDb.getInstance().getConnection(id);
							if (connection != null) {
								System.out.println("["+(new Date()).toString()+"]: Data send to: " + id);
								connection.send(object.toString());
							} else {
								System.out.println("["+(new Date()).toString()+"]: Not found: " + id);
							}
						}
					} else {
						System.out.println("["+(new Date()).toString()+"]: Print command send with wrong key, disconnecting...");
						close();
					}
				} else {
					return;
				}
			} catch (IOException e) {
				System.out.println("["+(new Date()).toString()+"]: Exception in server socket, closing the connection.");
				System.out.println("--> Cause: "+e.getMessage());
				close();
			}
		}
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("["+(new Date()).toString()+"]: Exception while closing connection to server.");
			System.out.println("--> Cause: "+e.getMessage());
		}
	}
}
