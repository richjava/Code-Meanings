package com.richjavalabs.codemeanings.appengine.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.richjavalabs.backend.codeItemApi.CodeItemApi;
import com.richjavalabs.codemeanings.CMApplication;

import java.io.IOException;

/**
 * Created by richard_lovell on 1/24/2015.
 */
public class RemoveCodeItemTask extends AsyncTask<Void, Void, Boolean> {
    private static CodeItemApi apiService = null;
    private Context context;
    private long codeItemId;

    public RemoveCodeItemTask(long codeItemId, Context context) {
        this.codeItemId = codeItemId;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (apiService == null) {  // Only do this once
            //options for devappserver
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
            apiService = builder.build();
        }
        boolean isDeleted = false;
        try {
            apiService.remove(codeItemId).execute();
            isDeleted = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isDeleted;
    }




    @Override
    protected void onPostExecute(Boolean isDeleted) {
        if(isDeleted) {
            Toast.makeText(context, "Code Item deleted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "CodeItem is null!", Toast.LENGTH_LONG).show();
        }
    }
}