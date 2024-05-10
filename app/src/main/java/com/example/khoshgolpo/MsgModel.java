package com.example.khoshgolpo;

public class MsgModel {
    String msg,senderId;
    long timeStamp;

    public MsgModel() {

    }

    public MsgModel(String msg, String senderId, long timeStamp) {
        this.msg = msg;
        this.senderId=senderId;
        this.timeStamp=timeStamp;

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }



    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
