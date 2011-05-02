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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.sudfiwe.iihy.data.stop.Stop;
import com.sudfiwe.iihy.data.stop.StopDirection;
import com.sudfiwe.iihy.data.stop.StopList;
import com.sudfiwe.iihy.io.DBResult;
import com.sudfiwe.iihy.util.Filter;
import com.sudfiwe.iihy.util.Filter.FilterType;

public class StopListMapper extends AbstractFeedMapper<StopList> {

	public DBResult<ArrayList<StopList>> getList(Filter f) {

		DBResult<ArrayList<StopList>> ret=
			new DBResult<ArrayList<StopList>>();

		String url=AbstractFeedMapper.URL+"?"+
		AbstractFeedMapper.QUERY_COMMAND+"="+
		AbstractFeedMapper.VALUE_COMMAND_ROUTECONFIG;

		this.setAfterFirstQueryComponent(true);
		url=url+
		this.createQuery(
				f,FilterType.STOP_AGENCYTAG,
				AbstractFeedMapper.QUERY_AGENCY)+
				this.createQuery(f,FilterType.STOP_ROUTETAG,
						AbstractFeedMapper.QUERY_ROUTE);

		URL nextBusURL;
		try {
			nextBusURL=new URL(url);

			Document doc=this.getXMLFromURL(nextBusURL);

			Element root=doc.getDocumentElement();

			if (root.getTagName().equals("body")) {

				Hashtable<StopDirection,Stop[]> stopDirection=new Hashtable<StopDirection,Stop[]>();

				ArrayList<Stop> rawStops=new ArrayList<Stop>();

				NodeList bodyChildren=root.getElementsByTagName(
				"route");

				for (int bodyNodeIndex=0;bodyNodeIndex<
				bodyChildren.getLength();bodyNodeIndex++) {

					Element routeElement=
						(Element)bodyChildren.item(
								bodyNodeIndex);

					NodeList directionChildren=
						routeElement.getElementsByTagName("direction");

					for (int dirChildrenInd=0;
					dirChildrenInd<directionChildren.getLength();
					dirChildrenInd++) {

						stopDirection=getStopDirections(stopDirection,
								(Element)directionChildren.item(dirChildrenInd),f);
					}

					NodeList stopChildren=
						routeElement.getElementsByTagName("stop");

					for (int stopChildrenInd=0;
					stopChildrenInd<stopChildren.getLength();
					stopChildrenInd++) {

						if (((Element)stopChildren.item(stopChildrenInd)
								.getParentNode()).getTagName().equals("route")) {

							Stop test=getStop(
									(Element)stopChildren.item(stopChildrenInd),f);

							rawStops.add(test);
						}
					}
				}

				Stop[] rawStopsArr=
					rawStops.toArray(new Stop[rawStops.size()]);

				Arrays.sort(rawStopsArr);

				ArrayList<StopList> encap=new ArrayList<StopList>();

				encap.add
				(new StopList(organizeByDirection(stopDirection,
						rawStopsArr)));
				
				ret.setSuccess(true);
				ret.setObject(encap);
			}
		} catch (MalformedURLException e) {
			ret.setSuccess(false);
			ret.setMessage(e.getMessage());
		} catch (IOException e) {
			ret.setSuccess(false);
			ret.setMessage(e.getMessage());
		} catch (ParserConfigurationException e) {
			ret.setSuccess(false);
			ret.setMessage(e.getMessage());
		} catch (SAXException e) {
			ret.setSuccess(false);
			ret.setMessage(e.getMessage());
		}

		return ret;
	}

	private Hashtable<StopDirection,Stop[]> getStopDirections(
			Hashtable<StopDirection,Stop[]> table,
			Element directionElement,Filter f) {

		String tag=
			directionElement.getAttribute("tag");
		String title=
			directionElement.getAttribute("title");

		String name=
			directionElement.getAttribute("name");

		StopDirection direction=new StopDirection(tag,title,name);

		NodeList dirChildren=directionElement.getElementsByTagName(
		"stop");

		ArrayList<Stop> stops=new ArrayList<Stop>();

		for (int dirStopNodeIndex=0;dirStopNodeIndex<
		dirChildren.getLength();dirStopNodeIndex++) {

			Element stopElement=
				(Element)dirChildren.item(
						dirStopNodeIndex);

			stops.add(getStop(stopElement,f));
		}

		table.put(direction,stops.toArray(
				new Stop[stops.size()]));

		return table;
	}

	private Stop getStop(Element stopElement,Filter f) {

		String tag=
			stopElement.getAttribute("tag");
		String title=
			stopElement.getAttribute("title");
		String shortTitle=
			stopElement.getAttribute("shortTitle");
		String stopId=
			stopElement.getAttribute("stopId");

		return new Stop(tag,title,shortTitle,stopId,
				(String)f.getEntry(FilterType.STOP_AGENCYTAG),
				(String)f.getEntry(FilterType.STOP_ROUTETAG));
	}

	private Hashtable<StopDirection,Stop[]> organizeByDirection(
			Hashtable<StopDirection,Stop[]> barebones,
			Stop[] full) {

		Hashtable<StopDirection,Stop[]> ret=new Hashtable<StopDirection,Stop[]>();

		for (StopDirection stopDir:barebones.keySet()) {

			Stop[] bareList=barebones.get(stopDir);

			for (int stopIndex=0;stopIndex<bareList.length;
			stopIndex++) {

				int i=
					Arrays.binarySearch(full,new Stop(bareList[stopIndex].getTag()));

				if (i>=0&&i<full.length) {

					bareList[stopIndex].copyFrom(full[i]);
				}
			}

			ret.put(stopDir,bareList);
		}

		return ret;
	}

	public DBResult<StopList> getItem(Filter f) {

		DBResult<StopList> ret=new DBResult<StopList>();

		ret.setMessage("Feature not implemented.");

		return ret;
	}
}
