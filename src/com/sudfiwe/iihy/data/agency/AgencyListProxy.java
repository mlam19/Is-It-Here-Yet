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

import java.util.ArrayList;

import com.sudfiwe.iihy.data.agency.Agency;
import com.sudfiwe.iihy.io.DBFacade;
import com.sudfiwe.iihy.io.DBResult;
import com.sudfiwe.iihy.util.Filter;
import com.sudfiwe.iihy.util.Filter.FilterType;

public class AgencyListProxy implements IAgencyList {

	private AgencyList realList;
	
	private void generateList() {
		
		if (realList==null) {
			
			regenerateList();
		}
	}
	
	public void regenerateList() {
		
		Filter f=new Filter();
		
		DBResult<ArrayList<Agency>> res=
			(DBResult<ArrayList<Agency>>)
			DBFacade.getInstance().getList(
					Agency.class,f);

		
		realList=new AgencyList();
		
		if (res.getSuccess()) {
			
			realList.setList(res.getObject());
		}
	}
	
	public Agency getItem(String tag) {
		
		generateList();
		
		return realList.getItem(tag);
	}
	
	public Agency[] getList() {
		
		generateList();
		
		return realList.getList();
	}
}
