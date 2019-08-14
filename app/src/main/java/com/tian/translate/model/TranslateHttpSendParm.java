package com.tian.translate.model;

import java.io.Serializable;

public class TranslateHttpSendParm implements Serializable {
    private String q;
    private String from;
    private String to;
    private String appid;
    private String salt;
    private String sign;

    public TranslateHttpSendParm(String q, String from, String to) {
        this.q = q;
        this.from = from;
        this.to = to;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSalt() {
        return String.valueOf(System.currentTimeMillis());
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
