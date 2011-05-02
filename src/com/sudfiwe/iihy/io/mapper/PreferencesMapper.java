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

import java.util.ArrayList;

import com.sudfiwe.iihy.data.Preferences;
import com.sudfiwe.iihy.io.DBResult;
import com.sudfiwe.iihy.util.Filter;
import com.sudfiwe.iihy.util.Filter.FilterType;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesMapper implements
IMapper<Preferences> {

	private final String AGENCY_TAG="agencyTag";
	private final String ROUTE_TAG="routeTag";
	private final String DIRECTION_TAG="directionTag";
	private final String STOP_TAG="stopTag";
	
	public DBResult<Preferences> saveItem(
			Preferences obj, Filter f) {

		DBResult<Preferences> ret=
			new DBResult<Preferences>();

		Activity appAct=(Activity)f.getEntry(
				FilterType.PREF_APPACT);

		SharedPreferences prefs=PreferenceManager
		.getDefaultSharedPreferences(appAct);
		
		SharedPreferences.Editor editor=prefs.edit();
        editor.putString(AGENCY_TAG,obj.getAgencyTag());
        editor.putString(ROUTE_TAG,obj.getRouteTag());
        editor.putString(DIRECTION_TAG,obj.getDirectionTag());
        editor.putString(STOP_TAG,obj.getStopTag());
        editor.commit();

        ret.setSuccess(true);
        
		return ret;
	}

	public DBResult<Preferences> getItem(Filter f) {

		DBResult<Preferences> ret=
			new DBResult<Preferences>();

		Activity appAct=(Activity)f.getEntry(
				FilterType.PREF_APPACT);
		
		SharedPreferences prefs=PreferenceManager
		.getDefaultSharedPreferences(appAct);
		
		Preferences pref=new Preferences();
		pref.setAgencyTag(prefs.getString(AGENCY_TAG,""));
		pref.setRouteTag(prefs.getString(ROUTE_TAG,""));
		pref.setDirectionTag(prefs.getString(DIRECTION_TAG,""));
		pref.setStopTag(prefs.getString(STOP_TAG,""));

		ret.setObject(pref);
		ret.setSuccess(true);
		
		return ret;
	}

	public DBResult<ArrayList<Preferences>>
	getList(Filter f) {

		DBResult<ArrayList<Preferences>> ret=
			new DBResult<ArrayList<Preferences>>();

		ret.setMessage("Feature not implemented.");

		return ret;
	}
}
