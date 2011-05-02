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

import java.util.Arrays;
import java.util.Hashtable;

public class StopList implements IStopList {

	private StopDirection[] directionSortedList;
	private Hashtable<StopDirection,Stop[]> list;
	private Hashtable<StopDirection,Stop[]> sortedList;
	
	public StopList() {
		
		directionSortedList=new StopDirection[0];
		list=new Hashtable<StopDirection,Stop[]>();
		sortedList=new Hashtable<StopDirection,Stop[]>();
	}
	
	public StopList(Hashtable<StopDirection,Stop[]> pList) {
		
		setList(pList);
	}
	
	public void setList(Hashtable<StopDirection,Stop[]> pList) {
		
		list=pList;
		sortedList=new Hashtable<StopDirection,Stop[]>();
		
		directionSortedList=
			list.keySet().toArray(new StopDirection[list.size()]);
		
		Arrays.sort(directionSortedList);
		
		for (StopDirection dir:directionSortedList) {
			
			Stop[] rawList=list.get(dir).clone();
			Arrays.sort(rawList);
			sortedList.put(dir,rawList);
		}
	}
	
	public void regenerateList() {
		
	}
	
	public StopDirection[] getDirections() {
		
		return directionSortedList;
	}
	
	public StopDirection getDirection(String dirTag) {
		
		StopDirection ret=null;
		
		int index=Arrays.binarySearch(directionSortedList,
				new StopDirection(dirTag));
		
		if (index>=0&&index<directionSortedList.length) {
			
			ret=directionSortedList
			[Arrays.binarySearch(directionSortedList,
					new StopDirection(dirTag))];
		} else {
			ret=directionSortedList[0];
		}
		
		return ret;
	}
	
	public Stop getItem(String dirTag,String stopTag) {

		Stop[] rawList=sortedList.get(getDirection(dirTag));
		
		return rawList[Arrays.binarySearch(rawList,
				new Stop(stopTag))];
	}

	public Stop[] getList(String dirTag) {
		
		return list.get(getDirection(dirTag));
	}
}
