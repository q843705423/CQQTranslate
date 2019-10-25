package com.cqq.entity;

import java.util.List;

public class TranslateData {
    private String from;
    private String to;
    private List<DataUnit> trans_result;

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

    public List<DataUnit> getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(List<DataUnit> trans_result) {
        this.trans_result = trans_result;
    }
}
