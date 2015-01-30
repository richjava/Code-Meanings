package com.richjavalabs.codemeanings.custom.backend;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard_lovell on 1/26/2015.
 */
public class EndpointHelper {

    private final String tag;
    private final String rootUrl;
    private final List<NameValuePair> params;

    private EndpointHelper(Builder builder) {
        this.tag = builder.tag;
        this.rootUrl = builder.rootUrl;
        this.params = builder.params;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public String getTag() {
        return tag;
    }

    public List<NameValuePair> getParams() {
        return params;
    }

    public static class Builder {

        private String tag;
        private String rootUrl;
        private List<NameValuePair> params = new ArrayList<NameValuePair>();


        public Builder(String tag, String rootUrl) {
            this.tag = tag;
            this.rootUrl = rootUrl;
            params.add(new BasicNameValuePair("tag", tag));
        }


        public Builder param(String key, String value) {
            params.add(new BasicNameValuePair(key, value));
            return this;
        }

        public EndpointHelper build() {
            return new EndpointHelper(this);
        }

    }
}
