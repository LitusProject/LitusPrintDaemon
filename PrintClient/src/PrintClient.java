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

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;


public class PrintClient {

	private static String serverIP;
	private static int serverPort;
	private static String clientId;

	public static void main(String[] args) {
		try {
			serverIP = args[0];
		} catch (Exception e) {
			serverIP = "127.0.0.1";
			System.out.println("[" + (new Date()).toString() + "]: ERROR: arg1 is not a valid adress, using default adress: " + serverIP);
		}

		try {
			serverPort = Integer.parseInt(args[1]);
		} catch (Exception e) {
			serverPort = 4444;
			System.out.println("[" + (new Date()).toString() + "]: ERROR: arg2 is not a valid port number, using default port: " + serverPort);
		}

		try {
			clientId = args[2];
		} catch (Exception e) {
			clientId = "LITUS-DefaultPrinter";
			System.out.println("[" + (new Date()).toString() + "]: ERROR: arg3 is not a valid id, using default id: " + clientId);
		}

		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(serverIP);
		} catch (UnknownHostException e) {
			System.out.println("[" + (new Date()).toString() + "]: Given server IP is not valid, exiting...");
			System.exit(0);
		}

		while(true) {
			System.out.println("[" + (new Date()).toString() + "]: Connecting to server at " + serverIP + " at port " + serverPort + " with id '" + clientId + "'.");

			try {
				Socket socket = new Socket(addr,serverPort);
				ServerConnection connection = new ServerConnection(socket, clientId);
				connection.sendGreeting();
				connection.startListening();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {}
			}
		}
	}
}
