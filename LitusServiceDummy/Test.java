import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;


public class Test {

	private static PrintWriter out;
	private static BufferedReader in;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String serverIP = "127.0.0.1";
		int serverPort = 4445;



		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(serverIP);
		} catch (UnknownHostException e) {
			System.out.println("Given server IP is not valid, exiting ...");
			System.exit(0);
		}

		System.out.println("Making server connection ...");

		try {
			Socket socket = new Socket(addr,serverPort);

			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			Ticket ticket1 = new Ticket();
			ticket1.setId("s0202187");
			ticket1.setBarcode("1234567890");
			ticket1.setQueuNumber("3");
			ticket1.setTotalAmount("63,00");

			String[] items = {"Fundamentals of Computer Graphics","De Bijbel"};
			String[] prices = {"45,00","8,00"};

			ticket1.setItems(Arrays.asList(items));
			ticket1.setPrices(Arrays.asList(prices));


			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("Sending the print command ...");
			out.write("PRINT WaitingTicketPrinter 2 "+ticket1.toString()+"\n");
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
