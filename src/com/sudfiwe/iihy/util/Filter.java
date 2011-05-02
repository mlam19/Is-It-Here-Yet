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

package com.sudfiwe.iihy.util;

import java.util.Hashtable;

public class Filter {

	public enum FilterType {
		PREDICT_AGENCYTAG,
		PREDICT_DIRTAG,
		PREDICT_ROUTETAG,
		PREDICT_STOPTAG,
		STOP_AGENCYTAG,
		STOP_ROUTETAG,
		ROUTE_AGENCYTAG,
		PREF_APPACT
	}
	
	private Hashtable<FilterType,Object> terms;
	
	public Filter() {
		
		terms=new Hashtable<FilterType,Object>();
	}
	
	public boolean addEntry(FilterType type,Object value) {
		boolean ret=true;
		
        if (terms.containsKey(type)) {
            ret=false;
        } else {
            terms.put(type,value);
        }
            
        return ret;
    }
	
	 public void removeEntry(FilterType type) {
         terms.remove(type);
     }
	 
	 public boolean contains(FilterType type) {
         return terms.containsKey(type);
     }

     public Object getEntry(FilterType type) {
    	 
    	 Object obj=null;
    	 
         if (this.contains(type)) {
             obj=terms.get(type);
         }
         
         return obj;
     }
}
