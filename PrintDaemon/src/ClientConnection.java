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
import java.util.Date;
import java.util.Properties;
import net.sf.json.JSONObject;


public class ClientConnection implements Runnable {

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private String id;

	public ClientConnection(Socket socket) {
		if (socket == null)
			throw new IllegalArgumentException("Socket may not be null!");
		this.socket = socket;

		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("[" + (new Date()).toString() + "]: Could not open streams in socket from '" + id + "', closing the socket.");
			System.out.println("--> Cause: " + e.getMessage());
			try {
				socket.close();
			} catch (IOException e2) {
				System.out.println("[" + (new Date()).toString() + "]: Could not close socket from '" + id + "'.");
				System.out.println("--> Cause: " + e2.getMessage());
			}
		}
	}

	@Override
	public void run() {
		try {
			String line = in.readLine();

			if (line != null) {
				Properties prop = new Properties();
				prop.loadFromXML(new FileInputStream("keys.xml"));
				JSONObject keys = JSONObject.fromObject(prop.getProperty("keys"));

				JSONObject jsonObject = JSONObject.fromObject(line);
				String command = jsonObject.getString("command");
				id = jsonObject.getString("id");

				if (command != null && id != null && command.equals("CONNECT") && id.indexOf("-") > 0) {
					String organization = id.substring(0, id.indexOf("-"));

					if (jsonObject.getString("key").equals(keys.getString(organization))) {
						ConnectionDb.getInstance().addConnection(id, this);
						System.out.println("[" + (new Date()).toString() + "]: Client from " + socket.getInetAddress().toString() + " connected as '" + id + "'.");
					} else {
						System.out.println("[" + (new Date()).toString() + "]: Client from " + socket.getInetAddress().toString() + " tried to connect as '" + id + "' with wrong key, disconnecting...");
						socket.close();
					}
				} else {
					System.out.println("[" + (new Date()).toString() + "]: Client " + socket.getInetAddress().toString() + " does not use the LPS protocol, disconnecting...");
					socket.close();
				}
			}

		} catch (IOException e) {
			System.out.println("[" + (new Date()).toString() + "]: Exception in socket from '" + id + "', closing the connection.");
			System.out.println("--> Cause: " + e.getMessage());
			close();
		}
	}

	public void close() {
		if (id != null)
			ConnectionDb.getInstance().removeConnection(id);

		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("[" + (new Date()).toString() + "]: Exception while closing connection to '" + id + "'.");
			System.out.println("--> Cause: "+e.getMessage());
		}
	}

	public void send(String command) {
		try {
			out.write(command+"\n");
			out.flush();
		} catch (Exception e) {
			System.out.println("[" + (new Date()).toString() + "]: Exception in socket from '" + id + "', closing the connection.");
			System.out.println("--> Cause: " + e.getMessage());
			close();
		}
	}
}
