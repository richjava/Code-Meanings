package com.richjavalabs.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
/**
 * Created by richard_lovell on 1/14/2015.
 */
@Entity
public class CodeItem {
    @Id
    private Long id;
    private String code;
    private String meaning;
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}