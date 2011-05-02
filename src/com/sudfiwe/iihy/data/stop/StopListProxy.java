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

import java.util.ArrayList;

import com.sudfiwe.iihy.data.route.Route;
import com.sudfiwe.iihy.io.DBFacade;
import com.sudfiwe.iihy.io.DBResult;
import com.sudfiwe.iihy.util.Filter;
import com.sudfiwe.iihy.util.Filter.FilterType;

public class StopListProxy implements IStopList {

	private Route route;
	private StopList realList;
	
	public StopListProxy(Route pRoute) {
		
		route=pRoute;
	}
	
	private void generateList() {
		
		if (realList==null) {
			
			regenerateList();
		}
	}
	
	public void regenerateList() {
		
		Filter f=new Filter();
		f.addEntry(FilterType.STOP_AGENCYTAG,
				route.getAgencyTag());
		f.addEntry(FilterType.STOP_ROUTETAG,
				route.getTag());
		
		DBResult<ArrayList<StopList>> res=
			(DBResult<ArrayList<StopList>>)
			DBFacade.getInstance().getList(
					StopList.class,f);
		
		realList=new StopList();
		
		if (res.getSuccess()&&res.getObject().size()==1) {
			
			realList=res.getObject().get(0);
		} else {
			realList=new StopList();
		}
	}
	
	public StopDirection[] getDirections() {
		
		generateList();
		
		return realList.getDirections();
	}
	
	public StopDirection getDirection(String dirTitle) {
	
		generateList();
		
		return realList.getDirection(dirTitle);
	}
	
	public Stop getItem(String dirTitle,String stopTitle) {
		
		generateList();
		
		return realList.getItem(dirTitle,stopTitle);
	}
	
	public Stop[] getList(String dirTitle) {
		
		generateList();
		
		return realList.getList(dirTitle);
	}
}
