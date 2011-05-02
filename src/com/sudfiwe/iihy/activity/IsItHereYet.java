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

package com.sudfiwe.iihy.activity;

import com.sudfiwe.iihy.R;
import com.sudfiwe.iihy.uiinterface.DataManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class IsItHereYet extends TabActivity {

	protected static final int SELECT_TAB_INDEX=0;
	protected static final int RESULT_TAB_INDEX=1;

	private final String SELECT_TAB_TAG="selectTab";
	private final String RESULT_TAB_TAG="resultTab";

	private SelectActivity selectActivity;
	private ResultActivity resultActivity;

	private final int ABOUT_DIALOG_INDEX=0;

	private AlertDialog aboutDialog;

	private TabHost tabHost;

	private boolean updateResults;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tabHost=getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		DataManager.getInstance().setActivity(this);

		intent=new Intent().setClass(this,SelectActivity.class);

		spec=tabHost.newTabSpec(SELECT_TAB_TAG)
		.setIndicator("Choose A Stop").setContent(intent);
		tabHost.addTab(spec);

		intent=new Intent().setClass(this,ResultActivity.class);
		spec=tabHost.newTabSpec(RESULT_TAB_TAG)
		.setIndicator("Is It Here Yet?").setContent(intent);
		tabHost.addTab(spec);

		tabHost.setOnTabChangedListener(
				new OnTabChangeListener() {

					public void onTabChanged(String tabId) {

						if (tabId.equals(RESULT_TAB_TAG)) {

							resultActivity.showTab(updateResults);
						}
					}
				}
		);
	}

	public void showSelectTab() {

		getTabHost().setCurrentTab(IsItHereYet.SELECT_TAB_INDEX);
	}

	public void showResultTab() {

		updateResults=true;

		getTabHost().setCurrentTab(IsItHereYet.RESULT_TAB_INDEX);

		updateResults=false;
	}

	public void setSelectActivity(SelectActivity act) {

		selectActivity=act;
	}

	public void setResultActivity(ResultActivity act) {

		resultActivity=act;
	}

	public AlertDialog createAboutDialog() {

		if (aboutDialog==null) {

			LayoutInflater inflater=(LayoutInflater)
			this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout=inflater.inflate(R.layout.aboutdialog,
					(ListView)findViewById(R.id.aboutRoot));

			TextView text=(TextView)layout.findViewById(R.id.aboutText);
			text.setText(Html.fromHtml("<b>©2011, Matthew Lam.</b><br/>"+
					"<b><a href='http://www.reservedbits.com/javawork/isithereyet'>Homepage</a></b><br/>"+
					"Data provided by <b><a href='http://www.nextbus.com/homepage/'>NextBus</a></b>® public XML feed."));
			text.setMovementMethod(LinkMovementMethod.getInstance());
			
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   aboutDialog.dismiss();
		           }
		       });
			aboutDialog=builder.create();
			aboutDialog.setTitle("About Is It Here Yet");
			aboutDialog.setIcon(R.drawable.ic_logo);
		}


		return aboutDialog;
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.menu,menu);
		return true;
	}

	protected Dialog onCreateDialog(int id) {

		Dialog dialog=null;

		if (id==ABOUT_DIALOG_INDEX) {

			dialog=createAboutDialog();
		}

		return dialog;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		int id=item.getItemId();

		if (id==R.id.refreshMenu) {

			selectActivity.load();
			showSelectTab();
		} else if (id==R.id.saveMenu) {

			DataManager.getInstance().savePreferences();
		} else if (id==R.id.aboutMenu) {

			showDialog(ABOUT_DIALOG_INDEX);
		} else if (id==R.id.exitMenu) {

			finish();
		}

		return true;
	}
}