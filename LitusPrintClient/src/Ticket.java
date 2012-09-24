import java.util.Arrays;
import java.util.List;


public class Ticket {

	/**
	 * The data fields MAY NOT contain the DELIMETER char. This is the only restriction! 
	 */
	
	public static final String DELIMETER ="#";
	
	/**
	 * KOPER ID (s-nummer, ...)
	 */
	private String id;
	
	/**
	 * BARCODE ID
	 */
	private String barcode;
	
	/**
	 * WACHTRIJNUMMER
	 */
	private String queuNumber;
	
	/**
	 * TOTALE PRIJS AANKOOP
	 */
	private String totalAmount;
	
	/**
	 * LIJST VAN AFZONDERLIJK AANGEKOCHTE ITEMS
	 */
	private List<String> items;
	
	/**
	 * PRIJSLIJST VAN DE AFZONDERLIJK AANGEKOCHTE ITEMS
	 */
	private List<String> prices;
	
	
	
	
	/**
	 * Represents all the information of this object in a String.
	 */
	public String toString() {
		
		
		// ALLE VELDEN VAN DIT OBJECT WORDEN IN ÉÉN STRING AAN ELKAAR GEZET,
		// GESCHEIDEN DOOR TWEE KEER DELIMETER CHAR (#). DE VELDEN STAAN IN 
		// VOLGENDE VOLGORDE:
		// 			1. KOPER ID
		//			2. BARCODE ID
		//			3. WACHTRIJNUMMER
		//			4. TOTALE PRIJS AANKOOP
		//			5. LIJST VAN AFZONDERLIJK AANGEKOCHTE ITEMS
		// 			6. PRIJSLIJST VAN DE AFZONDERLIJK AANGEKOCHTE ITEMS	
		//
		// ALS EEN VELD EEN LIJST IS, WORDEN DE AFZONDERLIJKE WAARDEN GESCHEIDEN
		// DOOR ÉÉN KEER HET DELIMETER CHAR (#).
		
		
		
		// We beginnen dus met de enkelle velden al aan elkaar te koppellen m.b.v.
		// het dubbelle DELIMETER char.
		String s = 	id+DELIMETER+DELIMETER+
					barcode+DELIMETER+DELIMETER+
					queuNumber+DELIMETER+DELIMETER+
					totalAmount;
		
		
		
		// We maken van de lijsten strings door hun items aan elkaar te koppellen
		// m.b.v. het enkelle DELIMETER char.
		String itemString = "";
		String priceString = "";
		for (String item: items) {
			itemString+=DELIMETER+item;
		}
		for (String price: prices) {
			priceString+=DELIMETER+price;
		}
		
		// We plakken nu ook deze velden achter de andere velden met een dubbel
		// DELIMETER char (merk op dat de lijsten starten met de DELIMETER char,
		// hierdoor worden dat dubbelle chars als ze aan elkaar worden geplakt).
		return s + DELIMETER + itemString +DELIMETER+ priceString;
	}
	
	/**
	 * Make an object from the String.
	 */
	public static Ticket fromString(String string) {
		
		// Splits eerst alles op per veld.
		String[] fields = string.split(DELIMETER+DELIMETER);
		
		try {
			
			Ticket job = new Ticket();
			job.setId(fields[0]);
			job.setBarcode(fields[1]);
			job.setQueuNumber(fields[2]);
			job.setTotalAmount(fields[3]);
			
			// Splits de velden die lijsten zijn vervolgens op
			// in hun afzonderlijke waarden.
			String[] items = fields[4].split(DELIMETER);
			String[] prices = fields[5].split(DELIMETER);
			job.setItems(Arrays.asList(items));
			job.setPrices(Arrays.asList(prices));
			return job;
			
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		
		if (id.contains(DELIMETER))
			throw new IllegalArgumentException("id may not contain the delimeter char: \""+DELIMETER+"\"");
		
		this.id = id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		
		if (barcode.contains(DELIMETER))
			throw new IllegalArgumentException("barcode may not contain the delimeter char: \""+DELIMETER+"\"");
		
		this.barcode = barcode;
	}

	public String getQueuNumber() {
		return queuNumber;
	}

	public void setQueuNumber(String queuNumber) {

		if (queuNumber.contains(DELIMETER))
			throw new IllegalArgumentException("queuNumber may not contain the delimeter char: \""+DELIMETER+"\"");
		
		this.queuNumber = queuNumber;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		
		if (totalAmount.contains(DELIMETER))
			throw new IllegalArgumentException("totalAmount may not contain the delimeter char: \""+DELIMETER+"\"");
		
		this.totalAmount = totalAmount;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		
		for (String item: items)
			if (item.contains(DELIMETER))
				throw new IllegalArgumentException("item may not contain the delimeter char: \""+DELIMETER+"\"");
		
		this.items = items;
	}

	public List<String> getPrices() {
		return prices;
	}

	public void setPrices(List<String> prices) {

		for (String price: prices)
			if (price.contains(DELIMETER))
				throw new IllegalArgumentException("price may not contain the delimeter char: \""+DELIMETER+"\"");
		
		this.prices = prices;
	}
	
	
	public boolean equals(Ticket ticket) {
		
		if (!ticket.getId().equals(id)) {
			return false;
		} else if (!ticket.getBarcode().equals(barcode)) {
			return false;
		} else if (!ticket.getQueuNumber().equals(queuNumber)) {
			return false;
		} else if (!ticket.getTotalAmount().equals(totalAmount)) {
			return false;
		}
		
		if (items.size() != ticket.getItems().size()) {
			return false;
		}
		for (int i = 0; i < items.size(); i++) {
			if (!ticket.getItems().get(i).equals(items.get(i))) {
				return false;
			}
		}
		
		if (prices.size() != ticket.getPrices().size()) {
			return false;
		}
		for (int i = 0; i < prices.size(); i++) {
			if (!ticket.getPrices().get(i).equals(prices.get(i))) {
				return false;
			}
		}
		
		return true;
	}
	
}
