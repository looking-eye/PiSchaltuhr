/**
 * 
 */
package de.dirkherrling.piTimeTrigger.model;

import java.io.Serializable;
import java.util.Arrays;

import de.dirkherrling.piTimeTrigger.engine.Utils;

/**
 * @author Dirk Herrling
 *
 */
public class PlannedPowerEvent implements Serializable {
	
	private static final long serialVersionUID = 2783286516553829517L;
	
	private String timeOfDay;
	private String date;
	private boolean recurring;
	private boolean[] dayOfWeekPattern;
	private String firstDate;
	private String lastDate;
	private boolean switchOn;
	private boolean active;
	private int[] affectedPlugs;
	private String description;
	private int ID;

	
	public PlannedPowerEvent(String timeOfDay, String date, boolean recurring, boolean[] dayOfWeekPattern,
			String firstDate, String lastDate, boolean switchOn, int[] affectedPlugs, String description, int ID, 
			boolean active) {
		super();
		this.timeOfDay = timeOfDay;
		this.date = date;
		this.recurring = recurring;
		this.dayOfWeekPattern = dayOfWeekPattern;
		this.firstDate = firstDate;
		this.lastDate = lastDate;
		this.switchOn = switchOn;
		this.affectedPlugs = affectedPlugs;
		this.description = description;
		this.ID = ID;
		this.active = active;
	}
	

	public String toHTMLString() {
		String result = "";
		String switchOn = "";
		if (this.switchOn) {
			switchOn = "anschalten";
		} else {
			switchOn = "ausschalten";
		}
		
		String localDescription = "";
		if (this.recurring) {
			localDescription = " Wöchentlich " + Utils.boolArrayToDayOfWeek(dayOfWeekPattern) + " um " + this.timeOfDay + " Steckdosen " + Arrays.toString(this.affectedPlugs) + " " + switchOn + ".";
		} else {
			localDescription = " Einmalig am " + this.date + " um " + this.timeOfDay + " Steckdosen " + Arrays.toString(this.affectedPlugs) + " " + switchOn + ".";
		}
		
		//Einmalig am 10.11.2016 um 17:30 Steckdosen 1, 2 und 3 anschalten/ausschalten
		//Wöchentlich Montags, Dienstags, Mittwochs um 17:30 Steckdosen 1, 2 und 3 anschalten/ausschalten
		
		String checked = "checked";
		if (!this.active) {
			checked = "";
		}
		
		result += "<li><form method=\"get\"><input type=\"hidden\" name=\"id\" value=\""+ this.ID +"\" />"
				+ "<button class=\"tableButton\" type=\"submit\" name=\"page\" value=\"editTimeTrigger\">Bearbeiten</button>"
				+ "<button class=\"tableButton\" type=\"submit\" name=\"action\" value=\"deleteItem\">Löschen</button> <input type=\"checkbox\" " + checked + " disabled/> " + localDescription +  "</form></li>"
						;
		
		return result;
	}
	
	public String getTimeOfDay() {
		return timeOfDay;
	}
	
	public void setTimeOfDay(String timeOfDay) {
		this.timeOfDay = timeOfDay;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public boolean isRecurring() {
		return recurring;
	}
	
	public void setRecurring(boolean recurring) {
		this.recurring = recurring;
	}
	
	public boolean[] getDayOfWeekPattern() {
		return dayOfWeekPattern;
	}
	
	public void setDayOfWeekPattern(boolean[] dayOfWeekPattern) {
		this.dayOfWeekPattern = dayOfWeekPattern;
	}
	
	public String getFirstDate() {
		return firstDate;
	}
	
	public void setFirstDate(String firstDate) {
		this.firstDate = firstDate;
	}
	
	public String getLastDate() {
		return lastDate;
	}
	
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	
	public boolean isSwitchOn() {
		return switchOn;
	}
	
	public void setSwitchOn(boolean switchOn) {
		this.switchOn = switchOn;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public int[] getAffectedPlugs() {
		return affectedPlugs;
	}
	
	public void setAffectedPlugs(int[] affectedPlugs) {
		this.affectedPlugs = affectedPlugs;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
}
