/**
 * 
 */
package de.dirkherrling.piTimeTrigger.engine;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import de.dirkherrling.piTimeTrigger.model.PiTimeTriggerModel;
import de.dirkherrling.piTimeTrigger.model.PlannedPowerEvent;
import de.dirkherrling.piTimeTrigger.model.PowerPlug;

/**
 * @author Dirk Herrling
 *
 */
public class Engine implements Runnable {
	
	private enum Weekday {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, INVALID};
	
	private int lastProcessedMinute = -1;
//	private int lastProcessedHour = -1;
//	private Weekday lastProcessedWeekday = Weekday.INVALID;
//	private int lastProcessedDayOfTheMonth = -1;
//	private int lastProcessedMonth = -1;
//	private int lastProcessedYear = -1;
	
//	private boolean hourChanged(long timeStamp) {
//		Calendar c = GregorianCalendar.getInstance();
//		c.setTimeInMillis(timeStamp);
//		return (getHour(timeStamp) != lastProcessedHour);
//	}
	
	private boolean minuteChanged(long timeStamp) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTimeInMillis(timeStamp);
		return (getMinute(timeStamp) != lastProcessedMinute);
	}
	
	private static int getMinute(long timeStamp) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTimeInMillis(timeStamp);
		return c.get(GregorianCalendar.MINUTE);
	}
	
//	private static int getHour(long timeStamp) {
//		Calendar c = GregorianCalendar.getInstance();
//		c.setTimeInMillis(timeStamp);
//		return c.get(GregorianCalendar.HOUR_OF_DAY);
//	}
	
