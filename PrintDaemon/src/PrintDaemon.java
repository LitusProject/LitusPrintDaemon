

public class PrintDaemon {

	public static void main(String[] args) {

		int serverPortNum;
		try {
			serverPortNum = Integer.parseInt(args[1]);
			System.out.println("Starting listening for server connection on port "+serverPortNum);
		} catch (Exception e) {
			serverPortNum = 4445;
			System.out.println("ERROR: arg2 is not a valid port number, using default port: "+serverPortNum);
		}

		int clientPortNum;
		try {
			clientPortNum = Integer.parseInt(args[0]);
			System.out.println("Starting listening for client connections on port "+clientPortNum);
		} catch (Exception e) {
			clientPortNum = 4444;
			System.out.println("ERROR: arg1 is not a valid port number, using default port: "+clientPortNum);
		}

		// Start running the Listener threads
		ServerListeningThread serverListeningThread = new ServerListeningThread(serverPortNum);
		PrinterClientListeningThread printerCLientListeningThread = new PrinterClientListeningThread(clientPortNum);
		(new Thread(serverListeningThread)).start();
		(new Thread(printerCLientListeningThread)).start();

	}

}
