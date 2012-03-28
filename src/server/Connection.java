package server;

import java.io.IOException;
import java.net.Socket;

/**
 * A generic Connection.
 */

public abstract class Connection {

	private Socket socket;
	private Listener listener;
	private Sender sender;
	private PrinterServer printerServer;
	
	/**
	 * Constructor for making a new connection with a new Listener and Sender
	 * 
	 * @param socket
	 * @param chatserver
	 * @throws IOException
	 */
	public Connection(Socket socket, PrinterServer chatserver)
			throws IOException {
		this.socket = socket;
		this.printerServer = chatserver;
		sender = new Sender(this);
		listener = new Listener(this);
		(new Thread(listener)).start();
	}

	public void send(String command, String[] arguments) {
		sender.send(command, arguments);
	}

	public Socket getSocket() {
		return socket;
	}

	public PrinterServer getPrinterServer() {
		return printerServer;
	}

	protected Listener getListener() {
		return listener;
	}

	protected void setListener(Listener listener) {
		this.listener = listener;
	}

	protected Sender getSender() {
		return sender;
	}

	protected void setSender(Sender sender) {
		this.sender = sender;
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Connection
				&& ((Connection) o).getSocket().equals(socket) 
				&& ((Connection) o).getPrinterServer().equals(printerServer));
	}

	@Override
	public int hashCode() {
		return socket.hashCode() ^ printerServer.hashCode();
	}
	
}
