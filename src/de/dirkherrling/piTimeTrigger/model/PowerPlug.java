/**
 * 
 */
package de.dirkherrling.piTimeTrigger.model;

import java.io.Serializable;

/**
 * @author Dirk Herrling
 *
 */
public class PowerPlug implements Serializable {

	private static final long serialVersionUID = -1542196030916222461L;
	//series of 0 and 1 specified often in DIP switches in the radio controller
	private String systemCode; //2432
	//0 = off, 1 = on
	private byte state;
	//0..3 integer specifying the number of the power plug
	private byte plugCode;
	//internal, unique id of this plug
	private int id;
	//description of this power plug, e.g. 'living room'
	private String description;
	
	
	public PowerPlug(String systemCode, byte state, byte plugCode, String description, int ID) {
		super();
		this.systemCode = systemCode;
		this.state = state;
		this.plugCode = plugCode;
		this.id = ID;
		this.description = description;
	}
	
	public String toHTMLString() {
		String result = "";
		String powerState = "";
		if (this.state == 0) {
			powerState = "Aus";
		} else {
			powerState = "An";
		}
		
		result = "<li>" + " <form method=\"get\"><input type=\"hidden\" name=\"id\" value=\""+ this.id +"\" /><button class=\"tableButton\" type=\"submit\" name=\"page\" value=\"" +"editPowerPlug"+"\">Bearbeiten</button>"
				+ "<button class=\"tableButton\" type=\"submit\" name=\"action\" value=\"" +"deletePowerPlug"+"\">Löschen</button>"
				+ "<button class=\"tableButton\" type=\"submit\" name=\"action\" value=\"" +"switch"+"\">An / Aus</button> "
				 + this.description +"\t Kennung: " + this.systemCode + " " + this.plugCode + "\t " + powerState + 
				  "</form>" +"</li>";
		
		return result;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public byte getPlugCode() {
		return plugCode;
	}

	public void setPlugCode(byte plugCode) {
		this.plugCode = plugCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
