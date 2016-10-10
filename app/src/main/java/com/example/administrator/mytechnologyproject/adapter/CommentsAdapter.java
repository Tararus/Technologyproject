package com.example.administrator.mytechnologyproject.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.mytechnologyproject.R;
import com.example.administrator.mytechnologyproject.base.MyBaseAdapter;
import com.example.administrator.mytechnologyproject.bean.MyImageLoader;
import com.example.administrator.mytechnologyproject.model.Commenter;

/**
 * Created by 江鱼跃 on 2016/9/12.
 */
public class CommentsAdapter extends MyBaseAdapter<Commenter>{

    public CommentsAdapter(Context context) {
        super(context);
    }

    @Override
    public View getMyView(int i, View view, ViewGroup parent) {
        ViewHolder vh;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_comment_item, null);
            vh = new ViewHolder();
            vh.tv_uid = (TextView) view.findViewById(R.id.tv_uid);
            vh.tv_stamp = (TextView) view.findViewById(R.id.tv_stamp);
            vh.tv_content = (TextView) view.findViewById(R.id.tv_content);
            vh.im_portrait = (ImageView) view.findViewById(R.id.im_portrait);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        Commenter commenter = getItem(i);
        vh.tv_uid.setText(commenter.getUid());
        vh.tv_stamp.setText(commenter.getStamp());
        vh.tv_content.setText(commenter.getContent());
        new MyImageLoader(context).display(getItem(i).getPortrait(),vh.im_portrait);
        return view;
    }

    private class ViewHolder {
        TextView tv_uid,tv_stamp,tv_content;
        ImageView im_portrait;
    }
}
