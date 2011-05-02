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

import java.util.Hashtable;
import com.sudfiwe.iihy.data.stop.Stop;
import com.sudfiwe.iihy.io.DBFacade;
import com.sudfiwe.iihy.io.DBResult;
import com.sudfiwe.iihy.uiinterface.IPredictionDirection;
import com.sudfiwe.iihy.uiinterface.IPredictionItem;
import com.sudfiwe.iihy.util.Filter;
import com.sudfiwe.iihy.util.Filter.FilterType;

public class PredictionListProxy implements IPredictionList {

	private Stop stop;
	private PredictionList realList;
	
	public PredictionListProxy(Stop pStop) {
		
		stop=pStop;
	}
	
	public void generateList(String dirTag) {
		
		if (realList==null) {
			
			regenerateList(dirTag);
		}
	}
	
	public void regenerateList(String dirTag) {
		
		Filter f=new Filter();
		f.addEntry(FilterType.PREDICT_AGENCYTAG,
				stop.getAgencyTag());
		f.addEntry(FilterType.PREDICT_ROUTETAG,
				stop.getRouteTag());
		//f.addEntry(FilterType.PREDICT_DIRTAG,
		//		dirTag);
		f.addEntry(FilterType.PREDICT_STOPTAG,
				stop.getTag());
		
		DBResult<PredictionList> res=
			(DBResult<PredictionList>)
			DBFacade.getInstance().getList(
					PredictionList.class,f);
		
		realList=new PredictionList();
		
		if (res.getSuccess()) {
			
			realList=res.getObject();
		} else {
			
			realList=new PredictionList();
		}
	}
	
	public Hashtable<IPredictionDirection, IPredictionItem[]> getList() {
		
		return realList.getList();
	}
	
	public String getMessage() {
		
		return realList.getMessage();
	}
}
