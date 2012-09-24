import java.util.HashMap;


public class ConnectionDb {

	private HashMap<String,ClientConnection> connections = new HashMap<String, ClientConnection>();

	private static ConnectionDb instance;

	private ConnectionDb() {

	}

	public static ConnectionDb getInstance() {
		if (instance == null)
			instance = new ConnectionDb();
		return instance;
	}

	public void addConnection(String id, ClientConnection connection) {
		System.out.println("Adding connection with id: " + id);
		connections.put(id, connection);
	}

	public ClientConnection getConnection(String id) {
		System.out.println("Fetching connection with id: " + id);
		return connections.get(id);
	}

	public void removeConnection(String id) {
		connections.remove(id);
	}

}
