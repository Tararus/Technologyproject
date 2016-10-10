package com.example.administrator.mytechnologyproject.model.parser;

import com.example.administrator.mytechnologyproject.model.BaseEntity;
import com.example.administrator.mytechnologyproject.model.Update;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by 江鱼跃 on 2016/9/14.
 */
public class ParserUpdate {

    public static BaseEntity<Update> getUpdte(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<Update>>() {
        }.getType();
        BaseEntity<Update> entity = gson.fromJson(json, type);
        return entity;
    }
}
