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

package com.sudfiwe.iihy.data.stop;

import com.sudfiwe.iihy.data.predict.IPredictionList;
import com.sudfiwe.iihy.data.predict.PredictionListProxy;

public class Stop implements Comparable<Stop> {

	private String tag;
	private String title;
	private String shortTitle;
	private String stopId;
	private String agencyTag;
	private String routeTag;
	private IPredictionList predictions;
	
	public Stop(String pTag) {
		
		init(pTag,"","","","","");
	}
	
	public Stop(String pTag,String pTitle,
			String pShortTitle,String pStopId,
			String pAgencyTag,String pRouteTag) {
		
		init(pTag,pTitle,pShortTitle,pStopId,pAgencyTag,
				pRouteTag);
	}
	
	private void init(String pTag,String pTitle,
			String pShortTitle,String pStopId,
			String pAgencyTag,String pRouteTag) {
		
		tag=pTag;
		title=pTitle;
		shortTitle=pShortTitle;
		stopId=pStopId;
		agencyTag=pAgencyTag;
		routeTag=pRouteTag;
		predictions=new PredictionListProxy(this);
	}
	
	public String getTag() {
		return tag;
	}

	public String getTitle() {
		return title;
	}

	public String getShortTitle() {
		return shortTitle;
	}
	
	public String getStopId() {
		return stopId;
	}
	
	public String getAgencyTag() {
		return agencyTag;
	}
	
	public String getRouteTag() {
		return routeTag;
	}
	
	public IPredictionList getPredictions() {
		return predictions;
	}
	
	public int compareTo(Stop another) {
		
		return tag.compareTo(another.tag);
	}
	
	public void copyFrom(Stop another) {
		
		tag=another.tag;
		title=another.title;
		shortTitle=another.shortTitle;
		stopId=another.stopId;
		agencyTag=another.agencyTag;
		routeTag=another.routeTag;
	}
}
