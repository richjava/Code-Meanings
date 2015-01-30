package com.richjavalabs.codemeanings.appengine.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.mikpenz.iconics.IconicsDrawable;
import com.mikpenz.iconics.typeface.FontAwesome;

import com.richjavalabs.backend.codeItemApi.CodeItemApi;
import com.richjavalabs.backend.codeItemApi.model.CodeItem;
import com.richjavalabs.backend.codeItemApi.model.CollectionResponseCodeItem;
import com.richjavalabs.codemeanings.CMApplication;
import com.richjavalabs.codemeanings.R;
import com.richjavalabs.codemeanings.appengine.adapter.CodeItemAdapter;
import com.richjavalabs.codemeanings.appengine.tasks.RemoveCodeItemTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActionBarActivity {
    private List<CodeItem> codeItemList = new ArrayList<>();
    private CodeItemAdapter adapter;
    private ActionMode mActionMode;

    @InjectView(R.id.recycler_code_items)RecyclerView recyclerView;
    @InjectView (R.id.swipe_container)SwipeRefreshLayout swipeRefreshLayout;
    @InjectView (R.id.toolbar)Toolbar toolbar;
    @InjectView (R.id.fab_button)ImageButton fabButton;

    public static final String ARG_CODEITEM_ID = "codeitem_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        //Recycler
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
                new InitCodeItemsTask(MainActivity.this).execute();
            }
        });
        new InitCodeItemsTask(this).execute();
    }

    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, AddEditActivity.class);
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,  fabButton, "fab");
            startActivity(i, transitionActivityOptions.toBundle());
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void animateAddEditTransition(long id) {
        Intent i = new Intent(this, AddEditActivity.class);
        i.putExtra(ARG_CODEITEM_ID, id);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,  fabButton, "fab");
         startActivity(i, transitionActivityOptions.toBundle());
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
                CodeItemApi.Builder builder = new CodeItemApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)

                        //genymotion
                        .setRootUrl("http://10.0.3.2:8080/_ah/api/")
                        //android emulator
                        //.setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setApplicationName(CMApplication.APP_ENGINE_APP_NAME)
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                myApiService = builder.build();
                try {
                    codeItems = myApiService.list().execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return codeItems;
        }

        @Override
        protected void onPostExecute(CollectionResponseCodeItem codeItems) {
            progressDialog.dismiss();
            if(codeItems != null) {
                codeItemList = codeItems.getItems();
            }else{
                Ln.d("CollectionResponseCodeItem CodeItems is null");
            }
            //handle visibility
            recyclerView.setVisibility(View.VISIBLE);

            //set data for list
            adapter.addCodeItems(codeItemList);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /*
    Called from CodeItemAdapter onLongClickListener
     */
    public void startActionMode(View view){
        // Start the CAB using the ActionMode.Callback defined below
        mActionMode = startActionMode(mActionModeCallback);
        view.setActivated(true);
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.actionmode_crud, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_edit:
                    animateAddEditTransition(adapter.getCodeItemId());
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.item_delete:
                    new RemoveCodeItemTask(adapter.getCodeItemId(),MainActivity.this).execute();

                    adapter.delete();//delete phase
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };
}


