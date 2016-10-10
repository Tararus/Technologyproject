package com.example.administrator.mytechnologyproject.model;

/**
 * Created by 江鱼跃 on 2016/9/13.
 */
public class ToComment {
    private int result;
    private String explain;

    @Override
    public String toString() {
        return "ToComment{" +
                "explain='" + explain + '\'' +
                ", result=" + result +
                '}';
    }

    public ToComment(int result, String explain) {
        this.result = result;
        this.explain = explain;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
}
