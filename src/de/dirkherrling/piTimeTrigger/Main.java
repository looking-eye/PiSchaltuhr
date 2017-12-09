/**
 * 
 */
package de.dirkherrling.piTimeTrigger;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import de.dirkherrling.piTimeTrigger.engine.Engine;
import de.dirkherrling.piTimeTrigger.model.PiTimeTriggerModel;
import de.dirkherrling.piTimeTrigger.ui.MainView;



/**
 * @author Dirk Herrling
 *
 */
public class Main {
	
	@SuppressWarnings("unused")
	private static void testSetup() {
//		PlannedPowerEvent ppe = new PlannedPowerEvent("10:15", "", true, 
//				new boolean[] {true, true, true, true, true, true, true}, 
//				"", "", true, new int[] {0, 1, 2}, "0, 1 und 2 an");
		PiTimeTriggerModel.getInstance().addPlannedPowerEvent("10:15", "", true, 
				new boolean[] {true, true, true, true, true, true, true}, 
				"", "", true, new int[] {0, 1, 2}, "0, 1 und 2 an", true);
		PiTimeTriggerModel.getInstance().addPlannedPowerEvent("10:35", "", true, 
				new boolean[] {true, true, true, true, true, true, true}, 
				"", "", true, new int[] {0, 1, 2}, "0, 1 und 2 an", true);
		PiTimeTriggerModel.getInstance().addPlannedPowerEvent("11:15", "", true, 
				new boolean[] {true, true, true, true, true, true, true}, 
				"", "", true, new int[] {0, 1, 2}, "0, 1 und 2 an", true);
		PiTimeTriggerModel.getInstance().addPlannedPowerEvent("12:15", "", true, 
				new boolean[] {false, false, false, false, false, true, true}, 
				"", "", true, new int[] {0, 1, 2}, "0, 1 und 2 an", true);
		PiTimeTriggerModel.getInstance().addPlannedPowerEvent("14:15", "", true, 
				new boolean[] {true, true, true, true, true, true, true}, 
				"", "", true, new int[] {0, 1, 2}, "0, 1 und 2 an", true);
		PiTimeTriggerModel.getInstance().addPlannedPowerEvent("18:15", "", true, 
				new boolean[] {false, false, true, true, true, true, true}, 
				"", "", true, new int[] {0, 1, 2}, "0, 1 und 2 an", true);
		PiTimeTriggerModel.getInstance().addPlannedPowerEvent("20:15", "", true, 
				new boolean[] {true, true, true, true, true, false, false}, 
				"", "", true, new int[] {0, 1, 2}, "0, 1 und 2 an", true);
		PiTimeTriggerModel.getInstance().addPlannedPowerEvent("21:15", "", true, 
				new boolean[] {true, true, true, true, true, true, true}, 
				"", "", true, new int[] {0, 1, 2}, "0, 1 und 2 an", true);
		PiTimeTriggerModel.getInstance().addPlannedPowerEvent("23:15", "", true, 
				new boolean[] {true, true, true, true, true, false, false}, 
				"", "", true, new int[] {0, 1, 2}, "0, 1 und 2 an", true);
		PiTimeTriggerModel.saveInstance();
	}
	
	public static void testDates() {
		Calendar c = GregorianCalendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
//		System.out.println(c.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SUNDAY);
//		PlannedPowerEvent ppe = new PlannedPowerEvent("16:28", "26.12.2016", true, new boolean[] {true, true, true, true, true, true, true}, "24.12.2016", "28.12.2016", true, new int[] {}, "test", 0, true);
//		System.out.println(Engine.isToday(ppe, System.currentTimeMillis()));
//		System.out.println(Engine.isNow(ppe, System.currentTimeMillis()));
//		System.out.println(Engine.isBefore(ppe, System.currentTimeMillis()));
//		System.out.println(Engine.isAfter(ppe, System.currentTimeMillis()));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		testSetup();
//		testDates();
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        Server jettyServer = new Server(80);
        jettyServer.setHandler(context);
        ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", MainView.class.getCanonicalName());
        Thread t = new Thread(new Engine());
        t.start();
        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
            jettyServer.destroy();
        }
	}

}
