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

package com.sudfiwe.iihy.io.mapper;

import com.sudfiwe.iihy.data.Preferences;
import com.sudfiwe.iihy.data.agency.Agency;
import com.sudfiwe.iihy.data.predict.PredictionList;
import com.sudfiwe.iihy.data.route.Route;
import com.sudfiwe.iihy.data.stop.StopList;

public class MapperFactory {

	private static MapperFactory instance;
	
	private AgencyListMapper agencyListMapper;
	private PredictionListMapper predictionListMapper;
	private PreferencesMapper preferencesMapper;
	private RouteListMapper routeListMapper;
	private StopListMapper stopListMapper;
	
	private MapperFactory() {
		
		agencyListMapper=new AgencyListMapper();
		predictionListMapper=new PredictionListMapper();
		preferencesMapper=new PreferencesMapper();
		routeListMapper=new RouteListMapper();
		stopListMapper=new StopListMapper();
	}
	
	public IMapper getMapper(Class<?> cls) {
		IMapper mapper=null;
		
		String name=cls.getName();
		if (Agency.class.getName().equals(name)) {

			mapper=agencyListMapper;
		} else if (PredictionList.class.getName().equals(name)) {
			mapper=predictionListMapper;
		} else if (Preferences.class.getName().equals(name)) {
			mapper=preferencesMapper;
		} else if (Route.class.getName().equals(name)) {
			mapper=routeListMapper;
		} else if (StopList.class.getName().equals(name)) {
			mapper=stopListMapper;
		} 
		
		return mapper;
	}
	
	public static MapperFactory getInstance() {
		
		if (instance==null) {
			instance=new MapperFactory();
		}
		
		return instance;
	}
}
