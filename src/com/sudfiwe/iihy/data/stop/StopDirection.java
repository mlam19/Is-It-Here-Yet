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

public class StopDirection implements Comparable<StopDirection> {

	private String tag;
	private String title;
	private String name;
	
	public StopDirection(String pTag) {
		
		tag=pTag;
		title="";
		name="";
	}
	
	public StopDirection(String pTag,String pTitle,String pName) {
		
		tag=pTag;
		title=pTitle;
		name=pName;
	}
	
	public String getTag() {
		return tag;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getName() {
		return name;
	}
	
	public int compareTo(StopDirection another) {
		
		return tag.compareTo(another.tag);
	}
}
