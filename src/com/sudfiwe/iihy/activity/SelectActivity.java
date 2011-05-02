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

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SelectActivity extends Activity {

	private enum SpinnerId {
		agency,
		route,
		direction,
		stop
	}

	private Spinner agencySpinner;
	private Spinner routeSpinner;
	private Spinner directionSpinner;
	private Spinner stopSpinner;

	private boolean hasFirstFiredAgency;
	private boolean hasFirstFiredRoute;
	private boolean hasFirstFiredDirection;
	private boolean hasFirstFiredStop;

	private boolean refreshing;

	ProgressDialog refreshProgressDialog;

	final Handler handler=new Handler();

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.select);

		agencySpinner=(Spinner)findViewById(R.id.agencySpinner);
		routeSpinner=(Spinner)findViewById(R.id.routeSpinner);
		directionSpinner=(Spinner)findViewById(R.id.directionSpinner);
		stopSpinner=(Spinner)findViewById(R.id.stopSpinner);

		((IsItHereYet)this.getParent()).setSelectActivity(this);

		agencySpinner.setOnItemSelectedListener(
				new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view,int pos,long id) {

						if (!refreshing&&hasFirstFiredAgency
								&&DataManager.getInstance().hasAgencyChanged(pos)) {

							setRoutes(pos);
						}

						hasFirstFiredAgency=true;
					}

					public void onNothingSelected(AdapterView<?> parent) {
						// Do nothing.
					}
				}
		);

		routeSpinner.setOnItemSelectedListener(
				new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view,int pos,long id) {

						if (!refreshing&&hasFirstFiredRoute
								&&DataManager.getInstance().hasRouteChanged(pos)) {

							setDirections(pos);
						}

						hasFirstFiredRoute=true;
					}

					public void onNothingSelected(AdapterView<?> parent) {
						// Do nothing.
					}
				}
		);

		directionSpinner.setOnItemSelectedListener(
				new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view,int pos,long id) {

						if (!refreshing&&hasFirstFiredDirection
								&&DataManager.getInstance().hasDirectionChanged(pos)) {
							
							setStops(pos);
						}

						hasFirstFiredDirection=true;
					}

					public void onNothingSelected(AdapterView<?> parent) {
						// Do nothing.
					}
				}
		);

		stopSpinner.setOnItemSelectedListener(
				new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view,int pos,long id) {

						if (!refreshing&&hasFirstFiredStop
								&&DataManager.getInstance().hasStopChanged(pos)) {
							
							setStop();	
						}

						hasFirstFiredStop=true;
					}

					public void onNothingSelected(AdapterView<?> parent) {
						// Do nothing.
					}
				}
		);

		((Button)findViewById(R.id.resultButton))
		.setOnClickListener(
				new OnClickListener() {

					public void onClick(View v) {

						displayPredictions();
					}
				}
		);
	}

	public void onStart() {

		super.onStart();

		load();
	}

	public void load() {

		setAgencies(true,true);
	}

	private void startRefresh() {

		refreshing=true;

		if (refreshProgressDialog==null) {
			refreshProgressDialog=ProgressDialog.show(
					this,"Loading","Please wait...",true,false);
		}
	}

	private void stopRefresh() {

		if (refreshProgressDialog!=null) {
			refreshProgressDialog.dismiss();
			refreshProgressDialog=null;
		}

		refreshing=false;
	}

	private void setAgencies() {

		setAgencies(false,false);
	}

	private void setAgencies(final boolean refreshList,
			final boolean setDefault) {

		startRefresh();
		
		final Activity act=this;

		new Thread(new Runnable() {
			public void run() {
				String[] titles=DataManager.getInstance()
				.getAgencyTitles(refreshList);

				handler.post(new SpinnerValueSetter(
						SpinnerId.agency,act,titles,setDefault));
			}
		}).start();
	}

	private void setAgencyAdaptor(ArrayAdapter<String>
	adapter,boolean setDefault) {

		agencySpinner.setAdapter(adapter);

		if (setDefault) {

			int index=DataManager.getInstance().getDefaultAgency();

			if (index>=0) {
				agencySpinner.setSelection(index);
			}
		}
	}

	private void setRoutes(final int agencyIndex) {

		setRoutes(agencyIndex,false);
	}

	private void setRoutes(final int agencyIndex,
			final boolean setDefault) {

		startRefresh();
		
		final Activity act=this;

		new Thread(new Runnable() {
			public void run() {
				String[] titles=DataManager.getInstance()
				.getRouteTitles(agencyIndex);

				handler.post(new SpinnerValueSetter(
						SpinnerId.route,act,titles,setDefault));
			}
		}).start();
	}

	private void setRouteAdaptor(ArrayAdapter<String>
	adapter,boolean setDefault) {

		routeSpinner.setAdapter(adapter);

		if (setDefault) {

			int index=DataManager.getInstance().getDefaultRoute();

			if (index>=0) {
				routeSpinner.setSelection(index);
			}
		}
	}

	private void setDirections(final int routeIndex) {

		setDirections(routeIndex,false);
	}

	private void setDirections(final int routeIndex,
			final boolean setDefault) {

		startRefresh();
		
		final Activity act=this;

		new Thread(new Runnable() {
			public void run() {
				String[] titles=DataManager.getInstance()
				.getDirectionTitles(routeIndex);

				handler.post(new SpinnerValueSetter(
						SpinnerId.direction,act,titles,setDefault));
			}
		}).start();
	}

	private void setDirectionAdaptor(ArrayAdapter<String>
	adapter,boolean setDefault) {

		directionSpinner.setAdapter(adapter);

		if (setDefault) {

			int index=DataManager.getInstance()
			.getDefaultDirection();

			if (index>=0) {
				directionSpinner.setSelection(index);
			}
		}
	}

	private void setStops(final int dirIndex) {

		setStops(dirIndex,false);
	}

	private void setStops(final int dirIndex,
			final boolean setDefault) {

		startRefresh();
		
		final Activity act=this;

		new Thread(new Runnable() {
			public void run() {
				String[] titles=DataManager.getInstance()
				.getStopTitles(dirIndex);

				handler.post(new SpinnerValueSetter(
						SpinnerId.stop,act,titles,setDefault));
			}
		}).start();
	}

	private void setStopAdaptor(ArrayAdapter<String>
	adapter,boolean setDefault) {

		stopSpinner.setAdapter(adapter);

		if (setDefault) {

			int index=DataManager.getInstance()
			.getDefaultStop();

			if (index>=0) {
				stopSpinner.setSelection(index);
			}
		}
		
		setStop();
	}

	public void setStop() {

		DataManager.getInstance().setStop(
				stopSpinner.getSelectedItemPosition());
	}

	private void displayPredictions() {

		((IsItHereYet)this.getParent()).showResultTab();
	}

	public class SpinnerValueSetter implements Runnable {

		private final SpinnerId id;
		private final Activity act;
		private final String[] titles;
		private final boolean setDefault;

		public SpinnerValueSetter(SpinnerId pSpn,
				Activity pAct,String[] pT) {

			id=pSpn;
			act=pAct;
			titles=pT;
			setDefault=false;
		}

		public SpinnerValueSetter(SpinnerId pSpn,
				Activity pAct,String[] pT,boolean pDef) {

			id=pSpn;
			act=pAct;
			titles=pT;
			setDefault=pDef;
		}

		public void run() {

			ArrayAdapter<String> adapter=new ArrayAdapter<String>(act,
					android.R.layout.simple_spinner_item,titles);

			if (id==SpinnerId.agency) {

				setAgencyAdaptor(adapter,setDefault);

				setRoutes(agencySpinner
						.getSelectedItemPosition(),
						setDefault);
			} else if (id==SpinnerId.route) {

				setRouteAdaptor(adapter,setDefault);

				setDirections(routeSpinner
						.getSelectedItemPosition(),
						setDefault);
			} else if (id==SpinnerId.direction) {

				setDirectionAdaptor(adapter,setDefault);

				setStops(directionSpinner
						.getSelectedItemPosition(),
						setDefault);
			} else if (id==SpinnerId.stop) {

				setStopAdaptor(adapter,setDefault);

				stopRefresh();
			}
		}
	}
}
