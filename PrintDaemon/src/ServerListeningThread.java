import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerListeningThread implements Runnable {

	private final int portNum;
	private ServerSocket serverSocket;

	public ServerListeningThread(int portNum) {
		this.portNum = portNum;

	}

	@Override
	public void run() {

		try {
		    serverSocket = new ServerSocket(portNum);
		}
		catch (IOException e) {
		    System.out.println("ERROR: Could not listen on port: "+portNum);
		    e.printStackTrace();
		    System.exit(-1);
		}

		System.out.println("Listening for incoming server connection ...");

		while (true) {

			try {
			    Socket clientSocket = serverSocket.accept();
			    System.out.println("Server connected "+clientSocket.getInetAddress().toString()+", assigning new thread to socket ...");
			    ServerConnection thread = new ServerConnection(clientSocket);
			    (new Thread(thread)).start();
			}
			catch (IOException e) {
			    System.out.println("Accepting server socket failed: "+portNum);
			    e.printStackTrace();
			}
		}

	}


}
