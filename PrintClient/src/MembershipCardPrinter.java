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
import java.util.Map;

import com.zebra.sdk.printer.discovery.DiscoveredUsbPrinter;
import com.zebra.sdk.printer.discovery.UsbDiscoverer;


public class MembershipCardPrinter {

	private static MembershipCardPrinter instance;

	public static MembershipCardPrinter getInstance() {
		if (instance == null)
			instance = new MembershipCardPrinter();

		return instance;
	}
	
	public void printCard(MembershipCard card) {
		DiscoveredUsbPrinter printer = null;
		//Discover all USB printers
		try {
			for (DiscoveredUsbPrinter discoPrinter : UsbDiscoverer.getZebraUsbPrinters()) {
				if(discoPrinter.getDiscoveryDataMap().get("MODEL").contains("ZXP")) {
					printer = discoPrinter;
				}
			}
		} catch(Exception e) {
			System.out.println("[" + (new Date()).toString() + "]: Unable to discover printers : " + e.getLocalizedMessage());
		}
		
		if(printer == null) {
			System.out.println("[" + (new Date()).toString() + "]: No suitable printer was detected via USB.");
			return;
		}
		
		try {
			TemplateModel templateModel = new TemplateModel("card.xml");
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("barcode", card.getId());
			map.put("full_name", card.getFullName());
			map.put("academic_year", card.getComment());
			
			templateModel.print(printer, map);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
}