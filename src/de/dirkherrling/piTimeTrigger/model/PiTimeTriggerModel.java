/**
 * 
 */
package de.dirkherrling.piTimeTrigger.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

/**
 * @author Dirk Herrling
 *
 */
public class PiTimeTriggerModel implements Serializable {
	
	private static final long serialVersionUID = -1837332470057498344L;
	
	private Vector<PowerPlug> powerPlugs;
	private Vector<PlannedPowerEvent> powerEvents;
	private static PiTimeTriggerModel instance;
	private int nextFreePowerEventID;
	private int nextFreePowerPlugID;
	
	private PiTimeTriggerModel() {
		
	}
	
	public static void saveInstance() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("./piTimeTriggerModel.ser")));
			oos.writeObject(instance);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initialize() {
		this.powerEvents = new Vector<>();
		this.powerPlugs = new Vector<>();
		this.nextFreePowerEventID = 0;
		saveInstance();
	}
	
	public static PiTimeTriggerModel getInstance() {
		if (instance == null) {
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("./piTimeTriggerModel.ser")));
				instance = (PiTimeTriggerModel)ois.readObject();
				ois.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				instance = new PiTimeTriggerModel();
				instance.initialize();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				instance = new PiTimeTriggerModel();
				instance.initialize();
			} catch (IOException e) {
				e.printStackTrace();
				instance = new PiTimeTriggerModel();
				instance.initialize();
			}
		}
		return instance;
	}
	
	public boolean removeItem(int id) {
		PlannedPowerEvent itemToRemove = null;
		for (PlannedPowerEvent ppe : this.powerEvents) {
			if (ppe.getID() == id) {
				itemToRemove = ppe;
			}
		}
		if (itemToRemove != null) {
			this.powerEvents.remove(itemToRemove);
			saveInstance();
			return true;
		}
		return false;
	}
	
	public void addPowerPlug(String systemCode, byte plugCode, String description) {
		PowerPlug pp = new PowerPlug(systemCode, (byte)0, plugCode, description, getNextFreePowerPlugID());
		this.powerPlugs.add(pp);
		saveInstance();
	}
	
	public PowerPlug getPowerPlug(int id) {
		for (PowerPlug pp : this.powerPlugs) {
			if (pp.getId() == id) {
				return pp;
			}
		}
		return null;
	}
	
	public boolean removePowerPlug(int id) {
		PowerPlug ppToRemove = null;
		for (PowerPlug pp : this.powerPlugs) {
			if (pp.getId() == id) {
				ppToRemove = pp;
			}
		}
		if (ppToRemove != null) {
			this.powerPlugs.remove(ppToRemove);
			saveInstance();
			return true;
		} else {
			return false;
		}
	}
	
	public Vector<PowerPlug> getAllPowerPlugs() {
		return this.powerPlugs;
	}
	
	public void addPlannedPowerEvent(String timeOfDay, String date, boolean recurring, boolean[] dayOfWeekPattern,
			String firstDate, String lastDate, boolean switchOn, int[] affectedPlugs, String description, boolean active) {
		PlannedPowerEvent ppe2 = new PlannedPowerEvent(timeOfDay, date, recurring, dayOfWeekPattern, firstDate, lastDate, switchOn, affectedPlugs, description, getNextFreePowerEventID(), active);
		this.powerEvents.addElement(ppe2);
	}
	
	public void addPlannedPowerEvent(PlannedPowerEvent ppe) {
		this.powerEvents.add(ppe);
		PiTimeTriggerModel.saveInstance();
	}
	
	public void addPowerPlug(PowerPlug pp) {
		this.powerPlugs.add(pp);
		PiTimeTriggerModel.saveInstance();
	}

	public Vector<PlannedPowerEvent> getPowerEvents() {
		return powerEvents;
	}

	public int getNextFreePowerEventID() {
		this.nextFreePowerEventID++;
		return nextFreePowerEventID;
	}

	public int getNextFreePowerPlugID() {
		this.nextFreePowerPlugID++;
		return nextFreePowerPlugID;
	}

}
