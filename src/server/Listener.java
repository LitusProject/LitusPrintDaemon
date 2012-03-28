package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * The Listener will listen for incomming packets and will parse them.
 */
public class Listener implements Runnable {

    private Connection connection;
    private DataInputStream in;
	
	/**
     * Create a new listener for the given socket
     * @param server
     * @throws IOException
     * 		There was an IO problem creating the input stream for this listener
     */
    public Listener(Connection connection) throws IOException{
        this.connection = connection;

	    //*************************
		//TODO: ADD YOUR CODE HERE
		//*************************
	
    }
    
    /**
     * Keep listening for incoming messages from the socket of this Listener and parse the messages.
     */
    public void run() {  

	    //*************************
		//TODO: ADD YOUR CODE HERE
		//*************************
	
    }
    
}

