package se.rcdotnet.udacity.baking;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Recipe[]>,
        MainRecyclerAdapter.MainRecyclerAdapterClickHandler{
    RecyclerView mRecyclerMain;
    RecyclerView.LayoutManager mLayoutManager;
    MainRecyclerAdapter mAdapter;
    Recipe[] mRecipes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerMain = findViewById(R.id.main_recycler);
        mAdapter = new MainRecyclerAdapter(this,null,this);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerMain.setAdapter(mAdapter);
        mRecyclerMain.setLayoutManager(mLayoutManager);
        mRecyclerMain.addItemDecoration(new Decor());
        LoaderManager.LoaderCallbacks<Recipe[]> callbacks = this;
        getSupportLoaderManager().initLoader(100,null,callbacks);
    }
    @Override
    public Loader<Recipe[]> onCreateLoader(int id, Bundle args) {
        //Creating a loader to load data on a worker thread.
        // hide all other UI elements but show the progressbar.
        //mProgressBar.setVisibility(View.VISIBLE);
        //mRecyclerView.setVisibility(View.GONE);
        //mErrorTextView.setVisibility(View.GONE);
        Log.d("Loader","Loader created");
        return new AsyncTaskLoader<Recipe[]>(this) {
            Recipe[] mRecipes = null;
            @Override
            protected void onStartLoading() {
                // if we already have data just use it, otherways force the loading.
                if (mRecipes != null) deliverResult(mRecipes);
                else {
                    forceLoad();
                }
            }
            @Override
            public Recipe[] loadInBackground() {
                // Check the Internet connection,Load the data on a worker thread, and deliver it to the UI thread.
                // If missing internet connection or error happening during download, deliver null result and set the error text to the
                // appropriate error message.
                String data;
                Log.d("Loader","Loader loading");
                try {
                    //Checking internet connectivity as suggested in implementation guide pointing out stack owerflow post
                    Socket sock = new Socket();
                    sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
                    sock.close();
                    try{

                        data = NetworkU.getReservations();
                        //mReservations= new JsonParser().ParseReservations(data);
                        mRecipes = new JsonParser().Parse(data);
                    }
                    catch (Exception e)
                    {
                        // error during fetching data from the server
                        e.printStackTrace();
                        //mErrorText=getString(R.string.fetcherror);
                        mRecipes =null;
                        return null;
                    }
                    // check if the data is null, then there was an to get the data, f.ex. no api key.
                    if (mRecipes == null){
                        // we check if we got some erreor message from the server
                        if (!NetworkU.mStatus.equals("OK")){
                            //mErrorText=new JsonParser().ParseError(NetworkU.mStatus);
                            mRecipes =null;
                            return null;
                        }
                        else
                        {
                            //mErrorText="Unknown error";
                            mRecipes =null;
                            return null;
                        }
                    }
                    // All is OK, return the data
                    return mRecipes;
                } catch (IOException e) {
                    // there is no internet access return null.
                    //mErrorText=getString(R.string.nonetwork);
                    mRecipes =null;
                    return null;
                }
            }
            @Override
            public void deliverResult (Recipe[] m){
                // deliver the result to the UI thread
                Log.d("Loader", "Result delivered");
                mRecipes = m;
                super.deliverResult(m);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Recipe[]> loader, Recipe[] data) {
        // We got a result. If data is null, then something went wrong.
        // hide the progressbar and show the error TextView after the previously stored error text is set.
        if (data == null){
//            mErrorTextView.setText(mErrorText);
//            mErrorTextView.setVisibility(View.VISIBLE);
//            mProgressBar.setVisibility(View.GONE);
        }
        else {
            // all was OK, hide the progressbar and show the recycler.
//            mProgressBar.setVisibility(View.GONE);
//            mRecyclerView.setVisibility(View.VISIBLE);
           // t.setText(data);
            mAdapter.SwapData(data);
            mRecipes =data;
        }
        Log.d("Loader","Loader finished");
    }

    @Override
    public void onLoaderReset(Loader<Recipe[]> loader) {
        // logging loader reset for testing pourposes
        Log.d("Loader","Loader reset");
    }

    @Override
    public void onClick(int position) {
        Toast t = new Toast(this);
       // t.makeText(this,mRecipes[position].getName(),Toast.LENGTH_LONG).show();
        Intent startIntent = new Intent(this,StepsListActivity.class);
        startIntent.putExtra("Recipe", mRecipes[position]);
        startActivity(startIntent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int [] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,BakingWidget.class));
        Intent intent =new Intent(this,BakingWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra("RECIPE",mRecipes[position]);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIds);
    //    intent.setComponent(new ComponentName(this,BakingWidget.class));
         sendBroadcast(intent);
    }

    public class Decor extends RecyclerView.ItemDecoration {
        int mBottom = 10;
        int mLeft = 0;
        public Decor(){}
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.top = 10;
            outRect.bottom = 10;
            outRect.left = 2;
            outRect.right = 2;
        }
    }
}