//	private boolean dayChanged(long timeStamp) {
//		Calendar c = GregorianCalendar.getInstance();
//		c.setTimeInMillis(timeStamp);
//		if (lastProcessedHour == 23 && c.get(GregorianCalendar.HOUR_OF_DAY) == 0) {
//			return true;
//		} else {
//			return false;
//		}
//	}
	
	public static Weekday weekdayFromTimeStamp(long timestamp) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTimeInMillis(timestamp);
		int dayOfWeek = c.get(GregorianCalendar.DAY_OF_WEEK);
		Weekday result = Weekday.INVALID;

		switch (dayOfWeek) {
			case GregorianCalendar.MONDAY:
				result = Weekday.MONDAY;
				return result;
			case GregorianCalendar.TUESDAY:
				result = Weekday.TUESDAY;
				return result;
			case GregorianCalendar.WEDNESDAY:
				result = Weekday.WEDNESDAY;
				return result;
			case GregorianCalendar.THURSDAY:
				result = Weekday.THURSDAY;
				return result;
			case GregorianCalendar.FRIDAY:
				result = Weekday.FRIDAY;
				return result;
			case GregorianCalendar.SATURDAY:
				result = Weekday.SATURDAY;
				return result;
			case GregorianCalendar.SUNDAY:
				result = Weekday.SUNDAY;
				return result;
		}
		
		return result;
	}
	
	public static boolean isNow(PlannedPowerEvent ppe, long timeStamp) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTimeInMillis(timeStamp);
		try {
			int hour = Integer.valueOf(ppe.getTimeOfDay().split(":")[0]);
			int minute = Integer.valueOf(ppe.getTimeOfDay().split(":")[1]);
			if (minute == c.get(GregorianCalendar.MINUTE) && hour == c.get(GregorianCalendar.HOUR_OF_DAY)) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
//		if (c.get(GregorianCalendar.HOUR_OF_DAY) == ppe.get
	}
	
	public static boolean isToday(PlannedPowerEvent ppe, long timeStamp) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTimeInMillis(timeStamp);
		try {
			int dayOfMonth = Integer.valueOf(ppe.getDate().split("\\.")[0]);
			int monthOfYear = Integer.valueOf(ppe.getDate().split("\\.")[1]);
			int year = Integer.valueOf(ppe.getDate().split("\\.")[2]);
			if (dayOfMonth == c.get(GregorianCalendar.DAY_OF_MONTH)
					&& monthOfYear == c.get(GregorianCalendar.MONTH)+1
					&& year == c.get(GregorianCalendar.YEAR)) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			return false;
		}
	}
	
	public static boolean weekdayMatches(PlannedPowerEvent ppe, long timeStamp) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTimeInMillis(timeStamp);
		if (c.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.MONDAY) {
			return ppe.getDayOfWeekPattern()[0];
		} else if (c.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.TUESDAY) {
			return ppe.getDayOfWeekPattern()[1];
		} else if (c.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.WEDNESDAY) {
			return ppe.getDayOfWeekPattern()[2];
		} else if (c.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.THURSDAY) {
			return ppe.getDayOfWeekPattern()[3];
		} else if (c.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.FRIDAY) {
			return ppe.getDayOfWeekPattern()[4];
		} else if (c.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SATURDAY) {
			return ppe.getDayOfWeekPattern()[5];
		} else if (c.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SUNDAY) {
			return ppe.getDayOfWeekPattern()[6];
		}

		return true;
	}
	
	public static boolean isBefore(PlannedPowerEvent ppe, long timeStamp) {
		if (ppe.getLastDate() == null || ppe.getLastDate().equals("")) {
			return true;
		}
		Calendar plannedEvent = GregorianCalendar.getInstance();
		plannedEvent.set(GregorianCalendar.HOUR_OF_DAY, 23);
		plannedEvent.set(GregorianCalendar.MINUTE, 59);
		Calendar now;
		try {
//			System.out.println(ppe.getLastDate());
			plannedEvent.set(Integer.valueOf(ppe.getLastDate().split("\\.")[2]),
					Integer.valueOf(ppe.getLastDate().split("\\.")[1])-1,
					Integer.valueOf(ppe.getLastDate().split("\\.")[0]));
			now = GregorianCalendar.getInstance();
			now.setTimeInMillis(timeStamp);
//			System.out.println(plannedEvent.getTime());
//			System.out.println(now.getTime());
			if (now.compareTo(plannedEvent) <=0) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return true;
		}
	}
	
	public static boolean isAfter(PlannedPowerEvent ppe, long timeStamp) {
		if (ppe.getFirstDate() == null || ppe.getFirstDate().equals("")) {
			return true;
		}
		Calendar plannedEvent = GregorianCalendar.getInstance();
		plannedEvent.set(GregorianCalendar.HOUR_OF_DAY, 00);
		plannedEvent.set(GregorianCalendar.MINUTE, 00);
		Calendar now;
		try {
			plannedEvent.set(Integer.valueOf(ppe.getFirstDate().split("\\.")[2]),
					Integer.valueOf(ppe.getFirstDate().split("\\.")[1])-1,
					Integer.valueOf(ppe.getFirstDate().split("\\.")[0]));
			now = GregorianCalendar.getInstance();
			now.setTimeInMillis(timeStamp);
			if (now.compareTo(plannedEvent) >=0) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return true;
		}
	}
	
	public boolean evaluate(PlannedPowerEvent ppe, long timeStamp) {
		if (ppe.isActive()) {
			//single event
			if (!ppe.isRecurring()) {
				if (isToday(ppe, timeStamp) && isNow(ppe, timeStamp)) {
					return true;
				} else {
					return false;
				}
			}
			//recurring event
			else if (ppe.isRecurring() && isAfter(ppe, timeStamp) && isBefore(ppe, timeStamp)) {
				if (isNow(ppe, timeStamp)) {
					if (weekdayMatches(ppe, timeStamp)) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public void execute(PlannedPowerEvent ppe) {
		Commands.init();
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (ppe.isSwitchOn()) {
			if (ppe.getAffectedPlugs() == null || ppe.getAffectedPlugs().length == 0) {
				//switch all on
				Commands.allOn();
			} else {
				for (int i = 0; i < ppe.getAffectedPlugs().length; i++) {
//					PowerPlug ppToSwitch = PiTimeTriggerModel.getInstance().getPowerPlug(ppe.getID());
					PowerPlug ppToSwitch = PiTimeTriggerModel.getInstance().getPowerPlug(ppe.getAffectedPlugs()[i]);
					Commands.switchPowerPlug(ppToSwitch, true);
				}
			}
		} else {
			if (ppe.getAffectedPlugs() == null || ppe.getAffectedPlugs().length == 0) {
				//switch all on
				Commands.allOff();
			} else {
				for (int i = 0; i < ppe.getAffectedPlugs().length; i++) {
					//PowerPlug ppToSwitch = PiTimeTriggerModel.getInstance().getPowerPlug(ppe.getID());
					PowerPlug ppToSwitch = PiTimeTriggerModel.getInstance().getPowerPlug(ppe.getAffectedPlugs()[i]);
					Commands.switchPowerPlug(ppToSwitch, false);
				}
			}
		}
	}
	
	public void setLastProcessed(long timeStamp) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTimeInMillis(timeStamp);
//		this.lastProcessedHour = c.get(GregorianCalendar.HOUR_OF_DAY);
		this.lastProcessedMinute = c.get(GregorianCalendar.MINUTE);
	}
	
	public void run() {
		System.out.println(new Date() + " Starting PiSchaltuhr worker thread.");
		while (true) {
			long timeStamp = System.currentTimeMillis();
			try {
				if (minuteChanged(timeStamp)) {
					Vector<PlannedPowerEvent> plannedEventsToProcess = PiTimeTriggerModel.getInstance().getPowerEvents();
					for (PlannedPowerEvent ppe : plannedEventsToProcess) {
						if (evaluate(ppe, timeStamp)) {
							execute(ppe);
						}
					}
					setLastProcessed(timeStamp);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}

}
