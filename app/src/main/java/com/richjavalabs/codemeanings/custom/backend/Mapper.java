package com.richjavalabs.codemeanings.custom.backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import roboguice.util.Ln;

/**
 * Created by richard_lovell on 1/25/2015.
 */
public class Mapper {

    public static CodeItem map(JSONObject jsonCodeItem){

        CodeItem codeItem = new CodeItem();
        try {
            codeItem.setId(Long.parseLong(jsonCodeItem.getString("id")));
            codeItem.setCode(jsonCodeItem.getString("code"));
            codeItem.setMeaning(jsonCodeItem.getString("meaning"));
            codeItem.setType(jsonCodeItem.getString("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return codeItem;
    }

    public static List<CodeItem> mapList(JSONObject json){
        Ln.d("json:"+json);
        List<CodeItem> codeItems = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = json.getJSONArray("codeItems");
            for(int i = 0; i < jsonArray.length(); i++){
                CodeItem codeItem = map(jsonArray.getJSONObject(i));
                codeItems.add(codeItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return codeItems;
    }
}
