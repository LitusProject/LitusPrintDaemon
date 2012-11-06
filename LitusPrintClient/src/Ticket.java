import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


public class Ticket {
	/**
	 * KOPER ID (s-nummer, ...)
	 */
	private String id;
	
	/**
	 * KOPER NAAM ()
	 */
	private String name;
	
	/**
	 * BARCODE ID
	 */
	private String barcode;
	
	/**
	 * TICKET TYPE
	 */
	private int type = 1;
	
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
	 * BARCODE LIJST VAN DE AFZONDERLIJK AANGEKOCHTE ITEMS
	 */
	private List<String> itemBarcodes;
	
	public static Ticket fromJson(String string) {
		JSONObject jsonObject = JSONObject.fromObject(string);
		
		Ticket job = new Ticket();
		job.setId(jsonObject.getString("id"));
		job.setName(jsonObject.getString("name"));
		job.setBarcode(jsonObject.getString("barcode"));
		job.setQueuNumber(jsonObject.getString("queuenumber"));
		job.setTotalAmount(jsonObject.getString("totalAmount"));
		job.setType(jsonObject.getInt("type"));
		
		Object[] oArray = jsonObject.getJSONArray("items").toArray();
		String[] array =Arrays.asList(oArray).toArray(new String[oArray.length]);
		job.setItems(Arrays.asList(array));
		oArray = jsonObject.getJSONArray("prices").toArray();
		array =Arrays.asList(oArray).toArray(new String[oArray.length]);
		job.setPrices(Arrays.asList(array));
		oArray = jsonObject.getJSONArray("itemBarcodes").toArray();
		array =Arrays.asList(oArray).toArray(new String[oArray.length]);
		job.setItemBarcodes(Arrays.asList(array));
		
		return job;
		
	}
	
	public JSONObject toJsonObject() {
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("id", getId());
	    map.put("name", getName());
	    map.put("barcode", getBarcode());
	    map.put("queuenumber", getQueueNumber());
	    map.put("totalAmount", getTotalAmount());
	    map.put("type", getType()+"");
	    map.put("items", getItems());
	    map.put("prices", getPrices());	    
	    map.put("itemBarcodes", getItemBarcodes());
	    
	    return (JSONObject) JSONSerializer.toJSON(map);
	}
	
	public String toJsonString() {
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("id", getId());
	    map.put("barcode", getBarcode());
	    map.put("queuenumber", getQueueNumber());
	    map.put("totalAmount", getTotalAmount());
	    map.put("items", getItems());
	    map.put("prices", getPrices());	    
	    map.put("itemBarcodes", getItemBarcodes());
	    
	    JSONObject obj = (JSONObject) JSONSerializer.toJSON(map);
	    return obj.toString();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getQueueNumber() {
		return queuNumber;
	}

	public void setQueuNumber(String queuNumber) {
		this.queuNumber = queuNumber;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	public List<String> getPrices() {
		return prices;
	}

	public void setPrices(List<String> prices) {

		
		this.prices = prices;
	}
	
	public void setItemBarcodes(List<String> itemBarcodes) {
		this.itemBarcodes = itemBarcodes;
	}
	
	public List<String> getItemBarcodes() {
		return itemBarcodes;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean equals(Ticket ticket) {
		if (!ticket.getId().equals(id)) {
			return false;
		} else if (!ticket.getName().equals(name)) {
			return false;
		} else if (!ticket.getBarcode().equals(barcode)) {
			return false;
		} else if (!ticket.getQueueNumber().equals(queuNumber)) {
			return false;
		} else if (!ticket.getTotalAmount().equals(totalAmount)) {
			return false;
		} else if (ticket.getType() != type) {
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
		
		if (itemBarcodes.size() != ticket.getItemBarcodes().size()) {
			return false;
		}
		for (int i = 0; i < prices.size(); i++) {
			if (!ticket.getItemBarcodes().get(i).equals(itemBarcodes.get(i))) {
				return false;
			}
		}
		
		return true;
	}
}
