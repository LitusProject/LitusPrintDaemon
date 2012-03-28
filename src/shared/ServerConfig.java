package shared;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A class that reads a configuration file "servers.xml" and stores the specified servers
 * and port numbers. This class can also return the first free local port that was specified
 * in the config file.
 */
public class ServerConfig {

	/**
	 * Get the first free local server port.
	 * @throws IllegalStateException
	 * 		All server ports specified in the config file are taken
	 */
	public static int getLocalServerPort() {
		initialize();
		if(localserverindex > -1)
			return (Integer.valueOf((String) serverPorts.get(localserverindex))).intValue();
		else
			return -1;
	}

	/**
	 * Get the first free local client port.
	 * @throws IllegalStateException
	 * 		All server ports specified in the config file are taken
	 */
	public static int getLocalClientPort() throws IllegalStateException {
		initialize();
		if(localserverindex > -1)
			return (Integer.valueOf((String) clientPorts.get(localserverindex))).intValue();
		else
			return -1;
	}

	/**
	 * Get the i-th server.
	 * @param i
	 * @return
	 */
	public static String getRemoteServer(int i) {
		initialize();
		if (i < localserverindex)
			return (String) hosts.get(i);
		else
			return (String) hosts.get(i + 1);
	}

	/**
	 * Get the port of the i-th server
	 * @param i
	 * @return
	 */
	public static int getRemoteServerPort(int i) {
		initialize();
		if (i < localserverindex)
			return (Integer.valueOf((String) serverPorts.get(i))).intValue();
		else
			return (Integer.valueOf((String) serverPorts.get(i + 1))).intValue();
	}
	
	/**
	 * Get the port of the i-th server
	 * @param i
	 * @return
	 */
	public static int getRemoteClientPort(int i) {
		initialize();
		if (i < localserverindex)
			return (Integer.valueOf((String) clientPorts.get(i))).intValue();
		else
			return (Integer.valueOf((String) clientPorts.get(i + 1))).intValue();
	}

	/**
	 * Get the number of other servers
	 * @return
	 */
	public static int getNbOtherServers() {
		initialize();
		return hosts.size() - 1;
	}

	/**
	 * Get the total number of servers
	 * @return
	 */
	public static int getNbServers() {
		initialize();
		return hosts.size();
	}

	/**
	 * Parse the servers.xml config file.
	 * The servers will be stored in the vector hosts, the ports will be stored in the vector ports. 
	 * The port corresponding to a host will be found in the same position in both vectors, for example 
	 * the port of the second host is the second element of the ports vector.
	 */
	private static void parseConfig() {
		try {
			/** A "Document" an entire XML document. 
			 * Conceptually, it is the root of the document tree, and provides 
			 * the primary access to the document's data. */
			Document servers;
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
			.newDocumentBuilder();
			servers = builder.parse(new File(CONFIG_FILE));
			Element root = servers.getDocumentElement();
			NodeList nodes = root.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeName().equals("server")) {
					Node host = null, serverport = null, clientport = null;
					for (int j = 0; j < node.getChildNodes().getLength(); j++) {
						if (node.getChildNodes().item(j).getNodeName().equals(
						"host"))
							host = node.getChildNodes().item(j);
						else if (node.getChildNodes().item(j).getNodeName()
								.equals("serverport"))
							serverport = node.getChildNodes().item(j);
						else if (node.getChildNodes().item(j).getNodeName()
								.equals("clientport"))
							clientport = node.getChildNodes().item(j);
					}
					hosts.add(host.getFirstChild().getNodeValue());
					clientPorts.add(clientport.getFirstChild().getNodeValue());
					serverPorts.add(serverport.getFirstChild().getNodeValue());
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize local variables.
	 */
	private static synchronized void initialize() {
		if (hosts == null || serverPorts == null || clientPorts == null) {
			hosts =  new Vector<String>();
			serverPorts = new Vector<String>();
			clientPorts = new Vector<String>();
		}
		if (localserverindex == -1) {
			parseConfig();
			localserverindex = getUnusedLocalhostServerConfigurationIndex();
		}
	}

	/**
	 * Returns an unused configuration index for the localhost server.
	 */
	private static int getUnusedLocalhostServerConfigurationIndex(){
		int serverConfigurationIndex = -1;

		boolean inuse = true;
		ServerSocket testsocket;
		Iterator iter = hosts.iterator();
		for (int i = 0; iter.hasNext() && inuse; i++) {
			String element = (String) iter.next();
			if (element.equals("localhost")) {
				try {
					int localPort = new Integer(serverPorts.elementAt(i));
					testsocket = new ServerSocket(localPort);
					inuse = false;
					testsocket.close();
					serverConfigurationIndex = i;
				} catch (IOException e) {
					// Tested port was not available
				}
			}
		}
		
		if(serverConfigurationIndex == -1){
				throw new IllegalStateException(
				"All local server ports are already used");
		}
		return serverConfigurationIndex;
	}
	
	
	private static void log(String s) {
		System.out.println("ServerConfig: " + s);
	}

	private static Vector<String> hosts;
	private static Vector<String> serverPorts;
	private static Vector<String> clientPorts;
	private static int localserverindex = -1;
	private static final String CONFIG_FILE = "servers.xml";
}