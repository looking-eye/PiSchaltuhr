/**
 * 
 */
package de.dirkherrling.piTimeTrigger.engine;

import java.util.Vector;

/**
 * @author Dirk Herrling
 *
 */
public class Utils {
	
	public static boolean[] dayOfWeekToBoolArray(String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {
		boolean[] result = new boolean[7];
		
		if (monday != null && monday.equals("on")) {
			result[0] = true;
		}
		if (tuesday != null && tuesday.equals("on")) {
			result[1] = true;
		}
		if (wednesday != null && wednesday.equals("on")) {
			result[2] = true;
		}
		if (thursday != null && thursday.equals("on")) {
			result[3] = true;
		}
		if (friday != null && friday.equals("on")) {
			result[4] = true;
		}
		if (saturday != null && saturday.equals("on")) {
			result[5] = true;
		}
		if (sunday != null && sunday.equals("on")) {
			result[6] = true;
		}
		
		return result;
	}


	public static String boolArrayToDayOfWeek(boolean[] dayOfWeekPattern) {
		String result = "";
		
		if (dayOfWeekPattern[0]) {
			result += "Mo, ";
		}
		if (dayOfWeekPattern[1]) {
			result += "Di, ";
		}
		if (dayOfWeekPattern[2]) {
			result += "Mi, ";
		}
		if (dayOfWeekPattern[3]) {
			result += "Do, ";
		}
		if (dayOfWeekPattern[4]) {
			result += "Fr, ";
		}
		if (dayOfWeekPattern[5]) {
			result += "Sa, ";
		}
		if (dayOfWeekPattern[6]) {
			result += "So, ";
		}
		
		result = result.substring(0, Math.max(result.length()-2, 0));
		return result;
	}
	
	
	public static boolean urlStringToBoolean(String string) {
		if (string != null && string.equals("on")) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public static int[] csvToIntArray(String csv) {
		String temp = csv;
		int[] result;
		if (csv == null || csv.equals("")) {
			return new int[0];
		}
		temp = temp.trim();
		Vector<Integer> tempResult = new Vector<>();
		String values[] = temp.split(",");
		for (String s : values) {
			tempResult.add(Integer.valueOf(s.trim()));
		}
		result = new int[tempResult.size()];
		for (int i = 0; i < tempResult.size(); i++) {
			result[i] = tempResult.get(i);
		}
		return result;
	}



}
