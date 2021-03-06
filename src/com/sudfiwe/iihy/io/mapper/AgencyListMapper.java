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
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.sudfiwe.iihy.data.agency.Agency;
import com.sudfiwe.iihy.io.DBResult;
import com.sudfiwe.iihy.util.Filter;

public class AgencyListMapper extends AbstractFeedMapper<Agency> {

	public DBResult<ArrayList<Agency>> getList(Filter f) {

		DBResult<ArrayList<Agency>> ret=
			new DBResult<ArrayList<Agency>>();

		String url=AbstractFeedMapper.URL+"?"+
		AbstractFeedMapper.QUERY_COMMAND+"="+
		AbstractFeedMapper.VALUE_COMMAND_AGENCYLIST;

		URL nextBusURL;
		try {
			nextBusURL=new URL(url);

			Document doc=this.getXMLFromURL(nextBusURL);

			Element root=doc.getDocumentElement();

			if (root.getTagName().equals("body")) {

				ArrayList<Agency> list=new ArrayList<Agency>();

				NodeList children=root.getElementsByTagName(
						"agency");

				for (int currNode=0;currNode<children.getLength();currNode++) {

					Element agencyElement=
						(Element)children.item(currNode);

					String tag=
						agencyElement.getAttribute("tag");
					String title=
						agencyElement.getAttribute("title");
					String shortTitle=
						agencyElement.getAttribute("shortTitle");
					String regionTitle=
						agencyElement.getAttribute("regionTitle");

					list.add(new Agency(tag,title,
							shortTitle,regionTitle));
				}

				ret.setSuccess(true);
				ret.setObject(list);
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

	public DBResult<Agency> getItem(Filter f) {

		DBResult<Agency> ret=new DBResult<Agency>();

		ret.setMessage("Feature not implemented.");

		return ret;
	}
}
