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

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


public class TicketItem {
	/**
	 * TITLE
	 */
	private String title;

	/**
	 * BARCODE
	 */
	private String barcode;

	/**
	 * PRICE
	 */
	private String price;

	/**
	 * NUMBER
	 */
	private int number;

	public static TicketItem fromJson(String string) {
		JSONObject jsonObject = JSONObject.fromObject(string);

		TicketItem job = new TicketItem();
		job.setTitle(jsonObject.getString("title"));
		job.setBarcode(jsonObject.getString("barcode"));
		job.setNumber(jsonObject.getInt("number"));
		job.setPrice(jsonObject.getString("price"));

		return job;

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public boolean equals(TicketItem item) {
		if (!item.getTitle().equals(title)) {
			return false;
		} else if (!item.getBarcode().equals(barcode)) {
			return false;
		} else if (item.getNumber() != number) {
			return false;
		} else if (!item.getPrice().equals(price)) {
			return false;
		}

		return true;
	}

	public String toJsonString() {
	    return toJsonObject().toString();
	}

	public JSONObject toJsonObject() {
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("title", getTitle());
	    map.put("barcode", getBarcode());
	    map.put("number", getNumber());
	    map.put("price", getPrice());

	    return (JSONObject) JSONSerializer.toJSON(map);
	}
}
