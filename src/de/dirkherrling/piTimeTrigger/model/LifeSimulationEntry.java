/**
 * 
 */
package de.dirkherrling.piTimeTrigger.model;

import java.io.Serializable;

/**
 * @author Dirk Herrling
 *
 */
public class LifeSimulationEntry implements Serializable {

	private static final long serialVersionUID = 6360445288962521625L;
	
	private String firstDate;
	private String lastDate;
	private boolean active;
	private boolean[] dayOfWeekPattern;
	private String gettingUpFrom;
	private String gettingUpTill;
	private String goingToBedFrom;
	private String goingToBedTill;
	private int[] idsOfPowerPlugsToUse;
	private int[] idsOfPowerPlugsToKeepOn;

}
