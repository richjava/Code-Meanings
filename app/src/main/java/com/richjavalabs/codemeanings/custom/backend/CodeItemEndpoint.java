package com.richjavalabs.codemeanings.custom.backend;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by richard_lovell on 1/25/2015.
 */
public class CodeItemEndpoint {


    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ for AVD http://10.0.2.3/ for genymotion to connect to localhost

    private static String rootUrl;

    private static String GET_TAG = "get";
    private final String INSERT_TAG = "insert";
    private final String UPDATE_TAG = "update";
    private final String REMOVE_TAG = "remove";
    private final String LIST_TAG = "list";


    public CodeItemEndpoint(){

    }

    public void setRootUrl(String rootUrl){
        this.rootUrl = rootUrl;
    }

    public CodeItem get(Long id){
        EndpointHelper eh = new EndpointHelper.Builder(GET_TAG, rootUrl)
                .param("id",id.toString())
                .build();
        return Mapper.map(getJsonObject(eh));
    }

    public CodeItem insert(CodeItem codeItem){
        EndpointHelper eh = new EndpointHelper.Builder(INSERT_TAG, rootUrl)
                .param("code",codeItem.getCode())
                .param("meaning", codeItem.getMeaning())
                .param("type", codeItem.getType())
                .build();
        return Mapper.map(getJsonObject(eh));
    }

    public CodeItem update(CodeItem codeItem){
        EndpointHelper eh = new EndpointHelper.Builder(UPDATE_TAG, rootUrl)
                .param("code",codeItem.getCode())
                .param("meaning", codeItem.getMeaning())
                .param("type", codeItem.getType())
                .build();
        return Mapper.map(getJsonObject(eh));
    }

    public void remove(Long id){
        new EndpointHelper.Builder(REMOVE_TAG, rootUrl)
                .param("id",id.toString())
                .build();
    }

    public List list(Integer limit){
        List<CodeItem> list = new ArrayList<>();
        EndpointHelper eh = new EndpointHelper.Builder(LIST_TAG, rootUrl)
                .param("limit", limit.toString())
                .build();
        JSONObject json = new JSONParser(eh).getJSONObject();
        return Mapper.mapList(json);
    }

    private JSONObject getJsonObject(EndpointHelper eh){
        JSONObject json = null;
        try {
            json = new JSONParser(eh).getJSONObject().getJSONObject("codeItem");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}