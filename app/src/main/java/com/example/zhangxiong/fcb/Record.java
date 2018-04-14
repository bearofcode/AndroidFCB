package com.example.zhangxiong.fcb;

import java.io.Serializable;

/**
 * Created by ZhangXiong on 2017/12/5.
 */

public class Record implements Serializable{
    private int rid;
    private String account;
    private String address;
    private String carnumber="";
    private String date;
    private int state;
    private double jindu;
    private double weidu;
    private byte[]photo;

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getJindu() {
        return jindu;
    }

    public void setJindu(double jindu) {
        this.jindu = jindu;
    }

    public double getWeidu() {
        return weidu;
    }

    public void setWeidu(double weidu) {
        this.weidu = weidu;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
