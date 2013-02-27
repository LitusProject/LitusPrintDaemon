import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class PrintClient {

	private static String serverIP;
	private static int serverPort;
	private static String clientId;
	
	public static void main(String[] args) {
		try {
			serverIP = args[0];
		} catch (Exception e) {
			serverIP = "127.0.0.1";
			System.out.println("ERROR: arg1 is not a valid adress, using default adress: "+serverIP);
		}
		
		try {
			serverPort = Integer.parseInt(args[1]);
		} catch (Exception e) {
			serverPort = 4444;
			System.out.println("ERROR: arg2 is not a valid port number, using default port: "+serverPort);
		}
		
		try {
			clientId = args[2];
		} catch (Exception e) {
			clientId = "DefaultPrinter";
			System.out.println("ERROR: arg3 is not a valid id, using default id: "+clientId);
		}
		
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(serverIP);
		} catch (UnknownHostException e) {
			System.out.println("Given server IP is not valid, exiting ...");
			System.exit(0);
		}
		
		System.out.println("Connecting to server at "+serverIP+" at port "+serverPort+" with id '"+clientId+"'.");
		
		while(true) {
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
