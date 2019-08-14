package com.tian.translate.model;

import java.io.Serializable;
import java.util.List;

public class TranslateHttpSendResult implements Serializable {
    private String from;
    private String to;
    private String error_code;
    private String error_msg;
    private List<ResultDetail> trans_result;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<ResultDetail> getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(List<ResultDetail> trans_result) {
        this.trans_result = trans_result;
    }

    @Override
    public String toString() {
        return "TranslateHttpSendResult{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", trans_result=" + trans_result +
                '}';
    }
}
