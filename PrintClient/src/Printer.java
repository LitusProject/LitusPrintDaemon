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

	public void printCollectTicket(Ticket ticket) {
		CollectTicketPrinter.getInstance().printTicket(ticket);
	}
	
	public void printMembershipCard(MembershipCard card) {
		MembershipCardPrinter.getInstance().printCard(card);
	}

	public static void main (String[] args) {
		Ticket ticket1 = new Ticket();
		ticket1.setId("s0202187");
		ticket1.setBarcode("1234567890");
		ticket1.setQueueNumber("3");
		ticket1.setTotalAmount("63,00");
		ticket1.setType(2);

		TicketItem item1 = new TicketItem();
		item1.setTitle("Fundamentals of Computer Graphics");
		item1.setBarcode("12345");
		item1.setPrice("45,00");
		item1.setNumber(1);
		ticket1.addItem(item1);

		TicketItem item2 = new TicketItem();
		item2.setTitle("De Bijbel");
		item2.setBarcode("67890");
		item2.setPrice("8,00");
		item2.setNumber(1);
		ticket1.addItem(item2);

		System.out.println(ticket1.toJsonString());

		getInstance().printWaitingTicket(ticket1);
	}
}