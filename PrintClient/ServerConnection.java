import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerConnection {

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private String id;

	public ServerConnection(Socket socket, String id) {
		this.id = id;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendGreeting() {
		try {
			out.write("CONNECT "+id+"\n");
			out.flush();
		} catch (Exception e) {

		}

	}

	public void startListening() {

		while (true) {
			try {
				String s = in.readLine();
				if (s != null) {
					String[] parts = s.split(" ");
					if (parts[0].equals("PRINT")) {
						try {
							int type = Integer.parseInt(parts[2]);
							String ticketString = s.substring(parts[0].length()+parts[1].length()+parts[2].length()+3);
							Ticket ticket = Ticket.fromString(ticketString);

							if (type == 1) {
								Printer.getInstance().printWaitingTicket(ticket);
							} else if (type == 2) {
								Printer.getInstance().printBillTicket(ticket);
							}


						} catch (Exception e) {
							System.out.println("Server "+socket.getInetAddress().toString()+"does not use the LPS protocol, disconnecting ...");
							socket.close();
						}
					}
				} else {
					System.out.println("String was null!");
					return;
				}



			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}

	}
}
