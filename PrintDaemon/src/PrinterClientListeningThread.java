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
		}
		catch (IOException e) {
		    System.out.println("["+(new Date()).toString()+"]: ERROR: Could not listen on port: "+portNum);
		    e.printStackTrace();
		    System.exit(-1);
		}

		System.out.println("["+(new Date()).toString()+"]: Listening for incoming printer client connections ...");

		while (true) {
			try {
			    Socket clientSocket = serverSocket.accept();
			    System.out.println("["+(new Date()).toString()+"]: Client connected "+clientSocket.getInetAddress().toString()+", assigning new thread to socket.");
			    ClientConnection thread = new ClientConnection(clientSocket);
			    (new Thread(thread)).start();
			}
			catch (IOException e) {
			    System.out.println("["+(new Date()).toString()+"]: Accepting of printer client failed: "+portNum);
			    e.printStackTrace();
			}
		}
	}
}
