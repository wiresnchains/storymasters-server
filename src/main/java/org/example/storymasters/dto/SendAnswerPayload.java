package org.example.storymasters.dto;

public class SendAnswerPayload {
    private String as;
    private String wantTo;
    private String soThat;

    public SendAnswerPayload() {}

    public SendAnswerPayload(String as, String wantTo, String soThat) {
        this.as = as;
        this.wantTo = wantTo;
        this.soThat = soThat;
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }

    public String getWantTo() {
        return wantTo;
    }

    public void setWantTo(String wantTo) {
        this.wantTo = wantTo;
    }

    public String getSoThat() {
        return soThat;
    }

    public void setSoThat(String soThat) {
        this.soThat = soThat;
    }
}
