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

public abstract class IDBResult<T> {

	/** Denotes whether the operation was successful or not. */
	private boolean success;
	
	/** Message from the database operation. */
	private String message;
	
	/**
	 * Sets the success flag.
	 * @param b The flag.
	 */
	public void setSuccess(boolean b) {
		success=b;
	}
	
	/**
	 * Gets the success flag.
	 * @return The flag.
	 */
	public boolean getSuccess() {
		return success;
	}
	
	/**
	 * Sets the database message.
	 * @param m The message.
	 */
	public void setMessage(String msg) {
		message=msg;	
	}
	
	/**
	 * Get the database message.
	 * @return The message.
	 */
	public String getMessage() {
		return message;
	}
}
