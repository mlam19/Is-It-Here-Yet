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
import java.util.Calendar;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.sudfiwe.iihy.data.predict.Prediction;
import com.sudfiwe.iihy.data.predict.PredictionDirection;
import com.sudfiwe.iihy.data.predict.PredictionList;
import com.sudfiwe.iihy.io.DBResult;
import com.sudfiwe.iihy.uiinterface.IPredictionDirection;
import com.sudfiwe.iihy.uiinterface.IPredictionItem;
import com.sudfiwe.iihy.util.Filter;
import com.sudfiwe.iihy.util.Filter.FilterType;

public class PredictionListMapper extends AbstractFeedMapper<PredictionList> {

	public DBResult<PredictionList> getList(Filter f) {

		DBResult<PredictionList> ret=
			new DBResult<PredictionList>();

		String url=AbstractFeedMapper.URL+"?"+
		AbstractFeedMapper.QUERY_COMMAND+"="+
		AbstractFeedMapper.VALUE_COMMAND_PREDICT;

		this.setAfterFirstQueryComponent(true);
		url=url+
		this.createQuery(f,FilterType.PREDICT_AGENCYTAG,
				AbstractFeedMapper.QUERY_AGENCY)+
				this.createQuery(f,FilterType.PREDICT_ROUTETAG,
						AbstractFeedMapper.QUERY_ROUTE)+
						this.createQuery(f,FilterType.PREDICT_STOPTAG,
								AbstractFeedMapper.QUERY_STOP);

		URL nextBusURL;;
		try {
			
			Calendar requestTime=Calendar.getInstance();
			
			nextBusURL=new URL(url);

			Document doc=this.getXMLFromURL(nextBusURL);

			Element root=doc.getDocumentElement();

			if (root.getTagName().equals("body")) {

				Hashtable<IPredictionDirection,IPredictionItem[]> table=new Hashtable<IPredictionDirection,IPredictionItem[]>();

				String message="";

				NodeList bodyChildren=root.getElementsByTagName(
				"predictions");

				for (int bodyNodeIndex=0;bodyNodeIndex<
				bodyChildren.getLength();bodyNodeIndex++) {

					Element routeElement=
						(Element)bodyChildren.item(
								bodyNodeIndex);

					NodeList routeDirChildren=
						routeElement.getElementsByTagName(
						"direction");

					for (int routeDirNodeIndex=0;routeDirNodeIndex<
					routeDirChildren.getLength();routeDirNodeIndex++) {

						Element dirElement=
							(Element)routeDirChildren.item(
									routeDirNodeIndex);

						String title=
							dirElement.getAttribute("title");

						PredictionDirection predictDir=
							new PredictionDirection(title);

						NodeList dirChildren=dirElement.getElementsByTagName(
						"prediction");

						ArrayList<Prediction> predictItems=
							new ArrayList<Prediction>();

						for (int dirStopNodeIndex=0;dirStopNodeIndex<
						dirChildren.getLength();dirStopNodeIndex++) {

							Element predictElement=
								(Element)dirChildren.item(
										dirStopNodeIndex);

							String dirTag=predictElement.getAttribute("dirTag");

							boolean doIt=true;
							if (f.contains(FilterType.PREDICT_DIRTAG)&&
									!f.getEntry(FilterType.PREDICT_DIRTAG).equals(dirTag)) {
								doIt=false;
							}

							if (doIt) {
								
								int minutes=Integer.parseInt(
										predictElement.getAttribute("minutes"));
								
								Calendar arrival=(Calendar)requestTime.clone();
								arrival.add(Calendar.MINUTE,minutes);
								
								boolean isDeparture=Boolean.parseBoolean(
										predictElement.getAttribute("isDeparture"));
								String vehicle=predictElement.getAttribute("vehicle");

								predictItems.add(new Prediction(arrival,
										isDeparture,vehicle));
							}
						}

						if (predictItems.size()>0) {
							table.put(predictDir,predictItems.toArray(
									new Prediction[predictItems.size()]));
						}
					}

					PredictionList list=new PredictionList(table,
							message);

					ret.setSuccess(true);
					ret.setObject(list);
				}
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

	public DBResult<PredictionList> getItem(Filter f) {

		DBResult<PredictionList> ret=new DBResult<PredictionList>();

		ret.setMessage("Feature not implemented.");

		return ret;
	}
}
