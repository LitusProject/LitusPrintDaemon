import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


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
			System.out.println("Could not open streams in socket from '"+id+"', closing the socket.");
			System.out.println("--> Cause: "+e.getMessage());
			try {
				socket.close();
			} catch (IOException e2) {
				System.out.println("Could not close socket from '"+id+"'.");
				System.out.println("--> Cause: "+e2.getMessage());
			}
		}
	}

	@Override
	public void run() {
		try {
			String line = in.readLine();
			if (line != null) {

				String[] parts = line.split(" ");
				if (parts.length != 2) {
					System.out.println("Client "+socket.getInetAddress().toString()+"does not use the LPS protocol, disconnecting ...");
					socket.close();
				} else if (!parts[0].equals("CONNECT")) {
					System.out.println("Client "+socket.getInetAddress().toString()+"does not use the LPS protocol, disconnecting ...");
					socket.close();
				}
				id = parts[1];

				ConnectionDb.getInstance().addConnection(id, this);
				System.out.println("Client from "+socket.getInetAddress().toString()+" connected as '"+id+"'.");
			}

		} catch (IOException e) {
			System.out.println("Exception in socket from '"+id+"', closing the connection.");
			System.out.println("--> Cause: "+e.getMessage());
			close();
		}
	}

	public void close() {
		if (id != null) {
			ConnectionDb.getInstance().removeConnection(id);
		}
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Exception while closing connection to '"+id+"'.");
			System.out.println("--> Cause: "+e.getMessage());
		}
	}

	public void send(String command) {
		try {
			out.write(command+"\n");
			out.flush();
		} catch (Exception e) {
			System.out.println("Exception in socket from '"+id+"', closing the connection.");
			System.out.println("--> Cause: "+e.getMessage());
			close();
		}
	}
}
