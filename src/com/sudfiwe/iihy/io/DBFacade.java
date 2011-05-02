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

import java.util.ArrayList;

import com.sudfiwe.iihy.io.mapper.MapperFactory;
import com.sudfiwe.iihy.util.Filter;

public class DBFacade {

	private static DBFacade instance; 
	
	private DBFacade() {
		
	}
	
	public IDBResult<?> saveItem(Class<?> cls,Object obj,Filter f) {
		return MapperFactory.getInstance().getMapper(cls).
		saveItem(obj,f);
	}
	
	public IDBResult<?> getItem(Class<?> cls,Filter f) {
		return MapperFactory.getInstance().getMapper(cls).
		getItem(f);
	}
	
	public IDBResult<ArrayList<?>> getList(Class<?> cls,Filter f) {
		return MapperFactory.getInstance().getMapper(cls).
		getList(f);
	}

	public static DBFacade getInstance() {
		if (instance==null) {
			instance=new DBFacade();
		}

		return instance;
	}
}
