import java.util.Date;
import java.util.Map;

/**
 * @author Karsten Daemen
 */
public class QueueNumberData {

	private int queueNumber;
	private String userId;
	private Map<String, Integer> items;
	private Date registrationTime;
	private float price;
	private String barcode;
	
	public QueueNumberData(int queueNumber, String userId, Map<String, Integer> items, float price, String barcode, Date registrationTime) {
		this.queueNumber = queueNumber;
		this.userId = userId;
		this.items = items;
		this.price = price;
		this.barcode = barcode;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	
	public int getQueueNumber() {
		return queueNumber;
	}

	public void setQueueNumber(int queueNumber) {
		this.queueNumber = queueNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, Integer> getItems() {
		return items;
	}

	public void setItems(Map<String, Integer> items) {
		this.items = items;
	}

	public Date getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(Date registrationTime) {
		this.registrationTime = registrationTime;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	
}
