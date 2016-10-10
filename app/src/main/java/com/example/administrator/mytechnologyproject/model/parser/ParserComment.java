package com.example.administrator.mytechnologyproject.model.parser;

import com.example.administrator.mytechnologyproject.model.BaseEntity;
import com.example.administrator.mytechnologyproject.model.Commenter;
import com.example.administrator.mytechnologyproject.model.ToComment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by 江鱼跃 on 2016/9/12.
 */
public class ParserComment {

    public static int getNuFoComment(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<Integer>>() {
        }.getType();
        BaseEntity<Integer> entity = gson.fromJson(json, type);
        return entity.getData();
    }
    public static List<Commenter> getCommentList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<List<Commenter>>>() {
        }.getType();
        BaseEntity<List<Commenter>> entity = gson.fromJson(json,type);
        return entity.getData();
    }
    public static String toCommentList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<ToComment>>() {
        }.getType();
        BaseEntity<ToComment> entity = gson.fromJson(json,type);
        String res = entity.getData().getExplain();
        return res;
    }


}
