package com.example.administrator.mytechnologyproject.model.parser;

import com.example.administrator.mytechnologyproject.model.BaseEntity;
import com.example.administrator.mytechnologyproject.model.Password;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by 江鱼跃 on 2016/9/26.
 */

public class ParserPassword {
    public static Password setPassword(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<Password>>() {
        }.getType();
        BaseEntity<Password> entity = gson.fromJson(json, type);
        return entity.getData();
    }
}
