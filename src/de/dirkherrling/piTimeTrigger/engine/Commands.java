/**
 * 
 */
package de.dirkherrling.piTimeTrigger.engine;

import java.io.IOException;
import java.util.Date;

import de.dirkherrling.piTimeTrigger.model.PiTimeTriggerModel;
import de.dirkherrling.piTimeTrigger.model.PowerPlug;

/**
 * @author Dirk Herrling
 *
 */
public class Commands {
	
	public static void allOn() {
		for (PowerPlug pp : PiTimeTriggerModel.getInstance().getAllPowerPlugs()) {
			Commands.switchPowerPlug(pp, true);
		}
	}
	
	public static void allOff() {
		for (PowerPlug pp : PiTimeTriggerModel.getInstance().getAllPowerPlugs()) {
			Commands.switchPowerPlug(pp, false);
		}
	}
	
	public static void shutdown() {
		System.out.println("Shutting down");
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
					ProcessBuilder pb = new ProcessBuilder("shutdown", "-h", "now");
					pb.start();
					Thread.sleep(5000);
					System.exit(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
	
	public static void update() {
		System.out.println(new Date() +  " Updating and rebooting");
		ProcessBuilder pb = new ProcessBuilder("sudo", "/bin/bash", "update.sh");
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void init() {
		System.out.println(new Date() + " Initialising pilight");
		ProcessBuilder pb = new ProcessBuilder("sudo", "/bin/bash", "init.sh");
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void switchPowerPlug(PowerPlug pp, boolean switchOn) {
		System.out.println("Switching " + pp.getDescription() + " (" + pp.getId() + " " + pp.getSystemCode() + " " + pp.getPlugCode() + ") to " + switchOn);
//		String plugCode = String.valueOf(pp.getPlugCode());
		String targetState = "-f";
		pp.setState((byte)0);
		if (switchOn) {
			targetState = "-t";
			pp.setState((byte)1);
		}
		try {
//			String commandString = new File(".").getAbsolutePath();
//			commandString += "/raspberry-remote/send";
//			System.out.println("Debug: " + commandString + " " + pp.getSystemCode() + " " + String.valueOf(pp.getPlugCode()) + " " + targetState);
			ProcessBuilder pb = new ProcessBuilder("sudo", "pilight-send", "-p", "quigg_gt7000", targetState, "-i", pp.getSystemCode(), "-u", String.valueOf(pp.getPlugCode()));
//			ProcessBuilder pb = new ProcessBuilder(commandString, "-u",  pp.getSystemCode(), plugCode, targetState);
//			pb.directory(new File("raspberry-remote"));
			Process p = pb.start();
			while (p.isAlive()) {
				Thread.sleep(10);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
