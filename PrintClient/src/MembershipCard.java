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


public class MembershipCard {
	/**
	 * University identification of the card owner
	 */
	private String id;
	
	/**
	 * First name of the card owner
	 */
	private String firstName;
	
	/**
	 * Last name of the card owner
	 */
	private String lastName;
	
	/**
	 * A comment to print on the card (like, for example, the year of the membership)
	 */
	private String comment;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
		
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public static MembershipCard fromJson(String string) {
		JSONObject jsonObject = JSONObject.fromObject(string);

		MembershipCard card = new MembershipCard();
		card.setId(jsonObject.getString("id"));
		card.setFirstName(jsonObject.getString("firstName"));
		card.setLastName(jsonObject.getString("lastName"));
		card.setComment(jsonObject.getString("comment"));
		
		return card;
	}
}
