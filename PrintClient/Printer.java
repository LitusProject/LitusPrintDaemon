
public class Printer {


	public static Printer instance;

	private Printer() {

	}

	public static Printer getInstance() {
		if (instance == null)
			instance = new Printer();
		return instance;
	}


	public void printWaitingTicket(Ticket ticket) {
		System.out.println("Printing Waiting Ticket ...");
	}

	public void printBillTicket(Ticket ticket) {
		System.out.println("Printing Bill Ticket ...");
	}



}
