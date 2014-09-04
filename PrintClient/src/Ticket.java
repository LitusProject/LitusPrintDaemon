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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


public class Ticket {
	/**
	 * KOPER ID (s-nummer, ...)
	 */
	private String id;

	/**
	 * TICKET TITEL
	 */
	private String title;

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
	private String queueNumber;

	/**
	 * TOTALE PRIJS AANKOOP
	 */
	private String totalAmount;

	/**
	 * LIJST VAN AFZONDERLIJK AANGEKOCHTE ITEMS
	 */
	private List<TicketItem> items = new ArrayList<TicketItem>();

	public static Ticket fromJson(String string) {
		JSONObject jsonObject = JSONObject.fromObject(string);

		Ticket job = new Ticket();
		job.setId(jsonObject.getString("id"));
		job.setTitle(jsonObject.getString("title"));
		job.setName(jsonObject.getString("name"));
		job.setBarcode(jsonObject.getString("barcode"));
		job.setQueueNumber(jsonObject.getString("queuenumber"));
		job.setTotalAmount(jsonObject.getString("totalAmount"));
		job.setType(jsonObject.getInt("type"));


		JSONArray items = jsonObject.getJSONArray("items");

		for (int i = 0; i < items.size(); i++) {
			job.addItem(TicketItem.fromJson(items.get(i).toString()));
		}

		return job;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
		return queueNumber;
	}

	public void setQueueNumber(String queueNumber) {
		this.queueNumber = queueNumber;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<TicketItem> getItems() {
		return items;
	}

	public void setItems(List<TicketItem> items) {
		this.items = items;
	}

	public void addItem(TicketItem item) {
		this.items.add(item);
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
		} else if (!ticket.getTitle().equals(title)) {
			return false;
		} else if (!ticket.getName().equals(name)) {
			return false;
		} else if (!ticket.getBarcode().equals(barcode)) {
			return false;
		} else if (!ticket.getQueueNumber().equals(queueNumber)) {
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
			if (!ticket.getItems().get(i).equals(items.get(i)))
				return false;
		}

		return true;
	}

	public String toJsonString() {
	    return toJsonObject().toString();
	}

	public JSONObject toJsonObject() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", getId());
		map.put("title", getTitle());
		map.put("name", getName());
		map.put("barcode", getBarcode());
		map.put("queuenumber", getQueueNumber());
		map.put("totalAmount", getTotalAmount());

		JSONObject[] oArray = new JSONObject[getItems().size()];
		for (int i = 0; i < items.size(); i++) {
			oArray[i] = items.get(i).toJsonObject();
		}
		map.put("items", oArray);

	    return (JSONObject) JSONSerializer.toJSON(map);
	}
}
