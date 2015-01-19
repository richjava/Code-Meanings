package com.richjavalabs.codemeanings.model;

import com.richjavalabs.backend.codeItemApi.model.CodeItem;

import java.util.ArrayList;
import java.util.List;

import roboguice.util.Ln;

/**
 * Created by richard_lovell on 1/16/2015.
 */
public class CodeItemManager {
    private static CodeItemManager instance;

    public static CodeItemManager getInstance() {
        if (instance == null) {
            instance = new CodeItemManager();
        }
        return instance;
    }

    public List<CodeItem> getCodeItems(){
        Ln.d("refreshing...");
        List<CodeItem> codeItems = new ArrayList<CodeItem>();

        CodeItem ci = new CodeItem();
        ci.setCode("foreach($day => $holidays){doHappy();}");
        ci.setMeaning("Happy holidays!");
        codeItems.add(ci);

        ci = new CodeItem();
        ci.setCode("<p>First paragraph</p><p>Second paragraph</p>");
        ci.setMeaning("My meaning 2");
        codeItems.add(ci);

        return codeItems;

    }
}
