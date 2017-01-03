package de.dirkherrling.piTimeTrigger.ui;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.dirkherrling.piTimeTrigger.engine.Commands;
import de.dirkherrling.piTimeTrigger.engine.Utils;
import de.dirkherrling.piTimeTrigger.model.PiTimeTriggerModel;
import de.dirkherrling.piTimeTrigger.model.PlannedPowerEvent;
import de.dirkherrling.piTimeTrigger.model.PowerPlug;

@Path("/")
public class MainView {
	
	private static String header;
	private static String footer;
	
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("helloWorld")
	public String helloWorld() {
		return "Hello World";
	}
	
	
	public String getMainContent() {
		String result = "";
		result += "\t<div id=\"content\"><form method=\"get\">\n"
				+ "\t\t<button type=\"submit\" name=\"action\" value=\"allOn\" style=\"background-color: #4CAF50;height=50px;border:none;color: white;padding: 15px 32px;text-align: center;text-decoration: none;display: inline-block;font-size: 16px;vertical-align:middle;margin:5%;width:40%;\">Alle An</button>"
				+ "<button type=\"submit\" name=\"action\" value=\"allOff\" style=\"background-color: #f44336;height=50px;border:none;color: white;padding: 15px 32px;text-align: center;text-decoration: none;display: inline-block;font-size: 16px;vertical-align:middle;margin:5%;width:40%;\">Alle Aus</button>\n"
				+ "\t</form>\n"
				+ ""
				+ "<p><h2>Liste der geplanten Ereignisse:</h2></p>\n"
				+ "<p>"
				+ "<ul>";
		
		for (PlannedPowerEvent ppe : PiTimeTriggerModel.getInstance().getPowerEvents()) {
			result += ppe.toHTMLString();
		}
				
		result += "</ul></p></div>\n\t\t<div id=\"footer\" class=\"footer\"><form method=\"get\">\n"
				+ "\t\t<button type=\"submit\" name=\"page\" value=\"editTimeTrigger\"  class=\"footerButton\">Zeitschaltung hinzufügen</button>\n"
				+ "\t\t<button type=\"submit\" name=\"page\" value=\"editSimulation\" class=\"footerButton\" disabled>Simulation hinzufügen</button>\n"
				+ "\t\t<button type=\"submit\" name=\"page\" value=\"preferences\" class=\"footerButton\">Einstellungen</button></form>\n";
		return result;
	}
	
	
	public String getEditTimeTriggerContent(String id) {
		String result = "";
		
		if (id == null) {
			//new entry
			result += "\t<div id=\"content\"><form method=\"get\">\n";
			result += "<p><h2>Neue Zeitschaltung anlegen</h2></p>";
			result += "<p>";
			result += "<table style=\"width:75%\">";
			result += "<tr><td>Beschreibung: </td><td><input name=\"description\"></td></tr>";
			result += "<tr><td>Einzelnes Datum (TT.MM.JJJJ): </td><td><input name=\"date\"></td></tr>";
			result += "<tr><td>Zeit (HH:MM): </td><td><input name=\"timeOfDay\"></td></tr>";
			result += "<tr><td>Regelmäßig? </td><td><input name=\"recurring\" type=\"checkbox\"></td></tr>";
			result += "<tr><td>Montag </td><td><input name=\"monday\" type=\"checkbox\"></td></tr>";
			result += "<tr><td>Dienstag </td><td><input name=\"tuesday\" type=\"checkbox\"></td></tr>";
			result += "<tr><td>Mittwoch </td><td><input name=\"wednesday\" type=\"checkbox\"></td></tr>";
			result += "<tr><td>Donnerstag </td><td><input name=\"thursday\" type=\"checkbox\"></td></tr>";
			result += "<tr><td>Freitag </td><td><input name=\"friday\" type=\"checkbox\"></td></tr>";
			result += "<tr><td>Samstag </td><td><input name=\"saturday\" type=\"checkbox\"></td></tr>";
			result += "<tr><td>Sonntag </td><td><input name=\"sunday\" type=\"checkbox\"></td></tr>";
			result += "<tr><td>Anschalten? (Haken: an, kein Haken: aus) </td><td><input name=\"switchOn\" type=\"checkbox\" checked></td></tr>";
			result += "<tr><td>Erste Ausführung: </td><td><input name=\"firstDate\"></td></tr>";
			result += "<tr><td>Letzte Ausführung: </td><td><input name=\"lastDate\"></td></tr>";
			result += "<tr><td>Nummern der Steckdosen<br/>(Komma getrennt, leer lassen für alle)</td><td><input name=\"affectedPlugs\"></td></tr>";
//			result += "<tr><td>Regelmäßig? </td><td><input name=\"recurring\" type=\"checkbox\"></td></tr>";
			result += "<tr><td>Aktiviert </td><td><input name=\"active\" type=\"checkbox\" checked></td></tr>";
			result += "</table></p></div>";
		} else {
			//edit existing entry
			int localID = Integer.valueOf(id);
			PlannedPowerEvent ppeToEdit = null;
			for (PlannedPowerEvent ppe : PiTimeTriggerModel.getInstance().getPowerEvents()) {
				if (ppe.getID() == localID) {
					ppeToEdit = ppe;
					break;
				}
			}
			if (ppeToEdit != null) {
				String dayChecked[] = new String[7];
				for (int i = 0; i < 7; i++) {
					if (ppeToEdit.getDayOfWeekPattern()[i]) {
						dayChecked[i] = " checked ";
					} else {
						dayChecked[i] = " ";
					}
				}
				String recurringString = "";
				if (ppeToEdit.isRecurring()) {
					recurringString = "checked";
				}
				String activeString = "";
				if (ppeToEdit.isActive()) {
					activeString = "checked";
				}
				String switchOnString = "";
				if (ppeToEdit.isSwitchOn()) {
					switchOnString = "checked";
				}
				String affectedPlugsString = "";
				for (int i = 0; i < ppeToEdit.getAffectedPlugs().length; i++) {
					affectedPlugsString += String.valueOf(ppeToEdit.getAffectedPlugs()[i]);
					affectedPlugsString += ", ";
				}
				if (affectedPlugsString.length() > 1) {
					affectedPlugsString = affectedPlugsString.substring(0, affectedPlugsString.length()-2);
				}
				
				result += "\t<div id=\"content\"><form method=\"get\">\n";
				result += "<p><h2>Eintrag " + id + " bearbeiten</h2></p>";
				result += "<p>";
				result += "<table style=\"width:75%\">";
				result += "<tr><td>Beschreibung: </td><td><input name=\"description\" value=\"" + ppeToEdit.getDescription() + "\"></td></tr>";
				result += "<tr><td>Einzelnes Datum (TT.MM.JJJJ): </td><td><input name=\"date\" value=\"" + ppeToEdit.getDate() + "\"></td></tr>";
				result += "<tr><td>Zeit (HH:MM): </td><td><input name=\"timeOfDay\" value=\"" + ppeToEdit.getTimeOfDay() + "\"></td></tr>";
				result += "<tr><td>Regelmäßig? </td><td><input name=\"recurring\" type=\"checkbox\" " + recurringString + " ></td></tr>";
				result += "<tr><td>Montag </td><td><input name=\"monday\" type=\"checkbox\" " + dayChecked[0] + " ></td></tr>";
				result += "<tr><td>Dienstag </td><td><input name=\"tuesday\" type=\"checkbox\" " + dayChecked[1] + " ></td></tr>";
				result += "<tr><td>Mittwoch </td><td><input name=\"wednesday\" type=\"checkbox\" " + dayChecked[2] + " ></td></tr>";
				result += "<tr><td>Donnerstag </td><td><input name=\"thursday\" type=\"checkbox\" " + dayChecked[3] + " ></td></tr>";
				result += "<tr><td>Freitag </td><td><input name=\"friday\" type=\"checkbox\" " + dayChecked[4] + " ></td></tr>";
				result += "<tr><td>Samstag </td><td><input name=\"saturday\" type=\"checkbox\" " + dayChecked[5] + " ></td></tr>";
				result += "<tr><td>Sonntag </td><td><input name=\"sunday\" type=\"checkbox\" " + dayChecked[6] + " ></td></tr>";
				result += "<tr><td>Anschalten? (Haken: an, kein Haken: aus) </td><td><input name=\"switchOn\" type=\"checkbox\" " + switchOnString + " ></td></tr>";
				result += "<tr><td>Erste Ausführung: </td><td><input name=\"firstDate\" value=\"" + ppeToEdit.getFirstDate() + "\"></td></tr>";
				result += "<tr><td>Letzte Ausführung: </td><td><input name=\"lastDate\" value=\"" + ppeToEdit.getLastDate() + "\"></td></tr>";
				result += "<tr><td>Nummern der Steckdosen<br/>(Komma getrennt, leer lassen für alle)</td><td><input name=\"affectedPlugs\" value=\"" + affectedPlugsString + "\"></td></tr>";
//				result += "<tr><td>Regelmäßig? </td><td><input name=\"recurring\" type=\"checkbox\"></td></tr>";
				result += "<tr><td>Aktiviert </td><td><input name=\"active\" type=\"checkbox\" " + activeString + " ></td></tr>";
				result += "<input type=\"hidden\" name=\"id\" value=\"" + ppeToEdit.getID() + "\"/>";
				result += "</table></p></div>";
			} else {
				result += "<p>Eintrag " + id + " wurde nicht gefunden.</p><p><a href=\"?page=main\">Weiter</a></p>"; 
			}
		}
		result += "<div id=\"footer\" class=\"footer\">\n"
				+ "\t\t<button type=\"submit\" name=\"action\" value=\"saveTimeTrigger\" class=\"footerButton\">Speichern</button>\n"
				+ "\t\t<button type=\"submit\" name=\"page\" value=\"main\"  class=\"footerButton\">Abbrechen</button>\n"
				+ "\t\t</form>\n";
		
		return result;
	}
	
	
	public String getPreferencesContent() {
		String result = "\t<div id=\"content\"><form method=\"get\">\n"
			+ "\t\t<button type=\"submit\" name=\"action\" value=\"shutdown\" style=\"background-color: #f44336;height=50px;border:none;color: white;padding: 15px 32px;text-align: center;text-decoration: none;display: inline-block;font-size: 16px;vertical-align:middle;margin:5%;width:40%;\">Herunterfahren</button>"
			+ "<button type=\"submit\" name=\"action\" value=\"update\" style=\"background-color: #f44336;height=50px;border:none;color: white;padding: 15px 32px;text-align: center;text-decoration: none;display: inline-block;font-size: 16px;vertical-align:middle;margin:5%;width:40%;\">Aktualisieren</button>\n"
			+ "\t</form>\n"
			+ ""
			+ "<p><h2>Liste der registrierten Steckdosen:</h2></p>\n"
			+ "<p>"
			+ "<ul>";

		for (PowerPlug pp : PiTimeTriggerModel.getInstance().getAllPowerPlugs()) {
			result += pp.toHTMLString();
		}
		
		result += "</ul></p></div>\n\t\t<div id=\"footer\" class=\"footer\"><form method=\"get\">\n"
			+ "\t\t<button type=\"submit\" name=\"page\" value=\"editPowerPlug\"  class=\"footerButton\">Steckdose hinzufügen</button>\n"
//			+ "\t\t<button type=\"submit\" name=\"page\" value=\"editSimulation\" class=\"footerButton\" disabled>Simulation hinzufügen</button>\n"
			+ "\t\t<button type=\"submit\" name=\"page\" value=\"main\" class=\"footerButton\">Zurück</button></form>\n";

		return result;
	}
	
	
	public String getEditPowerPlug(String id) {
		String result = "";
		
		if (id == null) {
			result += "\t<div id=\"content\"><form method=\"get\">\n";
			result += "<p><h2>Neue Steckdose anlegen</h2></p>";
			result += "<p>";
			result += "<table style=\"width:75%\">";
			result += "<tr><td>Beschreibung: </td><td><input name=\"description\"></td></tr>";
			result += "<tr><td>System code (1..4095): </td><td><input name=\"systemCode\"></td></tr>";
			result += "<tr><td>Plug code (0..3): </td><td><input name=\"plugCode\"></td></tr>";
			
//			result += "<input type=\"hidden\" name=\"id\" value=\"" + id + "\"/></table></p></div>";
			result += "<div id=\"footer\" class=\"footer\">\n"
					+ "\t\t<button type=\"submit\" name=\"action\" value=\"savePowerPlug\" class=\"footerButton\">Speichern</button>\n"
					+ "\t\t<button type=\"submit\" name=\"page\" value=\"preferences\"  class=\"footerButton\">Abbrechen</button>\n"
					+ "\t\t</form></div>\n";
		} else {
			PowerPlug ppToEdit = null;
			for (PowerPlug pp : PiTimeTriggerModel.getInstance().getAllPowerPlugs()) {
				if (pp.getId() == Integer.valueOf(id)) {
					ppToEdit = pp;
					break;
				}
			}
			
			result += "\t<div id=\"content\"><form method=\"get\">\n";
			result += "<p><h2>Neue Steckdose anlegen</h2></p>";
			result += "<p>";
			result += "<table style=\"width:75%\">";
			result += "<tr><td>Beschreibung: </td><td><input name=\"description\" value=\"" + ppToEdit.getDescription() + "\"/></td></tr>";
			result += "<tr><td>System code (z.B. 00100): </td><td><input name=\"systemCode\" value=\"" + ppToEdit.getSystemCode() + "\"/></td></tr>";
			result += "<tr><td>Plug code (0..31): </td><td><input name=\"plugCode\" value=\"" + ppToEdit.getPlugCode() + "\"/></td></tr>";
			
			result += "<input type=\"hidden\" name=\"id\" value=\"" + id + "\"/></table></p></div>";
			result += "<div id=\"footer\" class=\"footer\">\n"
					+ "\t\t<button type=\"submit\" name=\"action\" value=\"savePowerPlug\" class=\"footerButton\">Speichern</button>\n"
					+ "\t\t<button type=\"submit\" name=\"page\" value=\"preferences\"  class=\"footerButton\">Abbrechen</button>\n"
					+ "\t\t</form></div>\n";
		}
		
		return result;
	}
	
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getPage(@QueryParam("page") String page, @QueryParam("action") String action, @QueryParam("id") String id,
			@QueryParam("description") String description, @QueryParam("date") String date, @QueryParam("recurring") String recurring,
			@QueryParam("monday") String monday, @QueryParam("tuesday") String tuesday, @QueryParam("wednesday") String wednesday, @QueryParam("thursday") String thursday, @QueryParam("friday") String friday, @QueryParam("saturday") String saturday, @QueryParam("sunday") String sunday,
			@QueryParam("firstDate") String firstDate, @QueryParam("lastDate") String lastDate, @QueryParam("switchOn") String switchOn,  @QueryParam("active") String active, 
			@QueryParam("affectedPlugs") String affectedPlugs, @QueryParam("timeOfDay") String timeOfDay,
			@QueryParam("systemCode") String systemCode, @QueryParam("plugCode") String plugCode) {
		String result = this.getHeader();
		if (page == null && action == null) {
			page = "main";
		}
		if (page != null) {
			if (page.equals("main")) {
				result += getMainContent();
			} else if (page.equals("editTimeTrigger")) {
				result += getEditTimeTriggerContent(id);
			} else if (page.equals("editSimulation")) {
				
			} else if (page.equals("preferences")) {
				result += getPreferencesContent();
			} else if (page.equals("editPowerPlug")) {
				result += getEditPowerPlug(id);
			}
			
			result += this.getFooter();
			return result;
		}
		
		if (action != null) {
			if (action.equals("deleteItem")) {
				if (id != null) {
					int idAsInt = Integer.valueOf(id);
					boolean success = PiTimeTriggerModel.getInstance().removeItem(idAsInt);
					if (success) {
						result += "<p>Eintrag wurde entfernt.</p><p><a href=\"?page=main\">Weiter</a></p>"; 
					} else {
						result += "<p>Eintrag Nummer " + id + " wurde nicht gefunden.</p><p><a href=\"?page=main\">Weiter</a></p>";
					}
					result += this.getFooter();
					return result;
				}
			} else if (action.equals("deletePowerPlug")) {
				if (id != null) {
					int idAsInt = Integer.valueOf(id);
					boolean success = PiTimeTriggerModel.getInstance().removePowerPlug(idAsInt);
					if (success) {
						result += "<p>Steckdose Nummer " + id + " wurde entfernt.</p><p><a href=\"?page=main\">Weiter</a></p>"; 
					} else {
						result += "<p>Steckdose Nummer " + id + " wurde nicht gefunden.</p><p><a href=\"?page=main\">Weiter</a></p>";
					}
					result += this.getFooter();
					return result;
				}
			} else if (action.equals("allOn")) {
				Commands.allOn();
				return "<p>Alle registrierten Steckdosen eingeschaltet</p><p><a href=\"?page=main\">Weiter</a></p>";
			} else if (action.equals("allOff")) {
				Commands.allOff();
				return "<p>Alle registrierten Steckdosen ausgeschaltet</p><p><a href=\"?page=main\">Weiter</a></p>";
			} else if (action.equals("saveTimeTrigger")) {
				this.addTimeTrigger(timeOfDay, id, description, date, recurring, monday, tuesday, wednesday, thursday, friday, saturday, sunday, firstDate, lastDate, switchOn, active, affectedPlugs);
				PiTimeTriggerModel.saveInstance();
				return "<p>Eintrag wurde gespeichert</p><p><a href=\"?page=main\">Weiter</a></p>";
			} else if (action.equals("savePowerPlug")) {
				this.addPowerPlug(id, description, systemCode, plugCode);
				PiTimeTriggerModel.saveInstance();
				return "<p>Steckdose gespeichert</p><p><a href=\"?page=preferences\">Weiter</a></p>";
			} else if (action.equals("switch")) {
				for (PowerPlug pp : PiTimeTriggerModel.getInstance().getAllPowerPlugs()) {
					if (pp.getId() == Integer.valueOf(id)) {
						boolean currentlyOn = (pp.getState()==1);
						Commands.switchPowerPlug(pp, !currentlyOn);
						break;
					}
				}
				return "<p>Steckdose an/aus geschaltet</p><p><a href=\"?page=preferences\">Weiter</a></p>";
			} else if (action.equals("shutdown")) {
				Commands.shutdown();
				return "<p>Wird heruntergefahren</p><p><a href=\"?page=main\">Weiter</a></p>";
			} else if (action.equals("update")) {
				Commands.update();
				return "<p>Wird aktualisiert und anschliessend neu gestartet</p><p><a href=\"?page=main\">Weiter</a></p>";
			}
				
		}
		return "<p>Ungültige URL</p><p><a href=\"?page=main\">Weiter</a>";
	}
	
