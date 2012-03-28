package server;

import java.io.IOException;
import java.net.Socket;

/**
 * The ClientConnection is a connection that has been made by a client.
 */
public class ClientConnection extends Connection {

	
	private String nickname;
	private boolean connected;
	
	
	/**
	 * Constructor of ClientConnection that will make a complete new ClientConnection.
	 * @param socket
	 * @param chatServer
	 * @throws IOException
	 */
	public ClientConnection(Socket socket, PrinterServer chatServer) throws IOException {
		super(socket, chatServer);
		connected = false;
	}
	
	
	public String getNickname() {
		return nickname;
	}
	
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
	
	public boolean isConnected() {
	    return connected;
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}
