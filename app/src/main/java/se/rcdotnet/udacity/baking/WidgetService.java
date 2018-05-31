package se.rcdotnet.udacity.baking;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

public class WidgetService extends RemoteViewsService {
    Recipe mRecipe;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        if (intent.hasExtra("RECIPE")) {
            mRecipe = intent.getParcelableExtra("RECIPE");

        } else {
            mRecipe = null;
        }
        Log.d("WIDGETSERVICE", "onGetfactory" + intent.toString()+intent.getExtras().toString());
        return (new ViewFactory(this.getApplicationContext(),intent));
    }

}

class ViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String[] items= { "lorem", "ipsum", "dolor",

            "sit", "amet", "consectetuer", "adipiscing", "elit", "morbi",

            "vel", "ligula", "vitae", "arcu", "aliquet", "mollis", "etiam",

            "vel", "erat", "placerat", "ante", "porttitor", "sodales",

            "pellentesque", "augue", "purus" };
    Context mContext;
    int mWidgetId;
    Recipe mRecipe;

    public  ViewFactory (Context c, Intent intent){
        mContext = c;
        Bundle b = intent.getBundleExtra("DATA");
        mRecipe = b.getParcelable("RECIPE");
        Log.d("WIDGETSERVICE", "bundle:"+b.toString());
        mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);

        if (mRecipe == null){
            Log.d("WIDGETSERVICE", "Created factory with mRecipe null " +intent.toString());
        }
        else {
            Log.d("WIDGETSERVICE", "Created factory with mRecipe :" + mRecipe.toString()+" "+intent.toString());
        }
    }

    @Override
    public void onCreate() {
        int a=0;
        a=1;

    }

    @Override
    public void onDataSetChanged() {
        Log.d("WIDGETFACTORY", "onDataChanged, recipe:" + mRecipe.toString());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
//        return items.length;
       if (mRecipe == null)return  0;
       Log.d("WIDGETSERVICE","Count:" + String.valueOf(mRecipe.getIngredients().size()));
       return mRecipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d("WIDGETFACTORY", "Creating view" + String.valueOf(position));
        if (mRecipe != null) Log.d("WIDGETFACTORY", "recipe:" + mRecipe.toString());
        ArrayList<Ingredient> ingredients = mRecipe.getIngredients();
        RemoteViews item = new RemoteViews(mContext.getPackageName(),R.layout.w_baking_ingrediens_item_layout);
        item.setTextViewText(R.id.Ingredient_name_TextView,ingredients.get(position).getIngredient()+ " "+
        ingredients.get(position).getQuantity()+" " +
        ingredients.get(position).getMeasure());
     //   item.setTextViewText(R.id.Ingredient_name_TextView,items[position]);
        return item;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
