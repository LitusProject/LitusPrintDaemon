import jpos.JposConst;
import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;

import jpos.events.ErrorEvent;
import jpos.events.ErrorListener;
import jpos.events.OutputCompleteEvent;
import jpos.events.OutputCompleteListener;
import jpos.events.StatusUpdateEvent;
import jpos.events.StatusUpdateListener;
import jpos.util.JposPropertiesConst;

public class WaitingTicketPrinter implements OutputCompleteListener, StatusUpdateListener, ErrorListener {

	
	private static WaitingTicketPrinter instance;
	
	public static WaitingTicketPrinter getInstance() {
		if (instance == null) {
			instance = new WaitingTicketPrinter();
		}
		return instance;
	}
	
	public void outputCompleteOccurred(OutputCompleteEvent event) {
		System.out.println("OutputCompleteEvent received: time = "
				+ System.currentTimeMillis() + " output id = "
				+ event.getOutputID());
	}

	public void statusUpdateOccurred(StatusUpdateEvent event) {
		System.out.println("StatusUpdateEvent : status id = " + event.getStatus());
	}

	public void errorOccurred(ErrorEvent event) {
		System.out.println("ErrorEvent received: time = "
				+ System.currentTimeMillis() + " error code = "
				+ event.getErrorCode() + " error code extended = "
				+ event.getErrorCodeExtended());

		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}

		event.setErrorResponse(JposConst.JPOS_ER_RETRY);
	}

	public void printTicket(Ticket ticket) {
		/*
		 * If you want to place the jpos.xml file elsewhere on your local file
		 * system then uncomment the following line and specify the full path to
		 * jpos.xml.
		 * 
		 * If you want to place the jpos.xml file on a webserver for access over
		 * the internet then uncomment the second System.setProperty line below
		 * and specify the full URL to jpos.xml.
		 */
		System.setProperty(	JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME, "jpos.xml");
		// System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_URL_PROP_NAME, "http://some-where-remote.com/jpos.xml");

		// constants defined for convenience sake (could be inlined)
		String ESC = ((char) 0x1b) + "";
		String LF = ((char) 0x0a) + "";
		String EURO =  "";

		// instantiate a new jpos.POSPrinter object
		POSPrinter printer = new POSPrinter();

		try {
			// register for asynchronous OutputCompleteEvent notification
			printer.addOutputCompleteListener(this);

			// register for asynchronous StatusUpdateEvent notification
			printer.addStatusUpdateListener(this);

			// register for asynchronous ErrorEvent notification
			printer.addErrorListener(this);

			// open the printer object according to the entry names defined in jpos.xml
			// printer.open("default");
			printer.open("Star TSP100 Cutter (TSP143)_1");

			// claim exclusive usage of the printer object
			printer.claim(1);

			// enable the device for input and output
			printer.setDeviceEnabled(true);

			printer.setAsyncMode(true);

			// set map mode to metric - all dimensions specified in 1/100mm units
			printer.setMapMode(POSPrinterConst.PTR_MM_METRIC); // unit = 1/100 mm - i.e. 1 cm = 10 mm = 10 * 100 units

			do {
				// register for asynchronous StatusUpdateEvent notification
				// see the JavaPOS specification for details on this

				// printer.checkHealth(JposConst.JPOS_CH_EXTERNAL);
//				printer.checkHealth(JposConst.JPOS_CH_INTERACTIVE);

				// check if the cover is open
				if (printer.getCoverOpen() == true) {
					System.out.println("printer.getCoverOpen() == true");

					// cover open so do not attempt printing
					break;
				}

				// check if the printer is out of paper
				if (printer.getRecEmpty() == true) {
					System.out.println("printer.getRecEmpty() == true");

					// the printer is out of paper so do not attempt printing
					break;
				}

				// being a transaction
				// transaction mode causes all output to be buffered
				// once transaction mode is terminated, the buffered data is
				// outputted to the printer in one shot - increased reliability
				printer.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_TRANSACTION);

				if (printer.getCapRecBitmap() == true) {
					// print an image file
					try {
						printer.printBitmap(POSPrinterConst.PTR_S_RECEIPT, "vtk.gif", POSPrinterConst.PTR_BM_ASIS, POSPrinterConst.PTR_BM_CENTER);
					} catch (JposException e) {
						if (e.getErrorCode() != JposConst.JPOS_E_NOEXIST) {
							// error other than file not exist - propogate it
							throw e;
						}
						// image file not found - ignore this error & proceed
					}
				}

				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|4C" + ESC + "|bC" + "VTK Cursusdienst" + LF);
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + ticket.getId() + LF);
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + ticket.getName() + LF);
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "Nummer in Wachtrij: "+ticket.getQueueNumber() + LF);
				
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT,"\n");
				for (int i = 0;i < ticket.getItems().size();i++) {
					
					String price = ticket.getPrices().get(i);
					int spacesToAdd = 6-price.length();
					for (int j=0;j<spacesToAdd;j++) {
						price+=" ";
					}
					String itemName = ticket.getItems().get(i);
					if (itemName.length() > 31) {
						itemName = itemName.substring(0, 31);
						itemName += "...";
					}
					
					printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, " "+EURO+" "+price+" "+itemName + LF);
					
				}
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT,"\n");
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|rA" + ESC + "|bC" + "Totaal:  "+EURO+" "+ticket.getTotalAmount() + LF);

				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT,"\n");
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT,"\n");		
				
				// the ESC + "|100fP" control code causes the printer to execute
				// a paper cut after feeding to the cutter position
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|100fP");

				// terminate the transaction causing all of the above buffered
				// data to be sent to the printer
				printer.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_NORMAL);

				System.out.println("Async transaction print submited: time = "
						+ System.currentTimeMillis() + " output id = " + printer.getOutputID());

				// exit our printing loop
			} while (false);
		} catch (JposException e) {
			// display any errors that come up
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// close the printer object
			if (printer.getState() != JposConst.JPOS_S_CLOSED) {
				try {
					while (printer.getState() != JposConst.JPOS_S_IDLE) {
						Thread.sleep(0);
					}

					printer.close();
				} catch (Exception e) {
				}
			}
		}

		System.out.println("Waiting ticket printed");
	}
}