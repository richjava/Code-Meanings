package com.richjavalabs.codemeanings.custom.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.richjavalabs.codemeanings.custom.backend.CodeItem;
import com.richjavalabs.codemeanings.custom.backend.CodeItemEndpoint;

/**
 * Created by richard_lovell on 1/25/2015.
 */
public class UpdateCodeItemTask extends AsyncTask<Void, Void, CodeItem> {

    private Context context;
    private CodeItem codeItem;

    public UpdateCodeItemTask(CodeItem codeItem, Context context) {
        this.codeItem = codeItem;
        this.context = context;
    }

    @Override
    protected CodeItem doInBackground(Void... params) {
        CodeItemEndpoint endpoint = new CodeItemEndpoint();
        //Genymotion localhost ip = 192.168.56.1 (find this in VirtualBox File > Preferences > Network
        // > Edit host-only network
        endpoint.setRootUrl("http://192.168.56.1/CodeMeanings/");
        CodeItem codeItem = endpoint.update(this.codeItem);
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
