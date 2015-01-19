package com.richjavalabs.codemeanings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.mikpenz.iconics.IconicsDrawable;
import com.mikpenz.iconics.typeface.FontAwesome;

import com.richjavalabs.backend.codeItemApi.CodeItemApi;
import com.richjavalabs.backend.codeItemApi.model.CodeItem;
import com.richjavalabs.backend.codeItemApi.CodeItemApi;
import com.richjavalabs.backend.codeItemApi.model.CodeItem;
import com.richjavalabs.backend.codeItemApi.model.CollectionResponseCodeItem;
import com.richjavalabs.codemeanings.adapter.CodeItemAdapter;
import com.richjavalabs.codemeanings.model.CodeItemManager;
import com.richjavalabs.codemeanings.tasks.InsertCodeItemTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActionBarActivity {
    private List<CodeItem> codeItemList = new ArrayList<CodeItem>();
    private CodeItemAdapter adapter;
    @InjectView(R.id.recycler_code_items)RecyclerView recyclerView;
    @InjectView (R.id.swipe_container)SwipeRefreshLayout swipeRefreshLayout;
    @InjectView (R.id.toolbar)Toolbar toolbar;
    @InjectView (R.id.fab_button)ImageButton fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        //Recylcer
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CodeItemAdapter(new ArrayList<CodeItem>(), R.layout.row_code_item, this);
        recyclerView.setAdapter(adapter);

        // Fab Button
        fabButton.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_plus).color(Color.WHITE).actionBarSize());
        fabButton.setOnClickListener(fabClickListener);

        //Swipe refresh
        Resources res = getResources();
        swipeRefreshLayout.setColorSchemeColors(res.getColor(R.color.theme_accent),res.getColor(R.color.yellow),res.getColor(R.color.theme_default_primary),res.getColor(R.color.theme_default_primary_dark));
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //new InitCodeItemsTask(MainActivity.this).execute();
                new InitCodeItemsTask(MainActivity.this).execute();
            }
        });
        new InitCodeItemsTask(this).execute();
        //new InitializeCodeItemsTask().execute();
        //new InsertCodeItemTask(this).execute();
    }

    private Bitmap getTransparentBitmapCopy(Bitmap source)
    {
        int width =  source.getWidth();
        int height = source.getHeight();
        Bitmap copy = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] pixels = new int[width * height];
        source.getPixels(pixels, 0, width, 0, 0, width, height);
        copy.setPixels(pixels, 0, width, 0, 0, width, height);
        return copy;
    }

    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,
                    AddEditActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void animateActivity(CodeItem codeItem, View appIcon) {
        Ln.d("on click!");
       // Intent i = new Intent(this, DetailActivity.class);
       // i.putExtra("codeItemId", codeItem.getId());

        //ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create((View) fabButton, "fab"), Pair.create(appIcon, "appIcon"));
        //startActivity(i, transitionActivityOptions.toBundle());
    }

    private class InitializeCodeItemsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            adapter.clearCodeItems();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            codeItemList.clear();

            //Query the applications
            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            codeItemList = CodeItemManager.getInstance().getCodeItems();
//            List<ResolveInfo> ril = getPackageManager().queryIntentActivities(mainIntent, 0);
//            for (ResolveInfo ri : ril) {
//                codeItemList.add(new CodeItem());
//            }
            //Collections.sort(codeItemList);

//            for (CodeItem codeItem : codeItemList) {
//                //load icons before shown. so the list is smoother
//                appInfo.getIcon();
//            }

            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            adapter.addCodeItems(codeItemList);
            swipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }

    public void addCodeItem(View v) {

    }


    private class InitCodeItemsTask extends AsyncTask<Void, Void, CollectionResponseCodeItem> {
        private ProgressDialog progressDialog;
        private Context context;
        private CodeItemApi myApiService = null;

        public InitCodeItemsTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Retrieving Code Items...");
            progressDialog.show();
            adapter.clearCodeItems();
            super.onPreExecute();
        }


        protected CollectionResponseCodeItem doInBackground(Void... unused) {
            CollectionResponseCodeItem codeItems = null;
            //try {

                CodeItemApi.Builder builder = new CodeItemApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver


                        //genymotion
                        .setRootUrl("http://10.0.3.2:8080/_ah/api/")
                        //android emulator
                        //.setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setApplicationName("global-matrix-825")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                //end options for devappserver

                //for deployed backend server
                // QuoteEndpoint.Builder builder = new QuoteEndpoint.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null);

                //builder.setApplicationName("HelloCloudEndpoints");
                myApiService = builder.build();


                try {
                    codeItems = myApiService.list().execute();
                    //myApiService.insertQuote(quote).execute();


                } catch (IOException e) {
                    e.printStackTrace();


                }


//                CodeItemApi.Builder builder = new CodeItemApi.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
//                builder.setRootUrl("http://10.0.2.2:8080/_ah/api/");
//                CodeItemApi service =  builder.build();
//                codeItems = service.list().execute();
//            } catch (Exception e) {
//                Ln.d("Could not retrieve Code Items: "+e.getMessage());
//            }
            return codeItems;
        }


//        @Override
//        protected Void doInBackground(Void... params) {
//            codeItemList.clear();
//
//            //Query the applications
//            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//
////            List<CodeItem> codeItems = //getPackageManager().queryIntentActivities(mainIntent, 0);
////            for (CodeItem codeItem : codeItem) {
////                codeItemList.add(new AppInfo(MainActivity.this, ri));
////            }
////            Collections.sort(applicationList);
//
////            for (AppInfo appInfo : applicationList) {
////                //load icons before shown. so the list is smoother
////                appInfo.getIcon();
////            }
//
//            return null;
//        }

        @Override
        protected void onPostExecute(CollectionResponseCodeItem codeItems) {
            progressDialog.dismiss();

            ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
            if(codeItems != null) {
                codeItemList = codeItems.getItems();
            }else{
                Ln.d("CollectionResponseCodeItem CodeItems is null");
            }
 //           List<CodeItem> _list = codeItems.getItems();
//            for (CodeItem codeItem : _list) {
//                HashMap<String, String> item = new HashMap<String, String>();
//                item.put("id", Long.toString(codeItem.getId()));
//                item.put("code", codeItem.getCode());
//                item.put("meaning", codeItem.getMeaning());
//                item.put("type", codeItem.getType());
//                list.add(item);
//            }
            //adapter = new SimpleAdapter(QuotesListActivity.this, list,android.R.layout.simple_list_item_2, from, to);
            //setListAdapter(adapter);


            //handle visibility
            recyclerView.setVisibility(View.VISIBLE);

            //set data for list
            adapter.addCodeItems(codeItemList);
            swipeRefreshLayout.setRefreshing(false);

            //super.onPostExecute(result);
        }
    }
}