	private void addPowerPlug(String id, String description, String systemCode, String plugCode) {
		if (id == null || id.trim().equals("")) {		//new power plug
			int localID = PiTimeTriggerModel.getInstance().getNextFreePowerPlugID();
			byte plugCodeAsByte = Byte.valueOf(plugCode);
			PowerPlug pp = new PowerPlug(systemCode, (byte)0, plugCodeAsByte, description, localID);
			PiTimeTriggerModel.getInstance().addPowerPlug(pp);
		} else {
			PowerPlug plugToEdit = null;
			for (PowerPlug pp : PiTimeTriggerModel.getInstance().getAllPowerPlugs()) {
				if (pp.getId() == Integer.valueOf(id)) {
					plugToEdit = pp;
					break;
				}
			}
			plugToEdit.setDescription(description);
			plugToEdit.setSystemCode(systemCode);
			plugToEdit.setPlugCode(Byte.valueOf(plugCode));
		}
	}
	
	
	private void addTimeTrigger(String timeOfDay, String id, String description, String date, String recurring,
			String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday,
			String firstDate, String lastDate, String switchOn,  String active, 
			String affectedPlugs) {
		if (id == null) {
			PlannedPowerEvent ppe = new PlannedPowerEvent(timeOfDay, date, Utils.urlStringToBoolean(recurring), 
					Utils.dayOfWeekToBoolArray(monday, tuesday, wednesday, thursday, friday, saturday, sunday), 
					firstDate, lastDate, Utils.urlStringToBoolean(switchOn), Utils.csvToIntArray(affectedPlugs), 
					description, PiTimeTriggerModel.getInstance().getNextFreePowerEventID(), Utils.urlStringToBoolean(active));
			PiTimeTriggerModel.getInstance().addPlannedPowerEvent(ppe);
		} else {
			int localID = Integer.valueOf(id);
			PlannedPowerEvent ppeToEdit = null;
			for (PlannedPowerEvent ppe : PiTimeTriggerModel.getInstance().getPowerEvents()) {
				if (ppe.getID() == localID) {
					ppeToEdit = ppe;
					break;
				}
			}
			if (ppeToEdit == null) {
				PlannedPowerEvent ppe = new PlannedPowerEvent(timeOfDay, date, Utils.urlStringToBoolean(recurring), 
						Utils.dayOfWeekToBoolArray(monday, tuesday, wednesday, thursday, friday, saturday, sunday), 
						firstDate, lastDate, Utils.urlStringToBoolean(switchOn), Utils.csvToIntArray(affectedPlugs), 
						description, PiTimeTriggerModel.getInstance().getNextFreePowerEventID(), Utils.urlStringToBoolean(active));
				PiTimeTriggerModel.getInstance().addPlannedPowerEvent(ppe);
			} else {
				ppeToEdit.setTimeOfDay(timeOfDay);
				ppeToEdit.setDate(date);
				ppeToEdit.setRecurring(Utils.urlStringToBoolean(recurring));
				ppeToEdit.setDayOfWeekPattern(Utils.dayOfWeekToBoolArray(monday, tuesday, wednesday, thursday, friday, saturday, sunday));
				ppeToEdit.setFirstDate(firstDate);
				ppeToEdit.setLastDate(lastDate);
				ppeToEdit.setSwitchOn(Utils.urlStringToBoolean(switchOn));
				ppeToEdit.setAffectedPlugs(Utils.csvToIntArray(affectedPlugs));
				ppeToEdit.setDescription(description);
				ppeToEdit.setActive(Utils.urlStringToBoolean(active));
			}
		}
	}
	
	/*@GET
	@Produces(MediaType.TEXT_HTML)
	public String getAction(@QueryParam("action") String action) {
		if (action.equals("Test")) {
			return "Test!";
		}
		return "Kein Test!";
	}*/
	
	private String getHeader() {
		if (MainView.header != null) {
			return header;
		} else {
			String result = "";
			try {
				File f = new File("./htmlHeader.template");
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line = br.readLine();
				while (line != null) {
					result += line;
					result += "\n";
					line = br.readLine();
				}
				br.close();
				return result;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
	}
	
	private String getFooter() {

		if (MainView.footer != null) {
			return footer;
		} else {
			String result = "";
			try {
				File f = new File("./htmlFooter.template");
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line = br.readLine();
				while (line != null) {
					result += line;
					result += "\n";
					line = br.readLine();
				}
				br.close();
				return result;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

}
