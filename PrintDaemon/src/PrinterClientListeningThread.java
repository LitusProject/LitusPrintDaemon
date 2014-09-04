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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class PrinterClientListeningThread implements Runnable {

	private final int portNum;
	private ServerSocket serverSocket;

	public PrinterClientListeningThread(int portNum) {
		this.portNum = portNum;
	}

	@Override
	public void run() {
		try {
		    serverSocket = new ServerSocket(portNum);
		} catch (IOException e) {
		    System.out.println("[" + (new Date()).toString() + "]: ERROR: Could not listen on port: " + portNum);
		    e.printStackTrace();
		    System.exit(-1);
		}

		System.out.println("[" + (new Date()).toString() + "]: Listening for incoming printer client connections...");

		while (true) {
			try {
			    Socket clientSocket = serverSocket.accept();
			    System.out.println("[" + (new Date()).toString() + "]: Client connected " + clientSocket.getInetAddress().toString() + ", assigning new thread to socket.");
			    ClientConnection thread = new ClientConnection(clientSocket);
			    (new Thread(thread)).start();
			} catch (IOException e) {
			    System.out.println("[" + (new Date()).toString() + "]: Accepting of printer client failed: " + portNum);
			    e.printStackTrace();
			}
		}
	}
}
