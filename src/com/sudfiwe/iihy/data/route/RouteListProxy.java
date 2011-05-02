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

package com.sudfiwe.iihy.data.route;

import java.util.ArrayList;

import com.sudfiwe.iihy.data.agency.Agency;
import com.sudfiwe.iihy.io.DBFacade;
import com.sudfiwe.iihy.io.DBResult;
import com.sudfiwe.iihy.util.Filter;
import com.sudfiwe.iihy.util.Filter.FilterType;

public class RouteListProxy implements IRouteList {

	private Agency agency;
	private RouteList realList;
	
	public RouteListProxy(Agency pAgency) {
		
		agency=pAgency;
	}
	
	private void generateList() {
		
		if (realList==null) {
			
			regenerateList();
		}
	}
	
	public void regenerateList() {
		
		Filter f=new Filter();
		f.addEntry(FilterType.ROUTE_AGENCYTAG,
				agency.getTag());
		
		DBResult<ArrayList<Route>> res=
			(DBResult<ArrayList<Route>>)
			DBFacade.getInstance().getList(
					Route.class,f);
		
		realList=new RouteList();
		
		if (res.getSuccess()) {
			
			realList.setList(res.getObject());
		}
	}
	
	public Route getItem(String tag) {
		
		generateList();
		
		return realList.getItem(tag);
	}
	
	public Route[] getList() {
		
		generateList();
		
		return realList.getList();
	}
}
