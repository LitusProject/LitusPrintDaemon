import java.util.Date;
import java.util.HashMap;

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


public class QueueNumberPrinter implements OutputCompleteListener, StatusUpdateListener, ErrorListener {

	public static void main(String[] args) {
		HashMap<String,Integer> items = new HashMap<String,Integer>();
		items.put("Fundamentals Of Computer Graphics: 3 Edition",1);
		QueueNumberData data = new QueueNumberData(24, "s0202187", items, 60.35f, "1234560", new Date());
		QueueNumberPrinter printer = new QueueNumberPrinter();
		printer.print(data);
	}

	
	public void print(QueueNumberData data) {
		
		System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME, "jpos.xml");
		
				// constants defined for convenience sake (could be inlined)
		String ESC = ((char) 0x1b) + "";
		String LF = ((char) 0x0a) + "";
		String SPACES = "                                                                      ";

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
			printer.open("Star TSP100 Cutter (TSP143)_1");

			// claim exclusive usage of the printer object
			printer.claim(1);

			// enable the device for input and output
			printer.setDeviceEnabled(true);

			// sets the device in asychrone mode
			printer.setAsyncMode(true);

			// set map mode to metric - all dimensions specified in 1/100mm units
			printer.setMapMode(POSPrinterConst.PTR_MM_METRIC);
			
			// Start met het echte Printen.
			Startprinting: {
				
				// check if the cover is open
				if (printer.getCoverOpen() == true) {
					allert("The printer cover is open");
					
					// cover open so do not attempt printing
					break Startprinting;
				}

				// check if the printer is out of paper
				if (printer.getRecEmpty() == true) {
					allert("The printer is out of paper");

					// the printer is out of paper so do not attempt printing
					break Startprinting;
				}
				
				
				// being a transaction
				// transaction mode causes all output to be buffered
				// once transaction mode is terminated, the buffered data is
				// outputted to the printer in one shot - increased reliability
				printer.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_TRANSACTION);

				if (printer.getCapRecBitmap() == true) {
					// print an image file
					try {
						printer.printBitmap(POSPrinterConst.PTR_S_RECEIPT, "star.gif", POSPrinterConst.PTR_BM_ASIS, POSPrinterConst.PTR_BM_CENTER);
					} catch (JposException e) {
						if (e.getErrorCode() != JposConst.JPOS_E_NOEXIST) {
							// error other than file not exist - propogate it
							throw e;
						}
						// image file not found, this is impossible
						assert(false);
					}
				}
				
				
				
				// call printNormal repeatedly to generate out receipt the following
				// JavaPOS-POSPrinter control code sequences are used here
				// ESC + "|cA" -> center alignment
				// ESC + "|4C" -> double high double wide character printing
				// ESC + "|bC" -> bold character printing
				// ESC + "|uC" -> underline character printing
				// ESC + "|rA" -> right alignment

				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|4C" + ESC + "|bC" + "Litus" + LF);
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|bC" + "VTK, version" + LF);

				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, "   1  830    lol" + LF);
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, "   1  180    Dit werkt bitches ..." + LF);

				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|rA" + "Subtotal:  2160" + LF);
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|rA" + "Tax:         24" + LF);
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|rA" + ESC + "|bC" + "Total:     2184" + LF);
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|rA" + "Tender:    2200" + LF);
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|rA" + ESC + "|bC" + "Change:      16" + LF);
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, LF);


				if (printer.getCapRecBarCode() == true) {
					// print a Code 3 of 9 barcode with the data "123456789012" encoded
					// the 10 * 100, 60 * 100 parameters below specify the barcode's
					// height and width in the metric map mode (1cm tall, 6cm wide)
					printer.printBarCode(POSPrinterConst.PTR_S_RECEIPT, "123456789012", POSPrinterConst.PTR_BCS_Code39,
							10 * 100, 60 * 100, POSPrinterConst.PTR_BC_CENTER, POSPrinterConst.PTR_BC_TEXT_BELOW);
				}

				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|cA" + ESC + "|4C" + ESC + "|bC" + "Thank you" + LF);

				// the ESC + "|100fP" control code causes the printer to execute
				// a paper cut after feeding to the cutter position
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|100fP");

				// terminate the transaction causing all of the above buffered
				// data to be sent to the printer
				printer.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_NORMAL);
				
				// the ESC + "|100fP" control code causes the printer to execute
				// a paper cut after feeding to the cutter position
				printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|100fP");

				// terminate the transaction causing all of the above buffered
				// data to be sent to the printer
				printer.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_NORMAL);
			}
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
		
	}


	public void allert(String alert) {
		// TODO
		System.out.println("ALERT: "+alert);
	}
	
	@Override
	public void errorOccurred(ErrorEvent error) {
		System.err.println("ERROR: ID "+error.getErrorCodeExtended());
	}


	@Override
	public void statusUpdateOccurred(StatusUpdateEvent update) {
		System.out.println("STATUS: ID "+update.getStatus());
	}


	@Override
	public void outputCompleteOccurred(OutputCompleteEvent output) {
		System.out.println("OUTPUT: ID "+output.getOutputID());
	} 
}
