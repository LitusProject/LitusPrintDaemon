package server;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The Sender will take care of all actions required for sending messages.
 */
public class Sender {

    private Connection connection;
    private DataOutputStream out;
	
	/**
     * Create a new Sender that has an empty nickname-socket mapping
     */
    public Sender(Connection connection) throws IOException {
    	this.connection = connection;

	    //*************************
		//TODO: ADD YOUR CODE HERE
		//*************************
	
    }
    
    /**
     * Send a message
     * @param command The command of this message
     * @param arguments	The arguments of this message
     */
    public void send(String command, String ... arguments) {

	    //*************************
		//TODO: ADD YOUR CODE HERE
		//*************************
	
    }
}