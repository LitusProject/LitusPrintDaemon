package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

import shared.ProtocolDB;

public class PrinterServer {

	/*************
	 * VARIABLES *
	 *************/

	// Two maps are constructed, linking the names to the connection and vice
	// versa. We do this so we can search quickly on both name and connection,
	// this increase of responsiveness is at the expense of more memory. 
	private HashMap<String, ClientConnection> mapNamesToClientConnections;
	private HashMap<ClientConnection, String> mapClientConnectionsToNames;

	/***************
	 * CONSTRUCTOR *
	 ***************/

	public PrinterServer() {
		mapNamesToClientConnections = new HashMap<String, ClientConnection>();
		mapClientConnectionsToNames = new HashMap<ClientConnection, String>();

		(new Thread(new ClientListenThread(this))).start();
	}

	/******************
	 * LISTEN THREADS *
	 ******************/

	private class ClientListenThread implements Runnable {

		private PrinterServer printerServer;

		public ClientListenThread(PrinterServer printerserver) {
			this.printerServer = printerserver;
		}

		public void run() {

			while (true) {
				// *************************
				// TODO: ADD YOUR CODE HERE
				// *************************
			}

		}

	}

	/********************
	 * PROTOCOL METHODS *
	 ********************/

	/**
	 * Connect a client to the chat network.
	 * 
	 * @param connection
	 * @param nickname
	 */
	public void clientConnect(ClientConnection clientConnection, String name) {

		mapNamesToClientConnections.put(name, clientConnection);
		mapClientConnectionsToNames.put(clientConnection, name);
		
	}

	/**
	 * removes a client connection
	 * 
	 * @param clientConnection
	 */
	public void clientDisconnect(ClientConnection clientConnection) {
		
		String name = mapClientConnectionsToNames.get(clientConnection);
		
		mapNamesToClientConnections.remove(name);
		mapClientConnectionsToNames.remove(clientConnection);
		
	}

	/**
	 * 
	 * 
	 * @param clientConnection
	 * @param messageText
	 */
	public void clientMessage(String name, String messageText) {

		ClientConnection connection = mapNamesToClientConnections.get(name);
		connection.getSender().send(ProtocolDB.CLIENTPRINT_COMMAND, messageText);
	}
}