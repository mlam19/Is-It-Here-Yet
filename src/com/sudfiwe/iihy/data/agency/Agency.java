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

package com.sudfiwe.iihy.data.agency;

import com.sudfiwe.iihy.data.route.IRouteList;
import com.sudfiwe.iihy.data.route.RouteListProxy;

public class Agency implements Comparable<Agency> {

	private String tag;
	private String title;
	private String shortTitle;
	private String regionTitle;
	private IRouteList routes;

	public Agency(String pTag) {
		this(pTag,"","","");
	}
	
	public Agency(String pTag,String pTitle,String pShortTitle,
			String pRegionTitle) {

		tag=pTag;
		title=pTitle;
		shortTitle=pShortTitle;
		regionTitle=pRegionTitle;
		routes=new RouteListProxy(this);
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

	public String getRegionTitle() {
		return regionTitle;
	}
	
	public IRouteList getRoutes() {
		return routes;
	}

	public int compareTo(Agency another) {
		
		return tag.compareTo(another.tag);
	}
}
