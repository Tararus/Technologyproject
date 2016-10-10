package com.example.administrator.mytechnologyproject.model;



/**
 * Created by 江鱼跃 on 2016/9/12.
 */
public class Commenter {
    private int cid;
    private String uid;
    private String portrait;
    private String stamp;
    private String content;

    @Override
    public String toString() {
        return "Commenter{" +
                "cid=" + cid +
                ", uid='" + uid + '\'' +
                ", portrait='" + portrait + '\'' +
                ", stamp='" + stamp + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public Commenter(int cid, String uid, String portrait, String stamp, String content) {
        this.cid = cid;
        this.uid = uid;
        this.portrait = portrait;
        this.stamp = stamp;
        this.content = content;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
