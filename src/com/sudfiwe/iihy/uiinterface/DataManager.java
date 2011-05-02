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

package com.sudfiwe.iihy.uiinterface;

import java.util.Arrays;
import java.util.LinkedList;

import com.sudfiwe.iihy.data.Preferences;
import com.sudfiwe.iihy.data.agency.Agency;
import com.sudfiwe.iihy.data.agency.AgencyListProxy;
import com.sudfiwe.iihy.data.agency.IAgencyList;
import com.sudfiwe.iihy.data.predict.IPredictionList;
import com.sudfiwe.iihy.data.route.Route;
import com.sudfiwe.iihy.data.stop.Stop;
import com.sudfiwe.iihy.data.stop.StopDirection;
import com.sudfiwe.iihy.io.DBFacade;
import com.sudfiwe.iihy.io.DBResult;
import com.sudfiwe.iihy.util.Filter;

import android.app.Activity;

public class DataManager {

	private static DataManager instance;

	private Activity activity;
	private Preferences preferences;
	private IAgencyList agencies;

	private String agencyTag;
	private String routeTag;
	private String directionTag;
	private String stopTag;

	public DataManager() {

		agencies=new AgencyListProxy();
	}

	public void setActivity(Activity pAct) {

		activity=pAct;

		Filter f=new Filter();
		f.addEntry(Filter.FilterType.PREF_APPACT,activity);

		DBResult<Preferences> res=(DBResult<Preferences>)
		DBFacade.getInstance().getItem(Preferences.class,f);

		preferences=res.getObject();
	}

	public void savePreferences() {

		Filter f=new Filter();
		f.addEntry(Filter.FilterType.PREF_APPACT,activity);

		preferences.setAgencyTag(agencyTag);
		preferences.setRouteTag(routeTag);
		preferences.setDirectionTag(directionTag);
		preferences.setStopTag(stopTag);

		DBResult<Preferences> res=(DBResult<Preferences>)
		DBFacade.getInstance().saveItem(Preferences.class,
				preferences,f);
	}

	public int getDefaultAgency() {

		Agency[] list=new Agency[0];
		int index=-1;

		String tag=preferences.getAgencyTag();

		if (tag.trim().length()>0) {
			list=agencies.getList();
			index=Arrays.binarySearch(list,new Agency(tag));
		}

		if (index<0||index>=list.length) {
			index=-1;
		} else {
			agencyTag=tag;
		}

		return index;
	}

	public int getDefaultRoute() {

		Route[] list=new Route[0];
		int index=-1;

		String tag=preferences.getRouteTag();

		if (tag.trim().length()>0) {
			list=agencies.getItem(preferences.getAgencyTag())
			.getRoutes().getList();
			index=Arrays.binarySearch(list,new Route(tag));
		}

		if (index<0||index>=list.length) {
			index=-1;
		} else {
			routeTag=tag;
		}

		return index;
	}

	public int getDefaultDirection() {

		StopDirection[] list=new StopDirection[0];
		int index=-1;

		String tag=preferences.getDirectionTag();

		if (tag.trim().length()>0) {
			list=agencies.getItem(preferences.getAgencyTag())
			.getRoutes().getItem(preferences.getRouteTag())
			.getStops().getDirections();
			index=Arrays.binarySearch(list,new StopDirection(tag));
		}

		if (index<0||index>=list.length) {
			index=-1;
		} else {
			directionTag=tag;
		}

		return index;
	}

	public int getDefaultStop() {

		Stop[] list=new Stop[0];
		int index=-1;

		String tag=preferences.getStopTag();

		if (tag.trim().length()>0) {
			list=agencies.getItem(preferences.getAgencyTag())
			.getRoutes().getItem(preferences.getRouteTag())
			.getStops().getList(preferences.getDirectionTag());

			for (int iter=0;iter<list.length&&index==-1;iter++) {

				if (list[iter].getTag().equals(tag)) {
					index=iter;
				}
			}
		}

		if (index<0||index>=list.length) {
			index=-1;
		} else {
			stopTag=tag;
		}

		return index;
	}

	public String[] getAgencyTitles(boolean refreshList) {

		LinkedList<String> list=new LinkedList<String>();

		if (refreshList) {

			agencies.regenerateList();
		}

		for (Agency a:agencies.getList()) {

			list.addLast(a.getTitle());
		}

		return list.toArray(new String[list.size()]);
	}

	public String[] getRouteTitles(int aIndex) {

		agencyTag=agencies.getList()[aIndex].getTag();

		LinkedList<String> list=new LinkedList<String>();

		for (Route r:agencies.getItem(agencyTag)
				.getRoutes().getList()) {

			list.addLast(r.getTitle());
		}

		return list.toArray(new String[list.size()]);
	}

	public String[] getDirectionTitles(int rIndex) {

		routeTag=agencies.getItem(agencyTag).getRoutes()
		.getList()[rIndex].getTag();

		LinkedList<String> list=new LinkedList<String>();

		for (StopDirection sd:agencies.getItem(agencyTag)
				.getRoutes().getItem(routeTag)
				.getStops().getDirections()) {

			list.addLast(sd.getTitle());
		}

		return list.toArray(new String[list.size()]);
	}

	public String[] getStopTitles(int dIndex) {

		directionTag=agencies.getItem(agencyTag).getRoutes()
		.getItem(routeTag).getStops()
		.getDirections()[dIndex].getTag();

		LinkedList<String> list=new LinkedList<String>();

		for (Stop s:agencies.getItem(agencyTag)
				.getRoutes().getItem(routeTag)
				.getStops().getList(directionTag)) {

			list.addLast(s.getTitle());
		}

		return list.toArray(new String[list.size()]);
	}

	public void setStop(int sIndex) {

		stopTag=agencies.getItem(agencyTag).getRoutes()
		.getItem(routeTag).getStops()
		.getList(directionTag)[sIndex].getTag();
	}

	public boolean hasAgencyChanged(int aIndex) {

		return !agencies.getList()[aIndex].getTag()
		.equals(agencyTag);
	}

	public boolean hasRouteChanged(int rIndex) {

		return !agencies.getItem(agencyTag).getRoutes()
		.getList()[rIndex].getTag().equals(routeTag);
	}

	public boolean hasDirectionChanged(int dIndex) {

		return !agencies.getItem(agencyTag).getRoutes()
		.getItem(routeTag).getStops()
		.getDirections()[dIndex].getTag().equals(directionTag);
	}
	
	public boolean hasStopChanged(int sIndex) {

		return !agencies.getItem(agencyTag).getRoutes()
		.getItem(routeTag).getStops()
		.getList(directionTag)[sIndex].equals(stopTag);
	}

	public String getAgencyName() {

		return agencies.getItem(agencyTag).getTitle();
	}

	public String getRouteName() {

		return agencies.getItem(agencyTag)
		.getRoutes().getItem(routeTag)
		.getTitle();
	}

	public String getDirectionName() {

		return agencies.getItem(agencyTag)
		.getRoutes().getItem(routeTag).getStops()
		.getDirection(directionTag).getTitle();
	}

	public String getStopName() {

		return agencies.getItem(agencyTag)
		.getRoutes().getItem(routeTag).getStops()
		.getItem(directionTag,stopTag).getTitle();
	}

	public IPredictions getPredictions() {

		IPredictionList list=agencies.getItem(agencyTag)
		.getRoutes().getItem(routeTag).getStops()
		.getItem(directionTag,stopTag).getPredictions();

		list.regenerateList(directionTag);

		return list;
	}

	public static DataManager getInstance() {

		if (instance==null) {

			instance=new DataManager();
		}

		return instance;
	}
}
