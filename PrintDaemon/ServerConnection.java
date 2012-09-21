import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


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
			System.out.println("Could not open streams in server socket connection, closing the socket.");
			System.out.println("--> Cause: "+e.getMessage());
			try {
				socket.close();
			} catch (IOException e2) {
				System.out.println("Could not close server socket connection.");
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
					String[] parts = s.split(" ");
					if (parts.length < 2) {
						System.out.println("Server "+socket.getInetAddress().toString()+"does not use the LPS protocol, disconnecting ...");
						socket.close();
					} else if (!parts[0].equals("PRINT")) {
						System.out.println("Server "+socket.getInetAddress().toString()+"does not use the LPS protocol, disconnecting ...");
						socket.close();
					} else {
						String id = parts[1];
						System.out.println("Looking for id: " + id);
						System.out.println("Looking for id: " + id);
						ClientConnection connection  = ConnectionDb.getInstance().getConnection(id);
						connection.send(s);
					}
				} else {
					return;
				}
			} catch (IOException e) {
				System.out.println("Exception in server socket, closing the connection.");
				System.out.println("--> Cause: "+e.getMessage());
				close();
			}
		}
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Exception while closing connection to server.");
			System.out.println("--> Cause: "+e.getMessage());
		}
	}


}
