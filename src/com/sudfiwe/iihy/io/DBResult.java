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

package com.sudfiwe.iihy.io;

public class DBResult<T> extends IDBResult {

	/** The object returned from the database. */
	private T object;
	
	/**
	 * Creates a new object.
	 */
	public DBResult() {
		object=null;
		setSuccess(false);
		setMessage("");
	}
	
	public void setObject(T obj) {
		object=obj;
	}
	
	/**
	 * Gets the object returned by the database.
	 * @return The object.
	 */
	public T getObject() {
		return object;
	}
}
