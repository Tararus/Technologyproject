package com.example.administrator.mytechnologyproject.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.mytechnologyproject.R;
import com.example.administrator.mytechnologyproject.base.MyBaseAdapter;
import com.example.administrator.mytechnologyproject.bean.MyImageLoader;
import com.example.administrator.mytechnologyproject.model.News;

/**
 * Created by 江鱼跃 on 2016/9/20.
 */
public class PhotoAdapter extends MyBaseAdapter <String>{

    public PhotoAdapter(Context context) {
        super(context);
    }

    @Override
    public View getMyView(int i, View view, ViewGroup parent) {
        ViewHolder vh;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_phone_item, null);
            vh = new ViewHolder();
            vh.iv_image = (ImageView)view.findViewById(R.id.iv_image);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        new MyImageLoader(context).display(getItem(i),vh.iv_image);
        return view;
    }

    private class ViewHolder {
        ImageView iv_image;
    }
}
