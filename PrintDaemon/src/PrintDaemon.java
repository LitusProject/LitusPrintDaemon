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

import java.util.Date;


public class PrintDaemon {

	public static void main(String[] args) {
		int serverPortNum;
		try {
			serverPortNum = Integer.parseInt(args[1]);
			System.out.println("[" + (new Date()).toString() + "]: Starting listening for server connection on port " + serverPortNum);
		} catch (Exception e) {
			serverPortNum = 4445;
			System.out.println("[" + (new Date()).toString() + "]: ERROR: arg2 is not a valid port number, using default port: " + serverPortNum);
		}

		int clientPortNum;
		try {
			clientPortNum = Integer.parseInt(args[0]);
			System.out.println("[" + (new Date()).toString() + "]: Starting listening for client connections on port " + clientPortNum);
		} catch (Exception e) {
			clientPortNum = 4444;
			System.out.println("[" + (new Date()).toString() + "]: ERROR: arg1 is not a valid port number, using default port: " + clientPortNum);
		}

		// Start running the Listener threads
		ServerListeningThread serverListeningThread = new ServerListeningThread(serverPortNum);
		PrinterClientListeningThread printerCLientListeningThread = new PrinterClientListeningThread(clientPortNum);
		(new Thread(serverListeningThread)).start();
		(new Thread(printerCLientListeningThread)).start();
	}
}
