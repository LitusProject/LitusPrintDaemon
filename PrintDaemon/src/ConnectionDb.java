/**
 * Litus is a project by a group of students from the KU Leuven. The goal is to create
 * various applications to support the IT needs of student unions.
 *
 * @author Niels Avonds <niels.avonds@litus.cc>
 * @author Karsten Daemen <karsten.daemen@litus.cc>
 * @author Koen Certyn <koen.certyn@litus.cc>
 * @author Bram Gotink <bram.gotink@litus.cc>
 * @author Dario Incalza <dario.incalza@litus.cc>
 * @author Pieter Maene <pieter.maene@litus.cc>
 * @author Kristof Mariën <kristof.marien@litus.cc>
 * @author Lars Vierbergen <lars.vierbergen@litus.cc>
 * @author Daan Wendelen <daan.wendelen@litus.cc>
 *
 * @license http://litus.cc/LICENSE
 */

import java.util.Date;
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
		System.out.println("[" + (new Date()).toString() + "]: Adding connection with id: " + id);
		connections.put(id, connection);
	}

	public ClientConnection getConnection(String id) {
		System.out.println("[" + (new Date()).toString() + "]: Fetching connection with id: " + id);
		return connections.get(id);
	}

	public void removeConnection(String id) {
		connections.remove(id);
	}

}
