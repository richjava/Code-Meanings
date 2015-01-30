package com.richjavalabs.codemeanings.custom.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.richjavalabs.codemeanings.custom.backend.CodeItem;
import com.richjavalabs.codemeanings.custom.backend.CodeItemEndpoint;

/**
 * Created by richard_lovell on 1/25/2015.
 */
public class RemoveCodeItemTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private long codeItemId;

    public RemoveCodeItemTask(long codeItemId, Context context) {
        this.codeItemId = codeItemId;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        CodeItemEndpoint endpoint = new CodeItemEndpoint();
        //Genymotion localhost ip = 192.168.56.1 (find this in VirtualBox File > Preferences > Network
        // > Edit host-only network
        endpoint.setRootUrl("http://192.168.56.1/CodeMeanings/");
        boolean isDeleted = false;
        try {
            endpoint.remove(codeItemId);
            isDeleted = true;
        } catch (Exception e) {
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
