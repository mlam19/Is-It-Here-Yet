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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import com.sudfiwe.iihy.R;
import com.sudfiwe.iihy.uiinterface.DataManager;
import com.sudfiwe.iihy.uiinterface.IPredictionDirection;
import com.sudfiwe.iihy.uiinterface.IPredictionItem;
import com.sudfiwe.iihy.uiinterface.IPredictions;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ResultActivity extends Activity {

	private TextView agencyName;
	private TextView routeName;
	private TextView directionName;
	private TextView stopName;
	private TextView summary;
	private TextView timeDisplay;
	private TextView message;
	private ListView itemList;

	private final int NOTIFY_DIALOG_INDEX=0;
	private final int NONOTIFY_DIALOG_INDEX=1;

	private AlertDialog notifyDialog;
	private AlertDialog noNotifyDialog;

	private final int YES_TIME=1;
	private final int YES_COLOUR=Color.GREEN;
	private final int SOON_TIME=5;
	private final int SOON_COLOUR=Color.YELLOW;
	private final int NO_COLOUR=Color.RED;

	private String stopNameText;
	
	private Calendar lastUpdate;
	private TimeEntry[] lastData;

	private TimeEntry selectedEntry;

	ProgressDialog refreshProgressDialog;

	private boolean shownOnceAlready;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.result);

		agencyName=((TextView)findViewById(R.id.agencyText));
		routeName=((TextView)findViewById(R.id.routeText));
		directionName=((TextView)findViewById(R.id.dirText));
		stopName=((TextView)findViewById(R.id.stopText));
		summary=((TextView)findViewById(R.id.summaryText));
		timeDisplay=((TextView)findViewById(R.id.timeText));
		message=((TextView)findViewById(R.id.msgText));

		itemList=((ListView)findViewById(R.id.itemList));
		itemList.setOnItemClickListener(
				new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent,View view,
							int position,long id) {

						chooseDialog(position);
					}
				});


		((Button)findViewById(R.id.refreshButton))
		.setOnClickListener(
				new OnClickListener() {

					public void onClick(View v) {

						displayResults();
					}
				}
		);

		shownOnceAlready=false;

		((IsItHereYet)this.getParent()).setResultActivity(this);
	}

	public void displayResults() {

		shownOnceAlready=true;

		refreshProgressDialog=ProgressDialog.show(
				this,"Loading","Please wait...",true,false);

		final Handler handler=new Handler();

		new Thread(new Runnable() {
			public void run() {

				handler.post(new DisplayResults(
						DataManager.getInstance()
						.getPredictions()));
			}
		}).start();
	}

	private void outputResults(IPredictions data) {

		DataManager dm=DataManager.getInstance();

		lastUpdate=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss a");
		timeDisplay.setText("Updated at "+sdf.format(lastUpdate.getTime()));

		agencyName.setText(dm.getAgencyName());	
		routeName.setText(dm.getRouteName());
		directionName.setText(dm.getDirectionName());
		
		stopNameText=dm.getStopName();
		stopName.setText(stopNameText);

		Hashtable<IPredictionDirection,IPredictionItem[]> list=data.getList();

		IPredictionDirection[] dir=data.getList()
		.keySet().toArray(new IPredictionDirection[data.getList().keySet().size()]);

		int numEntries=0;
		for (IPredictionDirection dirObj:dir) {

			numEntries=numEntries+list.get(dirObj).length;
		}

		lastData=new TimeEntry[numEntries];
		int index=0;
		for (IPredictionDirection dirObj:dir) {

			for (IPredictionItem item:list.get(dirObj)) {
				lastData[index]=new TimeEntry(item,dirObj);
				index++;
			}
		}

		Arrays.sort(lastData);

		String txt="No";
		int col=Color.RED;

		if (lastData.length>0) {

			int small=lastData[0].getData()
			.getMinuteDifference(lastUpdate);

			if (small<=YES_TIME) {
				txt="Yes!";
				col=YES_COLOUR;
			} else if (small<=SOON_TIME) {
				txt="Soon";
				col=SOON_COLOUR;
			}
		}

		summary.setText(txt);
		summary.setTextColor(col);

		message.setText(data.getMessage());

		itemList.setAdapter(new TimeEntryAdapter(
				this,R.layout.resultlistrow,lastData));

		refreshProgressDialog.dismiss();
	}

	public void showTab(boolean force) {

		if (!shownOnceAlready||force) {
			displayResults();
		}
	}

	private void chooseDialog(int position) {

		selectedEntry=lastData[position];

		int diff=selectedEntry.getData().
		getMinuteDifference(Calendar.getInstance());

		if (diff<=YES_TIME) {
			showDialog(NONOTIFY_DIALOG_INDEX);
		} else {
			showDialog(NOTIFY_DIALOG_INDEX);
		}
	}

	private AlertDialog createNotifyDialog() {

		LayoutInflater inflater=(LayoutInflater)
		this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout=inflater.inflate(R.layout.notifydialog,
				(ListView)findViewById(R.id.notifyRoot));

		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setView(layout);

		TextView text=(TextView)layout.findViewById(R.id.notifyText);
		text.setText(Html.fromHtml("Do you want to be notified when this ride arrives?"));

		builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				createNotification();
				notifyDialog.dismiss();
			}
		});

		builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				notifyDialog.dismiss();
			}
		});

		notifyDialog=builder.create();

		notifyDialog.setTitle("Add Notification?");

		notifyDialog.setIcon(R.drawable.ic_logo);

		return notifyDialog;
	}

	private void updateNotifyDialog(AlertDialog dialog,TimeEntry entry) {

		int diff=entry.getData().getMinuteDifference(Calendar.getInstance());

		TextView timeText=(TextView)dialog.findViewById(R.id.notifyTimeText);
		SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss a");
		timeText.setText(sdf.format(entry.getData().getArrival().getTime()));
		if (diff<=YES_TIME) {
			timeText.setTextColor(YES_COLOUR);
		} else if (diff<=SOON_TIME) {
			timeText.setTextColor(SOON_COLOUR);
		} else {
			timeText.setTextColor(NO_COLOUR);
		}

		TextView dirText=(TextView)dialog.findViewById(R.id.notifyDirectionText);
		dirText.setText(entry.getDestination().getTitle());
	}

	private void createNotification() {

		final String id=this.toString()+Calendar.getInstance().getTimeInMillis();

		final long pauseTime=selectedEntry.getData().getArrival().getTimeInMillis()-
		Calendar.getInstance().getTimeInMillis()-30000;
		
		String destination=selectedEntry.getDestination().getTitle();

		SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss a");
		String time=sdf.format(selectedEntry.getData().getArrival().getTime());

		final Notification.Builder builder=
			new Notification.Builder(this.getApplicationContext())
		.setAutoCancel(true)
		.setContentTitle(destination)
		.setContentText(stopNameText)
		.setWhen(selectedEntry.getData().getArrival().getTimeInMillis())
		.setSmallIcon(R.drawable.ic_note)
		.setTicker("The "+time+" ride to "+destination+" has arrived"+
				" at "+stopNameText+".");
		
		new Thread(new Runnable() {
			public void run() {
				
				try {
					Thread.sleep(pauseTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				NotificationManager notificationManager=
					(NotificationManager)getSystemService(
							NOTIFICATION_SERVICE);

				notificationManager.notify(id,id.hashCode(),
						builder.getNotification());
			}
		}).start();
	}

	private AlertDialog createNoNotifyDialog() {

		LayoutInflater inflater=(LayoutInflater)
		this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout=inflater.inflate(R.layout.notifydialog,
				(ListView)findViewById(R.id.notifyRoot));

		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setView(layout);

		builder.setNeutralButton("Back",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				noNotifyDialog.dismiss();
			}
		});

		noNotifyDialog=builder.create();

		noNotifyDialog.setTitle("Cannot Add Notification");

		noNotifyDialog.setIcon(R.drawable.ic_logo);

		return noNotifyDialog;
	}

	private void updateNoNotifyDialog(AlertDialog dialog,TimeEntry entry) {

		TextView text=(TextView)dialog.findViewById(R.id.notifyText);

		int diff=entry.getData().getMinuteDifference(Calendar.getInstance());

		String message="";

		if (diff<0) {

			message="This ride has already passed!";
		} else if (diff<=YES_TIME) {

			message="This ride is almost here.";
		}

		text.setText(Html.fromHtml(message));

		TextView timeText=(TextView)dialog.findViewById(R.id.notifyTimeText);
		SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss a");
		timeText.setText(sdf.format(entry.getData().getArrival().getTime()));
		if (diff<=YES_TIME) {
			timeText.setTextColor(YES_COLOUR);
		}

		TextView dirText=(TextView)dialog.findViewById(R.id.notifyDirectionText);
		dirText.setText(entry.getDestination().getTitle());
	}

	private class TimeEntry implements Comparable<TimeEntry> {

		private IPredictionItem data;
		private IPredictionDirection destination;

		public TimeEntry(IPredictionItem pData,
				IPredictionDirection pDest) {

			data=pData;
			destination=pDest;
		}

		public IPredictionItem getData() {
			return data;
		}

		public IPredictionDirection getDestination() {
			return destination;
		}

		public int compareTo(TimeEntry another) {

			return data.getArrival().compareTo(
					another.data.getArrival());
		}
	}

	private class TimeEntryAdapter extends ArrayAdapter<TimeEntry> {

		private TimeEntry[] items;
		private int rowId;

		public TimeEntryAdapter(Context pCtx,
				int pId,TimeEntry[] pItems) {

			super(pCtx,pId,pItems);
			rowId=pId;
			items=pItems;
		}

		public View getView(int position,View convertView,
				ViewGroup parent) {

			View v=convertView;

			if (v==null) {

				LayoutInflater vi=(LayoutInflater)getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				v=vi.inflate(rowId,null);
			}

			TimeEntry entry=items[position];

			if (position%2==0) {
				v.setBackgroundColor(Color.rgb(40,40,40));
			} else {
				v.setBackgroundColor(Color.rgb(15,15,15));
			}

			int min=entry.getData()
			.getMinuteDifference(lastUpdate);
			if (min<0) {
				min=0;
			}

			String minTxt=" minute";
			if (min!=1) {
				minTxt=minTxt+"s";
			}
			int minCol=NO_COLOUR;
			if (min<=YES_TIME) {
				minCol=YES_COLOUR;
			} else if (min<=SOON_TIME) {
				minCol=SOON_COLOUR;
			}

			TextView rowTimeText=
				((TextView)v.findViewById(R.id.rowTimeText));
			rowTimeText.setText(min+"");
			rowTimeText.setTextColor(minCol);

			TextView rowMinuteText=
				((TextView)v.findViewById(R.id.rowMinuteText));
			rowMinuteText.setText(minTxt);
			rowMinuteText.setTextColor(minCol);

			((TextView)v.findViewById(R.id.rowVehicleText))
			.setText("Vehicle "+entry.getData().getVehicle());

			((TextView)v.findViewById(R.id.rowDestinationText))
			.setText(entry.getDestination().getTitle());

			return v;
		}
	}

	public class DisplayResults implements Runnable {

		private IPredictions data;

		public DisplayResults(IPredictions pData) {

			data=pData;
		}

		public void run() {

			outputResults(data);
		}
	}

	protected Dialog onCreateDialog(int id,Bundle bundle) {

		Dialog dialog=null;

		if (id==NOTIFY_DIALOG_INDEX) {

			dialog=createNotifyDialog();
		} else if (id==NONOTIFY_DIALOG_INDEX) {

			dialog=createNoNotifyDialog();
		}

		return dialog;
	}

	protected void onPrepareDialog(int id,Dialog dialog,Bundle bundle) {

		if (id==NOTIFY_DIALOG_INDEX) {

			updateNotifyDialog((AlertDialog)dialog,selectedEntry);
		} else if (id==NONOTIFY_DIALOG_INDEX) {

			updateNoNotifyDialog((AlertDialog)dialog,selectedEntry);
		}
	}
}
