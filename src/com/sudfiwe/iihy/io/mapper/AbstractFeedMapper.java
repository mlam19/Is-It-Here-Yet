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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.sudfiwe.iihy.io.DBResult;
import com.sudfiwe.iihy.util.Filter;
import com.sudfiwe.iihy.util.Filter.FilterType;

/**
 * http://www.nextbus.com/xmlFeedDocs/NextBusXMLFeed.pdf
 */
public abstract class AbstractFeedMapper<E> implements IMapper<E> {

	public static final String URL=
		"http://webservices.nextbus.com/service/publicXMLFeed";
	
	public static final String QUERY_COMMAND=
		"command";
	
	public static final String VALUE_COMMAND_AGENCYLIST=
		"agencyList";
	
	public static final String VALUE_COMMAND_ROUTELIST=
		"routeList";
	
	public static final String VALUE_COMMAND_ROUTECONFIG=
		"routeConfig";
	
	public static final String VALUE_COMMAND_PREDICT=
		"predictions";
	
	public static final String VALUE_COMMAND_VEHLOC=
		"vehicleLocations";
	
	public static final String QUERY_AGENCY="a";
	
	public static final String QUERY_ROUTE="r";
	
	public static final String QUERY_STOP="s";
	
	public static final String QUERY_TIME="t";
	
	private boolean afterFirstQueryComponent;

	protected boolean isAfterFirstQueryComponent() {
		return afterFirstQueryComponent;
	}

	protected void setAfterFirstQueryComponent(boolean afterFirstQueryComponent) {
		this.afterFirstQueryComponent=afterFirstQueryComponent;
	}

	protected String createQuery(
			Filter f,FilterType t,String q) {

		String ret="";

		if (f.contains(t)) {

			if (afterFirstQueryComponent) {
				ret=ret+"&";
			} else {
				ret=ret+"?";
			}

			ret=ret+q+"="+f.getEntry(t);

			afterFirstQueryComponent=true;
		}

		return ret;
	}

	protected Document getXMLFromURL(URL url)
	throws IOException,ParserConfigurationException,SAXException {

		HttpURLConnection urlConnection=
			(HttpURLConnection)url.openConnection();

		BufferedReader in=new BufferedReader(
				new InputStreamReader(urlConnection.getInputStream()));

		StringBuilder xmlBuilder=new StringBuilder();
		String line;

		while ((line=in.readLine())!=null) {
			xmlBuilder.append(line+'\n');
		}

		in.close();
		urlConnection.disconnect();

		InputSource inSrc=new InputSource();
		inSrc.setCharacterStream(new StringReader(
				xmlBuilder.toString()));

		DocumentBuilder builder=
			DocumentBuilderFactory.newInstance().newDocumentBuilder();

		return builder.parse(inSrc);
	}
	
	public DBResult<E> saveItem(E obj,Filter f) {

		DBResult<E> ret=new DBResult<E>();

		ret.setMessage("Feature not implemented.");

		return ret;
	}
}
