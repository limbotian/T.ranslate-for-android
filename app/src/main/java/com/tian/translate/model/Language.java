package com.tian.translate.model;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Language extends LitePalSupport implements Serializable {
    @Column
    private String code;
    @Column
    private String Name;
    @Column
    private int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "Language{" +
                "code='" + code + '\'' +
                ", Name='" + Name + '\'' +
                ", type=" + type +
                '}';
    }
}
