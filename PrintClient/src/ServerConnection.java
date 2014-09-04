/**
 * Litus is a project by a group of students from the KU Leuven. The goal is to create
 * various applications to support the IT needs of student unions.
 *
 * @author Niels Avonds <niels.avonds@litus.cc>
 * @author Karsten Daemen <karsten.daemen@litus.cc>
 * @author Koen Certyn <koen.certyn@litus.cc>
 * @author Bram Gotink <bram.gotink@litus.cc>
 * @author Dario Incalza <dario.incalza@litus.cc>
 * @author Pieter Maene <pieter.maene@litus.cc>
 * @author Kristof Mariën <kristof.marien@litus.cc>
 * @author Lars Vierbergen <lars.vierbergen@litus.cc>
 * @author Daan Wendelen <daan.wendelen@litus.cc>
 *
 * @license http://litus.cc/LICENSE
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.channels.ClosedChannelException;
import java.util.Date;
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
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
			prop.loadFromXML(new FileInputStream("keys.xml"));
			JSONObject keys = JSONObject.fromObject(prop.getProperty("keys"));
			String organization = id.substring(0, id.indexOf("-"));

			map.put("key", keys.getString(organization));

			out.write(JSONSerializer.toJSON(map).toString() + "\n");
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startListening() throws IOException {
		while (true) {
			try {
				String s = in.readLine();

				if (s != null) {
					JSONObject jsonObject = JSONObject.fromObject(s);

					Properties prop = new Properties();
					prop.loadFromXML(new FileInputStream("keys.xml"));
					JSONObject keys = JSONObject.fromObject(prop.getProperty("keys"));

					String id = jsonObject.getString("printer");
					String organization = id.substring(0, id.indexOf("-"));

					if (jsonObject.getString("key") != null && jsonObject.getString("key").equals(keys.getString(organization))) {
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
							System.out.println("[" + (new Date()).toString() + "]");
							System.out.println("> Error: " + e.getMessage());
							System.out.println("> Received string: '" + s + "'");
							System.out.println("> Server does not use the LPS protocol, disconnecting ...");
							e.printStackTrace();
							socket.close();
						}
					} else {
						System.out.println("[" + (new Date()).toString() + "]");
						System.out.println("> Error: wrong authentication key");
						System.out.println("> Received string: '" + s + "'");
						System.out.println("> Server gave wrong authentication key, disconnecting ...");
						socket.close();
					}
				} else {
					throw new ClosedChannelException();
				}
			} catch(ClosedChannelException e) {
				throw e;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
