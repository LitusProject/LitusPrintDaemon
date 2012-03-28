package shared;
/**
 * The ProtocolDB class contains all the commands that can been exchanged between the clients and servers.
 */
public final class ProtocolDB {
	
	// With the CLIENTCONNECT command, the client asks the server for a connection. The client will add his nickname.
	public static final String CLIENTCONNECT_COMMAND = "CLIENTCONNECT";
	// With the ACCEPTED command tells a server to a client that the connect has succeeded.
	public static final String ACCEPTED_COMMAND = "ACCEPTED";
	// With the REJECTED command tells a server to a client that the connect is failed.
	public static final String REJECTED_COMMAND = "REJECTED";
	// With the CLIENTDISCONNECT command, the client tells the server he is disconnecting.
	public static final String CLIENTDISCONNECT_COMMAND = "CLIENTDISCONNECT";

	// A CLIENTMESSAGE is a textmessage that has been sent by the client.
	public static final String CLIENTPRINT_COMMAND = "CLIENTPRINT";
	// A SERVERMESSAGE is the forwarding of a clientmessage by the server, either to other clients or to the other servers. This message will contain the nickname of the sender.
	public static final String SERVERMESSAGE_COMMAND = "SERVERMESSAGE";
	
	// The COMMAND_DELIMITER is used for separating the different fields of the network messages.
	public static final String COMMAND_DELIMITER = "#";
}
