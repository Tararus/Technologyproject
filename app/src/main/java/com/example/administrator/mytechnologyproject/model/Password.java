package com.example.administrator.mytechnologyproject.model;

/**
 * Created by 江鱼跃 on 2016/9/26.
 */

public class Password {
    private int result;
    private String explain;

    public Password(int result, String explain) {
        this.result = result;
        this.explain = explain;
    }

    @Override
    public String toString() {
        return "Password{" +
                "result=" + result +
                ", explain='" + explain + '\'' +
                '}';
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
