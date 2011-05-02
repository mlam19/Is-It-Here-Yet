/**
 * Is It Here Yet? is a simple Android application that reads the
 * NextBus public XML feed and displays the arrival times for
 * transit vehicles for a chosen stop. 
 * Copyright (C) 2011 Matthew Lam
 *
 * Is It Here Yet? is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Is It Here Yet? is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Is It Here Yet?.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sudfiwe.iihy.data.predict;

import java.util.Calendar;

import com.sudfiwe.iihy.uiinterface.IPredictionItem;

public class Prediction implements IPredictionItem {

	private Calendar arrival;
	private boolean isDeparture;
	private String vehicle;
	
	public Prediction(Calendar pArrival,boolean pIsDeparture,
			String pVeh) {
		
		arrival=pArrival;
		isDeparture=pIsDeparture;
		vehicle=pVeh;
	}
	
	public Calendar getArrival() {
		return arrival;
	}

	public boolean isDeparture() {
		return isDeparture;
	}

	public String getVehicle() {
		return vehicle;
	}
	
	/**
	 * Gets the number of minutes until the given time from the
	 * predicted arrival time.
	 * @param compare The time to check against the predicted
	 * arrival time.
	 * @return The difference in minutes. If the predicted time
	 * is after the given time, then the value is negative.
	 */
	public int getMinuteDifference(Calendar compare) {
		
		long predict=compare.getTimeInMillis()
		-arrival.getTimeInMillis();
		
		int diffMin=(int)(predict/1000/60);

		if (arrival.after(compare)) {
			diffMin=-diffMin;
		}
		
		return diffMin;
	}
}
