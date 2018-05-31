package se.rcdotnet.udacity.baking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {

    static  Recipe mRecipe;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId ) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        // Set the remote adapter for the ListView
         //   views.setTextViewText(R.id.appwidget_text, mReceipe.getName() + " " + context.getString(R.string.appwidgetTitleText));
        if (mRecipe != null) {
            Intent serviceIntent = new Intent(context, WidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            Bundle b = new Bundle();
            b.putParcelable("RECIPE",mRecipe);
            serviceIntent.putExtra("DATA", b);
            views.setTextViewText(R.id.appwidget_text,mRecipe.getName());
            views.setRemoteAdapter(R.id.ingredients_listview, serviceIntent);
            Log.d("APPWIDGET", "Starting service with mRecipe" + mRecipe.toString());
        }
        // set onCLick
        Intent startintent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,startintent,0);
        views.setOnClickPendingIntent(R.id.widget_relative,pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        Log.d("APPWIDGET","updateAppWidget:" + String.valueOf(appWidgetId));
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            if (intent.hasExtra("RECIPE")) {
                mRecipe = intent.getParcelableExtra("RECIPE");
                AppWidgetManager man = AppWidgetManager.getInstance(context.getApplicationContext());
                ComponentName cn = new ComponentName(context.getApplicationContext(),BakingWidget.class);
                onUpdate(context,man,man.getAppWidgetIds(cn));
                //man.notifyAppWidgetViewDataChanged(man.getAppWidgetIds(cn),R.id.ingredients_listview);
            } else {
                mRecipe = null;
            }
        }
        Log.d("APPWIDGET","onReceive:" + intent.getAction());
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.d("APPWIDGETRECEIVER","Widgets updating");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
     //   super.onUpdate(context,appWidgetManager,appWidgetIds);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d("APPWIDGET","Widget enbled");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d("APPWIDGET","Widget disabled");
    }
}

