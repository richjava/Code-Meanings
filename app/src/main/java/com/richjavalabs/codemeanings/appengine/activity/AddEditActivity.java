package com.richjavalabs.codemeanings.appengine.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.mikpenz.iconics.IconicsDrawable;
import com.mikpenz.iconics.typeface.FontAwesome;
import com.richjavalabs.backend.codeItemApi.CodeItemApi;
import com.richjavalabs.backend.codeItemApi.model.CodeItem;
import com.richjavalabs.codemeanings.CMApplication;
import com.richjavalabs.codemeanings.R;
import com.richjavalabs.codemeanings.appengine.tasks.InsertCodeItemTask;
import com.richjavalabs.codemeanings.appengine.tasks.UpdateCodeItemTask;

import java.io.IOException;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

//use the codeItemApi CodeItem here, not the POJO

@ContentView(R.layout.activity_add_edit)
public class AddEditActivity extends RoboActionBarActivity {

    @InjectView (R.id.toolbar)Toolbar toolbar;
    @InjectView (R.id.fab_button)ImageButton fabButton;
    @InjectView (R.id.edit_code)EditText codeEdit;
    @InjectView (R.id.edit_meaning)EditText meaningEdit;
    @InjectView (R.id.spinner_type)Spinner typeSpinner;

    private long codeItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        //Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Fab Button
        fabButton.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_plus).color(Color.WHITE).actionBarSize());
        fabButton.setOnClickListener(fabClickListener);

        //Type Spinner
        String[] units = getResources().getStringArray(
                R.array.array_type);
        ArrayAdapter adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, units);
        typeSpinner.setAdapter(adapter);

        //check if edit
        codeItemId = getIntent().getLongExtra(MainActivity.ARG_CODEITEM_ID,-1);
        if(codeItemId != -1){
            fillEditData();
        }
    }

    private void fillEditData(){
        new GetCodeItemTask(this).execute();
    }

    public CodeItem getCodeItem(){
        CodeItem codeItem = new CodeItem();
        if(codeItemId!=-1){
            codeItem.setId(codeItemId);
        }
        codeItem.setCode(codeEdit.getText().toString());
        codeItem.setMeaning(meaningEdit.getText().toString());
        codeItem.setType(typeSpinner.getSelectedItem().toString());
        return codeItem;
    }

    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(codeItemId == -1){
                new InsertCodeItemTask(getCodeItem(),AddEditActivity.this).execute();
            }else{
                new UpdateCodeItemTask(getCodeItem(),AddEditActivity.this).execute();
            }

            Intent i = new Intent(AddEditActivity.this, MainActivity.class);
            startActivity(i);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    /*

     */
    private class GetCodeItemTask extends AsyncTask<Void, Void, CodeItem> {
        private ProgressDialog progressDialog;
        private Context context;
        private CodeItemApi myApiService = null;

        public GetCodeItemTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Retrieving Code Item...");
            progressDialog.show();
            super.onPreExecute();
        }


        protected CodeItem doInBackground(Void... unused) {
            CodeItem codeItem = null;
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
                codeItem = myApiService.get(codeItemId).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return codeItem;
        }

        @Override
        protected void onPostExecute(CodeItem codeItem) {
            progressDialog.dismiss();
            if(codeItem != null) {
                codeEdit.setText(codeItem.getCode());
                meaningEdit.setText(codeItem.getMeaning());
                typeSpinner.setSelection(1);
            }else{
                Ln.d("CodeItem is null");
            }
        }
    }
}
