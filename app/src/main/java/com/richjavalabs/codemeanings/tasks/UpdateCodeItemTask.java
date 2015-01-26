package com.richjavalabs.codemeanings.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.richjavalabs.backend.codeItemApi.CodeItemApi;
import com.richjavalabs.backend.codeItemApi.model.CodeItem;
import com.richjavalabs.codemeanings.CMApplication;

import java.io.IOException;

/**
 * Created by richard_lovell on 1/24/2015.
 */
public class UpdateCodeItemTask extends AsyncTask<Void, Void, CodeItem> {
    private static CodeItemApi apiService = null;
    private Context context;
    private CodeItem codeItem;

    public UpdateCodeItemTask(CodeItem codeItem, Context context) {
        this.codeItem = codeItem;
        this.context = context;
    }

    @Override
    protected CodeItem doInBackground(Void... params) {
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
        try {
            apiService.update(codeItem.getId(),codeItem).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return codeItem;
    }




    @Override
    protected void onPostExecute(CodeItem quote) {
        if(quote != null) {
            Toast.makeText(context, "Code Item updated", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "CodeItem is null!", Toast.LENGTH_LONG).show();
        }
    }
}