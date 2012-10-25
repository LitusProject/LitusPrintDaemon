import java.util.Arrays;


public class Printer {

	private static Printer instance;

	private Printer() {
	
	}

	public static Printer getInstance() {
		if (instance == null)
			instance = new Printer();
		return instance;
	}

	public void printWaitingTicket(Ticket ticket) {
		WaitingTicketPrinter.getInstance().printTicket(ticket);
	}
	
	public void printBillTicket(Ticket ticket) {
		BillTicketPrinter.getInstance().printTicket(ticket);
	}
	
	public static void main (String[] args) {
		Ticket ticket1 = new Ticket();
		ticket1.setId("s0202187");
		ticket1.setBarcode("1234567890");
		ticket1.setQueuNumber("3");
		ticket1.setTotalAmount("63,00");
		
		String[] items = {"Fundamentals of Computer Graphics","De Bijbel"};
		String[] itemBarcodes = {"12345","67890"};
		String[] prices = {"45,00","8,00"};
		
		ticket1.setItems(Arrays.asList(items));
		ticket1.setItemBarcodes(Arrays.asList(itemBarcodes));
		ticket1.setPrices(Arrays.asList(prices));
		
		System.out.println(ticket1.toJsonString());
		
//		getInstance().printBillTicket(ticket1);
	}
	
}